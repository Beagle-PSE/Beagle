package de.uka.ipd.sdq.beagle.core.judge;

import de.uka.ipd.sdq.beagle.core.ExternalCallParameter;
import de.uka.ipd.sdq.beagle.core.ResourceDemandingInternalAction;
import de.uka.ipd.sdq.beagle.core.SeffBranch;
import de.uka.ipd.sdq.beagle.core.SeffLoop;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpression;

/**
 * Fitness function regarding abstract but precise expression as fittest. Naturally, these
 * requirements contradict each other. This fitness function grades the expression finding
 * the best compromise (reasonable abstracted but still precise enough to be meaningful)
 * best.
 *
 * @author Joshua Gleitze
 */
public class AbstractionAndPrecisionFitnessFunction implements EvaluableExpressionFitnessFunction {

	@Override
	public double gradeFor(final ResourceDemandingInternalAction rdia, final EvaluableExpression expression,
		final EvaluableExpressionFitnessFunctionBlackboardView blackboard) {
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
