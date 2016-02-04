package de.uka.ipd.sdq.beagle.core.testutil.factories;

import de.uka.ipd.sdq.beagle.core.CodeSection;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Builds CodeSections and provides a method to get all in one array.
 *
 * @author Annika Berger
 */
public final class CodeSectionFactory {

	/**
	 * A {@link TestFileFactory} providing methods to get files to use for tests.
	 */
	private static final TestFileFactory TEST_FILE_FACTORY = new TestFileFactory();

	/**
	 * Creates code sections.
	 *
	 * @return array containing all defined code sections
	 */
	public CodeSection[] getAll() {
		final File[] files = TEST_FILE_FACTORY.getAll();
		final int numberOfCodeSections = files.length * 3;
		final CodeSection[] codeSections = new CodeSection[numberOfCodeSections];

		for (int i = 0; i < numberOfCodeSections; i++) {
			final File startFile = files[i % files.length];
			final File endFile = files[2 * i % files.length];
			// file.length gets the number of bytes, so it is an approximatation for the
			// number of characters. 50 is subtracted to be sure to stay within the file.
			final int startMaximum = (int) startFile.length() - 50;
			final int endMaximum = (int) endFile.length() - 50;
			codeSections[i] =
				new CodeSection(startFile, (i + 10) * 111 % startMaximum, endFile, (i + 3) * 77 % endMaximum);
		}

		return codeSections;
	}

	/**
	 * Creates a set containing the code sections from {@link CodeSectionFactory#getAll()}
	 * .
	 *
	 * @return code sections in a set.
	 */
	public Set<CodeSection> getAllAsSet() {
		return new HashSet<>(Arrays.asList(this.getAll()));
	}

	/**
	 * Creates a set containing the code sections from {@link CodeSectionFactory#getAll()}
	 * .
	 *
	 * @return code sections in a list.
	 */
	public List<CodeSection> getAllAsList() {
		return new ArrayList<>(Arrays.asList(this.getAll()));
	}

	/**
	 * Gets one code section.
	 *
	 * @return A newly instantiated Code Section (you may not make any assumptions about).
	 */
	public CodeSection getOne() {
		return this.getAll()[0];
	}

}
