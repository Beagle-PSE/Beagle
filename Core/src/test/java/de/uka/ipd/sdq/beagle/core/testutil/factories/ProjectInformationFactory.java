package de.uka.ipd.sdq.beagle.core.testutil.factories;

import de.uka.ipd.sdq.beagle.core.ProjectInformation;
import de.uka.ipd.sdq.beagle.core.facade.SourceCodeFileProvider;
import de.uka.ipd.sdq.beagle.core.timeout.NoTimeout;
import de.uka.ipd.sdq.beagle.core.timeout.Timeout;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.file.Files;

/**
 * Creates test project information instances.
 *
 * @author Joshua Gleitze
 */
public class ProjectInformationFactory implements SourceCodeFileProvider {

	/**
	 * See {@link Serializable}.
	 */
	private static final long serialVersionUID = -2919717213224100923L;

	/**
	 * Creates a new project information.
	 *
	 * @return A newly initialised project information you must not make any assumptions
	 *         about.
	 */
	public ProjectInformation getOne() {
		final Timeout timeout = new NoTimeout();
		return new ProjectInformation(timeout, this, "", Charset.defaultCharset(),
			new LaunchConfigurationFactory().getAllAsSet());
	}

	@Override
	public File getSourceFile(final String fullyQualifiedJavaPath) {

		try {
			return Files.createTempFile("testfile", ".java").toFile();
		} catch (final IOException ioException) {
			throw new RuntimeException(ioException);
		}
	}

	/**
	 * Creates a copy of the provided project information but sets the provided
	 * {@code timeout} on it.
	 *
	 * @param sourceInformation The Blackboard to copy.
	 * @param timeout The timeout to use on the copy.
	 * @return A Project Information with the same content, except that {@code timeout} is
	 *         set on it.
	 */
	public ProjectInformation setTimeout(final ProjectInformation sourceInformation, final Timeout timeout) {
		return new ProjectInformation(timeout, sourceInformation.getFileProvider(), sourceInformation.getBuildPath(),
			sourceInformation.getCharset(), sourceInformation.getLaunchConfigurations());
	}
}
