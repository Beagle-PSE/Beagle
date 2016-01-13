package de.uka.ipd.sdq.beagle.core;

import java.util.Set;

/**
 * Models branches in a component's SEFF, originating from conditional constructs.
 *
 * <p>SEFF conditions are conditions (like Java’s if , if - else and switch - case
 * statements) which affect the calls a component makes to other components. Such
 * conditions are—contrary to conditions that stay within an internal action—modelled in a
 * component’s SEFF.
 *
 * @author Annika Berger
 * @author Roman Langrehr
 */
public class SeffBranch implements MeasurableSeffElement {

	/**
	 * Serialisation version UID, see {@link java.io.Serializable}.
	 */
	private static final long serialVersionUID = -1525692970296450080L;

	/**
	 * All branches for this SeffBranch.
	 */
	private Set<CodeSection> branches;

	/**
	 * Creates a SeffBranch using a given code section.
	 *
	 * @param branches A set of valid code sections. Each code sections represents a
	 *            branch of this SeffBranch. It must be sure, that two points of execution
	 *            exists, the so called beginning and ending of this branch, with the
	 *            following properties: When you would insert an empty code line at the
	 *            beginning and an empty code line at the ending, those would form a valid
	 *            code section with the beginning line as first line and the line at the
	 *            ending as last line. And each time the beginning point was reached,
	 *            exactly one code section of this branch gets executed immediately after
	 *            that. Immediately after that, the ending point is reached. The set must
	 *            contain at least {@code 2} code sections. Must not be {@code null}.
	 */
	public SeffBranch(final Set<CodeSection> branches) {
		this.branches = branches;
	}

	/**
	 * Gives a set of valid code sections representing a branch of this SeffBranch.
	 *
	 * @return A set of valid code sections representing a branch of this SeffBranch. They
	 *         fulfil the following property: Two points of execution exist, the so called
	 *         beginning and ending of this branch, with the following properties: When
	 *         you would insert an (empty) code line at the beginning and an (empty) code
	 *         line at the ending, those would form a valid code section with the
	 *         beginning line as first line and the line at the ending as last line. And
	 *         each time the beginning point was reached, exactly one code section of this
	 *         branch gets executed immediately after that. Immediately after that, the
	 *         ending point is reached. The set contains at least {@code 2} code sections.
	 *         The set is never {@code null}.
	 */
	public Set<CodeSection> getBranches() {
		return this.branches;
	}
}
