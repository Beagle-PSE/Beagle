package de.uka.ipd.sdq.beagle.core.evaluableexpressions;

/**
 * Expression that executes an if-then-else-statement based on its contained expressions.
 * 
 * @author Annika Berger
 */
public class IfThenElseExpression implements EvaluableExpression {

	/**
	 * The expression containing the if-statement.
	 */
	private EvaluableExpression ifStatement;

	/**
	 * The expression containing the else-statement.
	 */
	private EvaluableExpression elseStatement;

	/**
	 * The expression containing the then-statement.
	 */
	private EvaluableExpression thenStatement;

	/**
	 * Builds an expression which returns .
	 *
	 * @param ifStatement The expression which contains the if-statement.
	 * 
	 * @param thenStatement The expression which contains the then-statement.
	 * @param elseStatement The expression which contains the else-statement.
	 */
	public IfThenElseExpression(final EvaluableExpression ifStatement, final EvaluableExpression thenStatement,
		final EvaluableExpression elseStatement) {
		this.ifStatement = ifStatement;
		this.elseStatement = elseStatement;
		this.thenStatement = thenStatement;

	}

	/**
	 * Get expression contained in if-statement.
	 * 
	 * @return if-Expression
	 */
	public EvaluableExpression getIfStatement() {
		return this.ifStatement;
	}

	/**
	 * Get expression contained in else-statement.
	 * 
	 * @return else-Expression
	 */
	public EvaluableExpression getElseStatement() {
		return this.elseStatement;
	}

	/**
	 * Get expression contained in then-statement.
	 * 
	 * @return then-expression
	 */
	public EvaluableExpression getThenStatement() {
		return this.thenStatement;
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
		if (this.ifStatement.evaluate(variableAssignments) == 0) {
			return this.thenStatement.evaluate(variableAssignments);
		} else {
			return this.elseStatement.evaluate(variableAssignments);
		}
	}

}
