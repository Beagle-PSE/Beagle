package de.uka.ipd.sdq.beagle.core.timeout;

/**
 * A timeout policy that never aborts execution.
 *
 * @author Christoph Michelbach
 */
public class NoTimeout implements Timeout {

	@Override
	public boolean isReached() {
		return false;
	}

	@Override
	public void reportOneStepProgress() {
	}

	@Override
	public void init() {
		// nothing to do here
	}

	@Override
	public void registerCallback(final Runnable callback) {
		// there is no timeout, thus, the callbacks will never be called.
	}

	@Override
	public void unregisterCallback(final Runnable callback) {
		// nothing registered → nothing to unregister
	}

}
