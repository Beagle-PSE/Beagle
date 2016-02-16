package de.uka.ipd.sdq.beagle.core.measurement;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.theInstance;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;

import de.uka.ipd.sdq.beagle.core.Blackboard;
import de.uka.ipd.sdq.beagle.core.BlackboardStorer;
import de.uka.ipd.sdq.beagle.core.ExternalCallParameter;
import de.uka.ipd.sdq.beagle.core.MeasurableSeffElement;
import de.uka.ipd.sdq.beagle.core.ResourceDemandingInternalAction;
import de.uka.ipd.sdq.beagle.core.SeffBranch;
import de.uka.ipd.sdq.beagle.core.SeffLoop;
import de.uka.ipd.sdq.beagle.core.analysis.MeasurementResultAnalyserBlackboardView;
import de.uka.ipd.sdq.beagle.core.judge.EvaluableExpressionFitnessFunction;
import de.uka.ipd.sdq.beagle.core.testutil.EqualsMatcher;
import de.uka.ipd.sdq.beagle.core.testutil.factories.BlackboardFactory;
import de.uka.ipd.sdq.beagle.core.testutil.factories.EvaluableExpressionFitnessFunctionFactory;
import de.uka.ipd.sdq.beagle.core.testutil.factories.ExternalCallParameterFactory;
import de.uka.ipd.sdq.beagle.core.testutil.factories.ResourceDemandingInternalActionFactory;
import de.uka.ipd.sdq.beagle.core.testutil.factories.SeffBranchFactory;
import de.uka.ipd.sdq.beagle.core.testutil.factories.SeffLoopFactory;

import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * Testing the {@link MeasurementControllerBlackboardView}.
 *
 * @author Joshua Gleitze
 * @author Ansgar Spiegler
 */
public class MeasurementControllerBlackboardViewTest {

	/**
	 * A {@link ResourceDemandingInternalAction} factory to easily obtain new instances
	 * from.
	 */
	private static final ResourceDemandingInternalActionFactory RDIA_FACTORY =
		new ResourceDemandingInternalActionFactory();

	/**
	 * A {@link ResourceDemandingInternalAction} factory to easily obtain new instances
	 * from.
	 */
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
	 * A {@link SeffBranch} factory to easily obtain new instances from.
	 */
	private static final EvaluableExpressionFitnessFunctionFactory FITNESS_FUNCTION_FACTORY =
		new EvaluableExpressionFitnessFunctionFactory();

	/**
	 * A {@link SeffBranch} factory to easily obtain new instances from.
	 */
	private static final BlackboardFactory BLACKBOARD_FACTORY = new BlackboardFactory();

	/**
	 * Blackboard mock-up that handles method calls.
	 */
	private Blackboard mockedBlackboard;

	/**
	 * The referenced view by this test.
	 */
	private MeasurementControllerBlackboardView testedView;

	/**
	 * Initializes {@link #mockedBlackboard} with Mockit and creates a new
	 * {@link #testedView} of it.
	 *
	 * @throws Exception Non specified exception because every exception in the following
	 *             test cases result in
	 */
	@Before
	public void createView() {
		this.mockedBlackboard = mock(Blackboard.class);
		this.testedView = new MeasurementControllerBlackboardView(this.mockedBlackboard);
	}

	/**
	 * Test method for {@link MeasurementControllerBlackboardView#hashCode()} . Asserts
	 * that:
	 *
	 * <ul>
	 *
	 * <li> The hash code is the same for equal view.
	 *
	 * </ul>
	 */
	@Test
	public void hashCodeT() {
		// Creating equal view to the existing one
		final MeasurementControllerBlackboardView secondView =
			new MeasurementControllerBlackboardView(this.mockedBlackboard);
		assertThat("Two equal MeasurementControllerBlackboardViews should have the same hashCode!",
			secondView.hashCode(), is(equalTo(this.testedView.hashCode())));
	}

	/**
	 * Test method for
	 * {@link MeasurementControllerBlackboardView#equals(MeasurementControllerBlackboardView)}
	 * . Asserts that:
	 *
	 * <ul>
	 *
	 * <li> Two different views containing the same Blackboard reference are equal
	 *
	 * <li> Two different views containing equal Blackboards that have not the same
	 * reference, are not equal.
	 *
	 * </ul>
	 */
	@Test
	public void equalsT() {
		// Creating equal view to the existing one
		final MeasurementControllerBlackboardView secondView =
			new MeasurementControllerBlackboardView(this.mockedBlackboard);
		assertThat(
			"Two MeasurementControllerBlackboardViews should be equal when they have the same blackboard instance!",
			secondView, is(equalTo(this.testedView)));

		// Equals for two views should only return true, if the views have exactly the
		// same blackboard reference!
		final Blackboard blackboardEmptyOne = BLACKBOARD_FACTORY.getEmpty();
		final Blackboard blackboardEmptyTwo = BLACKBOARD_FACTORY.getEmpty();
		final MeasurementControllerBlackboardView emptyViewOne =
			new MeasurementControllerBlackboardView(blackboardEmptyOne);
		final MeasurementControllerBlackboardView emptyViewTwo =
			new MeasurementControllerBlackboardView(blackboardEmptyTwo);

		assertThat("Two MeasurementControllerBlackboardViews should not be equal,"
			+ "if they have not exact the same Blackboard reference!", emptyViewOne, not(equalTo(emptyViewTwo)));

		assertThat("The Equals function should work properly for null, same instances and other objects", emptyViewOne,
			EqualsMatcher.hasDefaultEqualsProperties());
	}

