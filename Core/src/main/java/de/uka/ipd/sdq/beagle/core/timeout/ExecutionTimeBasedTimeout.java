package de.uka.ipd.sdq.beagle.core.timeout;

import org.apache.commons.lang3.Validate;

import java.util.HashSet;
import java.util.Set;

/**
 * Abstract class for timeouts deciding when to stop based on the executed program’s run
 * time. Subclasses should always call {@code super.init()} when overriding
 * {@link #init()}. They are also encouraged to check on the {@link #initialised} flag in
 * all methods.The class furthermore provides functionality to register and unregister
 * callbacks.
 *
 * @author Christoph Michelbach
 */
public abstract class ExecutionTimeBasedTimeout implements Timeout {

	/**
	 * Indicates whether this object has been initialised.
	 */
	protected boolean initialised;

	/**
	 * The millisecond timestamp indicating when this Timeout object was initialised.
	 */
	protected long startingTime;

	/**
	 * A set of callbacks which will be called once the timeout is reached.
	 */
	protected final Set<Runnable> callbacks = new HashSet<>();

	@Override
	public void init() {
		Validate.validState(!this.initialised);

		this.startingTime = System.currentTimeMillis();
		this.initialised = true;
	}

	@Override
	public void registerCallback(final Runnable callback) {
		this.callbacks.add(callback);
	}

	@Override
	public void unregisterCallback(final Runnable callback) {
		this.callbacks.remove(callback);
	}
}
