package de.uka.ipd.sdq.beagle.core;

import static de.uka.ipd.sdq.beagle.core.testutil.EqualsMatcher.hasDefaultEqualsProperties;
import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertThat;

import de.uka.ipd.sdq.beagle.core.testutil.ThrowingMethod;
import de.uka.ipd.sdq.beagle.core.testutil.factories.CodeSectionFactory;

import org.junit.Test;

/**
 * Tests {@link ExternalCallParameter} and contains all test cases needed to check every
 * method.
 *
 *
 * @author Annika Berger
 */
public class ExternalCallParameterTest {

	/**
	 * A {@link CodeSectionFactory}, which is able to generate {@link CodeSection}s.
	 */
	private static final CodeSectionFactory CODE_SECTION_FACTORY = new CodeSectionFactory();

	/**
	 * Test method for
	 * {@link ExternalCallParameter#ExternalCallParameter(de.uka.ipd.sdq.beagle.core.CodeSection, int)}
	 * .
	 *
	 * <p>Asserts that instantiation is possible for Code sections and a positive index,
	 * while entering a negative index throws an exception.
	 */
	@Test
	public void testExternalCallParameter() {
		final CodeSection[] codeSections = CODE_SECTION_FACTORY.getAll();
		int index = 0;
		for (final CodeSection call : codeSections) {
			new ExternalCallParameter(call, index);
			index++;
		}

		ThrowingMethod method = () -> {
			new ExternalCallParameter(codeSections[0], -1);
		};
		assertThat("Index must not be negative.", method, throwsException(IllegalArgumentException.class));

		method = () -> {
			new ExternalCallParameter(null, 1);
		};
		assertThat("Code section must not be null.", method, throwsException(NullPointerException.class));
	}

	/**
	 * Test method for {@link ExternalCallParameter#getCallCodeSection()}.
	 */
	@Test
	public void testGetCallCodeSection() {
		final CodeSection[] codeSections = CODE_SECTION_FACTORY.getAll();
		for (final CodeSection codeSection : codeSections) {
			final ExternalCallParameter externalCallP = new ExternalCallParameter(codeSection, 1);
			assertThat(externalCallP.getCallCodeSection(), is(equalTo(codeSection)));
		}
	}

	/**
	 * Test method for {@link ExternalCallParameter#getIndex()} .
	 */
	@Test
	public void testGetIndex() {
		final CodeSection codeSection = CODE_SECTION_FACTORY.getOne();
		final int[] indizes = {
			1, 6, 100, 0, 1000, 9949
		};
		for (final int index : indizes) {
			final ExternalCallParameter externalCallP = new ExternalCallParameter(codeSection, index);
			assertThat(externalCallP.getIndex(), is(equalTo(index)));
		}
	}

	/**
	 * Test method for {@link ExternalCallParameter#equals(java.lang.Object)} and
	 * {@link ExternalCallParameter#hashCode()}.
	 */
	@Test
	public void testEqualsAndHashcode() {
		assertThat(new ExternalCallParameter(CODE_SECTION_FACTORY.getOne(), 3), hasDefaultEqualsProperties());
		final ExternalCallParameter externalCallP = new ExternalCallParameter(CODE_SECTION_FACTORY.getAll()[0], 1);
		final ExternalCallParameter externalCallP2 = new ExternalCallParameter(CODE_SECTION_FACTORY.getAll()[0], 1);
		final ExternalCallParameter externalCallP3 = new ExternalCallParameter(CODE_SECTION_FACTORY.getAll()[1], 1);
		final ExternalCallParameter externalCallP4 = new ExternalCallParameter(CODE_SECTION_FACTORY.getAll()[0], 2);

		assertThat(externalCallP, is(equalTo(externalCallP2)));
		assertThat(externalCallP.hashCode(), is(equalTo(externalCallP2.hashCode())));
		assertThat(externalCallP, is(not(equalTo(externalCallP3))));
		assertThat(externalCallP2, is(not(equalTo(externalCallP4))));
	}

	/**
	 * Test method for {@link ExternalCallParameter#toString()} .
	 */
	@Test
	public void testToString() {
		final CodeSection[] codeSections = CODE_SECTION_FACTORY.getAll();
		for (final CodeSection codeSection : codeSections) {
			final ExternalCallParameter externalCallP = new ExternalCallParameter(codeSection, 1);
			assertThat(externalCallP.toString(), not(startsWith("ExternalCallParameter@")));
		}
	}

}
