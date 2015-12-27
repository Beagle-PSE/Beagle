package de.uka.ipd.sdq.beagle.core.expressions;

/*
 * ATTENTION: Checkstyle turned off!
 * remove this comment block when implementing this class!
 *
 * CHECKSTYLE:OFF
 *
 * TODO
 */

/**
 * TODO Document class
 *
 */
public class EvaluableVariable implements EvaluableExpression {
	/**
	 * TODO describe name
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
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		// TODO: Implement method
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uka.ipd.sdq.beagle.core.expressions.EvaluableExpression#receive(de.uka.sdq.beagle.
	 * core.expressions.EvaluableExpressionVisitor)
	 */
	@Override
	public void receive(EvaluableExpressionVisitor visitor) {
		// TODO: Implement method
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uka.ipd.sdq.beagle.core.expressions.EvaluableExpression#evaluate(de.uka.sdq.beagle.
	 * core.expressions.EvaluableVariableAssignment)
	 */
	@Override
	public double evaluate(EvaluableVariableAssignment variableAssignments) {
		// TODO Auto-generated method stub
		return 0;
	}

}
