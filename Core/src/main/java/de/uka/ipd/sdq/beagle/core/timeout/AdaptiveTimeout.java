package de.uka.ipd.sdq.beagle.core.timeout;

import org.apache.commons.lang3.Validate;

import java.util.Arrays;

/**
 * Implements an adaptive timeout. This means that later calls to {@link #isReached()}
 * will return {@code false} if the previous calls took a long time, too.
 *
 * @author Christoph Michelbach
 */
public class AdaptiveTimeout extends Timeout {

	/**
	 * How much additional time is always tolerated. Stated in milliseconds.
	 */
	private static final long ADDITIONAL_TIME_TOLEARANCE = 5 * 60 * 1000;

	/**
	 * How far the adaptive timeout looks back to the past (calls to {@link #isReached()})
	 * before forgetting about events. This is also how often {@link #isReached()} will
	 * return {@code true} for sure because this much data has to be collected before
	 * enabling the timeout to make a sensible decision.
	 */
	private static final int RANGE = 10;

	/**
	 * Whether the timeout has been reached in the past. {@code true} if it did;
	 * {@code false} otherwise.
	 */
	private boolean reachedTimeoutInThePast;

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
	private long[] previousTellingTimes = new long[RANGE];

	/**
	 * The current regression line.
	 */

	private RegressionLine regressionLine;

	@Override
	public void init() {
		Validate.isTrue(!this.initialised);

		super.init();

		this.timeOfPreviousCall = System.currentTimeMillis();
	}

	@Override
	public void reportOneStepProgress() {
		Validate.isTrue(this.initialised);

		if (this.reachedTimeoutInThePast) {
			return;
		}

		final long currentTime = System.currentTimeMillis();
		this.regressionLine = new RegressionLine(this.previousTellingTimes);
		this.regressionLine.init();

		final long predictedValue = (long) this.regressionLine.getValueFor(RANGE);
		final long maximallyTolerableTime = predictedValue + ADDITIONAL_TIME_TOLEARANCE;
		final boolean reachedTimeout = (currentTime - this.timeOfPreviousCall) > maximallyTolerableTime;

		this.reachedTimeoutInThePast = reachedTimeout;

		// Prepare everything for the next call to this method.
		this.addTellingTimeToPreviousTellingTimes(currentTime - this.timeOfPreviousCall);
		this.timeOfPreviousCall = currentTime;
		this.numberOfPreviousCalls++;
	}

	@Override
	public boolean isReached() {
		Validate.isTrue(this.initialised);

		if (this.reachedTimeoutInThePast) {
			return true;
		}

		final long currentTime = System.currentTimeMillis();
		final long predictedValue = (long) this.regressionLine.getValueFor(RANGE);
		final long maximallyTolerableTime = predictedValue + ADDITIONAL_TIME_TOLEARANCE;

		final boolean returnValue = (currentTime - this.timeOfPreviousCall) > maximallyTolerableTime;

		this.reachedTimeoutInThePast = returnValue;

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
	 * A regression line with data points of equal distance on the x-axis.
	 *
	 * @author Christoph Michelbach
	 */
	private class RegressionLine {

		/**
		 * The data to determine the regression line for.
		 */
		private long[] data;

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
			this.data = data.clone();
			this.initialised = false;
		}

		/**
		 * Calculates the offset and slope of the regression line.
		 */
		public void init() {

			final double averageTime = Arrays.stream(this.data).average().getAsDouble();

			double sum1 = 0;
			for (int i = 0; i < this.data.length; i++) {
				final long time = this.data[i];

				sum1 += (i - this.data.length / 2d) * (time - averageTime);
			}

			/*
			 * If you have equal distances between the x-coordinates of data and start
			 * counting with 0, the divisor sum of the slope of the regression line can be
			 * simplified to this. Go grab a pencil and a peace of paper and try it
			 * yourself. :)
			 */
			final double sum2 = 1 / 12d * this.data.length * (-1 + Math.pow(this.data.length, 2));

			this.slope = sum1 / sum2;
			this.offset = averageTime - this.slope * (1 / 2d * (this.data.length - 1) * this.data.length);

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
