package de.uka.ipd.sdq.beagle.core.pcmconnection;

import de.uka.ipd.sdq.beagle.core.Blackboard;
import de.uka.ipd.sdq.beagle.core.ExternalCallParameter;
import de.uka.ipd.sdq.beagle.core.MeasurableSeffElement;
import de.uka.ipd.sdq.beagle.core.ResourceDemandType;
import de.uka.ipd.sdq.beagle.core.ResourceDemandingInternalAction;
import de.uka.ipd.sdq.beagle.core.SeffBranch;
import de.uka.ipd.sdq.beagle.core.SeffLoop;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpression;
import de.uka.ipd.sdq.beagle.core.failurehandling.FailureHandler;
import de.uka.ipd.sdq.beagle.core.failurehandling.FailureReport;

import de.uka.ipd.sdq.identifier.Identifier;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.palladiosimulator.pcm.repository.impl.RepositoryImpl;
import org.palladiosimulator.pcm.seff.BranchAction;
import org.palladiosimulator.pcm.seff.ExternalCallAction;
import org.palladiosimulator.pcm.seff.InternalAction;
import org.palladiosimulator.pcm.seff.LoopAction;
import org.palladiosimulator.pcm.seff.impl.BranchActionImpl;
import org.palladiosimulator.pcm.seff.impl.ExternalCallActionImpl;
import org.palladiosimulator.pcm.seff.impl.InternalActionImpl;
import org.palladiosimulator.pcm.seff.impl.LoopActionImpl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

/**
 * This class is offering a method to annotate all final EvaluableExpressions of a given
 * Blackboard to a PCM-repository.
 *
 * @author Ansgar Spiegler
 */
public class PcmRepositoryWriterAnnotator {

	/**
	 * The FailureHandler for this class.
	 */
	private static final FailureHandler FAILURE_HANDLER = FailureHandler.getHandler("PcmStorer");

	/**
	 * Blackboard to get Mapping from.
	 */
	private final Blackboard blackboard;

	/**
	 * The Mappings of Seffs to Ids.
	 */
	private final PcmBeagleMappings pcmMappings;

	/**
	 * Object for converting and annotating EvaEx.
	 */
	private final PcmRepositoryWriterAnnotatorEvaEx annotatorForEvaEx = new PcmRepositoryWriterAnnotatorEvaEx();

	/**
	 * Helper class for {@link PcmRepositoryWriter}. Offering a method to write all final
	 * {@link EvaluableExpression} from a {@link Blackboard} to a given
	 * {@link RepositoryImpl}.
	 *
	 * @param blackboard The blackboard to read from
	 * @param pcmMappings The PcmMapping to get the IDs from
	 */
	public PcmRepositoryWriterAnnotator(final Blackboard blackboard, final PcmBeagleMappings pcmMappings) {
		if (blackboard == null || pcmMappings == null) {
			throw new NullPointerException("Construtor arguments for PcmRepositoryWriterAnnotator must not be null!");
		}
		this.blackboard = blackboard;
		this.pcmMappings = pcmMappings;
	}

	/**
	 * This method looks up each SeffLoop on the {@link #blackboard} and maps its ID to
	 * its finalExpression {@link Blackboard#getFinalExpressionFor(MeasurableSeffElement)}
	 * if such one exists. Otherwise, the SeffElement will not occur as Key-element.
	 *
	 * @return A map of SeffLoops IDs to its final EvaluableExpressions.
	 */
	private Map<String, EvaluableExpression> getMapFromIdToEvaExOfAllSeffLoopsWithFinalExpressionsFromBlackboard() {

		final Map<String, EvaluableExpression> seffLoopIdToEvaEx = new HashMap<String, EvaluableExpression>();

		for (final SeffLoop seffLoop : this.blackboard.getAllSeffLoops()) {
			final EvaluableExpression evaEx = this.blackboard.getFinalExpressionFor(seffLoop);
			if (evaEx != null && this.pcmMappings.hasPcmIdOf(seffLoop)) {

				seffLoopIdToEvaEx.put(this.pcmMappings.getPcmIdOf(seffLoop), evaEx);
			}
		}
		return seffLoopIdToEvaEx;
	}

