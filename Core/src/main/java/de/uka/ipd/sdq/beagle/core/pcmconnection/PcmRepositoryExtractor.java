package de.uka.ipd.sdq.beagle.core.pcmconnection;

import de.uka.ipd.sdq.beagle.core.Blackboard;
import de.uka.ipd.sdq.beagle.core.CodeSection;
import de.uka.ipd.sdq.beagle.core.ExternalCallParameter;
import de.uka.ipd.sdq.beagle.core.ResourceDemandingInternalAction;
import de.uka.ipd.sdq.beagle.core.SeffBranch;
import de.uka.ipd.sdq.beagle.core.SeffLoop;
import de.uka.ipd.sdq.beagle.core.judge.EvaluableExpressionFitnessFunction;

import de.uka.ipd.sdq.identifier.Identifier;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.palladiosimulator.pcm.repository.RepositoryComponent;
import org.palladiosimulator.pcm.repository.impl.BasicComponentImpl;
import org.palladiosimulator.pcm.repository.impl.RepositoryImpl;
import org.palladiosimulator.pcm.seff.ServiceEffectSpecification;
import org.palladiosimulator.pcm.seff.impl.ResourceDemandingSEFFImpl;

import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Extracting a given Pcm repository. Offering 2 methods:
 * {@link #getBlackboardForAllElements(RepositoryImpl)} and
 * {@link #getBlackboardForIds(RepositoryImpl, Collection)}
 * 
 * @author Ansgar Spiegler
 */
public class PcmRepositoryExtractor {

	/**
	 * Temporary storage for all extracted {@link SeffLoop SeffLoops} that should be
	 * written on the {@link Blackboard}.
	 */
	private Set<SeffLoop> seffLoopSet;

	/**
	 * Temporary storage for all extracted {@link SeffBranch SeffBranches} that should be
	 * written on the {@link Blackboard}.
	 */
	private Set<SeffBranch> seffBranchSet;

	/**
	 * Temporary storage for all extracted {@link ResourceDemandingInternalAction rdSeffs}
	 * that should be written on the {@link Blackboard}.
	 */
	private Set<ResourceDemandingInternalAction> rdiaSet;

	/**
	 * Temporary storage for all extracted {@link ExternalCallParameter} that should be
	 * written on the {@link Blackboard}.
	 */
	private Set<ExternalCallParameter> externalCallParameterSet;

	/**
	 * Helping class instance for extracting, creating and adding SEFF elements to Sets.
	 */
	private PcmRepositorySeffExtractor pcmSeffExtractor;

	/**
	 * The fitnessFucntion to initialize the blackboard with.
	 */
	private final EvaluableExpressionFitnessFunction fitnessFunction;

	/**
	 * Constructor needs EvaluableExpressionFitnessFunction.
	 *
	 * @param fitnessFunction The fitnessFucntion to initialize the blackboard with
	 */
	public PcmRepositoryExtractor(final EvaluableExpressionFitnessFunction fitnessFunction) {
		this.fitnessFunction = fitnessFunction;
	}

	/**
	 * Builds a new blackboard and writes the Beagle objects representing all found
	 * <em>PCM elements</em> to it. Only <em>PCM elements</em> that fulfil the
	 * restrictions described in the class description will be written.
	 *
	 * @param repository The repository to look up.
	 * @return A new blackboard having all translated <em>PCM elements</em> written on it.
	 *         Will never be {@code null}.
	 * @throws FileNotFoundException If the file for creating {@link CodeSection} was not
	 *             found at the specified path in the repository-file.
	 */
	public Blackboard getBlackboardForAllElements(final RepositoryImpl repository) throws FileNotFoundException {
		this.seffLoopSet = new HashSet<SeffLoop>();
		this.seffBranchSet = new HashSet<SeffBranch>();
		this.rdiaSet = new HashSet<ResourceDemandingInternalAction>();
		this.externalCallParameterSet = new HashSet<ExternalCallParameter>();
		this.pcmSeffExtractor = new PcmRepositorySeffExtractor(this.seffLoopSet, this.seffBranchSet, this.rdiaSet,
			this.externalCallParameterSet);

		this.scanRepository(repository);
		return new Blackboard(this.rdiaSet, this.seffBranchSet, this.seffLoopSet, this.externalCallParameterSet,
			this.fitnessFunction);
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
	 * @param repository The repository to look up.
	 * @param identifiers Identifiers of elements in the repository files that shall be
	 *            written to the Blackboard.
	 * @return A new blackboard having all selected and translated <em>PCM elements</em>
	 *         written on it. Will never be {@code null}.
	 * @throws FileNotFoundException If the file for creating {@link CodeSection} was not
	 *             found at the specified path in the repository-file.
	 */
	public Blackboard getBlackboardForIds(final RepositoryImpl repository, final Collection<String> identifiers)
		throws FileNotFoundException {

		final Set<EObject> setOfIdentifiedObjects = new HashSet<EObject>();

		this.seffLoopSet = new HashSet<SeffLoop>();
		this.seffBranchSet = new HashSet<SeffBranch>();
		this.rdiaSet = new HashSet<ResourceDemandingInternalAction>();
		this.externalCallParameterSet = new HashSet<ExternalCallParameter>();
		this.pcmSeffExtractor = new PcmRepositorySeffExtractor(this.seffLoopSet, this.seffBranchSet, this.rdiaSet,
			this.externalCallParameterSet);

		if (identifiers.contains(repository.getId())) {
			this.scanRepository(repository);
			return new Blackboard(this.rdiaSet, this.seffBranchSet, this.seffLoopSet, this.externalCallParameterSet,
				this.fitnessFunction);
		}

		// Look up for each Repository-object ID if its found in the
		// identifiers-Collection
		// If so, the Object is added to a Set named "setOfIdentifiedObjects"
		final TreeIterator<EObject> contentIterator = repository.eAllContents();
		while (contentIterator.hasNext()) {
			final EObject content = contentIterator.next();

			Identifier contentIdentifier = null;
			if (content instanceof Identifier) {
				contentIdentifier = (Identifier) content;
				if (identifiers.contains(contentIdentifier.getId())) {
					setOfIdentifiedObjects.add(content);
				}
			}
		}

		// Find for every "useful" object its content and extract it into the given Sets
		for (final EObject identifiedObject : setOfIdentifiedObjects) {
			if (identifiedObject.getClass() == BasicComponentImpl.class) {
				this.extractBasicComponentAndAddContentsToSet((BasicComponentImpl) identifiedObject);
			} else if (identifiedObject.getClass() == ResourceDemandingSEFFImpl.class) {
				this.extractResourceDemandingSEFFImplAndAddContentsToSet((ResourceDemandingSEFFImpl) identifiedObject);
			} else {
				this.pcmSeffExtractor.extractBehaviourAndAddToSet(identifiedObject);
			}
		}

		// Add the sets to the blackboard and return
		return new Blackboard(this.rdiaSet, this.seffBranchSet, this.seffLoopSet, this.externalCallParameterSet,
			this.fitnessFunction);
	}

	/**
	 * This method takes the whole {@link PcmRepositoryBlackboardFactory#repository} and
	 * extracts all needed content into the storing sets.
	 *
	 * @param repositoryToScan The repository to read from.
	 * @throws FileNotFoundException If the file for creating {@link CodeSection} was not
	 *             found at the specified path in the repository-file.
	 */
	private void scanRepository(final RepositoryImpl repositoryToScan) throws FileNotFoundException {
		final EList<RepositoryComponent> componentList = repositoryToScan.getComponents__Repository();
		for (final RepositoryComponent component : componentList) {
			if (component.getClass() == BasicComponentImpl.class) {
				this.extractBasicComponentAndAddContentsToSet((BasicComponentImpl) component);
			}
		}
	}

	/**
	 * BasicComponent extracting method. Looking for all included SEFFs. Recursive calls
	 * to methods that save all needed SEFF-elements into the sets.
	 *
	 * @param basicComponent Component of the Repository.
	 * @throws FileNotFoundException If the file for creating {@link CodeSection} was not
	 *             found at the specified path in the repository-file.
	 */
	private void extractBasicComponentAndAddContentsToSet(final BasicComponentImpl basicComponent)
		throws FileNotFoundException {
		final EList<ServiceEffectSpecification> seffList =
			basicComponent.getServiceEffectSpecifications__BasicComponent();
		for (final ServiceEffectSpecification seff : seffList) {
			if (seff.getClass() == ResourceDemandingSEFFImpl.class) {
				this.extractResourceDemandingSEFFImplAndAddContentsToSet((ResourceDemandingSEFFImpl) seff);
			}
		}
	}

	/**
	 * ResourceDemandingSEFF extracting method. Looking for all including Contents.
	 * Recursive calls to methods that save all needed SEFF-elements into the sets.
	 *
	 * @param rdSeff rdSeff of the Repository.
	 * @throws FileNotFoundException If the file for creating {@link CodeSection} was not
	 *             found at the specified path in the repository-file.
	 */
	private void extractResourceDemandingSEFFImplAndAddContentsToSet(final ResourceDemandingSEFFImpl rdSeff)
		throws FileNotFoundException {
		final EList<EObject> rdSeffContentList = rdSeff.eContents();
		for (final EObject rdSeffContent : rdSeffContentList) {
			this.pcmSeffExtractor.extractBehaviourAndAddToSet(rdSeffContent);
		}
	}

}
