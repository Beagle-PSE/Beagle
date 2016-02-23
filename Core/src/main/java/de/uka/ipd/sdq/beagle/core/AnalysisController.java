package de.uka.ipd.sdq.beagle.core;

import de.uka.ipd.sdq.beagle.core.analysis.MeasurementResultAnalyser;
import de.uka.ipd.sdq.beagle.core.analysis.MeasurementResultAnalyserBlackboardView;
import de.uka.ipd.sdq.beagle.core.analysis.ProposedExpressionAnalyser;
import de.uka.ipd.sdq.beagle.core.analysis.ProposedExpressionAnalyserBlackboardView;
import de.uka.ipd.sdq.beagle.core.analysis.ReadOnlyMeasurementResultAnalyserBlackboardView;
import de.uka.ipd.sdq.beagle.core.analysis.ReadOnlyProposedExpressionAnalyserBlackboardView;
import de.uka.ipd.sdq.beagle.core.judge.FinalJudge;
import de.uka.ipd.sdq.beagle.core.measurement.MeasurementController;
import de.uka.ipd.sdq.beagle.core.measurement.MeasurementControllerBlackboardView;
import de.uka.ipd.sdq.beagle.core.measurement.MeasurementTool;
import de.uka.ipd.sdq.beagle.core.measurement.ReadOnlyMeasurementControllerBlackboardView;

import org.apache.commons.lang3.Validate;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

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
 * @author Christoph Michelbach
 *
 * @see MeasurementController
 * @see Blackboard
 */
public class AnalysisController {

	/**
	 * The {@link Blackboard} this {@link AnalysisController} knows and uses.
	 */
	private final Blackboard blackboard;

	/**
	 * The {@link MeasurementController} this {@link AnalysisController} knows and uses.
	 */
	private final MeasurementController measurementController;

	/**
	 * The {@link MeasurementResultAnalyser}s this {@link AnalysisController} knows and
	 * uses.
	 */
	private final Set<MeasurementResultAnalyser> measurementResultAnalysers;

	/**
	 * The {@link ProposedExpressionAnalyser}s this {@link AnalysisController} knows and
	 * uses.
	 */
	private final Set<ProposedExpressionAnalyser> proposedExpressionAnalysers;

	/**
	 * Creates a controller to analyse all elements written on {@code blackboard}.
	 *
	 * @param blackboard A blackboard having everything to be analysed written on it. Must
	 *            not be {@code null}.
	 * @param measurementTools The {@link MeasurementTool}s to use. Must not be
	 *            {@code null} and must not contain {@code null}.
	 * @param measurementResultAnalysers The {@link MeasurementResultAnalyser}s to use.
	 *            Must not be {@code null} and must not contain {@code null}.
	 * @param proposedExpressionAnalysers The {@link ProposedExpressionAnalyser}s to use.
	 *            Must not be {@code null} and must not contain {@code null}.
	 */
	public AnalysisController(final Blackboard blackboard, final Set<MeasurementTool> measurementTools,
		final Set<MeasurementResultAnalyser> measurementResultAnalysers,
		final Set<ProposedExpressionAnalyser> proposedExpressionAnalysers) {
		Validate.notNull(blackboard);
		Validate.notNull(measurementTools);
		Validate.notNull(measurementResultAnalysers);
		Validate.notNull(proposedExpressionAnalysers);
		Validate.noNullElements(measurementTools);
		Validate.noNullElements(measurementResultAnalysers);
		Validate.noNullElements(proposedExpressionAnalysers);

		this.blackboard = blackboard;
		this.measurementController = new MeasurementController(measurementTools);
		this.measurementResultAnalysers = new HashSet<>(measurementResultAnalysers);
		this.proposedExpressionAnalysers = new HashSet<>(proposedExpressionAnalysers);
	}

	/**
	 * Runs the complete analysis, including measurements, result analysis and the final
	 * judging. See the class description for more details on the analysis process.
	 */
	public void performAnalysis() {
		final ReadOnlyMeasurementControllerBlackboardView readOnlyMeasurementControllerBlackboardView =
			new ReadOnlyMeasurementControllerBlackboardView(this.blackboard);
		final MeasurementControllerBlackboardView measurementControllerBlackboardView =
			new MeasurementControllerBlackboardView(this.blackboard);

		this.addAllSeffElementsAsToBeMeasured();

		final FinalJudge finalJudge = new FinalJudge();
		finalJudge.init(this.blackboard);

		while (!finalJudge.judge(this.blackboard)) {
			if (this.measurementController.canMeasure(readOnlyMeasurementControllerBlackboardView)) {
				this.measurementController.measure(measurementControllerBlackboardView);

				// After the measurements completed, clear the seff elements to be
				// measured on the blackboard so they won't be measured again in the
				// next iteration.
				this.clearSeffElementsToBeMeasuredFromBlackboard();
			}

			if (!this.chooseRandomMeasurementResultAnalyserToContribute()) {
				this.chooseRandomPropesedExpressionAnalyserToContribute();
			}
		}
	}

