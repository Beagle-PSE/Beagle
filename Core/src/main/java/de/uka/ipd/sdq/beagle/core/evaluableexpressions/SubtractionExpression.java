package de.uka.ipd.sdq.beagle.core.evaluableexpressions;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Expression that subtracts a subtrahend from a minuend.
 *
 * @author Annika Berger
 *
 */
public class SubtractionExpression implements EvaluableExpression {

	/**
	 * The subtrahend of the expression.
	 */
	private final EvaluableExpression subtrahend;

	/**
	 * The minuend of the expressions.
	 */
	private final EvaluableExpression minuend;

	/**
	 * Builds an expression which returns the difference of subtrahend and minuend.
	 *
	 * @param subtrahend The subtrahend of the expression. Must not be {@code null}.
	 * @param minuend The minuend of the expression. Must not be {@code null}.
	 */
	public SubtractionExpression(final EvaluableExpression minuend, final EvaluableExpression subtrahend) {
		Validate.notNull(subtrahend);
		Validate.notNull(minuend);
		this.subtrahend = subtrahend;
		this.minuend = minuend;
	}

	/**
	 * Get the {@link EvaluableExpression} which is the subtrahend.
	 *
	 * @return This expression's subtrahend.
	 */
	public EvaluableExpression getSubtrahend() {
		return this.subtrahend;
	}

	/**
	 * Get the {@link EvaluableExpression} which is the minuend.
	 *
	 * @return This expression's minuend.
	 */
	public EvaluableExpression getMinuend() {
		return this.minuend;
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
		return this.minuend.evaluate(variableAssignments) - this.subtrahend.evaluate(variableAssignments);
	}

	@Override
	public String toString() {
		return String.format("(%s - %s)", this.minuend, this.subtrahend);
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
		final SubtractionExpression other = (SubtractionExpression) object;
		return new EqualsBuilder().append(this.minuend, other.minuend).append(this.subtrahend, other.subtrahend)
			.isEquals();
	}

	@Override
	public int hashCode() {
		// you pick a hard-coded, randomly chosen, non-zero, odd number
		// ideally different for each class
		return new HashCodeBuilder(235, 237).append(this.minuend).append(this.subtrahend).toHashCode();
	}

}
