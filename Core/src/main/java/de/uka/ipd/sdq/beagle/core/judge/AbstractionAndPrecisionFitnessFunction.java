package de.uka.ipd.sdq.beagle.core.judge;

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

import org.apache.commons.lang3.Validate;

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

	/**
	 * How much more valuable human-readability is compared to low computational
	 * complexity. The higher the value, the more important human-readability is.
	 */
	private static final double HUMAN_READABLITY_COEFFICIENT = 7d;

	/**
	 * How bad deviation is seen. The higher the value, the worse deviation is seen.
	 */
	private static final double DEMANDED_PRECISION_COEFFICIENT = 10000d;

	@Override
	public double gradeFor(final ResourceDemandingInternalAction rdia, final EvaluableExpression expression,
		final EvaluableExpressionFitnessFunctionBlackboardView blackboard) {
		Validate.notNull(rdia);
		Validate.notNull(expression);
		Validate.notNull(blackboard);

		final Set<ResourceDemandMeasurementResult> resourceDemandMeasurementResults =
			blackboard.getMeasurementResultsFor(rdia);

		// If there is no expression, return infinity.
		if (resourceDemandMeasurementResults.size() == 0) {
			return Double.POSITIVE_INFINITY;
		}

		double meanSquareDeviation = 0;

		for (final ResourceDemandMeasurementResult resourceDemandMeasurementResult : resourceDemandMeasurementResults) {
			final double realValue = resourceDemandMeasurementResult.getValue();
			// final Parameterisation parameterisation =
			// resourceDemandMeasurementResult.getParameterisation();

			final EvaluableVariableAssignment evaluableVariableAssignment = new EvaluableVariableAssignment();

			final double predictedValue = expression.evaluate(evaluableVariableAssignment);

			final double squareDeviation = Math.pow(Math.abs(realValue) - Math.abs(predictedValue), 2);
			meanSquareDeviation += squareDeviation / resourceDemandMeasurementResults.size();
		}

		final double numericComplexity = this.determineNumericComplexity(expression);

		return meanSquareDeviation + numericComplexity / DEMANDED_PRECISION_COEFFICIENT;
	}

	@Override
	public double gradeFor(final SeffBranch branch, final EvaluableExpression expression,
		final EvaluableExpressionFitnessFunctionBlackboardView blackboard) {
		Validate.notNull(branch);
		Validate.notNull(expression);
		Validate.notNull(blackboard);

		final Set<BranchDecisionMeasurementResult> branchDecisionMeasurementResults =
			blackboard.getMeasurementResultsFor(branch);

		// If there is no expression, return infinity.
		if (branchDecisionMeasurementResults.size() == 0) {
			return Double.POSITIVE_INFINITY;
		}

		double meanSquareDeviation = 0;

		for (final BranchDecisionMeasurementResult branchDecisionMeasurementResult : branchDecisionMeasurementResults) {
			final double realValue = branchDecisionMeasurementResult.getBranchIndex();
			// final Parameterisation parameterisation =
			// branchDecisionMeasurementResult.getParameterisation();

			final EvaluableVariableAssignment evaluableVariableAssignment = new EvaluableVariableAssignment();

			final double predictedValue = expression.evaluate(evaluableVariableAssignment);

			final double squareDeviation = Math.pow(Math.abs(realValue) - Math.abs(predictedValue), 2);
			meanSquareDeviation += squareDeviation / branchDecisionMeasurementResults.size();
		}

		final double numericComplexity = this.determineNumericComplexity(expression);

		return meanSquareDeviation + numericComplexity / DEMANDED_PRECISION_COEFFICIENT;
	}

	@Override
	public double gradeFor(final SeffLoop loop, final EvaluableExpression expression,
		final EvaluableExpressionFitnessFunctionBlackboardView blackboard) {
		Validate.notNull(loop);
		Validate.notNull(expression);
		Validate.notNull(blackboard);

		final Set<LoopRepetitionCountMeasurementResult> loopRepetitionCountMeasurementResults =
			blackboard.getMeasurementResultsFor(loop);

		// If there is no expression, return infinity.
		if (loopRepetitionCountMeasurementResults.size() == 0) {
			return Double.POSITIVE_INFINITY;
		}

		double meanSquareDeviation = 0;

		// @formatter:off
		for (final LoopRepetitionCountMeasurementResult loopRepetitionCountMeasurementResult
			: loopRepetitionCountMeasurementResults) {
			// @formatter:on
			final double realValue = loopRepetitionCountMeasurementResult.getCount();
			// final Parameterisation parameterisation =
			// loopRepetitionCountMeasurementResult.getParameterisation();

			final EvaluableVariableAssignment evaluableVariableAssignment = new EvaluableVariableAssignment();

			final double predictedValue = expression.evaluate(evaluableVariableAssignment);

			final double squareDeviation = Math.pow(Math.abs(realValue) - Math.abs(predictedValue), 2);
			meanSquareDeviation += squareDeviation / loopRepetitionCountMeasurementResults.size();
		}

		final double numericComplexity = this.determineNumericComplexity(expression);

		return meanSquareDeviation + numericComplexity / DEMANDED_PRECISION_COEFFICIENT;
	}

	@Override
	public double gradeFor(final ExternalCallParameter parameter, final EvaluableExpression expression,
		final EvaluableExpressionFitnessFunctionBlackboardView blackboard) {
		Validate.notNull(parameter);
		Validate.notNull(expression);
		Validate.notNull(blackboard);

		final Set<ParameterChangeMeasurementResult> parameterChangeMeasurementResults =
			blackboard.getMeasurementResultsFor(parameter);

		// If there is no expression, return infinity.
		if (parameterChangeMeasurementResults.size() == 0) {
			return Double.POSITIVE_INFINITY;
		}

		double meanSquareDeviation = 0;

		for (final ParameterChangeMeasurementResult parameterChangeMeasurementResult : parameterChangeMeasurementResults) {
			final double realValue = parameterChangeMeasurementResult.getCount();
			// final Parameterisation parameterisation =
			// parameterChangeMeasurementResult.getParameterisation();

			final EvaluableVariableAssignment evaluableVariableAssignment = new EvaluableVariableAssignment();

			final double predictedValue = expression.evaluate(evaluableVariableAssignment);

			final double squareDeviation = Math.pow(Math.abs(realValue) - Math.abs(predictedValue), 2);
			meanSquareDeviation += squareDeviation / parameterChangeMeasurementResults.size();
		}

		final double numericComplexity = this.determineNumericComplexity(expression);

		return meanSquareDeviation + numericComplexity / DEMANDED_PRECISION_COEFFICIENT;
	}

	/**
	 * Determines the numeric complexity for {@code expression}.
	 *
	 * @param expression The {@link EvaluableExpression}.
	 * @return The numeric complexity of {@code expression}.
	 */
	private double determineNumericComplexity(final EvaluableExpression expression) {
		final Complexity complexity = new Complexity();
		complexity.determineComplexity(expression);

		return complexity.getComputationalComplexitySum()
			+ complexity.getHumanComprehensibilityComplexitySum() * HUMAN_READABLITY_COEFFICIENT;

	}
}
