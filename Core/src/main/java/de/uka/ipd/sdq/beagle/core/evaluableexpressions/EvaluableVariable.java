package de.uka.ipd.sdq.beagle.core.evaluableexpressions;

/**
 * An {@link EvaluableExpression} representing a named variable.
 * 
 * @author Annika Berger
 */
public class EvaluableVariable implements EvaluableExpression {

	/**
	 * The name of the evaluable variable.
	 */
	private String name;

	/**
	 * Builds an evaluable variable with the given name.
	 *
	 * @param name The name of the variable.
	 */
	public EvaluableVariable(final String name) {
		this.name = name;
	}

	/**
	 * Get this evaluable varibale's name.
	 *
	 * @return The variable's name. Is never {@code null}.
	 */
	public String getName() {
		return this.name;
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
		return variableAssignments.getValueFor(this);
	}

}
