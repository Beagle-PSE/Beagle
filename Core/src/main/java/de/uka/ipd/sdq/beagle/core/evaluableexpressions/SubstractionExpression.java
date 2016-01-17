package de.uka.ipd.sdq.beagle.core.evaluableexpressions;

/**
 * Expression that subtracts a subtrahend from a minuend.
 *
 * @author Annika Berger
 *
 */
public class SubstractionExpression implements EvaluableExpression {

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uka.ipd.sdq.beagle.core.expressions.EvaluableExpression#evaluate(de.uka.sdq.
	 * beagle. core.expressions.EvaluableVariableAssignment)
	 */
	@Override
	public double evaluate(final EvaluableVariableAssignment variableAssignments) {
		return 0;
	}

	/**
	 * Get the {@link EvaluableExpression} which is the minuend.
	 *
	 * @return This expression's minuend.
	 */
	public EvaluableExpression getMinuend() {
		return null;
	}

	/**
	 * Get the {@link EvaluableExpression} which is the substrahend.
	 *
	 * @return This expression's substrahend.
	 */
	public EvaluableExpression getSubstrahend() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.uka.ipd.sdq.beagle.core.expressions.EvaluableExpression#receive(de.uka.sdq.
	 * beagle. core.expressions.EvaluableExpressionVisitor)
	 */
	@Override
	public void receive(final EvaluableExpressionVisitor visitor) {

	}

	/**
	 * Set an {@link EvaluableExpression} as minuend of the {@code SubstractionExpression}
	 * .
	 *
	 * @param expression The expression to be the minuend.
	 */
	public void setMinuend(final EvaluableExpression expression) {

	}

	/**
	 * Set an {@link EvaluableExpression} as substrahend of the
	 * {@code SubstractionExpression}.
	 *
	 * @param expression The expression to be the substrahend.
	 */
	public void setSubtrahend(final EvaluableExpression expression) {

	}

}
