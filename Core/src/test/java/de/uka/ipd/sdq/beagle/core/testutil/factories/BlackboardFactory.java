package de.uka.ipd.sdq.beagle.core.testutil.factories;

import de.uka.ipd.sdq.beagle.core.Blackboard;
import de.uka.ipd.sdq.beagle.core.ExternalCallParameter;
import de.uka.ipd.sdq.beagle.core.MeasurableSeffElement;
import de.uka.ipd.sdq.beagle.core.ResourceDemandingInternalAction;
import de.uka.ipd.sdq.beagle.core.SeffBranch;
import de.uka.ipd.sdq.beagle.core.SeffLoop;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpression;
import de.uka.ipd.sdq.beagle.core.measurement.BranchDecisionMeasurementResult;
import de.uka.ipd.sdq.beagle.core.measurement.LoopRepetitionCountMeasurementResult;
import de.uka.ipd.sdq.beagle.core.measurement.ParameterChangeMeasurementResult;
import de.uka.ipd.sdq.beagle.core.measurement.ResourceDemandMeasurementResult;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Factory for prepared Blackboard instances to be used by tests.
 *
 * @author Joshua Gleitze
 * @author Ansgar Spiegler
 */
public class BlackboardFactory {

	/**
	 * A {@link ResourceDemandingInternalAction} factory to easily obtain new instances
	 * from.
	 */
	private static final ResourceDemandingInternalActionFactory RDIA_FACTORY =
		new ResourceDemandingInternalActionFactory();

	/**
	 * A {@link SeffBranch} factory to easily obtain new instances from.
	 */
	private static final SeffBranchFactory SEFF_BRANCH_FACTORY = new SeffBranchFactory();

	/**
	 * A {@link SeffLoop} factory to easily obtain new instances from.
	 */
	private static final SeffLoopFactory SEFF_LOOP_FACTORY = new SeffLoopFactory();

	/**
	 * A {@link ExternalCallParameter} factory to easily obtain new instances from.
	 */
	private static final ExternalCallParameterFactory EXTERNAL_CALL_PARAMETER_FACTORY =
		new ExternalCallParameterFactory();

	/**
	 * A {@link EvaluableExpressionFitnessFunction} factory to easily obtain new instances
	 * from.
	 */
	private static final EvaluableExpressionFitnessFunctionFactory FITNESS_FUNCTION_FACTORY =
		new EvaluableExpressionFitnessFunctionFactory();

	/**
	 * A {@link EvaluableExpression} factory to easily obtain new instances from.
	 */
	private static final EvaluableExpressionFactory EVALUABLE_EXPRESSION_FACTORY = new EvaluableExpressionFactory();

	/**
	 * A Meausurement result factory to easily obtain new instances from.
	 */
	private static final MeasurementResultFactory MEAUSUREMENT_RESULT_FACTORY = new MeasurementResultFactory();

	/**
	 * Creates a new blackboard with nothing written on it.
	 *
	 * @return A new blackboard instance, without any data on it.
	 */
	public Blackboard getEmpty() {
		return new Blackboard(new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>(),
			FITNESS_FUNCTION_FACTORY.getOne());
	}

	/**
	 * Creates a new blackboard, filled with all MeasurableSeffElements and
	 * ToBeMeasurement Content.
	 *
	 * @return A new blackboard instance with data.
	 */
	public Blackboard getWithToBeMeasuredContent() {
		final Set<ResourceDemandingInternalAction> rdiaSet = RDIA_FACTORY.getAllAsSet();
		final Set<SeffBranch> seffBranchSet = SEFF_BRANCH_FACTORY.getAllAsSet();
		final Set<SeffLoop> seffLoopSet = SEFF_LOOP_FACTORY.getAllAsSet();
		final Set<ExternalCallParameter> externalCallParameterSet = EXTERNAL_CALL_PARAMETER_FACTORY.getAllAsSet();

		final Blackboard blackboard = new Blackboard(rdiaSet, seffBranchSet, seffLoopSet, externalCallParameterSet,
			FITNESS_FUNCTION_FACTORY.getOne());
		blackboard.addToBeMeasuredExternalCallParameters(externalCallParameterSet);
		blackboard.addToBeMeasuredRdias(rdiaSet);
		blackboard.addToBeMeasuredSeffBranches(seffBranchSet);
		blackboard.addToBeMeasuredSeffLoops(seffLoopSet);

		return blackboard;
	}

