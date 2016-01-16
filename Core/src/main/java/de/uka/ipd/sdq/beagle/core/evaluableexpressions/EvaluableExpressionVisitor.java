package de.uka.ipd.sdq.beagle.core.evaluableexpressions;

/**
 * Provides methods to visit all evaluable expressions. Implements the well known Visitor
 * Pattern.
 * 
 * @author Annika Berger
 * @see <a href="https://en.wikipedia.org/wiki/Visitor_pattern">The Visitor Pattern on
 *      Wikipedia</a>
 *
 */
public interface EvaluableExpressionVisitor {

	/**
	 * Called if the visited expression is a {@link AdditionExpression}.
	 *
	 * @param expression The visited expression.
	 */
	void visit(AdditionExpression expression);

	/**
	 * Called if the visited expression is a {@link MultiplicationExpression}.
	 *
	 * @param expression The visited expression.
	 */
	void visit(MultiplicationExpression expression);

	/**
	 * Called if the visited expression is a {@link EvaluableVariable}.
	 *
	 * @param variable The visited expression.
	 */
	void visit(EvaluableVariable variable);

	/**
	 * Called if the visited expression is a {@link ComparisonExpression}.
	 *
	 * @param expression The visited expression.
	 */
	void visit(ComparisonExpression expression);

	/**
	 * Called if the visited expression is a {@link ConstantExpression}.
	 *
	 * @param constant The visited expression.
	 */
	void visit(ConstantExpression constant);

	/**
	 * Called if the visited expression is a {@link DivisionExpression}.
	 *
	 * @param expression The visited expression.
	 */
	void visit(DivisionExpression expression);

	/**
	 * Called if the visited expression is a {@link ExponentationExpression}.
	 *
	 * @param expression The visited expression.
	 */
	void visit(ExponentationExpression expression);

	/**
	 * Called if the visited expression is a {@link ExponentialFunctionExpression}.
	 *
	 * @param expression The visited expression.
	 */
	void visit(ExponentialFunctionExpression expression);

	/**
	 * Called if the visited expression is a {@link IfThenElseExpression}.
	 *
	 * @param expression The visited expression.
	 */
	void visit(IfThenElseExpression expression);

	/**
	 * Called if the visited expression is a {@link LogarithmExpression}.
	 *
	 * @param expression The visited expression.
	 */
	void visit(LogarithmExpression expression);

	/**
	 * Called if the visited expression is a {@link NaturalLogarithmExpression}.
	 *
	 * @param expression The visited expression.
	 */
	void visit(NaturalLogarithmExpression expression);

	/**
	 * Called if the visited expression is a {@link SineExpression}.
	 *
	 * @param expression The visited expression.
	 */
	void visit(SineExpression expression);

	/**
	 * Called if the visited expression is a {@link SubstractionExpression}.
	 *
	 * @param expression The visited expression.
	 */
	void visit(SubstractionExpression expression);
}
