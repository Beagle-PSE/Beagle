package de.uka.ipd.sdq.beagle.core.evaluableexpressions;

import java.util.Collection;

/**
 * Expression that multiplies all its contained expressions.
 *
 * @author Joshua Gleitze
 */
public class MultiplicationExpression implements EvaluableExpression {

	/**
	 * All factors of this expression as collection.
	 */
	private Collection<EvaluableExpression> factors;

	/**
	 * Builds an expression that will return the product of all {@code factors} on
	 * evaluation.
	 *
	 * @param factors The factors forming this expression’s product.
	 *            {@code factors.size()} must at least be 2.
	 */
	public MultiplicationExpression(final Collection<EvaluableExpression> factors) {
		this.factors = factors;
	}

	/**
	 * Builds an expression that will return the sum of all {@code factors} on evaluation.
	 *
	 * @param factors The factors forming this expression’s product.
	 *            {@code factors.length} must at least be 2.
	 */
	public MultiplicationExpression(final EvaluableExpression... factors) {
		for (EvaluableExpression factor : factors) {
			this.factors.add(factor);
		}
	}

	/**
	 * Gets all factors (expressions that will be multiplied on evaluation).
	 *
	 * @return The expressions forming this expression’s product.
	 */
	public Collection<EvaluableExpression> getFactors() {
		return this.factors;
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
		double product = 1;
		for (EvaluableExpression factor : this.factors) {
			product *= factor.evaluate(variableAssignments);
		}
		return product;
	}
}
