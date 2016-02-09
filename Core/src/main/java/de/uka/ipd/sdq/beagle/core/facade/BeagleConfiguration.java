package de.uka.ipd.sdq.beagle.core.facade;

import org.apache.commons.lang3.Validate;
import org.palladiosimulator.pcm.core.entity.Entity;

import java.io.File;
import java.util.List;

/**
 * Configures a whole execution of Beagle. Therefore contains all values needed to set up
 * Beagle. The class defines meaningful default values for all its settings.
 *
 * @author Christoph Michelbach
 * @author Joshua Gleitze
 */
public class BeagleConfiguration {

	/**
	 * Numeric value for "adaptive timeout".
	 *
	 * @see #getTimeout()
	 */
	public static final int ADAPTIVE_TIMEOUT = -2;

	/**
	 * Numeric value for "no timeout".
	 *
	 * @see #getTimeout()
	 */
	public static final int NO_TIMEOUT = -1;

	/**
	 * The default setting for the {@linkplain #getTimeout() timeout}.
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
	private final File repositoryFile;

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
		Validate.notNull(repositoryFile);

		this.elements = elements;
		this.repositoryFile = repositoryFile;
		this.timeout = DEFAULT_TIMEOUT;
	}

	/**
	 * Returns the elements to be measured or {@code null} to indicate that everything in
	 * the {@linkplain #getRepositoryFile() repository file} should be analysed.
	 *
	 * @return The elements to be measured or {@code null} to indicate that everything in
	 *         the {@linkplain #getRepositoryFile() repository file} should be analysed.
	 */
	public List<Entity> getElements() {
		return this.elements;
	}

	/**
	 * Sets the elements to be measured to {@code elements}. {@code null} indicates that
	 * everything in the {@linkplain #getRepositoryFile() repository file} should be
	 * analysed.
	 *
	 * @param elements The elements to be measured or {@code null} to indicate that
	 *            everything in the {@linkplain #getRepositoryFile() repository file}
	 *            should be analysed.
	 * @see #getElements()
	 */
	public void setElements(final List<Entity> elements) {
		this.elements = elements;
	}

	/**
	 * Returns the timeout to be used.
	 *
	 * <table> <caption>timeout value description</caption>
	 *
	 * <tr><td>{@link #ADAPTIVE_TIMEOUT}</td><td>Beagle will use a timeout that adapts to
	 * the quality of the analysis’ findings in the past.</td>
	 *
	 * <tr><td>{@link #NO_TIMEOUT}</td><td>no timeout will be used</td>
	 *
	 * <tr><td>{@code ≥ 0}</td><td>the given value will be used as a fixed timeout in
	 * seconds</td>
	 *
	 * </table>
	 *
	 * @return The timeout that will be used by Beagle.
	 */
	public int getTimeout() {
		return this.timeout;
	}

	/**
	 * Sets the timeout to be used to {@code timeout}. The timeout describes the minimum
	 * time Beagle shall keep trying to find results while no perfect results were found.
	 *
	 * <table> <caption>timeout value description</caption>
	 *
	 * <tr><td>{@link #ADAPTIVE_TIMEOUT}</td><td>Beagle will use a timeout that adapts to
	 * the quality of the analysis’ findings in the past.</td>
	 *
	 * <tr><td>{@link #NO_TIMEOUT}</td><td>no timeout will be used</td>
	 *
	 * <tr><td>{@code ≥ 0}</td><td>the given value will be used as a fixed timeout in
	 * seconds</td>
	 *
	 * </table>
	 *
	 * @param timeout The timeout to be used by Beagle.
	 */
	public void setTimeout(final int timeout) {
		this.timeout = timeout;
	}

	/**
	 * Returns the repository file that contains all elements that shall be analysed.
	 *
	 * @return The repository file Beagle will operate on.
	 */
	public File getRepositoryFile() {
		return this.repositoryFile;
	}

}
