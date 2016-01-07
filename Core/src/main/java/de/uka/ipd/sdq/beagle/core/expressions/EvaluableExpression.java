package de.uka.ipd.sdq.beagle.core.expressions;

/**
 * Expressions describing relations between {@link EvaluableVariable}s and a
 * {@code double} value. Expressions will usually combine other expressions, called
 * <em>contained expressions</em>. This implements the well-known composite pattern. An
 * expression is said to <em>contain</em> all expressions that are used to calculate its
 * value or are contained by any these. Some contained expression will usually be
 * {@link EvaluableVariable}s. Most expressions will define a requirement on the minimum
 * and/or maximum amount of contained evaluable expressions. An expression can be
 * evaluated by calling {@link #evaluate(EvaluableVariableAssignment)}.
 *
 * <p>Note that expressions may not contain themselves. Any operation on an expression
 * which contains itself or anyhow contains an expression containing itself has undefined
 * behaviour. An {@link ExpressionRecursionException} may be thrown to indicate this
 * situation.
 *
 * <p>Implementations are encouraged to override {@link Object#equals(Object)} and
 * {@link Object#hashCode()} to fulfill the following: If for two evaluable expressions
 * {@code a} and {@code b} {@code a.equals(b)} returns {@code true} true,
 * {@code a.evaluate(ass)} will return a value equal to {@code b.evaluate(ass)} for any
 * {@link EvaluableVariableAssignment} {@code ass}. The inverse implication will usually
 * not hold, e.g. for most cases, there will be two evaluable expressions {@code c} and
 * {@code d} for which {@code c.evaluate(ass)} returns a value equal to
 * {@code d.evaluate(ass)} for any {@link EvaluableVariableAssignment} {@code ass} but
 * {@code a.equals(b)} returns {@code false}. However, the implementation of
 * {@code equals} should aim to allow as few such pairs {@code (c,d)} as possible. <br>
 * Users should be aware that calling {@link Object#equals(Object)
 * EvaluableExpression#equals(Object)} may be very costly, as it might require a lot of
 * comparisons and expressions may an unlimited amount of contained expressions.
 *
 * @author Joshua Gleitze
 */
public interface EvaluableExpression {

	/**
	 * Calls the appropriate overload of {@link EvaluableExpressionVisitor#visit} on
	 * {@code visitor}. This implements the well-known <em>visitor pattern</em>.
	 *
	 * @param visitor The visitor wishing to visit this expression.
	 */
	void receive(EvaluableExpressionVisitor visitor);

	/**
	 * Calculates this expression’s value for the given {@code variableAssignments}.
	 *
	 * @param variableAssignments must assign a value to at least all
	 *            {@link EvaluableVariable}s contained in this expression.
	 * @return the value for the given assignments.
	 * @throws UndefinedExpressionException if {@code variableAssignments} does not
	 *             contain a valid assignment for a contained variable.
	 */
	double evaluate(EvaluableVariableAssignment variableAssignments);
}
