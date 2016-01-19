package de.uka.ipd.sdq.beagle.core.evaluableexpressions;

/**
 * Expression that compares both its contained expressions.
 * 
 * @author Annika Berger
 *
 */
public class ComparisonExpression implements EvaluableExpression {

	/**
	 * The {@link EvaluableExpression} which is supposed to be the smaller one in the
	 * comparison.
	 */
	private final EvaluableExpression smaller;

	/**
	 * The {@link EvaluableExpression} which is supposed to be the greater one in the
	 * comparison.
	 */
	private final EvaluableExpression greater;

	/**
	 * Builds an expression that will return '1' if the expression set as smaller is
	 * smaller and '0' else.
	 *
	 * @param smaller The {@link EvaluableExpression} which is supposed to be the smaller
	 *            one in the comparison.
	 * @param greater The {@link EvaluableExpression} which is supposed to be the greater
	 *            one in the comparison.
	 */
	public ComparisonExpression(final EvaluableExpression smaller, final EvaluableExpression greater) {
		this.smaller = smaller;
		this.greater = greater;
	}

	/**
	 * Get the {@link EvaluableExpression} which is supposed to be the greater one in the
	 * comparison.
	 * 
	 * <p>For example: if {@code e1 < e2} should be expressed, {@code e1} is set as
	 * smaller and {@code e2} as greater expression.
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
	 * <p>For example: if {@code e1 < e2} should be expressed, {@code e1} is set as
	 * smaller and {@code e2} as greater expression.
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
		if (this.smaller.evaluate(variableAssignments) < this.greater.evaluate(variableAssignments)) {
			return 1;
		} else {
			return 0;
		}
	}

}
