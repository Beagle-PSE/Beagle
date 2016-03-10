package de.uka.ipd.sdq.beagle.systemtest;

import org.eclipse.core.runtime.FileLocator;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Executes Beagle on the PalladioFileShare project.
 *
 * @author Joshua Gleitze
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PalladioFileShareSystemTest {

	/**
	 * The classpath relative path to the PalladioFileShare project.
	 */
	private static final String FILE_SHARE_LOCATION = "/PalladioFileShare";

	/**
	 * The classpath relative path to the CompressMockup project, a stub project to
	 * satisfy PalladioFileShare’s dependencies.
	 */
	private static final String COMPRESS_MOCK_LOCATION = "/CompressMockup";

	/**
	 * The classpath relative path to the launch configuration executing
	 * PalladioFileShare’s TestDriver.
	 */
	private static final String TEST_DRIVER_LAUNCH_CONFIGURATION =
		"/Launch Configurations/PalladioFileShare TestDriver.launch";

	/**
	 * The classpath relative path to the launch configuration executing Beagle on
	 * PalladioFileShare.
	 */
	private static final String BEAGLE_LAUNCH_CONFIGURATION =
		"/Launch Configurations/Beagle on PalladioFileShare.launch";

	/**
	 * The classpath relative path to the launch configuration executing SoMoX on
	 * PalladioFileShare.
	 */
	private static final String SOMOX_LAUNCH_CONFIGURATION = "/Launch Configurations/SoMoX on PalladioFileShare.launch";

	/**
	 * The workspace abstraction.
	 */
	private static final Workspace WORKSPACE = new Workspace();

	/**
	 * Prepares the workspace for the system test.
	 */
	@Test
	public void m0setupWorkspace() {

		// import the projects
		WORKSPACE.importProject(load(COMPRESS_MOCK_LOCATION));
		final File fileShareProjectFolder = WORKSPACE.importProject(load(FILE_SHARE_LOCATION));

		// create folders PalladioFileShare needs
		new File(fileShareProjectFolder, "results").mkdir();

		// import the test driver configuration
		WORKSPACE.loadLaunchConfiguration(load(TEST_DRIVER_LAUNCH_CONFIGURATION));
	}

	/**
	 * Executes SoMoX on the project.
	 */
	@Test
	public void m1runSomox() {
		WORKSPACE.launchConfiguration(load(SOMOX_LAUNCH_CONFIGURATION));
	}

	/**
	 * Executes Beagle on the project.
	 */
	@Test
	public void m2runBeagle() {
		WORKSPACE.launchConfiguration(load(BEAGLE_LAUNCH_CONFIGURATION));
	}

	/**
	 * Loads a resource from the classpath.
	 *
	 * @param path The classpath relative path to the resource.
	 * @return The requested resource.
	 */
	private static File load(final String path) {
		final URL classpathUrl = PalladioFileShareSystemTest.class.getResource(path);
		if (classpathUrl == null) {
			throw new RuntimeException(path + " cannot be found!");
		}
		try {
			final URL fileUrl = FileLocator.toFileURL(classpathUrl);
			// The URL to Path conversion has to be done like this, to make it work with
			// Linux and Windows style paths.
			final URI urii = new URI(fileUrl.getProtocol(), fileUrl.getPath(), null);
			return new File(urii);
		} catch (final URISyntaxException | IOException loadError) {
			throw new RuntimeException(loadError);
		}
	}
}
