package de.uka.ipd.sdq.beagle.core.timeout;

import org.apache.commons.lang3.Validate;

import java.util.Arrays;

/**
 * Implements an adaptive timeout based on linear regression. This means that later calls
 * to {@link #isReached()} will return {@code false} if the previous calls took a long
 * time, too. This class is thread safe.
 *
 * @author Christoph Michelbach
 */
public class LinearRegressionAdaptiveTimeout extends ExecutionTimeBasedTimeout {

	/**
	 * How much additional time is always tolerated. Stated in milliseconds.
	 */
	private static final long ADDITIONAL_TIME_TOLEARANCE = 30 * 1000;

	/**
	 * How far the adaptive timeout looks back to the past (calls to
	 * {@link #reportOneStepProgress()}) before forgetting about events. If there have
	 * been fewer than this calls to {@link #reportOneStepProgress()},
	 * {@link #isReached()} will return {@code true} for sure because this much data has
	 * to be collected before enabling the adaptive timeout to make a sensible decision.
	 */
	private static final int RANGE = 10;

	/**
	 * Whether the timeout has been reached in the past. {@code true} if it did;
	 * {@code false} otherwise.
	 */
	private Boolean reachedTimeoutInThePast = false;

	/**
	 * When {@link #isReached()} has been called the last time. Stated in milliseconds.
	 */
	private long timeOfPreviousCall;

	/**
	 * How often {@link #isReached()} has been called in the past.
	 */
	private int numberOfPreviousCalls;

	/**
	 * Contains the telling times of the previous calls to {@link #isReached()}.
	 */
	private final long[] previousTellingTimes = new long[RANGE];

	/**
	 * The current regression line.
	 */
	private RegressionLine regressionLine;

	/**
	 * The millisecond timestamp of when {@link #currentMaximallyTolerableTime} has been
	 * updated the last time.
	 */
	private long lastTimeUpdatedCurrentMaximallyTolerableTime;

	/**
	 * The latest value of {@code maximallyTolerabeTime} (see
	 * {@link #reportOneStepProgress()}) or {@code -1} to indicate infinity.
	 */
	private long currentMaximallyTolerableTime = -1;

	@Override
	protected void implementationInit() {
		this.timeOfPreviousCall = System.currentTimeMillis();
	}

	@Override
	public synchronized void reportOneStepProgress() {
		Validate.isTrue(this.initialised);

		if (this.reachedTimeoutInThePast) {
			return;
		}

		final long currentTime = System.currentTimeMillis();

		this.addTellingTimeToPreviousTellingTimes(currentTime - this.timeOfPreviousCall);
		this.timeOfPreviousCall = currentTime;
		this.numberOfPreviousCalls++;

		this.regressionLine = new RegressionLine(this.previousTellingTimes);
		this.regressionLine.init();

		final long predictedValue = (long) this.regressionLine.getValueFor(RANGE);
		final long maximallyTolerableTime = predictedValue + ADDITIONAL_TIME_TOLEARANCE;
		final boolean reachedTimeout = (currentTime - this.timeOfPreviousCall) > maximallyTolerableTime;

		this.reachedTimeoutInThePast = reachedTimeout;
		this.currentMaximallyTolerableTime = maximallyTolerableTime;
		this.lastTimeUpdatedCurrentMaximallyTolerableTime = System.currentTimeMillis();

		// Prepare everything for the next call to this method.

		if (this.numberOfPreviousCalls == RANGE) {
			new Thread(this::notifyOnReachedTimeout).start();
		}
	}

	@Override
	public boolean isReached() {
		Validate.isTrue(this.initialised);

		if (this.numberOfPreviousCalls < RANGE) {
			return false;
		}

		if (this.reachedTimeoutInThePast) {
			return true;
		}

		final boolean returnValue;
		synchronized (this.reachedTimeoutInThePast) {
			final long currentTime = System.currentTimeMillis();
			returnValue = (currentTime - this.timeOfPreviousCall) > this.currentMaximallyTolerableTime;
			this.reachedTimeoutInThePast = returnValue;
		}

		return returnValue;
	}

	/**
	 * Shifts the contents of {@link #previousTimes} one down and adds {@code} time to the
	 * highest element.
	 *
	 *
	 * @param time The telling time to add.
	 */
	private void addTellingTimeToPreviousTellingTimes(final long time) {
		for (int i = 1; i < RANGE; i++) {
			this.previousTellingTimes[i - 1] = this.previousTellingTimes[i];
		}

		this.previousTellingTimes[RANGE - 1] = time;
	}

	/**
	 * Calls the callback handlers once the timeout is reached.
	 */
	private void notifyOnReachedTimeout() {
		assert this.currentMaximallyTolerableTime >= 0;

		long timeToSleep = this.currentMaximallyTolerableTime + this.lastTimeUpdatedCurrentMaximallyTolerableTime
			- System.currentTimeMillis();

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
			timeToSleep = this.currentMaximallyTolerableTime + this.lastTimeUpdatedCurrentMaximallyTolerableTime
				- System.currentTimeMillis();

		}

		for (final Runnable callback : this.callbacks) {
			new Thread(callback).start();
		}
	}

	/**
	 * A regression line with data points of equal distance on the x-axis.
	 *
	 * @author Christoph Michelbach
	 */
	private class RegressionLine {

		/**
		 * The data to determine the regression line for.
		 */
		private final long[] data;

		/**
		 * Whether the regression line has been initialised since its data has been
		 * updated the last time.
		 */
		private boolean initialised;

		/**
		 * The offset of the regression line.
		 */
		private double offset;

		/**
		 * The slope of the regression line.
		 */
		private double slope;

		/**
		 * Constructs a new {@link RegressionLine} object.
		 *
		 * @param data The data to determine the regression line for.
		 */
		RegressionLine(final long[] data) {
			this.data = data;
			this.initialised = false;
		}

		/**
		 * Calculates the offset and slope of the regression line.
		 */
		public void init() {

			double sum1Part1 = 0;
			for (int i = 0; i < this.data.length; i++) {
				sum1Part1 += i * this.data[i];
			}

			final double averageTime = Arrays.stream(this.data).average().getAsDouble();

			final double sum1Part2 = RANGE * averageTime * 1 / 2 * (RANGE - 1) * RANGE;
			final double sum1 = sum1Part1 - sum1Part2;

			/*
			 * If you have equal distances between the x-coordinates of data and start
			 * counting with 0, the divisor sum of the slope of the regression line can be
			 * simplified to this. Go grab a pencil and a peace of paper and try it
			 * yourself. :)
			 */
			final double sum2 = 1 / 12 * RANGE * (3 * Math.pow(RANGE, 4) - 12 * Math.pow(RANGE, 3) - 12 * RANGE + 2);

			this.slope = sum2 == 0 ? 0 : sum1 / sum2;
			this.offset = averageTime - this.slope * (1 / 2d * (RANGE - 1) * RANGE);

			this.initialised = true;
		}

		/**
		 * Returns the function value for {@code xValue}.
		 *
		 * @param xValue The value on the x-axis.
		 * @return The function value for {@code xValue}.
		 */
		public double getValueFor(final double xValue) {
			Validate.validState(this.initialised);

			return this.offset + xValue * this.slope;
		}

	}

}
