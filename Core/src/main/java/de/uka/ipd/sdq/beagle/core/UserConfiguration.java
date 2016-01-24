package de.uka.ipd.sdq.beagle.core;

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
	public static final int ADAPTIVE_TIMEOUT = -2;

	/**
	 * Numeric value for "no timeout".
	 */
	public static final int NO_TIMEOUT = -1;

	/**
	 * The default setting for the timeout. [-2 → adaptive timeout] [-1 → no timeout] [≥ 0
	 * → timeout in seconds]
	 */
	public static final int DEFAULT_TIMEOUT = ADAPTIVE_TIMEOUT;

	/**
	 * All elements to measure.
	 */
	private List<String> elements;

	/**
	 * The timeout to be used. [-2 → adaptive timeout] [-1 → no timeout] [≥ 0 → timeout in
	 * seconds]
	 */
	private int timeout;

	/**
	 * Constructs a new {@link UserConfiguration} using {@code elements} as the default
	 * elements to be measured.
	 * 
	 * @param elements The default elements to be measured.
	 */
	public UserConfiguration(final List<String> elements) {
		this.elements = elements;
		this.timeout = DEFAULT_TIMEOUT;
	}

	/**
	 * Returns the elements to be measured.
	 * 
	 * @return The elements to be measured.
	 */
	public List<String> getElements() {
		return this.elements;
	}

	/**
	 * Sets the elements to be measured to {@code elements}.
	 * 
	 * @param elements The elements to be measured to {@code elements}.
	 */
	public void setElements(final List<String> components) {
		this.elements = components;
	}

	/**
	 * Returns the timeout to be used. [-2 → adaptive timeout] [-1 → no timeout] [≥ 0 →
	 * timeout in seconds]
	 * 
	 * @return The timeout to be used. [-2 → adaptive timeout] [-1 → no timeout] [≥ 0 →
	 *         timeout in seconds]
	 */
	public int getTimeout() {
		return this.timeout;
	}

	/**
	 * Sets the timeout to be used to {@code timeout}. [-2 → adaptive timeout] [-1 → no
	 * timeout] [≥ 0 → timeout in seconds]
	 * 
	 * @param timeout The timeout to be used to {@code timeout}. [-2 → adaptive timeout]
	 *            [-1 → no timeout] [≥ 0 → timeout in seconds]
	 */
	public void setTimeout(final int timeout) {
		this.timeout = timeout;
	}

}
