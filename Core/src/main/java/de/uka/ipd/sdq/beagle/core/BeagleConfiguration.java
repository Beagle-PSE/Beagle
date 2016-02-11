package de.uka.ipd.sdq.beagle.core;

import org.apache.commons.lang3.Validate;
import org.palladiosimulator.pcm.core.entity.Entity;

import java.io.File;
import java.util.List;

/**
 * Stores all settings done by the user. This object starts out with default values set to
 * it.
 * 
 * @author Christoph Michelbach
 */
public class BeagleConfiguration {

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
	 * All elements to measure or {@code null} to indicate that everything in
	 * {@code repositoryFile} should be analysed.
	 */
	private List<Entity> elements;

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
	 * Constructs a new {@link BeagleConfiguration} using {@code elements} as the default
	 * elements to be measured.
	 * 
	 * @param elements The elements to be measured or {@code null} to indicate that
	 *            everything in {@code repositoryFile} should be analysed.
	 * @param repositoryFile The repository file to use. Must not be {@code null}.
	 */
	public BeagleConfiguration(final List<Entity> elements, final File repositoryFile) {
		Validate.notNull(elements);
		Validate.notNull(repositoryFile);

		if (!repositoryFile.exists()) {
			throw new IllegalArgumentException("Repository file must exist.");
		}

		this.elements = elements;
		this.repositoryFile = repositoryFile;
		this.timeout = DEFAULT_TIMEOUT;
	}

	/**
	 * Returns the elements to be measured or {@code null} to indicate that everything in
	 * {@code repositoryFile} should be analysed.
	 * 
	 * @return The elements to be measured or {@code null} to indicate that everything in
	 *         {@code repositoryFile} should be analysed.
	 */
	public List<Entity> getElements() {
		return this.elements;
	}

	/**
	 * Sets the elements to be measured to {@code elements}. {@code null} to indicate that
	 * everything in {@code repositoryFile} should be analysed.
	 * 
	 * @param elements The elements to be measured or {@code null} to indicate that
	 *            everything in {@code repositoryFile} should be analysed.
	 */
	public void setElements(final List<Entity> elements) {
		this.elements = elements;
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
