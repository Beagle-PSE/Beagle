package de.uka.ipd.sdq.beagle.core;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.CoreMatchers.theInstance;
import static org.junit.Assert.*;

import org.junit.Test;

import java.io.File;

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
		final File file = new File(SeffLoopTest.class.getResource("de/uka/ipd/sdq/beale/core/TestFile.java").getPath());
		final int startCodeLine = 4;
		final int endCodeLine = 15;
		final CodeSection[] codeSections = {null, new CodeSection(file, startCodeLine, file, endCodeLine)

		};
		SeffLoop loop;
		for (CodeSection codeSection : codeSections) {
			loop = new SeffLoop(codeSection);
			assertThat(loop.getLoopBody(), is(theInstance(codeSection)));
		}
	}

	/**
	 * Test method for {@link de.uka.ipd.sdq.beagle.core.SeffLoop#getLoopBody()}.
	 */
	@Test
	public void testGetLoopBody() {
		final File file = new File(SeffLoopTest.class.getResource("de/uka/ipd/sdq/beale/core/TestFile.java").getPath());
		final int startCodeLine = 4;
		final int endCodeLine = 15;
		final File file2 =
			new File(SeffLoopTest.class.getResource("de/uka/ipd/sdq/beale/core/PingPong.java").getPath());
		final int startCodeLine2 = 60;
		final int endCodeLine2 = 68;
		final CodeSection[] codeSections = {new CodeSection(file, startCodeLine, file, endCodeLine),
			new CodeSection(file2, startCodeLine2, file2, endCodeLine2)};
		SeffLoop loop;
		for (CodeSection codeSection : codeSections) {
			loop = new SeffLoop(codeSection);
			assertThat(loop.getLoopBody(), is(theInstance(codeSection)));
		}
	}

	/**
	 * Test method for {@link de.uka.ipd.sdq.beagle.core.SeffLoop#equals()}.
	 */
	@Test
	public void testEquals() {
		final File file = new File(SeffLoopTest.class.getResource("de/uka/ipd/sdq/beale/core/TestFile.java").getPath());
		final int startCodeLine = 4;
		final int endCodeLine = 15;
		final File file2 =
			new File(SeffLoopTest.class.getResource("de/uka/ipd/sdq/beale/core/PingPong.java").getPath());
		final int startCodeLine2 = 60;
		final int endCodeLine2 = 68;
		final CodeSection[] codeSections = {new CodeSection(file, startCodeLine, file, endCodeLine),
			new CodeSection(file2, startCodeLine2, file2, endCodeLine2)};
		final SeffLoop loop = new SeffLoop(codeSections[0]);
		final SeffLoop loopB = new SeffLoop(codeSections[1]);
		
		final File file3 = new File(SeffLoopTest.class.getResource("de/uka/ipd/sdq/beale/core/TestFile.java").getPath());
		final int startCodeLine3 = 4;
		final int endCodeLine3 = 15;
		final CodeSection codeSection = new CodeSection(file3, startCodeLine3, file3, endCodeLine3);
		final SeffLoop loopC = new SeffLoop(codeSection);
		assertThat(loop.equals(loopB), is(equalTo(false)));
		assertThat(loop.equals(loopC), is(equalTo(true)));
	}
	
	/**
	 * Test method for {@link de.uka.ipd.sdq.beagle.core.SeffLoop#toString()}.
	 */
	@Test
	public void testToString() {
		final File file = new File(SeffLoopTest.class.getResource("de/uka/ipd/sdq/beale/core/TestFile.java").getPath());
		final int startCodeLine = 4;
		final int endCodeLine = 15;
		final File file2 =
			new File(SeffLoopTest.class.getResource("de/uka/ipd/sdq/beale/core/PingPong.java").getPath());
		final int startCodeLine2 = 60;
		final int endCodeLine2 = 68;
		final CodeSection[] codeSections = {new CodeSection(file, startCodeLine, file, endCodeLine),
			new CodeSection(file2, startCodeLine2, file2, endCodeLine2)};
		SeffLoop loop;
		for (CodeSection codeSection : codeSections) {
			loop = new SeffLoop(codeSection);
			assertThat(loop.toString(), not(startsWith("SeffLoop@")));
		}
	}

}
