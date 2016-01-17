package de.uka.ipd.sdq.beagle.core;

import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.CoreMatchers.theInstance;
import static org.junit.Assert.assertThat;

import de.uka.ipd.sdq.beagle.core.testutil.CodeSectionFactory;
import de.uka.ipd.sdq.beagle.core.testutil.ThrowingMethod;

import org.junit.Test;

/**
 * Tests {@link de.uka.ipd.sdq.beage.core.ExternalCallParameter} and contains all test
 * cases needed to check every method.
 *
 *
 * @author Annika Berger
 */
public class ExternalCallParameterTest {

	/**
	 * Test method for
	 * {@link de.uka.ipd.sdq.beagle.core.ExternalCallParameter#ExternalCallParameter(de.uka.ipd.sdq.beagle.core.CodeSection, int)}
	 * .
	 * 
	 * <p>Asserts that instanziation is positive for Code sections and a positive index,
	 * while entering a negative index throws an exception.
	 */
	@Test
	public void testExternalCallParameter() {
		final CodeSection[] codeSections = CodeSectionFactory.getAllCodeSections();
		int index = 0;
		for (CodeSection call : codeSections) {
			final ExternalCallParameter externalCallP = new ExternalCallParameter(call, index);
			assertThat(externalCallP.getCallCodeSection(), is(theInstance(call)));
			index++;
		}

		ThrowingMethod method = () -> {
			new ExternalCallParameter(codeSections[0], -1);
		};
		assertThat("IllegalArgumentException expected for invalid input.", method,
			throwsException(IllegalArgumentException.class));

		method = () -> {
			new ExternalCallParameter(null, 1);
		};
		assertThat("NullPointerException expected for invalid input (null).", method,
			throwsException(NullPointerException.class));
	}

	/**
	 * Test method for
	 * {@link de.uka.ipd.sdq.beagle.core.ExternalCallParameter#getCallCodeSection()}.
	 */
	@Test
	public void testGetCallCodeSection() {
		final CodeSection[] codeSections = CodeSectionFactory.getAllCodeSections();
		for (CodeSection codeSection : codeSections) {
			final ExternalCallParameter externalCallP = new ExternalCallParameter(codeSection, 1);
			assertThat(externalCallP.getCallCodeSection(), is(equalTo(codeSection)));
		}
	}

	/**
	 * Test method for {@link de.uka.ipd.sdq.beagle.core.ExternalCallParameter#getIndex()}
	 * .
	 */
	@Test
	public void testGetIndex() {
		final CodeSection[] codeSections = CodeSectionFactory.getAllCodeSections();
		final int[] indizes = {1, 6, 100, 0, 1000, 9949};
		for (int index : indizes) {
			final ExternalCallParameter externalCallP = new ExternalCallParameter(codeSections[0], index);
			assertThat(externalCallP.getIndex(), is(equalTo(index)));
		}
	}

	/**
	 * Test method for
	 * {@link de.uka.ipd.sdq.beagle.core.ExternalCallParameter#equals(java.lang.Object)}
	 * and {@link de.uka.ipd.sdq.beagle.core.ExternalCallParameter#hashCode()}.
	 */
	@Test
	public void testEqualsAndHashcode() {
		final CodeSection[] codeSections = CodeSectionFactory.getAllCodeSections();
		final ExternalCallParameter externalCallP = new ExternalCallParameter(codeSections[0], 1);
		final ExternalCallParameter externalCallP2 = new ExternalCallParameter(codeSections[0], 1);
		final ExternalCallParameter externalCallP3 = new ExternalCallParameter(codeSections[1], 1);

		assertThat(externalCallP, is(equalTo(externalCallP2)));
		assertThat(externalCallP.hashCode(), is(equalTo(externalCallP2.hashCode())));
		assertThat(externalCallP, is(not(equalTo(externalCallP3))));
	}

	/**
	 * Test method for {@link de.uka.ipd.sdq.beagle.core.ExternalCallParameter#toString()}
	 * .
	 */
	@Test
	public void testToString() {
		final CodeSection[] codeSections = CodeSectionFactory.getAllCodeSections();
		for (CodeSection codeSection : codeSections) {
			final ExternalCallParameter externalCallP = new ExternalCallParameter(codeSection, 1);
			assertThat(externalCallP.toString(), not(startsWith("ExternalCallParameter@")));
		}
	}

}
