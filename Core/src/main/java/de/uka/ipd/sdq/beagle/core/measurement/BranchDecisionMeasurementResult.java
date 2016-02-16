package de.uka.ipd.sdq.beagle.core.measurement;

import de.uka.ipd.sdq.beagle.core.SeffBranch;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * A result of measuring which branch is taken in a branching source code construct. This
 * result expresses that the result’s “indexth” branch was taken when a branching
 * construct was executed with the given {@link Parameterisation}.
 *
 * @author Joshua Gleitze
 * @author Roman Langrehr
 */
public class BranchDecisionMeasurementResult extends ParameterisationDependentMeasurementResult {

	/**
	 * The index of the branch in the associated {@link SeffBranch}'s branch list that was
	 * executed.
	 */
	private final int branchIndex;

	/**
	 * Creates a result for a branch measurement for which no parameterisation was
	 * recorded.
	 *
	 * @param branchIndex The index of the branch in the associated {@link SeffBranch}'s
	 *            branch list that was executed.
	 */
	public BranchDecisionMeasurementResult(final int branchIndex) {
		Validate.isTrue(branchIndex >= 0, "The measured branch index was negative: %d", branchIndex);
		this.branchIndex = branchIndex;
	}

	/**
	 * Creates a result for a parameterised branch measurement.
	 *
	 * @param parameterisation The state of variables during measurement.
	 * @param branchIndex The index of the branch in the associated {@link SeffBranch}'s
	 *            branch list that was executed.
	 */
	public BranchDecisionMeasurementResult(final Parameterisation parameterisation, final int branchIndex) {
		super(parameterisation);
		Validate.isTrue(branchIndex >= 0, "The measured branch index was negative: %d", branchIndex);
		this.branchIndex = branchIndex;
	}

	/**
	 * Gets the taken branch’s index.
	 *
	 * @return The index of the branch that was taken. Counting starts at 0, thus, this is
	 *         a positive Integer or 0.
	 */
	public int getBranchIndex() {
		return this.branchIndex;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).appendSuper(super.toString())
			.append("branch index", this.branchIndex)
			.toString();
	}
}
