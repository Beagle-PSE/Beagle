package de.uka.ipd.sdq.beagle.core.evaluableexpressions;

import java.util.Collection;

/**
 * Expression that sums up all its contained expressions.
 *
 * @author Joshua Gleitze
 */
public class AdditionExpression implements EvaluableExpression {

	/**
	 * Builds an expression that will return the sum of all {@code summands} on
	 * evaluation.
	 *
	 * @param summands The summands forming this expression’s sum. {@code summands.size()}
	 *            must at least be 2.
	 */
	public AdditionExpression(final Collection<EvaluableExpression> summands) {
	}

	/**
	 * Builds an expression that will return the sum of all {@code summands} on
	 * evaluation.
	 *
	 * @param summands The summands forming this expression’s sum. {@code summands.length}
	 *            must at least be 2.
	 */
	public AdditionExpression(final EvaluableExpression... summands) {
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
