package de.uka.ipd.sdq.beagle.core.evaluableexpressions;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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
	 * @param ifStatement The expression which contains the if-statement. Must not be {@code null}.
	 * 
	 * @param thenStatement The expression which contains the then-statement. Must not be {@code null}.
	 * @param elseStatement The expression which contains the else-statement. Must not be {@code null}.
	 */
	public IfThenElseExpression(final EvaluableExpression ifStatement, final EvaluableExpression thenStatement,
		final EvaluableExpression elseStatement) {
		Validate.notNull(ifStatement);
		Validate.notNull(thenStatement);
		Validate.notNull(elseStatement);
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

	@Override
	public String toString() {
		return String.format("(%s ? %s : %s)", this.ifStatement, this.thenStatement, this.elseStatement);
	}

	@Override
	public boolean equals(final Object object) {
		if (object == null) {
			return false;
		}
		if (object == this) {
			return true;
		}
		if (object.getClass() != this.getClass()) {
			return false;
		}
		final IfThenElseExpression other = (IfThenElseExpression) object;
		return new EqualsBuilder().append(this.ifStatement, other.ifStatement)
			.append(this.thenStatement, other.thenStatement).append(this.elseStatement, other.elseStatement).isEquals();
	}

	@Override
	public int hashCode() {
		// you pick a hard-coded, randomly chosen, non-zero, odd number
		// ideally different for each class
		return new HashCodeBuilder(215, 217).append(this.ifStatement).append(this.thenStatement)
			.append(this.elseStatement).toHashCode();
	}

}
