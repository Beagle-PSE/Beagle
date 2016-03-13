package de.uka.ipd.sdq.beagle.measurement.kieker;

import de.uka.ipd.sdq.beagle.core.failurehandling.FailureHandler;
import de.uka.ipd.sdq.beagle.core.failurehandling.FailureReport;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.Validate;
import org.eclipse.core.runtime.FileLocator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Administers all files required by the {@link KiekerMeasurementTool}. The methods return
 * paths to files that don’t necessarily exist. The manager must be set up through
 * {@link #allocate()} before it may be used.
 *
 * @author Joshua Gleitze
 * @author Roman Langrehr
 */
class MeasurementFileManager {

	/**
	 * Handler of failures.
	 */
	private static final FailureHandler FAILURE_HANDLER = FailureHandler.getHandler("Measurement I/O");

	/**
	 * Name of the partition containing instrumented source code.
	 */
	private static final String INSTRUMENTED_SOURCE_CODE_PARTITION_NAME = "instrumented-src";

	/**
	 * Name of the partition containing compilation results.
	 */
	private static final String COMPILED_BYTE_CODE_PARTITION_NAME = "bin";

	/**
	 * Name of the partition containing Kieker’s results before they have been processed.
	 */
	private static final String KIEKER_NEW_RESULTS_PARTIOTION_NAME = "kieker-result-new";

	/**
	 * Name of the partition containing Kieker’s results after they have been processed.
	 */
	private static final String KIEKER_PROCESSED_RESULTS_PARTITION_NAME = "kieker-result-processed";

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
		"/de/uka/ipd/sdq/beagle/measurement/kieker/kieker.monitoring.properties";

	/**
	 * The root folder, containing all partitions.
	 */
	private Path rootFolder;

	/**
	 * Path to the Kieker jar.
	 */
	private Path kiekerJar;

	/**
	 * The folder containing the measurement classes to be used in the measured software.
	 */
	private Path remoteCodeFolder;

	/**
	 * Path to the kieker configuration file.
	 */
	private Path kiekerConfigFile;

	/**
	 * Whether {@link #allocate()} was run yet.
	 */
	private boolean wasSetUp;

	/**
	 * Creates the file manager.
	 */
	MeasurementFileManager() {
	}

	/**
	 * Initialises the file manager, allocating the needed folders. This method may be run
	 * multiple times to create a new file structure.
	 */
	void allocate() {
		try {
			this.wasSetUp = true;
			this.rootFolder = Files.createTempDirectory("beagle-kieker-measurement");
			FileUtils.forceDeleteOnExit(this.rootFolder.toFile());
			Files.createDirectory(this.getInstrumentedSourceCodeFolder());
			Files.createDirectory(this.getCompiledByteCodeFolder());
			Files.createDirectory(this.getKiekerResultsFolder());
			Files.createDirectory(this.getKiekerProcessedResultsFolder());
			this.kiekerJar = this.loadFromClasspath(KIEKER_JAR_PATH, "Kieker jar");
			this.remoteCodeFolder = this.loadFromClasspath(REMOTE_CODE_PATH, "remote measurment code");
			this.kiekerConfigFile = this.loadFromClasspath(KIEKER_CONFIG_FILE, "Kieker configuration file");
		} catch (final IOException ioError) {
			final FailureReport<Void> failure = new FailureReport<Void>().cause(ioError)
				.message("Creating the measurment folder structure failed.")
				.retryWith(this::allocate);
			this.wasSetUp = false;
			FAILURE_HANDLER.handle(failure);
		}
	}

	/**
	 * Tries to load the given {@code path} from the classpath. Throws an exception
	 * containing the given {@code description} if this fails.
	 *
	 * @param path The path to retrieve.
	 * @param description Description of what is expected to be found at {@code path}.
	 * @return The retrieved path.
	 * @throws IOException If retrieving the {@code path} is not possible.
	 */
	private Path loadFromClasspath(final String path, final String description) throws IOException {
		final URL classpathUrl = MeasurementFileManager.class.getResource(path);
		if (classpathUrl == null) {
			throw new FileNotFoundException(String.format("Cannot find the %s!", description));
		}
		final URL fileUrl = FileLocator.toFileURL(classpathUrl);
		try {
			// The URL to Path conversion has to be done like this, to make it work with
			// Linux and Windows style paths.
			final URI urii = new URI(fileUrl.getProtocol(), fileUrl.getPath(), null);
			final Path returnValue = Paths.get(urii).toAbsolutePath();
			return returnValue;
		} catch (final URISyntaxException uriSyntaxException) {
			throw new FileNotFoundException(uriSyntaxException.getMessage());
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
	 * @throw IllegalStateException If this manager was not yet set up through
	 *        {@link #allocate()}.
	 */
	Path getInstrumentationFileFor(final String fullyQualifiedName) {
		Validate.notNull(fullyQualifiedName);
		Validate.validState(this.wasSetUp, "The file manager was not yet set up!");
		return this.getInstrumentedSourceCodeFolder()
			.resolve(fullyQualifiedName.replace('.', '/') + ".java")
			.toAbsolutePath();
	}

	/**
	 * Queries the path to the folder to put all instrumented source code files in.
	 *
	 * @return The folder for all instrumented source code. Will never be {@code null}.
	 * @throw IllegalStateException If this manager was not yet set up through
	 *        {@link #allocate()}.
	 */
	Path getInstrumentedSourceCodeFolder() {
		Validate.validState(this.wasSetUp, "The file manager was not yet set up!");
		return this.rootFolder.resolve(INSTRUMENTED_SOURCE_CODE_PARTITION_NAME).toAbsolutePath();
	}

	/**
	 * Queries the path to the folder to put all compiled Java byte code in.
	 *
	 * @return The folder for all compiled source code. Will never be {@code null}.
	 * @throw IllegalStateException If this manager was not yet set up through
	 *        {@link #allocate()}.
	 */
	Path getCompiledByteCodeFolder() {
		Validate.validState(this.wasSetUp, "The file manager was not yet set up!");
		return this.rootFolder.resolve(COMPILED_BYTE_CODE_PARTITION_NAME).toAbsolutePath();
	}

	/**
	 * Queries the path to the folder to put Kieker’s results in.
	 *
	 * @return The folder for Kieker’s results.
	 * @throw IllegalStateException If this manager was not yet set up through
	 *        {@link #allocate()}.
	 */
	Path getKiekerResultsFolder() {
		Validate.validState(this.wasSetUp, "The file manager was not yet set up!");
		return this.rootFolder.resolve(KIEKER_NEW_RESULTS_PARTIOTION_NAME).toAbsolutePath();
	}

	/**
	 * Queries the path to the folder to put processed Kieker results into.
	 *
	 * @return The folder for processed Kieker results.
	 */
	private Path getKiekerProcessedResultsFolder() {
		return this.rootFolder.resolve(KIEKER_PROCESSED_RESULTS_PARTITION_NAME).toAbsolutePath();
	}

	/**
	 * Queries the path to the Kieker jar.
	 *
	 * @return The path to the Kieker jar. Will never be {@code null}.
	 * @throw IllegalStateException If this manager was not yet set up through
	 *        {@link #allocate()}.
	 */
	Path getKiekerJar() {
		Validate.validState(this.wasSetUp, "The file manager was not yet set up!");
		return this.kiekerJar;
	}

	/**
	 * Queries the folder containing the compiled measurement code from the
	 * {@code de.uka.ipd.sdq.beagle.measurement.kieker} package.
	 *
	 * @return The path to the remote measurement code. Will never be {@code null}.
	 * @throw IllegalStateException If this manager was not yet set up through
	 *        {@link #allocate()}.
	 */
	Path getMeasurementRemotePackage() {
		Validate.validState(this.wasSetUp, "The file manager was not yet set up!");
		return this.remoteCodeFolder;
	}

	/**
	 * Queries the path to the kieker configuration file.
	 *
	 * @return The path to the kieker monitoring configuration file. Will never be
	 *         {@code null}.
	 * @throw IllegalStateException If this manager was not yet set up through
	 *        {@link #allocate()}.
	 */
	Path getKiekerConfigurationFile() {
		Validate.validState(this.wasSetUp, "The file manager was not yet set up!");
		return this.kiekerConfigFile;
	}

	/**
	 * Copies the remote measurement code to the compiled instrumented code.
	 *
	 * @throws IOException If the copy operation fails.
	 * @throw IllegalStateException If this manager was not yet set up through
	 *        {@link #allocate()}.
	 */
	void copyRemoteMeasurementByteCodeToInstrumentedByteCode() {
		Validate.validState(this.wasSetUp, "The file manager was not yet set up!");
		try {
			FileUtils.copyDirectory(this.getMeasurementRemotePackage().toFile(),
				this.getCompiledByteCodeFolder().resolve(REMOTE_CODE_PATH.substring(1)).toFile());
		} catch (final IOException ioError) {
			final FailureReport<Void> failure = new FailureReport<Void>().cause(ioError)
				.message("Preparing the measurement remote code failed.")
				.retryWith(this::copyRemoteMeasurementByteCodeToInstrumentedByteCode);
			FAILURE_HANDLER.handle(failure);
		}
	}

	/**
	 * Moves the results in the {@linkplain Kieker results folder} to the done partition.
	 * This marks them as being processed and prevents them from being processed again.
	 *
	 * @throws IOException If moving the files fails.
	 * @throw IllegalStateException If this manager was not yet set up through
	 *        {@link #allocate()}.
	 */
	void moveKiekerResultsToDone() {
		Validate.validState(this.wasSetUp, "The file manager was not yet set up!");
		try {
			for (final File result : this.getKiekerResultsFolder().toFile().listFiles()) {
				if (result.isDirectory()) {
					FileUtils.moveDirectoryToDirectory(result, this.getKiekerProcessedResultsFolder().toFile(), false);
				} else {
					FileUtils.moveFileToDirectory(result, this.getKiekerProcessedResultsFolder().toFile(), false);
				}
			}
		} catch (final IOException ioError) {
			final FailureReport<Void> failure = new FailureReport<Void>().cause(ioError)
				.message("Moving processed results failed.")
				.recoverable()
				.retryWith(this::moveKiekerResultsToDone);
			FAILURE_HANDLER.handle(failure);
		}
	}
}
