package de.uka.ipd.sdq.beagle.core.evaluableexpressions;

import java.util.HashMap;
import java.util.Map;

/**
 * Assigns {@link EvaluableVariable EvaluableVariables} to values.
 * 
 * @author Annika Berger
 * 
 * @see EvaluableVariable
 *
 */
public class EvaluableVariableAssignment {

	/**
	 * Contains all assignements of evaluable variables and their double value.
	 */
	private Map<EvaluableVariable, Double> assignement = new HashMap<EvaluableVariable, Double>();

	/**
	 * Gets the assigned value for the {@code EvaluableVariable variable}.
	 *
	 * @param variable Whose value is wanted.
	 * @return The value for the given variable.
	 */
	public double getValueFor(final EvaluableVariable variable) {
		return this.assignement.get(variable);
	}

	/**
	 * Assigns a value to an evaluable variable.
	 *
	 * @param variable To which the value belongs.
	 * @param value To be set.
	 */
	public void setValueFor(final EvaluableVariable variable, final double value) {
		this.assignement.put(variable, value);
	}

	/**
	 * Checks whether the {@code EvaluableVariable variable} has already a value assigned
	 * or not.
	 *
	 * @param variable Whose assignment status should be identified.
	 * @return {@code true} if a value is assigned for the given variable. Else it returns
	 *         {@code false}.
	 */
	public boolean isValueAssignedFor(final EvaluableVariable variable) {
		if (this.assignement.containsKey(variable)) {
			return true;
		}
		return false;
	}
}
