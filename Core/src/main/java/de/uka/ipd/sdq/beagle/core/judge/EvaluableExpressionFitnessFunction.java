package de.uka.ipd.sdq.beagle.core.judge;

import de.uka.ipd.sdq.beagle.core.ExternalCallParameter;
import de.uka.ipd.sdq.beagle.core.MeasurableSeffElement;
import de.uka.ipd.sdq.beagle.core.ResourceDemandingInternalAction;
import de.uka.ipd.sdq.beagle.core.SeffBranch;
import de.uka.ipd.sdq.beagle.core.SeffLoop;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpression;

/**
 * Judges {@link EvaluableExpression EvaluableExpressions} for their fitness to describe
 * measured parametric dependencies. What the expression exactly describes depends on the
 * {@linkplain MeasurableSeffElement} it belongs to. Implements the well known strategy
 * design pattern.
 *
 * @author Christoph Michelbach
 * @author Joshua Gleitze
 */
public interface EvaluableExpressionFitnessFunction {

	/**
	 * Judges how well {@code expression} fits to describe the measurement results of
	 * {@code rdia}.
	 *
	 * @param rdia A resource demanding internal action.
	 * @param expression A expression proposed to describe {@code rdia}’s measurement
	 *            results.
	 * @param blackboard Beagle’s blackboard instance.
	 * @return A value judging how well {@code expression} fits to describe the
	 *         measurement results of {@code rdia}. Will be a value between 0 and
	 *         {@link Double#MAX_VALUE}. The lower the value, the better the fitness.
	 */
	double gradeFor(ResourceDemandingInternalAction rdia, EvaluableExpression expression,
		EvaluableExpressionFitnessFunctionBlackboardView blackboard);

	/**
	 * Judges how well {@code expression} fits to describe the measurement results of
	 * {@code branch}.
	 *
	 * @param branch A SEFF Branch.
	 * @param expression A expression proposed to describe {@code branch}’s measurement
	 *            results.
	 * @param blackboard Beagle’s blackboard instance.
	 * @return A value judging how well {@code expression} fits to describe the
	 *         measurement results of {@code branch}. Will be a value between 0 and
	 *         {@link Double#MAX_VALUE}. The lower the value, the better the fitness.
	 */
	double gradeFor(SeffBranch branch, EvaluableExpression expression,
		EvaluableExpressionFitnessFunctionBlackboardView blackboard);

	/**
	 * Judges how well {@code expression} fits to describe the measurement results of
	 * {@code loop}.
	 *
	 * @param loop A SEFF Loop.
	 * @param expression A expression proposed to describe {@code loop}’s measurement
	 *            results.
	 * @param blackboard Beagle’s blackboard instance.
	 * @return A value judging how well {@code expression} fits to describe the
	 *         measurement results of {@code loop}. Will be a value between 0 and
	 *         {@link Double#MAX_VALUE}. The lower the value, the better the fitness.
	 */
	double gradeFor(SeffLoop loop, EvaluableExpression expression,
		EvaluableExpressionFitnessFunctionBlackboardView blackboard);

	/**
	 * Judges how well {@code expression} fits to describe the measurement results of
	 * {@code parameter}.
	 *
	 * @param parameter An external call parameter.
	 * @param expression A expression proposed to describe {@code parameter}’s measurement
	 *            results.
	 * @param blackboard Beagle’s blackboard instance.
	 * @return A value judging how well {@code expression} fits to describe the
	 *         measurement results of {@code parameter}. Will be a value between 0 and
	 *         {@link Double#MAX_VALUE}. The lower the value, the better the fitness.
	 */
	double gradeFor(ExternalCallParameter parameter, EvaluableExpression expression,
		EvaluableExpressionFitnessFunctionBlackboardView blackboard);
}
