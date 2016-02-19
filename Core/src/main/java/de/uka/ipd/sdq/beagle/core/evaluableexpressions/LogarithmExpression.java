package de.uka.ipd.sdq.beagle.core.evaluableexpressions;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Expression that executes a logarithm with defined expressions as base and
 * antilogarithm.
 *
 * @author Annika Berger
 */
public class LogarithmExpression implements EvaluableExpression {

	/**
	 * The Base of the expression.
	 */
	private final EvaluableExpression base;

	/**
	 * The antilogarithm of the expression.
	 */
	private final EvaluableExpression antilogarithm;

	/**
	 * Builds an expression which returns the result of logarithm of antilogarithm to the
	 * base.
	 *
	 * @param base The base of the logarithm. Must not be {@code null}.
	 * @param antilogarithm The antilogarithm of the expression. Must not be {@code null}.
	 */
	public LogarithmExpression(final EvaluableExpression base, final EvaluableExpression antilogarithm) {
		Validate.notNull(base);
		Validate.notNull(antilogarithm);
		this.antilogarithm = antilogarithm;
		this.base = base;
	}

	/**
	 * Get the {@link EvaluableExpression} which is the base of the logarithm.
	 *
	 * @return the base of the logarithm.
	 */
	public EvaluableExpression getBase() {
		return this.base;
	}

	/**
	 * Get the {@link EvaluableExpression} which is the antilogarithm, or parameter of the
	 * {@code LogarithmExpression}.
	 *
	 * @return the antilogarithm expression.s
	 */
	public EvaluableExpression getAntilogarithm() {
		return this.antilogarithm;
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
		return Math.log(this.antilogarithm.evaluate(variableAssignments))
			/ Math.log(this.base.evaluate(variableAssignments));
	}

	@Override
	public String toString() {
		return String.format("(log_%s%s)", this.base, this.antilogarithm);
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
		final LogarithmExpression other = (LogarithmExpression) object;
		return new EqualsBuilder().append(this.base, other.base)
			.append(this.antilogarithm, other.antilogarithm)
			.isEquals();
	}

	@Override
	public int hashCode() {
		// you pick a hard-coded, randomly chosen, non-zero, odd number
		// ideally different for each class
		return new HashCodeBuilder(219, 221).append(this.base).append(this.antilogarithm).toHashCode();
	}

}
