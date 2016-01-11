package de.uka.ipd.sdq.beagle.core.measurement.order;

import de.uka.ipd.sdq.beagle.core.CodeSection;
import de.uka.ipd.sdq.beagle.core.ResourceDemandingInternalAction;

/**
 * An event for the fact that the amount of a certain resource a certain code section
 * demands was captured. This implies that the section was completely executed.
 *
 * @author Joshua Gleitze
 */
public class ResourceDemandCapturedEvent extends AbstractMeasurementEvent {

	/**
	 * Creates an event for the fact that {@code codeSection} has been completely executed
	 * and demanded {@code value} resources of the type {@code type}.
	 *
	 * @param codeSection The code section that was executed. Must not be {@code null}.
	 * @param type The demanded resource’s type. See
	 *            {@link ResourceDemandingInternalAction} for a list of conventional
	 *            strings. Must not be null.
	 * @param value The resource demand’s value, expressed in the unit implied by
	 *            {@code type}. Must not be negative.
	 */
	public ResourceDemandCapturedEvent(final CodeSection codeSection, final String type,
		final double value) {
		super(codeSection);
	}

}
