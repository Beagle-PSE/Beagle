package de.uka.ipd.sdq.beagle.judge;

import de.uka.ipd.sdq.beagle.core.Blackboard;
import de.uka.ipd.sdq.beagle.core.ResourceDemandingInternalAction;
import de.uka.ipd.sdq.beagle.core.SEFFBranch;
import de.uka.ipd.sdq.beagle.core.SEFFLoop;
import de.uka.ipd.sdq.beagle.core.expressions.EvaluableExpression;

import java.util.Set;

/**
 * View of the {@link Blackboard} designed to be used by
 * {@link de.uka.ipd.sdq.beagle.judge.FinalJudge}, therefore allowing reading access,
 * remeasuring {@link ResourceDemandingInternalAction}s, {@link SEFFBranch}es, and
 * {@link SEFFLoop}s, and setting final expressions.
 * 
 * @author Christoph Michelbach
 * 
 * @see {@link Blackboard} for information about {@link Blackboard} views
 */
public class FinalJudgeBlackboardView {

	/**
	 * Creates a new {@code FinalJudgeBlackboardView} for {@code blackboard}.
	 *
	 * @param blackboard The {@link Blackboard} to be accessed through the
	 *            {@code FinalJudgeBlackboardView}.
	 */
	public FinalJudgeBlackboardView(final Blackboard blackboard) {
	}

	/**
	 * Delegates to {@link de.uka.ipd.sdq.beagle.core.Blackboard#getRDIAsToBeMeasured()}.
	 *
	 * @return All {@linkplain ResourceDemandingInternalAction resource demanding internal
	 *         actions} to be measured. Changes to the returned set will not modify the
	 *         blackboard content. Is never {@code null}.
	 * @see de.uka.ipd.sdq.beagle.core.Blackboard#getRDIAsToBeMeasured()
	 */
	public Set<ResourceDemandingInternalAction> getRDIAsToBeMeasured() {
		return null;
	}

	/**
	 * Delegates to
	 * {@link de.uka.ipd.sdq.beagle.core.Blackboard#getSEFFBranchesToBeMeasured()}.
	 *
	 * @return All {@linkplain SEFFBranch SEFF branches} to be measured. Changes to the
	 *         returned set will not modify the blackboard content. Is never {@code null}.
	 * @see de.uka.ipd.sdq.beagle.core.Blackboard#getSEFFBranchesToBeMeasured()
	 */
	public Set<SEFFBranch> getSEFFBranchesToBeMeasured() {
		return null;
	}

	/**
	 * Delegates to
	 * {@link de.uka.ipd.sdq.beagle.core.Blackboard#getSEFFLoopsToBeMeasured()}.
	 *
	 * @return All {@linkplain SEFFLoop SEFF loops} to be measured. Changes to the
	 *         returned set will not modify the blackboard content. Is never {@code null}.
	 * @see de.uka.ipd.sdq.beagle.core.Blackboard#getSEFFLoopsToBeMeasured()
	 */
	public Set<SEFFLoop> getSEFFLoopsToBeMeasured() {
		return null;
	}

	/**
	 * Delegates to {@link de.uka.ipd.sdq.beagle.core.Blackboard#setFinalExpressionFor()}.
	 *
	 * @param rdia A resource demanding internal action. Must not be {@code null}.
	 * @param expression An evaluable expression describing {@code rdia}’s measurement
	 *            results. May be {@code null} to describe that no suitable expression was
	 *            found.
	 * @see de.uka.ipd.sdq.beagle.core.Blackboard#setFinalExpressionFor()
	 */
	public void setFinalExpressionFor(final ResourceDemandingInternalAction rdia,
		final EvaluableExpression expression) {
	}

	/**
	 * Delegates to {@link de.uka.ipd.sdq.beagle.core.Blackboard#setFinalExpressionFor()}.
	 *
	 * @param branch A SEFF Branch. Must not be {@code null}.
	 * @param expression An evaluable expression describing {@code branch}’s measurement
	 *            results. May be {@code null} to describe that no suitable expression was
	 *            found.
	 * @see de.uka.ipd.sdq.beagle.core.Blackboard#setFinalExpressionFor()
	 */
	public void setFinalExpressionFor(final SEFFBranch branch, final EvaluableExpression expression) {
	}

	/**
	 * Delegates to {@link de.uka.ipd.sdq.beagle.core.Blackboard#setFinalExpressionFor()}.
	 *
	 * @param loop A SEFF Loop. Must not be {@code null}.
	 * @param expression An evaluable expression describing {@code loop}’s measurement
	 *            results. May be {@code null} to describe that no suitable expression was
	 *            found.
	 * @see de.uka.ipd.sdq.beagle.core.Blackboard#setFinalExpressionFor()
	 */
	public void setFinalExpressionFor(final SEFFLoop loop, final EvaluableExpression expression) {
	}

	/**
	 * Remeasures the {@link ResourceDemandingInternalAction}s in {@code remeasure}.
	 *
	 * @param remeasure The {@link ResourceDemandingInternalAction}s to be remeasured.
	 */
	public void remeasureResourceDemandingInternalActions(final Set<ResourceDemandingInternalAction> remeasure) {

	}

	/**
	 * Remeasures the {@link SEFFBranch}es in {@code remeasure}.
	 *
	 * @param remeasure The {@link SEFFBranch}es to be remeasured.
	 */
	public void remeasureSeffBranches(final Set<SEFFBranch> remeasure) {

	}

	/**
	 * Remeasures the {@link SEFFLoop}s in {@code remeasure}.
	 *
	 * @param remeasure The {@link SEFFLoop}s to be remeasured.
	 */
	public void remeasureSeffLoops(final Set<SEFFLoop> remeasure) {

	}

}
