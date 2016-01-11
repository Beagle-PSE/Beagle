package de.uka.ipd.sdq.beagle.core;

import java.io.File;
import java.io.Serializable;

/**
 * Describes a section in examined software’s source code. Code sections may span multiple
 * methods and types. They are defined by a start statement in one source code file and an
 * end statement in another source code file that may or may not be the same as the first
 * one. Code sections are immutable, meaning that once created, their attributes cannot be
 * changed.
 *
 * <p>A code section must describe a continuous part of source code, meaning that the last
 * statement in the section will always be executed if the first one was executed, given
 * that no Exceptions occur. All statements that may potentially be executed after the
 * first statement but before the last one are considered to be in the section. A code
 * section is considered to have been “completely executed” if the section’s first and
 * last statement was executed. This does generally not mean that all statements in the
 * section were executed.
 *
 * @author Joshua Gleitze
 */
public class CodeSection implements Serializable {

	/**
	 * Serialisation version UID, see {@link java.io.Serializable}.
	 */
	private static final long serialVersionUID = -1823330022448293103L;

	/**
	 * Creates a code section that spans from the {@code startIndex}th statement in
	 * {@code startFile} to the {@code endIndex}th statement in {@code endFile}.
	 *
	 * @param startFile The file containing this section’s first statement. Must not be
	 *            {@code null} and {@code startFile.exists()} must return {@code true}.
	 * @param startIndex The index of the first statement in this section. Counting starts
	 *            at 0.
	 * @param endFile The file containing this section’s last statement. Must not be
	 *            {@code null} and {@code startFile.exists()} must return {@code true}.
	 * @param endIndex The index of the last statement in this section. Counting starts at
	 *            0.
	 */
	public CodeSection(final File startFile, final int startIndex, final File endFile, final int endIndex) {

	}

	/**
	 * Gets the file that contains this section’s first statement.
	 *
	 * @return The file containing this section’s first statement. Will never be
	 *         {@code null}.
	 */
	public File getStartFile() {
		return null;
	}

	/**
	 * Gets the index of the first statement in this code section. Counting starts at 0.
	 * The number thus describes how many statements precede the section’s first statement
	 * in the {@linkplain #getStartFile() start source code file}.
	 *
	 * @return The first statement’s index. A positive integer or 0.
	 */
	public int getStartSectionIndex() {
		return 0;
	}

	/**
	 * Gets the file that contains this section’s last statement.
	 *
	 * @return The file containing this section’s last statement. Will never be
	 *         {@code null}.
	 */
	public File getEndFile() {
		return null;
	}

	/**
	 * Gets the index of the last statement in this code section. Counting starts at 0.
	 * The number thus describes how many statements precede the section’s last statement
	 * in the {@linkplain #getStartFile() end source code file}.
	 *
	 * @return The last statement’s index. A positive integer or 0.
	 */
	public int getEndSectionIndex() {
		return 0;
	}

}
