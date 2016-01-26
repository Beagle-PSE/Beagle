package de.uka.ipd.sdq.beagle.core.measurement.order;
/**
 * ATTENTION: Test coverage check turned off. Remove this comments block when implementing
 * this class!
 * 
 * <p>COVERAGE:OFF
 */

import de.uka.ipd.sdq.beagle.core.CodeSection;

/**
 * An abstract {@link MeasurementEvent}, already having a
 * {@link MeasurementEvent#getCodeSection() code section}.
 *
 * @author Joshua Gleitze
 */
public class AbstractMeasurementEvent implements MeasurementEvent {

	/**
	 * The code section this measurement event was created for.
	 *
	 * @see MeasurementEvent#getCodeSection()
	 */
	private final CodeSection codeSection;

	/**
	 * Creates an event for the measurement of {@code codeSection}.
	 *
	 * @param codeSection The code section this event is being created for.
	 */
	protected AbstractMeasurementEvent(final CodeSection codeSection) {
		this.codeSection = codeSection;
	}

	@Override
	public CodeSection getCodeSection() {
		return this.codeSection;
	}

}