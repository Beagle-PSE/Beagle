package de.uka.ipd.sdq.beagle.core;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.CoreMatchers.theInstance;
import static org.junit.Assert.assertThat;

import de.uka.ipd.sdq.beagle.core.testutil.CodeSectionFactory;

import org.junit.Test;

/**
 * Tests {@link de.uka.ipd.sdq.beage.core.SeffLoop} and contains all test cases needed to
 * check every method.
 *
 * @author Annika Berger
 */
public class SeffLoopTest {

	/**
	 * Test method for
	 * {@link de.uka.ipd.sdq.beagle.core.SeffLoop#SeffLoop(de.uka.ipd.sdq.beagle.core.CodeSection)}
	 * .
	 */
	@Test
	public void testConstructor() {
		final CodeSection[] codeSections = CodeSectionFactory.getAllCodeSections();
		SeffLoop loop;
		for (final CodeSection codeSection : codeSections) {
			loop = new SeffLoop(codeSection);
			assertThat(loop.getLoopBody(), is(theInstance(codeSection)));
		}
	}

	/**
	 * Test method for {@link de.uka.ipd.sdq.beagle.core.SeffLoop#equals()}.
	 */
	@Test
	public void testEquals() {
		final CodeSection[] codeSections = CodeSectionFactory.getAllCodeSections();
		final SeffLoop loop = new SeffLoop(codeSections[0]);
		final SeffLoop loopB = new SeffLoop(codeSections[1]);
		final SeffLoop loopC = new SeffLoop(codeSections[0]);
		assertThat(loop, is(not(equalTo(loopB))));
		assertThat(loop, is(equalTo(loopC)));
	}

	/**
	 * Test method for {@link de.uka.ipd.sdq.beagle.core.SeffLoop#getLoopBody()}.
	 */
	@Test
	public void testGetLoopBody() {
		final CodeSection[] codeSections = CodeSectionFactory.getAllCodeSections();
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
		final CodeSection[] codeSections = CodeSectionFactory.getAllCodeSections();
		SeffLoop loop;
		for (final CodeSection codeSection : codeSections) {
			loop = new SeffLoop(codeSection);
			assertThat(loop.toString(), not(startsWith("SeffLoop@")));
		}
	}

}
