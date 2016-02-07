package de.uka.ipd.sdq.beagle.core.pcmconnection;

import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

import de.uka.ipd.sdq.beagle.core.Blackboard;
import de.uka.ipd.sdq.beagle.core.ExternalCallParameter;
import de.uka.ipd.sdq.beagle.core.ResourceDemandingInternalAction;
import de.uka.ipd.sdq.beagle.core.SeffBranch;
import de.uka.ipd.sdq.beagle.core.SeffLoop;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.util.HashSet;

/**
 * Tests for {@link PcmRepositoryBlackboardFactory}.
 * 
 * @author Christoph Michelbach
 */
public class PcmRepositoryBlackboardFactoryTest {

	/**
	 * A factory which creates instances of {@link PcmRepositoryBlackboardFactory}.
	 */
	private static PcmRepositoryBlackboardFactoryFactory pcmRepositoryBlackboardFactoryFactory;

	/**
	 * Ran before every method in this class.
	 */
	@BeforeClass
	public static void beforeClass() {
		PcmRepositoryBlackboardFactoryTest.pcmRepositoryBlackboardFactoryFactory =
			new PcmRepositoryBlackboardFactoryFactory();
	}

	/**
	 * Test method for
	 * {@link PcmRepositoryBlackboardFactory#PcmRepositoryBlackboardFactory(java.util.Set)
	 * and PcmRepositoryBlackboardFactory#PcmRepositoryBlackboardFactory(String)}. Asserts
	 * that creation is possible and {@code null} or an empty string or otherwise
	 * impossible path cannot be passed.
	 */
	@Test
	public void pcmRepositoryBlackboardFactory() {
		assertThat(() -> new PcmRepositoryBlackboardFactory((String) null),
			throwsException(NullPointerException.class));
		assertThat(() -> new PcmRepositoryBlackboardFactory((File) null), throwsException(NullPointerException.class));
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
			assertThat(() -> new PcmRepositoryBlackboardFactory(impossibleRepositoryFile),
				throwsException(IllegalArgumentException.class));
		}
	}

	/**
	 * Test method for
	 * {@link PcmRepositoryBlackboardFactory#getBlackboardForAllElements()} .
	 */
	@Test
	public void getBlackboardForAllElements() {
		final PcmRepositoryBlackboardFactory pcmRepositoryBlackboardFactory =
			pcmRepositoryBlackboardFactoryFactory.getValidInstance();
		final Blackboard result = pcmRepositoryBlackboardFactory.getBlackboardForAllElements();
		assertThat(result, is(notNullValue()));

		assertThat(result.getAllRdias().size(), is(not(0)));
		assertThat(result.getAllSeffBranches().size(), is(not(0)));
		assertThat(result.getAllSeffLoops().size(), is(not(0)));
	}

	/**
	 * Test method for
	 * {@link PcmRepositoryBlackboardFactory#getBlackboardForIds(java.util.Collection)} .
	 */
	@Test
	public void getBlackboardForIdsCollectionOfString() {
		final PcmRepositoryBlackboardFactory pcmRepositoryBlackboardFactory =
			pcmRepositoryBlackboardFactoryFactory.getValidInstance();

		final HashSet<String> collection = new HashSet<String>();
		collection.add("");

		assertThat(() -> pcmRepositoryBlackboardFactory.getBlackboardForIds(collection),
			throwsException(IllegalArgumentException.class));
	}

	/**
	 * Test method for
	 * {@link PcmRepositoryBlackboardFactory#getBlackboardForIds(java.lang.String[])} .
	 */
	@Test
	public void getBlackboardForIdsStringArray() {
		final PcmRepositoryBlackboardFactory pcmRepositoryBlackboardFactory =
			pcmRepositoryBlackboardFactoryFactory.getValidInstance();

		assertThat(() -> pcmRepositoryBlackboardFactory.getBlackboardForIds(""),
			throwsException(IllegalArgumentException.class));

		assertThat(pcmRepositoryBlackboardFactory.getBlackboardForIds("_6f1a4LnmEeWVlphM5rov7g"), is(not(null)));

		assertThat(pcmRepositoryBlackboardFactory.getBlackboardForIds("_6f1a4LnmEeWVlphM5rov7g"),
			is(pcmRepositoryBlackboardFactory.getBlackboardForIds("_6f1a4LnmEeWVlphM5rov7g")));

		assertThat(pcmRepositoryBlackboardFactory.getBlackboardForIds("_6f1a4LnmEeWVlphM5rov7g"),
			is(not(pcmRepositoryBlackboardFactory.getBlackboardForIds("_FaSO4LnqEeWVlphM5rov7g"))));

		assertThat(pcmRepositoryBlackboardFactory.getBlackboardForIds("_6f1a4LnmEeWVlphM5rov7g"),
			is(not(pcmRepositoryBlackboardFactory.getBlackboardForAllElements())));

		final Blackboard emptyBlackboard = new Blackboard(new HashSet<ResourceDemandingInternalAction>(),
			new HashSet<SeffBranch>(), new HashSet<SeffLoop>(), new HashSet<ExternalCallParameter>());

		assertThat(pcmRepositoryBlackboardFactory.getBlackboardForIds("_SomeIdWhichDosntExistA"), is(emptyBlackboard));

		assertThat(pcmRepositoryBlackboardFactory.getBlackboardForIds("_TooShortId"), is(emptyBlackboard));

		assertThat(pcmRepositoryBlackboardFactory.getBlackboardForIds("IllegalId"), is(emptyBlackboard));
	}

}
