package de.uka.ipd.sdq.beagle.core.judge;

import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;

import de.uka.ipd.sdq.beagle.core.Blackboard;
import de.uka.ipd.sdq.beagle.core.ExternalCallParameter;
import de.uka.ipd.sdq.beagle.core.MeasurableSeffElement;
import de.uka.ipd.sdq.beagle.core.ProjectInformation;
import de.uka.ipd.sdq.beagle.core.ResourceDemandingInternalAction;
import de.uka.ipd.sdq.beagle.core.SeffBranch;
import de.uka.ipd.sdq.beagle.core.SeffLoop;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.ConstantExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpression;
import de.uka.ipd.sdq.beagle.core.testutil.factories.BlackboardFactory;
import de.uka.ipd.sdq.beagle.core.testutil.factories.EvaluableExpressionFactory;
import de.uka.ipd.sdq.beagle.core.testutil.factories.ProjectInformationFactory;
import de.uka.ipd.sdq.beagle.core.timeout.Timeout;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.Answer;

import java.util.Set;

/**
 * Tests for {@link FinalJudge}.
 *
 * @author Joshua Gleitze
 */
public class FinalJudgeTest {

	/**
	 * A factory for {@link Blackboard}s to easily obtain new instances from.
	 */
	private static final BlackboardFactory BLACKBOARD_FACTORY = new BlackboardFactory();

	/**
	 * A factory for {@link ProjectInformation}s to easily obtain new instances from.
	 */
	private static final ProjectInformationFactory PROJECT_INFORMATION_FACTORY = new ProjectInformationFactory();

	/**
	 * A {@link EvaluableExpression} factory to easily obtain new instances from.
	 */
	private static final EvaluableExpressionFactory EVALUABLE_EXPRESSION_FACTORY = new EvaluableExpressionFactory();

	/**
	 * Matcher for the fact that the judge ends the analysis.
	 */
	private static final Matcher<Boolean> ENDS_ANALYSIS = is(true);

	/**
	 * Matcher for the fact that the judge does not end the analysis.
	 */
	private static final Matcher<Boolean> CONTINUES_ANALYSIS = is(false);

	/**
	 * The Final Judge under test.
	 */
	private FinalJudge testedJudge;

	/**
	 * The blackboard that the {@link #testedJudge} operates on.
	 */
	private Blackboard testBlackboard;

	/**
	 * All seff elements on {@link #testBlackboard}.
	 */
	private Set<MeasurableSeffElement> allSeffElements;

	/**
	 * A mocked fitness function that is set on {@link #testBlackboard}.
	 */
	private EvaluableExpressionFitnessFunction mockFitnessFunction;

	/**
	 * A mocked timeout that is set on {@link #testBlackboard}.
	 */
	private Timeout mockTimeout;

	/**
	 * Creates default instances for the test methods.
	 */
	@Before
	public void createObjects() {
		this.testedJudge = new FinalJudge();
		this.mockFitnessFunction = mock(EvaluableExpressionFitnessFunction.class);
		this.mockTimeout = mock(Timeout.class);
		this.testBlackboard = BLACKBOARD_FACTORY.getWithFewElements();
		this.testBlackboard = BLACKBOARD_FACTORY.setFitnessFunction(this.testBlackboard, this.mockFitnessFunction);
		this.testBlackboard = BLACKBOARD_FACTORY.setProjectInformation(this.testBlackboard,
			PROJECT_INFORMATION_FACTORY.setTimeout(this.testBlackboard.getProjectInformation(), this.mockTimeout));
		this.allSeffElements = BLACKBOARD_FACTORY.getAllSeffElements(this.testBlackboard);
	}

	/**
	 * Asserts that working on a not-initialised Final Judge gracefully throws an
	 * {@link IllegalStateException}. Asserts that initialisation is statelessly.
	 */
	@Test
	public void initialisation() {
		assertThat(() -> this.testedJudge.judge(this.testBlackboard), throwsException(IllegalStateException.class));
		this.testedJudge.init(this.testBlackboard);

		then(this.mockTimeout).should().init();

		// now judging should be possible without an exception
		this.testedJudge.judge(this.testBlackboard);
		new FinalJudge().judge(this.testBlackboard);
	}

	/**
	 * Asserts that the judge throws a {@linkplain NullPointerException} if the passed
	 * blackboard is {@code null}.
	 */
	@Test
	public void detectsNull() {
		assertThat(() -> new FinalJudge().init(null), throwsException(NullPointerException.class));
		this.testedJudge.init(this.testBlackboard);
		assertThat(() -> this.testedJudge.judge(null), throwsException(NullPointerException.class));
	}

