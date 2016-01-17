package de.uka.ipd.sdq.beagle.core;

import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.CoreMatchers.theInstance;
import static org.junit.Assert.assertThat;

import de.uka.ipd.sdq.beagle.core.testutil.ThrowingMethod;

import org.junit.Test;

import java.io.File;

/**
 * Tests {@link de.uka.ipd.sdq.beagle.core.CodeSection} and contains all test cases needed
 * to check every method.
 *
 * @author Annika Berger
 */
public class CodeSectionTest {

	/**
	 * Test method for
	 * {@link de.uka.ipd.sdq.beagle.core.CodeSection#CodeSection(java.io.File, int, java.io.File, int)}
	 * .
	 *
	 * <p>Asserts that Constructor works for correct input, an IllegalArgumentException is
	 * thrown if input for one of the files is no file or startIndex is greater than
	 * endIndex.
	 */
	@Test
	public void testCodeSection() {
		final File file =
			new File(CodeSectionTest.class.getResource("/de/uka/ipd/sdq/beagle/core/TestFile.java").getPath());
		final int startCodeLine = 4;
		final int endCodeLine = 15;
		final CodeSection codeSection = new CodeSection(file, startCodeLine, file, endCodeLine);
		assertThat(codeSection.getStartFile(), is(theInstance(file)));

		final File notExistingFile =
			new File("/de/uka/ipd/sdq/beagle/core/NotExisting.java");
		ThrowingMethod method = () -> {
			new CodeSection(notExistingFile, startCodeLine, notExistingFile, endCodeLine);
		};
		assertThat("IllegalArgumentException expected for invalid input.", method,
			throwsException(IllegalArgumentException.class));

		final int invalidStartIndex = -4;
		final int invalidEndIndex = -15;
		final int invalidEndIndex2 = 300;

		method = () -> {
			new CodeSection(file, invalidStartIndex, file, endCodeLine);
		};
		assertThat("IllegalArgumentException expected for invalid input (negativ start index).", method,
			throwsException(IllegalArgumentException.class));

		method = () -> {
			new CodeSection(file, startCodeLine, file, invalidEndIndex);
		};
		assertThat("IllegalArgumentException expected for invalid input (negativ end index).", method,
			throwsException(IllegalArgumentException.class));

		method = () -> {
			new CodeSection(file, startCodeLine, file, invalidEndIndex2);
		};
		assertThat("IllegalArgumentException expected for invalid input.", method,
			throwsException(IllegalArgumentException.class));
	}

	/**
	 * Test method for {@link de.uka.ipd.sdq.beagle.core.CodeSection#equals()}.
	 **/
	@Test
	public void testEqualsAndHashCode() {
		final File file =
			new File(CodeSectionTest.class.getResource("/de/uka/ipd/sdq/beagle/core/TestFile.java").getPath());
		final int startCodeLine = 4;
		final int endCodeLine = 15;
		final CodeSection codeSection = new CodeSection(file, startCodeLine, file, endCodeLine);

		final File secFile =
			new File(CodeSectionTest.class.getResource("/de/uka/ipd/sdq/beagle/core/PingPong.java").getPath());
		final int startIndex = 60;
		final int endIndex = 68;
		final CodeSection secCodeSection = new CodeSection(secFile, startIndex, secFile, endIndex);

		final File fileA =
			new File(CodeSectionTest.class.getResource("/de/uka/ipd/sdq/beagle/core/TestFile.java").getPath());
		final int startCodeLineA = 4;
		final int endCodeLineA = 15;
		final CodeSection codeSectionA = new CodeSection(fileA, startCodeLineA, fileA, endCodeLineA);

		assertThat(codeSection, is(equalTo(codeSectionA)));
		assertThat(codeSection.hashCode(), is(equalTo(codeSectionA.hashCode())));
		assertThat(codeSection, is(not(equalTo(secCodeSection))));
	}

