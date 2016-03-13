package de.uka.ipd.sdq.beagle.core.facade;

import de.uka.ipd.sdq.beagle.core.AnalysisController;
import de.uka.ipd.sdq.beagle.core.AnalysisState;
import de.uka.ipd.sdq.beagle.core.Blackboard;
import de.uka.ipd.sdq.beagle.core.LaunchConfiguration;
import de.uka.ipd.sdq.beagle.core.ProjectInformation;
import de.uka.ipd.sdq.beagle.core.analysis.MeasurementResultAnalyserContributionsHandler;
import de.uka.ipd.sdq.beagle.core.analysis.ProposedExpressionAnalyserContributionsHandler;
import de.uka.ipd.sdq.beagle.core.failurehandling.FailureHandler;
import de.uka.ipd.sdq.beagle.core.failurehandling.FailureReport;
import de.uka.ipd.sdq.beagle.core.judge.AbstractionAndPrecisionFitnessFunction;
import de.uka.ipd.sdq.beagle.core.measurement.MeasurementToolContributionsHandler;
import de.uka.ipd.sdq.beagle.core.pcmconnection.PcmRepositoryBlackboardFactoryAdder;
import de.uka.ipd.sdq.beagle.core.pcmconnection.PcmRepositoryWriter;
import de.uka.ipd.sdq.beagle.core.pcmsourcestatementlink.PcmSourceStatementLinkReader;
import de.uka.ipd.sdq.beagle.core.pcmsourcestatementlink.PcmSourceStatementLinkRepository;

import org.apache.commons.lang3.Validate;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;

import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Set;

/**
 * Controls the execution of the Beagle Analysis. {@code BeagleController} can start,
 * pause, continue, and abort an Analysis. The controller has to be initialised through
 * {@link #initialise()} before it can be used.
 *
 * @author Christoph Michelbach
 * @author Roman Langrehr
 */
public class BeagleController {

	/**
	 * The failure handler for this class.
	 */
	private static final FailureHandler FAILURE_HANDLER = FailureHandler.getHandler(BeagleConfiguration.class);

	/**
	 * The analysis controller used for this project.
	 */
	private AnalysisController analysisController;

	/**
	 * The final {@link BeagleConfiguration} for this analysis.
	 */
	private final BeagleConfiguration beagleConfiguration;

	/**
	 * The {@link Blackboard} for this analysis.
	 */
	private Blackboard blackboard;

	/**
	 * Will be set to {@code true} after {@link #initialise()} has been run.
	 */
	private boolean inited;

	/**
	 * Will be set to {@code true} after {@link #startAnalysis()} has been run.
	 */
	private boolean started;

	/**
	 * Constructs a new {@code BeagleController} with the given
	 * {@link BeagleConfiguration}.
	 *
	 * @param beagleConfiguration The {@link BeagleConfiguration} this BeagleController
	 *            has permanently. It cannot be changed.
	 *            {@link BeagleConfiguration#finalise()} must have been called.
	 */
	public BeagleController(final BeagleConfiguration beagleConfiguration) {
		Validate.isTrue(beagleConfiguration.isFinal(),
			"The BeagleConfiguration must be final for the BeagleController");
		this.beagleConfiguration = beagleConfiguration;

	}

