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
import de.uka.ipd.sdq.beagle.core.measurement.Parameterisation;
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
		for (ResourceDemandMeasurementResult resourceDemandMeasurementResult : resourceDemandMeasurementResults) {
			final double realValue = resourceDemandMeasurementResult.getValue();
			final Parameterisation parameterisation = resourceDemandMeasurementResult.getParameterisation();

			final EvaluableVariableAssignment evaluableVariableAssignment = new EvaluableVariableAssignment();

			final double predictedValue = expression.evaluate(evaluableVariableAssignment);

			final double squareDeviation = Math.pow(Math.abs(realValue) - Math.abs(predictedValue), 2);
			return squareDeviation;
		}
		return 0;
	}

	@Override
	public double gradeFor(final SeffBranch branch, final EvaluableExpression expression,
		final EvaluableExpressionFitnessFunctionBlackboardView blackboard) {
		return 0;
	}

	@Override
	public double gradeFor(final SeffLoop loop, final EvaluableExpression expression,
		final EvaluableExpressionFitnessFunctionBlackboardView blackboard) {
		return 0;
	}

	@Override
	public double gradeFor(final ExternalCallParameter parameter, final EvaluableExpression expression,
		final EvaluableExpressionFitnessFunctionBlackboardView blackboard) {
		return 0;
	}

}
