package de.uka.ipd.sdq.beagle.core.expressions;

/**
 * TODO Document class
 * 
 * @author Annika Berger
 *
 */
public class EvaluableVariableAssignment {

	/**
	 * 
	 *
	 * @param variable whose value is wanted
	 * @return the value for the given variable
	 */
	public double getValueFor(final EvaluableVariable variable) {
		return 0;
	}

	/**
	 * Sets a value for an evaluable variable.
	 *
	 * @param variable to which the value belongs
	 * @param value to be set
	 */
	public void setValueFor(final EvaluableVariable variable, final double value) {
	}

	/**
	 * 
	 * TODO document this method.
	 *
	 * @param variable
	 * @return
	 */
	public boolean valueAssignedFor(final EvaluableVariable variable) {
		return false;
	}
}
