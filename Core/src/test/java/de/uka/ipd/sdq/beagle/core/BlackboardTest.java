package de.uka.ipd.sdq.beagle.core;

import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

import de.uka.ipd.sdq.beagle.core.testutil.BlackboardFactory;
import de.uka.ipd.sdq.beagle.core.testutil.SeffBranchFactory;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class BlackboardTest {

	/**
	 * A blackboard factory instance to easily obtain new blackboards from.
	 */
	private static final BlackboardFactory BLACKBOARD_FACTORY = new BlackboardFactory();

	/**
	 * A SeffBranch factory instance to easily obtain new SeffBranches from.
	 */
	private static final SeffBranchFactory SEFF_BRANCH_FACTORY = new SeffBranchFactory();

	@Test
	public void testBlackboard() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetAllRdias() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetAllSeffBranches() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetAllSeffLoops() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetAllExternalCallParameters() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetRdiasToBeMeasured() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetSeffBranchesToBeMeasured() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetSeffLoopsToBeMeasured() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetExternalCallParametersToBeMeasured() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddToBeMeasuredRdiasResourceDemandingInternalActionArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddToBeMeasuredRdiasCollectionOfResourceDemandingInternalAction() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddToBeMeasuredSeffBranches() {
		final Set<SeffBranch> seffBranchesSetWithNull =
			new HashSet<>(Arrays.asList(SEFF_BRANCH_FACTORY.getAllSeffBranches()));
		seffBranchesSetWithNull.add(null);
		final SeffBranch[] seffBranchesArrayWithNull =
			ArrayUtils.add(SEFF_BRANCH_FACTORY.getAllSeffBranches(), 1, (SeffBranch) null);

		assertThat("Adding null to measured SEFF Branches must not be possible (Set)",
			() -> BLACKBOARD_FACTORY.getEmpty().addToBeMeasuredSeffBranches(seffBranchesSetWithNull),
			throwsException(NullPointerException.class));
		assertThat("Adding null to measured SEFF Branches must not be possible (Array)",
			() -> BLACKBOARD_FACTORY.getEmpty().addToBeMeasuredSeffBranches(seffBranchesArrayWithNull),
			throwsException(NullPointerException.class));
		assertThat("Passing null to addToBeMeasuredSeffBranches is forbidden (Set)",
			() -> BLACKBOARD_FACTORY.getEmpty().addToBeMeasuredSeffBranches((Set<SeffBranch>) null),
			throwsException(NullPointerException.class));
		assertThat("Passing null to addToBeMeasuredSeffBranches is forbidden (Array)",
			() -> BLACKBOARD_FACTORY.getEmpty().addToBeMeasuredSeffBranches((SeffBranch[]) null),
			throwsException(NullPointerException.class));
	}

	@Test
	public void testAddToBeMeasuredSeffLoopsSeffLoopArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddToBeMeasuredSeffLoopsCollectionOfSeffLoop() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddToBeMeasuredExternalCallParametersExternalCallParameterArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddToBeMeasuredExternalCallParametersCollectionOfExternalCallParameter() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetMeasurementResultsForResourceDemandingInternalAction() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetMeasurementResultsForSeffBranch() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetMeasurementResultsForSeffLoop() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetMeasurementResultsForExternalCallParameter() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddMeasurementResultForResourceDemandingInternalActionResourceDemandMeasurementResult() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddMeasurementResultForSeffBranchBranchDecisionMeasurementResult() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddMeasurementResultForSeffLoopLoopRepetitionCountMeasurementResult() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddMeasurementResultForExternalCallParameterParameterChangeMeasurementResult() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetProposedExpressionFor() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddProposedExpressionFor() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetFinalExpressionFor() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetFinalExpressionFor() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetFitnessFunction() {
		fail("Not yet implemented");
	}

	@Test
	public void testWriteFor() {
		fail("Not yet implemented");
	}

	@Test
	public void testReadFor() {
		fail("Not yet implemented");
	}
}