	/**
	 * Test method for {@link MeasurementControllerBlackboardView#toString} . Asserts
	 * that:
	 *
	 * <ul>
	 *
	 * <li> toString does not return the standard String as defined in Object.class.
	 *
	 * </ul>
	 */
	@Test
	public void toStringT() {
		final String standardRepresentation = this.testedView.getClass().getName() + "@" + this.testedView.hashCode();
		assertThat("toString should be overwritten by a meaningful representation of this object!",
			standardRepresentation, not(equalTo(this.testedView.toString())));
	}

	/**
	 * Test method for {@link MeasurementControllerBlackboardView#getRdiasToBeMeasured()}.
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
	public void getRdiasToBeMeasured() {
		final Set<ResourceDemandingInternalAction> testInstance = new HashSet<>();
		given(this.mockedBlackboard.getRdiasToBeMeasured()).willReturn(testInstance);

		final Set<ResourceDemandingInternalAction> result = this.testedView.getRdiasToBeMeasured();
		assertThat("The testedView should return the instance it obtained from the blackboad", result,
			is(theInstance(testInstance)));
	}

	/**
	 * Test method for
	 * {@link MeasurementControllerBlackboardView#getSeffBranchesToBeMeasured()}. Asserts
	 * that:
	 *
	 * <ul>
	 *
	 * <li> The tested view returns the instance it obtained from the blackboard
	 *
	 * </ul>
	 *
	 */
	@Test
	public void getSeffBranchesToBeMeasured() {
		final Set<SeffBranch> testInstance = new HashSet<>();
		given(this.mockedBlackboard.getSeffBranchesToBeMeasured()).willReturn(testInstance);

		final Set<SeffBranch> result = this.testedView.getSeffBranchesToBeMeasured();
		assertThat("The testedView should return the instance it obtained from the blackboad", result,
			is(theInstance(testInstance)));
	}

	/**
	 * Test method for
	 * {@link MeasurementControllerBlackboardView#getSeffLoopsToBeMeasured()}. Asserts
	 * that:
	 *
	 * <ul>
	 *
	 * <li> The tested view returns the instance it obtained from the blackboard
	 *
	 * </ul>
	 *
	 */
	@Test
	public void getSeffLoopsToBeMeasured() {
		final Set<SeffLoop> testInstance = new HashSet<>();
		given(this.mockedBlackboard.getSeffLoopsToBeMeasured()).willReturn(testInstance);

		final Set<SeffLoop> result = this.testedView.getSeffLoopsToBeMeasured();
		assertThat("The testedView should return the instance it obtained from the blackboad", result,
			is(theInstance(testInstance)));
	}

	/**
	 * Test method for
	 * {@link MeasurementControllerBlackboardView#getExternalCallParametersToBeMeasured()}
	 * . Asserts that:
	 *
	 * <ul>
	 *
	 * <li> The tested view returns the instance it obtained from the blackboard
	 *
	 * </ul>
	 *
	 */
	@Test
	public void getExternalCallParametersToBeMeasured() {
		final Set<ExternalCallParameter> testInstance = new HashSet<>();
		given(this.mockedBlackboard.getExternalCallParametersToBeMeasured()).willReturn(testInstance);

		final Set<ExternalCallParameter> result = this.testedView.getExternalCallParametersToBeMeasured();
		assertThat("The testedView should return the instance it obtained from the blackboad", result,
			is(theInstance(testInstance)));
	}

	/**
	 * // @formatter:off
	 * Test method for
	 * {@link MeasurementControllerBlackboardView#addMeasurementResultFor(MeasurableSeffElement,
	 * MesasurableSeffElementMeasurementResult)}.
	 * // @formatter:on
	 * Asserts that:
	 *
	 * <ul>
	 *
	 * <li> The call is delegated to the blackboard.
	 *
	 * </ul>
	 */
	@Test
	public void addMeasurementResultForResourceDemandingInternalActionResourceDemandMeasurementResult() {
		final ResourceDemandingInternalAction rdia = RDIA_FACTORY.getOne();
		final ResourceDemandMeasurementResult results = new ResourceDemandMeasurementResult(2.5);

		this.testedView.addMeasurementResultFor(rdia, results);
		then(this.mockedBlackboard).should().addMeasurementResultFor(same(rdia), same(results));
	}

