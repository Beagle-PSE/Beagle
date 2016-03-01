package de.uka.ipd.sdq.beagle.core.timeout;

import org.apache.commons.lang3.Validate;

/**
 * Provides a constant timeout which cannot be changed.
 *
 * @author Christoph Michelbach
 */
public class ConstantTimeout extends Timeout {

	/**
	 * The timeout in milliseconds.
	 */
	private long timeout;

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

		return System.currentTimeMillis() - this.startingTime > this.timeout;
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
	}

}
