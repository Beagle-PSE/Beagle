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
	 * Defines how important human-comprehensibility is compared to low computational
	 * complexity. Values are ϵ [0, 1]. The importance of computational complexity is
	 * {@code 1 - HUMAN_COMPREHENSIBILITY_VALUE}.
	 */
	private static final double HUMAN_COMPREHENSIBILITY_VALUE = .5d;

	/**
	 * Defines how important human-comprehensibility and low computational complexity are
	 * compared to precision. The importance of precision is {@code 1 - NICE_VALUE}.
	 */
	private static final double NICE_VALUE = .2d;

	/**
	 * Norms the human-comprehensibility.
	 */
	private static final double HUMAN_COMPREHENSIBILITY_NORMATION = .01d;

	/**
	 * Norms the computational complexity.
	 */
	private static final double COMPUTATIONIONAL_COMPLEXITY_NORMATION = .01d;

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

		return this.determineFitnessValue(expression, meanSquareDeviation);
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

		return this.determineFitnessValue(expression, meanSquareDeviation);
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

		return this.determineFitnessValue(expression, meanSquareDeviation);
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

		return this.determineFitnessValue(expression, meanSquareDeviation);
	}

	/**
	 * Determines the fitness value of {@code expression}.
	 *
	 * @param expression The {@link EvaluableExpression}.
	 * @param meanSquareDeviation The mean square deviation of {@code expression} from the
	 *            measured values.
	 * @return The fitness value of {@code expression}.
	 */
	private double determineFitnessValue(final EvaluableExpression expression, final double meanSquareDeviation) {
		final EvaluableExpressionComplexityAnalyser evaluableExpressionComplexityAnalyser =
			new EvaluableExpressionComplexityAnalyser();
		evaluableExpressionComplexityAnalyser.determineComplexity(expression);

		final double humanComprehensibilityComplexity =
			evaluableExpressionComplexityAnalyser.getHumanComprehensibilityComplexitySum()
				* HUMAN_COMPREHENSIBILITY_NORMATION;
		final double computationalComplexity = evaluableExpressionComplexityAnalyser.getComputationalComplexitySum()
			* COMPUTATIONIONAL_COMPLEXITY_NORMATION;

		final double combinedComplexity = HUMAN_COMPREHENSIBILITY_VALUE * humanComprehensibilityComplexity
			+ (1 - HUMAN_COMPREHENSIBILITY_VALUE) * computationalComplexity;

		return NICE_VALUE * combinedComplexity + (1 - NICE_VALUE) * meanSquareDeviation;
	}
}
