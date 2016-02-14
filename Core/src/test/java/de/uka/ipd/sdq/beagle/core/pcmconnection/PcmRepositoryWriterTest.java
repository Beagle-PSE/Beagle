package de.uka.ipd.sdq.beagle.core.pcmconnection;

import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static org.hamcrest.MatcherAssert.assertThat;

import de.uka.ipd.sdq.beagle.core.Blackboard;
import de.uka.ipd.sdq.beagle.core.testutil.factories.BlackboardFactory;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;

/**
 * Tests {@link PcmRepositoryWriter} and contains all test cases needed to check all
 * methods.
 * 
 * @author Annika Berger
 */
public class PcmRepositoryWriterTest {

	/**
	 * A {@link BlackboardFactory} to easily create {@link Blackboard}s.
	 */
	private static final BlackboardFactory BLACKBOARD_FACTORY = new BlackboardFactory();

	/**
	 * Test method for {@link PcmRepositoryWriter#PcmRepositoryWriter(Blackboard)}.
	 * 
	 * <p>Asserts that a {@link NullPointerException} is thrown if parameter
	 * {@link Blackboard} is {@code null} and no exceptions are thrown for a valid
	 * blackboard.
	 */
	@Test
	public void constructor() {
		new PcmRepositoryWriter(BLACKBOARD_FACTORY.getFull());

		assertThat("Blackboard must not be null.", () -> new PcmRepositoryWriter(null),
			throwsException(NullPointerException.class));
	}

	/**
	 * Test method for {@link PcmRepositoryWriter#writeTo(File)}.
	 * 
	 * <p>Asserts that a {@link NullPointerException} is thrown if method is called with
	 * parameter {@code null}, a {@link FileNotFoundException} is thrown if the File does
	 * not exist and no exception is thrown for correct input parameters.
	 */
	@Test
	public void writeTo() {
		final PcmRepositoryWriter writer = new PcmRepositoryWriter(BLACKBOARD_FACTORY.getFull());
		assertThat("File must not be null", () -> writer.writeTo(null), throwsException(NullPointerException.class));
		
		final File notExistingFile = new File("notExistingFile.txt");
		assertThat("File must exist.", () -> writer.writeTo(notExistingFile), throwsException(FileNotFoundException.class));
		
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
