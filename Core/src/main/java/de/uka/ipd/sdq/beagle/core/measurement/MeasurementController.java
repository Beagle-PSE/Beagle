package de.uka.ipd.sdq.beagle.core.measurement;

import de.uka.ipd.sdq.beagle.core.AnalysisController;
import de.uka.ipd.sdq.beagle.core.Blackboard;

import java.util.Collection;

/**
 * Controls which measurement tool is working.
 *
 * <p>There is always at most one measurement tool working.
 *
 * @author Roman Langrehr
 * @see AnalysisController
 */
public class MeasurementController {

	/**
	 * All measurement tools that can be used for measurements.
	 */
	@SuppressWarnings("unused")
	private Collection<MeasurementTool> measurementTools;

	/**
	 * Determines whether a {@link MeasurementTool} can contribute to the
	 * {@link Blackboard}.
	 *
	 * @param blackboard The blackboard.
	 * @return Whether a {@link MeasurementTool} can measure something which is marked as
	 *         'to be measured'. When {@code true} is returned, this is no guarantee that
	 *         at least one new measurement result will be added.
	 */
	public boolean canMeasure(final ReadOnlyMeasurementControllerBlackboardView blackboard) {
		return false;
	}

	/**
	 * Instructs all available {@link MeasurementTool MeasurementTools} to measure all
	 * items marked as “to be measured”. {@link MeasurementTool MeasurementTools} may not
	 * produce results for every item but will report results for all items they were able
	 * to measure.
	 *
	 * <p>This method may only be called, when {@link #canMeasure} returned {@code true}
	 * before and the {@link Blackboard} wasn't changed between this call. Otherwise the
	 * behaviour of this method is undefined.
	 *
	 * @param blackboard The blackboard.
	 */
	public void measure(final MeasurementControllerBlackboardView blackboard) {
		// implement method
	}
}
