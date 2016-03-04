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
	 * The AGING_ALPHA value of the aging algorithm.
	 */
	private static final double AGING_ALPHA = 0.5;

	/**
	 * How much additional time is always tolerated. Stated in milliseconds.
	 */
	private static final long CONSTANT_ADDITIONAL_TIME_TOLEARANCE = 5 * 60 * 1000;

	/**
	 * How much additional time is always tolerated. Relative to the calculated time
	 * tolerance (without {@link #CONSTANT_ADDITIONAL_TIME_TOLEARANCE}).
	 */
	private static final double MULTIPLICATIVE_ADDITIONAL_TIME_TOLEARANCE = 0.3;

	/**
	 * The start value for the aging algorithm.
	 */
	private static final long AGING_START_VALUE = 3600 * 1000;

	/**
	 * The previous maximally tolerable time in milliseconds or {@code -1} to indicate
	 * that there is none.
	 */
	private long previousMaximallyTolerableTime = -1;

	/**
	 * The maximally tolerable time in milliseconds or {@code -1} to indicate that there
	 * is none.
	 */
	private long maximallyTolerableTime = AGING_START_VALUE;

	/**
	 * When {@link #isReached()} has been called the last time. Stated in milliseconds.
	 */
	private long timeOfPreviousCall;

	/**
	 * Whether the callback thread has been started.
	 */
	private boolean startedCallbackThread;

	@Override
	public boolean isReached() {
		if (this.maximallyTolerableTime == -1) {
			return false;
		} else {
			return this.timeOfPreviousCall + this.maximallyTolerableTime - System.currentTimeMillis() < 0;
		}
	}

	@Override
	public synchronized void reportOneStepProgress() {
		this.previousMaximallyTolerableTime = this.maximallyTolerableTime;
		final long tellingTime = this.timeOfPreviousCall - System.currentTimeMillis();
		this.timeOfPreviousCall = System.currentTimeMillis();

		if (this.previousMaximallyTolerableTime == -1) {
			this.maximallyTolerableTime = (long) (tellingTime * (1 + MULTIPLICATIVE_ADDITIONAL_TIME_TOLEARANCE))
				+ CONSTANT_ADDITIONAL_TIME_TOLEARANCE;
		} else {
			this.maximallyTolerableTime =
				(long) (this.previousMaximallyTolerableTime * (1 - AGING_ALPHA) + tellingTime * AGING_ALPHA);

			if (!this.startedCallbackThread) {
				this.startedCallbackThread = true;
				new Thread(this::notifyOnReachedTimeout).start();
			}
		}
	}

	@Override
	protected void implementationInit() {
		this.timeOfPreviousCall = System.currentTimeMillis();
	}

	/**
	 * Calls the callback handlers once the timeout is reached.
	 */
	private void notifyOnReachedTimeout() {
		assert this.maximallyTolerableTime != -1;

		long timeToSleep = this.timeOfPreviousCall + this.maximallyTolerableTime - System.currentTimeMillis();

		// Wait until the timeout is up.
		while (!this.isReached()) {
			assert timeToSleep > 0;

			try {
				Thread.sleep(timeToSleep);
			} catch (final InterruptedException exception) {
				// Retry on interrupt. No handling is needed because the loop just tries
				// again.
			}

			/**
			 * This has to be done at the end of the loop, not at the beginning. Otherwise
			 * the timeout can be reached right before the first instruction in the loop
			 * body but not in the loop header causing {@code timeToSleep} to become
			 * negative. This would be an illegal argument for {@link Thread#sleep(long)}.
			 */
			timeToSleep = this.timeOfPreviousCall + this.maximallyTolerableTime - System.currentTimeMillis();
		}

		for (final Runnable callback : this.callbacks) {
			new Thread(callback).start();
		}
	}
}
