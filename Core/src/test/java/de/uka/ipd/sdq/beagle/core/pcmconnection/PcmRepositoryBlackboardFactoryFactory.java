package de.uka.ipd.sdq.beagle.core.pcmconnection;

/**
 * A factory which creates instances of {@link PcmRepositoryBlackboardFactory}.
 * 
 * @author Christoph Michelbach
 */
public class PcmRepositoryBlackboardFactoryFactory {

	/**
	 * Returns a valid {@link PcmRepositoryBlackboardFactory} object.
	 *
	 * @return A valid {@link PcmRepositoryBlackboardFactory} object.
	 */
	public PcmRepositoryBlackboardFactory getValidInstance() {
		return new PcmRepositoryBlackboardFactory(
			"/Beagle Core/src/test/resources/de/uka/ipd/sdq/beagle/core/pcmconnection/Family.repository");
	}

}
