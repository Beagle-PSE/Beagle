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
 * Expression that sums up all its contained expressions.
 *
 * @author Joshua Gleitze
 * @author Christoph Michelbach
 */
public class AdditionExpression implements EvaluableExpression {

	/**
	 * States how long it takes to evaluate the expression for a computer. The bigger the
	 * number, the harder it is. The norm is addition which means that the
	 * {@code COMPUTATINOAL_COMPLEXITY} of addition is {@code 1}. Scaling is linear.
	 */
	public static final double COMPUTATINOAL_COMPLEXITY = 1d;

	/**
	 * States how hard it is for educated humans to understand the expression. The bigger
	 * the number, the harder it is. The norm is addition which means that the
	 * {@code HUMAN_UNDERSTANDABILITY_COMPXELITY} of addition is {@code 1}. Scaling is
	 * linear.
	 */
	public static final double HUMAN_UNDERSTANDABILITY_COMPXELITY = 1d;

	/**
	 * The minimum number of summands needed to create a correct expression.
	 */
	private static final int MIN_SUMMANDS = 2;

	/**
	 * All summands of this evaluable expression.
	 */
	private final MultiSet<EvaluableExpression> summands;

	/**
	 * Builds an expression that will return the sum of all {@code summands} on
	 * evaluation.
	 *
	 * @param summands The summands forming this expression’s sum. {@code summands.size()}
	 *            must at least be 2.
	 */
	public AdditionExpression(final Collection<EvaluableExpression> summands) {
		Validate.noNullElements(summands);
		Validate.isTrue(summands.size() >= MIN_SUMMANDS, "The expression must contain at least %d summands.",
			MIN_SUMMANDS);
		this.summands = new HashMultiSet<>(summands);
	}

	/**
	 * Builds an expression that will return the sum of all {@code summands} on
	 * evaluation.
	 *
	 * @param summands The summands forming this expression’s sum. {@code summands.length}
	 *            must at least be 2.
	 */
	public AdditionExpression(final EvaluableExpression... summands) {
		this(Arrays.asList(summands));
	}

	/**
	 * Get all evaluable expressions which are forming this expression's sum. There have
	 * to be at least two.
	 *
	 * @return A collection of all summands of this addition expression.
	 */
	public Collection<EvaluableExpression> getSummands() {
		return new ArrayList<>(this.summands);
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
		double result = 0;
		double value;
		for (final EvaluableExpression summand : this.summands) {
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
		for (final EvaluableExpression summand : this.summands) {
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
