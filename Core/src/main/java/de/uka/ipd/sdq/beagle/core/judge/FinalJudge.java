package de.uka.ipd.sdq.beagle.core.judge;

import de.uka.ipd.sdq.beagle.core.Blackboard;
import de.uka.ipd.sdq.beagle.core.BlackboardStorer;
import de.uka.ipd.sdq.beagle.core.ExternalCallParameter;
import de.uka.ipd.sdq.beagle.core.MeasurableSeffElement;
import de.uka.ipd.sdq.beagle.core.ResourceDemandingInternalAction;
import de.uka.ipd.sdq.beagle.core.SeffBranch;
import de.uka.ipd.sdq.beagle.core.SeffLoop;
import de.uka.ipd.sdq.beagle.core.analysis.ProposedExpressionAnalyserBlackboardView;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpression;

import org.apache.commons.lang3.Validate;

import java.util.Set;

/**
 * Implements the break condition for evolution of evaluable expressions and decides which
 * proposed evaluable expression describes the measured result best and will be annotated
 * in the PCM.
 *
 * @author Christoph Michelbach
 */
public class FinalJudge implements BlackboardStorer<FinalJudgeData> {

	/**
	 * Fitness values bigger than this number will be set to this number and therefore
	 * will not be considered as the values they truly are. This includes infinity.
	 */
	public static final double MAX_CONSIDERED_FITNESS_VALUE = 10E250d;

	/**
	 * If a proposed expression is fitter than this value, it will be considered
	 * “perfect”, e.g. no further evolution of the element the expression was proposed for
	 * is needed.
	 */
	private static final double PERFECT_FITNESS = 0.5E-10d;

	/**
	 * The maximum amount of time (stated in milliseconds) allowed to have passed since
	 * the application started so evolution of evaluable expressions will be continued.
	 */
	private static final long MAX_TIME_PASSED = 3 * 24 * 3600 * 1000;

	/**
	 * If more generations than this have less than {@link #SIGNIFICANT_IMPROVEMENT}
	 * relative improvement to the previously best value, evolution of evaluable
	 * expressions will be stopped.
	 */
	private static final int MAX_NUMBER_OF_GENERATIONS_WITHOUT_SIGNIFICANT_IMPROVEMENT = 12;

	/**
	 * If more generations than
	 * {@link #MAX_NUMBER_OF_GENERATIONS_WITHOUT_SIGNIFICANT_IMPROVEMENT} have less than
	 * this much relative improvement to the previously best value, evolution of evaluable
	 * expressions will be stopped.
	 */
	private static final double SIGNIFICANT_IMPROVEMENT = 0.06;

	/**
	 * Stores all status of this {@link FinalJudge} object.
	 */
	private FinalJudgeData data;

	/**
	 * Initialises the {@link FinalJudge} object. Call this method before starting
	 * evolution of evaluable expressions to start counting the total time the entire
	 * evolution of evaluable expressions takes.
	 *
	 * @param blackboard The {@link Blackboard} to store the data of this
	 *            {@link FinalJudge} on. Must not be {@code null}.
	 *
	 */
	public void init(final Blackboard blackboard) {
		Validate.notNull(blackboard);

		blackboard.writeFor(FinalJudge.class, new FinalJudgeData());

		this.loadData(blackboard);

		this.data.setStartTime(System.currentTimeMillis());
	}

	/**
	 * Implements the break condition for evolution of evaluable expressions. Decides
	 * whether evolution will be continued or stopped depending on the fitness of the
	 * fittest evaluable expression, the number of generations passed, the time passed,
	 * and the relative improvement of a generation.
	 *
	 * @param blackboard The {@link Blackboard} this {@link FinalJudge} operates on. Must
	 *            not be {@code null}.
	 * @return {@code true} to indicate that evolution of evaluable expressions will be
	 *         stopped; {@code false} otherwise.
	 */
	public boolean judge(final Blackboard blackboard) {
		Validate.notNull(blackboard);
		this.loadData(blackboard);
		this.data.newGeneration();

		// Determine the criteria which aren't CPU-intensive first.
		if (this.timePassedTooHigh()) {
			return true;
		}

		// Take the measurements.
		this.measure(blackboard);

		return this.allElementsArePerfect() || !this.sufficientRelativeImprovement();
	}