	/**
	 * Creates a new blackboard, filled with all possible data.
	 *
	 * @return A new blackboard instance with data.
	 */
	public Blackboard getFull() {
		final Blackboard blackboard = this.getWithToBeMeasuredContent();

		Iterator<ResourceDemandingInternalAction> blackboardRdias = blackboard.getAllRdias().iterator();
		for (final ResourceDemandMeasurementResult meausurementResult : MEAUSUREMENT_RESULT_FACTORY.getRdiaResults()) {
			blackboard.addMeasurementResultFor(blackboardRdias.next(), meausurementResult);
			if (!blackboardRdias.hasNext()) {
				blackboardRdias = blackboard.getAllRdias().iterator();
			}
		}

		Iterator<SeffLoop> blackboardLoops = blackboard.getAllSeffLoops().iterator();
		for (final LoopRepetitionCountMeasurementResult meausurementResult : MEAUSUREMENT_RESULT_FACTORY
			.getLoopResults()) {
			blackboard.addMeasurementResultFor(blackboardLoops.next(), meausurementResult);
			if (!blackboardLoops.hasNext()) {
				blackboardLoops = blackboard.getAllSeffLoops().iterator();
			}
		}

		Iterator<SeffBranch> blackboardBranches = blackboard.getAllSeffBranches().iterator();
		for (final BranchDecisionMeasurementResult meausurementResult : MEAUSUREMENT_RESULT_FACTORY
			.getBranchResults()) {
			blackboard.addMeasurementResultFor(blackboardBranches.next(), meausurementResult);
			if (!blackboardBranches.hasNext()) {
				blackboardBranches = blackboard.getAllSeffBranches().iterator();
			}
		}

		Iterator<ExternalCallParameter> blackboardParameters = blackboard.getAllExternalCallParameters().iterator();
		for (final ParameterChangeMeasurementResult meausurementResult : MEAUSUREMENT_RESULT_FACTORY
			.getParameterResults()) {
			blackboard.addMeasurementResultFor(blackboardParameters.next(), meausurementResult);
			if (!blackboardParameters.hasNext()) {
				blackboardParameters = blackboard.getAllExternalCallParameters().iterator();
			}
		}

		boolean first = true;
		for (final EvaluableExpression expression : EVALUABLE_EXPRESSION_FACTORY.getAll()) {
			for (final MeasurableSeffElement seffElement : this.getAllSeffElements(blackboard)) {
				blackboard.addProposedExpressionFor(seffElement, expression);
			}
			if (first) {
				for (final MeasurableSeffElement seffElement : this.getAllSeffElements(blackboard)) {
					blackboard.setFinalExpressionFor(seffElement, expression);
				}
				first = false;
			}
		}
		return blackboard;
	}

	/**
	 * Joins all Seff Elements stored on a blackboard.
	 *
	 * @param blackboard A blackboard to get seff elements from
	 * @return All seff elements stored on {@code blackboard}.
	 */
	public Set<MeasurableSeffElement> getAllSeffElements(final Blackboard blackboard) {
		final Set<MeasurableSeffElement> result = new HashSet<>();
		result.addAll(blackboard.getAllExternalCallParameters());
		result.addAll(blackboard.getAllRdias());
		result.addAll(blackboard.getAllSeffBranches());
		result.addAll(blackboard.getAllSeffLoops());
		return result;
	}

}
