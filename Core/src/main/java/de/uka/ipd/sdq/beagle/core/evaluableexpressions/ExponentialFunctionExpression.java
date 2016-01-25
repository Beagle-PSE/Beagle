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

/**
 * Expression that potentises the given exponent to the base e (Euler’s number).
 *
 * @author Annika Berger
 *
 */
public class ExponentialFunctionExpression implements EvaluableExpression {

	/**
	 * The {@link EvaluableExpression} which is the exponent of this expression.
	 */
	private EvaluableExpression exponent;

	/**
	 * Builds an expression which returns e raised to the power of the exponent.
	 *
	 * @param exponent The expression which is the exponent of this expression. Must not be {@code null}.
	 */
	public ExponentialFunctionExpression(final EvaluableExpression exponent) {
		Validate.notNull(exponent);
		this.exponent = exponent;
	}

	/**
	 * Get the {@link EvaluableExpression} which is set as exponent.
	 *
	 * @return The expression's exponent.
	 */
	public EvaluableExpression getExponent() {
		return this.exponent;
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
		return Math.pow(Math.E, this.exponent.evaluate(variableAssignments));
	}
	
	@Override
	public String toString() {
		return String.format("(e^%s)", this.exponent);
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
		final ExponentialFunctionExpression other = (ExponentialFunctionExpression) object;
		return new EqualsBuilder().append(this.exponent, other.exponent).isEquals();
	}

	@Override
	public int hashCode() {
		// you pick a hard-coded, randomly chosen, non-zero, odd number
		// ideally different for each class
		return new HashCodeBuilder(207, 209).append(this.exponent).toHashCode();
	}

}
