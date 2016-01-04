package de.uka.ipd.sdq.beagle.core.expressions;

/**
 * 
 * @author Annika Berger
 *
 */
public class ConstantExpression implements EvaluableExpression {

	/**
	 * TODO document this method.
	 *
	 * @param value TODO
	 * @return TODO
	 */
	public static ConstantExpression forValue(final double value) {
		return null;
	}

	/**
	 * TODO document this constructor.
	 *
	 */
	private ConstantExpression(final double value) {
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
	public double evaluate(EvaluableVariableAssignment variableAssignments) {
		return 0;
	}

}
