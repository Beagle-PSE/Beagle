package de.uka.ipd.sdq.beagle.core.pcmsourcestatementlink;

import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Tests {@link PcmSourceStatementLinkReader} and contains the test cases needed to check
 * every method.
 *
 * @author Annika Berger
 */
public class PcmSourceStatementLinkReaderTest {

	/**
	 * Correct File to be used in tests.
	 */
	private final File linkRepositoryFile = this.getFile(
		"/de/uka/ipd/sdq/beagle/core/pcmsourcestatementlink/internal_architecture_model_source_statement_links.xml");

	/**
	 * Test method for
	 * {@link PcmSourceStatementLinkReader#PcmSourceStatementLinkReader(File)} .
	 */
	@Test
	public void constructor() {
		assertThat("linkRepositoryFile must not be null.", () -> new PcmSourceStatementLinkReader(null),
			throwsException(NullPointerException.class));

		new PcmSourceStatementLinkReader(this.linkRepositoryFile);

		final File notExistingFile = new File("/de/uka/ipd/sdq/beagle/core/pcmsourcestatementlink/NotExisting.xml");
		assertThat("File must exist.", () -> new PcmSourceStatementLinkReader(notExistingFile),
			throwsException(IllegalArgumentException.class));
	}

	/**
	 * Test method for {@link PcmSourceStatementLinkReader#getPcmSourceLinkRepository()} .
	 */
	@Test
	public void getPcmSourceLinkRepository() {
		final PcmSourceStatementLinkReader reader = new PcmSourceStatementLinkReader(this.linkRepositoryFile);
		final PcmSourceStatementLinkRepository repository = reader.getPcmSourceLinkRepository();
		assertThat(repository.getLinks().size(), is(11));
		assertThat(repository.getHashes().size(), is(3));
	}

	/**
	 * Gets a File that contains what the resource denoted by
	 * {@code classpathRelativePath} contains.
	 *
	 * @param classpathRelativePath The classpath relative path of a resource.
	 * @return A file containing what the resource denoted by
	 *         {@code classpathRelativePath} contains.
	 */
	private File getFile(final String classpathRelativePath) {

		try {
			final URL classpathURL = PcmSourceStatementLinkReaderTest.class.getResource(classpathRelativePath);
			if (classpathURL == null) {
				throw new FileNotFoundException(
					"Cannot find the resource " + classpathRelativePath + " on the classpath.");
			}

			final URI classpathURI = classpathURL.toURI();
			final File classpathFile = new File(classpathURI);
			return classpathFile;

		} catch (final IOException | URISyntaxException ioError) {
			throw new RuntimeException("Could not load test file " + classpathRelativePath, ioError);
		}
	}

}
