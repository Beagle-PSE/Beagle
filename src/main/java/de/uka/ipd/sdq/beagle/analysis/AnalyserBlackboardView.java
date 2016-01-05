package de.uka.ipd.sdq.beagle.analysis;

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
import java.util.Set;

/**
 * View of the {@link Blackboard} designed to be used by {@link ResultAnalyser}. Reading
 * information and adding proposed expressions to the {@link Blackboard} is supported.
 *
 * @author Christoph Michelbach
 * @author Roman Langrehr
 *
 * @see Blackboard for information about Blackboard views
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
	 * @return all {@linkplain ResourceDemandingInternalAction resource demanding internal
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
	 * Delegates to
	 * {@link de.uka.ipd.sdq.beagle.core.Blackboard#proposeExpressionFor(ResourceDemandingInternalAction, EvaluableExpression)}
	 * .
	 *
	 * @param rdia A resource demanding internal action. Must not be {@code null}.
	 * @param expression An evaluable expression proposed to describe {@code rdia}’s
	 *            measurement results. Must not be {@code null}.
	 * @see de.uka.ipd.sdq.beagle.core.Blackboard#proposeExpressionFor(ResourceDemandingInternalAction,
	 *      EvaluableExpression)
	 */
	public void proposeExpressionFor(final ResourceDemandingInternalAction rdia, final EvaluableExpression expression) {
	}

	/**
	 * Delegates to
	 * {@link de.uka.ipd.sdq.beagle.core.Blackboard#proposeExpressionFor(SEFFLoop, EvaluableExpression)}
	 * .
	 *
	 * @param loop A SEFF Loop. Must not be {@code null}.
	 * @param expression An evaluable expression proposed to describe {@code loop}’s
	 *            measurement results. Must not be {@code null}.
	 * @see de.uka.ipd.sdq.beagle.core.Blackboard#proposeExpressionFor(SEFFLoop,
	 *      EvaluableExpression)
	 */
	public void proposeExpressionFor(final SEFFLoop loop, final EvaluableExpression expression) {
	}

	/**
	 * Delegates to
	 * {@link de.uka.ipd.sdq.beagle.core.Blackboard#proposeExpressionFor(SEFFBranch, EvaluableExpression)}
	 * .
	 *
	 * @param branch A SEFF Branch. Must not be {@code null}.
	 * @param expression An evaluable expression proposed to describe {@code branch}’s
	 *            measurement results. Must not be {@code null}.
	 * @see de.uka.ipd.sdq.beagle.core.Blackboard#proposeExpressionFor(SEFFBranch,
	 *      EvaluableExpression)
	 */
	public void proposeExpressionFor(final SEFFBranch branch, final EvaluableExpression expression) {
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
	 * Delegates to
	 * {@link de.uka.ipd.sdq.beagle.core.Blackboard#addRDIAsToBeMeasured(ResourceDemandingInternalAction...)}
	 * .
	 *
	 * @param rdias Resource demanding internal actions that shall be measured. Must not
	 *            be {@code null} and must be known to the {@link Blackboard}.
	 * @see de.uka.ipd.sdq.beagle.core.Blackboard#addRDIAsToBeMeasured(ResourceDemandingInternalAction...)
	 */
	public void addRDIAsToBeMeasured(final ResourceDemandingInternalAction... rdias) {
	}

	/**
	 * Delegates to
	 * {@link de.uka.ipd.sdq.beagle.core.Blackboard#addRDIAsToBeMeasured(Collection)} .
	 *
	 * @param rdias Resource demanding internal actions that shall be measured. Must not
	 *            be {@code null} and must be known to the {@link Blackboard}.
	 * @see de.uka.ipd.sdq.beagle.core.Blackboard#addRDIAsToBeMeasured(Collection)
	 */
	public void addRDIAsToBeMeasured(final Collection<ResourceDemandingInternalAction> rdias) {
	}

	/**
	 * Delegates to
	 * {@link de.uka.ipd.sdq.beagle.core.Blackboard#addSEFFBranchesToBeMeasured(SEFFBranch...)}
	 * .
	 *
	 * @param branches SEFF branches that shall be measured. Must not be {@code null} and
	 *            must be known to the Blackboard.
	 * @see de.uka.ipd.sdq.beagle.core.Blackboard#addSEFFBranchesToBeMeasured(SEFFBranch...)
	 */
	public void addSEFFBranchesToBeMeasured(final SEFFBranch... branches) {
	}

	/**
	 * Delegates to
	 * {@link de.uka.ipd.sdq.beagle.core.Blackboard#addSEFFBranchesToBeMeasured(Collection)}
	 * .
	 *
	 * @param branches SEFF branches that shall be measured. Must not be {@code null} and
	 *            must be known to the Blackboard.
	 * @see de.uka.ipd.sdq.beagle.core.Blackboard#addSEFFBranchesToBeMeasured(Collection)
	 */
	public void addSEFFBranchesToBeMeasured(final Collection<SEFFBranch> branches) {
	}

	/**
	 * Delegates to
	 * {@link de.uka.ipd.sdq.beagle.core.Blackboard#addSEFFLoopsToBeMeasured(SEFFLoop...)}
	 * .
	 *
	 * @param loops SEFF Loops that shall be measured. Must not be {@code null} and must
	 *            be known to the Blackboard.
	 * @see de.uka.ipd.sdq.beagle.core.Blackboard#addSEFFLoopsToBeMeasured(SEFFLoop...)
	 */
	public void addSEFFLoopsToBeMeasured(final SEFFLoop... loops) {
	}

	/**
	 * Delegates to
	 * {@link de.uka.ipd.sdq.beagle.core.Blackboard#addSEFFLoopsToBeMeasured(Collection)}
	 * .
	 *
	 * @param loops SEFF Loops that shall be measured. Must not be {@code null} and must
	 *            be known to the Blackboard.
	 * @see de.uka.ipd.sdq.beagle.core.Blackboard#addSEFFLoopsToBeMeasured(Collection)
	 */
	public void addSEFFLoopsToBeMeasured(final Collection<SEFFLoop> loops) {
	}
}