	/**
	 * Initialises this controller, making it ready to start the analysis.
	 */
	public void initialise() {
		final BlackboardCreator blackboardFactory = new BlackboardCreator();
		final SourceCodeFileProvider sourceCodeFileProvider =
			new JdtProjectSourceCodeFileProvider(this.beagleConfiguration.getJavaProject());

		final PcmSourceStatementLinkReader linkReader =
			new PcmSourceStatementLinkReader(this.beagleConfiguration.getSourceStatementLinkFile());
		final PcmSourceStatementLinkRepository linkRepository = linkReader.getPcmSourceLinkRepository();

		if (this.beagleConfiguration.getElements() == null) {
			new PcmRepositoryBlackboardFactoryAdder(this.beagleConfiguration.getRepositoryFile(),
				sourceCodeFileProvider, linkRepository).getBlackboardForAllElements(blackboardFactory);
		} else {
			new PcmRepositoryBlackboardFactoryAdder(this.beagleConfiguration.getRepositoryFile(),
				sourceCodeFileProvider, linkRepository).getBlackboardForIds(this.beagleConfiguration.getElements(),
					blackboardFactory);
		}

		final Charset charset = this.readCharset(this.beagleConfiguration.getJavaProject());
		final String buildPath =
			new JdtProjectClasspathExtractor(this.beagleConfiguration.getJavaProject()).getClasspath();

		final Set<LaunchConfiguration> launchConfigurations = this.beagleConfiguration.getLaunchConfigurations();
		blackboardFactory.setProjectInformation(new ProjectInformation(this.beagleConfiguration.getTimeout(),
			sourceCodeFileProvider, buildPath, charset, launchConfigurations));

		blackboardFactory.setFitnessFunction(new AbstractionAndPrecisionFitnessFunction());
		this.blackboard = blackboardFactory.createBlackboard();
		this.analysisController = new AnalysisController(this.blackboard,
			new HashSet<>(new MeasurementToolContributionsHandler().getAvailableMeasurmentTools()),
			new HashSet<>(new MeasurementResultAnalyserContributionsHandler().getAvailableMeasurmentResultAnalysers()),
			new HashSet<>(
				new ProposedExpressionAnalyserContributionsHandler().getAvailableProposedExpressionAnalysers()));
		this.inited = true;
	}

	/**
	 * Starts the analysis. This method can only be used once per {@link BeagleController}
	 * . Further calls will be ignored.
	 *
	 * @throws IllegalStateException If {@link #initialise()} has not been called yet.
	 */
	public void startAnalysis() {
		Validate.validState(this.inited, "The Beagle Controller has not yet been initialised!");

		if (!this.started) {
			this.started = true;
			this.analysisController.performAnalysis();
			new PcmRepositoryWriter(this.blackboard).writeTo(this.beagleConfiguration.getRepositoryFile());
		}
	}

	/**
	 * Reads the default charset of an {@link IJavaProject}.
	 *
	 * @param javaProject the {@link IJavaProject} to read from.
	 * @return the charset specified by {@link IProject#getDefaultCharset()}
	 */
	private Charset readCharset(final IJavaProject javaProject) {
		try {
			return Charset.forName(javaProject.getProject().getDefaultCharset());
		} catch (final CoreException coreException) {
			final FailureReport<Charset> failure = new FailureReport<Charset>().cause(coreException)
				.continueWith(Charset::defaultCharset)
				.retryWith(() -> this.readCharset(javaProject));
			return FAILURE_HANDLER.handle(failure);
		}
	}

	/**
	 * Pauses the analysis. The call will be directed to the {@link AnalysisController}
	 * started by this controller.
	 *
	 * @throws IllegalStateException If the analysis has not been started yet or this
	 *             controller has not been initialised yet.
	 */
	public void pauseAnalysis() {
		Validate.validState(this.inited, "The Beagle Controller has not yet been initialised!");
		Validate.validState(this.started, "The analysis has not yet been started!");
		this.analysisController.setAnalysisState(AnalysisState.ENDING);
	}

	/**
	 * Continues the analysis if it is paused. The call will be directed to the
	 * {@link AnalysisController} started by this controller.
	 *
	 * @throws IllegalStateException If the analysis has not been started yet or this
	 *             controller has not been initialised yet.
	 */
	public void continueAnalysis() {
		Validate.validState(this.inited, "The Beagle Controller has not yet been initialised!");
		Validate.validState(this.started, "The analysis has not yet been started!");
		this.analysisController.setAnalysisState(AnalysisState.RUNNING);
	}

	/**
	 * Aborts the analysis. The call will be directed to the {@link AnalysisController}
	 * started by this controller.
	 *
	 * @throws IllegalStateException If the analysis has not been started yet or this
	 *             controller has not been initialised yet.
	 */
	public void abortAnalysis() {
		Validate.validState(this.inited, "The Beagle Controller has not yet been initialised!");
		Validate.validState(this.started, "The analysis has not yet been started!");
		this.analysisController.setAnalysisState(AnalysisState.ABORTING);
	}
}
