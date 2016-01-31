package de.uka.ipd.sdq.beagle.core.evaluableexpressions;

import org.apache.commons.collections4.MultiSet;
import org.apache.commons.collections4.multiset.HashMultiSet;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Expression that multiplies all its contained expressions.
 *
 * @author Joshua Gleitze
 */
public class MultiplicationExpression implements EvaluableExpression {

	/**
	 * The minimum number of summands needed to create a correct expression.
	 */
	private static final int MIN_FACTORS = 2;

	/**
	 * All factors of this expression as collection.
	 */
	private final MultiSet<EvaluableExpression> factors;

	/**
	 * Builds an expression that will return the product of all {@code factors} on
	 * evaluation.
	 *
	 * @param factors The factors forming this expression’s product.
	 *            {@code factors.size()} must at least be 2.
	 */
	public MultiplicationExpression(final Collection<EvaluableExpression> factors) {
		Validate.noNullElements(factors);
		Validate.isTrue(factors.size() >= MIN_FACTORS, "The expression must contain at least %d factors.", MIN_FACTORS);
		this.factors = new HashMultiSet<>(factors);
	}

	/**
	 * Builds an expression that will return the sum of all {@code factors} on evaluation.
	 *
	 * @param factors The factors forming this expression’s product.
	 *            {@code factors.length} must at least be 2.
	 */
	public MultiplicationExpression(final EvaluableExpression... factors) {
		this(Arrays.asList(factors));
	}

	/**
	 * Gets all factors (expressions that will be multiplied on evaluation).
	 *
	 * @return The expressions forming this expression’s product.
	 */
	public Collection<EvaluableExpression> getFactors() {
		return new ArrayList<>(this.factors);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.uka.ipd.sdq.beagle.core.expressions.EvaluableExpression#receive(de.uka.sdq.
	 * beagle. core.expressions.EvaluableExpressionVisitor)
	 */
	@Override
	public void receive(final EvaluableExpressionVisitor visitor) {
		Validate.notNull(visitor);
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
		Validate.notNull(variableAssignments);
		double product = 1;
		for (final EvaluableExpression factor : this.factors) {
			product *= factor.evaluate(variableAssignments);
		}
		return product;
	}

	@Override
	public String toString() {
		final StringBuilder result = new StringBuilder();
		result.append("(");
		boolean first = true;
		for (final EvaluableExpression factor : this.factors) {
			if (!first) {
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
