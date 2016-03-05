package de.uka.ipd.sdq.beagle.core.timeout;

import org.apache.commons.lang3.Validate;

/**
 * Provides a constant timeout which cannot be changed.
 *
 * @author Christoph Michelbach
 */
public class ConstantTimeout extends ExecutionTimeBasedTimeout {

	/**
	 * The timeout in milliseconds.
	 */
	private final long timeout;

	/**
	 * Constructs a new constant timeout.
	 *
	 * @param timeout The timeout in milliseconds. Must not be negative.
	 */
	public ConstantTimeout(final int timeout) {
		Validate.isTrue(timeout >= 0);

		this.timeout = timeout;
	}

	@Override
	public boolean isReached() {
		Validate.isTrue(this.initialised);

		return this.startingTime + this.timeout - System.currentTimeMillis() < 0;

	}

	/**
	 * Returns the timeout in milliseconds.
	 *
	 * @return The timeout in milliseconds.
	 */
	public long getTimeout() {
		Validate.isTrue(this.initialised);

		return this.timeout;
	}

	@Override
	public void reportOneStepProgress() {
		Validate.isTrue(this.initialised);

		this.startingTime = System.currentTimeMillis();
	}

	/**
	 * Calls the callback handlers once the timeout is reached.
	 */
	private void notifyOnReachedTimeout() {

		long timeToSleep = this.startingTime + this.timeout - System.currentTimeMillis();

		// Wait until the timeout is up.
		while (!this.isReached()) {
			assert timeToSleep >= 0;

			try {
				Thread.sleep(timeToSleep);

			} catch (final InterruptedException exception) {
				// Retry on interrupt. No handling is needed because the loop just
				// tries again.
			}

			/**
			 * This has to be done at the end of the loop, not at the beginning. Otherwise
			 * the timeout can be reached right before the first instruction in the loop
			 * body but not in the loop header causing {@code timeToSleep} to become
			 * negative. This would be an illegal argument for {@link Thread#sleep(long)}.
			 */
			timeToSleep = this.startingTime + this.timeout - System.currentTimeMillis();
		}

		for (final Runnable callback : this.callbacks) {
			new Thread(callback).start();
		}
	}

	@Override
	protected void implementationInit() {
		new Thread(this::notifyOnReachedTimeout).start();
	}
}
