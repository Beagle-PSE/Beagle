package de.uka.ipd.sdq.beagle.core.testutil.factories;

import static org.mockito.Mockito.mock;

import de.uka.ipd.sdq.beagle.core.ProjectInformation;
import de.uka.ipd.sdq.beagle.core.facade.SourceCodeFileProvider;
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
		final Timeout timeout = mock(Timeout.class);
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
}
