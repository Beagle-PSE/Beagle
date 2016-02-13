package de.uka.ipd.sdq.beagle.core.testutil.factories;

import de.uka.ipd.sdq.beagle.core.ProjectInformation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Creates test project information instances.
 *
 * @author Joshua Gleitze
 */
public class ProjectInformationFactory {

	/**
	 * Creates a new project information.
	 *
	 * @return A newly initialised project information you must not make any assumptions
	 *         about.
	 */
	public ProjectInformation getOne() {
		return new ProjectInformation(0, this::sourceFileProvider, "", null);
	}

	/**
	 * Source file provider for project informations. Creates temporary files, so that
	 * checks whether the file exists will succeed.
	 *
	 * @param name Fully qualified name of a Java type.
	 * @return A dummy file.
	 */
	private File sourceFileProvider(final String name) {
		try {
			return Files.createTempFile("testfile", ".java").toFile();
		} catch (final IOException ioException) {
			throw new RuntimeException(ioException);
		}
	}
}
