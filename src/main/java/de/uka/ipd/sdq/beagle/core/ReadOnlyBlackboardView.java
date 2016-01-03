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

	/**
	 * Creates a new ReadOnlyBlackboardView for {@param blackboard}.
	 *
	 * @param blackboard The {@link Blackboard} to be accessed through the
	 *            ReadOnlyBlackboardView.
	 */
	public ReadOnlyBlackboardView(Blackboard blackboard) {
		// TODO: Implement method
	}

	/**
	 * Returns the {@link ResourceDemandingInternalAction}s to be measured.
	 *
	 * @return the {@link ResourceDemandingInternalAction}s to be measured
	 */
	public Collection<ResourceDemandingInternalAction> getResourceDemandingInternalActionsToBeMeasured() {
		return null; // TODO: Implement method
	}

	/**
	 * Returns the {@link SEFFBranch}es to be measured.
	 *
	 * @return the {@link SEFFBranch}es to be measured
	 */
	public Collection<SEFFBranch> getSeffBranchesToBeMeasured() {
		return null; // TODO: Implement method
	}

	/**
	 * Returns the {@link SEFFLoop}s to be measured.
	 *
	 * @return the {@link SEFFLoop}s to be measured
	 */
	public Collection<SEFFLoop> getSeffLoopsToBeMeasured() {
		return null; // TODO Implement method
	}

	/**
	 * Delegates to {@link de.uka.ipd.sdq.beagle.core.Blackboard#getAllRDIAs()}.
	 *
	 * @return all {@linkplain ResourceDemandingInternalAction resource demanding internal
	 *         actions} known to Beagle
	 * @see de.uka.ipd.sdq.beagle.core.Blackboard#getAllRDIAs()
	 */
	public Collection<ResourceDemandingInternalAction> getAllResourceDemandingInternalActions() {
		return null; // TODO Implement method
	}

	/**
	 * Delegates to {@link de.uka.ipd.sdq.beagle.core.Blackboard#getAllSEFFBranches()}.
	 *
	 * @return all {@linkplain SEFFBranch SEFF branches} known to Beagle
	 * @see de.uka.ipd.sdq.beagle.core.Blackboard#getAllSEFFBranches()
	 */
	public Collection<SEFFBranch> getAllSeffBranches() {
		return null; // TODO Implement method
	}

	/**
	 * Delegates to {@link de.uka.ipd.sdq.beagle.core.Blackboard#getAllSeffLoops()}.
	 *
	 * @return all {@linkplain SEFFLoop SEFF loops} known to Beagle
	 * @see de.uka.ipd.sdq.beagle.core.Blackboard#getAllSeffLoops()
	 */
	public Collection<SEFFLoop> getAllSeffLoops() {
		return null; // TODO Implement method
	}
}
