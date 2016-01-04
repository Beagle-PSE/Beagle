package de.uka.ipd.sdq.beagle.core.expressions;

/**
 * Provides methods to visit all evaluable expressions. This is needed as it is not always
 * known which expression is currently used. The visitor ensures the correct behavior.
 * 
 * @author Annika Berger
 *
 */
public interface EvaluableExpressionVisitor {

	/**
	 * Called if the visited expression is a {@code AdditionExpression}.
	 *
	 * @param expression to be visited
	 */
	void visit(AdditionExpression expression);

	/**
	 * Called if the visited expression is a {@code MultiplicationExpression}.
	 *
	 * @param expression visited expression
	 */
	void visit(MultiplicationExpression expression);

	/**
	 * Called if the visited expression is a {@code EvaluableVariable}.
	 *
	 * @param variable visited expression
	 */
	void visit(EvaluableVariable variable);

	/**
	 * Called if the visited expression is a {@code ComparisonExpression}.
	 *
	 * @param expression visited expression
	 */
	void visit(ComparisonExpression expression);

	/**
	 * Called if the visited expression is a {@code ConstantExpression}.
	 *
	 * @param constant visited expression
	 */
	void visit(ConstantExpression constant);

	/**
	 * Called if the visited expression is a {@code DivisionExpression}.
	 *
	 * @param expression visited expression
	 */
	void visit(DivisionExpression expression);

	/**
	 * Called if the visited expression is a {@code ExponentationExpression}.
	 *
	 * @param expression visited expression
	 */
	void visit(ExponentationExpression expression);

	/**
	 * Called if the visited expression is a {@code ExponentialFunctionExpression}.
	 *
	 * @param expression visited expression
	 */
	void visit(ExponentialFunctionExpression expression);

	/**
	 * Called if the visited expression is a {@code IfThenElseExpression}.
	 *
	 * @param expression visited expression
	 */
	void visit(IfThenElseExpression expression);

	/**
	 * Called if the visited expression is a {@code LogarithmExpression}.
	 *
	 * @param expression visited expression
	 */
	void visit(LogarithmExpression expression);

	/**
	 * Called if the visited expression is a {@code NaturalLogarithmExpression}.
	 *
	 * @param expression visited expression
	 */
	void visit(NaturalLogarithmExpression expression);

	/**
	 * Called if the visited expression is a {@code SineExpression}.
	 *
	 * @param expression visited expression
	 */
	void visit(SineExpression expression);

	/**
	 * Called if the visited expression is a {@code SubstractionExpression}.
	 *
	 * @param expression visited expression
	 */
	void visit(SubstractionExpression expression);
}
