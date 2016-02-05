package de.uka.ipd.sdq.beagle.core;

import static de.uka.ipd.sdq.beagle.core.testutil.EqualsMatcher.hasDefaultEqualsProperties;
import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.CoreMatchers.theInstance;
import static org.junit.Assert.assertThat;

import de.uka.ipd.sdq.beagle.core.testutil.ThrowingMethod;
import de.uka.ipd.sdq.beagle.core.testutil.factories.TestFileFactory;

import org.junit.Test;

import java.io.File;

/**
 * Tests {@link CodeSection} and contains all test cases needed to check every method.
 *
 * @author Annika Berger
 */
public class CodeSectionTest {

	/**
	 * A {@link TestFileFactory} providing methods to get files to use for tests.
	 */
	private static final TestFileFactory TEST_FILE_FACTORY = new TestFileFactory();

	/**
	 * Test method for
	 * {@link CodeSection#CodeSection(java.io.File, int, java.io.File, int)} .
	 */
	@Test
	public void constructor() {
		final File[] files = TEST_FILE_FACTORY.getAll();
		for (final File file : files) {
			// file.length gets the number of bytes, so it is an approximatation for the
			// number of characters. 50 is subtracted to be sure to stay within the file.
			final int maximum = (int) file.length() - 50;
			final int startIndex = 23;
			final int endIndex = maximum - 74;

			new CodeSection(file, startIndex, file, endIndex);
		}

		int startIndex = 4;
		int endIndex = 18;
		final CodeSection codeSection = new CodeSection(files[0], startIndex, files[0], endIndex);
		startIndex = 10;
		endIndex = 24;
		assertThat("Changing start index after initialisation must not affect the code section.",
			codeSection.getStartSectionIndex(), is(4));
		assertThat("Changing end index after initialisation must not affect the code section.",
			codeSection.getEndSectionIndex(), is(18));

		final File notExistingFile = new File("/de/uka/ipd/sdq/beagle/core/NotExisting.java");
		ThrowingMethod method = () -> {
			new CodeSection(notExistingFile, 23, files[0], 74);
		};
		assertThat("Startfile must exist.", method, throwsException(IllegalArgumentException.class));

		method = () -> {
			new CodeSection(files[0], 23, notExistingFile, 74);
		};
		assertThat("Endfile must exist.", method, throwsException(IllegalArgumentException.class));

		final int maximum = (int) files[0].length() - 50;
		final int validStartIndex = 7;
		final int validEndIndex = maximum - 7;
		final int invalidStartIndex = -4;
		final int invalidStartIndex2 = 50000;
		final int invalidEndIndex = -15;
		final int invalidEndIndex2 = 30000;

		method = () -> {
			new CodeSection(files[0], invalidStartIndex, files[0], validEndIndex);
		};
		assertThat("Start index must not be negative.", method, throwsException(IllegalArgumentException.class));

		method = () -> {
			new CodeSection(files[0], validStartIndex, files[0], invalidEndIndex);
		};
		assertThat("End index must not be negative.", method, throwsException(IllegalArgumentException.class));

		method = () -> {
			new CodeSection(files[0], validStartIndex, files[0], invalidEndIndex2);
		};
		assertThat("End index must be within the file.", method, throwsException(IllegalArgumentException.class));

		method = () -> {
			new CodeSection(files[0], invalidStartIndex2, files[0], validEndIndex);
		};
		assertThat("Start index must be within the file.", method, throwsException(IllegalArgumentException.class));

		method = () -> {
			new CodeSection(null, validStartIndex, files[0], validEndIndex);
		};
		assertThat("StartFile must not be null.", method, throwsException(NullPointerException.class));
		method = () -> {
			new CodeSection(files[0], validStartIndex, null, validEndIndex);
		};
		assertThat("EndFile must not be null.", method, throwsException(NullPointerException.class));
	}

