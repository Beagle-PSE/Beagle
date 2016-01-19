package de.uka.ipd.sdq.beagle.core.evaluableexpressions;

import java.util.HashMap;
import java.util.Map;

/**
 * An {@link EvaluableExpression} having a constant value.
 * 
 * @author Annika Berger
 *
 */
public final class ConstantExpression implements EvaluableExpression {

	/**
	 * Hash map containing all existing constant expressions with their value as key.
	 */
	private static Map<Double, ConstantExpression> constantExpressions = new HashMap<Double, ConstantExpression>();

	/**
	 * The value of this constant expression.
	 */
	private double value;

	/**
	 * Builds an expression which returns the value of the expression.
	 * 
	 * @param value The Constant value of this expression.
	 */
	private ConstantExpression(final double value) {
		this.value = value;
		constantExpressions.put(value, this);
	}

	/**
	 * Gets the value of this constant expression.
	 *
	 * @return The value mapped to the constant expression.
	 */
	public double getValue() {
		return this.value;
	}

	/**
	 * Looks for the constant expression belonging to an input {@code value}.
	 *
	 * @param value The value whose expression should be returned.
	 * @return The constant expression belonging to the given {@code value}.
	 */
	public static ConstantExpression forValue(final double value) {
		if (constantExpressions.containsKey(value)) {
			return constantExpressions.get(value);
		} else {
			final ConstantExpression newConstant = new ConstantExpression(value);
			return newConstant;
		}
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
		return this.value;
	}

}
