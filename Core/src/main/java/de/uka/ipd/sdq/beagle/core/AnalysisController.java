package de.uka.ipd.sdq.beagle.core;

import de.uka.ipd.sdq.beagle.core.analysis.ProposedExpressionAnalyser;
import de.uka.ipd.sdq.beagle.core.judge.FinalJudge;
import de.uka.ipd.sdq.beagle.core.measurement.MeasurementController;

/**
 * Conducts a complete analysis of elements on a blackboard. Controls the
 * {@link MeasurementController}, {@link ProposedExpressionAnalyser ResultAnalysers} and
 * the {@link FinalJudge} to measure and analyse parametric dependencies.
 *
 * <h3>The Analysis</h3> During the analysis, the controller will ask participants whether
 * they can contribute (except for the {@linkplain FinalJudge}, which must always be able
 * to judge). The participants may only make these assumptions of the control flow:
 *
 * <ul>
 *
 * <li>There is always only the {@link MeasurementController}, only the {@link FinalJudge}
 * or only one {@link ProposedExpressionAnalyser} (or none of the previous) running. They
 * may use parallelisation as they wish but are self-responsible for synchronisation.
 *
 * <li>A {@link ProposedExpressionAnalyser} will only be called if its
 * {@link ProposedExpressionAnalyser#canContribute} method returns {@code true}. The
 * {@link MeasurementController} will only be called if its
 * {@link MeasurementController#canMeasure canMeasure} method returns {@code true}.
 *
 * <li>When picking the next participant, the {@link MeasurementController} will be always
 * be called next if its {@link MeasurementController#canMeasure canMeasure} method
 * returns {@code true}.
 *
 * <li>Any {@link ProposedExpressionAnalyser} whose
 * {@link ProposedExpressionAnalyser#canContribute canContribute} method returns
 * {@code true} will be called.
 *
 * <li>The {@linkplain FinalJudge} will only be called if no
 * {@link ProposedExpressionAnalyser} can contribute.
 *
 * </ul>
 *
 * @author Roman Langrehr
 * @author Joshua Gleitze
 *
 * @see MeasurementController
 * @see Blackboard
 */
public class AnalysisController {

	/**
	 * This controller's blackboard, passed to all interacting jobs to communicate. Any
	 * data exchange in any action invoked by this controller must happen exclusively via
	 * this {@link Blackboard} instance.
	 */
	@SuppressWarnings("unused")
	private final Blackboard blackboard = null;

	/**
	 * Creates a controller to analyse all elements written on {@code blackboard}.
	 *
	 * @param blackboard A blackboard having everything to be analysed written on it.
	 */
	public AnalysisController(final Blackboard blackboard) {

	}

	/**
	 * Runs the complete analysis, including measurements, result analysis and the final
	 * judging. See the class description for more details on the analysis process.
	 */
	public void performAnalysis() {
	}
}
