package de.uka.ipd.sdq.beagle.core.facade;

import de.uka.ipd.sdq.beagle.core.testutil.factories.TestFileFactory;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Ansgar didn't write Javadoc here.
 *
 * @author Ansgar Spiegler
 */
public class BeagleControllerTest {

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
		// this.elements = new ArrayList<String>();
		// this.elements.add(mock(Entity.class));
	}

	/**
	 * Some missing doc.
	 *
	 */
	@Test
	public void beagleController() {
		/*
		 * //final File[] files = TEST_FILE_FACTORY.getAll(); final File file = new
		 * File(BeagleControllerTest.class.getResource(
		 * "/de/uka/ipd/sdq/beagle/core/pcmconnection/Family.repository").getFile());
		 * final BeagleConfiguration beagleConfiguration = new BeagleConfiguration(
		 * null,file, mock(IJavaProject.class)); beagleConfiguration.finalise(); new
		 * BeagleController(beagleConfiguration);
		 */

	}

	/**
	 * Some missing doc.
	 *
	 */
	@Test
	public void startAnalysis() {
		// fail("Not yet implemented");
	}

	/**
	 * Some missing doc.
	 *
	 */
	@Test
	public void pauseAnalysis() {
		// fail("Not yet implemented");
	}

	/**
	 * Some missing doc.
	 *
	 */
	@Test
	public void continueAnalysis() {
		// fail("Not yet implemented");
	}

	/**
	 * Some missing doc.
	 *
	 */
	@Test
	public void abortAnalysis() {
		// fail("Not yet implemented");
	}

}
