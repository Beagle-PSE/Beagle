package de.uka.ipd.sdq.beagle.core.pcmconnection;

import de.uka.ipd.sdq.beagle.core.judge.EvaluableExpressionFitnessFunction;
import de.uka.ipd.sdq.beagle.core.testutil.factories.EvaluableExpressionFitnessFunctionFactory;

/**
 * A factory which creates instances of {@link PcmRepositoryBlackboardFactoryAdder}.
 *
 * @author Christoph Michelbach
 * @author Ansgar Spiegler
 */
public class PcmRepositoryBlackboardFactoryFactory {

	/**
	 * An {@link EvaluableExpressionFitnessFunction} factory to easily obtain new
	 * instances from.
	 */
	public static final EvaluableExpressionFitnessFunctionFactory FITNESS_FUNCTION_FACTORY =
		new EvaluableExpressionFitnessFunctionFactory();

	/**
	 * Returns a valid {@link PcmRepositoryBlackboardFactoryAdder} object.
	 *
	 * @return A valid {@link PcmRepositoryBlackboardFactoryAdder} object.
	 */
	public PcmRepositoryBlackboardFactoryAdder getValidInstance() {
		return new PcmRepositoryBlackboardFactoryAdder(
			"src/test/resources/de/uka/ipd/sdq/beagle/core/pcmconnection/Family.repository",
			PcmRepositoryBlackboardFactoryFactory.FITNESS_FUNCTION_FACTORY.getOne());
	}

	/**
	 * Returns a valid instance of {@link PcmRepositoryBlackboardFactoryAdder} initialised with
	 * the AppSensor repository.
	 *
	 * @return A valid instance of {@link PcmRepositoryBlackboardFactoryAdder} initialised with
	 *         the AppSensor repository.
	 */
	public PcmRepositoryBlackboardFactoryAdder getAppSensorProjectInstance() {
		return new PcmRepositoryBlackboardFactoryAdder(
			"src/test/resources/de/uka/ipd/sdq/beagle/core/pcmconnection/AppSensor.repository",
			PcmRepositoryBlackboardFactoryFactory.FITNESS_FUNCTION_FACTORY.getOne());
	}

}