	/**
	 * Test method for {@link CodeSection#equals()}.
	 *
	 * <p>Asserts that {@link CodeSection}s are equal if all parameters are the same and
	 * that they are not equal if one of the parameter changes.
	 **/
	@Test
	public void equalsAndHashCode() {
		final File[] files = TEST_FILE_FACTORY.getAll();
		final CodeSection[] codeSections = {
			new CodeSection(files[0], 7, files[1], 15), new CodeSection(files[1], 7, files[1], 15),
			new CodeSection(files[0], 7, files[0], 15), new CodeSection(files[0], 4, files[0], 15),
			new CodeSection(files[0], 4, files[0], 25), new CodeSection(files[0], 7, files[0], 15)
		};

		assertThat(codeSections[0], hasDefaultEqualsProperties());
		assertThat(codeSections[0], is(equalTo(codeSections[0])));
		assertThat(codeSections[2], is(equalTo(codeSections[5])));
		assertThat(codeSections[5], is(equalTo(codeSections[2])));
		assertThat(codeSections[0].hashCode(), is(equalTo(codeSections[0].hashCode())));
		assertThat(codeSections[5].hashCode(), is(equalTo(codeSections[2].hashCode())));
		assertThat(codeSections[2].hashCode(), is(equalTo(codeSections[5].hashCode())));
		assertThat(codeSections[0], is(not(equalTo(codeSections[1]))));
		assertThat(codeSections[0], is(not(equalTo(codeSections[2]))));
		assertThat(codeSections[2], is(not(equalTo(codeSections[3]))));
		assertThat(codeSections[3], is(not(equalTo(codeSections[4]))));
	}

	/**
	 * Test method for {@link CodeSection#getEndCodeLine()}.
	 **/
	@Test
	public void getEndCodeLine() {
		final File[] files = TEST_FILE_FACTORY.getAll();
		final int[] endIndices = {
			7, 19, 34, 93, 83, 2
		};
		for (int i = 0; i < endIndices.length; i++) {
			final CodeSection codeSection =
				new CodeSection(files[i % files.length], 45, files[i % files.length], endIndices[i]);
			assertThat(codeSection.getEndSectionIndex(), is(endIndices[i]));
		}
	}

	/**
	 * Test method for {@link CodeSection#getEndCodeFile()}.
	 **/
	@Test
	public void getEndFile() {
		final File[] files = TEST_FILE_FACTORY.getAll();
		final File file = files[0];
		final int startCodeLine = 4;
		final int endCodeLine = 15;
		CodeSection codeSection = new CodeSection(file, startCodeLine, file, endCodeLine);
		assertThat(codeSection.getEndFile(), is(theInstance(file)));

		final File secFile = files[1];
		final int startIndex = 60;
		final int endIndex = 68;
		codeSection = new CodeSection(secFile, startIndex, secFile, endIndex);
		assertThat(codeSection.getEndFile(), is(theInstance(secFile)));

		codeSection = new CodeSection(file, startCodeLine, secFile, endIndex);
		assertThat(codeSection.getEndFile(), is(theInstance(secFile)));
	}

	/**
	 * Test method for {@link CodeSection#getStartCodeLine()}.
	 **/
	@Test
	public void getStartCodeLine() {
		final File[] files = TEST_FILE_FACTORY.getAll();
		final int[] startIndices = {
			7, 19, 34, 93, 83, 2
		};
		for (int i = 0; i < startIndices.length; i++) {
			final CodeSection codeSection =
				new CodeSection(files[i % files.length], 45, files[i % files.length], startIndices[i]);
			assertThat(codeSection.getEndSectionIndex(), is(startIndices[i]));
		}
	}

	/**
	 * Test method for {@link CodeSection#getStartFile()}.
	 *
	 */
	@Test
	public void getStartFile() {
		final File[] files = TEST_FILE_FACTORY.getAll();
		final File file = files[0];
		final int startCodeLine = 4;
		final int endCodeLine = 15;
		CodeSection codeSection = new CodeSection(file, startCodeLine, file, endCodeLine);
		assertThat(codeSection.getStartFile(), is(theInstance(file)));

		final File secFile = files[1];
		final int startIndex = 60;
		final int endIndex = 68;
		codeSection = new CodeSection(secFile, startIndex, secFile, endIndex);
		assertThat(codeSection.getStartFile(), is(theInstance(secFile)));
	}

	/**
	 * Test method for {@link CodeSection#toString()}.
	 **/
	@Test
	public void toStringT() {
		final File file = TEST_FILE_FACTORY.getOne();
		final int startCodeLine = 4;
		final int endCodeLine = 15;
		final CodeSection codeSection = new CodeSection(file, startCodeLine, file, endCodeLine);
		assertThat(codeSection.toString(), not(startsWith("CodeSection@")));
	}

}
