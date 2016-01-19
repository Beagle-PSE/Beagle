package de.uka.ipd.sdq.beagle.core.evaluableexpressions;

/**
 * Expression that potentises the given exponent to the given base.
 * 
 * @author Annika Berger
 */
public class ExponentationExpression implements EvaluableExpression {

	/**
	 * The {@link EvaluableExpression} which is the exponent of this expression.
	 */
	private EvaluableExpression exponent;

	/**
	 * The {@link EvaluableExpression} which is the base of this expression.
	 */
	private EvaluableExpression base;

	/**
	 * Build an expression which returns the base raised to the power of the exponent.
	 *
	 * @param exponent The expression which is the exponent of this expression.
	 * @param base The expression which is the base of this expression.
	 */
	public ExponentationExpression(final EvaluableExpression exponent, final EvaluableExpression base) {
		this.exponent = exponent;
		this.base = base;
	}

	/**
	 * Get the {@link EvaluableExpression} which is set as exponent.
	 *
	 * @return The expression's exponent.
	 */
	public EvaluableExpression getExponent() {
		return this.exponent;
	}

	/**
	 * Get the {@link EvaluableExpression} which is set as base.
	 *
	 * @return The expression's base.
	 */
	public EvaluableExpression getBase() {
		return this.base;
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
		return Math.pow(this.base.evaluate(variableAssignments), this.exponent.evaluate(variableAssignments));		
	}

}
