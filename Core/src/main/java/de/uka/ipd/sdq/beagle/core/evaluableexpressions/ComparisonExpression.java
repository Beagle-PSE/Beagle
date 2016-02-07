package de.uka.ipd.sdq.beagle.core.evaluableexpressions;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Expression that compares both its contained expressions.
 *
 * @author Annika Berger
 *
 */
public class ComparisonExpression implements EvaluableExpression {
	
	
	/**
	 * The used value to express {@code true} as double.
	 */
	private static final double TRUE = 1.0;

	/**
	 * The {@link EvaluableExpression} which is supposed to be the smaller one in the
	 * comparison.
	 */
	private final EvaluableExpression smaller;

	/**
	 * The {@link EvaluableExpression} which is supposed to be the greater one in the
	 * comparison.
	 */
	private final EvaluableExpression greater;

	/**
	 * Builds an expression that will return '1' if the expression set as smaller is
	 * smaller and '0' else.
	 *
	 * @param smaller The {@link EvaluableExpression} which is supposed to be the smaller
	 *            one in the comparison.
	 * @param greater The {@link EvaluableExpression} which is supposed to be the greater
	 *            one in the comparison.
	 */
	public ComparisonExpression(final EvaluableExpression smaller, final EvaluableExpression greater) {
		Validate.notNull(smaller);
		Validate.notNull(greater);
		this.smaller = smaller;
		this.greater = greater;
	}

	/**
	 * Get the {@link EvaluableExpression} which is supposed to be the greater one in the
	 * comparison.
	 *
	 * <p>For example: if {@code e1 < e2} should be expressed, {@code e1} is set as
	 * smaller and {@code e2} as greater expression.
	 *
	 * @return the greater expression
	 */
	public EvaluableExpression getGreater() {
		return this.greater;
	}

	/**
	 * Get the {@link EvaluableExpression} which is supposed to be the smaller one in the
	 * comparison.
	 *
	 * <p>For example: if {@code e1 < e2} should be expressed, {@code e1} is set as
	 * smaller and {@code e2} as greater expression.
	 *
	 * @return the smaller expression
	 */
	public EvaluableExpression getSmaller() {
		return this.smaller;
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
		if (this.smaller.evaluate(variableAssignments) < this.greater.evaluate(variableAssignments)) {
			return TRUE;
		} else {
			return FALSE;
		}
	}

	@Override
	public String toString() {
		return String.format("(%s < %s)", this.smaller, this.greater);
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
		final ComparisonExpression other = (ComparisonExpression) object;
		return new EqualsBuilder().append(this.smaller, other.smaller).append(this.greater, other.greater).isEquals();
	}

	@Override
	public int hashCode() {
		// you pick a hard-coded, randomly chosen, non-zero, odd number
		// ideally different for each class
		return new HashCodeBuilder(839, 39901).append(this.smaller).append(this.greater).hashCode();
	}

}
