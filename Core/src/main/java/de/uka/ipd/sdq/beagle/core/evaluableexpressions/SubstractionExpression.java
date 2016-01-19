package de.uka.ipd.sdq.beagle.core.evaluableexpressions;

/**
 * Expression that subtracts a subtrahend from a minuend.
 * 
 * @author Annika Berger
 *
 */
public class SubstractionExpression implements EvaluableExpression {
	
	/**
	 * The substrahend of the expression.
	 */
	private EvaluableExpression substrahend;
	
	/**
	 * The minuend of the expressions.
	 */
	private EvaluableExpression minuend;

	/**
	 * Builds an expression which returns the difference of substrahend and minuend.
	 *
	 * @param substrahend The substrahend of the expression.
	 * @param minuend The minuend of the expression.
	 */
	public SubstractionExpression(final EvaluableExpression substrahend, final EvaluableExpression minuend) {
		this.substrahend = substrahend;
		this.minuend = minuend;
	}

	/**
	 * Get the {@link EvaluableExpression} which is the substrahend.
	 *
	 * @return This expression's substrahend.
	 */
	public EvaluableExpression getSubstrahend() {
		return this.substrahend;
	}

	/**
	 * Get the {@link EvaluableExpression} which is the minuend.
	 *
	 * @return This expression's minuend.
	 */
	public EvaluableExpression getMinuend() {
		return this.minuend;
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
		return this.minuend.evaluate(variableAssignments) - this.substrahend.evaluate(variableAssignments);
	}

}
