package de.uka.ipd.sdq.beagle.core.evaluableexpressions;

/**
 * Thrown if a {@link EvaluableExpression} is to be evaluated, but insufficient variable
 * assignments are provided.
 *
 * @author Joshua Gleitze
 * @see EvaluableExpression
 */
public class UndefinedExpressionException extends RuntimeException {
	
	/**
	 * Serialisation version UID, see {@link java.io.Serializable}.
	 */
	private static final long serialVersionUID = -80875322029735423L;

	/**
	 * The variable missing in the assignment.
	 */
	private EvaluableVariable undefinedVariable;

	/**
	 * The assignment raising the exception.
	 */
	private EvaluableVariableAssignment assignment;

	/**
	 * Creates an exception for an encountered undefined variable. Describes the situation
	 * that an {@link EvaluableExpression} containing {@code undefinedVariable} was tried
	 * to be {@link EvaluableExpression#evaluate}d, but the passed {@code assignment} did
	 * not contain a valid assignment for {@code undefinedVariable}.
	 *
	 * @param assignment The assignment raising the exception.
	 * @param undefinedVariable The variable missing in {@code assignment}.
	 */
	public UndefinedExpressionException(final EvaluableVariableAssignment assignment,
		final EvaluableVariable undefinedVariable) {
		super(String.format("Can not evaluate: There is no value for %s defined in %s!", undefinedVariable, assignment));
		this.assignment = assignment;
		this.undefinedVariable = undefinedVariable;
	}

	/**
	 * The assignment that caused this exception by not assigning a value to
	 * {@link #getMissingVariable()}.
	 *
	 * @return the causing assignment.
	 */
	public EvaluableVariableAssignment getCausingAssignment() {
		return this.assignment;
	}

	/**
	 * The variable having no assignment in {@link #getCausingAssignment()} while being
	 * needed.
	 *
	 * @return the missing variable.
	 */
	public EvaluableVariable getMissingVariable() {
		return this.undefinedVariable;

	}
}
