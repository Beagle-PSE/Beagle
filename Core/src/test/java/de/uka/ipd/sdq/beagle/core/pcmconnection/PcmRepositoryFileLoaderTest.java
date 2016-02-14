package de.uka.ipd.sdq.beagle.core.pcmconnection;

import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;

/**
 * Tests {@link PcmRepositoryFileLoader} and contains all test cases needed to check all
 * methods.
 * 
 * @author Annika Berger
 */
public class PcmRepositoryFileLoaderTest {

	/**
	 * Test method for {@link PcmRepositoryFileLoader#loadRepositoryFromFile(File)}.
	 * 
	 * <p>Asserts that a {@link NullPointerException} is thrown if method is called with
	 * parameter {@code null}, a {@link FileNotFoundException} is thrown if the File does
	 * not exist and no exception is thrown for correct input parameters.
	 */
	@Test
	public void loadRepositoryFromFile() {
		final PcmRepositoryFileLoader loader = new PcmRepositoryFileLoader();
		assertThat(() -> loader.loadRepositoryFromFile(null), throwsException(NullPointerException.class));

		final File file = new File("notExistingFile.txt");
		assertThat(() -> loader.loadRepositoryFromFile(file), throwsException(FileNotFoundException.class));

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
