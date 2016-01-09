package de.uka.ipd.sdq.beagle.core.evaluableexpressions;

/**
 * An {@link EvaluableExpression} representing a named variable.
 * 
 * @author Annika Berger
 */
public class EvaluableVariable implements EvaluableExpression {

	/**
	 * Get this evaluable varibale's name.
	 *
	 * @return The variable's name. Is never {@code null}.
	 */
	public String getName() {
		return null;
	}

	/**
	 * Names this evaluable variable.
	 *
	 * @param name This variable's name. Must not be {@code null}.
	 */
	public void setName(final String name) {
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
