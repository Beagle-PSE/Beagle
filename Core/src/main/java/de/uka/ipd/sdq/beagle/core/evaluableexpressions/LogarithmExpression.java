package de.uka.ipd.sdq.beagle.core.evaluableexpressions;

/**
 * Expression that executes a logarithm with defined expressions as base and
 * antilogarithm.
 * 
 * @author Annika Berger
 */
public class LogarithmExpression implements EvaluableExpression {

	/**
	 * Set the base of the logarithm.
	 *
	 * @param expression which is supposed to be the base.
	 */
	public void setBase(final EvaluableExpression expression) {

	}

	/**
	 * Get the {@link EvaluableExpression} which is the base of the logarithm.
	 *
	 * @return the base of the logarithm.
	 */
	public EvaluableExpression getBase() {
		return null;
	}

	/**
	 * Set a {@link EvaluableExpression} to be the antilogarithm, or parameter of the
	 * {@code LogarithmExpression}.
	 *
	 * @param expression to be the antilogarithm
	 */
	public void setAnitlogarithm(final EvaluableExpression expression) {

	}

	/**
	 * Get the {@link EvaluableExpression} which is the antilogarithm, or parameter of the
	 * {@code LogarithmExpression}.
	 *
	 * @return the antilogarithm expression.s
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
