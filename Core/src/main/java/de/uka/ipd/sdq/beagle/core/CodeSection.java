package de.uka.ipd.sdq.beagle.core;

import java.io.File;
import java.io.Serializable;

/**
 * Describes a section in examined software’s source code. Code sections may span multiple
 * methods and types. They are defined by the start statement in one compilation unit and
 * the end statement in another compilation unit that may or may not be the same as the
 * first one. Code sections are immutable, meaning that once created, their attribute
 * cannot be changed.
 *
 * <p>It must be sure that in each run of the project under analysis the first line of
 * this code section is always executed before the last line and that the last line is
 * always executed after the first line, if the program did not crash during execution.
 * Otherwise the code section is not "valid".
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
	 * The java file which contains the {@link #startCodeLine} of this code section.
	 */
	private File startFile;

	/**
	 * The line index, starting with {@code 0} in the {@link #startFile}, of the first
	 * line in this code section.
	 */
	private int startCodeLine;

	/**
	 * The java file which contains the {@link #endCodeLine} of this code section.
	 */
	private File endFile;

	/**
	 * The line index, starting with {@code 0} in the {@link #endFile}, of the last line
	 * in this code section.
	 */
	private int endCodeLine;

	/**
	 * Create a code section from given code positions in the java files.
	 *
	 * @param startFile The java file which contains the {@code startCodeLine} of this
	 *            code section. Must not be {@code null} and {@code startFile.isFile()}
	 *            must return {@code true}.
	 * @param startCodeLine The line index, starting with {@code 0} in the
	 *            {@code startFile}, of the first line in this code section.
	 * @param endFile The java file which contains the {@code endCodeLine} of this code
	 *            section. Must not be {@code null} and {@code endFile.isFile()} must
	 *            return {@code true}.
	 * @param endCodeLine The line index, starting with {@code 0} in the {@code endFile},
	 *            of the last line in this code section.
	 * @throws IllegalArgumentException When {@code startFile.isFile()} or
	 *             {@code endFile.isFile()} returned {@code false}.
	 */
	public CodeSection(final File startFile, final int startCodeLine, final File endFile, final int endCodeLine) {
		if (!startFile.isFile()) {
			throw new IllegalArgumentException("The given startFile is not a file");
		}
		if (!endFile.isFile()) {
			throw new IllegalArgumentException("The given endFile is not a file");
		}
		this.startFile = startFile;
		this.startCodeLine = startCodeLine;
		this.endFile = endFile;
		this.endCodeLine = endCodeLine;
	}

	/**
	 * Gives the java file which contains the {@link #getStartCodeLine()} of this code
	 * section.
	 *
	 * @return the java file which contains the {@link #getStartCodeLine()} of this code
	 *         section. Is never {@code null} and {@code getStartFile().isFile()} always
	 *         returns {@code true}.
	 */
	public File getStartFile() {
		return this.startFile;
	}

	/**
	 * Gives the line index, starting with {@code 0} in the {@link #getStartFile()}, of
	 * the first line in this code section.
	 *
	 * @return the line index, starting with {@code 0} in the {@link #getStartFile()}, of
	 *         the first line in this code section.
	 */
	public int getStartCodeLine() {
		return this.startCodeLine;
	}

	/**
	 * Gives the java file which contains the {@link #getEndCodeLine} of this code
	 * section.
	 *
	 * @return the java file which contains the {@link #getEndCodeLine} of this code
	 *         section. Is never {@code null} and {@code getEndFile().isFile()} always
	 *         returns {@code true}.
	 */
	public File getEndFile() {
		return this.endFile;
	}

	/**
	 * Gives the line index, starting with {@code 0} in the {@code endFile}, of the last
	 * line in this code section.
	 *
	 * @return the line index, starting with {@code 0} in the {@code endFile}, of the last
	 *         line in this code section.
	 */
	public int getEndCodeLine() {
		return this.endCodeLine;
	}

}
