package de.uka.ipd.sdq.beagle.analysis;

import de.uka.ipd.sdq.beagle.core.Blackboard;
import de.uka.ipd.sdq.beagle.core.ResourceDemandingInternalAction;
import de.uka.ipd.sdq.beagle.core.SEFFBranch;
import de.uka.ipd.sdq.beagle.core.SEFFLoop;

import java.util.Collection;

/**
 * This view of the {@link Blackboard} is designed to be used by {@link ResultAnalyser}.
 * 
 * @author Christoph Michelbach
 */
public class AnalyserBlackboardView {

	/**
	 * Creates a new {@code AnalyserBlackboardView} for {@code blackboard}.
	 *
	 * @param blackboard The {@link Blackboard} to be accessed through the
	 *            {@code AnalyserBlackboardView}.
	 */
	public AnalyserBlackboardView(final Blackboard blackboard) {

	}

	/**
	 * Delegates to {@link de.uka.ipd.sdq.beagle.core.Blackboard#getAllRDIAs()}.
	 *
	 * @return all {@linkplain ResourceDemandingInternalAction resource demanding internal
	 *         actions} known to Beagle
	 * @see de.uka.ipd.sdq.beagle.core.Blackboard#getAllRDIAs()
	 */
	public Collection<ResourceDemandingInternalAction> getAllResourceDemandingInternalActions() {
		return null;
	}

	/**
	 * Delegates to {@link de.uka.ipd.sdq.beagle.core.Blackboard#getAllSEFFBranches()}.
	 *
	 * @return all {@linkplain SEFFBranch SEFF branches} known to Beagle
	 * @see de.uka.ipd.sdq.beagle.core.Blackboard#getAllSEFFBranches()
	 */
	public Collection<SEFFBranch> getAllSeffBranches() {
		return null;
	}

	/**
	 * Delegates to {@link de.uka.ipd.sdq.beagle.core.Blackboard#getAllSeffLoops()}.
	 *
	 * @return all {@linkplain SEFFLoop SEFF loops} known to Beagle
	 * @see de.uka.ipd.sdq.beagle.core.Blackboard#getAllSeffLoops()
	 */
	public Collection<SEFFLoop> getAllSeffLoops() {
		return null;
	}

	/**
	 * Adds a result to the {@link Blackboard}.
	 */
	public void addAnalyseResult() {

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
}
