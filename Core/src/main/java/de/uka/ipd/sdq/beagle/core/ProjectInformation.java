package de.uka.ipd.sdq.beagle.core;

import de.uka.ipd.sdq.beagle.core.facade.BeagleConfiguration;
import de.uka.ipd.sdq.beagle.core.facade.SourceCodeFileProvider;

import java.io.Serializable;

/**
 * Hold information about the project under analysis. Objects of this class are immutable.
 *
 * @author Roman Langrehr
 */
public class ProjectInformation implements Serializable {

	/**
	 * See {@link Serializable}.
	 */
	private static final long serialVersionUID = 2451089669559562551L;

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
	 * Creates a new Project Information.
	 *
	 * @param timeout The timeout to be used
	 *
	 *            <table> <caption>timeout value description</caption>
	 *
	 *            <tr><td>{@link BeagleConfiguration#ADAPTIVE_TIMEOUT}</td><td>Beagle will
	 *            use a timeout that adapts to the quality of the analysis’ findings in
	 *            the past.</td>
	 *
	 *            <tr><td>{@link BeagleConfiguration#NO_TIMEOUT}</td><td>no timeout will
	 *            be used</td>
	 *
	 *            <tr><td>{@code ≥ 0}</td><td>the given value will be used as a fixed
	 *            timeout in seconds</td>
	 *
	 *            </table>
	 * @param fileProvider The provider of the source files to be analysed
	 */
	public ProjectInformation(final int timeout, final SourceCodeFileProvider fileProvider) {
		super();
		this.timeout = timeout;
		this.fileProvider = fileProvider;
	}

	/**
	 * Returns the timeout to be used. The timeout describes the minimum time Beagle shall
	 * keep trying to find results while no perfect results were found.
	 *
	 * <table> <caption>timeout value description</caption>
	 *
	 * <tr><td>{@link BeagleConfiguration#ADAPTIVE_TIMEOUT}</td><td>Beagle will use a
	 * timeout that adapts to the quality of the analysis’ findings in the past.</td>
	 *
	 * <tr><td>{@link BeagleConfiguration#NO_TIMEOUT}</td><td>no timeout will be used</td>
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
	 * Queries the provider responsible to get the source files Beagle shall analyse.
	 *
	 *
	 * @return The provider of source files.
	 */
	public SourceCodeFileProvider getFileProvider() {
		return this.fileProvider;
	}
}