	/**
	 * Test method for {@link de.uka.ipd.sdq.beagle.core.CodeSection#getEndCodeLine()}.
	 **/
	@Test
	public void testGetEndCodeLine() {
		final File file =
			new File(CodeSectionTest.class.getResource("/de/uka/ipd/sdq/beagle/core/TestFile.java").getPath());
		final int startCodeLine = 4;
		final int endCodeLine = 15;
		CodeSection codeSection = new CodeSection(file, startCodeLine, file, endCodeLine);
		assertThat(codeSection.getEndSectionIndex(), is(endCodeLine));

		final File secFile =
			new File(CodeSectionTest.class.getResource("/de/uka/ipd/sdq/beagle/core/PingPong.java").getPath());
		final int startIndex = 60;
		final int endIndex = 68;
		codeSection = new CodeSection(secFile, startIndex, secFile, endIndex);
		assertThat(codeSection.getEndSectionIndex(), is(theInstance(endIndex)));
	}

	/**
	 * Test method for {@link de.uka.ipd.sdq.beagle.core.CodeSection#getEndCodeFile()}.
	 **/
	@Test
	public void testGetEndFile() {
		final File file =
			new File(CodeSectionTest.class.getResource("/de/uka/ipd/sdq/beagle/core/TestFile.java").getPath());
		final int startCodeLine = 4;
		final int endCodeLine = 15;
		CodeSection codeSection = new CodeSection(file, startCodeLine, file, endCodeLine);
		assertThat(codeSection.getEndFile(), is(theInstance(file)));

		final File secFile =
			new File(CodeSectionTest.class.getResource("/de/uka/ipd/sdq/beagle/core/PingPong.java").getPath());
		final int startIndex = 60;
		final int endIndex = 68;
		codeSection = new CodeSection(secFile, startIndex, secFile, endIndex);
		assertThat(codeSection.getEndFile(), is(theInstance(secFile)));

		codeSection = new CodeSection(file, startCodeLine, secFile, endIndex);
		assertThat(codeSection.getEndFile(), is(theInstance(secFile)));
	}

	/**
	 * Test method for {@link de.uka.ipd.sdq.beagle.core.CodeSection#getStartCodeLine()}.
	 **/
	@Test
	public void testGetStartCodeLine() {
		final File file =
			new File(CodeSectionTest.class.getResource("/de/uka/ipd/sdq/beagle/core/TestFile.java").getPath());
		final int startCodeLine = 4;
		final int endCodeLine = 15;
		CodeSection codeSection = new CodeSection(file, startCodeLine, file, endCodeLine);
		assertThat(codeSection.getStartSectionIndex(), is(startCodeLine));

		final File secFile =
			new File(CodeSectionTest.class.getResource("/de/uka/ipd/sdq/beagle/core/PingPong.java").getPath());
		final int startIndex = 60;
		final int endIndex = 68;
		codeSection = new CodeSection(secFile, startIndex, secFile, endIndex);
		assertThat(codeSection.getStartSectionIndex(), is(theInstance(startIndex)));
	}

	/**
	 * Test method for {@link de.uka.ipd.sdq.beagle.core.CodeSection#getStartFile()}.
	 *
	 */
	@Test
	public void testGetStartFile() {
		final File file =
			new File(CodeSectionTest.class.getResource("/de/uka/ipd/sdq/beagle/core/TestFile.java").getPath());
		final int startCodeLine = 4;
		final int endCodeLine = 15;
		CodeSection codeSection = new CodeSection(file, startCodeLine, file, endCodeLine);
		assertThat(codeSection.getStartFile(), is(theInstance(file)));

		final File secFile =
			new File(CodeSectionTest.class.getResource("/de/uka/ipd/sdq/beagle/core/PingPong.java").getPath());
		final int startIndex = 60;
		final int endIndex = 68;
		codeSection = new CodeSection(secFile, startIndex, secFile, endIndex);
		assertThat(codeSection.getStartFile(), is(theInstance(secFile)));
	}

	/**
	 * Test method for {@link de.uka.ipd.sdq.beagle.core.CodeSection#toString()}.
	 **/
	@Test
	public void testToString() {
		final File file =
			new File(CodeSectionTest.class.getResource("/de/uka/ipd/sdq/beagle/core/TestFile.java").getPath());
		final int startCodeLine = 4;
		final int endCodeLine = 15;
		final CodeSection codeSection = new CodeSection(file, startCodeLine, file, endCodeLine);
		assertThat(codeSection.toString(), not(startsWith("CodeSection@")));
	}

}
