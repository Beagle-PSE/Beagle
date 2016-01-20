package de.uka.ipd.sdq.beagle.core.testutil.factories;

import de.uka.ipd.sdq.beagle.core.SeffBranch;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Factory for pre-initialised Seff Branches to be used by tests.
 *
 * @author Joshua Gleitze
 */
public class SeffBranchFactory {

	/**
	 * Creates a new seff branch.
	 *
	 * @return A newly instantiated seff branch (you may not make any assumptions about).
	 */
	public SeffBranch getOne() {
		return new SeffBranch();
	}

	/**
	 * Creates an array of newly initialised seff branches.
	 *
	 * @return 3 newly initialised seff branches.
	 */
	public SeffBranch[] getAll() {
		// will be done right once the proper SeffBranch constructor is there.
		return new SeffBranch[] {new SeffBranch(), new SeffBranch(), new SeffBranch()};
	}

	/**
	 * Creates a set of newly initialised seff branches.
	 *
	 * @return 3 newly initialised seff branches.
	 */
	public Set<SeffBranch> getAllAsSet() {
		return new HashSet<>(Arrays.asList(this.getAll()));
	}
}
