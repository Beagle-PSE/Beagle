package de.uka.ipd.sdq.beagle.core;

import de.uka.ipd.sdq.beagle.core.analysis.ResultAnalyser;
import de.uka.ipd.sdq.beagle.core.judge.FinalJudge;
import de.uka.ipd.sdq.beagle.core.measurement.MeasurementTool;

/**
 * Controls whether a {@link MeasurementTool}, a {@link ResultAnalyser} or the
 * {@link FinalJudge} is working. If a {@link ResultAnalyser} is working, it also
 * controls, which one. There is always only at most one {@link MeasurementTool} or one
 * {@link ResultAnalyser} or the {@link FinalJudge} working.
 *
 * @author Roman Langrehr
 * @see MeasurementController
 */
public class BeagleController {

	/**
	 * This controller's blackboard, passed to all interacting jobs to communicate. Any
	 * data exchange in any action invoked by this controller must happen exclusively via
	 * this {@link Blackboard} instance.
	 */
	private final Blackboard blackboard = new Blackboard();

	/**
	 * Runs the complete analysis, including measurements, result analysis and the final
	 * judging.
	 */
	public void main() {
	}
}
