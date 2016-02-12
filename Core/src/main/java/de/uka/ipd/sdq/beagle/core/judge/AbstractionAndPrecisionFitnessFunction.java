package de.uka.ipd.sdq.beagle.core.judge;
/**
 * ATTENTION: Test coverage check turned off. Remove this comments block when implementing
 * this class!
 * 
 * <p>COVERAGE:OFF
 */

import de.uka.ipd.sdq.beagle.core.ExternalCallParameter;
import de.uka.ipd.sdq.beagle.core.ResourceDemandingInternalAction;
import de.uka.ipd.sdq.beagle.core.SeffBranch;
import de.uka.ipd.sdq.beagle.core.SeffLoop;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableVariableAssignment;
import de.uka.ipd.sdq.beagle.core.measurement.BranchDecisionMeasurementResult;
import de.uka.ipd.sdq.beagle.core.measurement.LoopRepetitionCountMeasurementResult;
import de.uka.ipd.sdq.beagle.core.measurement.ParameterChangeMeasurementResult;
import de.uka.ipd.sdq.beagle.core.measurement.ResourceDemandMeasurementResult;

import java.util.Set;

/**
 * Fitness function regarding abstract but precise expression as fittest. Naturally, these
 * requirements contradict each other. This fitness function grades the expression finding
 * the best compromise (reasonable abstracted but still precise enough to be meaningful)
 * best.
 *
 * @author Joshua Gleitze
 * @author Christoph Michelbach
 */
public class AbstractionAndPrecisionFitnessFunction implements EvaluableExpressionFitnessFunction {

	@Override
	public double gradeFor(final ResourceDemandingInternalAction rdia, final EvaluableExpression expression,
		final EvaluableExpressionFitnessFunctionBlackboardView blackboard) {

		final Set<ResourceDemandMeasurementResult> resourceDemandMeasurementResults =
			blackboard.getMeasurementResultsFor(rdia);

		// If there is no expression, return infinity.
		double meanSquareDeviation = Double.POSITIVE_INFINITY;

		for (ResourceDemandMeasurementResult resourceDemandMeasurementResult : resourceDemandMeasurementResults) {
			final double realValue = resourceDemandMeasurementResult.getValue();
			// final Parameterisation parameterisation =
			// resourceDemandMeasurementResult.getParameterisation();

			final EvaluableVariableAssignment evaluableVariableAssignment = new EvaluableVariableAssignment();

			final double predictedValue = expression.evaluate(evaluableVariableAssignment);

			final double squareDeviation = Math.pow(Math.abs(realValue) - Math.abs(predictedValue), 2);
			meanSquareDeviation += squareDeviation / resourceDemandMeasurementResults.size();
		}

		return meanSquareDeviation;
	}

	@Override
	public double gradeFor(final SeffBranch branch, final EvaluableExpression expression,
		final EvaluableExpressionFitnessFunctionBlackboardView blackboard) {

		final Set<BranchDecisionMeasurementResult> branchDecisionMeasurementResults =
			blackboard.getMeasurementResultsFor(branch);

		// If there is no expression, return infinity.
		double meanSquareDeviation = Double.POSITIVE_INFINITY;

		for (BranchDecisionMeasurementResult branchDecisionMeasurementResult : branchDecisionMeasurementResults) {
			final double realValue = branchDecisionMeasurementResult.getBranchIndex();
			// final Parameterisation parameterisation =
			// branchDecisionMeasurementResult.getParameterisation();

			final EvaluableVariableAssignment evaluableVariableAssignment = new EvaluableVariableAssignment();

			final double predictedValue = expression.evaluate(evaluableVariableAssignment);

			final double squareDeviation = Math.pow(Math.abs(realValue) - Math.abs(predictedValue), 2);
			meanSquareDeviation += squareDeviation / branchDecisionMeasurementResults.size();
		}

		return meanSquareDeviation;
	}

	@Override
	public double gradeFor(final SeffLoop loop, final EvaluableExpression expression,
		final EvaluableExpressionFitnessFunctionBlackboardView blackboard) {

		final Set<LoopRepetitionCountMeasurementResult> loopRepetitionCountMeasurementResults =
			blackboard.getMeasurementResultsFor(loop);

		// If there is no expression, return infinity.
		double meanSquareDeviation = Double.POSITIVE_INFINITY;

		for (LoopRepetitionCountMeasurementResult loopRepetitionCountMeasurementResult : loopRepetitionCountMeasurementResults) {
			final double realValue = loopRepetitionCountMeasurementResult.getCount();
			// final Parameterisation parameterisation =
			// loopRepetitionCountMeasurementResult.getParameterisation();

			final EvaluableVariableAssignment evaluableVariableAssignment = new EvaluableVariableAssignment();

			final double predictedValue = expression.evaluate(evaluableVariableAssignment);

			final double squareDeviation = Math.pow(Math.abs(realValue) - Math.abs(predictedValue), 2);
			meanSquareDeviation += squareDeviation / loopRepetitionCountMeasurementResults.size();
		}

		return meanSquareDeviation;
	}

	@Override
	public double gradeFor(final ExternalCallParameter parameter, final EvaluableExpression expression,
		final EvaluableExpressionFitnessFunctionBlackboardView blackboard) {

		final Set<ParameterChangeMeasurementResult> parameterChangeMeasurementResults =
			blackboard.getMeasurementResultsFor(parameter);

		// If there is no expression, return infinity.
		double meanSquareDeviation = Double.POSITIVE_INFINITY;

		for (ParameterChangeMeasurementResult parameterChangeMeasurementResult : parameterChangeMeasurementResults) {
			final double realValue = parameterChangeMeasurementResult.getCount();
			// final Parameterisation parameterisation =
			// parameterChangeMeasurementResult.getParameterisation();

			final EvaluableVariableAssignment evaluableVariableAssignment = new EvaluableVariableAssignment();

			final double predictedValue = expression.evaluate(evaluableVariableAssignment);

			final double squareDeviation = Math.pow(Math.abs(realValue) - Math.abs(predictedValue), 2);
			meanSquareDeviation += squareDeviation / parameterChangeMeasurementResults.size();
		}

		return meanSquareDeviation;
	}
}
