package de.uka.ipd.sdq.beagle.gui;

import java.util.List;

/**
 * Stores all settings done by the user. This object starts out with default values set to
 * it.
 * 
 * @author Christoph Michelbach
 */
public class UserConfiguration {

	/**
	 * Numeric value for "adaptive timeout".
	 */
	private static final int ADAPTIVE_TIMEOUT = -2;

	/**
	 * Numeric value for "no timeout".
	 */
	private static final int NO_TIMEOUT = -1;

	/**
	 * The default setting for the timeout. [-2 → adaptive timeout] [-1 → no timeout] [>=
	 * 0 → timeout in seconds]
	 */
	private static final int DEFAULT_TIMEOUT = ADAPTIVE_TIMEOUT;

	/**
	 * All components to measure.
	 */
	private List<String> components;

	/**
	 * The timeout to be used. [-2 → adaptive timeout] [-1 → no timeout] [>= 0 → timeout
	 * in seconds]
	 */
	private int timeout;

	/**
	 * Constructs a new {@link UserConfiguration} using {@code components} as the default
	 * components to be measured.
	 * 
	 * @param components The default components to be measured.
	 */
	public UserConfiguration(final List<String> components) {
		this.components = components;
	}

	/**
	 * Returns the components to be measured.
	 * 
	 * @return The components to be measured.
	 */
	public List<String> getComponents() {
		return this.components;
	}

	/**
	 * Sets the components to be measured to {@code components}.
	 * 
	 * @param components The components to be measured to {@code components}.
	 */
	public void setComponents(final List<String> components) {
		this.components = components;
	}

	/**
	 * Returns the timeout to be used. [-2 → adaptive timeout] [-1 → no timeout] [>= 0 →
	 * timeout in seconds]
	 * 
	 * @return The timeout to be used. [-2 → adaptive timeout] [-1 → no timeout] [>= 0 →
	 *         timeout in seconds]
	 */
	public int getTimeout() {
		return this.timeout;
	}

	/**
	 * Sets the timeout to be used to {@code timeout}. [-2 → adaptive timeout] [-1 → no
	 * timeout] [>= 0 → timeout in seconds]
	 * 
	 * @param timeout The timeout to be used to {@code timeout}. [-2 → adaptive timeout]
	 *            [-1 → no timeout] [>= 0 → timeout in seconds]
	 */
	public void setTimeout(final int timeout) {
		this.timeout = timeout;
	}

}
