package de.uka.ipd.sdq.beagle.core;

import java.util.Collection;

/*
 * ATTENTION: Checkstyle turned off! remove this comment block when implementing this
 * class! CHECKSTYLE:OFF TODO
 */

/**
 * A view of the Blackboard only allowing reading access.
 * 
 * @author Christoph Michelbach
 */
public class ReadOnlyBlackboardView {

	public ReadOnlyBlackboardView(Blackboard blackboard) {
		// TODO: Implement method
	}

	public Collection<ResourceDemandingInternalAction> getResourceDemandingInternalActionsToBeMeasured() {
		return null; // TODO: Implement method
	}

	public Collection<SEFFBranch> getSeffBranchesToBeMeasured() {
		return null; // TODO: Implement method
	}

	public Collection<SEFFLoop> getSeffLoopsToBeMeasured() {
		return null; // TODO Implement method
	}

	public Collection<ResourceDemandingInternalAction> getAllResourceDemandingInternalActions() {
		return null; // TODO Implement method
	}

	public Collection<SEFFBranch> getAllSeffBranches() {
		return null; // TODO Implement method
	}

	public Collection<SEFFLoop> getAllSeffLoops() {
		return null; // TODO Implement method
	}
}
