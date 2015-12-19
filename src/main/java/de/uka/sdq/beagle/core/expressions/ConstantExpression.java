package de.uka.sdq.beagle.core.expressions;

/**
 * TODO Document this type.
 *
 */
public class ConstantExpression implements EvaluableExpression {

	/**
	 * TODO document this method.
	 *
	 * @param value
	 * @return
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
	 * @see
	 * de.uka.sdq.beagle.core.expressions.EvaluableExpression#receive(de.uka.sdq.beagle.
	 * core.expressions.EvaluableExpressionVisitor)
	 */
	@Override
	public void receive(EvaluableExpressionVisitor visitor) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uka.sdq.beagle.core.expressions.EvaluableExpression#evaluate(de.uka.sdq.beagle.
	 * core.expressions.EvaluableVariableAssignment)
	 */
	@Override
	public double evaluate(EvaluableVariableAssignment variableAssignments) {
		// TODO Auto-generated method stub
		return 0;
	}

}
