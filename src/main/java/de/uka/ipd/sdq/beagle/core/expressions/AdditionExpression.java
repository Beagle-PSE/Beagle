package de.uka.ipd.sdq.beagle.core.expressions;

import java.util.Collection;

/*
 * ATTENTION: Checkstyle turned off!
 * remove this comment block when implementing this class!
 *
 * CHECKSTYLE:OFF
 *
 * TODO
 */

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
	 * @param summands
	 *            The summands forming this expression’s sum. {@code summands.size()} must
	 *            at least be 2.
	 */
	public AdditionExpression(final Collection<EvaluableExpression> summands) {
		// TODO: Implement method
	}

	/**
	 * Builds an expression that will return the sum of all {@code summands} on
	 * evaluation.
	 *
	 * @param summands
	 *            The summands forming this expression’s sum. {@code summands.length} must
	 *            at least be 2.
	 */
	public AdditionExpression(final EvaluableExpression... summands) {
		// TODO: Implement method
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.uka.ipd.sdq.beagle.core.expressions.EvaluableExpression#receive(de.uka.sdq.
	 * beagle. core.expressions.EvaluableExpressionVisitor)
	 */
	@Override
	public void receive(final EvaluableExpressionVisitor visitor) {
		// TODO: Implement method
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
		// TODO: Implement method
		return 0;
	}
}
