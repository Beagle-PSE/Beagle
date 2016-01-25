package de.uka.ipd.sdq.beagle.core.evaluableexpressions;

/**
 * Provides methods to visit any {@linkplain EvaluableExpression}. Implements the well
 * known Visitor Pattern.
 *
 * <p>This interface defines {@code visit} methods {@linkplain EvaluableExpression
 * EvaluableExpressions} call to register themselves at the visitor. These methods are not
 * meant to be used by clients. Implementors will define methods designated to access
 * their functionality for clients.
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
	 * Called if the visited expression is a {@link SubtractionExpression}.
	 *
	 * @param expression The visited expression.
	 */
	void visit(SubtractionExpression expression);
}
