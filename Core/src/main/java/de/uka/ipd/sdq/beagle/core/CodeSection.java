package de.uka.ipd.sdq.beagle.core;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.File;
import java.io.Serializable;

/**
 * Describes a section in examined software’s source code. Code sections may span multiple
 * methods, types and compilation units. They are defined by a start statement in one
 * source code file and an end statement in another source code file that may or may not
 * be the same as the first one. A statement is identified by the index of the statement’s
 * first character in its source file. Code sections are immutable, meaning that once
 * created, their attributes cannot be changed.
 *
 * <p>A code section must describe a continuous part of source code, meaning that the last
 * statement in the section will always be executed if the first one was executed, given
 * that no Exceptions occur. Furthermore, if the last statement is not the same as the
 * first one, the last statement may only be executed after the first one was. All
 * statements that may potentially be executed after the first statement but before the
 * last one are considered to be “in” the section. A code section is considered to have
 * been “completely executed” if the section’s first and last statement were executed.
 * This does generally not mean that all statements in the section were executed. Code
 * Sections no fulfilling these requirements must not be created. However, this is not
 * checked at run time, because doing so would solve the halting problem.
 *
 * @author Joshua Gleitze
 * @author Roman Langrehr
 */
public class CodeSection implements Serializable {

	/**
	 * Serialisation version UID, see {@link java.io.Serializable}.
	 */
	private static final long serialVersionUID = -1823330022448293103L;

	/**
	 * The java file which contains the {@link #startStatementNumber} of this code
	 * section.
	 */
	private final File startFile;

	/**
	 * This code section’s first statement’s first character’s index in {@link #startFile}
	 * , starting with {@code 0}.
	 */
	private final int startStatementNumber;

	/**
	 * The java file which contains the {@link #endStatementNumber} of this code section.
	 */
	private final File endFile;

	/**
	 * This code section’s last statement’s first character’s index in {@link #endFile},
	 * starting with {@code 0}.
	 */
	private final int endStatementNumber;

	/**
	 * Creates a code section that spans from the statements starting at
	 * {@code startIndex} in {@code startFile} to the statement starting at
	 * {@code endIndex} in {@code endFile}.
	 *
	 * @param startFile The file containing this section’s first statement. Must not be
	 *            {@code null} and {@link File#isFile() startFile.isFile()} must return
	 *            {@code true}.
	 * @param startIndex The index of this section’s first statement’s first character in
	 *            {@code startFile}. Counting starts at {@code 0}.
	 * @param endFile The file containing this section’s last statement. Must not be
	 *            {@code null} and {@link File#isFile() endFile.isFile()} must return
	 *            {@code true}.
	 * @param endIndex The index of this section’s last statement’s first character in
	 *            {@code endFile}. Counting starts at {@code 0}.
	 * @throws IllegalArgumentException When {@code startFile.isFile()} or
	 *             {@code endFile.isFile()} returned {@code false}.
	 * @throws RuntimeException If {@code startFile} or {@code endFile} cannot be read.
	 */
	public CodeSection(final File startFile, final int startIndex, final File endFile, final int endIndex) {
		if (!startFile.isFile()) {
			throw new IllegalArgumentException("The given startFile is not a file.");
		}
		if (!endFile.isFile()) {
			throw new IllegalArgumentException("The given endFile is not a file.");
		}
		Validate.isTrue(startIndex >= 0, "The startIndex must be non-neagtive, but was %d", startIndex);
		Validate.isTrue(endIndex >= 0, "The endIndex must be non-neagtive, but was %d", endIndex);
		final long startFileChars = this.countChars(startFile);
		final long endFileChars = this.countChars(endFile);
		Validate.isTrue(startIndex < startFileChars,
			"The startIndex was not in the startFile. It was %d, but the file’s size was %d.", startIndex,
			startFileChars);
		Validate.isTrue(endIndex < startFileChars,
			"The endIndex was not in the endFile. It was %d, but the file’s size was %d.", endIndex, endFileChars);
		this.startFile = startFile;
		this.startStatementNumber = startIndex;
		this.endFile = endFile;
		this.endStatementNumber = endIndex;
	}

	@Override
	public boolean equals(final Object object) {
		if (object == null) {
			return false;
		}
		if (object == this) {
			return true;
		}
		if (object.getClass() != this.getClass()) {
			return false;
		}
		final CodeSection other = (CodeSection) object;
		return new EqualsBuilder().append(this.startFile, other.startFile)
			.append(this.startStatementNumber, other.startStatementNumber)
			.append(this.endFile, other.endFile)
			.append(this.endStatementNumber, other.endStatementNumber)
			.isEquals();
	}

	/**
	 * Gets the file that contains this section’s last statement.
	 *
	 * @return The file containing this section’s last statement. Will never be
	 *         {@code null} and {@code getEndFile().isFile()} always returns {@code true}.
	 */
	public File getEndFile() {
		return this.endFile;
	}

	/**
	 * Gets the index of the first character of the last statement in this code section.
	 * Counting starts at 0. The number thus describes how many characters precede the
	 * section’s last statement in the {@linkplain #getEndFile() end source code file}.
	 *
	 * @return The last statement’s index. A non-negative integer.
	 */
	public int getEndSectionIndex() {
		return this.endStatementNumber;
	}

	/**
	 * Gets the file that contains this section’s first statement.
	 *
	 * @return The file containing this section’s first statement. Will never be
	 *         {@code null} and {@code getStartFile().isFile()} always returns
	 *         {@code true}.
	 */
	public File getStartFile() {
		return this.startFile;
	}

	/**
	 * Gets the index of the first character of the last statement in this code section.
	 * Counting starts at 0. The number thus describes how many characters precede the
	 * section’s last statement in the {@linkplain #getEndFile() end source code file}.
	 *
	 * @return The first statement’s index. A non-negative integer.
	 */
	public int getStartSectionIndex() {
		return this.startStatementNumber;
	}

	@Override
	public int hashCode() {
		// you pick a hard-coded, randomly chosen, non-zero, odd number
		// ideally different for each class
		return new HashCodeBuilder(23, 45).append(this.startFile)
			.append(this.startStatementNumber)
			.append(this.endFile)
			.append(this.endStatementNumber)
			.toHashCode();
	}

	@Override
	public String toString() {
		final String startFileName = this.startFile.getName().replace(".java", "");
		final String endFileName =
			this.startFile.equals(this.endFile) ? "" : this.endFile.getName().replace(".java", "") + ":";
		return String.format("%s:%d–%s%d", startFileName, this.startStatementNumber, endFileName,
			this.endStatementNumber);
	}

	/**
	 * Reads the number of characters in a text-file.
	 *
	 * @param file The file to read.
	 * @return The number of bytes in this file. The number of bytes is returned as this
	 *         is, for most source code files, a good approximation for the number of
	 *         characters and the files don't need to be read for this. This approximation
	 *         never produces false errors.
	 */
	private long countChars(final File file) {
		return file.length();
	}
}
