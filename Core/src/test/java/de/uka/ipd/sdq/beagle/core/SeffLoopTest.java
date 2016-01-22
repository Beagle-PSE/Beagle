package de.uka.ipd.sdq.beagle.core;

import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static de.uka.ipd.sdq.beagle.core.testutil.EqualsMatcher.hasDefaultEqualsProperties;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.CoreMatchers.theInstance;
import static org.hamcrest.MatcherAssert.assertThat;

import de.uka.ipd.sdq.beagle.core.testutil.ThrowingMethod;
import de.uka.ipd.sdq.beagle.core.testutil.factories.CodeSectionFactory;

import org.junit.Test;

/**
 * Tests {@link de.uka.ipd.sdq.beagle.core.SeffLoop} and contains all test cases needed to
 * check every method.
 *
 * @author Annika Berger
 */
public class SeffLoopTest {

	/**
	 * A {@link CodeSectionFactory}, which is able to generate {@link CodeSection}s.
	 */
	private static final CodeSectionFactory CODE_SECTION_FACTORY = new CodeSectionFactory();

	/**
	 * Test method for
	 * {@link de.uka.ipd.sdq.beagle.core.SeffLoop#SeffLoop(de.uka.ipd.sdq.beagle.core.CodeSection)}
	 * .
	 */
	@Test
	public void testSeffLoop() {
		final CodeSection[] codeSections = CODE_SECTION_FACTORY.getAll();
		for (final CodeSection codeSection : codeSections) {
			new SeffLoop(codeSection);
		}
		final ThrowingMethod method = () -> {
			new SeffLoop(null);
		};
		assertThat("Code section must not be null.", method, throwsException(NullPointerException.class));
	}

	/**
	 * Test method for {@link de.uka.ipd.sdq.beagle.core.SeffLoop#equals()}.
	 */
	@Test
	public void testEqualsAndHashCode() {
		assertThat(new SeffLoop(CODE_SECTION_FACTORY.getOne()), hasDefaultEqualsProperties());
		final SeffLoop loop = new SeffLoop(CODE_SECTION_FACTORY.getAll()[0]);
		final SeffLoop loopB = new SeffLoop(CODE_SECTION_FACTORY.getAll()[1]);
		final SeffLoop loopC = new SeffLoop(CODE_SECTION_FACTORY.getAll()[0]);
		assertThat(loop, is(not(equalTo(loopB))));
		assertThat(loop, is(equalTo(loopC)));
		assertThat(loop.hashCode(), is(equalTo(loopC.hashCode())));
	}

	/**
	 * Test method for {@link de.uka.ipd.sdq.beagle.core.SeffLoop#getLoopBody()}.
	 */
	@Test
	public void testGetLoopBody() {
		final CodeSection[] codeSections = CODE_SECTION_FACTORY.getAll();
		SeffLoop loop;
		for (final CodeSection codeSection : codeSections) {
			loop = new SeffLoop(codeSection);
			assertThat(loop.getLoopBody(), is(theInstance(codeSection)));
		}
	}

	/**
	 * Test method for {@link de.uka.ipd.sdq.beagle.core.SeffLoop#toString()}.
	 */
	@Test
	public void testToString() {
		final CodeSection[] codeSections = CODE_SECTION_FACTORY.getAll();
		SeffLoop loop;
		for (final CodeSection codeSection : codeSections) {
			loop = new SeffLoop(codeSection);
			assertThat(loop.toString(), not(startsWith("SeffLoop@")));
		}
	}

}