	/**
	 * Asserts that the final judge ends the analysis if it ran for too long. To not
	 * impose any restriction that might be chosen with much consideration, this tests
	 * only that the analysis was aborted after 5 days.
	 */
	@Test
	public void endsAtTimeout() {
		final EvaluableExpression testExpression = EVALUABLE_EXPRESSION_FACTORY.getOne();
		final SeffBranch seffElement = this.testBlackboard.getAllSeffBranches().iterator().next();

		given(this.mockFitnessFunction.gradeFor(any(SeffBranch.class), any(), any())).willReturn(3d);

		this.testedJudge.init(this.testBlackboard);

		given(this.mockTimeout.isReached()).willReturn(true);

		this.testBlackboard.addProposedExpressionFor(seffElement, testExpression);
		assertThat("The final judge should end the analysis if the timeout is reached",
			this.testedJudge.judge(this.testBlackboard), ENDS_ANALYSIS);
		assertThat("The test for running to long should be performed statelessly",
			new FinalJudge().judge(this.testBlackboard), ENDS_ANALYSIS);
		assertThat("Must propose the fittest expression", this.testBlackboard.getFinalExpressionFor(seffElement),
			is(testExpression));
	}

	/**
	 * Asserts that the final judge does not end the analysis while there is still a lot
	 * of improvement.
	 */
	@Test
	public void doesNotEndWhileThereIsImprovement() {
		final int numberOfIterations = 100;
		final double startValue = FinalJudge.MAX_CONSIDERED_FITNESS_VALUE;

		this.testedJudge.init(this.testBlackboard);

		// each expression that’s new for an element will have half of the previous
		// fitness value.
		final Answer<Double> answerWithBigChange =
			(info) -> startValue * Math.pow(2, -info.getArgumentAt(1, ConstantExpression.class).getValue());
		given(this.mockFitnessFunction.gradeFor(any(SeffBranch.class), any(), any())).will(answerWithBigChange);
		given(this.mockFitnessFunction.gradeFor(any(SeffLoop.class), any(), any())).will(answerWithBigChange);
		given(this.mockFitnessFunction.gradeFor(any(ExternalCallParameter.class), any(), any()))
			.will(answerWithBigChange);
		given(this.mockFitnessFunction.gradeFor(any(ResourceDemandingInternalAction.class), any(), any()))
			.will(answerWithBigChange);

		for (int i = 0; i <= numberOfIterations / this.allSeffElements.size(); i++) {
			for (final MeasurableSeffElement seffElement : this.allSeffElements) {
				this.testBlackboard.addProposedExpressionFor(seffElement, ConstantExpression.forValue(i));

				assertThat(
					String.format("The final judge must not end the analysis while there’s still great improvement "
						+ "(stopped after %d iterations)", i * this.allSeffElements.size()),
					this.testedJudge.judge(this.testBlackboard), CONTINUES_ANALYSIS);
				assertThat("Must always select the best expression",
					this.testBlackboard.getFinalExpressionFor(seffElement), is(ConstantExpression.forValue(i)));
			}
		}
	}

	/**
	 * Asserts that the analysis is ended if the proposed expression’s fitness value
	 * didn’t sufficiently decrease in the last iterations. To not impose any restriction
	 * that might be chosen with much consideration, this tests only that the analysis was
	 * ended after 500 iterations.
	 */
	@Test
	public void endsIfTooLittleImprovement() {
		final double startValue = 100d;
		this.testedJudge.init(this.testBlackboard);

		// each expression that’s new for an element will have 99,9% of the previous
		// fitness value.
		final Answer<Double> answerWithLittleChange =
			(info) -> startValue * Math.pow(0.999, info.getArgumentAt(1, ConstantExpression.class).getValue());
		given(this.mockFitnessFunction.gradeFor(any(SeffBranch.class), any(), any())).will(answerWithLittleChange);
		given(this.mockFitnessFunction.gradeFor(any(SeffLoop.class), any(), any())).will(answerWithLittleChange);
		given(this.mockFitnessFunction.gradeFor(any(ExternalCallParameter.class), any(), any()))
			.will(answerWithLittleChange);
		given(this.mockFitnessFunction.gradeFor(any(ResourceDemandingInternalAction.class), any(), any()))
			.will(answerWithLittleChange);

		// the value the final judge returned the last time it was called
		boolean decision = false;
		int iteration;
		for (iteration = 0; iteration < 500; iteration++) {
			for (final MeasurableSeffElement seffElement : this.allSeffElements) {
				this.testBlackboard.addProposedExpressionFor(seffElement, ConstantExpression.forValue(iteration));
			}
			decision = this.testedJudge.judge(this.testBlackboard);
			if (decision) {
				break;
			}
		}
		assertThat("The final judge must end the analysis if the results do not improve significantly", decision,
			is(ENDS_ANALYSIS));
		for (final MeasurableSeffElement seffElement : this.allSeffElements) {
			assertThat("Must propose the best expression even if stopping the analysis",
				this.testBlackboard.getFinalExpressionFor(seffElement), is(ConstantExpression.forValue(iteration)));
		}

		this.createObjects();
		new FinalJudge().init(this.testBlackboard);

		// the value the final judge returned the last time it was called
		decision = false;
		for (iteration = 0; iteration < 500; iteration++) {
			for (final MeasurableSeffElement seffElement : this.allSeffElements) {
				this.testBlackboard.addProposedExpressionFor(seffElement, ConstantExpression.forValue(iteration));
			}
			decision = new FinalJudge().judge(this.testBlackboard);
			if (decision) {
				break;
			}
		}
		assertThat("Ending the analysis must be done statelessly", decision, is(ENDS_ANALYSIS));
		for (final MeasurableSeffElement seffElement : this.allSeffElements) {
			assertThat("Must select the best expression", this.testBlackboard.getFinalExpressionFor(seffElement),
				is(ConstantExpression.forValue(iteration)));
		}
	}

