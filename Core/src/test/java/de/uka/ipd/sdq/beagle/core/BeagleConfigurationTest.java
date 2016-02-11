package de.uka.ipd.sdq.beagle.core;

import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import de.uka.ipd.sdq.beagle.core.testutil.ThrowingMethod;
import de.uka.ipd.sdq.beagle.core.testutil.factories.TestFileFactory;

import org.junit.Test;
import org.palladiosimulator.pcm.core.entity.Entity;

import java.io.File;
import java.util.ArrayList;
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

		new BeagleConfiguration(this.elements, files[1]);
	}

	/**
	 * Test method for {@link BeagleConfiguration#getElements()}.
	 */
	@Test
	public void getElements() {
		final File[] files = TEST_FILE_FACTORY.getAll();
		final File file = files[0];
		final List<Entity> testElements = new ArrayList<>();
		testElements.add(mock(Entity.class));
		final BeagleConfiguration beagleConfig = new BeagleConfiguration(testElements, file);
		assertThat(beagleConfig.getElements(), is(sameInstance(this.elements)));
	}

	/**
	 * Test method for {@link BeagleConfiguration#getTimeout()}.
	 */
	@Test
	public void getTimeout() {
		final int timeout = 42;
		final File[] files = TEST_FILE_FACTORY.getAll();
		final File file = files[0];
		final List<Entity> testElements = new ArrayList<>();
		testElements.add(mock(Entity.class));
		final BeagleConfiguration beagleConfig = new BeagleConfiguration(this.elements, file);
		assertThat(beagleConfig.getTimeout(), is(sameInstance(timeout)));
	}

	/**
	 * Test method for {@link BeagleConfiguration#setElements()}.
	 * 
	 * @param elements The elements to be measured or {@code null} to indicate that
	 *            everything in {@code repositoryFile} should be analysed.
	 */
	@Test
	public void setElements(final List<Entity> elements) {
		final File[] files = TEST_FILE_FACTORY.getAll();
		final File file = files[0];
		final List<Entity> testElements = new ArrayList<>();
		testElements.add(mock(Entity.class));
		final BeagleConfiguration beagleConfig = new BeagleConfiguration(this.elements, file);
		beagleConfig.setElements(testElements);
		assertThat(beagleConfig.getElements(), is(sameInstance(testElements)));
	}

	/**
	 * Test method for {@link BeagleConfiguration#setTimeout()}.
	 * 
	 * @param timeout The timeout to be used to {@code timeout}. [-2 → adaptive timeout]
	 *            [-1 → no timeout] [≥ 0 → timeout in seconds]
	 */
	@Test
	public void setTimeout(final int timeout) {
		final int testTimeout = 31;
		final File[] files = TEST_FILE_FACTORY.getAll();
		final File file = files[0];
		final List<Entity> testElements = new ArrayList<>();
		testElements.add(mock(Entity.class));
		final BeagleConfiguration beagleConfig = new BeagleConfiguration(this.elements, file);
		beagleConfig.setTimeout(testTimeout);
		assertThat(beagleConfig.getTimeout(), is(sameInstance(testTimeout)));

	}
}
