package de.uka.ipd.sdq.beagle.measurement;

/**
 * A result of measuring which branch is taken in a branching source code construct. This
 * result expresses that the result’s “indexth” branch was taken when a branching
 * construct was executed with the given {@link Parameterisation}.
 *
 * @author Joshua Gleitze
 */
public class BranchDecisionResult extends ParameterisationDependentMeasurementResult {

	/**
	 * Creates a result for a branch measurement for which no parameterisation was
	 * recorded.
	 */
	public BranchDecisionResult() {
	}

	/**
	 * Creates a result for a branch measurement.
	 *
	 * @param parameterisation The state of variables during measurement.
	 */
	public BranchDecisionResult(final Parameterisation parameterisation) {
	}

	/**
	 * Gets the taken branch’s index.
	 *
	 * @return The index of the branch that was taken. Counting starts at 0, thus, this is
	 *         a positive Integer or 0.
	 */
	public int getBranchIndex() {
		return 0;
	}

	/**
	 * Gets the taken branch’s index.
	 *
	 * @param branchIndex The index of the branch that was taken. Counting starts at 0,
	 *            thus, it must not be smaller than 0.
	 * @throws IllegalArgumentException If {@code branchIndex < 0}.
	 */
	public void setBranchIndex(final int branchIndex) {
	}
}
