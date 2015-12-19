package de.uka.sdq.beagle.core.expressions;

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
}