	/**
	 * Loads the data stored for this object from the {@link Blackboard}.
	 *
	 * @param blackboard The {@link Blackboard} to use. Must not be {@code null}.
	 * @throws IllegalStateException Thrown if this method is called before
	 *             {@link #init()}.
	 */
	private void loadData(final Blackboard blackboard) throws IllegalStateException {
		Validate.notNull(blackboard);

		this.data = blackboard.readFor(FinalJudge.class);

		if (this.data == null) {
			throw new IllegalStateException("loadData(Blackboard) cannot be called on FinalJudge before init().");
		}
	}

	/**
	 * Measures the fitness of all seff branches, seff loops, rdias, and external call
	 * parameters on the {@link Blackboard}.
	 *
	 * @param blackboard The {@link Blackboard} to use.
	 */
	private void measure(final Blackboard blackboard) {
		final EvaluableExpressionFitnessFunction fitnessFunction = blackboard.getFitnessFunction();

		final Set<SeffBranch> seffBranches = blackboard.getAllSeffBranches();
		final Set<SeffLoop> seffLoops = blackboard.getAllSeffLoops();
		final Set<ResourceDemandingInternalAction> rdias = blackboard.getAllRdias();
		final Set<ExternalCallParameter> extCallParameters = blackboard.getAllExternalCallParameters();

		this.measureFitnessAndAddToBlackboard(seffBranches, blackboard, fitnessFunction::gradeFor);
		this.measureFitnessAndAddToBlackboard(seffLoops, blackboard, fitnessFunction::gradeFor);
		this.measureFitnessAndAddToBlackboard(rdias, blackboard, fitnessFunction::gradeFor);
		this.measureFitnessAndAddToBlackboard(extCallParameters, blackboard, fitnessFunction::gradeFor);
	}

	/**
	 * Measures the fitness of all {@linkplain MeasurableSeffElement measurable SEFF
	 * elements} and for each seff element adds the expression describing it best to the
	 * blackboard.
	 *
	 * <p/> CAUTION: All elements of {@code measurableSeffElements} have to be of type
	 * {@code SE}.
	 *
	 * @param <SEFF_ELEMENT_TYPE> The type of which all {@linkplain MeasurableSeffElement
	 *            MeasurableSeffElements} of the set {@code measurableSeffElements} are.
	 *            Must not be {@code null}.
	 *
	 * @param measurableSeffElements The set of {@linkplain MeasurableSeffElement
	 *            measurable SEFF elements} to operate on. Must not be {@code null}.
	 * @param blackboard The {@link Blackboard} to operate on. Must not be {@code null}.
	 * @param fitnessFunction The fitness function to use. Must not be {@code null}.
	 * @return {@code true} if {@code measurableSeffElements} contains an element with
	 *         sufficient fitness to stop evolution of evaluable expressions;
	 *         {@code false} otherwise.
	 */
	private <SEFF_ELEMENT_TYPE extends MeasurableSeffElement> boolean measureFitnessAndAddToBlackboard(
		final Set<SEFF_ELEMENT_TYPE> measurableSeffElements, final Blackboard blackboard,
		final TypedFitnessFunction<SEFF_ELEMENT_TYPE> fitnessFunction) {

		final EvaluableExpressionFitnessFunctionBlackboardView fitnessFunctionView =
			new ProposedExpressionAnalyserBlackboardView(blackboard);

		for (final SEFF_ELEMENT_TYPE seffElement : measurableSeffElements) {
			double fittest = MAX_CONSIDERED_FITNESS_VALUE;
			EvaluableExpression bestExpression = null;
			// Then let it attain a better fitness if possible.
			for (final EvaluableExpression proposedExpression : blackboard.getProposedExpressionFor(seffElement)) {
				final double fitnessValue =
					fitnessFunction.gradeFor(seffElement, proposedExpression, fitnessFunctionView);
				if (fitnessValue < fittest) {
					fittest = fitnessValue;
					bestExpression = proposedExpression;
				}

			}

			this.data.addFittestValue(fittest);

			// Add the best expression to the blackboard.
			blackboard.setFinalExpressionFor(seffElement, bestExpression);
		}

		return true;
	}

