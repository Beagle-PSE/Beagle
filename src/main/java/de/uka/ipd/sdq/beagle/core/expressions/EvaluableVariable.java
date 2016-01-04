package de.uka.ipd.sdq.beagle.core.expressions;

/**
 * TODO Document class
 *
 */
public class EvaluableVariable implements EvaluableExpression {

	/**
	 * 
	 *
	 * @return the name
	 */
	public String getName() {
		// TODO: Implement method
		return null;
	}

	/**
	 * TODO describe name
	 *
	 * @param name the name to set
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
