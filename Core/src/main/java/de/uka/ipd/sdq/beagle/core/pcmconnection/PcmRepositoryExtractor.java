package de.uka.ipd.sdq.beagle.core.pcmconnection;

import de.uka.ipd.sdq.beagle.core.Blackboard;
import de.uka.ipd.sdq.beagle.core.BlackboardCreator;
import de.uka.ipd.sdq.beagle.core.ExternalCallParameter;
import de.uka.ipd.sdq.beagle.core.ResourceDemandingInternalAction;
import de.uka.ipd.sdq.beagle.core.SeffBranch;
import de.uka.ipd.sdq.beagle.core.SeffLoop;
import de.uka.ipd.sdq.beagle.core.facade.SourceCodeFileProvider;

import de.uka.ipd.sdq.identifier.Identifier;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.palladiosimulator.pcm.repository.RepositoryComponent;
import org.palladiosimulator.pcm.repository.impl.BasicComponentImpl;
import org.palladiosimulator.pcm.repository.impl.RepositoryImpl;
import org.palladiosimulator.pcm.seff.ServiceEffectSpecification;
import org.palladiosimulator.pcm.seff.impl.ResourceDemandingSEFFImpl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Extracting a given Pcm repository. Offering 2 methods:
 * {@link #getBlackboardForAllElements(RepositoryImpl, BlackboardCreator)} and
 * {@link #getBlackboardForIds(RepositoryImpl, Collection, BlackboardCreator)}
 *
 * @author Ansgar Spiegler
 * @author Roman Langrehr
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
	 * The {@link SourceCodeFileProvider} for the project under analysis.
	 */
	private SourceCodeFileProvider sourceCodeFileProvider;

	/**
	 * Creates a new name parser for a specific project to analyse.
	 *
	 * @param sourceCodeFileProvider The {@link SourceCodeFileProvider} for the project
	 *            under analysis.
	 */
	public PcmRepositoryExtractor(final SourceCodeFileProvider sourceCodeFileProvider) {
		this.sourceCodeFileProvider = sourceCodeFileProvider;
	}

	/**
	 * Builds a new blackboard and writes the Beagle objects representing all found
	 * <em>PCM elements</em> to it. Only <em>PCM elements</em> that fulfil the
	 * restrictions described in the class description will be written.
	 *
	 * @param repository The repository to look up.
	 * @param blackboardFactory all translated <em>PCM elements</em> will be written on
	 *            it. The rdias, seffLoops, seffBranches and externalCallPAramerts will
	 *            never be {@code null} afterwards.
	 */
	public void getBlackboardForAllElements(final RepositoryImpl repository,
		final BlackboardCreator blackboardFactory) {
		final PcmBeagleMappings pcmMappings = new PcmBeagleMappings();
		this.seffLoopSet = new HashSet<SeffLoop>();
		this.seffBranchSet = new HashSet<SeffBranch>();
		this.rdiaSet = new HashSet<ResourceDemandingInternalAction>();
		this.externalCallParameterSet = new HashSet<ExternalCallParameter>();
		this.pcmSeffExtractor = new PcmRepositorySeffExtractor(this.seffLoopSet, this.seffBranchSet, this.rdiaSet,
			this.externalCallParameterSet, pcmMappings, this.sourceCodeFileProvider);

		this.scanRepository(repository);

		blackboardFactory.setRdias(this.rdiaSet);
		blackboardFactory.setBranches(this.seffBranchSet);
		blackboardFactory.setLoops(this.seffLoopSet);
		blackboardFactory.setExternalCalls(this.externalCallParameterSet);
		blackboardFactory.setPcmMappings(pcmMappings);
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
	 * @param blackboardFactory all translated <em>PCM elements</em> will be written on
	 *            it. The rdias, seffLoops, seffBranches and externalCallPAramerts will
	 *            never be {@code null} afterwards.
	 */
	public void getBlackboardForIds(final RepositoryImpl repository, final Collection<String> identifiers,
		final BlackboardCreator blackboardFactory) {
		final PcmBeagleMappings pcmMappings = new PcmBeagleMappings();
		final Set<EObject> setOfIdentifiedObjects = new HashSet<EObject>();

		this.seffLoopSet = new HashSet<SeffLoop>();
		this.seffBranchSet = new HashSet<SeffBranch>();
		this.rdiaSet = new HashSet<ResourceDemandingInternalAction>();
		this.externalCallParameterSet = new HashSet<ExternalCallParameter>();
		this.pcmSeffExtractor = new PcmRepositorySeffExtractor(this.seffLoopSet, this.seffBranchSet, this.rdiaSet,
			this.externalCallParameterSet, pcmMappings, this.sourceCodeFileProvider);

		if (identifiers.contains(repository.getId())) {
			this.scanRepository(repository);

			blackboardFactory.setRdias(this.rdiaSet);
			blackboardFactory.setBranches(this.seffBranchSet);
			blackboardFactory.setLoops(this.seffLoopSet);
			blackboardFactory.setExternalCalls(this.externalCallParameterSet);
			blackboardFactory.setPcmMappings(pcmMappings);
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

		blackboardFactory.setRdias(this.rdiaSet);
		blackboardFactory.setBranches(this.seffBranchSet);
		blackboardFactory.setLoops(this.seffLoopSet);
		blackboardFactory.setExternalCalls(this.externalCallParameterSet);
		blackboardFactory.setPcmMappings(pcmMappings);
	}

	/**
	 * This method takes the whole {@link PcmRepositoryBlackboardFactoryAdder#repository}
	 * and extracts all needed content into the storing sets.
	 *
	 * @param repositoryToScan The repository to read from.
	 */
	private void scanRepository(final RepositoryImpl repositoryToScan) {
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
	 */
	private void extractBasicComponentAndAddContentsToSet(final BasicComponentImpl basicComponent) {
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
	 */
	private void extractResourceDemandingSEFFImplAndAddContentsToSet(final ResourceDemandingSEFFImpl rdSeff) {
		final EList<EObject> rdSeffContentList = rdSeff.eContents();
		for (final EObject rdSeffContent : rdSeffContentList) {
			this.pcmSeffExtractor.extractBehaviourAndAddToSet(rdSeffContent);
		}
	}

}
