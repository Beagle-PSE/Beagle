package de.uka.ipd.sdq.beagle.judge;

import de.uka.ipd.sdq.beagle.core.Blackboard;
import de.uka.ipd.sdq.beagle.core.ResourceDemandingInternalAction;
import de.uka.ipd.sdq.beagle.core.SEFFBranch;
import de.uka.ipd.sdq.beagle.core.SEFFLoop;

import java.util.Collection;

/**
 * This view of the {@link Blackboard} is designed to be used by
 * {@link de.uka.ipd.sdq.beagle.judge.FinalJudge}, therefore allowing remeasuring of
 * {@link ResourceDemandingInternalAction}s, {@link SEFFBranch}es, and {@link SEFFLoop}s.
 * 
 * @author Christoph Michelbach
 */
public class FinalJudgeBlackboardView {

	/**
	 * Creates a new {@code FinalJudgeBlackboardView} for {@code blackboard}.
	 *
	 * @param blackboard The {@link Blackboard} to be accessed through the
	 *            {@code FinalJudgeBlackboardView}.
	 */
	public FinalJudgeBlackboardView(final Blackboard blackboard) {
	}

	/**
	 * Returns the {@link ResourceDemandingInternalAction}s to be measured.
	 * 
	 * @return the {@link ResourceDemandingInternalAction}s to be measured
	 */
	public Collection<ResourceDemandingInternalAction> getResourceDemandingInternalActionsToBeMeasured() {
		return null;
	}

	/**
	 * Returns the {@link SEFFBranch}es to be measured.
	 * 
	 * @return the {@link SEFFBranch}es to be measured
	 */
	public Collection<SEFFBranch> getSeffBranchesToBeMeasured() {
		return null;
	}

	/**
	 * Returns the {@link SEFFLoop}s to be measured.
	 * 
	 * @return the {@link SEFFLoop}s to be measured
	 */
	public Collection<SEFFLoop> getSeffLoopsToBeMeasured() {
		return null;
	}

	/**
	 * Sets the final {@link ResourceDemandingInternalAction}s on the {@link Blackboard}.
	 *
	 * @param finalRdias A collection of the final {@link ResourceDemandingInternalAction}
	 *            s to be set on the {@link Blackboard}
	 */
	public void setFinalResourceDemandingInternalAction(final Collection<ResourceDemandingInternalAction> finalRdias) {

	}

	/**
	 * Sets the final {@link SEFFBranch}es on the {@link Blackboard}.
	 *
	 * @param finalSeffBranches A collection of the final {@link SEFFBranch}es to be set
	 *            on the {@link Blackboard}
	 */
	public void setFinalSeffBranches(final Collection<SEFFBranch> finalSeffBranches) {

	}

	/**
	 * Sets the final {@link SEFFLoop}s on the {@link Blackboard}.
	 *
	 * @param finalSeffLoops A collection of the final {@link SEFFLoop}s to be set on the
	 *            {@link Blackboard}
	 */
	public void setFinalSeffLoops(final Collection<SEFFLoop> finalSeffLoops) {

	}

	/**
	 * Remeasures the {@link ResourceDemandingInternalAction}s in {@code remeasure}.
	 *
	 * @param remeasure The {@link ResourceDemandingInternalAction}s to be remeasured.
	 */
	public void remeasureResourceDemandingInternalActions(final Collection<ResourceDemandingInternalAction> remeasure) {

	}

	/**
	 * Remeasures the {@link SEFFBranch}es in {@code remeasure}.
	 *
	 * @param remeasure The {@link SEFFBranch}es to be remeasured.
	 */
	public void remeasureSeffBranches(final Collection<SEFFBranch> remeasure) {

	}

	/**
	 * Remeasures the {@link SEFFLoop}s in {@code remeasure}.
	 *
	 * @param remeasure The {@link SEFFLoop}s to be remeasured.
	 */
	public void remeasureSeffLoops(final Collection<SEFFLoop> remeasure) {

	}

}
