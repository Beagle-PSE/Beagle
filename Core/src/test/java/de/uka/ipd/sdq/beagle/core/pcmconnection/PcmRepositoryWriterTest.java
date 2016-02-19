package de.uka.ipd.sdq.beagle.core.pcmconnection;

import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import de.uka.ipd.sdq.beagle.core.Blackboard;
import de.uka.ipd.sdq.beagle.core.failurehandling.ExceptionThrowingFailureHandler.FailureException;
import de.uka.ipd.sdq.beagle.core.testutil.factories.BlackboardFactory;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;

/**
 * Tests {@link PcmRepositoryWriter} and contains all test cases needed to check all
 * methods.
 *
 * @author Annika Berger
 */
// @formatter:off
@PrepareForTest({PcmRepositoryWriterAnnotator.class, PcmRepositoryFileLoader.class,
	PcmRepositoryWriterAnnotatorEvaEx.class, PcmRepositoryWriter.class})
//@formatter:on
public class PcmRepositoryWriterTest {

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
	 *
	 * @throws Exception If mocking fails.
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
	 * Test method for {@link PcmRepositoryWriter#PcmRepositoryWriter(Blackboard)}.
	 *
	 * <p>Asserts that a {@link NullPointerException} is thrown if parameter
	 * {@link Blackboard} is {@code null} and no exceptions are thrown for a valid
	 * blackboard.
	 */
	@Test
	public void constructor() {
		final Blackboard blackboard = BLACKBOARD_FACTORY.getFull();
		final PcmBeagleMappings mappings = new PcmBeagleMappings();
		blackboard.writeFor(PcmRepositoryBlackboardFactoryAdder.class, mappings);
		new PcmRepositoryWriter(blackboard);

		assertThat("Blackboard must not be null.", () -> new PcmRepositoryWriter(null),
			throwsException(NullPointerException.class));
	}

	/**
	 * Test method for {@link PcmRepositoryWriter#writeTo(File)}.
	 *
	 * <p>Asserts that a {@link NullPointerException} is thrown if method is called with
	 * parameter {@code null}, a {@link FileNotFoundException} is thrown if the File does
	 * not exist and no exception is thrown for correct input parameters.
	 *
	 * @throws FileNotFoundException if Repository File was not found
	 */
	@Test
	public void writeTo() throws FileNotFoundException {
		final Blackboard blackboard = BLACKBOARD_FACTORY.getFull();
		final PcmBeagleMappings mappings = new PcmBeagleMappings();
		blackboard.writeFor(PcmRepositoryBlackboardFactoryAdder.class, mappings);
		final PcmRepositoryWriter writer = new PcmRepositoryWriter(blackboard);
		assertThat("File must not be null", () -> writer.writeTo(null), throwsException(NullPointerException.class));

		final File notExistingFile = new File("notExistingFile.txt");
		assertThat("File must exist.", () -> writer.writeTo(notExistingFile),
			throwsException(FailureException.class));

		final File repositoryFile;
		try {
			repositoryFile = new File(PcmRepositoryFileLoader.class
				.getResource("/de/uka/ipd/sdq/beagle/core/pcmconnection/Family.repository").toURI().getPath());
		} catch (final URISyntaxException uriSyntaxException) {
			throw new RuntimeException("Cannot find file called 'Family.repository' in /src/test/resources/.");
		}
		writer.writeTo(repositoryFile);
	}

}
