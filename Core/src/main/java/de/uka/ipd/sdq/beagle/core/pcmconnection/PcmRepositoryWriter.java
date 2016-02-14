package de.uka.ipd.sdq.beagle.core.pcmconnection;

import de.uka.ipd.sdq.beagle.core.Blackboard;
import de.uka.ipd.sdq.beagle.core.failurehandling.FailureHandler;
import de.uka.ipd.sdq.beagle.core.failurehandling.FailureReport;

import org.palladiosimulator.pcm.repository.impl.RepositoryImpl;

import java.io.File;
import java.io.IOException;

/**
 * Writes Beagle’s findings back to a PCM repository. This class is meant to be used for
 * Blackboard instances created by {@link PcmRepositoryBlackboardFactory} and may not be
 * useable with other blackboards.
 *
 * @author Joshua Gleitze
 * @author Ansgar Spiegler
 */
public class PcmRepositoryWriter {

	/**
	 * The FailureHandler for this class.
	 */
	private static final FailureHandler FAILURE_HANDLER = FailureHandler.getHandler("BlackboardStorer");
	
	/**
	 * Blackboard to get Mapping from.
	 */
	private final Blackboard blackboard;

	/**
	 * The Mappings of Seffs to Ids.
	 */
	private final PcmBeagleMappings pcmMappings;

	/**
	 * FileLoader to load a repository for a given file.
	 */
	private final PcmRepositoryFileLoader fileLoader;

	/**
	 * The annotator that writes all final EvaluableExpression of a given Blackboard to
	 * the Repository.
	 */
	private final PcmRepositoryWriterAnnotator annotator;



	/**
	 * Creates a writer to write the results written on {@code blackboard} back to a PCM
	 * repository.
	 *
	 * @param blackboard The blackboard containing results.
	 */
	public PcmRepositoryWriter(final Blackboard blackboard) {
		this.blackboard = blackboard;
		this.pcmMappings = this.blackboard.readFor(PcmRepositoryBlackboardFactory.class);
		this.fileLoader = new PcmRepositoryFileLoader();
		this.annotator = new PcmRepositoryWriterAnnotator(blackboard, this.pcmMappings);
	}

	/**
	 * Writes the Beagle’s findings to the {@code repositoryFile}. For each
	 * {@linkplain ResourceDemandingInternalAction}, {@linkplain SeffBranch} and
	 * {@linkplain SeffLoop}, the method will look up the identifier of the element in the
	 * source repository file the object was created for. If {@code repositoryFile}
	 * contains an element with this identifier that is of the appropriate type, the
	 * object’s result will appropriately written to {@code repositoryFile}. Nothing will
	 * be written otherwise.
	 * 
	 * 
	 * <p>If the element Id with final EvaluableExpression from the Blackboard can not be
	 * found in the repository file.
	 * 
	 * <p>If the Blackboard elements (e.g. SeffBranch) from the elements with final
	 * EvaluableExpression from the Blackboard do not Correspond to the repository PCM
	 * elements (e.g. BranchAction).
	 *
	 * @param repositoryFile A PCM repository file to write Beagle’s results to. It should
	 *            use the same identifiers for elements as the repository file Beagle’s
	 *            blackboard was initially created for.
	 */
	public void writeTo(final File repositoryFile) {
		final RepositoryImpl repository = this.fileLoader.loadRepositoryFromFile(repositoryFile);

		this.annotator.annotateAll(repository);
		this.saveRepository(repository, repositoryFile);
	}

	/**
	 * Method should be called after annotating changes on the {@link RepositoryImpl}. It
	 * will store the repository at the given File-path.
	 *
	 * @param repository modified Repository to store
	 * @param repositoryFile storing repository as this file
	 */
	private void saveRepository(final RepositoryImpl repository, final File repositoryFile) {
		try {
			EmfHelper.saveToXMIFile(repository, repositoryFile.getAbsolutePath());
		} catch (final IOException ioException) {
			final FailureReport<Void> failure = new FailureReport<Void>()
				.message("The modified repository can not be saved to %s!", repositoryFile.getAbsolutePath())
				.cause(ioException)
				.recoverable()
				.retryWith(() -> this.saveRepository(repository, repositoryFile));
			FAILURE_HANDLER.handle(failure);
		}
	}

}
