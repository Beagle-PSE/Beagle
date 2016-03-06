package de.uka.ipd.sdq.beagle.core.facade;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import de.uka.ipd.sdq.beagle.core.testutil.factories.TestFileFactory;

import org.eclipse.jdt.core.IJavaProject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.palladiosimulator.pcm.core.entity.Entity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BeagleControllerTest {

	/**
	 * A {@link TestFileFactory} providing methods to get files to use for tests.
	 */
	private static final TestFileFactory TEST_FILE_FACTORY = new TestFileFactory();

	/**
	 * A list of elements providing methods to get elements to use for tests.
	 */
	private List<Entity> elements;

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
		this.elements.add(mock(Entity.class));
	}

	@Test
	public void testBeagleController() {

		//final File[] files = TEST_FILE_FACTORY.getAll();
		final File file = new File(BeagleControllerTest.class.getResource("/de/uka/ipd/sdq/beagle/core/pcmconnection/Family.repository").getFile());
		final BeagleConfiguration beagleConfiguration = new BeagleConfiguration(
			this.elements,file,
			mock(IJavaProject.class));
		beagleConfiguration.finalise();
		new BeagleController(beagleConfiguration);

	}

	@Test
	public void testStartAnalysis() {
		// fail("Not yet implemented");
	}

	@Test
	public void testPauseAnalysis() {
		// fail("Not yet implemented");
	}

	@Test
	public void testContinueAnalysis() {
		// fail("Not yet implemented");
	}

	@Test
	public void testAbortAnalysis() {
		// fail("Not yet implemented");
	}

}
