package de.uka.ipd.sdq.beagle.core.analysis;

import de.uka.ipd.sdq.beagle.core.Blackboard;
import de.uka.ipd.sdq.beagle.core.BlackboardStorer;
import de.uka.ipd.sdq.beagle.core.EvaluableExpressionFitnessFunction;
import de.uka.ipd.sdq.beagle.core.ExternalCallParameter;
import de.uka.ipd.sdq.beagle.core.MeasurableSeffElement;
import de.uka.ipd.sdq.beagle.core.ResourceDemandingInternalAction;
import de.uka.ipd.sdq.beagle.core.SeffBranch;
import de.uka.ipd.sdq.beagle.core.SeffLoop;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpression;
import de.uka.ipd.sdq.beagle.core.measurement.BranchDecisionMeasurementResult;
import de.uka.ipd.sdq.beagle.core.measurement.ParameterChangeMeasurementResult;
import de.uka.ipd.sdq.beagle.core.measurement.ResourceDemandMeasurementResult;

import java.io.Serializable;
import java.util.Set;

/**
 * Read-only view of the {@link Blackboard} designed to be used by
 * {@link MeasurementResultAnalyser} . It allows reading access and adding access for
 * {@linkplain ResourceDemandingInternalAction resource demanding internal actions},
 * {@linkplain SeffBranch SEFF branches}, {@linkplain SeffLoop SEFF loops},
 * {@linkplain ExternalCallParameter external call parameters}, reading, writing, and the
 * fitness function.
 *
 * @author Christoph Michelbach
 */
public class ReadOnlyProposedExpressionAnalyserBlackboardView {

	/**
	 * Delegates to {@link de.uka.ipd.sdq.beagle.core.Blackboard#getAllSeffBranches()}.
	 *
	 * @return all {@linkplain ResourceDemandingInternalAction resource demanding internal
	 *         actions} known to Beagle. Changes to the returned set will not modify the
	 *         blackboard content. Is never {@code null}.
	 */
	public Set<ResourceDemandingInternalAction> getAllRdias() {
		return null;
	}

	/**
	 * Delegates to {@link de.uka.ipd.sdq.beagle.core.Blackboard#getAllSeffBranches()}.
	 *
	 * @return all {@linkplain SeffBranch SEFF branches} known to Beagle. Changes to the
	 *         returned set will not modify the blackboard content. Is never {@code null}.
	 */
	public Set<SeffBranch> getAllSeffBranches() {
		return null;
	}

	/**
	 * Delegates to {@link de.uka.ipd.sdq.beagle.core.Blackboard#getAllSeffLoops()}.
	 *
	 * @return all {@linkplain SeffLoop SEFF loops} known to Beagle. Changes to the
	 *         returned set will not modify the blackboard content. Is never {@code null}.
	 */
	public Set<SeffLoop> getAllSeffLoops() {
		return null;
	}

	/**
	 * Delegates to
	 * {@link de.uka.ipd.sdq.beagle.core.Blackboard#getAllExternalCallParameters()}.
	 *
	 * @return All {@linkplain ExternalCallParameter external call parameters} known to
	 *         Beagle. Is never {@code null}.
	 */
	public Set<ExternalCallParameter> getAllExternalCallParameters() {
		return null;
	}

	/**
	 * Delegates to
	 * {@link de.uka.ipd.sdq.beagle.core.Blackboard#getMeasurementResultsFor(ResourceDemandingInternalAction)}
	 * .
	 *
	 * @param rdia An resource demanding internal action to get the measurement results
	 *            of. Must not be {@code null}.
	 * @return All measurement results reported for {@code rdia}. Changes to the returned
	 *         set will not modify the blackboard content. Is never {@code null}.
	 * @see de.uka.ipd.sdq.beagle.core.Blackboard#getMeasurementResultsFor(ResourceDemandingInternalAction)
	 */
	public Set<ResourceDemandMeasurementResult> getMeasurementResultsFor(final ResourceDemandingInternalAction rdia) {
		return null;
	}

	/**
	 * Delegates to
	 * {@link de.uka.ipd.sdq.beagle.core.Blackboard#getMeasurementResultsFor(SeffBranch)}.
	 *
	 * @param branch A SEFF Branch to get the measurement results of. Must not be
	 *            {@code null}.
	 * @return All measurement results reported for {@code branch}. Changes to the
	 *         returned set will not modify the blackboard content. Is never {@code null}.
	 * @see de.uka.ipd.sdq.beagle.core.Blackboard#getMeasurementResultsFor(SeffBranch)
	 */
	public Set<BranchDecisionMeasurementResult> getMeasurementResultsFor(final SeffBranch branch) {
		return null;
	}

	/**
	 * Delegates to
	 * {@link de.uka.ipd.sdq.beagle.core.Blackboard#getMeasurementResultsFor(SeffLoop)}.
	 *
	 * @param loop A SEFF Loop to get the measurement results of. Must not be {@code null}
	 *            .
	 * @return All measurement results reported for {@code loop}. Changes to the returned
	 *         set will not modify the blackboard content. Is never {@code null}.
	 * @see de.uka.ipd.sdq.beagle.core.Blackboard#getMeasurementResultsFor(SeffLoop)
	 */
	public Set<ResourceDemandMeasurementResult> getMeasurementResultsFor(final SeffLoop loop) {
		return null;
	}

	/**
	 * Delegates to
	 * {@link de.uka.ipd.sdq.beagle.core.Blackboard#getMeasurementResultsFor(ExternalCallParameter)}
	 * .
	 *
	 * @param externalCallParameter An external parameter to get the measurement results
	 *            of. Must not be {@code null}.
	 * @return All measurement results reported for {@code loexternalCallParameterop}.
	 *         Changes to the returned set will not modify the blackboard content. Is
	 *         never {@code null}.
	 * @see de.uka.ipd.sdq.beagle.core.Blackboard#getMeasurementResultsFor(ExternalCallParameter)
	 */
	public Set<ParameterChangeMeasurementResult> getMeasurementResultsFor(
		final ExternalCallParameter externalCallParameter) {
		return null;
	}

	/**
	 * Delegates to
	 * {@link de.uka.ipd.sdq.beagle.core.Blackboard#getProposedlExpressionFor(MeasurableSeffElement)}
	 * .
	 *
	 * @param element A SEFF element. Must not be {@code null}.
	 */
	public void getProposedlExpressionFor(final MeasurableSeffElement element) {
	}

	/**
	 * Delegates to {@link de.uka.ipd.sdq.beagle.core.Blackboard#getFitnessFunction()} .
	 *
	 * @return An object which holds and is responsible allows access to the fitness
	 *         function grading {@linkplain EvaluableExpression evaluable expressions}
	 *         regarding their fitness.
	 * @see de.uka.ipd.sdq.beagle.core.Blackboard#getFitnessFunction()
	 */
	public EvaluableExpressionFitnessFunction getFitnessFunction() {
		return null;
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

}
