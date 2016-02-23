package de.uka.ipd.sdq.beagle.core.timeout;

/**
 * Decides when a timeout is reached. This interface has implementing classes which do so
 * depending on different criteria.
 *
 * @author Christoph Michelbach
 */
public interface Timeout {

	/**
	 * Determines whether the timeout is reached.
	 *
	 * @return {@code true} if the timeout is reached; {@code false} otherwise.
	 */
	boolean isReached();
}
