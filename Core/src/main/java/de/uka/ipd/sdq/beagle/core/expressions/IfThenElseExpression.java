package de.uka.ipd.sdq.beagle.core.expressions;

/**
 * Expression that executes an if-then-else-statement based on its contained expressions.
 * 
 * @author Annika Berger
 */
public class IfThenElseExpression implements EvaluableExpression {

	/**
	 * Get expression contained in if-statement.
	 * 
	 * @return if-Expression
	 */
	public EvaluableExpression getIfExpression() {
		return null;
	}

	/**
	 * Set the {@link EvaluableExpression} to be contained in the if statement.
	 *
	 * @param ifExpression The {@link EvaluableExpression} to be contained in the if
	 *            statement.
	 */
	public void setIfExpression(final EvaluableExpression ifExpression) {
	}

	/**
	 * Get expression contained in else-statement.
	 * 
	 * @return else-Expression
	 */
	public EvaluableExpression getElseExpression() {
		return null;
	}

	/**
	 * Set the {@link EvaluableExpression} to be contained in the else-statement.
	 *
	 * @param elseExpression The {@link EvaluableExpression} to be contained in the
	 *            else-statement.
	 */
	public void setElseExpression(final EvaluableExpression elseExpression) {
	}

	/**
	 * Get expression contained in then-statement.
	 * 
	 * @return then-expression
	 */
	public EvaluableExpression getThenExpression() {
		return null;
	}

	/**
	 * Set the {@link EvaluableExpression} to be contained in the then-statement.
	 *
	 * @param thenExpression The {@link EvaluableExpression} to be contained in the
	 *            then-statement.
	 */
	public void setThenExpression(final EvaluableExpression thenExpression) {
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
