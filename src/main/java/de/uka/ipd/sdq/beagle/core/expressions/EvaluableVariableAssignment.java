package de.uka.ipd.sdq.beagle.core.expressions;

/**
 * The assignment of an evaluable variable and a value.
 * 
 * @author Annika Berger
 *
 */
public class EvaluableVariableAssignment {

	/**
	 * Gets the assigned value for the {@code EvaluableVariable variable}.
	 *
	 * @param variable whose value is wanted
	 * @return the value for the given variable
	 */
	public double getValueFor(final EvaluableVariable variable) {
		return 0;
	}

	/**
	 * Assigns a value to an evaluable variable.
	 *
	 * @param variable to which the value belongs
	 * @param value to be set
	 */
	public void setValueFor(final EvaluableVariable variable, final double value) {
	}

	/**
	 * Checks whether the {@code EvaluableVariable variable} has already a value assigned
	 * or not.
	 *
	 * @param variable whose assignment status should be identified.
	 * @return {@code true} if a value is assigned for the given variable. Else it returns
	 *         {@code false}.
	 */
	public boolean valueAssignedFor(final EvaluableVariable variable) {
		return false;
	}
}
