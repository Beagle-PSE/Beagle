package de.uka.ipd.sdq.beagle.core.timeout;

/**
 * Decides when a timeout is reached. This abstract class has implementing classes which
 * do so depending on different criteria.
 *
 * @author Christoph Michelbach
 */
public abstract class Timeout {

	/**
	 * The millisecond timestamp when this Timeout object has been initialised.
	 */
	protected long startingTime;

	/**
	 * Determines whether the timeout is reached.
	 *
	 * @return {@code true} if the timeout is reached; {@code false} otherwise.
	 */
	abstract boolean isReached();

	/**
	 * Initialises the timeout object. Sets the starting time to the current time.
	 */
	void init() {
		this.startingTime = System.currentTimeMillis();
	}
}
