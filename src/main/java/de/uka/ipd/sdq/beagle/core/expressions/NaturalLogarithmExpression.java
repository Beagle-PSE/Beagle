package de.uka.ipd.sdq.beagle.core.expressions;

/**
 * Expression that executes the natural logarithmic function. The antilogarithm is a
 * contained {@link EvaluableExpression} and the base is e (Euler’s number).
 *
 * @author Annika Berger
 */
public class NaturalLogarithmExpression implements EvaluableExpression {

	/**
	 * Set a {@link EvaluableExpression} to be the antilogarithm, or parameter of the
	 * {@code LogarithmExpression}.
	 *
	 * @param expression To be the antilogarithm.
	 */
	public void setAnitlogarithm(final EvaluableExpression expression) {

	}

	/**
	 * Get the {@link EvaluableExpression} which is the antilogarithm, or parameter of the
	 * {@code LogarithmExpression}.
	 *
	 * @return The antilogarithm expression.s.
	 */
	public EvaluableExpression getAntilogarithm() {
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

}
