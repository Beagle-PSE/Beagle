package de.uka.ipd.sdq.beagle.core.pcmconnection;

/**
 * A factory which creates instances of {@link PcmRepositoryBlackboardFactoryAdder}.
 *
 * @author Christoph Michelbach
 * @author Ansgar Spiegler
 * @author Roman Langrehr
 */
public class PcmRepositoryBlackboardFactoryFactory {

	/**
	 * Returns a valid {@link PcmRepositoryBlackboardFactoryAdder} object.
	 *
	 * @return A valid {@link PcmRepositoryBlackboardFactoryAdder} object.
	 */
	public PcmRepositoryBlackboardFactoryAdder getValidInstance() {
		return new PcmRepositoryBlackboardFactoryAdder(
			"src/test/resources/de/uka/ipd/sdq/beagle/core/pcmconnection/Family.repository");
	}

	/**
	 * Returns a valid instance of {@link PcmRepositoryBlackboardFactoryAdder} initialised
	 * with the AppSensor repository.
	 *
	 * @return A valid instance of {@link PcmRepositoryBlackboardFactoryAdder} initialised
	 *         with the AppSensor repository.
	 */
	public PcmRepositoryBlackboardFactoryAdder getAppSensorProjectInstance() {
		return new PcmRepositoryBlackboardFactoryAdder(
			"src/test/resources/de/uka/ipd/sdq/beagle/core/pcmconnection/AppSensor.repository");
	}

}
