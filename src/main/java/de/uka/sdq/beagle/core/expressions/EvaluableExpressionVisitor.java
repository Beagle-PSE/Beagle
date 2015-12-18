package de.uka.sdq.beagle.core.expressions;

/**
 * TODO Document class
 *
 */
public interface EvaluableExpressionVisitor {
	void visit(AdditionExpression expression);

	void visit(MultiplicationExpression expression);

	void visit(EvaluableVariable variable);
}
