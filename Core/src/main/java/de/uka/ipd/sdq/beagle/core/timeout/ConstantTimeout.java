package de.uka.ipd.sdq.beagle.core.timeout;

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
		return this.startingTime - System.currentTimeMillis() > this.timeout;
	}

}
