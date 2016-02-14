package de.uka.ipd.sdq.beagle.core.pcmconnection;

import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

import de.uka.ipd.sdq.beagle.core.Blackboard;
import de.uka.ipd.sdq.beagle.core.testutil.factories.BlackboardFactory;

import org.junit.Test;
import org.palladiosimulator.pcm.repository.impl.RepositoryImpl;

/**
 * Tests {@link PcmRepositoryWriterAnnotator} and contains all test cases needed to check
 * all methods.
 * 
 * @author Annika Berger
 */
public class PcmRepositoryWriterAnnotatorTest {

	/**
	 * A {@link BlackboardFactory} to easily create {@link Blackboard}s.
	 */
	private static final BlackboardFactory BLACKBOARD_FACTORY = new BlackboardFactory();

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

		final RepositoryImpl repository = mock(RepositoryImpl.class);
		annotater.annotateAll(repository);

		assertThat("Repository must not be null.", () -> annotater.annotateAll(null),
			throwsException(NullPointerException.class));

	}

}
