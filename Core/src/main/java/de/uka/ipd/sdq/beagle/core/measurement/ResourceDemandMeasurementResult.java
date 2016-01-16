package de.uka.ipd.sdq.beagle.core.measurement;

import de.uka.ipd.sdq.beagle.core.ResourceDemandingInternalAction;

/**
 * A result of measuring resource demands of a code section. The result expresses that a
 * source code section demanded this result’s value of a certain resource when being
 * executed with the given {@link Parameterisation}.
 *
 * @author Joshua Gleitze
 * @see ResourceDemandingInternalAction
 */
public class ResourceDemandMeasurementResult extends ParameterisationDependentMeasurementResult {

	/**
	 * Creates a result for a resource demand measurement for which no parameterisation
	 * was recorded.
	 */
	public ResourceDemandMeasurementResult() {
	}

	/**
	 * Creates a result for a resource demand measurement.
	 *
	 * @param parameterisation The state of variables during measurement.
	 */
	public ResourceDemandMeasurementResult(final Parameterisation parameterisation) {
	}

	/**
	 * Gets this result’s <em>result type</em>.
	 *
	 * @return This result’s <em>result type</em>, as defined in the class description. Is
	 *         never {@code null}.
	 */
	public String getResourceType() {
		return null;
	}

	/**
	 * Sets this result’s <em>result type</em>.
	 *
	 * @param resourceType This result’s <em>result type</em>, as defined in the class
	 *            description. May not be {@code null}.
	 * @throws NullPointerException If {@code resourceType} is {@code null}.
	 */
	public void setResourceType(final String resourceType) {
	}

	/**
	 * Gets this result’s value.
	 *
	 * @return This result’s value: How much of the <em>resource type</em> was demanded. A
	 *         positive double or 0.
	 */
	public double getValue() {
		return 0;
	}

	/**
	 * Sets this result’s value.
	 *
	 * @param value This result’s value: How much of the <em>resource type</em> was
	 *            demanded. Must not be smaller than 0.
	 * @throws IllegalArgumentException If {@code value < 0}.
	 */
	public void setValue(final double value) {
	}
}