	/**
	 * Test method for
	 * // @formatter:off
	 * {@link MeasurementControllerBlackboardView#addMeasurementResultFor(MeasurableSeffElement,
	 * MesasurableSeffElementMeasurementResult)}.
	 * // @formatter:on
	 * Asserts that:
	 *
	 * <ul>
	 *
	 * <li> The call is delegated to the blackboard.
	 *
	 * </ul>
	 */
	@Test
	public void addMeasurementResultForSeffLoopLoopRepetitionCountMeasurementResult() {
		final SeffLoop seffLoop = SEFF_LOOP_FACTORY.getOne();
		final LoopRepetitionCountMeasurementResult results = new LoopRepetitionCountMeasurementResult(2);

		this.testedView.addMeasurementResultFor(seffLoop, results);
		then(this.mockedBlackboard).should().addMeasurementResultFor(same(seffLoop), same(results));
	}

	/**
	 * // @formatter:off
	 * Test method for
	 * {@link MeasurementControllerBlackboardView#addMeasurementResultFor(MeasurableSeffElement,
	 * MesasurableSeffElementMeasurementResult)}.
	 * // @formatter:on
	 * Asserts that:
	 *
	 * <ul>
	 *
	 * <li> The call is delegated to the blackboard.
	 *
	 * </ul>
	 */
	@Test
	public void addMeasurementResultForSeffBranchBranchDecisionMeasurementResult() {
		final SeffBranch seffBranch = SEFF_BRANCH_FACTORY.getOne();
		final BranchDecisionMeasurementResult results = new BranchDecisionMeasurementResult(2);

		this.testedView.addMeasurementResultFor(seffBranch, results);
		then(this.mockedBlackboard).should().addMeasurementResultFor(same(seffBranch), same(results));
	}

	/**
	 * // @formatter:off
	 * Test method for
	 * {@link MeasurementControllerBlackboardView#addMeasurementResultFor(MeasurableSeffElement,
	 * MesasurableSeffElementMeasurementResult)}.
	 * // @formatter:on
	 * Asserts that:
	 *
	 * <ul>
	 *
	 * <li> The call is delegated to the blackboard.
	 *
	 * </ul>
	 */
	@Test
	public void addMeasurementResultForExternalCallParameterParameterChangeMeasurementResult() {
		final ExternalCallParameter exParam = EXTERNAL_CALL_PARAMETER_FACTORY.getOne();
		final ParameterChangeMeasurementResult results = new ParameterChangeMeasurementResult();

		this.testedView.addMeasurementResultFor(exParam, results);
		then(this.mockedBlackboard).should().addMeasurementResultFor(same(exParam), same(results));
	}

	/**
	 * Test method for {@link MeasurementControllerBlackboardView#getGetFitnessFunction()}
	 * . Asserts that:
	 *
	 * <ul>
	 *
	 * <li> The tested view returns the instance it obtained from the blackboard.
	 *
	 * </ul>
	 */
	@Test
	public void getFitnessFunction() {
		final EvaluableExpressionFitnessFunction fitnessFunction = FITNESS_FUNCTION_FACTORY.getOne();
		given(this.mockedBlackboard.getFitnessFunction()).willReturn(fitnessFunction);

		final EvaluableExpressionFitnessFunction result = this.testedView.getFitnessFunction();
		assertThat("The testedView should return the Fitness Function instance it obtained from the blackboad", result,
			is(theInstance(fitnessFunction)));
	}

	/**
	 * Test method for {@link MeasurementResultAnalyserBlackboardView#readFor()} . Asserts
	 *
	 * <ul>
	 *
	 * <li> The tested view returns the instance it obtained from the blackboard.
	 *
	 * </ul>
	 *
	 */
	@Test
	public void readFor() {
		final String onBoard = new String();
		given(this.mockedBlackboard.readFor(TestStorer.class)).willReturn(onBoard);

		final String result = this.testedView.readFor(TestStorer.class);
		assertThat("The testedView should return the stored content it obtained from the blackboad", result,
			is(theInstance(onBoard)));
	}

	/**
	 * Test method for
	 * {@link MeasurementResultAnalyserBlackboardView#writeFor(writer, String)} . Asserts
	 * that:
	 *
	 * <ul>
	 *
	 * <li> The call is delegated to the blackboard.
	 *
	 * </ul>
	 */
	@Test
	public void writeFor() {
		final String writeOnBoard = new String();
		this.testedView.writeFor(TestStorer.class, writeOnBoard);
		then(this.mockedBlackboard).should().writeFor(same(TestStorer.class), same(writeOnBoard));
	}

	/**
	 * Test {@link BlackboardStorer}.
	 *
	 * @author Joshua Gleitze
	 */
	private final class TestStorer implements BlackboardStorer<String> {
	}
}
