package de.uka.ipd.sdq.beagle.core;

import static de.uka.ipd.sdq.beagle.core.testutil.EqualsMatcher.hasDefaultEqualsProperties;
import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static de.uka.ipd.sdq.beagle.core.testutil.ToStringMatcher.hasOverriddenToString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsSame.sameInstance;

import de.uka.ipd.sdq.beagle.core.testutil.ThrowingMethod;
import de.uka.ipd.sdq.beagle.core.testutil.factories.CodeSectionFactory;

import org.junit.Test;

/**
 * Tests {@link SeffLoop} and contains all test cases needed to check every method.
 *
 * @author Annika Berger
 */
public class SeffLoopTest {

	/**
	 * A {@link CodeSectionFactory}, which is able to generate {@link CodeSection}s.
	 */
	private static final CodeSectionFactory CODE_SECTION_FACTORY = new CodeSectionFactory();

	/**
	 * Test method for {@link SeffLoop#SeffLoop(CodeSection)} .
	 */
	@Test
	public void constructor() {
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
	 * Test method for {@link SeffLoop#equals()}.
	 */
	@Test
	public void equalsAndHashCode() {
		assertThat(new SeffLoop(CODE_SECTION_FACTORY.getOne()), hasDefaultEqualsProperties());
		final SeffLoop loop = new SeffLoop(CODE_SECTION_FACTORY.getAll()[0]);
		final SeffLoop loopB = new SeffLoop(CODE_SECTION_FACTORY.getAll()[1]);
		final SeffLoop loopC = new SeffLoop(CODE_SECTION_FACTORY.getAll()[0]);
		assertThat(loop, is(not(equalTo(loopB))));
		assertThat(loop, is(equalTo(loopC)));
		assertThat(loop.hashCode(), is(equalTo(loopC.hashCode())));
	}

	/**
	 * Test method for {@link SeffLoop#getLoopBody()}.
	 */
	@Test
	public void getLoopBody() {
		final CodeSection[] codeSections = CODE_SECTION_FACTORY.getAll();
		SeffLoop loop;
		for (final CodeSection codeSection : codeSections) {
			loop = new SeffLoop(codeSection);
			assertThat(loop.getLoopBody(), is(sameInstance(codeSection)));
		}
	}

	/**
	 * Test method for {@link SeffLoop#toString()}.
	 */
	@Test
	public void toStringT() {
		final CodeSection[] codeSections = CODE_SECTION_FACTORY.getAll();
		SeffLoop loop;
		for (final CodeSection codeSection : codeSections) {
			loop = new SeffLoop(codeSection);
			assertThat(loop, hasOverriddenToString());
		}
	}

}
