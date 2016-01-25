package de.uka.ipd.sdq.beagle.core.evaluableexpressions;
/**
 * ATTENTION: Test coverage check turned off. Remove this comments block when implementing
 * this class!
 * 
 * <p>COVERAGE:OFF
 */

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Collection;
import java.util.HashSet;

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
		Validate.noNullElements(factors);
		this.factors = new HashSet<>(factors);
	}

	/**
	 * Builds an expression that will return the sum of all {@code factors} on evaluation.
	 *
	 * @param factors The factors forming this expression’s product.
	 *            {@code factors.length} must at least be 2.
	 */
	public MultiplicationExpression(final EvaluableExpression... factors) {
		Validate.noNullElements(factors);
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
	
	@Override
	public String toString() {
		final StringBuilder result = new StringBuilder();
		result.append("(");
		boolean first = true;
		for (EvaluableExpression factor : this.factors) {
			if (!first)  {
				result.append(" * ");
			} else {
				first = false;
			}
			result.append(factor.toString());	
		}
		result.append(")");
		return result.toString();
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
		final MultiplicationExpression other = (MultiplicationExpression) object;
		return new EqualsBuilder().append(this.factors, other.factors).isEquals();
	}

	@Override
	public int hashCode() {
		// you pick a hard-coded, randomly chosen, non-zero, odd number
		// ideally different for each class
		return new HashCodeBuilder(223, 225).append(this.factors).toHashCode();
	}
}
