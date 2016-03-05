package de.uka.ipd.sdq.beagle.core.timeout;

/**
 * Policy deciding when to stop the execution of a program. Clients can report progress
 * through {@link #reportOneStepProgress()}. This information may be used by the policy to
 * make a decision. Once the policy decides to end the execution, it will call all
 * handlers registered through {@link #registerCallback(Runnable)}. This will happen
 * asynchronously.
 *
 * <p>A timeout must be initialised through {@link #init()} exactly once before it is
 * used.
 *
 * @author Joshua Gleitze
 * @author Christoph Michelbach
 */
public interface Timeout {

	/**
	 * Determines whether the timeout is reached. Once this method has returned
	 * {@code true}, it will return {@code true} on all further ivocations.
	 *
	 * @return {@code true} if the timeout is reached; {@code false} otherwise.
	 */
	boolean isReached();

	/**
	 * Reports one step of progress to the policy. What one step of progress is is to be
	 * defined by the client. The timeout may use this information to make its decision.
	 */
	void reportOneStepProgress();

	/**
	 * Initialises the timeout object. This method must be called exactly once before any
	 * other method is called.
	 */
	void init();

	/**
	 * Registers {@code callback} to be run once the timeout is reached.
	 *
	 * @param callback A {@link Runnable} object.
	 */
	void registerCallback(final Runnable callback);

	/**
	 * Removes {@code callback} from the set of callbacks to be run once the timeout is
	 * reached.
	 *
	 * @param callback A {@link Runnable} object.
	 */
	void unregisterCallback(final Runnable callback);
}
