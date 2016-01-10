package de.uka.ipd.sdq.beagle.core.analysis;

import de.uka.ipd.sdq.beagle.core.Blackboard;
import de.uka.ipd.sdq.beagle.core.BlackboardStorer;
import de.uka.ipd.sdq.beagle.core.ResourceDemandingInternalAction;
import de.uka.ipd.sdq.beagle.core.SEFFBranch;
import de.uka.ipd.sdq.beagle.core.SEFFLoop;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpression;
import de.uka.ipd.sdq.beagle.core.measurement.BranchDecisionMeasurementResult;
import de.uka.ipd.sdq.beagle.core.measurement.ResourceDemandMeasurementResult;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

/**
 * View of the {@link Blackboard} designed to be used by {@link ResultAnalyser}. Reading
 * information and adding proposed expressions to the {@link Blackboard} is supported.
 *
 * @author Christoph Michelbach
 * @author Roman Langrehr
 *
 * @see Blackboard for information about Blackboard views.
 *
 */
public class AnalyserBlackboardView {

	/**
	 * Creates a new {@code AnalyserBlackboardView} for {@code blackboard}.
	 *
	 * @param blackboard The {@link Blackboard} to be accessed through the
	 *            {@code AnalyserBlackboardView}.
	 */
	public AnalyserBlackboardView(final Blackboard blackboard) {

	}

	/**
	 * Delegates to {@link de.uka.ipd.sdq.beagle.core.Blackboard#getAllRDIAs()}.
	 *
	 * @return All {@linkplain ResourceDemandingInternalAction resource demanding internal
	 *         actions} known to Beagle. Changes to the returned set will not modify the
	 *         blackboard content. Is never {@code null}.
	 * @see de.uka.ipd.sdq.beagle.core.Blackboard#getAllRDIAs()
	 */
	public Set<ResourceDemandingInternalAction> getAllRDIAs() {
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
	 * @param rdia A resource demanding internal action to get the measurement results of.
	 *            Must not be {@code null}.
	 * @return All measurement results reported for {@code rdia}. Changes to the returned
	 *         set will not modify the blackboard content. Is never {@code null}.
	 * @see de.uka.ipd.sdq.beagle.core.Blackboard#getMeasurementResultsFor(ResourceDemandingInternalAction)
	 */
	public Set<ResourceDemandMeasurementResult> getMeasurementResultsFor(final ResourceDemandingInternalAction rdia) {
		return null;
	}

	/**
	 * Delegates to {@link de.uka.ipd.sdq.beagle.core.Blackboard#getAllSEFFBranches()}.
	 *
	 * @return All {@linkplain SEFFBranch SEFF branches} known to Beagle. Changes to the
	 *         returned set will not modify the blackboard content. Is never {@code null}.
	 * @see de.uka.ipd.sdq.beagle.core.Blackboard#getAllSEFFBranches()
	 */
	public Set<SEFFBranch> getAllSEFFBranches() {
		return null;
	}

	/**
	 * Delegates to {@link de.uka.ipd.sdq.beagle.core.Blackboard#getAllSEFFLoops()}.
	 *
	 * @return All {@linkplain SEFFLoop SEFF loops} known to Beagle. Changes to the
	 *         returned set will not modify the blackboard content. Is never {@code null}.
	 * @see de.uka.ipd.sdq.beagle.core.Blackboard#getAllSEFFLoops()
	 */
	public Set<SEFFLoop> getAllSEFFLoops() {
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
	public void proposeExpressionForRDIA(final ResourceDemandingInternalAction rdia,
		final EvaluableExpression expression) {
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
	public void proposeExpressionForSEFFLoop(final SEFFLoop loop, final EvaluableExpression expression) {
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
	public void proposeExpressionForSEFFBranch(final SEFFBranch branch, final EvaluableExpression expression) {
	}

	/**
	 * Delegates to
	 * {@link de.uka.ipd.sdq.beagle.core.Blackboard#addToBeMeasuredRDIAs(ResourceDemandingInternalAction...)}
	 * .
	 *
	 * @param rdias Resource demanding internal actions that shall be measured. Must not
	 *            be {@code null} and must be known to the {@link Blackboard}.
	 * @see de.uka.ipd.sdq.beagle.core.Blackboard#addToBeMeasuredRDIAs(ResourceDemandingInternalAction...)
	 * @see #remeasureRDIAs(Collection)
	 */
	public void remeasureRDIAs(final ResourceDemandingInternalAction... rdias) {
	}

	/**
	 * Delegates to
	 * {@link de.uka.ipd.sdq.beagle.core.Blackboard#addToBeMeasuredRDIAs(Collection)} .
	 *
	 * @param rdias Resource demanding internal actions that shall be measured. Must not
	 *            be {@code null} and must be known to the {@link Blackboard}.
	 * @see de.uka.ipd.sdq.beagle.core.Blackboard#addToBeMeasuredRDIAs(Collection)
	 * @see #remeasureRDIAs(ResourceDemandingInternalAction...)
	 */
	public void remeasureRDIAs(final Collection<ResourceDemandingInternalAction> rdias) {
	}

	/**
	 * Delegates to
	 * {@link de.uka.ipd.sdq.beagle.core.Blackboard#addToBeMeasuredSEFFBranches(SEFFBranch...)}
	 * .
	 *
	 * @param branches SEFF branches that shall be measured. Must not be {@code null} and
	 *            must be known to the Blackboard.
	 * @see de.uka.ipd.sdq.beagle.core.Blackboard#addToBeMeasuredSEFFBranches(SEFFBranch...)
	 * @see #remeasureSEFFBranches(Collection)
	 */
	public void remeasureSEFFBranches(final SEFFBranch... branches) {
	}

	/**
	 * Delegates to
	 * {@link de.uka.ipd.sdq.beagle.core.Blackboard#addToBeMeasuredSEFFBranches(Collection)} .
	 *
	 * @param branches SEFF branches that shall be measured. Must not be {@code null} and
	 *            must be known to the Blackboard.
	 * @see de.uka.ipd.sdq.beagle.core.Blackboard#addToBeMeasuredSEFFBranches(Collection)
	 * @see #remeasureSEFFBranches(SEFFBranch...)
	 */
	public void remeasureSEFFBranches(final Collection<SEFFBranch> branches) {
	}

	/**
	 * Delegates to
	 * {@link de.uka.ipd.sdq.beagle.core.Blackboard#addToBeMeasuredSEFFLoops(SEFFLoop...)} .
	 *
	 * @param loops SEFF Loops that shall be measured. Must not be {@code null} and must
	 *            be known to the Blackboard.
	 * @see de.uka.ipd.sdq.beagle.core.Blackboard#addToBeMeasuredSEFFLoops(SEFFLoop...)
	 * @see #remeasureSEFFLoops(Collection)
	 */
	public void remeasureSEFFLoops(final SEFFLoop... loops) {
	}

	/**
	 * Delegates to
	 * {@link de.uka.ipd.sdq.beagle.core.Blackboard#addToBeMeasuredSEFFLoops(Collection)} .
	 *
	 * @param loops SEFF Loops that shall be measured. Must not be {@code null} and must
	 *            be known to the Blackboard.
	 * @see de.uka.ipd.sdq.beagle.core.Blackboard#addToBeMeasuredSEFFLoops(Collection)
	 * @see #remeasureSEFFLoops(SEFFLoop...)
	 */
	public void remeasureSEFFLoops(final Collection<SEFFLoop> loops) {
	}
}
