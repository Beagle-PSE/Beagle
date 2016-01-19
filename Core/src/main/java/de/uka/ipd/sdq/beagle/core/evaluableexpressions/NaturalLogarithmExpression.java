package de.uka.ipd.sdq.beagle.core.evaluableexpressions;

/**
 * Expression that executes the natural logarithmic function. The antilogarithm is a
 * contained {@link EvaluableExpression} and the base is e (Euler’s number).
 *
 * @author Annika Berger
 */
public class NaturalLogarithmExpression implements EvaluableExpression {

	/**
	 * The antilogarithm of the expression.
	 */
	private EvaluableExpression antilogarithm;

	/**
	 * Builds an expression which returns the result of a logarithm of the antilogarithm
	 * to the base.
	 *
	 * @param antilogarithm The antilogarithm for this expression.
	 */
	public NaturalLogarithmExpression(final EvaluableExpression antilogarithm) {
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
		return Math.log(this.antilogarithm.evaluate(variableAssignments));
	}

}
