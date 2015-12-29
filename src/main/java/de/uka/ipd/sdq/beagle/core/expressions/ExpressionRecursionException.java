package de.uka.ipd.sdq.beagle.core.expressions;

/*
 * ATTENTION: Checkstyle turned off!
 * remove this comment block when implementing this class!
 *
 * CHECKSTYLE:OFF
 *
 * TODO
 */

/**
 * Might be thrown if a {@link EvaluableExpression} contains itself.
 *
 * @author Joshua Gleitze
 * @see EvaluableExpression
 */
public class ExpressionRecursionException extends RuntimeException {

	/**
	 * Serialisation version UID, see {@link java.io.Serializable}.
	 */
	private static final long serialVersionUID = -3704244273606345556L;

	/**
	 * Creates an Exception for the fact that {@code selfContainedExpression}, contains
	 * itself.
	 *
	 * @param selfContainedExpression
	 *            an expression containing itself.
	 */
	public ExpressionRecursionException(final EvaluableExpression selfContainedExpression) {
		// TODO Auto-generated constructor stub
	}

	/**
	 * The expression which contains itself and raised this exception.
	 *
	 * @return the causing, self-contained expression.
	 */
	public EvaluableExpression getCausingExpression() {
		// TODO implement method
		return null;
	}

}
