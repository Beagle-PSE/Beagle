package de.uka.ipd.sdq.beagle.core.measurement;

import de.uka.ipd.sdq.beagle.core.measurement.order.MeasurementEvent;
import de.uka.ipd.sdq.beagle.core.measurement.order.MeasurementOrder;

import java.util.List;

/**
 * An analyser that executes the test code an measures certain metrics. Measurement tools
 * don't have access to the blackboard. Instead of this, they get a
 * {@link MeasurementOrder} containing the information about what to measure and should
 * return {@linkplain MeasurementEvent MeasurementEvents} in the correct order.
 *
 * <p>A measurement tool does not need to analyse all things specified in the
 * {@link MeasurementOrder}.
 *
 * <p>Measurement tools need a public zero argument constructor for the instantiation,
 * because they are loaded through eclipse extension points.
 *
 * @author Roman Langrehr
 */
public interface MeasurementTool {

	/**
	 * Executes a measurement. This method should return after the complete measurement
	 * finished.
	 *
	 * @param measurementOrder Container for the information, what to measure and the
	 *            launch Configurations.
	 * @return All {@linkplain MeasurementEvent MeasurementEvents} in the order they
	 *         occurred during the measurement.
	 */
	List<MeasurementEvent> measure(MeasurementOrder measurementOrder);
}
