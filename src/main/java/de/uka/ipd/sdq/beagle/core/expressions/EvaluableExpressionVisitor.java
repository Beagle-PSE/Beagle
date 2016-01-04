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
	 * Called if the visited expression is a {@link AdditionExpression}.
	 *
	 * @param expression to be visited
	 */
	void visit(AdditionExpression expression);

	/**
	 * Called if the visited expression is a {@link MultiplicationExpression}.
	 *
	 * @param expression visited expression
	 */
	void visit(MultiplicationExpression expression);

	/**
	 * Called if the visited expression is a {@link EvaluableVariable}.
	 *
	 * @param variable visited expression
	 */
	void visit(EvaluableVariable variable);

	/**
	 * Called if the visited expression is a {@link ComparisonExpression}.
	 *
	 * @param expression visited expression
	 */
	void visit(ComparisonExpression expression);

	/**
	 * Called if the visited expression is a {@link ConstantExpression}.
	 *
	 * @param constant visited expression
	 */
	void visit(ConstantExpression constant);

	/**
	 * Called if the visited expression is a {@link DivisionExpression}.
	 *
	 * @param expression visited expression
	 */
	void visit(DivisionExpression expression);

	/**
	 * Called if the visited expression is a {@link ExponentationExpression}.
	 *
	 * @param expression visited expression
	 */
	void visit(ExponentationExpression expression);

	/**
	 * Called if the visited expression is a {@link ExponentialFunctionExpression}.
	 *
	 * @param expression visited expression
	 */
	void visit(ExponentialFunctionExpression expression);

	/**
	 * Called if the visited expression is a {@link IfThenElseExpression}.
	 *
	 * @param expression visited expression
	 */
	void visit(IfThenElseExpression expression);

	/**
	 * Called if the visited expression is a {@link LogarithmExpression}.
	 *
	 * @param expression visited expression
	 */
	void visit(LogarithmExpression expression);

	/**
	 * Called if the visited expression is a {@link NaturalLogarithmExpression}.
	 *
	 * @param expression visited expression
	 */
	void visit(NaturalLogarithmExpression expression);

	/**
	 * Called if the visited expression is a {@link SineExpression}.
	 *
	 * @param expression visited expression
	 */
	void visit(SineExpression expression);

	/**
	 * Called if the visited expression is a {@link SubstractionExpression}.
	 *
	 * @param expression visited expression
	 */
	void visit(SubstractionExpression expression);
}
