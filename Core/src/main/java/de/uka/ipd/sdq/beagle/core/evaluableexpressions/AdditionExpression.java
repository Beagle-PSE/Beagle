package de.uka.ipd.sdq.beagle.core.evaluableexpressions;

import java.util.Collection;

/**
 * Expression that sums up all its contained expressions.
 *
 * @author Joshua Gleitze
 */
public class AdditionExpression implements EvaluableExpression {

	/**
	 * All summands of this evaluable expression.
	 */
	private Collection<EvaluableExpression> summands;

	/**
	 * Builds an expression that will return the sum of all {@code summands} on
	 * evaluation.
	 *
	 * @param summands The summands forming this expression’s sum. {@code summands.size()}
	 *            must at least be 2.
	 */
	public AdditionExpression(final Collection<EvaluableExpression> summands) {
		this.summands = summands;
	}

	/**
	 * Builds an expression that will return the sum of all {@code summands} on
	 * evaluation.
	 *
	 * @param summands The summands forming this expression’s sum. {@code summands.length}
	 *            must at least be 2.
	 */
	public AdditionExpression(final EvaluableExpression... summands) {
		for (EvaluableExpression summand : summands) {
			this.summands.add(summand);
		}

	}

	/**
	 * Get all evaluable expressions which are forming this expression's sum. There have
	 * to be at least two.
	 *
	 * @return A collection of all summands of this addition expression.
	 */
	public Collection<EvaluableExpression> getSummands() {
		return this.summands;
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
		double result = 0;
		double value;
		for (EvaluableExpression summand : this.summands) {
			value = summand.evaluate(variableAssignments);
			result += value;
		}
		return result;
	}
}
