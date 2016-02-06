package de.uka.ipd.sdq.beagle.core.measurement.order;

import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;

import de.uka.ipd.sdq.beagle.core.CodeSection;
import de.uka.ipd.sdq.beagle.core.ResourceDemandType;
import de.uka.ipd.sdq.beagle.core.testutil.factories.CodeSectionFactory;

import org.junit.Test;

/**
 * Tests {@link ResourceDemandCapturedEvent} and contains all test cases needed to check
 * every method.
 * 
 * @author Annika Berger
 */
public class ResourceDemandCapturedEventTest {

	/**
	 * A {@link CodeSectionFactory} providing methods to get code sections to use for
	 * tests.
	 */
	private static final CodeSectionFactory CODE_SECTION_FACTORY = new CodeSectionFactory();

	/**
	 * Test method for
	 * {@link ResourceDemandCapturedEvent#ResourceDemandCapturedEvent(CodeSection, ResourceDemandType, double)}
	 * .
	 * 
	 * <p>Asserts that {@link NullPointerException}s are thrown if one of the arguments is
	 * null and that an {@link IllegalArgumentException} is thrown if the value is
	 * negative.
	 */
	@Test
	public void resourceDemandCapturedEvent() {
		final CodeSection codeSection = CODE_SECTION_FACTORY.getOne();
		final ResourceDemandType type = new ResourceDemandType("Test", true);
		final Double value = 2.0;
		new ResourceDemandCapturedEvent(codeSection, type, value);

		final CodeSection codeSection2 = CODE_SECTION_FACTORY.getOne();
		final ResourceDemandType type2 = new ResourceDemandType("New Type", false);
		final Double value2 = 0.0;
		new ResourceDemandCapturedEvent(codeSection2, type2, value2);

		assertThat("Code section must not be null", () -> new ResourceDemandCapturedEvent(null, type, value),
			throwsException(NullPointerException.class));

		assertThat("Resource demand type must not be null",
			() -> new ResourceDemandCapturedEvent(codeSection, null, value),
			throwsException(NullPointerException.class));

		final Double negativeValue = -1.3;
		assertThat("Value must be non-negative",
			() -> new ResourceDemandCapturedEvent(codeSection, type, negativeValue),
			throwsException(IllegalArgumentException.class));

	}

	/**
	 * Test method for {@link ResourceDemandCapturedEvent#getType()} .
	 */
	@Test
	public void getType() {
		final CodeSection codeSection = CODE_SECTION_FACTORY.getOne();
		ResourceDemandType type = new ResourceDemandType("Test", true);
		final Double value = 2.0;
		final ResourceDemandCapturedEvent event = new ResourceDemandCapturedEvent(codeSection, type, value);

		assertThat(event.getType(), is(type));

		type = new ResourceDemandType("new type", false);
		assertThat(event.getType(), is(not(type)));

	}

	/**
	 * Test method for {@link ResourceDemandCapturedEvent#getValue()} .
	 */
	@Test
	public void getValue() {
		final CodeSection codeSection = CODE_SECTION_FACTORY.getOne();
		final ResourceDemandType type = new ResourceDemandType("Test", true);
		Double value = 2.0;
		final ResourceDemandCapturedEvent event = new ResourceDemandCapturedEvent(codeSection, type, value);

		assertThat(event.getValue(), is(value));

		value = 5.5;
		assertThat(event.getValue(), is(not(value)));
	}

	/**
	 * Test method for
	 * {@link ResourceDemandCapturedEvent#receive(MeasurementEventVisitor)} .
	 */
	@Test
	public void receive() {
		final CodeSection codeSection = CODE_SECTION_FACTORY.getOne();
		final ResourceDemandType type = new ResourceDemandType("Test", true);
		final Double value = 2.0;
		final MeasurementEventVisitor mockVisitor = mock(MeasurementEventVisitor.class);
		final ResourceDemandCapturedEvent event = new ResourceDemandCapturedEvent(codeSection, type, value);
		assertThat(() -> event.receive(null), throwsException(NullPointerException.class));
		event.receive(mockVisitor);
		then(mockVisitor).should().visit(same(event));
	}

}
