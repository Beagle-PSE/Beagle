package de.uka.ipd.sdq.beagle.core.expressions;

/**
 * Expression that potentises the given exponent to the given base.
 * 
 * @author Annika Berger
 */
public class ExponentationExpression implements EvaluableExpression {

	/**
	 * Set an {@link EvaluableExpression} as exponent.
	 *
	 * @param expression which is the exponent
	 */
	public void setExponent(final EvaluableExpression expression) {

	}

	/**
	 * Set an {@link EvaluableExpression} as base.
	 *
	 * @param expression which is the base.
	 */
	public void setBase(final EvaluableExpression expression) {

	}

	/**
	 * Get the {@link EvaluableExpression} which is set as exponent.
	 *
	 * @return the exponent
	 */
	public EvaluableExpression getExponent() {
		return null;
	}

	/**
	 * Get the {@link EvaluableExpression} which is set as base.
	 *
	 * @return the base
	 */
	public EvaluableExpression getBase() {
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
