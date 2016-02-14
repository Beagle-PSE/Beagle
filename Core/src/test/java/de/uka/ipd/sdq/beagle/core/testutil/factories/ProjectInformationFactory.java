package de.uka.ipd.sdq.beagle.core.testutil.factories;

import de.uka.ipd.sdq.beagle.core.ProjectInformation;
import de.uka.ipd.sdq.beagle.core.facade.SourceCodeFileProvider;

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
		return new ProjectInformation(0, this, "", Charset.defaultCharset(),
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
