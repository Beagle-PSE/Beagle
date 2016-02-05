package de.uka.ipd.sdq.beagle.core.pcmconnection;

import de.uka.ipd.sdq.beagle.core.Blackboard;
import de.uka.ipd.sdq.beagle.core.CodeSection;
import de.uka.ipd.sdq.beagle.core.ExternalCallParameter;
import de.uka.ipd.sdq.beagle.core.ResourceDemandType;
import de.uka.ipd.sdq.beagle.core.ResourceDemandingInternalAction;
import de.uka.ipd.sdq.beagle.core.SeffBranch;
import de.uka.ipd.sdq.beagle.core.SeffLoop;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.palladiosimulator.pcm.seff.AbstractAction;
import org.palladiosimulator.pcm.seff.AbstractBranchTransition;
import org.palladiosimulator.pcm.seff.LoopAction;
import org.palladiosimulator.pcm.seff.ResourceDemandingBehaviour;
import org.palladiosimulator.pcm.seff.impl.BranchActionImpl;
import org.palladiosimulator.pcm.seff.impl.ExternalCallActionImpl;
import org.palladiosimulator.pcm.seff.impl.InternalActionImpl;
import org.palladiosimulator.pcm.seff.impl.LoopActionImpl;
import org.palladiosimulator.pcm.seff.impl.ResourceDemandingBehaviourImpl;

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Set;

/**
 * Extractor class for SEFFElements. Adding all EObjects that are SeffLoops, SeffBranches,
 * ResourceDemandingInternalActions or ExternalCallParameters to given sets.
 * 
 * @author Ansgar Spiegler
 */
public class PcmRepositorySeffExtractor {

	/**
	 * Temporary storage for all extracted {@link SeffLoop SeffLoops} that should be
	 * written on the {@link Blackboard}.
	 */
	private final Set<SeffLoop> seffLoopSet;

	/**
	 * Temporary storage for all extracted {@link SeffBranch SeffBranches} that should be
	 * written on the {@link Blackboard}.
	 */
	private final Set<SeffBranch> seffBranchSet;

	/**
	 * Temporary storage for all extracted {@link ResourceDemandingInternalAction rdSeffs}
	 * that should be written on the {@link Blackboard}.
	 */
	private final Set<ResourceDemandingInternalAction> rdiaSet;

	/**
	 * Temporary storage for all extracted {@link ExternalCallParameter} that should be
	 * written on the {@link Blackboard}.
	 */
	private final Set<ExternalCallParameter> externalCallParameterSet;

	/**
	 * The {@link PcmNameParser} that is needed for parsing the EntityNames created by
	 * SoMoX.
	 */
	private PcmNameParser nameParser;

	/**
	 * Integer with value 0.
	 */
	private final int zero = 0;

	/**
	 * Constructor needs access to the real sets (no copy!), manipulating them by adding
	 * all extracted SeffElements.
	 *
	 * @param seffLoopSet the SeffLoopSet to add SeffLoops
	 * @param seffBranchSet the seffBranchSet to add SeffBranches
	 * @param rdiaSet the rdiaSet to add RDIAS
	 * @param externalCallParameterSet the externalCallParameterSet to add
	 *            externalCallParameters
	 */
	public PcmRepositorySeffExtractor(final Set<SeffLoop> seffLoopSet, final Set<SeffBranch> seffBranchSet,
		final Set<ResourceDemandingInternalAction> rdiaSet, final Set<ExternalCallParameter> externalCallParameterSet) {
		this.seffLoopSet = seffLoopSet;
		this.seffBranchSet = seffBranchSet;
		this.rdiaSet = rdiaSet;
		this.externalCallParameterSet = externalCallParameterSet;
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
	public void extractBehaviourAndAddToSet(final EObject eObject) throws FileNotFoundException {
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
		for (final AbstractAction stepBehaviour : stepBehaviourList) {
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
		for (final EObject branchActionSpecificBranch : branchActionSpecificBranchList) {
			final EList<EObject> specificBranchContentList = branchActionSpecificBranch.eContents();
			for (final EObject specificBranchContent : specificBranchContentList) {
				if (specificBranchContent.getClass() == ResourceDemandingBehaviourImpl.class) {
					for (final EObject stepBehaviour : ((ResourceDemandingBehaviourImpl) specificBranchContent)
						.eContents()) {
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
	 * @throws FileNotFoundException If the file for creating {@link CodeSection} was not
	 *             found at the specified path in the repository-file.
	 */
	private void addExternalCallActionToSet(final ExternalCallActionImpl externalAction) throws FileNotFoundException {
		final CodeSection sectionTemp = this.nameParser.parse(externalAction.getEntityName());
		final ExternalCallParameter temp = new ExternalCallParameter(sectionTemp, this.zero);
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
