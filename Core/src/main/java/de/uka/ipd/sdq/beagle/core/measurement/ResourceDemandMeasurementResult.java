package de.uka.ipd.sdq.beagle.core.measurement;

import de.uka.ipd.sdq.beagle.core.ResourceDemandingInternalAction;

import org.apache.commons.lang3.Validate;

/**
 * A result of measuring resource demands of a code section. The result expresses that a
 * source code section demanded this result’s value of a certain resource when being
 * executed with the given {@link Parameterisation}.
 *
 * <p>The type of the resources measured is specified by the
 * {@link ResourceDemandingInternalAction}'s type it belongs to.
 *
 * @author Joshua Gleitze
 * @author Roman Langrehr
 * @see ResourceDemandingInternalAction
 */
public class ResourceDemandMeasurementResult extends ParameterisationDependentMeasurementResult {

	/**
	 * The value measured. The unit is specified by the
	 * {@link ResourceDemandingInternalAction}'s type it belongs to.
	 */
	private double value;

	/**
	 * Creates a result for a resource demand measurement for which no parameterisation
	 * was recorded.
	 *
	 * @param value The value measured. The unit is specified by the
	 *            {@link ResourceDemandingInternalAction}'s type it belongs to. Must be
	 *            equal or greater than {@code 0}.
	 */
	public ResourceDemandMeasurementResult(final double value) {
		Validate.isTrue(value >= 0, "The measured resources value was negative: %d", value);
		this.value = value;
	}

	/**
	 * Creates a result for a resource demand measurement.
	 *
	 * @param parameterisation The state of variables during measurement.
	 *
	 * @param value The value measured. The unit is specified by the
	 *            {@link ResourceDemandingInternalAction}'s type it belongs to. Must be
	 *            greater than {@code 0}.
	 */
	public ResourceDemandMeasurementResult(final Parameterisation parameterisation, final double value) {
		super(parameterisation);
		Validate.isTrue(value >= 0, "The measured resources value was negative: %d", value);
		this.value = value;
	}

	/**
	 * Gets this result’s value.
	 *
	 * @return This result’s value: How much of the <em>resource type</em> was demanded. A
	 *         positive double or 0.
	 */
	public double getValue() {
		return this.value;
	}
}
