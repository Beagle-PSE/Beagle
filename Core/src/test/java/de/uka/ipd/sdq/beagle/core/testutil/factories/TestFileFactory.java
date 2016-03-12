package de.uka.ipd.sdq.beagle.core.testutil.factories;

import de.uka.ipd.sdq.beagle.core.CodeSection;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Provides methods to get one, or more, Files which can be used to create e.g. a
 * {@link CodeSection}.
 *
 * @author Annika Berger
 */
public class TestFileFactory {

	/**
	 * A temporary folder we can copy files to.
	 */
	private static File temporary;

	/**
	 * Caches files we’ve already copied.
	 */
	private static final Map<String, String> CACHE = new HashMap<>();

	/**
	 * Gives the first File in {@link #getAll()}.
	 *
	 * @return a file.
	 */
	public File getOne() {
		return this.getAll()[0];
	}

	/**
	 * Builds and returns an array of newly initialised files.
	 *
	 * @return all files as array
	 */
	public File[] getAll() {
		return new File[] {
			this.getFile("/de/uka/ipd/sdq/beagle/core/TestFile.java"),
			this.getFile("/de/uka/ipd/sdq/beagle/core/PingPong.java")
		};
	}

	/**
	 * Gets a File that contains what the resource denoted by
	 * {@code classpathRelativePath} contains. Copies the resource file into a temporary
	 * file and returns the path to that temporary file if necessary. This is needed if
	 * this class is executed out of a JAR, in which case no direct files can be returned.
	 *
	 * @param classpathRelativePath The classpath relative path of a resource.
	 * @return A file containing what the resource denoted by
	 *         {@code classpathRelativePath} contains.
	 */
	private File getFile(final String classpathRelativePath) {
		// did we look up this file before?
		if (CACHE.containsKey(classpathRelativePath)) {
			return new File(CACHE.get(classpathRelativePath));
		}

		try {
			final URL classpathURL = TestFileFactory.class.getResource(classpathRelativePath);
			if (classpathURL == null) {
				throw new FileNotFoundException(
					"Cannot find the resource " + classpathRelativePath + " on the classpath.");
			}

			final URI classpathURI = classpathURL.toURI();
			if (!"jar".equals(classpathURI.getScheme())) {
				// the resource is a normal file, so we’ll use that
				final File classpathFile = new File(classpathURI);
				CACHE.put(classpathRelativePath, classpathFile.getAbsolutePath());
				return classpathFile;
			}

			// the resource is in a jar, so we’ll copy it into a normal file and use that.
			if (temporary == null) {
				temporary = Files.createTempDirectory("Beagle Tests").toFile();
				temporary.deleteOnExit();
			}

			final File outputFile = new File(temporary, new File(classpathRelativePath).getName());
			final InputStream resourceStream = TestFileFactory.class.getResourceAsStream(classpathRelativePath);
			IOUtils.copy(resourceStream, new FileOutputStream(outputFile));

			CACHE.put(classpathRelativePath, outputFile.getAbsolutePath());
			return outputFile;

		} catch (final IOException | URISyntaxException ioError) {
			throw new RuntimeException("Could not load test file " + classpathRelativePath, ioError);
		}
	}

	/**
	 * Builds and returns a set of newly initialised files.
	 *
	 * @return all files as set
	 */
	public Set<File> getAllAsSet() {
		return new HashSet<>(Arrays.asList(this.getAll()));
	}

}
