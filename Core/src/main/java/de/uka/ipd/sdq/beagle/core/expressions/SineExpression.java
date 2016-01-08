package de.uka.ipd.sdq.beagle.core.expressions;

/**
 * Expression that executes a sine function on its contained expression.
 * 
 * @author Annika Berger
 *
 */
public class SineExpression implements EvaluableExpression {

	/**
	 * Set an {@link EvaluableExpression} to be the argument of the sine function.
	 *
	 * @param expression The expression to be the argument.
	 */
	public void setArgument(final EvaluableExpression expression) {

	}

	/**
	 * Get the {@link EvaluableExpression} which is the argument of the sine function.
	 *
	 * @return The argument of the function.
	 */
	public EvaluableExpression getArgument() {
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
