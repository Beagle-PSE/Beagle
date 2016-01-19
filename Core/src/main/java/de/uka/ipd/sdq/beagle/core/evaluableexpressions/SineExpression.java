package de.uka.ipd.sdq.beagle.core.evaluableexpressions;

/**
 * Expression that executes a sine function on its contained expression.
 * 
 * @author Annika Berger
 *
 */
public class SineExpression implements EvaluableExpression {

	/**
	 * The argument of the sine expression.
	 */
	private EvaluableExpression argument;

	/**
	 * Builds an expression which returns the sine of the argument.
	 *
	 * @param argument The argument to  be used.
	 */
	public SineExpression(final EvaluableExpression argument) {
		this.argument = argument;
	}

	/**
	 * Get the {@link EvaluableExpression} which is the argument of the sine function.
	 *
	 * @return The argument of the function.
	 */
	public EvaluableExpression getArgument() {
		return this.argument;
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
		return Math.sin(this.argument.evaluate(variableAssignments));
	}

}
