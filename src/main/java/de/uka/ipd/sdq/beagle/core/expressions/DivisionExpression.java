package de.uka.ipd.sdq.beagle.core.expressions;

/**
 * Expression that divides its contained dividend through its contained divisor.
 * 
 * @author Annika Berger
 *
 */
public class DivisionExpression implements EvaluableExpression {

	/**
	 * Sets the {@link EvaluableExpression} which is the divisor.
	 *
	 * @param expression which is the divisor.
	 */
	public void setDivisor(final EvaluableExpression expression) {

	}

	/**
	 * Gets the divisor of the expression.
	 *
	 * @return the divisor
	 */
	public EvaluableExpression getDivisor() {
		return null;
	}
	/**
	 * Sets the {@link EvaluableExpression} which is the dividend.
	 *
	 * @param expression which is the dividendF.
	 */
	public void setDividend(final EvaluableExpression expression) {
		
	}
	
	/**
	 * Gets the dividend of the expression.
	 *
	 * @return the dividend
	 */
	public EvaluableExpression getDividend() {
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
