package de.uka.ipd.sdq.beagle.core.facade;

import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import de.uka.ipd.sdq.beagle.core.testutil.ThrowingMethod;
import de.uka.ipd.sdq.beagle.core.testutil.factories.TestFileFactory;
import de.uka.ipd.sdq.beagle.core.timeout.Timeout;

import org.eclipse.jdt.core.IJavaProject;
import org.junit.Before;
import org.junit.Test;

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
	private List<String> elements;

	/**
	 * Whether this configuration is in the <em>finalised</em> state.
	 */
	private boolean finalised;

	/**
	 * Mock an initialised element list.
	 */
	@Before
	public void initialiseElements() {
		this.elements = new ArrayList<>();
		this.elements.add("test");
	}

	/**
	 * Test method for {link BeagleConfiguration#BeagleConfiguration( List,
	 * java.io.File)}.
	 *
	 */
	@Test
	public void constructor() {
		final File[] files = TEST_FILE_FACTORY.getAll();

		final File notExistingFile = new File("/de/uka/ipd/sdq/beagle/core/NotExisting.java");
		final ThrowingMethod method = () -> new BeagleConfiguration(BeagleConfigurationTest.this.elements,
			notExistingFile, files[0], mock(IJavaProject.class));
		assertThat("repositoryFile must exist.", method, throwsException(IllegalArgumentException.class));

		final File notExistingFile2 = new File("/de/uka/ipd/sdq/beagle/core/NotExisting.java");
		ThrowingMethod method2 = () -> new BeagleConfiguration(BeagleConfigurationTest.this.elements, files[0],
			notExistingFile2, mock(IJavaProject.class));
		assertThat("source code link file must exist.", method2, throwsException(IllegalArgumentException.class));

		assertThat(new BeagleConfiguration(null, files[0], files[0], mock(IJavaProject.class)).getElements(),
			is(nullValue()));

		method2 = () -> new BeagleConfiguration(BeagleConfigurationTest.this.elements, null, files[0],
			mock(IJavaProject.class));
		assertThat("repositoryFile must not be null.", method2, throwsException(NullPointerException.class));
		method2 = () -> new BeagleConfiguration(BeagleConfigurationTest.this.elements, files[0], null,
			mock(IJavaProject.class));
		assertThat("source code link file must not be null.", method2, throwsException(NullPointerException.class));

		new BeagleConfiguration(this.elements, files[0], files[0], mock(IJavaProject.class));

		final BeagleConfiguration beagleConfig =
			new BeagleConfiguration(this.elements, files[0], files[0], mock(IJavaProject.class));
		beagleConfig.setElements(this.elements);
		final List<String> copyedElements = new ArrayList<>();
		copyedElements.addAll(this.elements);
		this.elements.clear();
		assertThat("The elements must be copyed.", beagleConfig.getElements(), is(copyedElements));

	}

	/**
	 * Test method for {@link BeagleConfiguration#getElements()}.
	 */
	@Test
	public void getElements() {
		final File[] files = TEST_FILE_FACTORY.getAll();
		final File file = files[0];
		final BeagleConfiguration beagleConfig =
			new BeagleConfiguration(this.elements, file, file, mock(IJavaProject.class));
		assertThat(beagleConfig.getElements(), is(this.elements));
	}

	/**
	 * Test method for {@link BeagleConfiguration#setElements()}.
	 */
	@Test
	public void setElements() {
		final File[] files = TEST_FILE_FACTORY.getAll();
		final File file = files[0];
		final BeagleConfiguration beagleConfig =
			new BeagleConfiguration(this.elements, file, file, mock(IJavaProject.class));
		beagleConfig.setElements(this.elements);
		assertThat(beagleConfig.getElements(), is(this.elements));
		beagleConfig.finalise();
		assertThat(new ThrowingMethod() {

			@Override
			public void throwException() throws Exception {
				beagleConfig.setElements(BeagleConfigurationTest.this.elements);
			}
		}, throwsException(IllegalStateException.class));
	}

	/**
	 * Test method for {@link BeagleConfiguration#setTimeout()} and
	 * {@link BeagleConfiguration#getTimeout()}.
	 */
	@Test
	public void timeoutTest() {
		final Timeout testTimeout = mock(Timeout.class);
		final File[] files = TEST_FILE_FACTORY.getAll();
		final File file = files[0];
		final BeagleConfiguration beagleConfig =
			new BeagleConfiguration(this.elements, file, file, mock(IJavaProject.class));
		beagleConfig.setTimeout(testTimeout);
		assertThat(beagleConfig.getTimeout(), is(testTimeout));
		beagleConfig.finalise();
		assertThat(new ThrowingMethod() {

			@Override
			public void throwException() throws Exception {
				beagleConfig.setTimeout(testTimeout);
			}
		}, throwsException(IllegalStateException.class));

	}

	/**
	 * Test method for {@link BeagleConfiguration#getRepositoryFile()} and
	 * {@link BeagleConfiguration#setRepositoryFile(File)}.
	 */
	@Test
	public void repositoryFileTest() {
		final File[] files = TEST_FILE_FACTORY.getAll();
		final File file = files[0];
		final BeagleConfiguration beagleConfig =
			new BeagleConfiguration(this.elements, file, file, mock(IJavaProject.class));
		beagleConfig.setRepositoryFile(file);
		assertThat(beagleConfig.getRepositoryFile(), is(file));
		beagleConfig.finalise();
		assertThat(new ThrowingMethod() {

			@Override
			public void throwException() throws Exception {
				beagleConfig.setRepositoryFile(file);
			}
		}, throwsException(IllegalStateException.class));
	}

	/**
	 * Test method for {@link BeagleConfiguration#isFinal()}.
	 */
	@Test
	public void isFinalTest() {
		final File[] files = TEST_FILE_FACTORY.getAll();
		final File file = files[0];
		final BeagleConfiguration beagleConfig =
			new BeagleConfiguration(this.elements, file, file, mock(IJavaProject.class));
		assertThat(beagleConfig.isFinal(), is(this.finalised));
	}

	/**
	 * Test method for {@link BeagleConfiguration#finalise()}.
	 */
	@Test
	public void finalise() {
		final boolean finalise = true;
		final File[] files = TEST_FILE_FACTORY.getAll();
		final File file = files[0];
		final BeagleConfiguration beagleConfig =
			new BeagleConfiguration(this.elements, file, file, mock(IJavaProject.class));
		beagleConfig.finalise();
		assertThat(beagleConfig.isFinal(), is(finalise));
	}

	/**
	 * Test method for {@link BeagleConfiguration#getRepositoryFile()} and
	 * {@link BeagleConfiguration#setFileProvider(SourceCodeFileProvider)}.
	 *
	 */
	@Test
	public void fileProviderTest() {
		final File[] files = TEST_FILE_FACTORY.getAll();
		final File file = files[0];
		final IJavaProject javaProject = mock(IJavaProject.class);
		final BeagleConfiguration beagleConfig = new BeagleConfiguration(this.elements, file, file, javaProject);
		assertThat(beagleConfig.getJavaProject(), is(javaProject));
	}
}
