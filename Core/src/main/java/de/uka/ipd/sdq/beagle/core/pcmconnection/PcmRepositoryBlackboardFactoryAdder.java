package de.uka.ipd.sdq.beagle.core.pcmconnection;

import de.uka.ipd.sdq.beagle.core.Blackboard;
import de.uka.ipd.sdq.beagle.core.BlackboardCreator;
import de.uka.ipd.sdq.beagle.core.BlackboardStorer;

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
 * @author Roman Langrehr
 */
public class PcmRepositoryBlackboardFactoryAdder implements BlackboardStorer<PcmBeagleMappings> {

	/**
	 * The repository where this class should extract all its information from.
	 */
	private final RepositoryImpl repository;

	/**
	 * Instance of helper class, extracting a given repository.
	 */
	private PcmRepositoryExtractor pcmExtractor;

	/**
	 * Creates a factory that will search the provided PCM files for <em>PCM
	 * elements</em>.
	 *
	 * @param repositoryFileName PCM repository to load from.
	 * @throws IllegalArgumentException If input parameter does not represent a valid
	 *             repository file or if repositoryFileName can not be resolved to a valid
	 *             file
	 */
	public PcmRepositoryBlackboardFactoryAdder(final String repositoryFileName) {

		final File test = new File(repositoryFileName);
		if (!test.exists() || !test.isFile()) {
			throw new IllegalArgumentException("No file found at: " + repositoryFileName);
		}

		RepositoryFactory.eINSTANCE.createRepository();
		// Not sure if this final declaration could lead to a problem.
		final EPackage ePackage = RepositoryFactory.eINSTANCE.getEPackage();

		final EObject eObject = EmfHelper.loadFromXMIFile(repositoryFileName, ePackage);
		if (!(eObject.getClass() == RepositoryImpl.class)) {
			throw new IllegalArgumentException();
		}
		this.repository = (RepositoryImpl) eObject;
	}

	/**
	 * Creates a factory that will search the provided PCM file for <em>PCM elements</em>.
	 *
	 * @param pcmRepositoryFiles PCM repository file.
	 * @throws FileNotFoundException If repositoryFileName can not be resolved to a valid
	 *             file
	 */
	public PcmRepositoryBlackboardFactoryAdder(final File pcmRepositoryFiles) throws FileNotFoundException {
		this(pcmRepositoryFiles.getAbsolutePath());
	}

	/**
	 * Builds a new blackboard and writes the Beagle objects representing all found
	 * <em>PCM elements</em> to it. Only <em>PCM elements</em> that fulfil the
	 * restrictions described in the class description will be written.
	 *
	 * @param blackboardFactory all translated <em>PCM elements</em> will be written on
	 *            it. The rdias, seffLoops, seffBranches and externalCallPAramerts will
	 *            never be {@code null} afterwards.
	 */
	public void getBlackboardForAllElements(final BlackboardCreator blackboardFactory) {
		this.pcmExtractor = new PcmRepositoryExtractor();
		this.pcmExtractor.getBlackboardForAllElements(this.repository, blackboardFactory);

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
	 * @param blackboardFactory all translated <em>PCM elements</em> will be written on
	 *            it. The rdias, seffLoops, seffBranches and externalCallPAramerts will
	 *            never be {@code null} afterwards.
	 */
	public void getBlackboardForIds(final Collection<String> identifiers, final BlackboardCreator blackboardFactory) {
		if (identifiers == null) {
			throw new NullPointerException();
		}
		for (final String identifier : identifiers) {
			if (identifier == null) {
				throw new NullPointerException();
			}
		}
		this.pcmExtractor = new PcmRepositoryExtractor();
		this.pcmExtractor.getBlackboardForIds(this.repository, identifiers, blackboardFactory);

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
	 * @param blackboardFactory all translated <em>PCM elements</em> will be written on
	 *            it. The rdias, seffLoops, seffBranches and externalCallPAramerts will
	 *            never be {@code null} afterwards.
	 */
	public void getBlackboardForIds(final BlackboardCreator blackboardFactory, final String... identifiers) {
		if (identifiers == null) {
			throw new NullPointerException();
		}
		for (final String identifier : identifiers) {
			if (identifier == null) {
				throw new NullPointerException();
			}
		}
		final Collection<String> identifierCollection = new LinkedList<String>();
		for (final String identifier : identifiers) {
			identifierCollection.add(identifier);
		}
		this.getBlackboardForIds(identifierCollection, blackboardFactory);
	}

}