	/**
	 * Clears the "to be measured" lists of seff elements on the blackboard.
	 *
	 */
	private void clearSeffElementsToBeMeasuredFromBlackboard() {
		this.blackboard.clearToBeMeasuredBranches();
		this.blackboard.clearToBeMeasuredLoops();
		this.blackboard.clearToBeMeasuredRdias();
		this.blackboard.clearToBeMeasuredExternalCalls();
	}

	/**
	 * Adds all seff elements on the blackboard to the "to be measured" sets.
	 *
	 */
	private void addAllSeffElementsAsToBeMeasured() {
		final Set<SeffBranch> seffBranches = this.blackboard.getAllSeffBranches();
		final Set<SeffLoop> seffLoops = this.blackboard.getAllSeffLoops();
		final Set<ResourceDemandingInternalAction> rdias = this.blackboard.getAllRdias();
		final Set<ExternalCallParameter> externalCallParameters = this.blackboard.getAllExternalCallParameters();

		this.blackboard.addToBeMeasuredSeffBranches(seffBranches);
		this.blackboard.addToBeMeasuredSeffLoops(seffLoops);
		this.blackboard.addToBeMeasuredRdias(rdias);
		this.blackboard.addToBeMeasuredExternalCallParameters(externalCallParameters);
	}

	/**
	 * Chooses a {@link MeasurementResultAnalyser} able to contribute at random and lets
	 * it contribute.
	 *
	 * @return {@code true} if the task was executed successfully; {@code false} if there
	 *         was no {@link MeasurementResultAnalyser} able to contribute.
	 */
	private boolean chooseRandomMeasurementResultAnalyserToContribute() {
		final ReadOnlyMeasurementResultAnalyserBlackboardView readOnlyMeasurementResultAnalyserBlackboardView =
			new ReadOnlyMeasurementResultAnalyserBlackboardView(this.blackboard);
		final MeasurementResultAnalyserBlackboardView measurementResultAnalyserBlackboardView =
			new MeasurementResultAnalyserBlackboardView(this.blackboard);

		final Set<MeasurementResultAnalyser> measurementResultAnalysersAbleToContribute =
			new HashSet<MeasurementResultAnalyser>();

		for (final MeasurementResultAnalyser measurementResultAnalyser : this.measurementResultAnalysers) {
			if (measurementResultAnalyser.canContribute(readOnlyMeasurementResultAnalyserBlackboardView)) {
				measurementResultAnalysersAbleToContribute.add(measurementResultAnalyser);
			}
		}

		if (measurementResultAnalysersAbleToContribute.size() != 0) {
			// Choose a measurement result analyser at random.
			final int minimum = 1;
			final int maximum = measurementResultAnalysersAbleToContribute.size();
			final int chosenResultAnalyser = new Random().nextInt(maximum - minimum + 1) + minimum;

			int count = 1;
			// @formatter:off
			for (final MeasurementResultAnalyser measurementResultAnalyserAbleToContribute
				: measurementResultAnalysersAbleToContribute) {
				// @formatter:on
				if (count == chosenResultAnalyser) {
					measurementResultAnalyserAbleToContribute.contribute(measurementResultAnalyserBlackboardView);
					return true;
				}

				count++;
			}

		}

		return false;
	}

	/**
	 * Chooses a {@link ProposedExpressionAnalyser} able to contribute at random and lets
	 * it contribute.
	 *
	 * @return {@code true} if the task was executed successfully; {@code false} if there
	 *         was no {@link ProposedExpressionAnalyser} able to contribute.
	 */
	private boolean chooseRandomPropesedExpressionAnalyserToContribute() {
		final ReadOnlyProposedExpressionAnalyserBlackboardView readOnlyProposedExpressionAnalyserBlackboardView =
			new ReadOnlyProposedExpressionAnalyserBlackboardView(this.blackboard);
		final ProposedExpressionAnalyserBlackboardView proposedExpressionAnalyserBlackboardView =
			new ProposedExpressionAnalyserBlackboardView(this.blackboard);

		final Set<ProposedExpressionAnalyser> proposedExpressionAnalysersAbleToContribute =
			new HashSet<ProposedExpressionAnalyser>();

		for (final ProposedExpressionAnalyser proposedExpressionAnalyser : this.proposedExpressionAnalysers) {
			if (proposedExpressionAnalyser.canContribute(readOnlyProposedExpressionAnalyserBlackboardView)) {
				proposedExpressionAnalysersAbleToContribute.add(proposedExpressionAnalyser);
			}
		}

		if (proposedExpressionAnalysersAbleToContribute.size() != 0) {
			// Choose a proposed expression analyser at random.
			final int minimum = 1;
			final int maximum = proposedExpressionAnalysersAbleToContribute.size();
			final int chosenProposedExpressionAnalyser = new Random().nextInt(maximum - minimum + 1) + minimum;

			int count = 1;
			// @formatter:off
			for (final ProposedExpressionAnalyser proposedExpressionAnalyserAbleToContribute
				: proposedExpressionAnalysersAbleToContribute) {
				// @formatter:on
				if (count == chosenProposedExpressionAnalyser) {
					proposedExpressionAnalyserAbleToContribute.contribute(proposedExpressionAnalyserBlackboardView);
					return true;
				}

				count++;
			}

		}

		return false;
	}
}
