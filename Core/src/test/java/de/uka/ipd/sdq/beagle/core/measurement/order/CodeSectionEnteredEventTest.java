package de.uka.ipd.sdq.beagle.core.measurement.order;

import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;

import de.uka.ipd.sdq.beagle.core.CodeSection;
import de.uka.ipd.sdq.beagle.core.testutil.factories.CodeSectionFactory;

import org.junit.Test;

/**
 * Tests {@link CodeSectionEnteredEvent} and contains all test cases needed to check every
 * method.
 *
 * @author Annika Berger
 */
public class CodeSectionEnteredEventTest {

	/**
	 * A {@link CodeSectionFactory} providing methods to get code sections to use for
	 * tests.
	 */
	private static final CodeSectionFactory CODE_SECTION_FACTORY = new CodeSectionFactory();

	/**
	 * Test method for
	 * {@link CodeSectionEnteredEvent#CodeSectionExecutedEvent(CodeSection)}.
	 */
	@Test
	public void constructor() {
		final CodeSection[] codeSections = CODE_SECTION_FACTORY.getAll();
		for (final CodeSection codeSection : codeSections) {
			new CodeSectionEnteredEvent(codeSection);
		}

		assertThat("Code section must not be null", () -> new CodeSectionEnteredEvent(null),
			throwsException(NullPointerException.class));
	}

	/**
	 * Test method for {@link CodeSectionEnteredEvent#receive(MeasurementEventVisitor)}.
	 */
	@Test
	public void receive() {
		final CodeSection codeSection = CODE_SECTION_FACTORY.getOne();
		final MeasurementEventVisitor mockVisitor = mock(MeasurementEventVisitor.class);
		final CodeSectionEnteredEvent event = new CodeSectionEnteredEvent(codeSection);
		assertThat(() -> event.receive(null), throwsException(NullPointerException.class));
		event.receive(mockVisitor);
		then(mockVisitor).should().visit(same(event));
	}

}
