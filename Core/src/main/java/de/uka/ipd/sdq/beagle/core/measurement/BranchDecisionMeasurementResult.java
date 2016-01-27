package de.uka.ipd.sdq.beagle.core.measurement;
/**
 * ATTENTION: Test coverage check turned off. Remove this comments block when implementing
 * this class!
 * 
 * <p>COVERAGE:OFF
 */

import de.uka.ipd.sdq.beagle.core.SeffBranch;

import org.apache.commons.lang3.Validate;

/**
 * A result of measuring which branch is taken in a branching source code construct. This
 * result expresses that the result’s “indexth” branch was taken when a branching
 * construct was executed with the given {@link Parameterisation}.
 *
 * TODO: make branch use list, not set, cause of index!!!
 *
 * @author Joshua Gleitze
 * @author Roman Langrehr
 */
public class BranchDecisionMeasurementResult extends ParameterisationDependentMeasurementResult {

	/**
	 * The index of the branch in the associated {@link SeffBranch}'s branch list that was
	 * executed.
	 */
	private int branchIndex;

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
	 * Creates a result for a branch measurement.
	 *
	 * @param parameterisation The state of variables during measurement.
	 * @param branchIndex The index of the branch in the associated {@link SeffBranch}'s
	 *            branch list that was executed.
	 */
	public BranchDecisionMeasurementResult(final Parameterisation parameterisation, final int branchIndex) {
		Validate.isTrue(this.branchIndex >= 0, "The measured branch index was negative: %d", this.branchIndex);
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
}
