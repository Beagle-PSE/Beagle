package de.uka.ipd.sdq.beagle.judge;

import de.uka.ipd.sdq.beagle.core.Blackboard;
import de.uka.ipd.sdq.beagle.core.BlackboardStorer;
import de.uka.ipd.sdq.beagle.core.ResourceDemandingInternalAction;
import de.uka.ipd.sdq.beagle.core.SEFFBranch;
import de.uka.ipd.sdq.beagle.core.SEFFLoop;
import de.uka.ipd.sdq.beagle.core.expressions.EvaluableExpression;
import de.uka.ipd.sdq.beagle.measurement.BranchDecisionMeasurementResult;
import de.uka.ipd.sdq.beagle.measurement.ResourceDemandMeasurementResult;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * View of the {@link Blackboard} designed to be used by
 * {@link de.uka.ipd.sdq.beagle.judge.FinalJudge}, therefore allowing reading access,
 * remeasuring {@link ResourceDemandingInternalAction}s, {@link SEFFBranch}es, and
 * {@link SEFFLoop}s, and setting final expressions.
 *
 * @author Christoph Michelbach
 * @author Roman Langrehr
 *
 * @see Blackboard for information about Blackboard views
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
	 * Delegates to {@link de.uka.ipd.sdq.beagle.core.Blackboard#getAllRDIAs()}.
	 *
	 * @return all {@linkplain ResourceDemandingInternalAction resource demanding internal
	 *         actions} known to Beagle. Changes to the returned set will not modify the
	 *         blackboard content. Is never {@code null}.
	 * @see de.uka.ipd.sdq.beagle.core.Blackboard#getAllRDIAs()
	 */
	public Set<ResourceDemandingInternalAction> getAllRDIAs() {
		return null;
	}

	/**
	 * Delegates to {@link de.uka.ipd.sdq.beagle.core.Blackboard#getAllSEFFBranches()}.
	 *
	 * @return all {@linkplain SEFFBranch SEFF branches} known to Beagle. Changes to the
	 *         returned set will not modify the blackboard content. Is never {@code null}.
	 * @see de.uka.ipd.sdq.beagle.core.Blackboard#getAllSEFFBranches()
	 */
	public Set<SEFFBranch> getAllSEFFBranches() {
		return null;
	}

	/**
	 * Delegates to {@link de.uka.ipd.sdq.beagle.core.Blackboard#getAllSEFFLoops()}.
	 *
	 * @return all {@linkplain SEFFLoop SEFF loops} known to Beagle. Changes to the
	 *         returned set will not modify the blackboard content. Is never {@code null}.
	 * @see de.uka.ipd.sdq.beagle.core.Blackboard#getAllSEFFLoops()
	 */
	public Set<SEFFLoop> getAllSEFFLoops() {
		return null;
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
	 * {@link de.uka.ipd.sdq.beagle.core.Blackboard#writeFor(Class, Serializable)} .
	 *
	 * @param writer The class the data should be written for. Must not be {@code null}.
	 * @param written The data to write.
	 * @param <WRITTEN_TYPE> {@code written}’s type.
	 * @see de.uka.ipd.sdq.beagle.core.Blackboard#writeFor(Class, Serializable)
	 */
	public <WRITTEN_TYPE extends Serializable> void writeFor(
		final Class<? extends BlackboardStorer<WRITTEN_TYPE>> writer, final WRITTEN_TYPE written) {
	}

	/**
	 * Delegates to {@link de.uka.ipd.sdq.beagle.core.Blackboard#readFor(Class)} .
	 *
	 * @param writer The class the desired data was written for. Must not be {@code null}.
	 * @param <WRITTEN_TYPE> The type of the data to be read.
	 * @return The data written in the last call to
	 *         {@linkplain de.uka.ipd.sdq.beagle.core.Blackboard#writeFor(Class, Serializable)}
	 *         for {@code writer}. {@code null} if no data has been written for
	 *         {@code writer} yet.
	 * @see de.uka.ipd.sdq.beagle.core.Blackboard#readFor(Class)
	 */
	public <WRITTEN_TYPE extends Serializable> WRITTEN_TYPE readFor(
		final Class<? extends BlackboardStorer<WRITTEN_TYPE>> writer) {
		return null;
	}

	/**
	 * Delegates to
	 * {@link de.uka.ipd.sdq.beagle.core.Blackboard#getMeasurementResultsFor(SEFFLoop)}.
	 *
	 * @param loop A SEFF Loop to get the measurement results of. Must not be {@code null}
	 *            .
	 * @return All measurement results reported for {@code loop}. Changes to the returned
	 *         set will not modify the blackboard content. Is never {@code null}.
	 * @see de.uka.ipd.sdq.beagle.core.Blackboard#getMeasurementResultsFor(SEFFLoop)
	 */
	public Set<ResourceDemandMeasurementResult> getMeasurementResultsFor(final SEFFLoop loop) {
		return null;
	}

	/**
	 * Delegates to
	 * {@link de.uka.ipd.sdq.beagle.core.Blackboard#getMeasurementResultsFor(SEFFBranch)}.
	 *
	 * @param branch A SEFF Branch to get the measurement results of. Must not be
	 *            {@code null}.
	 * @return All measurement results reported for {@code branch}. Changes to the
	 *         returned set will not modify the blackboard content. Is never {@code null}.
	 * @see de.uka.ipd.sdq.beagle.core.Blackboard#getMeasurementResultsFor(SEFFBranch)
	 */
	public Set<BranchDecisionMeasurementResult> getMeasurementResultsFor(final SEFFBranch branch) {
		return null;
	}

	/**
	 * Delegates to
	 * {@link de.uka.ipd.sdq.beagle.core.Blackboard#getMeasurementResultsFor(ResourceDemandingInternalAction)}
	 * .
	 *
	 * @param rdia An resource demanding internal action to get the measuremnt results of.
	 *            Must not be {@code null}.
	 * @return All measurement results reported for {@code rdia}. Changes to the returned
	 *         set will not modify the blackboard content. Is never {@code null}.
	 * @see de.uka.ipd.sdq.beagle.core.Blackboard#getMeasurementResultsFor(ResourceDemandingInternalAction)
	 */
	public Set<ResourceDemandMeasurementResult> getMeasurementResultsFor(final ResourceDemandingInternalAction rdia) {
		return null;
	}

	/**
	 * Delegates to
	 * {@link Blackboard#proposeExpressionForRDIA(ResourceDemandingInternalAction, EvaluableExpression)}
	 * .
	 *
	 * @param rdia A resource demanding internal action. Must not be {@code null}.
	 * @param expression An evaluable expression proposed to describe {@code rdia}’s
	 *            measurement results. Must not be {@code null}.
	 * @see Blackboard#proposeExpressionForRDIA(ResourceDemandingInternalAction,
	 *      EvaluableExpression)
	 */
	public void proposeExpressionFor(final ResourceDemandingInternalAction rdia, final EvaluableExpression expression) {
	}

	/**
	 * Delegates to
	 * {@link Blackboard#proposeExpressionForSEFFLoop(SEFFLoop, EvaluableExpression)} .
	 *
	 * @param loop A SEFF Loop. Must not be {@code null}.
	 * @param expression An evaluable expression proposed to describe {@code loop}’s
	 *            measurement results. Must not be {@code null}.
	 * @see Blackboard#proposeExpressionForSEFFLoop(SEFFLoop, EvaluableExpression)
	 */
	public void proposeExpressionFor(final SEFFLoop loop, final EvaluableExpression expression) {
	}

	/**
	 * Delegates to
	 * {@link Blackboard#proposeExpressionForSEFFBranch(SEFFBranch, EvaluableExpression)}
	 * .
	 *
	 * @param branch A SEFF Branch. Must not be {@code null}.
	 * @param expression An evaluable expression proposed to describe {@code branch}’s
	 *            measurement results. Must not be {@code null}.
	 * @see Blackboard#proposeExpressionForSEFFBranch(SEFFBranch, EvaluableExpression)
	 */
	public void proposeExpressionFor(final SEFFBranch branch, final EvaluableExpression expression) {
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
	 * Delegates to
	 * {@link Blackboard#setFinalExpressionForRDIA(ResourceDemandingInternalAction, EvaluableExpression)}
	 * .
	 *
	 * @param rdia A resource demanding internal action. Must not be {@code null}.
	 * @param expression An evaluable expression describing {@code rdia}’s measurement
	 *            results. May be {@code null} to describe that no suitable expression was
	 *            found.
	 * @see de.uka.ipd.sdq.beagle.core.Blackboard#setFinalExpressionForRDIA(ResourceDemandingInternalAction,
	 *      EvaluableExpression)
	 */
	public void setFinalExpressionFor(final ResourceDemandingInternalAction rdia,
		final EvaluableExpression expression) {
	}

	/**
	 * Delegates to
	 * {@link de.uka.ipd.sdq.beagle.core.Blackboard#setFinalExpressionForSEFFBranch(SEFFBranch, EvaluableExpression)}
	 * .
	 *
	 * @param branch A SEFF Branch. Must not be {@code null}.
	 * @param expression An evaluable expression describing {@code branch}’s measurement
	 *            results. May be {@code null} to describe that no suitable expression was
	 *            found.
	 * @see de.uka.ipd.sdq.beagle.core.Blackboard#setFinalExpressionForSEFFBranch(SEFFBranch,
	 *      EvaluableExpression)
	 */
	public void setFinalExpressionFor(final SEFFBranch branch, final EvaluableExpression expression) {
	}

	/**
	 * Delegates to
	 * {@link de.uka.ipd.sdq.beagle.core.Blackboard#setFinalExpressionForSEFFLoop(SEFFLoop, EvaluableExpression)}
	 * .
	 *
	 * @param loop A SEFF Loop. Must not be {@code null}.
	 * @param expression An evaluable expression describing {@code loop}’s measurement
	 *            results. May be {@code null} to describe that no suitable expression was
	 *            found.
	 * @see de.uka.ipd.sdq.beagle.core.Blackboard#setFinalExpressionForSEFFLoop(SEFFLoop,
	 *      EvaluableExpression)
	 */
	public void setFinalExpressionFor(final SEFFLoop loop, final EvaluableExpression expression) {
	}

	/**
	 * Delegates to {@link Blackboard#setFinalExpressionsForRDIAs(Map)}
	 *
	 * @param expressions An evaluable expression for some Resource Demanding Internal
	 *            Actions describing their measurement results. The evaluable expressions
	 *            may be {@code null} to describe that no suitable expression was found.
	 *            Must not be {@code null} and no key of this map may be {@code null}. Not
	 *            each Resource Demanding Internal Action of this blackboard needs to be a
	 *            key in this map.
	 */
	public void setFinalExpressionsForRDIAs(
		final Map<ResourceDemandingInternalAction, EvaluableExpression> expressions) {
	}

	/**
	 * Delegates to {@link Blackboard#setFinalExpressionsForSEFFLoops(Map)}
	 *
	 * @param expressions An evaluable expression for some SEFF Loops describing their
	 *            measurement results. The evaluable expressions may be {@code null} to
	 *            describe that no suitable expression was found. Must not be {@code null}
	 *            and no key of this map may be {@code null}. Not each SEFFLoop of this
	 *            blackboard needs to be a key in this map.
	 */
	public void setFinalExpressionsForSEFFLoops(final Map<SEFFLoop, EvaluableExpression> expressions) {
	}

	/**
	 * Delegates to {@link Blackboard#setFinalExpressionsForSEFFBranches(Map)}
	 *
	 * @param expressions An evaluable expression for some SEFF Branches describing their
	 *            measurement results. The evaluable expressions may be {@code null} to
	 *            describe that no suitable expression was found. Must not be {@code null}
	 *            and no key of this map may be {@code null}. Not each SEFFLoop of this
	 *            blackboard needs to be a key in this map.
	 */
	public void setFinalExpressionsForSEFFBranches(final Map<SEFFBranch, EvaluableExpression> expressions) {
	}

	/**
	 * Delegates to
	 * {@link de.uka.ipd.sdq.beagle.core.Blackboard#remeasureRDIAs(ResourceDemandingInternalAction...)}
	 * .
	 *
	 * @param rdias Resource demanding internal actions that shall be measured. Must not
	 *            be {@code null} and must be known to the {@link Blackboard}.
	 * @see de.uka.ipd.sdq.beagle.core.Blackboard#remeasureRDIAs(ResourceDemandingInternalAction...)
	 * @see #remeasureRDIAs(Collection)
	 */
	public void remeasureRDIAs(final ResourceDemandingInternalAction... rdias) {
	}

	/**
	 * Delegates to
	 * {@link de.uka.ipd.sdq.beagle.core.Blackboard#remeasureRDIAs(Collection)} .
	 *
	 * @param rdias Resource demanding internal actions that shall be measured. Must not
	 *            be {@code null} and must be known to the {@link Blackboard}.
	 * @see de.uka.ipd.sdq.beagle.core.Blackboard#remeasureRDIAs(Collection)
	 * @see #remeasureRDIAs(ResourceDemandingInternalAction...)
	 */
	public void remeasureRDIAs(final Collection<ResourceDemandingInternalAction> rdias) {
	}

	/**
	 * Delegates to
	 * {@link de.uka.ipd.sdq.beagle.core.Blackboard#remeasureSEFFBranches(SEFFBranch...)}
	 * .
	 *
	 * @param branches SEFF branches that shall be measured. Must not be {@code null} and
	 *            must be known to the Blackboard.
	 * @see de.uka.ipd.sdq.beagle.core.Blackboard#remeasureSEFFBranches(SEFFBranch...)
	 * @see #remeasureSEFFBranches(Collection)
	 */
	public void remeasureSEFFBranches(final SEFFBranch... branches) {
	}

	/**
	 * Delegates to
	 * {@link de.uka.ipd.sdq.beagle.core.Blackboard#remeasureSEFFBranches(Collection)} .
	 *
	 * @param branches SEFF branches that shall be measured. Must not be {@code null} and
	 *            must be known to the Blackboard.
	 * @see de.uka.ipd.sdq.beagle.core.Blackboard#remeasureSEFFBranches(Collection)
	 * @see #remeasureSEFFBranches(SEFFBranch...)
	 */
	public void remeasureSEFFBranches(final Collection<SEFFBranch> branches) {
	}

	/**
	 * Delegates to
	 * {@link de.uka.ipd.sdq.beagle.core.Blackboard#remeasureSEFFLoops(SEFFLoop...)} .
	 *
	 * @param loops SEFF Loops that shall be measured. Must not be {@code null} and must
	 *            be known to the Blackboard.
	 * @see de.uka.ipd.sdq.beagle.core.Blackboard#remeasureSEFFLoops(SEFFLoop...)
	 * @see #remeasureSEFFLoops(Collection)
	 */
	public void remeasureSEFFLoops(final SEFFLoop... loops) {
	}

	/**
	 * Delegates to
	 * {@link de.uka.ipd.sdq.beagle.core.Blackboard#remeasureSEFFLoops(Collection)} .
	 *
	 * @param loops SEFF Loops that shall be measured. Must not be {@code null} and must
	 *            be known to the Blackboard.
	 * @see de.uka.ipd.sdq.beagle.core.Blackboard#remeasureSEFFLoops(Collection)
	 * @see #remeasureSEFFLoops(SEFFLoop...)
	 */
	public void remeasureSEFFLoops(final Collection<SEFFLoop> loops) {
	}

	/**
	 * Delegates to {@link Blackboard#proposeExpressionsForRDIAs(Map)}.
	 *
	 * @param expressions A map which lists for each Resource Demanding Internal Action
	 *            some evaluable expression which are proposed to describe the resource
	 *            demanding internal action's measurement results. Must not be
	 *            {@code null} and no key or value of this map may be {@code null}. Not
	 *            each Resource Demanding Internal Action of this blackboard needs to be a
	 *            key in this map.
	 */
	public void proposeExpressionsForRDIAs(
		final Map<ResourceDemandingInternalAction, EvaluableExpression> expressions) {
	}

	/**
	 * Delegates to {@link Blackboard#proposeExpressionsForRDIAs(Map)}.
	 *
	 * @param expressions A map which lists for each SEFFBranch some evaluable expression
	 *            which are proposed to describe the SEFF Branch's measurement results.
	 *            Must not be {@code null} and no key or value of this map may be
	 *            {@code null}. Not each SEFFBranch of this blackboard needs to be a key
	 *            in this map.
	 */
	public void proposeExpressionsForSEFFBranches(final Map<SEFFBranch, EvaluableExpression> expressions) {
	}

	/**
	 * Delegates to {@link Blackboard#proposeExpressionsForSEFFLoops(Map)}.
	 *
	 * @param expressions A map which lists for each SEFF Loop some evaluable expression
	 *            which are proposed to describe the SEFF Loop's measurement results. Must
	 *            not be {@code null} and no key or value of this map may be {@code null}.
	 *            Not each SEFFLoop of this blackboard needs to be a key in this map.
	 */
	public void proposeExpressionsForSEFFLoops(final Map<SEFFLoop, EvaluableExpression> expressions) {
	}
}
