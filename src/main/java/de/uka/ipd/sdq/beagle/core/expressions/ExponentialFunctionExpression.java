package de.uka.ipd.sdq.beagle.core.expressions;

/**
 * Expression that potentises the given exponent to the base e (Euler’s number).
 * 
 * @author Annika Berger
 *
 */
public class ExponentialFunctionExpression implements EvaluableExpression {

	/**
	 * Set an {@link EvaluableExpression} as exponent.
	 *
	 * @param expression Which is the exponent.
	 */
	public void setExponent(final EvaluableExpression expression) {

	}

	/**
	 * Get the {@link EvaluableExpression} which is set as exponent.
	 *
	 * @return The exponent.
	 */
	public EvaluableExpression getExponent() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.uka.ipd.sdq.beagle.core.expressions.EvaluableExpression#receive(de.uka.sdq.
	 * beagle. core.expressions.EvaluableExpressionVisitor)
	 */
	@Override
	public void receive(final EvaluableExpressionVisitor visitor) {

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uka.ipd.sdq.beagle.core.expressions.EvaluableExpression#evaluate(de.uka.sdq.
	 * beagle. core.expressions.EvaluableVariableAssignment)
	 */
	@Override
	public double evaluate(final EvaluableVariableAssignment variableAssignments) {
		return 0;
	}

}
