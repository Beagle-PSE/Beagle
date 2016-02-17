package de.uka.ipd.sdq.beagle.core.evaluableexpressions;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Expression that executes the natural logarithmic function. The antilogarithm is a
 * contained {@link EvaluableExpression} and the base is e (Euler’s number).
 *
 * @author Annika Berger
 * @author Christoph Michelbach
 */
public class NaturalLogarithmExpression implements EvaluableExpression {

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
	public static final double HUMAN_UNDERSTANDABILITY_COMPXELITY = 17d;

	/**
	 * The antilogarithm of the expression.
	 */
	private final EvaluableExpression antilogarithm;

	/**
	 * Builds an expression which returns the result of a logarithm of the antilogarithm
	 * to the base.
	 *
	 * @param antilogarithm The antilogarithm for this expression. Must not be
	 *            {@code null}.
	 */
	public NaturalLogarithmExpression(final EvaluableExpression antilogarithm) {
		Validate.notNull(antilogarithm);
		this.antilogarithm = antilogarithm;
	}

	/**
	 * Get the {@link EvaluableExpression} which is the antilogarithm, or parameter of the
	 * {@code LogarithmExpression}.
	 *
	 * @return The antilogarithm expression.
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
		return Math.log(this.antilogarithm.evaluate(variableAssignments));
	}

	@Override
	public String toString() {
		return String.format("(ln%s)", this.antilogarithm);
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
		final NaturalLogarithmExpression other = (NaturalLogarithmExpression) object;
		return new EqualsBuilder().append(this.antilogarithm, other.antilogarithm).isEquals();
	}

	@Override
	public int hashCode() {
		// you pick a hard-coded, randomly chosen, non-zero, odd number
		// ideally different for each class
		return new HashCodeBuilder(227, 229).append(this.antilogarithm).toHashCode();
	}

}