	/**
	 * This method looks up each SeffBranch on the {@link #blackboard} and maps its ID to
	 * its finalExpression {@link Blackboard#getFinalExpressionFor(MeasurableSeffElement)}
	 * if such one exists. Otherwise, the SeffElement will not occur as Key-element.
	 *
	 * @return A map of SeffBranche IDs to its final EvaluableExpressions.
	 */
	private Map<String, EvaluableExpression> getMapFromIdToEvaExOfAllSeffBranchesWithFinalExpressionsFromBlackboard() {

		final Map<String, EvaluableExpression> seffBranchIdToEvaEx = new HashMap<String, EvaluableExpression>();

		for (final SeffBranch seffBranch : this.blackboard.getAllSeffBranches()) {
			final EvaluableExpression evaEx = this.blackboard.getFinalExpressionFor(seffBranch);
			if (evaEx != null && this.pcmMappings.hasPcmIdOf(seffBranch)) {

				seffBranchIdToEvaEx.put(this.pcmMappings.getPcmIdOf(seffBranch), evaEx);
			}
		}
		return seffBranchIdToEvaEx;
	}

	/**
	 * This method looks up each RDIA on the {@link #blackboard} and maps its ID to its
	 * finalExpression {@link Blackboard#getFinalExpressionFor(MeasurableSeffElement)} if
	 * such one exists. Otherwise, the SeffElement will not occur as Key-element.
	 *
	 * @return A map of RDIA IDs to its final EvaluableExpressions.
	 */
	private Map<String, EvaluableExpression> getMapFromIdToEvaExOfAllRdiasWithFinalExpressionsFromBlackboard() {

		final Map<String, EvaluableExpression> rdiaIdToEvaEx = new HashMap<String, EvaluableExpression>();

		for (final ResourceDemandingInternalAction rdia : this.blackboard.getAllRdias()) {
			final EvaluableExpression evaEx = this.blackboard.getFinalExpressionFor(rdia);
			if (evaEx != null && this.pcmMappings.hasPcmIdOf(rdia)) {

				rdiaIdToEvaEx.put(this.pcmMappings.getPcmIdOf(rdia), evaEx);
			}
		}
		return rdiaIdToEvaEx;
	}

	/**
	 * This method looks up each RDIA on the {@link #blackboard} and maps its ID to its
	 * ResourceDemandType if a final EvaluablExpression is found on the Blackboard.
	 * Otherwise, the RDIA will not occur as Key-element.
	 *
	 * @return A map of RDIA IDs to its ResourceDemandTypes.
	 */
	private Map<String, ResourceDemandType> getMapFromIdToResourceDemandTypeOfAllRdiasWithFinalExpressionsFromBlackboard() {

		final Map<String, ResourceDemandType> rdiaIdToDemandType = new HashMap<String, ResourceDemandType>();

		for (final ResourceDemandingInternalAction rdia : this.blackboard.getAllRdias()) {
			final EvaluableExpression evaEx = this.blackboard.getFinalExpressionFor(rdia);
			if (evaEx != null && this.pcmMappings.hasPcmIdOf(rdia)) {

				rdiaIdToDemandType.put(this.pcmMappings.getPcmIdOf(rdia), rdia.getResourceType());
			}
		}
		return rdiaIdToDemandType;
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

		final Map<String, EvaluableExpression> exParamIdToEvaEx = new HashMap<String, EvaluableExpression>();

		for (final ExternalCallParameter exParam : this.blackboard.getAllExternalCallParameters()) {
			final EvaluableExpression evaEx = this.blackboard.getFinalExpressionFor(exParam);
			if (evaEx != null && this.pcmMappings.hasPcmIdOf(exParam)) {

				exParamIdToEvaEx.put(this.pcmMappings.getPcmIdOf(exParam), evaEx);
			}
		}
		return exParamIdToEvaEx;
	}

