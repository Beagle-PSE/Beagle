package de.uka.ipd.sdq.beagle.analysis;

import de.uka.ipd.sdq.beagle.core.Blackboard;
import de.uka.ipd.sdq.beagle.core.ResourceDemandingInternalAction;
import de.uka.ipd.sdq.beagle.core.SEFFBranch;
import de.uka.ipd.sdq.beagle.core.SEFFLoop;

import java.util.Collection;

/*
 * ATTENTION: Checkstyle turned off! remove this comment block when implementing this
 * class! CHECKSTYLE:OFF TODO
 */

/**
 * This view of the Blackboard is designed to be used by {@link ResultAnalyser}.
 * 
 * @author Christoph Michelbach
 */
public class AnalyserBlackboardView {

	public AnalyserBlackboardView(Blackboard blackboard) {
		// TODO: Implement method
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

	public void addAnalyseResult() {
		// TODO Implement method
	}

	public Collection<ResourceDemandingInternalAction> getResourceDemandingInternalActionsToBeMeasured() {
		return null; // TODO Implement method
	}

	public Collection<SEFFBranch> getSeffBranchesToBeMeasured() {
		return null; // TODO Implement method
	}

	public Collection<SEFFLoop> getSeffLoopsToBeMeasured() {
		return null; // TODO Implement method
	}
}
