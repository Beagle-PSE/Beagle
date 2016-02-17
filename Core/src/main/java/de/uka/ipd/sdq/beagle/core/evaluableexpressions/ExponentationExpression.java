package de.uka.ipd.sdq.beagle.core.evaluableexpressions;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Expression that potentises the given exponent to the given base.
 *
 * @author Annika Berger
 * @author Christoph Michelbach
 */
public class ExponentationExpression implements EvaluableExpression {

	/**
	 * States how long it takes to evaluate the expression for a computer. The bigger the
	 * number, the harder it is. The norm is addition which means that the
	 * {@code COMPUTATINOAL_COMPLEXITY} of addition is {@code 1}. Scaling is linear.
	 */
	public static final double COMPUTATINOAL_COMPLEXITY = 10d;

	/**
	 * States how hard it is for educated humans to understand the expression. The bigger
	 * the number, the harder it is. The norm is addition which means that the
	 * {@code HUMAN_UNDERSTANDABILITY_COMPXELITY} of addition is {@code 1}. Scaling is
	 * linear.
	 */
	public static final double HUMAN_UNDERSTANDABILITY_COMPXELITY = 12d;

	/**
	 * The {@link EvaluableExpression} which is the exponent of this expression.
	 */
	private final EvaluableExpression exponent;

	/**
	 * The {@link EvaluableExpression} which is the base of this expression.
	 */
	private final EvaluableExpression base;

	/**
	 * Build an expression which returns the base raised to the power of the exponent.
	 *
	 * @param exponent The expression which is the exponent of this expression. Must not
	 *            be {@code null}.
	 * @param base The expression which is the base of this expression. Must not be
	 *            {@code null}.
	 */
	public ExponentationExpression(final EvaluableExpression base, final EvaluableExpression exponent) {
		Validate.notNull(exponent);
		Validate.notNull(base);
		this.exponent = exponent;
		this.base = base;
	}

	/**
	 * Get the {@link EvaluableExpression} which is set as exponent.
	 *
	 * @return The expression's exponent.
	 */
	public EvaluableExpression getExponent() {
		return this.exponent;
	}

	/**
	 * Get the {@link EvaluableExpression} which is set as base.
	 *
	 * @return The expression's base.
	 */
	public EvaluableExpression getBase() {
		return this.base;
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
		return Math.pow(this.base.evaluate(variableAssignments), this.exponent.evaluate(variableAssignments));
	}

	@Override
	public String toString() {
		return String.format("(%s^%s)", this.base, this.exponent);
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
		final ExponentationExpression other = (ExponentationExpression) object;
		return new EqualsBuilder().append(this.base, other.base).append(this.exponent, other.exponent).isEquals();
	}

	@Override
	public int hashCode() {
		// you pick a hard-coded, randomly chosen, non-zero, odd number
		// ideally different for each class
		return new HashCodeBuilder(203, 205).append(this.base).append(this.exponent).toHashCode();
	}
}
