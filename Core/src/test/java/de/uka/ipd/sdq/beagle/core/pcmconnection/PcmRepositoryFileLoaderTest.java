package de.uka.ipd.sdq.beagle.core.pcmconnection;

import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import de.uka.ipd.sdq.beagle.core.failurehandling.ExceptionThrowingFailureResolver.FailureException;

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
 * Tests {@link PcmRepositoryFileLoader} and contains all test cases needed to check all
 * methods.
 *
 * @author Annika Berger
 */
// @formatter:off
@PrepareForTest({PcmRepositoryWriterAnnotator.class, PcmRepositoryFileLoader.class,
	PcmRepositoryWriterAnnotatorEvaEx.class, PcmRepositoryWriter.class})
// @formatter:on
public class PcmRepositoryFileLoaderTest {

	/**
	 * Rule loading PowerMock (to mock static methods).
	 */
	@Rule
	public PowerMockRule loadPowerMock = new PowerMockRule();

	/**
	 * Mocks {@link ResourceTypeMappings} to be able to run the tests.
	 *
	 * @throws Exception if something went wrong while mocking the constructor of
	 *             {@link ResourceTypeMappings}.
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
	 * Test method for {@link PcmRepositoryFileLoader#loadRepositoryFromFile(File)}.
	 *
	 * <p>Asserts that a {@link NullPointerException} is thrown if method is called with
	 * parameter {@code null}, a {@link FileNotFoundException} is thrown if the File does
	 * not exist and no exception is thrown for correct input parameters.
	 *
	 * @throws FileNotFoundException thrown if File does not exist
	 */
	@Test
	public void loadRepositoryFromFile() throws FileNotFoundException {
		final PcmRepositoryFileLoader loader = new PcmRepositoryFileLoader();
		assertThat(() -> loader.loadRepositoryFromFile(null), throwsException(NullPointerException.class));

		final File file = new File("notExistingFile.txt");
		assertThat(() -> loader.loadRepositoryFromFile(file), throwsException(FailureException.class));

		final File repositoryFile;
		try {
			repositoryFile = new File(PcmRepositoryFileLoader.class
				.getResource("/de/uka/ipd/sdq/beagle/core/pcmconnection/Family.repository").toURI().getPath());
		} catch (final URISyntaxException uriSyntaxException) {
			throw new RuntimeException("Cannot find file called 'Family.repository' in /src/test/resources/.");
		}
		loader.loadRepositoryFromFile(repositoryFile);
	}

}
