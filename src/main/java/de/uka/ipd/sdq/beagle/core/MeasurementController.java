package de.uka.ipd.sdq.beagle.core;

import de.uka.ipd.sdq.beagle.measurement.MeasurementTool;

import java.util.Collection;

/**
 * Controls which measurement tool is working.
 *
 * <p>There is always at most one measurement
 * tool working.
 *
 * @author Roman Langrehr
 * @see BeagleController
 */
public class MeasurementController {

	/**
	 * All measurement tools that can be used for measurements.
	 */
	private Collection<MeasurementTool> measurementTools;

	/**
	 * Determines, weather at a measurement tool can contribute to the blackboard.
	 *
	 * @param blackboard The blackboard
	 * @return Whether a measurement toll can measure something, which is marked as 'to be
	 *         measured'. When {@code true} is returned this is no guarantee, that at
	 *         least one new measurement result will be added.
	 */
	public boolean canMeasure(final ReadOnlyBlackboardView blackboard) {
		// implement method
		return false;
	}

	/**
	 * Instructs all available {@link MeasurementTool MeasurementTools} to measure all
	 * items marked as “to be measured”. Measurement Tools may not produce results for
	 * every item but will report results for all items they were able to measure.
	 *
	 * <p>This method may only be called, when {@link #canMeasure(ReadOnlyBlackboardView)}
	 * returned {@code true} before and the blackboard wasn't changed between this call.
	 * Otherwise the behaviour of this method is undefined.
	 *
	 * @param blackboard The blackboard.
	 */
	public void measure(final MeasurementControllerBlackboardView blackboard) {
		// implement method
	}
}
