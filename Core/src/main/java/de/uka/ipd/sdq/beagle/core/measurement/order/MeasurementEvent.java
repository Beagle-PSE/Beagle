package de.uka.ipd.sdq.beagle.core.measurement.order;

import de.uka.ipd.sdq.beagle.core.CodeSection;
import de.uka.ipd.sdq.beagle.core.measurement.MeasurementTool;

/**
 * An event occurring while measuring software. These events are created by
 * {@linkplain MeasurementTool MeasurementTools}. They are visitable in terms of the
 * well-known visitor pattern. Every code event belongs to a specific code section it was
 * created for. Measurement Events are immutable, meaning that once created, their
 * attributes cannot be changed.
 *
 * @author Joshua Gleitze
 */
public interface MeasurementEvent {

	/**
	 * Queries the source code section this event occurred at.
	 *
	 * @return The source code section this event belongs to. Will never be {@code null}.
	 */
	CodeSection getCodeSection();
}
