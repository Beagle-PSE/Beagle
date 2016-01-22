package de.uka.ipd.sdq.beagle.core.analysis;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.theInstance;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;

import de.uka.ipd.sdq.beagle.core.Blackboard;
import de.uka.ipd.sdq.beagle.core.ExternalCallParameter;
import de.uka.ipd.sdq.beagle.core.ResourceDemandingInternalAction;
import de.uka.ipd.sdq.beagle.core.SeffBranch;
import de.uka.ipd.sdq.beagle.core.SeffLoop;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpression;
import de.uka.ipd.sdq.beagle.core.testutil.factories.EvaluableExpressionFactory;
import de.uka.ipd.sdq.beagle.core.testutil.factories.ExternalCallParameterFactory;
import de.uka.ipd.sdq.beagle.core.testutil.factories.ResourceDemandingInternalActionFactory;
import de.uka.ipd.sdq.beagle.core.testutil.factories.SeffBranchFactory;
import de.uka.ipd.sdq.beagle.core.testutil.factories.SeffLoopFactory;

import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class MeasurementResultAnalyserBlackboardViewTest {

	private Blackboard mockedBlackboard;

	private MeasurementResultAnalyserBlackboardView testedView;

	/**
	 * A {@link SeffBranch} factory to easily obtain new instances from.
	 */
	private static final SeffBranchFactory SEFF_BRANCH_FACTORY = new SeffBranchFactory();

	/**
	 * A {@link SeffLoop} factory to easily obtain new instances from.
	 */
	private static final SeffLoopFactory SEFF_LOOP_FACTORY = new SeffLoopFactory();

	/**
	 * An {@link ExternalCallParameter} factory to easily obtain new instances from.
	 */
	private static final ExternalCallParameterFactory EXTERNAL_CALL_PARAMETER_FACTORY =
		new ExternalCallParameterFactory();

	/**
	 * A {@link ResourceDemandingInternalAction} factory to easily obtain new instances
	 * from.
	 */
	private static final ResourceDemandingInternalActionFactory RDIA_FACTORY =
		new ResourceDemandingInternalActionFactory();

	/**
	 * An {@link EvaluableExpression} factory to easily obtain new instances from.
	 */
	private static final EvaluableExpressionFactory EVALUABLE_EXPRESSION_FACTORY = new EvaluableExpressionFactory();

	@Before
	public void createView() throws Exception {
		this.mockedBlackboard = mock(Blackboard.class);
		this.testedView = new MeasurementResultAnalyserBlackboardView(mockedBlackboard);
	}

	/**
	 * Test method for {@link MeasurementResultAnalyserBlackboardView#getAllRdias()}.
	 * Asserts that:
	 *
	 * <ul>
	 *
	 * <li> The tested view returns the instance it obtained from the blackboard
	 *
	 * </ul>
	 *
	 */
	@Test
	public void testGetAllRdias() {
		Set<ResourceDemandingInternalAction> testInstance = new HashSet<>();
		given(mockedBlackboard.getAllRdias()).willReturn(testInstance);

		Set<ResourceDemandingInternalAction> result = testedView.getAllRdias();
		assertThat("The testedView should return the instance it obtained from the blackboad", result,
			is(theInstance(testInstance)));
	}

	/**
	 * Test method for
	 * {@link MeasurementResultAnalyserBlackboardView#getAllSeffBranches()}. Asserts that:
	 *
	 * <ul>
	 *
	 * <li> The tested view returns the instance it obtained from the blackboard
	 *
	 * </ul>
	 *
	 */
	@Test
	public void testGetAllSeffBranches() {
		Set<SeffBranch> testInstance = new HashSet<>();
		given(mockedBlackboard.getAllSeffBranches()).willReturn(testInstance);

		Set<SeffBranch> result = testedView.getAllSeffBranches();
		assertThat("The testedView should return the instance it obtained from the blackboad", result,
			is(theInstance(testInstance)));
	}

	@Test
	public void testGetAllSeffLoops() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetAllExternalCallParameters() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link MeasurementResultAnalyserBlackboardView#addToBeMeasuredRdias(java.util.Collection)}
	 * . Asserts that:
	 *
	 * <ul>
	 *
	 * <li> The call is delegated to the blackboard.
	 *
	 * </ul>
	 */
	@Test
	public void testAddToBeMeasuredRdias() {
		Set<ResourceDemandingInternalAction> addedRdias = RDIA_FACTORY.getAllAsSet();

		testedView.addToBeMeasuredRdias(addedRdias);
		then(mockedBlackboard).should().addToBeMeasuredRdias(same(addedRdias));
	}

	/**
	 * Test method for
	 * {@link MeasurementResultAnalyserBlackboardView#addToBeMeasuredRdias(java.util.Collection)}
	 * . Asserts that:
	 *
	 * <ul>
	 *
	 * <li> The call is delegated to the blackboard.
	 *
	 * </ul>
	 */
	@Test
	public void testAddToBeMeasuredSeffBranches() {
		Set<SeffBranch> addedSeffBranches = SEFF_BRANCH_FACTORY.getAllAsSet();

		testedView.addToBeMeasuredSeffBranches(addedSeffBranches);
		then(mockedBlackboard).should().addToBeMeasuredSeffBranches(same(addedSeffBranches));
	}

	@Test
	public void testAddToBeMeasuredSeffLoops() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddToBeMeasuredExternalCallParameters() {
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
	public void testAddProposedExpressionFor() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetFitnessFunction() {
		fail("Not yet implemented");
	}

	@Test
	public void testReadFor() {
		fail("Not yet implemented");
	}

	@Test
	public void testWriteFor() {
		fail("Not yet implemented");
	}

	@Test
	public void testHashCode() {
		fail("Not yet implemented");
	}

	@Test
	public void testEquals() {
		fail("Not yet implemented");
	}

	@Test
	public void testToString() {
		fail("Not yet implemented");
	}

}
