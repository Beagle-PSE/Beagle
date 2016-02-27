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
	 * @param timeout The timeout in milliseconds.
	 */
	public ConstantTimeout(final int timeout) {
		this.timeout = timeout;
	}

	@Override
	public boolean isReached() {
		Validate.isTrue(this.initilised);

		return this.startingTime - System.currentTimeMillis() > this.timeout;
	}

	/**
	 * Returns the timeout in milliseconds.
	 *
	 * @return The timeout in milliseconds.
	 */
	public long getTimeout() {
		Validate.isTrue(this.initilised);

		return this.timeout;
	}

	@Override
	public void reportOneStepProgress() {
		Validate.isTrue(this.initilised);
	}

}
