package de.uka.ipd.sdq.beagle.core;

import java.io.Serializable;

/**
 * Describes a section in examined software’s source code. Code sections may span multiple
 * methods and types. They are defined by the start statement in one compilation unit and
 * the end statement in another one that may or may not be the same as the first one. Code
 * sections are immutable, meaning that once created, their attribute cannot be changed.
 *
 * <p>A code section must describe a continuous part of the source code, meaning that the
 * last statement in the section will always be executed if the first one was executed,
 * given that no Exceptions occur. All statements that may potentially be executed after
 * the first statement but before the last one are considered to be within the section.
 *
 * @author Joshua Gleitze
 */
public class CodeSection implements Serializable {

	/**
	 * Serialisation version UID, see {@link java.io.Serializable}.
	 */
	private static final long serialVersionUID = -1823330022448293103L;

}