	/**
	 * This method annotates given {@link EvaluableExpression} to corresponding PCM
	 * elements. Therefore all IDs of the given SeffElements with EvaluableExpression must
	 * be found in the repository-file.
	 *
	 * @param repository The repository file to store the given EvaluableExpressions (will
	 *            overwrite the old StochasticExpressions)
	 */
	public void annotateAll(final RepositoryImpl repository) {

		if (repository == null) {
			throw new NullPointerException(
				"The repository in the method \"RepositoryWriter.annotateAll\" must not be null!");
		}

		final Map<String, EvaluableExpression> seffLoopIdsToEvaEx =
			this.getMapFromIdToEvaExOfAllSeffLoopsWithFinalExpressionsFromBlackboard();
		final Map<String, EvaluableExpression> seffBranchIdsToEvaEx =
			this.getMapFromIdToEvaExOfAllSeffBranchesWithFinalExpressionsFromBlackboard();
		final Map<String, EvaluableExpression> rdiaIdsToEvaEx =
			this.getMapFromIdToEvaExOfAllRdiasWithFinalExpressionsFromBlackboard();
		final Map<String, ResourceDemandType> rdiaIdsToResourceDemandType =
			this.getMapFromIdToResourceDemandTypeOfAllRdiasWithFinalExpressionsFromBlackboard();
		final Map<String, EvaluableExpression> exParamIdsToEvaEx =
			this.getMapFromIdToEvaExOfAllExternalCallParameterWithFinalExpressionsFromBlackboard();

		//final ListIterator<EObject> contentListIterator = IteratorUtils.toListIterator(repository.eAllContents());
		final TreeIterator<EObject> treeIterator = repository.eAllContents();
		final LinkedList<EObject> copyList = new LinkedList<EObject>();
		while (treeIterator.hasNext()) {
			copyList.add(treeIterator.next());
		}
		final Iterator<EObject> copyListIterator = copyList.iterator();
		
		while (copyListIterator.hasNext()) {
			final EObject content = copyListIterator.next();

			this.annotateForEObject(content, seffLoopIdsToEvaEx, seffBranchIdsToEvaEx, rdiaIdsToEvaEx,
				rdiaIdsToResourceDemandType, exParamIdsToEvaEx);

		}

		this.shouldBeEmpty(seffLoopIdsToEvaEx);
		this.shouldBeEmpty(seffBranchIdsToEvaEx);
		this.shouldBeEmpty(rdiaIdsToEvaEx);
		this.shouldBeEmpty(exParamIdsToEvaEx);

	}

	/**
	 * Find out, if a given EObject has the ID of a given Set. If so, the
	 * EvaluableExpression of the given Set is annotate to the specific PCM element,
	 * represented by this ID.
	 *
	 * @param eObject The EObject to test the ID
	 * @param seffLoopIdsToEvaEx Mapping from seffElement ID to its EvaluableExpression
	 * @param seffBranchIdsToEvaEx Mapping from seffElement ID to its EvaluableExpression
	 * @param rdiaIdsToEvaEx Mapping from seffElement ID to its EvaluableExpression
	 * @param rdiaIdsToResourceDemandType Mapping from RDIA ID to its
	 *            {@link ResourceDemandType}
	 * @param exParamIdsToEvaEx Mapping from seffElement ID to its EvaluableExpression
	 */
	private void annotateForEObject(final EObject eObject, final Map<String, EvaluableExpression> seffLoopIdsToEvaEx,
		final Map<String, EvaluableExpression> seffBranchIdsToEvaEx,
		final Map<String, EvaluableExpression> rdiaIdsToEvaEx,
		final Map<String, ResourceDemandType> rdiaIdsToResourceDemandType,
		final Map<String, EvaluableExpression> exParamIdsToEvaEx) {

		if (eObject instanceof Identifier) {
			final Identifier contentIdentifier = (Identifier) eObject;
			final String contentId = contentIdentifier.getId();

			if (seffLoopIdsToEvaEx.containsKey(contentId) && this.shouldBeLoopAction(contentIdentifier)) {

				final LoopActionImpl loopAction = (LoopActionImpl) contentIdentifier;
				this.annotatorForEvaEx.annotateEvaExFor(loopAction, seffLoopIdsToEvaEx.remove(contentId));

			} else if (seffBranchIdsToEvaEx.containsKey(contentId) && this.shouldBeBranchAction(contentIdentifier)) {

				final BranchActionImpl branchAction = (BranchActionImpl) contentIdentifier;
				this.annotatorForEvaEx.annotateEvaExFor(branchAction, seffBranchIdsToEvaEx.remove(contentId));

			} else if (rdiaIdsToEvaEx.containsKey(contentId) && this.shouldBeInternalAction(contentIdentifier)) {

				final InternalActionImpl internalAction = (InternalActionImpl) contentIdentifier;
				this.annotatorForEvaEx.annotateEvaExFor(internalAction, rdiaIdsToResourceDemandType.get(contentId),
					rdiaIdsToEvaEx.remove(contentId));

			} else if (exParamIdsToEvaEx.containsKey(contentId) && this.shouldBeExternalCallAction(contentIdentifier)) {

				final ExternalCallActionImpl externalAction = (ExternalCallActionImpl) contentIdentifier;
				this.annotatorForEvaEx.annotateEvaExFor(externalAction, exParamIdsToEvaEx.remove(contentId));

			}
		}

	}

