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
 * Expression that multiplies all its contained expressions.
 *
 * @author Joshua Gleitze
 */
public class MultiplicationExpression implements EvaluableExpression {
	/**
	 * Builds an expression that will return the product of all {@code factors} on
	 * evaluation.
	 *
	 * @param factors
	 *            The factors forming this expression’s product. {@code factors.size()}
	 *            must at least be 2.
	 */
	public MultiplicationExpression(final Collection<EvaluableExpression> factors) {
		// TODO: Implement method
	}

	/**
	 * Builds an expression that will return the sum of all {@code factors} on evaluation.
	 *
	 * @param factors
	 *            The factors forming this expression’s product. {@code factors.length}
	 *            must at least be 2.
	 */
	public MultiplicationExpression(final EvaluableExpression... factors) {
		// TODO: Implement method
	}

	/**
	 * Gets all factors (expressions that will be multiplied on evaluation).
	 *
	 * @return The expressions forming this expression’s product.
	 */
	public EvaluableExpression[] getFactors() {
		// TODO: Implement method
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
	public double evaluate(EvaluableVariableAssignment variableAssignments) {
		// TODO: Implement method
		return 0;
	}
}
