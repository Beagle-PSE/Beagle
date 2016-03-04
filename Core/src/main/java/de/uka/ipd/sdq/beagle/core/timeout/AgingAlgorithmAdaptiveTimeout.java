package de.uka.ipd.sdq.beagle.core.timeout;

/**
 * Implements an adaptive timeout based on the aging algorithm. This means that later
 * calls to {@link #isReached()} will return {@code false} if the previous calls took a
 * long time, too.
 *
 * @author Christoph Michelbach
 */
public class AgingAlgorithmAdaptiveTimeout extends ExecutionTimeBasedTimeout {

	@Override
	public boolean isReached() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void reportOneStepProgress() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void implementationInit() {
		// TODO Auto-generated method stub

	}

}
