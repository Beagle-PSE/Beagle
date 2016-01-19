package de.uka.ipd.sdq.beagle.core.testutil;

import de.uka.ipd.sdq.beagle.core.SeffBranch;

/**
 * Factory for pre-initialised Seff Branches to be used by tests.
 *
 * @author Joshua Gleitze
 */
public class SeffBranchFactory {

	/**
	 * Creates an array of newly initialised seff branches.
	 *
	 * @return 3 newly initialised seff branches.
	 */
	public SeffBranch[] getAllSeffBranches() {
		// will be done right once the proper SeffBranch constructor is there.
		return new SeffBranch[] {new SeffBranch(), new SeffBranch(), new SeffBranch()};
	}
}
