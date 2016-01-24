package de.uka.ipd.sdq.beagle.core.judge;

import de.uka.ipd.sdq.beagle.core.Blackboard;
import de.uka.ipd.sdq.beagle.core.MeasurableSeffElement;
import de.uka.ipd.sdq.beagle.core.SeffBranch;
import de.uka.ipd.sdq.beagle.core.analysis.ProposedExpressionAnalyserBlackboardView;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpression;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Set;

/**
 * Implements the break condition for evolution of evaluable expressions and decides which
 * proposed evaluable expression describes the measured result best and will be annotated
 * in the PCM.
 * 
 * @author Christoph Michelbach
 */
public class FinalJudge {

	/**
	 * If a generation is fitter than this value, evolution of evaluable expressions will
	 * be stopped.
	 */
	private static final double FITNESS_EPSILON = 0.5d;

	/**
	 * IF the current generation is #maxNumberOfGenerationsPassed, evolution of evaluable
	 * expressions will be stopped.
	 */
	private static final int MAX_NUMBER_OF_GENERATIONS_PASSED = 500;

	/**
	 * The maximum amount of time (stated in milliseconds) allowed to have passed since
	 * the application started so evolution of evaluable expressions will be continued.
	 */
	private static final long MAX_TIME_PASSED = 3 * 24 * 3600;

	/**
	 * The number of generations passed. Will be the number of times a {@link FinalJugde}
	 * object received a call to {@link #judge(Blackboard)}.
	 */
	private int numberOfGenerationsPassed;

	/**
	 * Implements the break condition for evolution of evaluable expressions. Decides
	 * whether evolution will be continued or stopped depending on the fitness of the
	 * fittest evaluable expression, the number of generations passed, the time passed,
	 * and the relative improvement of a generation.
	 *
	 * @param blackboard The {@link Blackboard} this {@link FinalJudge} operates on.
	 * @return {@code true} to indicate that evolution of evaluable expressions will be
	 *         stopped; {@code false} otherwise.
	 */
	boolean judge(final Blackboard blackboard) {

		this.numberOfGenerationsPassed++;

		if (this.numberOfGenerationsPassed > MAX_NUMBER_OF_GENERATIONS_PASSED) {
			return true;
		}

		final RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();
		final long startTime = runtimeBean.getStartTime() / 1000;

		final long currentTime = System.currentTimeMillis();

		if ((currentTime - startTime) > MAX_TIME_PASSED) {
			return true;
		}

		final EvaluableExpressionFitnessFunction fitnessFunction = blackboard.getFitnessFunction();
		// determine the criteria which aren't CPU-intensive first

		final Set<SeffBranch> seffBranches = blackboard.getSeffBranchesToBeMeasured();
		this.containsElementWithSufficientFitness(seffBranches, blackboard, fitnessFunction::gradeFor);

		return true;
	}

	/**
	 * Determines whether a set of {@linkplain MeasurableSeffElement measurable SEFF
	 * elements} contains an element with sufficient fitness to stop evolution of
	 * evaluable expressions.
	 * 
	 * <p/> CAUTION: All elements of {@code measurableSeffElements} have to be of type
	 * {@code SE}.
	 * 
	 * @param <SEFF_ELEMENT_TYPE> The type of which all {@linkplain MeasurableSeffElement
	 *            MeasurableSeffElements} of the set {@code measurableSeffElements} are.
	 *
	 * @param measurableSeffElements The set of {@linkplain MeasurableSeffElement
	 *            measurable SEFF elements} to operate on.
	 * @param blackboard The {@link Blackboard} to operate on.
	 * @param fitnessFunction The fitness function to use.
	 * @return {@code true} if {@code measurableSeffElements} contains an element with
	 *         sufficient fitness to stop evolution of evaluable expressions;
	 *         {@code false} otherwise.
	 */
	private <SEFF_ELEMENT_TYPE extends MeasurableSeffElement> boolean containsElementWithSufficientFitness(
		final Set<SEFF_ELEMENT_TYPE> measurableSeffElements, final Blackboard blackboard,
		final TypedFitnessFunction<SEFF_ELEMENT_TYPE> fitnessFunction) {

		final EvaluableExpressionFitnessFunctionBlackboardView fitnessFunctionView =
			new ProposedExpressionAnalyserBlackboardView();

		for (SEFF_ELEMENT_TYPE seffElement : measurableSeffElements) {
			boolean foundOptimal = false;

			for (EvaluableExpression proposedExpression : blackboard.getProposedExpressionFor(seffElement)) {
				final double fitnessValue =
					fitnessFunction.gradeFor(seffElement, proposedExpression, fitnessFunctionView);
				foundOptimal = foundOptimal || fitnessValue < FITNESS_EPSILON;
			}

			// If a single MeasurableSeffElement to examine doesn't have a good enough
			// EvaluableExpression to describe it, the set of MeasurableSeffElements
			// doesn't have good enough EvaluableExpressions to describe it.
			if (!foundOptimal) {
				return false;
			}
		}

		return true;
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
		 * @param seffElement A SEFF element.
		 * @param expression An expression proposed to describe {@code seffElement}’s
		 *            measurement results.
		 * @param blackboard Beagle’s blackboard instance.
		 * @return A value judging how well {@code expression} fits to describe the
		 *         corresponding measurement results. Will be a value between 0 and
		 *         {@link Double#MAX_VALUE}. The lower the value, the better the fitness.
		 */
		double gradeFor(SEFF_ELEMENT_TYPE seffElement, EvaluableExpression expression,
			EvaluableExpressionFitnessFunctionBlackboardView blackboard);
	}
}
