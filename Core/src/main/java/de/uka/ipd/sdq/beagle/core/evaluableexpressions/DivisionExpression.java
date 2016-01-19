package de.uka.ipd.sdq.beagle.core.evaluableexpressions;

/**
 * Expression that divides its contained dividend through its contained divisor.
 * 
 * @author Annika Berger
 *
 */
public class DivisionExpression implements EvaluableExpression {

	/**
	 * The divisor of this division expression.
	 */
	private EvaluableExpression divisor;

	/**
	 * The dividend of this division expression.
	 */
	private EvaluableExpression dividend;

	/**
	 * Builds an expression which returns the quotient of a division using the given
	 * divisor and dividend.
	 *
	 * @param divisor The expression dividing the other one.
	 * @param dividend The expression being divided.
	 */
	public DivisionExpression(final EvaluableExpression divisor, final EvaluableExpression dividend) {
		this.divisor = divisor;
		this.dividend = dividend;
	}

	/**
	 * Gets the divisor of the expression.
	 *
	 * @return This expression's divisor.
	 */
	public EvaluableExpression getDivisor() {
		return this.divisor;
	}

	/**
	 * Gets the dividend of the expression.
	 *
	 * @return This expression's dividend.
	 */
	public EvaluableExpression getDividend() {
		return this.dividend;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uka.ipd.sdq.beagle.core.expressions.EvaluableExpression#receive(de.uka.sdq.
	 * beagle. core.expressions.EvaluableExpressionVisitor)
	 */
	@Override
	public void receive(final EvaluableExpressionVisitor visitor) {
		visitor.visit(this);
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
		final double quotient = this.dividend.evaluate(variableAssignments) / this.divisor.evaluate(variableAssignments);
		return quotient;
	}

}