	/**
	 * Checking if a given {@link Identifier} is a {@link LoopAction}. Otherwise a Failure
	 * is reported.
	 *
	 * @param content The {{@link Identifier} to check
	 * @return {@code true} if content is {@link LoopAction}
	 */
	private boolean shouldBeLoopAction(final Identifier content) {
		if (content.getClass() == LoopActionImpl.class) {
			return true;
		}

		final FailureReport<Void> failure = new FailureReport<Void>()
			.message("The SeffElement with ID %s is stored by Beagle as a LoopAction"
				+ " but is not a LoopAction in the repository-file!", content.getId())
			.cause(new IllegalArgumentException())
			.recoverable()
			.retryWith(() -> this.shouldBeLoopAction(content));
		FAILURE_HANDLER.handle(failure);

		return false;
	}

	/**
	 * Checking if a given {@link Identifier} is a {@link BranchAction}. Otherwise a
	 * Failure is reported.
	 *
	 * @param content The {{@link Identifier} to check
	 * @return {@code true} if content is {@link BranchAction}
	 */
	private boolean shouldBeBranchAction(final Identifier content) {
		if (content.getClass() == BranchActionImpl.class) {
			return true;
		}
		final FailureReport<Void> failure = new FailureReport<Void>()
			.message("The SeffElement with ID %s is stored by Beagle as a BranchAction"
				+ " but is not a BranchAction in the repository-file!", content.getId())
			.cause(new IllegalArgumentException())
			.recoverable()
			.retryWith(() -> this.shouldBeBranchAction(content));
		FAILURE_HANDLER.handle(failure);
		return false;
	}

	/**
	 * Checking if a given {@link Identifier} is a {@link InternalAction}. Otherwise a
	 * Failure is reported.
	 *
	 * @param content The {{@link Identifier} to check
	 * @return {@code true} if content is {@link InternalAction}
	 */
	private boolean shouldBeInternalAction(final Identifier content) {
		if (content.getClass() == InternalActionImpl.class) {
			return true;
		}
		final FailureReport<Void> failure = new FailureReport<Void>()
			.message("The SeffElement with ID %s is stored by Beagle as an InternalAction but is"
				+ " not an InternalAction in the repository-file!", content.getId())
			.cause(new IllegalArgumentException())
			.recoverable()
			.retryWith(() -> this.shouldBeInternalAction(content));
		FAILURE_HANDLER.handle(failure);
		return false;
	}

	/**
	 * Checking if a given {@link Identifier} is a {@link ExternalCallAction}. Otherwise a
	 * Failure is reported.
	 *
	 * @param content The {{@link Identifier} to check
	 * @return {@code true} if content is {@link ExternalCallAction}
	 */
	private boolean shouldBeExternalCallAction(final Identifier content) {
		if (content.getClass() == ExternalCallActionImpl.class) {
			return true;
		}
		final FailureReport<Void> failure = new FailureReport<Void>()
			.message("The SeffElement with ID %s is stored by Beagle as an ExternalCallAction but is"
				+ " not an ExternalCallAction in the repository-file!", content.getId())
			.cause(new IllegalArgumentException())
			.recoverable()
			.retryWith(() -> this.shouldBeExternalCallAction(content));
		FAILURE_HANDLER.handle(failure);
		return false;
	}

	/**
	 * Checking if a given Map is empty. This should be called right after modifying the
	 * repository file, to check that no ID is missing. In this case, there would be a
	 * SeffElement on the {@link Blackboard} that does not fit to a given Repository. That
	 * causes this method to do a Failure report.
	 *
	 * @param idMap The Map to check
	 * @return {@code true} if the given Map is empty
	 */
	private boolean shouldBeEmpty(final Map<String, EvaluableExpression> idMap) {
		if (idMap.isEmpty()) {
			return true;
		}
		final FailureReport<Void> failure = new FailureReport<Void>()
			.message("The Blackboard contains seffElements that do not fit to the given repository-file!")
			.cause(new IllegalArgumentException());
		FAILURE_HANDLER.handle(failure);
		return false;
	}

}
