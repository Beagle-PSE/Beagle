package de.uka.ipd.sdq.beagle.core;

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

}
