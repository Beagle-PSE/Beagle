package de.uka.ipd.sdq.beagle.core;

import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static org.junit.Assert.assertThat;

import de.uka.ipd.sdq.beagle.core.testutil.ThrowingMethod;
import de.uka.ipd.sdq.beagle.core.testutil.factories.TestFileFactory;

import org.junit.Test;
import org.palladiosimulator.pcm.core.entity.Entity;

import java.io.File;
import java.util.List;

/**
 * Tests {@link BeahgleConfiguration} and contains all test cases needed to check every
 * method.
 *
 * @author Michael Vogt
 */

public class BeagleConfigurationTest {

	/**
	 * A {@link TestFileFactory} providing methods to get files to use for tests.
	 */
	private static final TestFileFactory TEST_FILE_FACTORY = new TestFileFactory();

	/**
	 * A list of elements providing methods to get elements to use for tests.
	 */
	private List<Entity> elements;

	/**
	 * Test method for {link BeahgleConfiguration#Beagleconfiguration( List,
	 * java.io.File)}.
	 *
	 */
	@Test
	public void constructor() {
		final File[] files = TEST_FILE_FACTORY.getAll();

		final File notExistingFile = new File("/de/uka/ipd/sdq/beagle/core/NotExisting.java");
		ThrowingMethod method = () -> {
			new BeagleConfiguration(this.elements, notExistingFile);
		};
		assertThat("repositoryFile must exist.", method, throwsException(IllegalArgumentException.class));

		method = () -> {
			new BeagleConfiguration(null, files[0]);
		};
		assertThat("elements must not be null.", method, throwsException(NullPointerException.class));

		method = () -> {
			new BeagleConfiguration(this.elements, null);
		};
		assertThat("repositoryFile must not be null.", method, throwsException(NullPointerException.class));

	}
}