	/**
	 * Determines whether all seff elements on the Blackboard have a perfect expression
	 * proposed. This means that one of the element’s proposed expressions has a fitness
	 * value that’s below or equal to {@link #PERFECT_FITNESS}.
	 *
	 * @return {@code true} if all {@code measurableSeffElements} have an expression
	 *         proposed, such that its fitness is below or equal {@link #PERFECT_FITNESS}.
	 */
	private boolean allElementsArePerfect() {
		return this.data.getFittestValues().allMatch(fitnessValue -> fitnessValue <= PERFECT_FITNESS);
	}

	/**
	 * Determines whether the amount of time passed is too high.
	 *
	 * @return {@code true} if and only if the time passed is greater than
	 *         {@code MAX_TIME_PASSED}.
	 */
	private boolean timePassedTooHigh() {
		final long currentTime = System.currentTimeMillis();
		return (currentTime - this.data.getStartTime()) > MAX_TIME_PASSED;
	}

	/**
	 * Evaluates the relative improvement to the latest
	 * {@linkplain FinalJudgeData#getFitnessBaseline() fitness baseline}.
	 *
	 * @return {@code true} if the relative improvement to the latest baseline is
	 *         sufficient to continue the analysis or there still can be tries without
	 *         significant improvement; {@code false} otherwise.
	 */
	private boolean sufficientRelativeImprovement() {
		final double fitnessBaselineValue = this.data.getFitnessBaselineValue();
		final double overallFitniss = this.data.getFittestValues().average().orElse(0);

		// This never is a division by zero because if it was, we’d already exited the
		// final judge before entering this method.
		final double relativeImprovement = 1 - overallFitniss / fitnessBaselineValue;

		if (relativeImprovement > SIGNIFICANT_IMPROVEMENT) {
			// There was significant improvement so note the new value and set the number
			// of generations without significant improvement passed back to 0.
			this.data.setFitnessBaselineValue(overallFitniss);
			this.data.setNumberOfGenerationsWithoutSignificantImprovementPassed(0);
			return true;
		} else {
			// Without significant improvement, first note this fact and then check
			// whether this was the last try without success.
			this.data.setNumberOfGenerationsWithoutSignificantImprovementPassed(
				this.data.getNumberOfGenerationsWithoutSignificantImprovementPassed() + 1);
			// @formatter:off
			return this.data.getNumberOfGenerationsWithoutSignificantImprovementPassed()
				<= MAX_NUMBER_OF_GENERATIONS_WITHOUT_SIGNIFICANT_IMPROVEMENT;
			// @formatter:on
		}
	}

	/**
	 * Provides the method {@code EvaluableExpressionFitnessFunction#gradeFor} for a
	 * specified {@code SEFF_ELEMENT_TYPE}.
	 *
	 * @author Christoph Michelbach
	 * @param <SEFF_ELEMENT_TYPE> The type of which all {@linkplain MeasurableSeffElement
	 *            MeasurableSeffElements} of the set {@code measurableSeffElements} are.
	 */
	private interface TypedFitnessFunction<SEFF_ELEMENT_TYPE extends MeasurableSeffElement> {

		/**
		 * Provides the method {@code EvaluableExpressionFitnessFunction#gradeFor} for a
		 * specified {@code SEFF_ELEMENT_TYPE}.
		 *
		 * @param seffElement A SEFF element. Must not be {@code null}.
		 * @param expression An expression proposed to describe {@code seffElement}’s
		 *            measurement results. Must not be {@code null}.
		 * @param blackboard Beagle’s blackboard instance. Must not be {@code null}.
		 * @return A value judging how well {@code expression} fits to describe the
		 *         corresponding measurement results. Will be a value between 0 and
		 *         {@link Double#MAX_VALUE}. The lower the value, the better the fitness.
		 */
		double gradeFor(SEFF_ELEMENT_TYPE seffElement, EvaluableExpression expression,
			EvaluableExpressionFitnessFunctionBlackboardView blackboard);
	}
}
