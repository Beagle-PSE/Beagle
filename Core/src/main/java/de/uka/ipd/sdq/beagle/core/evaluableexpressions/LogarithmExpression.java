package de.uka.ipd.sdq.beagle.core.evaluableexpressions;

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
	private EvaluableExpression base;

	/**
	 * The antilogarithm of the expression.
	 */
	private EvaluableExpression antilogarith;

	/**
	 * Builds an expression which returns the result of logarithm of antilogarithm to the
	 * base.
	 *
	 * @param base The base of the logarithm.
	 * @param antilogarithm The antilogarithm of the expression.
	 */
	public LogarithmExpression(final EvaluableExpression base, final EvaluableExpression antilogarithm) {
		this.antilogarith = antilogarithm;
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
		return this.antilogarith;
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
		return Math.log(this.antilogarith.evaluate(variableAssignments))
			/ Math.log(this.base.evaluate(variableAssignments));
	}

}
