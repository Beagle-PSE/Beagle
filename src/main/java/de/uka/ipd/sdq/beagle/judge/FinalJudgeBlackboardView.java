package de.uka.ipd.sdq.beagle.judge;

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
 * This view of the Blackboard is designed to be used by {@link FinalJugde}, therefore
 * allowing remeasuring of {@link ResourceDemandingInternalAction}s, {@link SeffBranch}es,
 * and {@link SeffLoop}s.
 * 
 * @author Christoph Michelbach
 */
public class FinalJudgeBlackboardView {

	public FinalJudgeBlackboardView(Blackboard blackboard) {
		// TODO: Implement method
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

	public void setFinalResourceDemandingInternalAction(Collection<ResourceDemandingInternalAction> finalRdias) {
		// TODO Implement method
	}

	public void setFinalSeffBranches(Collection<SEFFBranch> finalSeffBranches) {
		// TODO Implement method
	}

	public void setFinalSeffLoops(Collection<SEFFLoop> finalSeffLoops) {
		// TODO Implement method
	}

	public void remeasureResourceDemandingInternalActions(Collection<ResourceDemandingInternalAction> remeasure) {
		// TODO Implement method
	}

	public void remeasureSeffBranches(Collection<SEFFBranch> remeasure) {
		// TODO Implement method
	}

	public void remeasureSeffLoops(Collection<SEFFLoop> remeasure) {
		// TODO Implement method
	}

}
