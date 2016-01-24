package de.uka.ipd.sdq.beagle.core.pcmconnection;

import de.uka.ipd.sdq.beagle.core.Blackboard;
import de.uka.ipd.sdq.beagle.core.BlackboardStorer;
import de.uka.ipd.sdq.beagle.core.CodeSection;
import de.uka.ipd.sdq.beagle.core.ExternalCallParameter;
import de.uka.ipd.sdq.beagle.core.ResourceDemandingInternalAction;
import de.uka.ipd.sdq.beagle.core.SeffBranch;
import de.uka.ipd.sdq.beagle.core.SeffLoop;

import de.uka.ipd.sdq.identifier.Identifier;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.palladiosimulator.pcm.repository.RepositoryComponent;
import org.palladiosimulator.pcm.repository.RepositoryFactory;
import org.palladiosimulator.pcm.repository.impl.BasicComponentImpl;
import org.palladiosimulator.pcm.repository.impl.RepositoryImpl;
import org.palladiosimulator.pcm.seff.AbstractAction;
import org.palladiosimulator.pcm.seff.AbstractBranchTransition;
import org.palladiosimulator.pcm.seff.LoopAction;
import org.palladiosimulator.pcm.seff.ResourceDemandingBehaviour;
import org.palladiosimulator.pcm.seff.ServiceEffectSpecification;
import org.palladiosimulator.pcm.seff.impl.BranchActionImpl;
import org.palladiosimulator.pcm.seff.impl.ExternalCallActionImpl;
import org.palladiosimulator.pcm.seff.impl.InternalActionImpl;
import org.palladiosimulator.pcm.seff.impl.LoopActionImpl;
import org.palladiosimulator.pcm.seff.impl.ResourceDemandingBehaviourImpl;
import org.palladiosimulator.pcm.seff.impl.ResourceDemandingSEFFImpl;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.instrument.IllegalClassFormatException;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

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
	private RepositoryImpl repository;

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
	 * The {@link PcmNameParser} that is needed for parsing the EntityNames created by
	 * SoMoX.
	 */
	private PcmNameParser nameParser;

	/**
	 * Creates a factory that will search the provided PCM files for <em>PCM
	 * elements</em>.
	 *
	 * @param repositoryFileName PCM repository to load from.
	 * @throws IllegalClassFormatException If input parameter does not represent a valid
	 *             repository file.
	 */
	public PcmRepositoryBlackboardFactory(final String repositoryFileName) throws IllegalClassFormatException {
		RepositoryFactory.eINSTANCE.createRepository();
		// Not sure if this final declaration could lead to a problem.
		final EPackage ePackage = RepositoryFactory.eINSTANCE.getEPackage();
		final EObject eObject = EMFHelper.loadFromXMIFile(repositoryFileName, ePackage);

		if (!(eObject.getClass() == RepositoryImpl.class)) {
			throw new IllegalClassFormatException();
		}

		this.repository = (RepositoryImpl) eObject;
	}

	/**
	 * Creates a factory that will search the provided PCM files for <em>PCM
	 * elements</em>.
	 *
	 * @param pcmRepositoryFiles PCM repository files.
	 */
	public PcmRepositoryBlackboardFactory(final Set<File> pcmRepositoryFiles) {
	}

	/**
	 * Builds a new blackboard and writes the Beagle objects representing all found
	 * <em>PCM elements</em> to it. Only <em>PCM elements</em> that fulfil the
	 * restrictions described in the class description will be written.
	 *
	 * @return A new blackboard having all translated <em>PCM elements</em> written on it.
	 *         Will never be {@code null}.
	 * @throws FileNotFoundException If the file for creating {@link CodeSection} was not
	 *             found at the specified path in the repository-file.
	 */
	public Blackboard getBlackboardForAllElements() throws FileNotFoundException {
		this.seffLoopSet = new HashSet<SeffLoop>();
		this.seffBranchSet = new HashSet<SeffBranch>();
		this.rdiaSet = new HashSet<ResourceDemandingInternalAction>();
		this.externalCallParameterSet = new HashSet<ExternalCallParameter>();

		this.nameParser = new PcmNameParser();

		this.scanRepository(this.repository);
		return new Blackboard(this.rdiaSet, this.seffBranchSet, this.seffLoopSet);
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
	public Blackboard getBlackboardForIds(final Collection<String> identifiers) {

		final Set<EObject> setOfIdentifiedObjects = new HashSet<EObject>();

		this.seffLoopSet = new HashSet<SeffLoop>();
		this.seffBranchSet = new HashSet<SeffBranch>();
		this.rdiaSet = new HashSet<ResourceDemandingInternalAction>();
		this.externalCallParameterSet = new HashSet<ExternalCallParameter>();

		this.nameParser = new PcmNameParser();

		if (identifiers.contains(this.repository.getId())) {
			this.scanRepository(this.repository);
			return new Blackboard(this.rdiaSet, this.seffBranchSet, this.seffLoopSet, this.externalCallParameterSet);
		}

		// Look up for each Repository-object ID if its found in the
		// identifiers-Collection
		// If so, the Object is added to a Set named "setOfIdentifiedObjects"
		final TreeIterator<EObject> contentIterator = this.repository.eAllContents();
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
		for (EObject identifiedObject : setOfIdentifiedObjects) {
			if (identifiedObject.getClass() == BasicComponentImpl.class) {
				this.extractBasicComponentAndAddContentsToSet((BasicComponentImpl) identifiedObject);
			} else if (identifiedObject.getClass() == ResourceDemandingSEFFImpl.class) {
				this.extractResourceDemandingSEFFImplAndAddContentsToSet((ResourceDemandingSEFFImpl) identifiedObject);
			} else {
				this.extractBehaviourAndAddToSet(identifiedObject);
			}
		}

		// Add the sets to the blackboard and return
		return new Blackboard(this.rdiaSet, this.seffBranchSet, this.seffLoopSet, this.externalCallParameterSet);
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
		for (String identifier : identifiers) {
			identifierCollection.add(identifier);
		}
		return this.getBlackboardForIds(identifierCollection);
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
		for (RepositoryComponent component : componentList) {
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
		for (ServiceEffectSpecification seff : seffList) {
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
		for (EObject rdSeffContent : rdSeffContentList) {
			this.extractBehaviourAndAddToSet(rdSeffContent);
		}
	}

	/**
	 * Extracts the specified SEFF-elements {@link InternalActionImpl},
	 * {@link ExternalCallActionImpl}, {@link BranchActionImpl} and {@link LoopAction}.
	 *
	 * @param eObject Expecting a {@link ResourceDemandingBehaviour} or any eObject that
	 *            has a concrete SEFF-Type.
	 * @throws FileNotFoundException If the file for creating {@link CodeSection} was not
	 *             found at the specified path in the repository-file.
	 */
	private void extractBehaviourAndAddToSet(final EObject eObject) throws FileNotFoundException {
		if (eObject.getClass() == InternalActionImpl.class) {
			this.addInternalActionToSet((InternalActionImpl) eObject);
		} else if (eObject.getClass() == ExternalCallActionImpl.class) {
			this.addExternalCallActionToSet((ExternalCallActionImpl) eObject);
		} else if (eObject.getClass() == BranchActionImpl.class) {
			this.addBranchActionToSet((BranchActionImpl) eObject);
			this.extractBranchAction((BranchActionImpl) eObject);
		} else if (eObject.getClass() == LoopActionImpl.class) {
			this.addLoopActionToSet((LoopActionImpl) eObject);
			this.extractLoopAction((LoopActionImpl) eObject);
		}
	}

	/**
	 * Extracting the information of a {@link LoopActionImpl}. That means recursively
	 * calling the extraction of its containing {@link ResourceDemandingBehaviour}.
	 *
	 * @param loopAction Action to extract.
	 * @throws FileNotFoundException If the file for creating {@link CodeSection} was not
	 *             found at the specified path in the repository-file.
	 */
	private void extractLoopAction(final LoopActionImpl loopAction) throws FileNotFoundException {
		// MISSING CODE FOR MAPPING ELEMENT
		// final PCMRandomVariable loopVariable =
		// loopAction.getIterationCount_LoopAction();

		final ResourceDemandingBehaviour rdBehave = loopAction.getBodyBehaviour_Loop();
		final EList<AbstractAction> stepBehaviourList = rdBehave.getSteps_Behaviour();
		for (AbstractAction stepBehaviour : stepBehaviourList) {
			this.extractBehaviourAndAddToSet(stepBehaviour);
		}
	}

	/**
	 * Extracting the information of a {@link BranchActionImpl}. That means recursively
	 * calling the extraction of its containing {@link ResourceDemandingBehaviour}.
	 *
	 * @param branchAction Action to extract.
	 * @throws FileNotFoundException If the file for creating {@link CodeSection} was not
	 *             found at the specified path in the repository-file.
	 */
	private void extractBranchAction(final BranchActionImpl branchAction) throws FileNotFoundException {
		final EList<AbstractBranchTransition> branchActionSpecificBranchList = branchAction.getBranches_Branch();
		for (EObject branchActionSpecificBranch : branchActionSpecificBranchList) {
			final EList<EObject> specificBranchContentList = branchActionSpecificBranch.eContents();
			for (EObject specificBranchContent : specificBranchContentList) {
				if (specificBranchContent.getClass() == ResourceDemandingBehaviourImpl.class) {
					for (EObject stepBehaviour : ((ResourceDemandingBehaviourImpl) specificBranchContent).eContents()) {
						this.extractBehaviourAndAddToSet(stepBehaviour);
					}
				}
			}
		}
	}

	/**
	 * Adding a new {@link ResourceDemandingInternalAction} based on a
	 * {@link InternalActionImpl} to the {@link rdiaSet}.
	 *
	 * @param internalAction SEFF-Action to add.
	 * @throws FileNotFoundException If the file for creating {@link CodeSection} was not
	 *             found at the specified path in the repository-file.
	 */
	private void addInternalActionToSet(final InternalActionImpl internalAction) throws FileNotFoundException {
		final CodeSection sectionTemp = this.nameParser.parse(internalAction.getEntityName());
		final ResourceDemandingInternalAction temp =
			new ResourceDemandingInternalAction(ResourceDemandType.RESOURCE_TYPE_CPU, sectionTemp);
		this.rdiaSet.add(temp);
	}

	/**
	 * Adding a new {@link ExternalCallParameter} based on a
	 * {@link ExternalCallActionImpl} to the {@link externalCallParameterSet}.
	 *
	 * @param externalAction SEFF-Action to add.
	 */
	private void addExternalCallActionToSet(final ExternalCallActionImpl externalAction) {
		final CodeSection sectionTemp = PcmNameParser.parse(externalAction.getEntityName());
		final ExternalCallParameter temp = new ExternalCallParameter(sectionTemp);
		this.externalCallParameterSet.add(temp);
	}

	/**
	 * Adding a new {@link SeffBranch} based on a {@link BranchActionImpl} to the
	 * {@link seffBranchSet}.
	 *
	 * @param branchAction SEFF-Action to add.
	 * @throws FileNotFoundException If the file for creating {@link CodeSection} was not
	 *             found at the specified path in the repository-file.
	 */
	private void addBranchActionToSet(final BranchActionImpl branchAction) throws FileNotFoundException {
		final Set<CodeSection> sectionSet = new HashSet<CodeSection>();
		sectionSet.add(this.nameParser.parse(branchAction.getEntityName()));
		sectionSet.add(null);
		final SeffBranch temp = new SeffBranch(sectionSet);
		this.seffBranchSet.add(temp);
	}

	/**
	 * Adding a new {@link SeffLoop} based on a {@link LoopActionImpl} to the
	 * {@link seffLoopSet}.
	 *
	 * @param loopAction SEFF-Action to add.
	 * @throws FileNotFoundException If the file for creating {@link CodeSection} was not
	 *             found at the specified path in the repository-file.
	 */
	private void addLoopActionToSet(final LoopActionImpl loopAction) throws FileNotFoundException {
		final CodeSection sectionTemp = this.nameParser.parse(loopAction.getEntityName());
		final SeffLoop temp = new SeffLoop(sectionTemp);
		this.seffLoopSet.add(temp);
	}
}
