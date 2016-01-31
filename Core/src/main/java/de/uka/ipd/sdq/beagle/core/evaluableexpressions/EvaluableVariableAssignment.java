package de.uka.ipd.sdq.beagle.core.evaluableexpressions;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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
	private final Map<EvaluableVariable, Double> assignment = new HashMap<EvaluableVariable, Double>();

	/**
	 * Gets the assigned value for the {@code EvaluableVariable variable}.
	 *
	 * @param variable Whose value is wanted. Must not be {@code null}.
	 * @return The value for the given variable.
	 */
	public double getValueFor(final EvaluableVariable variable) {
		Validate.notNull(variable);
		if (!this.assignment.containsKey(variable)) {
			throw new IllegalArgumentException("There is no value defined for " + variable.toString());
		}
		return this.assignment.get(variable);
	}

	/**
	 * Assigns a value to an evaluable variable.
	 *
	 * @param variable The variable for which the value shall be set. Must not be
	 *            {@code null}.
	 * @param value The value to be set.
	 */
	public void setValueFor(final EvaluableVariable variable, final double value) {
		Validate.notNull(variable);
		this.assignment.put(variable, value);
	}

	/**
	 * Checks whether the {@code EvaluableVariable variable} has already a value assigned
	 * or not.
	 *
	 * @param variable Whose assignment status should be identified. Must not be
	 *            {@code null}.
	 * @return {@code true} if a value is assigned for the given variable. Else it returns
	 *         {@code false}.
	 */
	public boolean isValueAssignedFor(final EvaluableVariable variable) {
		Validate.notNull(variable);
		if (this.assignment.containsKey(variable)) {
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		final StringBuilder result = new StringBuilder();
		boolean first = true;
		for (final EvaluableVariable variable : this.assignment.keySet()) {
			if (!first) {
				result.append(", ");
			} else {
				first = false;
			}
			result.append(variable.toString()).append(" = ").append(this.getValueFor(variable));
		}
		return result.toString();
	}

	@Override
	public boolean equals(final Object object) {
		if (object == null) {
			return false;
		}
		if (object == this) {
			return true;
		}
		if (object.getClass() != this.getClass()) {
			return false;
		}
		final EvaluableVariableAssignment other = (EvaluableVariableAssignment) object;
		final EqualsBuilder result = new EqualsBuilder();
		// asserts that it is the same key set
		result.append(this.assignment, other.assignment);
		for (final EvaluableVariable variable : this.assignment.keySet()) {
			result.append(this.getValueFor(variable), other.getValueFor(variable));
		}
		return result.isEquals();
	}

	@Override
	public int hashCode() {
		// you pick a hard-coded, randomly chosen, non-zero, odd number
		// ideally different for each class
		final HashCodeBuilder result = new HashCodeBuilder(199, 201);
		result.append(this.assignment);
		for (final EvaluableVariable variable : this.assignment.keySet()) {
			result.append(this.getValueFor(variable));
		}
		return result.toHashCode();
	}
}
