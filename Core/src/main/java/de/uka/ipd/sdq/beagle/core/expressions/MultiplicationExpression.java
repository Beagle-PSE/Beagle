package de.uka.ipd.sdq.beagle.core.expressions;

import java.util.Collection;

/**
 * Expression that multiplies all its contained expressions.
 *
 * @author Joshua Gleitze
 */
public class MultiplicationExpression implements EvaluableExpression {

	/**
	 * Builds an expression that will return the product of all {@code factors} on
	 * evaluation.
	 *
	 * @param factors The factors forming this expression’s product.
	 *            {@code factors.size()} must at least be 2.
	 */
	public MultiplicationExpression(final Collection<EvaluableExpression> factors) {
	}

	/**
	 * Builds an expression that will return the sum of all {@code factors} on evaluation.
	 *
	 * @param factors The factors forming this expression’s product.
	 *            {@code factors.length} must at least be 2.
	 */
	public MultiplicationExpression(final EvaluableExpression... factors) {
	}

	/**
	 * Gets all factors (expressions that will be multiplied on evaluation).
	 *
	 * @return The expressions forming this expression’s product.
	 */
	public EvaluableExpression[] getFactors() {
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
