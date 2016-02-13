package de.uka.ipd.sdq.beagle.core.pcmconnection;
/**
 * ATTENTION: Test coverage check turned off. Remove this comments block when implementing
 * this class!
 * 
 * <p>COVERAGE:OFF Also Checkstyle <p>CHECKSTYLE:OFF
 */

import de.uka.ipd.sdq.beagle.core.Blackboard;
import de.uka.ipd.sdq.beagle.core.ExternalCallParameter;
import de.uka.ipd.sdq.beagle.core.MeasurableSeffElement;
import de.uka.ipd.sdq.beagle.core.ResourceDemandType;
import de.uka.ipd.sdq.beagle.core.ResourceDemandingInternalAction;
import de.uka.ipd.sdq.beagle.core.SeffBranch;
import de.uka.ipd.sdq.beagle.core.SeffLoop;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpression;

import de.uka.ipd.sdq.identifier.Identifier;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.palladiosimulator.pcm.repository.RepositoryFactory;
import org.palladiosimulator.pcm.repository.impl.RepositoryImpl;
import org.palladiosimulator.pcm.resourcetype.ProcessingResourceType;
import org.palladiosimulator.pcm.seff.impl.BranchActionImpl;
import org.palladiosimulator.pcm.seff.impl.ExternalCallActionImpl;
import org.palladiosimulator.pcm.seff.impl.InternalActionImpl;
import org.palladiosimulator.pcm.seff.impl.LoopActionImpl;
import org.palladiosimulator.pcm.seff.seff_performance.ParametricResourceDemand;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

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
	 * Blackboard to get Mapping from.
	 */
	private final Blackboard blackboard;

	private final PcmBeagleMappings pcmMappings;

	/**
	 * Creates a writer to write the results written on {@code blackboard} back to a PCM
	 * repository.
	 *
	 * @param blackboard The blackboard containing results.
	 */
	public PcmRepositoryWriter(final Blackboard blackboard) {
		this.blackboard = blackboard;
		this.pcmMappings = this.blackboard.readFor(PcmRepositoryBlackboardFactory.class);
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
	 * FailureHandlers: If for an element with final EvaluableExpression from the
	 * Blackboard no Id mapping exists(!!!! NOT YET ANNOTATED WITH TO-DO!!!!)
	 * 
	 * If the element Id with final EvaluableExpression from the Blackboard can not be
	 * found in the repository file.
	 * 
	 * If the Blackboard elements (e.g. SeffBranch) from the elements with final
	 * EvaluableExpression from the Blackboard do not Correspond to the repository PCM
	 * elements (e.g. BranchAction).
	 *
	 * @param repositoryFile A PCM repository file to write Beagle’s results to. It should
	 *            use the same identifiers for elements as the repository file Beagle’s
	 *            blackboard was initially created for.
	 */
	public void writeTo(final File repositoryFile) {
		RepositoryImpl repository = loadRepositoryFromFile(repositoryFile);

		Map<String, EvaluableExpression> seffLoopIdsToEvaEx =
			this.getMapFromIdToEvaExOfAllSeffLoopsWithFinalExpressionsFromBlackboard();
		Map<String, EvaluableExpression> seffBranchIdsToEvaEx =
			this.getMapFromIdToEvaExOfAllSeffBranchesWithFinalExpressionsFromBlackboard();
		Map<String, EvaluableExpression> rdiaIdsToEvaEx =
			this.getMapFromIdToEvaExOfAllRdiasWithFinalExpressionsFromBlackboard();
		Map<String, EvaluableExpression> exParamIdsToEvaEx =
			this.getMapFromIdToEvaExOfAllExternalCallParameterWithFinalExpressionsFromBlackboard();

		this.annotateAll(repository, seffLoopIdsToEvaEx, seffBranchIdsToEvaEx, rdiaIdsToEvaEx, exParamIdsToEvaEx);

	}

	private RepositoryImpl loadRepositoryFromFile(final File repositoryFile) {
		if (repositoryFile == null) {
			throw new NullPointerException();
		}

		RepositoryFactory.eINSTANCE.createRepository();
		// Not sure if this final declaration could lead to a problem.
		final EPackage ePackage = RepositoryFactory.eINSTANCE.getEPackage();

		final EObject eObject = EMFHelper.loadFromXMIFile(repositoryFile.getAbsolutePath(), ePackage);
		if (!(eObject.getClass() == RepositoryImpl.class)) {
			throw new IllegalArgumentException("File is not a repository!");
		}
		final RepositoryImpl repository = (RepositoryImpl) eObject;
		return repository;
	}

	/**
	 * This method looks up each SeffLoop on the {@link #blackboard} and maps its ID to
	 * its finalExpression {@link Blackboard#getFinalExpressionFor(MeasurableSeffElement)}
	 * if such one exists. Otherwise, the SeffElement will no occur as Key-element.
	 *
	 * @return A map of SeffLoops IDs to its final EvaluableExpressions.
	 */
	private Map<String, EvaluableExpression> getMapFromIdToEvaExOfAllSeffLoopsWithFinalExpressionsFromBlackboard() {

		Map<String, EvaluableExpression> seffLoopIdToEvaEx = new HashMap<String, EvaluableExpression>();

		for (SeffLoop seffLoop : this.blackboard.getAllSeffLoops()) {
			EvaluableExpression evaEx = this.blackboard.getFinalExpressionFor(seffLoop);
			if (evaEx != null && this.pcmMappings.hasPcmIdOf(seffLoop)) {

				seffLoopIdToEvaEx.put(this.pcmMappings.getPcmIdOf(seffLoop), evaEx);
			}
		}
		return seffLoopIdToEvaEx;
	}

	/**
	 * This method looks up each SeffBranch on the {@link #blackboard} and maps its ID to
	 * its finalExpression {@link Blackboard#getFinalExpressionFor(MeasurableSeffElement)}
	 * if such one exists. Otherwise, the SeffElement will no occur as Key-element.
	 *
	 * @return A map of SeffBranche IDs to its final EvaluableExpressions.
	 */
	private Map<String, EvaluableExpression> getMapFromIdToEvaExOfAllSeffBranchesWithFinalExpressionsFromBlackboard() {

		Map<String, EvaluableExpression> seffBranchIdToEvaEx = new HashMap<String, EvaluableExpression>();

		for (SeffBranch seffBranch : this.blackboard.getAllSeffBranches()) {
			EvaluableExpression evaEx = this.blackboard.getFinalExpressionFor(seffBranch);
			if (evaEx != null && this.pcmMappings.hasPcmIdOf(seffBranch)) {

				seffBranchIdToEvaEx.put(this.pcmMappings.getPcmIdOf(seffBranch), evaEx);
			}
		}
		return seffBranchIdToEvaEx;
	}

	/**
	 * This method looks up each RDIA on the {@link #blackboard} and maps its ID to its
	 * finalExpression {@link Blackboard#getFinalExpressionFor(MeasurableSeffElement)} if
	 * such one exists. Otherwise, the SeffElement will no occur as Key-element.
	 *
	 * @return A map of RDIA IDs to its final EvaluableExpressions.
	 */
	private Map<String, EvaluableExpression> getMapFromIdToEvaExOfAllRdiasWithFinalExpressionsFromBlackboard() {

		Map<String, EvaluableExpression> rdiaIdToEvaEx = new HashMap<String, EvaluableExpression>();

		for (ResourceDemandingInternalAction rdia : this.blackboard.getAllRdias()) {
			EvaluableExpression evaEx = this.blackboard.getFinalExpressionFor(rdia);
			if (evaEx != null && this.pcmMappings.hasPcmIdOf(rdia)) {

				rdiaIdToEvaEx.put(this.pcmMappings.getPcmIdOf(rdia), evaEx);
			}
		}
		return rdiaIdToEvaEx;
	}

	/**
	 * This method looks up each ExternalCallParameter on the {@link #blackboard} and maps
	 * its ID to its finalExpression
	 * {@link Blackboard#getFinalExpressionFor(MeasurableSeffElement)} if such one exists.
	 * Otherwise, the SeffElement will no occur as Key-element.
	 *
	 * @return A map of ExternalCallParameter IDs to its final EvaluableExpressions.
	 */
	private Map<String, EvaluableExpression> getMapFromIdToEvaExOfAllExternalCallParameterWithFinalExpressionsFromBlackboard() {

		Map<String, EvaluableExpression> exParamIdToEvaEx = new HashMap<String, EvaluableExpression>();

		for (ExternalCallParameter exParam : this.blackboard.getAllExternalCallParameters()) {
			EvaluableExpression evaEx = this.blackboard.getFinalExpressionFor(exParam);
			if (evaEx != null && this.pcmMappings.hasPcmIdOf(exParam)) {

				exParamIdToEvaEx.put(this.pcmMappings.getPcmIdOf(exParam), evaEx);
			}
		}
		return exParamIdToEvaEx;
	}

	private void annotateAll(final RepositoryImpl repository, final Map<String, EvaluableExpression> seffLoopIdsToEvaEx,
		final Map<String, EvaluableExpression> seffBranchIdsToEvaEx,
		final Map<String, EvaluableExpression> rdiaIdsToEvaEx,
		final Map<String, EvaluableExpression> exParamIdsToEvaEx) {

		final TreeIterator<EObject> contentIterator = repository.eAllContents();
		while (contentIterator.hasNext()) {
			final EObject content = contentIterator.next();

			if (content instanceof Identifier) {
				Identifier contentIdentifier = (Identifier) content;
				String contentId = contentIdentifier.getId();

				if (seffLoopIdsToEvaEx.containsKey(contentId)) {
					if (shouldBeLoopAction(contentIdentifier)) {
						LoopActionImpl loopAction = (LoopActionImpl) contentIdentifier;
						this.annotateEvaExFor(loopAction, seffLoopIdsToEvaEx.remove(contentId));
					}
				} else if (seffBranchIdsToEvaEx.containsKey(contentId)) {
					if (shouldBeBranchAction(contentIdentifier)) {
						BranchActionImpl branchAction = (BranchActionImpl) contentIdentifier;
						this.annotateEvaExFor(branchAction, seffBranchIdsToEvaEx.remove(contentId));
					}
				} else if (rdiaIdsToEvaEx.containsKey(contentId)) {
					if (shouldBeInternalAction(contentIdentifier)) {
						InternalActionImpl internalAction = (InternalActionImpl) contentIdentifier;
						//TODO
						this.annotateEvaExFor(internalAction, rdiaIdsToEvaEx.remove(contentId), ResourceDemandType.RESOURCE_TYPE_CPU_NS);

					}
				} else if (exParamIdsToEvaEx.containsKey(contentId)) {
					if (shouldBeExternalCallAction(contentIdentifier)) {
						ExternalCallActionImpl externalAction = (ExternalCallActionImpl) contentIdentifier;
						this.annotateEvaExFor(externalAction, exParamIdsToEvaEx.remove(contentId));

					}
				}

			}
		}

		shouldBeEmpty(seffLoopIdsToEvaEx);
		shouldBeEmpty(seffBranchIdsToEvaEx);
		shouldBeEmpty(rdiaIdsToEvaEx);
		shouldBeEmpty(exParamIdsToEvaEx);

	}

	private boolean shouldBeLoopAction(final Identifier content) {
		if (content.getClass() == LoopActionImpl.class) {
			return true;
		} else {
			// TODO Implement Failure Handler
			return false;
		}
	}

	private boolean shouldBeBranchAction(final Identifier content) {
		if (content.getClass() == BranchActionImpl.class) {
			return true;
		} else {
			// TODO Implement Failure Handler
			return false;
		}
	}

	private boolean shouldBeInternalAction(final Identifier content) {
		if (content.getClass() == InternalActionImpl.class) {
			return true;
		} else {
			// TODO Implement Failure Handler
			return false;
		}
	}

	private boolean shouldBeExternalCallAction(final Identifier content) {
		if (content.getClass() == ExternalCallActionImpl.class) {
			return true;
		} else {
			// TODO Implement Failure Handler
			return false;
		}
	}

	private boolean shouldBeEmpty(final Map<String, EvaluableExpression> idMap) {
		if (idMap.isEmpty()) {
			return true;
		} else {
			// TODO Implement Failure Handler
			return false;
		}
	}

	private void annotateEvaExFor(final LoopActionImpl loopAction, final EvaluableExpression evaEx) {

	}

	private void annotateEvaExFor(final BranchActionImpl branchAction, final EvaluableExpression evaEx) {

	}

	private void annotateEvaExFor(final InternalActionImpl internalAction, final EvaluableExpression evaEx, ResourceDemandType type) {
		EList<ParametricResourceDemand> parametricsResourceDemands = internalAction.getResourceDemand_Action();
		for (ParametricResourceDemand parametricsResourceDemand: parametricsResourceDemands)  {
			ProcessingResourceType resourceType = parametricsResourceDemand.getRequiredResource_ParametricResourceDemand();
			
			
		}
	}

	private void annotateEvaExFor(final ExternalCallActionImpl externalAction, final EvaluableExpression evaEx) {

	}

}
