package de.uka.ipd.sdq.beagle.core;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.HashSet;
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
	private final Set<CodeSection> branches;

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
	 *            that. This is not checked during runtime, because it would solve the
	 *            halting problem. Immediately after that, the ending point is reached.
	 *            The set must contain at least {@code 2} code sections. Must not be
	 *            {@code null}. May not contain {@code null} entries.
	 * @throws IllegalArgumentException If {@code branches} has less than {@code 2}
	 *             branches.
	 */
	public SeffBranch(final Set<CodeSection> branches) {
		if (branches.size() <= 1) {
			throw new IllegalArgumentException(
				"The code section set for the SeffBranch had less than two code sections");
		}
		Validate.noNullElements(branches);
		this.branches = new HashSet<>(branches);
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
		final SeffBranch other = (SeffBranch) object;
		return new EqualsBuilder().append(this.branches, other.branches).isEquals();
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
	 *         The set is never {@code null}. This set never contains {@code null}
	 *         entries.
	 */
	public Set<CodeSection> getBranches() {
		return this.branches;
	}

	@Override
	public int hashCode() {
		// you pick a hard-coded, randomly chosen, non-zero, odd number
		// ideally different for each class
		return new HashCodeBuilder(21, 49).append(this.branches).toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("branches", this.branches).toString();
	}
}
