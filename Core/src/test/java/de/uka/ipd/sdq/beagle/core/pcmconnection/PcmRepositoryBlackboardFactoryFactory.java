package de.uka.ipd.sdq.beagle.core.pcmconnection;

import de.uka.ipd.sdq.beagle.core.judge.EvaluableExpressionFitnessFunction;
import de.uka.ipd.sdq.beagle.core.testutil.factories.EvaluableExpressionFitnessFunctionFactory;

/**
 * A factory which creates instances of {@link PcmRepositoryBlackboardFactory}.
 * 
 * @author Christoph Michelbach
 * @author Ansgar Spiegler
 */
public class PcmRepositoryBlackboardFactoryFactory {

	/**
	 * An {@link EvaluableExpressionFitnessFunction} factory to easily obtain new
	 * instances from.
	 */
	private static final EvaluableExpressionFitnessFunctionFactory FITNESS_FUNCTION_FACTORY =
		new EvaluableExpressionFitnessFunctionFactory();

	/**
	 * Returns a valid {@link PcmRepositoryBlackboardFactory} object.
	 *
	 * @return A valid {@link PcmRepositoryBlackboardFactory} object.
	 */
	public PcmRepositoryBlackboardFactory getValidInstance() {
		return new PcmRepositoryBlackboardFactory(
			"src/test/resources/de/uka/ipd/sdq/beagle/core/pcmconnection/Family.repository",
			PcmRepositoryBlackboardFactoryFactory.FITNESS_FUNCTION_FACTORY.getOne());
	}

}
