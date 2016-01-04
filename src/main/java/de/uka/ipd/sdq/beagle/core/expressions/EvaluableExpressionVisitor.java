package de.uka.ipd.sdq.beagle.core.expressions;

/*
 * ATTENTION: Checkstyle turned off!
 * remove this comment block when adding details to this interface!
 *
 * CHECKSTYLE:OFF
 *
 * TODO
 */

/**
 * TODO Document class
 *
 */
public interface EvaluableExpressionVisitor {
	void visit(AdditionExpression expression);

	void visit(MultiplicationExpression expression);

	void visit(EvaluableVariable variable);

	void visit(ComparisonExpression expression);

	void visit(ConstantExpression constant);

	void visit(DivisionExpression expression);

	void visit(ExponentationExpression expression);

	void visit(ExponentialFunctionExpression expression);

	void visit(IfThenElseExpression expression);

	void visit(InequationExpression expression);
	
	void visit(LogarithmExpression expression);

	void visit(NaturalLogarithmExpression expression);

	void visit(SineExpression expression);

	void visit(SubstractionExpression expression);
}
