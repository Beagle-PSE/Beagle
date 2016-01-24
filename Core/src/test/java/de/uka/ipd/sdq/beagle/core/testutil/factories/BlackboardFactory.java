package de.uka.ipd.sdq.beagle.core.testutil.factories;

import de.uka.ipd.sdq.beagle.core.Blackboard;
import de.uka.ipd.sdq.beagle.core.ExternalCallParameter;
import de.uka.ipd.sdq.beagle.core.ResourceDemandingInternalAction;
import de.uka.ipd.sdq.beagle.core.SeffBranch;
import de.uka.ipd.sdq.beagle.core.SeffLoop;

import java.util.HashSet;
import java.util.Set;

/**
 * Factory for prepared Blackboard instances to be used by tests.
 *
 * @author Joshua Gleitze
 * @author Ansgar Spiegler
 */
public class BlackboardFactory {

	/**
	 * A {@link ResourceDemandingInternalAction} factory to easily obtain new instances
	 * from.
	 */
	private final ResourceDemandingInternalActionFactory RDIA_FACTORY = new ResourceDemandingInternalActionFactory();

	/**
	 * A {@link SeffBranch} factory to easily obtain new instances from.
	 */
	private final SeffBranchFactory SEFF_BRANCH_FACTORY = new SeffBranchFactory();

	/**
	 * A {@link SeffLoop} factory to easily obtain new instances from.
	 */
	private final SeffLoopFactory SEFF_LOOP_FACTORY = new SeffLoopFactory();

	/**
	 * A {@link ExternalCallParameter} factory to easily obtain new instances from.
	 */
	private final ExternalCallParameterFactory EXTERNAL_CALL_PARAMETER_FACTORY = new ExternalCallParameterFactory();

	/**
	 * Creates a new blackboard with nothing written on it.
	 *
	 * @return A new blackboard instance, without any data on it.
	 */
	public Blackboard getEmpty() {
		return new Blackboard(new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
	}

	/**
	 * Creates a new blackboard, filled with all MeasurableSeffElements and
	 * ToBeMeasurement Content.
	 *
	 * @return A new blackboard instance with data.
	 */
	public Blackboard getWithToBeMeasuredContent() {
		final Set<ResourceDemandingInternalAction> rdiaSet = this.RDIA_FACTORY.getAllAsSet();
		final Set<SeffBranch> seffBranchSet = this.SEFF_BRANCH_FACTORY.getAllAsSet();
		final Set<SeffLoop> seffLoopSet = this.SEFF_LOOP_FACTORY.getAllAsSet();
		final Set<ExternalCallParameter> externalCallParameterSet = this.EXTERNAL_CALL_PARAMETER_FACTORY.getAllAsSet();

		final Blackboard blackboard =
			new Blackboard(this.RDIA_FACTORY.getAllAsSet(), this.SEFF_BRANCH_FACTORY.getAllAsSet(),
				this.SEFF_LOOP_FACTORY.getAllAsSet(), this.EXTERNAL_CALL_PARAMETER_FACTORY.getAllAsSet());
		blackboard.addToBeMeasuredExternalCallParameters(externalCallParameterSet);
		blackboard.addToBeMeasuredRdias(rdiaSet);
		blackboard.addToBeMeasuredSeffBranches(seffBranchSet);
		blackboard.addToBeMeasuredSeffLoops(seffLoopSet);

		return blackboard;

	}

}
