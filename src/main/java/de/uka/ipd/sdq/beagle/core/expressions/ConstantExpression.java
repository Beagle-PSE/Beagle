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
 * TODO Document this type.
 *
 */
public class ConstantExpression implements EvaluableExpression {

	/**
	 * TODO document this method.
	 *
	 * @param value
	 *            TODO
	 * @return TODO
	 */
	public static ConstantExpression forValue(double value) {
		// TODO implement this method
		return null;
	}

	/**
	 * TODO document this constructor.
	 *
	 */
	private ConstantExpression(double value) {
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.uka.ipd.sdq.beagle.core.expressions.EvaluableExpression#receive(de.uka.sdq.
	 * beagle. core.expressions.EvaluableExpressionVisitor)
	 */
	@Override
	public void receive(EvaluableExpressionVisitor visitor) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uka.ipd.sdq.beagle.core.expressions.EvaluableExpression#evaluate(de.uka.sdq.
	 * beagle. core.expressions.EvaluableVariableAssignment)
	 */
	@Override
	public double evaluate(EvaluableVariableAssignment variableAssignments) {
		// TODO Auto-generated method stub
		return 0;
	}

}
