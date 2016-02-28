package de.uka.ipd.sdq.beagle.core.timeout;

import org.apache.commons.lang3.Validate;

/**
 * Decides when a timeout is reached. This abstract class has implementing classes which
 * do so depending on different criteria.
 *
 * @author Christoph Michelbach
 */
public abstract class Timeout {

	/**
	 * Indicates whether this object has been initialised.
	 */
	protected boolean initialised;

	/**
	 * The millisecond timestamp when this Timeout object has been initialised.
	 */
	protected long startingTime;

	/**
	 * Determines whether the timeout is reached.
	 *
	 * @return {@code true} if the timeout is reached; {@code false} otherwise.
	 */
	public abstract boolean isReached();

	/**
	 * Call this method after each step of the work the timeout is about.
	 */
	public abstract void reportOneStepProgress();

	/**
	 * Initialises the timeout object. Sets the starting time to the current time.
	 */
	void init() {
		Validate.validState(!this.initialised);

		this.startingTime = System.currentTimeMillis();
		this.initialised = true;
	}
}
