package de.uka.ipd.sdq.beagle.core.expressions;

/**
 * Expression that compares all its contained expressions.
 * 
 * @author Annika Berger
 *
 */
public class ComparisonExpression implements EvaluableExpression {

	/**
	 * Set the {@link EvaluableExpression} which is supposed to be the smaller one in the
	 * comparison. As the comparison is only able to compare if one expression is smaller
	 * or greater than another, it is needed to determine which expression is on the side
	 * of the smaller one in the comparison.
	 * 
	 * <p>For example: if "e1 < e2" should be expressed, e1 is set as smaller and e2 as
	 * greater.
	 *
	 * @param expression which is supposed to be the smaller one
	 */
	public void setSmaller(final EvaluableExpression expression) {

	}

	/**
	 * Set the {@link EvaluableExpression} which is supposed to be the greater one in the
	 * comparison. As the comparison is only able to compare if one expression is smaller
	 * or greater than another, it is needed to determine which expression is on the side
	 * of the smaller one in the comparison.
	 * 
	 * <p>For example: if "e1 < e2" should be expressed, e1 is set as smaller and e2 as
	 * greater.
	 * 
	 * @param expression which is supposed to be the greater one
	 */
	public void setGreater(final EvaluableExpression expression) {

	}

	/**
	 * Get the {@link EvaluableExpression} which is supposed to be the greater one in the
	 * comparison.
	 * 
	 * <p>For example: if "e1 < e2" should be expressed, e1 is set as smaller and e2 as
	 * greater.
	 *
	 * @return the greater expression
	 */
	public EvaluableExpression getGreater() {
		return null;
	}

	/**
	 * Get the {@link EvaluableExpression} which is supposed to be the smaller one in the
	 * comparison.
	 * 
	 * <p>For example: if "e1 < e2" should be expressed, e1 is set as smaller and e2 as
	 * greater.
	 *
	 * @return the smaller expression
	 */
	public EvaluableExpression getSmaller() {
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
