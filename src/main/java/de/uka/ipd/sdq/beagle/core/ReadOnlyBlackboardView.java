package de.uka.ipd.sdq.beagle.core;

import java.util.Set;

/**
 * View of the {@link Blackboard} only allowing reading access. Writing access isn't
 * provided.
 * 
 * @author Christoph Michelbach
 * 
 * @see {@link Blackboard} for information about {@link Blackboard} views
 */
public class ReadOnlyBlackboardView {

	/**
	 * Creates a new {@code ReadOnlyBlackboardView} for {@param blackboard}.
	 *
	 * @param blackboard The {@link Blackboard} to be accessed through the
	 *            {@code ReadOnlyBlackboardView}.
	 */
	public ReadOnlyBlackboardView(final Blackboard blackboard) {

	}

	/**
	 * Delegates to {@link de.uka.ipd.sdq.beagle.core.Blackboard#getRDIAsToBeMeasured()}.
	 *
	 * @return All {@linkplain ResourceDemandingInternalAction resource demanding internal
	 *         actions} to be measured. Changes to the returned set will not modify the
	 *         blackboard content. Is never {@code null}.
	 * @see de.uka.ipd.sdq.beagle.core.Blackboard#getRDIAsToBeMeasured()
	 */
	public Set<ResourceDemandingInternalAction> getRDIAsToBeMeasured() {
		return null;
	}

	/**
	 * Delegates to
	 * {@link de.uka.ipd.sdq.beagle.core.Blackboard#getSEFFBranchesToBeMeasured()}.
	 *
	 * @return All {@linkplain SEFFBranch SEFF branches} to be measured. Changes to the
	 *         returned set will not modify the blackboard content. Is never {@code null}.
	 * @see de.uka.ipd.sdq.beagle.core.Blackboard#getSEFFBranchesToBeMeasured()
	 */
	public Set<SEFFBranch> getSEFFBranchesToBeMeasured() {
		return null;
	}

	/**
	 * Delegates to
	 * {@link de.uka.ipd.sdq.beagle.core.Blackboard#getSEFFLoopsToBeMeasured()}.
	 *
	 * @return All {@linkplain SEFFLoop SEFF loops} to be measured. Changes to the
	 *         returned set will not modify the blackboard content. Is never {@code null}.
	 * @see de.uka.ipd.sdq.beagle.core.Blackboard#getSEFFLoopsToBeMeasured()
	 */
	public Set<SEFFLoop> getSEFFLoopsToBeMeasured() {
		return null;
	}

	/**
	 * Delegates to {@link de.uka.ipd.sdq.beagle.core.Blackboard#getAllRDIAs()}.
	 *
	 * @return all {@linkplain ResourceDemandingInternalAction resource demanding internal
	 *         actions} known to Beagle. Changes to the returned set will not modify the
	 *         blackboard content. Is never {@code null}.
	 * @see de.uka.ipd.sdq.beagle.core.Blackboard#getAllRDIAs()
	 */
	public Set<ResourceDemandingInternalAction> getAllRDIAs() {
		return null;
	}

	/**
	 * Delegates to {@link de.uka.ipd.sdq.beagle.core.Blackboard#getAllSEFFBranches()}.
	 *
	 * @return all {@linkplain SEFFBranch SEFF branches} known to Beagle. Changes to the
	 *         returned set will not modify the blackboard content. Is never {@code null}.
	 * @see de.uka.ipd.sdq.beagle.core.Blackboard#getAllSEFFBranches()
	 */
	public Set<SEFFBranch> getAllSEFFBranches() {
		return null;
	}

	/**
	 * Delegates to {@link de.uka.ipd.sdq.beagle.core.Blackboard#getAllSeffLoops()}.
	 *
	 * @return all {@linkplain SEFFLoop SEFF loops} known to Beagle. Changes to the
	 *         returned set will not modify the blackboard content. Is never {@code null}.
	 * @see de.uka.ipd.sdq.beagle.core.Blackboard#getAllSeffLoops()
	 */
	public Set<SEFFLoop> getAllSEFFLoops() {
		return null;
	}
}
