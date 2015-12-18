package de.uka.sdq.beagle.core.expressions;

/**
 * TODO Document class
 *
 */
public interface EvaluableExpression {
	void receive(EvaluableExpressionVisitor visitor);

	double evaluate(EvaluableVariableAssignment variableAssignments);
}
