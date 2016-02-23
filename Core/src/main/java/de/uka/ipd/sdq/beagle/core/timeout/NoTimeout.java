package de.uka.ipd.sdq.beagle.core.timeout;

/**
 * Always says that the timeout isn't reached.
 *
 * @author Christoph Michelbach
 */
public class NoTimeout extends Timeout {

	@Override
	public boolean isReached() {
		return false;
	}

}
