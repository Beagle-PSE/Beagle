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
public class SubstractionExpression implements EvaluableExpression {
	
	/**
	 * The substrahend of the expression.
	 */
	private EvaluableExpression substrahend;
	
	/**
	 * The minuend of the expressions.
	 */
	private EvaluableExpression minuend;

	/**
	 * Builds an expression which returns the difference of substrahend and minuend.
	 *
	 * @param substrahend The substrahend of the expression. Must not be {@code null}.
	 * @param minuend The minuend of the expression. Must not be {@code null}.
	 */
	public SubstractionExpression(final EvaluableExpression substrahend, final EvaluableExpression minuend) {
		Validate.notNull(substrahend);
		Validate.notNull(minuend);
		this.substrahend = substrahend;
		this.minuend = minuend;
	}

	/**
	 * Get the {@link EvaluableExpression} which is the substrahend.
	 *
	 * @return This expression's substrahend.
	 */
	public EvaluableExpression getSubstrahend() {
		return this.substrahend;
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
		return this.minuend.evaluate(variableAssignments) - this.substrahend.evaluate(variableAssignments);
	}
	
	@Override
	public String toString() {
		return String.format("(%s - %s)", this.minuend, this.substrahend);
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
		final SubstractionExpression other = (SubstractionExpression) object;
		return new EqualsBuilder().append(this.minuend, other.minuend).append(this.substrahend, other.substrahend).isEquals();
	}

	@Override
	public int hashCode() {
		// you pick a hard-coded, randomly chosen, non-zero, odd number
		// ideally different for each class
		return new HashCodeBuilder(235, 237).append(this.minuend).append(this.substrahend).toHashCode();
	}

}
