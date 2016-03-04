package de.uka.ipd.sdq.beagle.core.timeout;

/**
 * Implements an adaptive timeout based on the aging algorithm. This means that later
 * calls to {@link #isReached()} will return {@code false} if the previous calls took a
 * long time, too.
 *
 * @author Christoph Michelbach
 */
public class AgingAlgorithmAdaptiveTimeout extends ExecutionTimeBasedTimeout {

	/**
	 * The alpha value of the aging algorithm.
	 */
	private static final double alpha = 0.5;

	/**
	 * The previous maximally tolerable time in milliseconds or {@code -1} to indicate
	 * that there is none.
	 */
	private long previousMaximallyTolerableTime = -1;

	/**
	 * The maximally tolerable time in milliseconds or {@code -1} to indicate that there
	 * is none.
	 */
	private long maximallyTolerableTime = -1;

	/**
	 * When {@link #isReached()} has been called the last time. Stated in milliseconds.
	 */
	private long timeOfPreviousCall;

	@Override
	public boolean isReached() {
		if (this.maximallyTolerableTime == -1) {
			return false;
		} else {
			return this.timeOfPreviousCall + this.previousMaximallyTolerableTime - System.currentTimeMillis() < 0;
		}
	}

	@Override
	public void reportOneStepProgress() {
		this.previousMaximallyTolerableTime = this.maximallyTolerableTime;
		final long tellingTime = this.timeOfPreviousCall - System.currentTimeMillis();
		this.timeOfPreviousCall = System.currentTimeMillis();

		if (this.previousMaximallyTolerableTime == -1) {
			this.maximallyTolerableTime = tellingTime;
		} else {
			this.maximallyTolerableTime =
				(long) (this.previousMaximallyTolerableTime * (1 - alpha) + tellingTime * alpha);
		}
	}

	@Override
	protected void implementationInit() {
		this.timeOfPreviousCall = System.currentTimeMillis();
	}

}
