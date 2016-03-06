package de.uka.ipd.sdq.beagle.core.pcmconnection;

import de.uka.ipd.sdq.beagle.core.Blackboard;
import de.uka.ipd.sdq.beagle.core.CodeSection;
import de.uka.ipd.sdq.beagle.core.ExternalCallParameter;
import de.uka.ipd.sdq.beagle.core.ResourceDemandType;
import de.uka.ipd.sdq.beagle.core.ResourceDemandingInternalAction;
import de.uka.ipd.sdq.beagle.core.SeffBranch;
import de.uka.ipd.sdq.beagle.core.SeffLoop;
import de.uka.ipd.sdq.beagle.core.facade.SourceCodeFileProvider;
import de.uka.ipd.sdq.beagle.core.failurehandling.FailureHandler;
import de.uka.ipd.sdq.beagle.core.failurehandling.FailureReport;
import de.uka.ipd.sdq.beagle.core.pcmsourcestatementlink.PcmSourceStatementLinkRepository;

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
	 * The {@link FailureHandler} that is called by FileNotFoundException, giving this
	 * information to the Fail API.
	 */
	private static final FailureHandler FAILURE_HANDLER = FailureHandler.getHandler("Beagle FileNotFound Handler");

	/**
	 * Temporary storage for all extracted {@link SeffLoop SeffLoops} that should be
	 * written on the {@link Blackboard}.
	 */
	private final Set<SeffLoop> seffLoopSet;

	/**
	 * Temporary storage for all extracted {@link SeffBranch SeffBranch} that should be
	 * written on the {@link Blackboard}.
	 */
	private final Set<SeffBranch> seffBranchSet;

	/**
	 * Temporary storage for all extracted {@link SeffBranch SeffBranches} that should be
	 * written on the {@link Blackboard}.
	 */
	// private final Set<SeffBranch> seffBranchSet;

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
	 * Integer with value 0.
	 */
	private final int zero = 0;

	/**
	 * The {@link PcmNameParser} that is needed for parsing the EntityNames created by
	 * SoMoX.
	 */
	// private final PcmNameParser nameParser;

	/**
	 * The object in which all created SeffElements are mapped to their original ID.
	 */
	private final PcmBeagleMappings pcmMapper;

	/**
	 * The {@link PcmCodeSectionGenerator} that creates {@link CodeSection} for given ID.
	 */
	private final PcmCodeSectionGenerator codeSectionGenerator;

	/**
	 * Constructor needs access to the real sets (no copy!), manipulating them by adding
	 * all extracted SeffElements.
	 *
	 * @param seffLoopSet the SeffLoopSet to add SeffLoops
	 * @param seffBranchSet the seffBranchSet to add SeffBranches
	 * @param rdiaSet the rdiaSet to add RDIAS
	 * @param externalCallParameterSet the externalCallParameterSet to add
	 *            externalCallParameters
	 * @param pcmMapper The mapping class that should contain a map of all SeffElements to
	 *            its IDs
	 * @param sourceCodeFileProvider The {@link SourceCodeFileProvider} for the project
	 *            under analysis.
	 * @param sourceStateLinkRepository The {@link PcmSourceStatementLinkRepository} that
	 *            contains a linking between the Pcm elements and SourceCode Sections
	 */
	public PcmRepositorySeffExtractor(final Set<SeffLoop> seffLoopSet, final Set<SeffBranch> seffBranchSet,
		final Set<ResourceDemandingInternalAction> rdiaSet, final Set<ExternalCallParameter> externalCallParameterSet,
		final PcmBeagleMappings pcmMapper, final SourceCodeFileProvider sourceCodeFileProvider,
		final PcmSourceStatementLinkRepository sourceStateLinkRepository) {
		this.seffLoopSet = seffLoopSet;
		this.seffBranchSet = seffBranchSet;
		this.rdiaSet = rdiaSet;
		this.externalCallParameterSet = externalCallParameterSet;
		this.pcmMapper = pcmMapper;

		// this.nameParser = new PcmNameParser(sourceCodeFileProvider);
		this.codeSectionGenerator = new PcmCodeSectionGenerator(sourceStateLinkRepository, sourceCodeFileProvider);
	}

	/**
	 * Extracts the specified SEFF-elements {@link InternalActionImpl},
	 * {@link ExternalCallActionImpl}, {@link BranchActionImpl} and {@link LoopAction}.
	 *
	 * @param eObject Expecting a {@link ResourceDemandingBehaviour} or any eObject that
	 *            has a concrete SEFF-Type.
	 *
	 */
	public void extractBehaviourAndAddToSet(final EObject eObject) {
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
	 */
	private void extractLoopAction(final LoopActionImpl loopAction) {
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
	 */
	private void extractBranchAction(final BranchActionImpl branchAction) {
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
	 * {@link InternalActionImpl} to the {@link rdiaSet}. Fails silently if file from
	 * EntityName not found!
	 *
	 * @param internalAction SEFF-Action to add.
	 *
	 */
	private void addInternalActionToSet(final InternalActionImpl internalAction) {

		try {
			final CodeSection codeSection = this.codeSectionGenerator.getCodeSectionForID(internalAction.getId());

			final ResourceDemandingInternalAction rdiaCpu =
				new ResourceDemandingInternalAction(ResourceDemandType.RESOURCE_TYPE_CPU, codeSection);
			this.rdiaSet.add(rdiaCpu);
			this.pcmMapper.addPcmIdOf(rdiaCpu, internalAction.getId());

			final ResourceDemandingInternalAction rdiaCpuNs =
				new ResourceDemandingInternalAction(ResourceDemandType.RESOURCE_TYPE_CPU_NS, codeSection);
			this.rdiaSet.add(rdiaCpuNs);
			this.pcmMapper.addPcmIdOf(rdiaCpuNs, internalAction.getId());

			final ResourceDemandingInternalAction rdiaHdd =
				new ResourceDemandingInternalAction(ResourceDemandType.RESOURCE_TYPE_HDD, codeSection);
			this.rdiaSet.add(rdiaHdd);
			this.pcmMapper.addPcmIdOf(rdiaHdd, internalAction.getId());

			final ResourceDemandingInternalAction rdiaHddNs =
				new ResourceDemandingInternalAction(ResourceDemandType.RESOURCE_TYPE_HDD_NS, codeSection);
			this.rdiaSet.add(rdiaHddNs);
			this.pcmMapper.addPcmIdOf(rdiaHddNs, internalAction.getId());

			final ResourceDemandingInternalAction rdiaLan =
				new ResourceDemandingInternalAction(ResourceDemandType.RESOURCE_TYPE_NETWORK, codeSection);
			this.rdiaSet.add(rdiaLan);
			this.pcmMapper.addPcmIdOf(rdiaLan, internalAction.getId());

			final ResourceDemandingInternalAction rdiaLanNs =
				new ResourceDemandingInternalAction(ResourceDemandType.RESOURCE_TYPE_NETWORK_NS, codeSection);
			this.rdiaSet.add(rdiaLanNs);
			this.pcmMapper.addPcmIdOf(rdiaLanNs, internalAction.getId());

		} catch (final FileNotFoundException exception) {
			this.handleFailureFor(internalAction, exception);
		}

	}

	/**
	 * Adding a new {@link ExternalCallParameter} based on a
	 * {@link ExternalCallActionImpl} to the {@link externalCallParameterSet}.
	 *
	 * @param externalAction SEFF-Action to add.
	 *
	 */
	private void addExternalCallActionToSet(final ExternalCallActionImpl externalAction) {

		try {
			final CodeSection codeSection = this.codeSectionGenerator.getCodeSectionForID(externalAction.getId());

			final ExternalCallParameter exCallParam = new ExternalCallParameter(codeSection, this.zero);
			this.externalCallParameterSet.add(exCallParam);
			this.pcmMapper.addPcmIdOf(exCallParam, externalAction.getId());
		} catch (final FileNotFoundException exception) {
			this.handleFailureFor(externalAction, exception);
		}

		/*
		 * try { final CodeSection codeSection =
		 * this.nameParser.parse(externalAction.getEntityName()); if (codeSection != null)
		 * { final ExternalCallParameter exCallParam = new
		 * ExternalCallParameter(codeSection, this.zero);
		 * this.externalCallParameterSet.add(exCallParam);
		 * this.pcmMapper.addPcmIdOf(exCallParam, externalAction.getId()); } } catch
		 * (final FileNotFoundException fileNotFoundE) {
		 * this.handleFailureFor(externalAction, fileNotFoundE); }
		 */

	}

	/**
	 * Adding a new {@link SeffBranch} based on a {@link BranchActionImpl} to the
	 * {@link seffBranchSet}. Fails silently if file from EntityName not found!
	 *
	 * @param branchAction SEFF-Action to add.
	 *
	 */
	private void addBranchActionToSet(final BranchActionImpl branchAction) {

		try {
			final EList<AbstractBranchTransition> branchTransitionList = branchAction.getBranches_Branch();
			final Set<CodeSection> codeSectionSet = new HashSet<CodeSection>();

			for (AbstractBranchTransition branchTransition : branchTransitionList) {
				codeSectionSet.add(this.codeSectionGenerator
					.getCodeSectionForID(branchTransition.getBranchBehaviour_BranchTransition().getId()));
			}
			final SeffBranch seffBranch = new SeffBranch(codeSectionSet);
			this.seffBranchSet.add(seffBranch);
			this.pcmMapper.addPcmIdOf(seffBranch, branchAction.getId());
		} catch (final FileNotFoundException exception) {
			this.handleFailureFor(branchAction, exception);

		}

		// try {
		// final Set<CodeSection> codeSectionSet = new HashSet<CodeSection>();
		// final CodeSection codeSection =
		// this.nameParser.parse(branchAction.getEntityName());
		// TO DO branches can't be analysed right now because we can't get the code
		// sections
		// if (codeSection != null) {
		// codeSectionSet.add(codeSection);
		// final CodeSection pseudoCodesection = new CodeSection(new
		// File("src/main/resources/pseudoFile.md"), 1,
		// new File("src/main/resources/pseudoFile.md"), 1);
		// codeSectionSet.add(pseudoCodesection);
		// final SeffBranch seffBranch = new SeffBranch(codeSectionSet);
		//
		// this.seffBranchSet.add(seffBranch);
		// this.pcmMapper.addPcmIdOf(seffBranch, branchAction.getId());
		// }
		// } catch (final FileNotFoundException fileNotFoundE) {
		// this.handleFailureFor(branchAction, fileNotFoundE);
		// }
	}

	/**
	 * Adding a new {@link SeffLoop} based on a {@link LoopActionImpl} to the
	 * {@link seffLoopSet}. Fails silently if file from EntityName not found!
	 *
	 * @param loopAction SEFF-Action to add.
	 */
	private void addLoopActionToSet(final LoopActionImpl loopAction) {

		try {
			final CodeSection codeSection =
				this.codeSectionGenerator.getCodeSectionForID(loopAction.getBodyBehaviour_Loop().getId());
			final SeffLoop seffLoop = new SeffLoop(codeSection);
			this.seffLoopSet.add(seffLoop);
			this.pcmMapper.addPcmIdOf(seffLoop, loopAction.getId());
		} catch (final FileNotFoundException exception) {
			this.handleFailureFor(loopAction, exception);
		}

		/*
		 * try {
		 * 
		 * final CodeSection codeSection =
		 * this.nameParser.parse(loopAction.getEntityName()); if (codeSection != null) {
		 * final SeffLoop seffLoop = new SeffLoop(codeSection);
		 * this.seffLoopSet.add(seffLoop); this.pcmMapper.addPcmIdOf(seffLoop,
		 * loopAction.getId()); } } catch (final FileNotFoundException fileNotFoundE) {
		 * this.handleFailureFor(loopAction, fileNotFoundE); }
		 */
	}

	/**
	 * Creating a Failure handling action for the specific SeffElement.
	 *
	 * @param loopAction The SeffElement.
	 * @param exception The FileNotFoundException.
	 */
	private void handleFailureFor(final LoopActionImpl loopAction, final FileNotFoundException exception) {
		final FailureReport<Void> failure =
			new FailureReport<Void>().message("The File for ID %s with EntityName %s can not be found!",
				loopAction.getId(), loopAction.getEntityName()).cause(exception).recoverable().retryWith(
					() -> PcmRepositorySeffExtractor.this.addLoopActionToSet(loopAction));
		FAILURE_HANDLER.handle(failure);
	}

	/**
	 * Creating a Failure handling action for the specific SeffElement.
	 *
	 * @param ecAction The SeffElement.
	 * @param exception The FileNotFoundException.
	 */
	private void handleFailureFor(final ExternalCallActionImpl ecAction, final FileNotFoundException exception) {
		final FailureReport<Void> failure =
			new FailureReport<Void>().message("The File for ID %s with EntityName %s can not be found!",
				ecAction.getId(), ecAction.getEntityName()).cause(exception).recoverable().retryWith(
					() -> PcmRepositorySeffExtractor.this.addExternalCallActionToSet(ecAction));
		FAILURE_HANDLER.handle(failure);
	}

	/**
	 * Creating a Failure handling action for the specific SeffElement.
	 *
	 * @param internalAction The SeffElement.
	 * @param exception The FileNotFoundException.
	 */
	private void handleFailureFor(final InternalActionImpl internalAction, final FileNotFoundException exception) {
		final FailureReport<Void> failure =
			new FailureReport<Void>().message("The File for ID %s with EntityName %s can not be found!",
				internalAction.getId(), internalAction.getEntityName()).cause(exception).recoverable().retryWith(
					() -> PcmRepositorySeffExtractor.this.addInternalActionToSet(internalAction));
		FAILURE_HANDLER.handle(failure);
	}

	/**
	 * Creating a Failure handling action for the specific SeffElement.
	 *
	 * @param branchAction The SeffElement.
	 * @param exception The FileNotFoundException.
	 */
	private void handleFailureFor(final BranchActionImpl branchAction, final FileNotFoundException exception) {
		final FailureReport<Void> failure =
			new FailureReport<Void>().message("The File for ID %s with EntityName %s can not be found!",
				branchAction.getId(), branchAction.getEntityName()).cause(exception).recoverable().retryWith(
					() -> PcmRepositorySeffExtractor.this.addBranchActionToSet(branchAction));
		FAILURE_HANDLER.handle(failure);
	}

}
