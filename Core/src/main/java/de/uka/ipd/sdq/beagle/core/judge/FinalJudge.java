package de.uka.ipd.sdq.beagle.core.judge;

import de.uka.ipd.sdq.beagle.core.Blackboard;
import de.uka.ipd.sdq.beagle.core.MeasurableSeffElement;
import de.uka.ipd.sdq.beagle.core.SeffBranch;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpression;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Iterator;
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
	private static final double fitnessε = 0.5d;

	/**
	 * IF the current generation is #maxNumberOfGenerationsPassed, evolution of evaluable
	 * expressions will be stopped.
	 */
	private static final int maxNumberOfGenerationsPassed = 500;

	/**
	 * The maximum amount of time (stated in milliseconds) allowed to have passed since
	 * the application started so evolution of evaluable expressions will be continued.
	 */
	private static final long maxTimePassed = 3 * 24 * 3600;

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

		if (this.numberOfGenerationsPassed > maxNumberOfGenerationsPassed) {
			return true;
		}

		final RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();
		final long startTime = runtimeBean.getStartTime() / 1000;

		final long currentTime = System.currentTimeMillis();

		if ((currentTime - startTime) > maxTimePassed) {
			return true;
		}

		// determine the criteria which aren't CPU-intensive first

		final Set<SeffBranch> seffBranches = blackboard.getSeffBranchesToBeMeasured();
		this.containsElementWithSufficientFitness(seffBranches, blackboard);

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
	 * @param <SE> The type of which all {@linkplain MeasurableSeffElement
	 *            MeasurableSeffElements} of the set {@code measurableSeffElements} are.
	 *
	 * @param measurableSeffElements The set of {@linkplain MeasurableSeffElement
	 *            measurable SEFF elements} to operate on.
	 * @param blackboard The {@link Blackboard} to operate on.
	 * @return {@code true} if {@code measurableSeffElements} contains an element with
	 *         sufficient fitness to stop evolution of evaluable expressions;
	 *         {@code false} otherwise.
	 * @throws ClassCastException Thrown if {@code measurableSeffElements} contains
	 *             elements not of type {@code SE}.
	 */
	private <SE extends MeasurableSeffElement> boolean containsElementWithSufficientFitness(
		final Set<SE> measurableSeffElements, final Blackboard blackboard) throws ClassCastException {

		final EvaluableExpressionFitnessFunction fitnessFunction = blackboard.getFitnessFunction();

		@SuppressWarnings("unchecked")
		final Iterator<MeasurableSeffElement> measurableSeffElementsIterator =
			(Iterator<MeasurableSeffElement>) measurableSeffElements.iterator();
		while (measurableSeffElementsIterator.hasNext()) {
			@SuppressWarnings("unchecked")
			final MeasurableSeffElement currentMeasurableSeffElement = measurableSeffElementsIterator.next();

			final Set<EvaluableExpression> proposedExpressions =
				blackboard.getProposedExpressionFor(currentMeasurableSeffElement);
			final Iterator<EvaluableExpression> proposedExpressionsIterator = proposedExpressions.iterator();

			boolean foundFittingEvaluableExpression = false;
			while (proposedExpressionsIterator.hasNext()) {
				final EvaluableExpression proposedExpression = proposedExpressionsIterator.next();

				if (fitnessFunction.gradeFor((SE) currentMeasurableSeffElement, proposedExpression,
					blackboard) < fitnessε) {
					foundFittingEvaluableExpression = true;
					break;
				}
			}

			// If a single MeasurableSeffElement to examine doesn't have a good enough
			// EvaluableExpression to describe it, the set of MeasurableSeffElements
			// doesn't have good enough EvaluableExpressions to describe it.
			if (!foundFittingEvaluableExpression) {
				return false;
			}
		}

		return true;
	}
}
