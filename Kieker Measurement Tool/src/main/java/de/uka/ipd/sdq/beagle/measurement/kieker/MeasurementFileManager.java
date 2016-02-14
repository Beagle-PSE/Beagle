package de.uka.ipd.sdq.beagle.measurement.kieker;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.Validate;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Administers all files required by the {@link KiekerMeasurementTool}. The methods return
 * paths to files that don’t necessarily exist.
 *
 * @author Joshua Gleitze
 */
class MeasurementFileManager {

	/**
	 * Name of the partition containing instrumented source code.
	 */
	private static final String INSTRUMENTED_SOURCE_CODE_PARTITION_NAME = "instrumented-src";

	/**
	 * Name of the partition containing compilation results.
	 */
	private static final String COMPILED_BYTE_CODE_PARTITION_NAME = "bin";

	/**
	 * Name of the partition containing Kieker’s results.
	 */
	private static final String KIEKER_RESULT_PARTIOTION_NAME = "kieker-result";

	/**
	 * Classpath-relative path to the Kieker jar.
	 */
	private static final String KIEKER_JAR_PATH = "/de/uka/ipd/sdq/beagle/measurement/kieker/kieker-1.12.jar";

	/**
	 * Classpath-relative path to the remote code folder.
	 */
	private static final String REMOTE_CODE_PATH = "/de/uka/ipd/sdq/beagle/measurement/kieker/remote";

	/**
	 * Classpath-relative path to the kieker monitoring configuration file.
	 */
	private static final String KIEKER_CONFIG_FILE =
		"/de/uka/ipd/sdq/beagle/measurement/kieker/remote/kieker.monitoring.properties";

	/**
	 * The root folder, containing all partitions.
	 */
	private final Path rootFolder;

	/**
	 * Path to the Kieker jar.
	 */
	private final Path kiekerJar;

	/**
	 * The folder containing the measurement classes to be used in the measured software.
	 */
	private final Path remoteCodeFolder;

	/**
	 * Path to the kieker configuration file.
	 */
	private final Path kiekerConfigFile;

	/**
	 * Initialises the file manager.
	 *
	 * @throws IOException If creating the basic file structure required by this manager
	 *             fails or required files are missing.
	 */
	MeasurementFileManager() throws IOException {
		this.rootFolder = Files.createTempDirectory("beagle-kieker-measurement");
		FileUtils.forceDeleteOnExit(this.rootFolder.toFile());
		Files.createDirectory(this.getInstrumentedSourceCodeFolder());
		Files.createDirectory(this.getCompiledByteCodeFolder());
		this.kiekerJar = this.loadFromClasspath(KIEKER_JAR_PATH, "Kieker jar");
		this.remoteCodeFolder = this.loadFromClasspath(REMOTE_CODE_PATH, "remote measurment code");
		this.kiekerConfigFile = this.loadFromClasspath(KIEKER_CONFIG_FILE, "Kieker configuration file");

	}

	/**
	 * Tries to load the given {@code path} from the classpath. Throws an exception
	 * containing the given {@code description} if this fails.
	 *
	 * @param path The path to retrieve.
	 * @param description Description of what is expected to be found at {@code path}.
	 * @return The retrieved path.
	 * @throws FileNotFoundException If {@code path} cannot be found.
	 */
	private Path loadFromClasspath(final String path, final String description) throws FileNotFoundException {
		final URL classpathUrl = MeasurementFileManager.class.getResource(KIEKER_JAR_PATH);
		if (classpathUrl == null) {
			throw new FileNotFoundException(String.format("Cannot find the %s!", description));
		}
		try {
			return Paths.get(classpathUrl.toURI()).toAbsolutePath();
		} catch (final URISyntaxException uriSyntaxError) {
			throw new FileNotFoundException(String.format("Retrieving the %s resulted in a malformed URI:\n%s",
				description, uriSyntaxError.getMessage()));
		}
	}

	/**
	 * Queries where to store the instrumentation result for the Java type denoted by the
	 * given {@code fullyQualifiedName}.
	 *
	 * @param fullyQualifiedName The fully qualified name of the instrumented java type.
	 *            Must not be {@code null}.
	 * @return The path to the file to store the instrumented code in. Will never be
	 *         {@code null}.
	 */
	Path getInstrumentationFileFor(final String fullyQualifiedName) {
		Validate.notNull(fullyQualifiedName);
		return this.getInstrumentedSourceCodeFolder().resolve(fullyQualifiedName.replace('.', '/')).toAbsolutePath();
	}

	/**
	 * Queries the path to the folder to put all instrumented source code files in.
	 *
	 * @return The folder for all instrumented source code. Will never be {@code null}.
	 */
	Path getInstrumentedSourceCodeFolder() {
		return this.rootFolder.resolve(INSTRUMENTED_SOURCE_CODE_PARTITION_NAME).toAbsolutePath();
	}

	/**
	 * Queries the path to the folder to put all compiled Java byte code in.
	 *
	 * @return The folder for all compiled source code. Will never be {@code null}.
	 */
	Path getCompiledByteCodeFolder() {
		return this.rootFolder.resolve(COMPILED_BYTE_CODE_PARTITION_NAME).toAbsolutePath();
	}

	/**
	 * Queries the path to the folder to put Kieker’s results in.
	 *
	 * @return The folder for Kieker’s results.
	 */
	Path getKiekerResultsFolder() {
		return this.rootFolder.resolve(KIEKER_RESULT_PARTIOTION_NAME).toAbsolutePath();
	}

	/**
	 * Queries the path to the Kieker jar.
	 *
	 * @return The path to the Kieker jar. Will never be {@code null}.
	 */
	Path getKiekerJar() {
		return this.kiekerJar;
	}

	/**
	 * Queries the folder containing the compiled measurement code from the
	 * {@code de.uka.ipd.sdq.beagle.measurement.kieker} package.
	 *
	 * @return The path to the remote measurement code. Will never be {@code null}.
	 */
	Path getMeasurementRemotePackage() {
		return this.remoteCodeFolder;
	}

	/**
	 * Queries the path to the kieker configuration file.
	 *
	 * @return The path to the kieker monitoring configuration file. Will never be
	 *         {@code null}.
	 */
	Path getKiekerConfigurationFile() {
		return this.kiekerConfigFile;
	}
}
