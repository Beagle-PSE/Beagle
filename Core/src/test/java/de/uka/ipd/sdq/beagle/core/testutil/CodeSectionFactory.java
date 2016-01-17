package de.uka.ipd.sdq.beagle.core.testutil;

import de.uka.ipd.sdq.beagle.core.CodeSection;

import java.io.File;

/**
 * Builds CodeSections and provides a method to get all in one array.
 *
 * @author Annika Berger
 */
public final class CodeSectionFactory {

	/**
	 * File for Test Class 'TestFile.java'.
	 */
	private static final File TEST_FILE =
		new File(CodeSectionFactory.class.getResource("/de/uka/ipd/sdq/beagle/core/TestFile.java").getPath());

	/**
	 * File for Test Class 'PingPong.java'.
	 */
	private static final File PINGPONG =
		new File(CodeSectionFactory.class.getResource("/de/uka/ipd/sdq/beagle/core/PingPong.java").getPath());

	/**
	 * Start Index of 'TestFile.java'.
	 */
	private static final int TESTFILE_STARTINDEX = 4;

	/**
	 * End Index of 'TestFile.java'.
	 */
	private static final int TESTFILE_ENDINDEX = 15;

	/**
	 * Start Index of 'PingPong.java'.
	 */
	private static final int PINGPONG_STARTINDEX = 60;

	/**
	 * End Index of 'PingPong.java'.
	 */
	private static final int PINGPONG_ENDINDEX = 68;
	
	/**
	 * Constructor, does not do anything.
	 *
	 */
	private CodeSectionFactory() {

	}

	/**
	 * Uses the in the class defined Files to create code section.
	 *
	 * @return array containing all defined code sections
	 */
	public static CodeSection[] getAllCodeSections() {
		final CodeSection[] codeSections =
			{new CodeSection(TEST_FILE, TESTFILE_STARTINDEX, TEST_FILE, TESTFILE_ENDINDEX),
				new CodeSection(PINGPONG, PINGPONG_STARTINDEX, PINGPONG, PINGPONG_ENDINDEX)

		};
		return codeSections;
	}

	
}
