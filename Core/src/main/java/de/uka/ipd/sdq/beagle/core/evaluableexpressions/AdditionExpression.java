package de.uka.ipd.sdq.beagle.core.evaluableexpressions;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Expression that sums up all its contained expressions.
 *
 * @author Joshua Gleitze
 */
public class AdditionExpression implements EvaluableExpression {

	/**
	 * All summands of this evaluable expression.
	 */
	private Set<EvaluableExpression> summands;

	/**
	 * Builds an expression that will return the sum of all {@code summands} on
	 * evaluation.
	 *
	 * @param summands The summands forming this expression’s sum. {@code summands.size()}
	 *            must at least be 2.
	 */
	public AdditionExpression(final Collection<EvaluableExpression> summands) {
		Validate.noNullElements(summands);
		this.summands = new HashSet<>(summands);
	}

	/**
	 * Builds an expression that will return the sum of all {@code summands} on
	 * evaluation.
	 *
	 * @param summands The summands forming this expression’s sum. {@code summands.length}
	 *            must at least be 2.
	 */
	public AdditionExpression(final EvaluableExpression... summands) {
		Validate.noNullElements(summands);
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

	@Override
	public String toString() {
		final StringBuilder result = new StringBuilder();
		result.append("(");
		boolean first = true;
		for (EvaluableExpression summand : this.summands) {
			if (!first) {
				result.append(" + ");
			} else {
				first = false;
			}
			result.append(summand.toString());
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
		final AdditionExpression other = (AdditionExpression) object;
		return new EqualsBuilder().append(this.summands, other.summands).isEquals();
	}

	@Override
	public int hashCode() {
		// you pick a hard-coded, randomly chosen, non-zero, odd number
		// ideally different for each class
		return new HashCodeBuilder(999, 393).append(this.summands).toHashCode();
	}
}
