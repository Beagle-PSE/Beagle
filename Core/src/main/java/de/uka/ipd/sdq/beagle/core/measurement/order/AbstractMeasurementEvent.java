package de.uka.ipd.sdq.beagle.core.measurement.order;

import de.uka.ipd.sdq.beagle.core.CodeSection;

import org.apache.commons.lang3.Validate;

/**
 * An abstract {@link MeasurementEvent}, already having a
 * {@link MeasurementEvent#getCodeSection() code section}.
 *
 * @author Joshua Gleitze
 */
public abstract class AbstractMeasurementEvent implements MeasurementEvent {

	/**
	 * The code section this measurement event was created for.
	 *
	 * @see MeasurementEvent#getCodeSection()
	 */
	private final CodeSection codeSection;

	/**
	 * Creates an event for the measurement of {@code codeSection}.
	 *
	 * @param codeSection The code section this event is being created for. Must not be
	 *            {@code null}.
	 */
	protected AbstractMeasurementEvent(final CodeSection codeSection) {
		Validate.notNull(codeSection);
		this.codeSection = codeSection;
	}

	@Override
	public CodeSection getCodeSection() {
		return this.codeSection;
	}
}
