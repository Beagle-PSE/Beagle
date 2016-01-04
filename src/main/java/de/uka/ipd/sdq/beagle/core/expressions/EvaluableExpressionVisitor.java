package de.uka.ipd.sdq.beagle.core.expressions;

/**
 * Provides methods to visit all evaluable expressions.
 * @author Annika Berger
 *
 */
public interface EvaluableExpressionVisitor {

	/**
	 * TODO document this method.
	 *
	 * @param expression to be visited
	 */
	void visit(AdditionExpression expression);

	void visit(MultiplicationExpression expression);

	void visit(EvaluableVariable variable);

	void visit(ComparisonExpression expression);

	void visit(ConstantExpression constant);

	void visit(DivisionExpression expression);

	void visit(ExponentationExpression expression);

	void visit(ExponentialFunctionExpression expression);

	void visit(IfThenElseExpression expression);

	void visit(LogarithmExpression expression);

	void visit(NaturalLogarithmExpression expression);

	void visit(SineExpression expression);

	void visit(SubstractionExpression expression);
}
