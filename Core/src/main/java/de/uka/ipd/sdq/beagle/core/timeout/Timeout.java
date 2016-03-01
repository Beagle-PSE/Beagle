package de.uka.ipd.sdq.beagle.core.timeout;

import org.apache.commons.lang3.Validate;

import java.util.HashSet;
import java.util.Set;

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
	 * A set of callbacks which will be called once the timeout is reached.
	 */
	protected Set<Runnable> callbacks = new HashSet<>();

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
	 * Initialises the timeout object. Sets the starting time to the current time. Must be
	 * called before any different method is called and must only be called once.
	 */
	public final void init() {
		Validate.validState(!this.initialised);

		this.startingTime = System.currentTimeMillis();
		this.initialised = true;

		this.implementationInit();
	}

	/**
	 * For init methots of implemanting classes. Runs after {@link #init()}.
	 */
	public abstract void implementationInit();

	/**
	 * Registers {@code callback} to be run once the timeout is reached.
	 *
	 * @param callback A {@link Runnable} object.
	 */
	public void registerCallback(final Runnable callback) {
		this.callbacks.add(callback);
	}

	/**
	 * Removes {@code callback} from the set of callbacks to be run once the timeout is
	 * reached.
	 *
	 * @param callback A {@link Runnable} object.
	 */
	public void unregisterCallback(final Runnable callback) {
		this.callbacks.remove(callback);
	}
}
