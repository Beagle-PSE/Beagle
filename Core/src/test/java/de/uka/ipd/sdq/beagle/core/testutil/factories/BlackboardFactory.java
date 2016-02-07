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
	private static final ResourceDemandingInternalActionFactory RDIA_FACTORY =
		new ResourceDemandingInternalActionFactory();

	/**
	 * A {@link SeffBranch} factory to easily obtain new instances from.
	 */
	private static final SeffBranchFactory SEFF_BRANCH_FACTORY = new SeffBranchFactory();

	/**
	 * A {@link SeffLoop} factory to easily obtain new instances from.
	 */
	private static final SeffLoopFactory SEFF_LOOP_FACTORY = new SeffLoopFactory();

	/**
	 * A {@link ExternalCallParameter} factory to easily obtain new instances from.
	 */
	private static final ExternalCallParameterFactory EXTERNAL_CALL_PARAMETER_FACTORY =
		new ExternalCallParameterFactory();

	private static final EvaluableExpressionFitnessFunctionFactory EVA_EX_FACTORY = new EvaluableExpressionFitnessFunctionFactory();
	
	/**
	 * Creates a new blackboard with nothing written on it.
	 *
	 * @return A new blackboard instance, without any data on it.
	 */
	public Blackboard getEmpty() {
		return new Blackboard(new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>(), EVA_EX_FACTORY.getOne());
	}

	/**
	 * Creates a new blackboard, filled with all MeasurableSeffElements and
	 * ToBeMeasurement Content.
	 *
	 * @return A new blackboard instance with data.
	 */
	public Blackboard getWithToBeMeasuredContent() {
		final Set<ResourceDemandingInternalAction> rdiaSet = RDIA_FACTORY.getAllAsSet();
		final Set<SeffBranch> seffBranchSet = SEFF_BRANCH_FACTORY.getAllAsSet();
		final Set<SeffLoop> seffLoopSet = SEFF_LOOP_FACTORY.getAllAsSet();
		final Set<ExternalCallParameter> externalCallParameterSet = EXTERNAL_CALL_PARAMETER_FACTORY.getAllAsSet();

		final Blackboard blackboard = new Blackboard(RDIA_FACTORY.getAllAsSet(), SEFF_BRANCH_FACTORY.getAllAsSet(),
			SEFF_LOOP_FACTORY.getAllAsSet(), EXTERNAL_CALL_PARAMETER_FACTORY.getAllAsSet(), EVA_EX_FACTORY.getOne());
		blackboard.addToBeMeasuredExternalCallParameters(externalCallParameterSet);
		blackboard.addToBeMeasuredRdias(rdiaSet);
		blackboard.addToBeMeasuredSeffBranches(seffBranchSet);
		blackboard.addToBeMeasuredSeffLoops(seffLoopSet);

		return blackboard;

	}

}
