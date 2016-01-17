package de.uka.ipd.sdq.beagle.core.evaluableexpressions;

/**
 * Expression that divides its contained dividend through its contained divisor.
 *
 * @author Annika Berger
 *
 */
public class DivisionExpression implements EvaluableExpression {

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

	/**
	 * Gets the dividend of the expression.
	 *
	 * @return This expression's dividend.
	 */
	public EvaluableExpression getDividend() {
		return null;
	}

	/**
	 * Gets the divisor of the expression.
	 *
	 * @return This expression's divisor.
	 */
	public EvaluableExpression getDivisor() {
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

	/**
	 * Sets the {@link EvaluableExpression} which is the dividend.
	 *
	 * @param expression which is the dividendF.
	 */
	public void setDividend(final EvaluableExpression expression) {

	}

	/**
	 * Sets the {@link EvaluableExpression} which is the divisor.
	 *
	 * @param expression which is the divisor.
	 */
	public void setDivisor(final EvaluableExpression expression) {

	}

}
