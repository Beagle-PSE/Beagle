package de.uka.ipd.sdq.beagle.core.facade;

import de.uka.ipd.sdq.beagle.core.AnalysisController;
import de.uka.ipd.sdq.beagle.core.BlackboardCreator;
import de.uka.ipd.sdq.beagle.core.EclipseLaunchConfiguration;
import de.uka.ipd.sdq.beagle.core.LaunchConfiguration;
import de.uka.ipd.sdq.beagle.core.ProjectInformation;
import de.uka.ipd.sdq.beagle.core.analysis.MeasurementResultAnalyserContributionsHandler;
import de.uka.ipd.sdq.beagle.core.analysis.ProposedExpressionAnalyserContributionsHandler;
import de.uka.ipd.sdq.beagle.core.failurehandling.FailureHandler;
import de.uka.ipd.sdq.beagle.core.failurehandling.FailureReport;
import de.uka.ipd.sdq.beagle.core.judge.AbstractionAndPrecisionFitnessFunction;
import de.uka.ipd.sdq.beagle.core.measurement.MeasurementToolContributionsHandler;
import de.uka.ipd.sdq.beagle.core.pcmconnection.PcmRepositoryBlackboardFactoryAdder;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.palladiosimulator.pcm.core.entity.Entity;

import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Controls the execution of the Beagle Analysis. {@code BeagleController} can start,
 * pause, continue, and abort an Analysis.
 *
 * @author Christoph Michelbach
 * @author Roman Langrehr
 */
public class BeagleController {

	/**
	 * The analysis controller used for this project.
	 */
	private final AnalysisController analysisController;

	/**
	 * Constructs a new {@code BeagleController} with the given
	 * {@link BeagleConfiguration}.
	 *
	 * @param beagleConfiguration The {@link BeagleConfiguration} this BeagleController
	 *            has permanently. It cannot be changed.
	 */
	public BeagleController(final BeagleConfiguration beagleConfiguration) {
		final BlackboardCreator blackboardFactory = new BlackboardCreator();
		final SourceCodeFileProvider sourceCodeFileProvider =
			new JdtProjectSourceCodeFileProvider(beagleConfiguration.getJavaProject());
		if (beagleConfiguration.getElements() == null) {
			try {
				new PcmRepositoryBlackboardFactoryAdder(beagleConfiguration.getRepositoryFile(), sourceCodeFileProvider)
					.getBlackboardForAllElements(blackboardFactory);
			} catch (final FileNotFoundException fileNotFoundException) {
				FailureHandler.getHandler(this.getClass()).handle(new FailureReport<>().cause(fileNotFoundException));
			}
		} else {
			try {
				new PcmRepositoryBlackboardFactoryAdder(beagleConfiguration.getRepositoryFile(), sourceCodeFileProvider)
					.getBlackboardForIds(this.entitysToStrings(beagleConfiguration.getElements()), blackboardFactory);
			} catch (final FileNotFoundException fileNotFoundException) {
				FailureHandler.getHandler(this.getClass()).handle(new FailureReport<>().cause(fileNotFoundException));
			}
		}
		Charset charset = null;
		try {
			charset = Charset.forName(beagleConfiguration.getJavaProject().getProject().getDefaultCharset());
		} catch (final CoreException coreException) {
			FailureHandler.getHandler(this.getClass()).handle(new FailureReport<>().cause(coreException));
		}
		final String buildPath = new JdtProjectClasspathExtractor(beagleConfiguration.getJavaProject()).getClasspath();
		final Set<ILaunchConfiguration> iLaunchConfigurations =
			new LauchConfigurationProvider(beagleConfiguration.getJavaProject())
				.getAllSuitableJUnitLaunchConfigurations();
		final Set<LaunchConfiguration> launchConfigurations = new HashSet<>();
		for (final ILaunchConfiguration iLaunchConfiguration : iLaunchConfigurations) {
			launchConfigurations
				.add(new EclipseLaunchConfiguration(iLaunchConfiguration, beagleConfiguration.getJavaProject()));

			blackboardFactory.setProjectInformation(new ProjectInformation(beagleConfiguration.getTimeout(),
				sourceCodeFileProvider, buildPath, charset, launchConfigurations));
		}
		blackboardFactory.setFitnessFunction(new AbstractionAndPrecisionFitnessFunction());
		this.analysisController = new AnalysisController(blackboardFactory.createBlackboard(),
			new HashSet<>(new MeasurementToolContributionsHandler().getAvailableMeasurmentTools()),
			new HashSet<>(new MeasurementResultAnalyserContributionsHandler().getAvailableMeasurmentResultAnalysers()),
			new HashSet<>(
				new ProposedExpressionAnalyserContributionsHandler().getAvailableProposedExpressionAnalysers()));
	}

	/**
	 * Starts the analysis. This method can only be used once per {@link BeagleController}
	 * . Further calls will be ignored.
	 *
	 */
	public void startAnalysis() {
		this.analysisController.performAnalysis();
	}

	/**
	 * Pauses the analysis. If the analysis is already paused, calls to this method are
	 * ignored.
	 *
	 */
	public void pauseAnalysis() {

	}

	/**
	 * Continues the analysis if it is paused. If it's running, calls to this method are
	 * ignored.
	 *
	 */
	public void continueAnalysis() {

	}

	/**
	 * Aborts the analysis. If it already is aborted, calls to this method are ignored.
	 * Aborting the analysis is also possible if it wasn't started yet. In this case it
	 * will never be possible to start it.
	 *
	 */
	public void abortAnalysis() {

	}

	/**
	 * Converts {@linkplain Entity Entities} to {@linkplain String Strings}.
	 *
	 * @param entities The {@linkplain Entity Entities} to convert.
	 * @return The ids of the {@code entities}.
	 */
	private List<String> entitysToStrings(final List<Entity> entities) {
		final List<String> strings = new ArrayList<>();
		for (final Entity entity : entities) {
			strings.add(entity.getId());
		}
		return strings;
	}
}