	/**
	 * Asserts that the analysis is ended if only perfect expressions are on the
	 * blackboard.
	 */
	@Test
	public void endsIfEverythingIsPerfect() {
		this.testBlackboard =
			BLACKBOARD_FACTORY.setFitnessFunction(BLACKBOARD_FACTORY.getFull(), this.mockFitnessFunction);
		this.testedJudge.init(this.testBlackboard);

		given(this.mockFitnessFunction.gradeFor(any(SeffBranch.class), any(), any())).willReturn(0d);
		given(this.mockFitnessFunction.gradeFor(any(SeffLoop.class), any(), any())).willReturn(0d);
		given(this.mockFitnessFunction.gradeFor(any(ExternalCallParameter.class), any(), any())).willReturn(0d);
		given(this.mockFitnessFunction.gradeFor(any(ResourceDemandingInternalAction.class), any(), any()))
			.willReturn(0d);
		assertThat("The final judge should end the analysis if everything is perfect",
			this.testedJudge.judge(this.testBlackboard), ENDS_ANALYSIS);
		for (final MeasurableSeffElement seffElemet : this.allSeffElements) {
			assertThat("Must select the best expression", this.testBlackboard.getFinalExpressionFor(seffElemet),
				is(not(nullValue())));
		}
	}

	/**
	 * Asserts that the fittest expression proposed is selected by the final judge.
	 */
	@Test
	public void selectsFittestExpression() {
		this.testedJudge.init(this.testBlackboard);

		final ResourceDemandingInternalAction[] seffElements =
			this.testBlackboard.getAllRdias().toArray(new ResourceDemandingInternalAction[0]);
		final EvaluableExpression[] testExpressions = EVALUABLE_EXPRESSION_FACTORY.getAll();

		this.testBlackboard.addProposedExpressionFor(seffElements[0], testExpressions[0]);
		given(this.mockFitnessFunction.gradeFor(eq(seffElements[0]), eq(testExpressions[0]), any())).willReturn(12d);
		this.testedJudge.judge(this.testBlackboard);
		assertThat("The final judge should select the fittest expression",
			this.testBlackboard.getFinalExpressionFor(seffElements[0]), is(testExpressions[0]));
		assertThat("The final judge should not ‘cross-select’ expressions",
			this.testBlackboard.getFinalExpressionFor(seffElements[1]), is(nullValue()));

		this.testBlackboard.addProposedExpressionFor(seffElements[1], testExpressions[1]);
		given(this.mockFitnessFunction.gradeFor(eq(seffElements[1]), eq(testExpressions[1]), any())).willReturn(24d);
		this.testedJudge.judge(this.testBlackboard);
		assertThat("The final judge should select the fittest expression",
			this.testBlackboard.getFinalExpressionFor(seffElements[0]), is(testExpressions[0]));
		assertThat("The final judge should select the fittest expression",
			this.testBlackboard.getFinalExpressionFor(seffElements[1]), is(testExpressions[1]));

		this.testBlackboard.addProposedExpressionFor(seffElements[0], testExpressions[2]);
		this.testBlackboard.addProposedExpressionFor(seffElements[0], testExpressions[3]);
		this.testBlackboard.addProposedExpressionFor(seffElements[0], testExpressions[4]);
		given(this.mockFitnessFunction.gradeFor(eq(seffElements[0]), eq(testExpressions[2]), any())).willReturn(48d);
		given(this.mockFitnessFunction.gradeFor(eq(seffElements[0]), eq(testExpressions[3]), any())).willReturn(6d);
		given(this.mockFitnessFunction.gradeFor(eq(seffElements[0]), eq(testExpressions[4]), any())).willReturn(12d);
		this.testedJudge.judge(this.testBlackboard);
		assertThat("The final judge should select the fittest expression",
			this.testBlackboard.getFinalExpressionFor(seffElements[0]), is(testExpressions[3]));
		assertThat("The final judge should select the fittest expression",
			this.testBlackboard.getFinalExpressionFor(seffElements[1]), is(testExpressions[1]));
	}
}
