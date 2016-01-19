package de.uka.ipd.sdq.beagle.core.evaluableexpressions;

/**
 * Expression that potentises the given exponent to the base e (Euler’s number).
 * 
 * @author Annika Berger
 *
 */
public class ExponentialFunctionExpression implements EvaluableExpression {

	/**
	 * The {@link EvaluableExpression} which is the exponent of this expression.
	 */
	private EvaluableExpression exponent;

	/**
	 * Builds an expression which returns e raised to the power of the exponent.
	 *
	 * @param exponent The expression which is the exponent of this expression.
	 */
	public ExponentialFunctionExpression(final EvaluableExpression exponent) {
		this.exponent = exponent;
	}

	/**
	 * Get the {@link EvaluableExpression} which is set as exponent.
	 *
	 * @return The expression's exponent.
	 */
	public EvaluableExpression getExponent() {
		return this.exponent;
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
		return Math.pow(Math.E, this.exponent.evaluate(variableAssignments));
	}

}
