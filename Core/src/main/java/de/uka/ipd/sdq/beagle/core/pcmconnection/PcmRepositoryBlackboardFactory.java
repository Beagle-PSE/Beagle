package de.uka.ipd.sdq.beagle.core.pcmconnection;
/**
 * ATTENTION: Test coverage check turned off. Remove this comments block when implementing
 * this class!
 *
 * <p>COVERAGE:OFF
 */

import de.uka.ipd.sdq.beagle.core.Blackboard;
import de.uka.ipd.sdq.beagle.core.BlackboardStorer;
import de.uka.ipd.sdq.beagle.core.judge.EvaluableExpressionFitnessFunction;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.palladiosimulator.pcm.repository.RepositoryFactory;
import org.palladiosimulator.pcm.repository.impl.RepositoryImpl;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.LinkedList;

/**
 * Creates {@link Blackboard} instances suitable to analyse elements from a PCM
 * repository. The factory searches the repository for resource demanding internal
 * actions, SEFF loops and SEFF branches (hereafter called <em>PCM elements</em>). It
 * translates these into the corresponding Beagle Object and puts them on a new
 * Blackboard. To be translated, <em>PCM elements</em> must have a binding to sections in
 * source code files expressed in the source code decorator model.
 *
 * @author Joshua Gleitze
 * @author Ansgar Spiegler
 */
public class PcmRepositoryBlackboardFactory implements BlackboardStorer<PcmBeagleMappings> {

	/**
	 * The repository where this class should extract all its information from.
	 */
	private final RepositoryImpl repository;

	/**
	 * Instance of helper class, extracting a given repository.
	 */
	private PcmRepositoryExtractor pcmExtractor;

	/**
	 * The fitnessFucntion to initialize the blackboard with.
	 */
	private final EvaluableExpressionFitnessFunction fitnessFunction;

	/**
	 * Creates a factory that will search the provided PCM files for <em>PCM
	 * elements</em>.
	 *
	 * @param repositoryFileName PCM repository to load from.
	 * @param fitnessFunction The fitnessFunction the blackboard should be initialized
	 *            with
	 * @throws IllegalArgumentException If input parameter does not represent a valid
	 *             repository file or if repositoryFileName can not be resolved to a valid
	 *             file
	 */
	public PcmRepositoryBlackboardFactory(final String repositoryFileName,
		final EvaluableExpressionFitnessFunction fitnessFunction) {

		if (fitnessFunction == null || repositoryFileName == null) {
			throw new NullPointerException();
		}

		this.fitnessFunction = fitnessFunction;

		final File test = new File(repositoryFileName);
		if (!test.isFile()) {
			throw new IllegalArgumentException("No file found at: " + repositoryFileName);
		}

		RepositoryFactory.eINSTANCE.createRepository();
		// Not sure if this final declaration could lead to a problem.
		final EPackage ePackage = RepositoryFactory.eINSTANCE.getEPackage();

		final EObject eObject = EMFHelper.loadFromXMIFile(repositoryFileName, ePackage);
		if (!(eObject.getClass() == RepositoryImpl.class)) {
			throw new IllegalArgumentException();
		}
		this.repository = (RepositoryImpl) eObject;
	}

	/**
	 * Creates a factory that will search the provided PCM file for <em>PCM elements</em>.
	 *
	 * @param pcmRepositoryFiles PCM repository file.
	 * @param fitnessFunction The fitnessFunction the blackboard should be initialized
	 *            with
	 * @throws FileNotFoundException If repositoryFileName can not be resolved to a valid
	 *             file
	 */
	public PcmRepositoryBlackboardFactory(final File pcmRepositoryFiles,
		final EvaluableExpressionFitnessFunction fitnessFunction) throws FileNotFoundException {
		this(pcmRepositoryFiles.getAbsolutePath(), fitnessFunction);
	}

	/**
	 * Builds a new blackboard and writes the Beagle objects representing all found
	 * <em>PCM elements</em> to it. Only <em>PCM elements</em> that fulfil the
	 * restrictions described in the class description will be written.
	 *
	 * @return A new blackboard having all translated <em>PCM elements</em> written on it.
	 *         Will never be {@code null}.
	 */
	public Blackboard getBlackboardForAllElements() {
		this.pcmExtractor = new PcmRepositoryExtractor(this.fitnessFunction);
		return this.pcmExtractor.getBlackboardForAllElements(this.repository);

	}

	/**
	 * Builds a new blackboard and writes the Beagle objects representing <em>PCM
	 * elements</em> related to {@code ids} to it. Only <em>PCM elements</em> that fulfil
	 * the restrictions described in the class description will be written.
	 *
	 * <p>For any provided ID {@code id}, <em>PCM elements</em> will be selected to be
	 * written on the blackboard as follows:
	 *
	 * <ul>
	 *
	 * <li>If {@code id} describes a component, all <em>PCM elements</em> that are part of
	 * the component’s service effect specification will be selected. Nested components
	 * will be inspected recursively.
	 *
	 * <li>If {@code id} describes a repository, all contained components will be
	 * inspected as if their ID had been provided.
	 *
	 * <li>If {@code id} describes a <em>PCM element</em>, it will be selected.
	 *
	 * <li>Any different ID will be silently ignored.
	 *
	 * </ul>
	 *
	 * @param identifiers Identifiers of elements in the repository files that shall be
	 *            written to the Blackboard.
	 * @return A new blackboard having all selected and translated <em>PCM elements</em>
	 *         written on it. Will never be {@code null}.
	 */
	public Blackboard getBlackboardForIds(final Collection<String> identifiers) {
		this.pcmExtractor = new PcmRepositoryExtractor(this.fitnessFunction);
		return this.pcmExtractor.getBlackboardForIds(this.repository, identifiers);

	}

	/**
	 * Builds a new blackboard and writes the Beagle objects representing <em>PCM
	 * elements</em> related to {@code ids} to it. Only <em>PCM elements</em> that fulfil
	 * the restrictions described in the class description will be written.
	 *
	 * <p>For any provided ID {@code id}, <em>PCM elements</em> will be selected to be
	 * written on the blackboard as follows:
	 *
	 * <ul>
	 *
	 * <li>If {@code id} describes a component, all <em>PCM elements</em> that are part of
	 * the component’s service effect specification will be selected. Nested components
	 * will be inspected recursively.
	 *
	 * <li>If {@code id} describes a repository, all contained components will be
	 * inspected as if their ID had been provided.
	 *
	 * <li>If {@code id} describes a <em>PCM element</em>, it will be selected.
	 *
	 * <li>Any other ID will be silently ignored.
	 *
	 * </ul>
	 *
	 * @param identifiers Identifiers of elements in the repository files that shall be
	 *            written to the Blackboard.
	 * @return A new blackboard having all selected and translated <em>PCM elements</em>
	 *         written on it. Will never be {@code null}.
	 */
	public Blackboard getBlackboardForIds(final String... identifiers) {
		final Collection<String> identifierCollection = new LinkedList<String>();
		for (final String identifier : identifiers) {
			identifierCollection.add(identifier);
		}
		return this.getBlackboardForIds(identifierCollection);
	}

}
