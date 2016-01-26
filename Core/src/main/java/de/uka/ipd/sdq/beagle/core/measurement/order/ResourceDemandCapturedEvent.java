package de.uka.ipd.sdq.beagle.core.measurement.order;
/**
 * ATTENTION: Test coverage check turned off. Remove this comments block when implementing
 * this class!
 * 
 * <p>COVERAGE:OFF
 */

import de.uka.ipd.sdq.beagle.core.CodeSection;
import de.uka.ipd.sdq.beagle.core.ResourceDemandType;

import org.apache.commons.lang3.Validate;

/**
 * An event for the fact that the amount of a certain resource a certain code section
 * demands was captured. This implies that the section was completely executed.
 *
 * @author Joshua Gleitze
 * @author Roman Langrehr
 */
public class ResourceDemandCapturedEvent extends AbstractMeasurementEvent {

	/**
	 * The demanded resource’s type. See {@link ResourceDemandType} for a list of
	 * conventional strings. Must not be null.
	 */
	private ResourceDemandType type;

	/**
	 * The resource demand’s value, expressed in the unit implied by {@code type}. Must
	 * not be negative.
	 */
	private double value;

	/**
	 * Creates an event for the fact that {@code codeSection} has been completely executed
	 * and demanded {@code value} resources of the type {@code type}.
	 *
	 * @param codeSection The code section that was executed. Must not be {@code null}.
	 * @param type The demanded resource’s type. See {@link ResourceDemandType} for a list
	 *            of conventional strings. Must not be {@code null}.
	 * @param value The resource demand’s value, expressed in the unit implied by
	 *            {@code type}. Must be non-negative.
	 */
	public ResourceDemandCapturedEvent(final CodeSection codeSection, final ResourceDemandType type,
		final double value) {
		super(codeSection);
		Validate.notNull(type);
		Validate.isTrue(value >= 0, "The measured resources value must be non-neagtive, but was %d", value);
	}

	/**
	 * Gives the demanded resource’s type. See {@link ResourceDemandType} for a list of
	 * conventional strings.
	 *
	 * @return The demanded resource’s type. See {@link ResourceDemandType} for a list of
	 *         conventional strings. Is never {@code null}
	 */
	public ResourceDemandType getType() {
		return this.type;
	}

	/**
	 * The resource demand’s value, expressed in the unit implied by {@code type}.
	 *
	 * @return The resource demand’s value, expressed in the unit implied by {@code type}.
	 *         Is non-negative.
	 */
	public double getValue() {
		return this.value;
	}

}
