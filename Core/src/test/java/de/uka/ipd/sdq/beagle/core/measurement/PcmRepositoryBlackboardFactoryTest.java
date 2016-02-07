package de.uka.ipd.sdq.beagle.core.measurement;

import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

import de.uka.ipd.sdq.beagle.core.pcmconnection.PcmRepositoryBlackboardFactory;

import org.junit.Test;

import java.io.File;
import java.lang.instrument.IllegalClassFormatException;
import java.util.HashSet;
import java.util.Set;

/**
 * Tests for {@link PcmRepositoryBlackboardFactory}.
 * 
 * @author Christoph Michelbach
 */
public class PcmRepositoryBlackboardFactoryTest {

	/**
	 * Test method for
	 * {@link PcmRepositoryBlackboardFactory#PcmRepositoryBlackboardFactory(java.util.Set)
	 * and PcmRepositoryBlackboardFactory#PcmRepositoryBlackboardFactory(String)}. Asserts
	 * that creation is possible and {@code null} or an empty string or otherwise
	 * impossible path cannot be passed.
	 * 
	 * @throws IllegalClassFormatException Just let the test fail for this one.
	 */
	@Test
	public void constructor() throws IllegalClassFormatException {
		new PcmRepositoryBlackboardFactory("");

		assertThat(() -> new PcmRepositoryBlackboardFactory((String) null),
			throwsException(NullPointerException.class));
		assertThat(() -> new PcmRepositoryBlackboardFactory((Set<File>) null),
			throwsException(NullPointerException.class));
		assertThat(() -> new PcmRepositoryBlackboardFactory(""), throwsException(IllegalArgumentException.class));
		assertThat(() -> new PcmRepositoryBlackboardFactory("."), throwsException(IllegalArgumentException.class));
		assertThat(() -> new PcmRepositoryBlackboardFactory(".."), throwsException(IllegalArgumentException.class));
		assertThat(() -> new PcmRepositoryBlackboardFactory("/"), throwsException(IllegalArgumentException.class));
		assertThat(() -> new PcmRepositoryBlackboardFactory("/tmp"), throwsException(IllegalArgumentException.class));
		assertThat(() -> new PcmRepositoryBlackboardFactory("\0"), throwsException(IllegalArgumentException.class));

		final File[] impossibleRepositoryFiles = {
			new File(""), new File("."), new File(".."), new File("/"), new File("/tmp"), new File("\0")
		};

		for (File impossibleRepositoryFile : impossibleRepositoryFiles) {
			final HashSet<File> hashSet = new HashSet<File>();
			hashSet.add(impossibleRepositoryFile);
			assertThat(() -> new PcmRepositoryBlackboardFactory(hashSet),
				throwsException(IllegalArgumentException.class));
		}
	}

	/**
	 * Test method for
	 * {@link PcmRepositoryBlackboardFactory#getBlackboardForAllElements()} .
	 */
	@Test
	public void getBlackboardForAllElements() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link PcmRepositoryBlackboardFactory#getBlackboardForIds(java.util.Collection)} .
	 */
	@Test
	public void getBlackboardForIdsCollectionOfString() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link PcmRepositoryBlackboardFactory#getBlackboardForIds(java.lang.String[])} .
	 */
	@Test
	public void getBlackboardForIdsStringArray() {
		fail("Not yet implemented");
	}

}
