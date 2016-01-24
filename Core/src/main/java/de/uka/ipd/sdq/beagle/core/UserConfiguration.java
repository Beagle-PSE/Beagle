package de.uka.ipd.sdq.beagle.core;

import org.palladiosimulator.pcm.repository.BasicComponent;
import org.palladiosimulator.pcm.seff.InternalAction;

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
	 * All components to measure.
	 */
	private List<BasicComponent> components;

	/**
	 * All internal actions to measure.
	 */
	private List<InternalAction> internalActions;

	/**
	 * The timeout to be used. [-2 → adaptive timeout] [-1 → no timeout] [≥ 0 → timeout in
	 * seconds]
	 */
	private int timeout;

	/**
	 * Constructs a new {@link UserConfiguration} using {@code elements} as the default
	 * elements to be measured.
	 * 
	 * @param components The default components to be measured.
	 * @param internalActions The default internal actions to be measured.
	 */
	public UserConfiguration(final List<BasicComponent> components, final List<InternalAction> internalActions) {
		this.components = components;
		this.internalActions = internalActions;
		this.timeout = DEFAULT_TIMEOUT;
	}

	/**
	 * Returns the components to be measured.
	 * 
	 * @return The components to be measured.
	 */
	public List<BasicComponent> getComponents() {
		return this.components;
	}

	/**
	 * Sets the components to be measured to {@code elements}.
	 * 
	 * @param components The components to be measured.
	 */
	public void setComponents(final List<BasicComponent> components) {
		this.components = components;
	}

	/**
	 * Returns the internal actions to be measured.
	 * 
	 * @return The internal actions to be measured.
	 */
	public List<InternalAction> getInternalActions() {
		return this.internalActions;
	}

	/**
	 * Sets the internal actions to be measured to {@code internalActions}.
	 * 
	 * @param internalActions The internalActions to be measured.
	 */
	public void setInternalActions(final List<InternalAction> internalActions) {
		this.internalActions = internalActions;
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
