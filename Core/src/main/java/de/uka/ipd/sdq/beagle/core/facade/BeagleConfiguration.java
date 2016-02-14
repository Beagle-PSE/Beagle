package de.uka.ipd.sdq.beagle.core.facade;

import org.apache.commons.lang3.Validate;
import org.palladiosimulator.pcm.core.entity.Entity;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * Configures a whole execution of Beagle. Therefore contains all values needed to set up
 * Beagle. The class defines meaningful default values for all its settings.
 *
 * <p>The configuration has two states: A <em>set up</em> state, during which values may
 * be modified and a <em>finalised</em> state, in which the configuration is immutable and
 * can only be used to obtain values. The transition from the <em>set up</em> state to the
 * <em>finalised</em> state is done through {@link #finalise()}. The inverse transition is
 * not possible. Trying to perform an action that is not allowed for the momentary state
 * results in an {@link IllegalStateException} being thrown.
 *
 * @author Christoph Michelbach
 * @author Joshua Gleitze
 * @author Michael Vogt
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
	private File repositoryFile;

	/**
	 * The timeout to be used. [-2 → adaptive timeout] [-1 → no timeout] [≥ 0 → timeout in
	 * seconds]
	 */
	private int timeout;

	/**
	 * The provider of the source files to be analysed.
	 */
	private SourceCodeFileProvider fileProvider;

	/**
	 * Whether this configuration is in the <em>finalised</em> state.
	 */
	private boolean finalised;

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

		if (!repositoryFile.exists()) {
			throw new IllegalArgumentException("Repository file must exist.");
		}

		this.elements = new LinkedList<>(elements);
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
	 * analysed. This operation is only allowed in the <em>set up</em> state.
	 *
	 *
	 * @param elements The elements to be measured or {@code null} to indicate that
	 *            everything in the {@linkplain #getRepositoryFile() repository file}
	 *            should be analysed.
	 * @throws IllegalStateException If this configuration is not in the <em>set up</em>
	 *             state.
	 * @see #getElements()
	 */
	public void setElements(final List<Entity> elements) {
		Validate.validState(!this.finalised,
			"setting values is only allowed if this configuration is not yet finalised");
		if (elements == null) {
			this.elements = null;
		} else {
			this.elements = new LinkedList<>(elements);
		}
	}

	/**
	 * Returns the timeout to be used. The timeout describes the minimum time Beagle shall
	 * keep trying to find results while no perfect results were found.
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
	 * This operation is only allowed in the <em>set up</em> state.
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
	 * @throws IllegalStateException If this configuration is not in the <em>set up</em>
	 *             state.
	 */
	public void setTimeout(final int timeout) {
		Validate.validState(!this.finalised,
			"setting values is only allowed if this configuration is not yet finalised");
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

	/**
	 * Sets the repository file containing the elements Beagle shall analyse. This
	 * operation is only allowed in the <em>set up</em> state.
	 *
	 *
	 * @param repositoryFile The pcm repository file containing all elements Beagle shall
	 *            analyse. Must not be {@code null}.
	 * @throws IllegalStateException If this configuration is not in the <em>set up</em>
	 *             state.
	 */
	public void setRepositoryFile(final File repositoryFile) {
		Validate.validState(!this.finalised,
			"setting values is only allowed if this configuration is not yet finalised");
		Validate.notNull(repositoryFile);
		this.repositoryFile = repositoryFile;
	}

	/**
	 * Queries wether this configuration is in the <em>finalised</em> state.
	 *
	 * @return {@code true} if this configuration is in the <em>finalised</em> state,
	 *         {@code false} if it’s in the <em>set up</em> state.
	 */
	public boolean isFinal() {
		return this.finalised;
	}

	/**
	 * Finalises this configuration, thus transitioning it into the <em>finalised</em>
	 * state. Calling this method when this configuration alread is in the
	 * <em>finalised</em> state has no effect.
	 */
	public void finalise() {
		this.finalised = true;
	}

	/**
	 * Queries the provider responsible to get the source files Beagle shall analyse.
	 *
	 *
	 * @return The provider of source files.
	 */
	public SourceCodeFileProvider getFileProvider() {
		return this.fileProvider;
	}

	/**
	 * Sets the provider responsible to get the source files Beagle shall analyse.
	 *
	 * @param fileProvider The provider of source files. Must not be {@code null}.
	 *
	 * @throws IllegalStateException If this configuration is not in the <em>set up</em>
	 *             state.
	 */
	public void setFileProvider(final SourceCodeFileProvider fileProvider) {
		Validate.validState(!this.finalised,
			"setting values is only allowed if this configuration is not yet finalised");
		Validate.notNull(fileProvider);
		this.fileProvider = fileProvider;
	}

}
