package de.uka.ipd.sdq.beagle.core.evaluableexpressions;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Expression that divides its contained dividend through its contained divisor.
 *
 * @author Annika Berger
 *
 */
public class DivisionExpression implements EvaluableExpression {

	/**
	 * The divisor of this division expression.
	 */
	private final EvaluableExpression divisor;

	/**
	 * The dividend of this division expression.
	 */
	private final EvaluableExpression dividend;

	/**
	 * Builds an expression which returns the quotient of a division using the given
	 * divisor and dividend.
	 *
	 * @param dividend The expression being divided.
	 * @param divisor The expression dividing the other one.
	 */
	public DivisionExpression(final EvaluableExpression dividend, final EvaluableExpression divisor) {
		Validate.notNull(divisor);
		Validate.notNull(dividend);
		this.divisor = divisor;
		this.dividend = dividend;
	}

	/**
	 * Gets the divisor of the expression.
	 *
	 * @return This expression's divisor.
	 */
	public EvaluableExpression getDivisor() {
		return this.divisor;
	}

	/**
	 * Gets the dividend of the expression.
	 *
	 * @return This expression's dividend.
	 */
	public EvaluableExpression getDividend() {
		return this.dividend;
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
		final double quotient =
			this.dividend.evaluate(variableAssignments) / this.divisor.evaluate(variableAssignments);
		return quotient;
	}

	@Override
	public String toString() {
		return String.format("(%s / %s)", this.dividend, this.divisor);
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
		final DivisionExpression other = (DivisionExpression) object;
		return new EqualsBuilder().append(this.dividend, other.dividend).append(this.divisor, other.divisor).isEquals();
	}

	@Override
	public int hashCode() {
		// you pick a hard-coded, randomly chosen, non-zero, odd number
		// ideally different for each class
		return new HashCodeBuilder(195, 197).append(this.dividend).append(this.divisor).toHashCode();
	}
}
