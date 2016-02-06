package de.uka.ipd.sdq.beagle.core;

import org.apache.commons.lang3.Validate;
import org.palladiosimulator.pcm.repository.BasicComponent;
import org.palladiosimulator.pcm.seff.InternalAction;

import java.io.File;
import java.util.LinkedList;
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
	 * All components to measure or {@code null} to indicate that everything in
	 * {@code repositoryFile} should be analysed.
	 */
	private List<BasicComponent> components;

	/**
	 * All internal actions to measure or {@code null} to indicate that everything in
	 * {@code repositoryFile} should be analysed.
	 */
	private List<InternalAction> internalActions;

	/**
	 * The repository file.
	 */
	private File repositoryFile;

	/**
	 * The timeout to be used. [-2 → adaptive timeout] [-1 → no timeout] [≥ 0 → timeout in
	 * seconds]
	 */
	private int timeout;

	/**
	 * Constructs a new {@link UserConfiguration} using {@code elements} as the default
	 * elements to be measured.
	 * 
	 * @param componentsParam The default components to be measured or {@code null} to
	 *            indicate that everything in {@code repositoryFile} should be analysed.
	 * @param internalActionsParam The default internal actions to be measured or
	 *            {@code null} to indicate that everything in {@code repositoryFile}
	 *            should be analysed.
	 * @param repositoryFile The repository file to use. Must not be {@code null}.
	 */
	public UserConfiguration(final List<BasicComponent> componentsParam,
		final List<InternalAction> internalActionsParam, final File repositoryFile) {
		Validate.notNull(repositoryFile);

		List<BasicComponent> componentsA = componentsParam;
		List<InternalAction> internalActionsA = internalActionsParam;

		if ((componentsA == null) ^ (internalActionsA == null)) {
			if (componentsA == null) {
				componentsA = new LinkedList<BasicComponent>();
			} else {
				internalActionsA = new LinkedList<InternalAction>();
			}
		}

		this.components = componentsA;
		this.internalActions = internalActionsA;
		this.repositoryFile = repositoryFile;
		this.timeout = DEFAULT_TIMEOUT;
	}

	/**
	 * Returns the components to be measured or {@code null} to indicate that everything
	 * in {@code repositoryFile} should be analysed.
	 * 
	 * @return The components to be measured or {@code null} to indicate that everything
	 *         in {@code repositoryFile} should be analysed.
	 */
	public List<BasicComponent> getComponents() {
		return this.components;
	}

	/**
	 * Sets the components to be measured to {@code elements}. {@code null} to indicate
	 * that everything in {@code repositoryFile} should be analysed.
	 * 
	 * @param components The components to be measured or {@code null} to indicate that
	 *            everything in {@code repositoryFile} should be analysed.
	 */
	public void setComponents(final List<BasicComponent> components) {
		this.components = components;
	}

	/**
	 * Returns the internal actions to be measured or {@code null} to indicate that
	 * everything in {@code repositoryFile} should be analysed.
	 * 
	 * @return The internal actions to be measured or {@code null} to indicate that
	 *         everything in {@code repositoryFile} should be analysed.
	 */
	public List<InternalAction> getInternalActions() {
		return this.internalActions;
	}

	/**
	 * Sets the internal actions to be measured to {@code internalActions}. {@code null}
	 * to indicate that everything in {@code repositoryFile} should be analysed.
	 * 
	 * @param internalActions The internalActions to be measured or {@code null} to
	 *            indicate that everything in {@code repositoryFile} should be analysed.
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

	/**
	 * Returns the repository file.
	 *
	 * @return the repository file.
	 */
	public File getRepositoryFile() {
		return this.repositoryFile;
	}

}
