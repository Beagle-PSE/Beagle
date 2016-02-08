package de.uka.ipd.sdq.beagle.core.testutil.factories;

import de.uka.ipd.sdq.beagle.core.judge.AbstractionAndPrecisionFitnessFunction;
import de.uka.ipd.sdq.beagle.core.judge.EvaluableExpressionFitnessFunction;

/**
 * Factory for pre-initialised EvaluableExpressionFitnessFunctions to be used by tests.
 *
 * @author Ansgar Spiegler
 */
public class EvaluableExpressionFitnessFunctionFactory {

	/**
	 * Creates a new FitnessFunction.
	 * 
	 * @return EvaluableExpressionFitnessFunction. You may not make any assumptions about
	 *         it.
	 */
	public EvaluableExpressionFitnessFunction getOne() {
		return new AbstractionAndPrecisionFitnessFunction();
	}
}
