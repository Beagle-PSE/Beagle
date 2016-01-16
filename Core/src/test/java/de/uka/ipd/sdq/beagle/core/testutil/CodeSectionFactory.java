package de.uka.ipd.sdq.beagle.core.testutil;

import de.uka.ipd.sdq.beagle.core.CodeSection;

import java.io.File;

/**
 * Builds CodeSections and provides a method to get all in one array.
 *
 * @author Annika Berger
 */
public class CodeSectionFactory {

	/**
	 * Constructor, does not do anything.
	 *
	 */
	private CodeSectionFactory() {

	}

	/**
	 * File for Test Class 'TestFile.java'.
	 */
	private final static File testFile =
		new File(CodeSectionFactory.class.getResource("/de/uka/ipd/sdq/beagle/core/TestFile.java").getPath());

	/**
	 * File for Test Class 'PingPong.java'.
	 */
	private final static File pingPong =
		new File(CodeSectionFactory.class.getResource("/de/uka/ipd/sdq/beagle/core/PingPong.java").getPath());

	/**
	 * Start Index of 'TestFile.java'.
	 */
	private final static int testFileStartIndex = 4;

	/**
	 * End Index of 'TestFile.java'.
	 */
	private final static int testFileEndIndex = 15;

	/**
	 * Start Index of 'PingPong.java'.
	 */
	private final static int pingPongStartIndex = 60;

	/**
	 * End Index of 'PingPong.java'.
	 */
	private final static int pingPongEndIndex = 68;

	/**
	 * Uses the in the class defined Files to create code section.
	 *
	 * @return array containing all defined code sections
	 */
	public static CodeSection[] getAllCodeSections() {
		final CodeSection[] codeSections = {new CodeSection(testFile, testFileStartIndex, testFile, testFileEndIndex),
			new CodeSection(pingPong, pingPongStartIndex, pingPong, pingPongEndIndex)

		};

		return codeSections;
	}
}
