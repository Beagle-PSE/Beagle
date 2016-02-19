package de.uka.ipd.sdq.beagle.core.measurement.order;

import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import de.uka.ipd.sdq.beagle.core.CodeSection;
import de.uka.ipd.sdq.beagle.core.testutil.ThrowingMethod;
import de.uka.ipd.sdq.beagle.core.testutil.factories.CodeSectionFactory;

import org.junit.Test;

/**
 * Tests {@link AbstractMeasurementEvent} and contains all test cases needed to check
 * every method.
 *
 * @author Annika Berger
 */
public class AbstractMeasurementEventTest {

	/**
	 * A {@link CodeSectionFactory} providing methods to get code sections to use for
	 * tests.
	 */
	private static final CodeSectionFactory CODE_SECTION_FACTORY = new CodeSectionFactory();

	/**
	 * Test method for
	 * {@link AbstractMeasurementEvent#AbstractMeasurementEvent(CodeSection)}.
	 */
	@Test
	public void constructor() {
		final CodeSection[] codeSections = CODE_SECTION_FACTORY.getAll();
		for (final CodeSection codeSection : codeSections) {
			new AbstractMeasurementEvent(codeSection) {

				@Override
				public void receive(final MeasurementEventVisitor visitor) {

				}
			};
		}

		final ThrowingMethod method = () -> {
			new AbstractMeasurementEvent(null) {

				@Override
				public void receive(final MeasurementEventVisitor visitor) {

				}
			};
		};

		assertThat("Code Section must not be null.", method, throwsException(NullPointerException.class));
	}

	/**
	 * Test method for {@link AbstractMeasurementEvent#getCodeSection()}.
	 */
	@Test
	public void getCodeSection() {
		final CodeSection[] codeSections = CODE_SECTION_FACTORY.getAll();
		for (final CodeSection codeSection : codeSections) {
			final AbstractMeasurementEvent event = new AbstractMeasurementEvent(codeSection) {

				@Override
				public void receive(final MeasurementEventVisitor visitor) {

				}
			};

			assertThat(event.getCodeSection(), is(codeSection));
		}

		CodeSection codeSection = CODE_SECTION_FACTORY.getOne();
		final CodeSection copiedSection = codeSection;
		final AbstractMeasurementEvent event = new AbstractMeasurementEvent(codeSection) {

			@Override
			public void receive(final MeasurementEventVisitor visitor) {

			}
		};
		int index = 0;
		while (codeSection.equals(codeSections[index])) {
			index++;
		}
		codeSection = codeSections[index];
		assertThat("Changing code section afterwards must not take effect on returned code section",
			event.getCodeSection(), is(copiedSection));

	}

}
