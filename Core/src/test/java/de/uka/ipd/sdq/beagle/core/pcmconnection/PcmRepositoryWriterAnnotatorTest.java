package de.uka.ipd.sdq.beagle.core.pcmconnection;

import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import de.uka.ipd.sdq.beagle.core.Blackboard;
import de.uka.ipd.sdq.beagle.core.testutil.factories.BlackboardFactory;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.palladiosimulator.pcm.repository.impl.RepositoryImpl;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;

/**
 * Tests {@link PcmRepositoryWriterAnnotator} and contains all test cases needed to check
 * all methods.
 * 
 * @author Annika Berger
 */
//@formatter: off
@PrepareForTest({PcmRepositoryWriterAnnotator.class, PcmRepositoryFileLoader.class,
	PcmRepositoryWriterAnnotatorEvaEx.class, PcmRepositoryWriter.class})
//@formatter: on
public class PcmRepositoryWriterAnnotatorTest {

	/**
	 * A {@link BlackboardFactory} to easily create {@link Blackboard}s.
	 */
	private static final BlackboardFactory BLACKBOARD_FACTORY = new BlackboardFactory();

	/**
	 * Rule loading PowerMock (to mock static methods).
	 */
	@Rule
	public PowerMockRule loadPowerMock = new PowerMockRule();

	/**
	 * Mocks {@link ResourceTypeMappings} to be able to run the tests.
	 * @throws Exception 
	 *
	 */
	@Before
	public void prepare() throws Exception {
		final ResourceTypeMappings mockedMappings = PowerMockito.mock(ResourceTypeMappings.class);
		mockStatic(ResourceTypeMappings.class);
		whenNew(ResourceTypeMappings.class).withNoArguments().thenReturn(mockedMappings);
		doNothing().when(mockedMappings).initialise();
	}

	/**
	 * Test method for
	 * {@link PcmRepositoryWriterAnnotator#PcmRepositoryWriterAnnotator(Blackboard, PcmBeagleMappings)}
	 * .
	 * 
	 * <p>Asserts that {@link NullPointerException} are thrown if one parameter is null
	 * and no exception is thrown for valid input.
	 */
	@Test
	public void constructor() {
		final PcmBeagleMappings pcmMappings = new PcmBeagleMappings();
		new PcmRepositoryWriterAnnotator(BLACKBOARD_FACTORY.getFull(), pcmMappings);

		assertThat("Blackboard must not be null", () -> new PcmRepositoryWriterAnnotator(null, pcmMappings),
			throwsException(NullPointerException.class));

		assertThat("Pcm Beagle mapping must not be null",
			() -> new PcmRepositoryWriterAnnotator(BLACKBOARD_FACTORY.getFull(), null),
			throwsException(NullPointerException.class));
	}

	/**
	 * Test method for {@link PcmRepositoryWriterAnnotator#annotateAll(RepositoryImpl)}.
	 * 
	 * <p>Asserts that a {@link NullPointerException} is thrown if the
	 * {@link RepositoryImpl} is {@code null} and no exceptions are thrown for a valid
	 * call.
	 */
	@Test
	public void annotateAll() {
		final PcmBeagleMappings pcmMappings = new PcmBeagleMappings();
		final PcmRepositoryWriterAnnotator annotater =
			new PcmRepositoryWriterAnnotator(BLACKBOARD_FACTORY.getFull(), pcmMappings);

		assertThat("Repository must not be null.", () -> annotater.annotateAll(null),
			throwsException(NullPointerException.class));

	}

}
