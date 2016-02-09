package de.uka.ipd.sdq.beagle.core.judge;

import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import de.uka.ipd.sdq.beagle.core.Blackboard;
import de.uka.ipd.sdq.beagle.core.ExternalCallParameter;
import de.uka.ipd.sdq.beagle.core.MeasurableSeffElement;
import de.uka.ipd.sdq.beagle.core.ResourceDemandingInternalAction;
import de.uka.ipd.sdq.beagle.core.SeffBranch;
import de.uka.ipd.sdq.beagle.core.SeffLoop;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.ConstantExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpression;
import de.uka.ipd.sdq.beagle.core.testutil.factories.BlackboardFactory;
import de.uka.ipd.sdq.beagle.core.testutil.factories.EvaluableExpressionFactory;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.stubbing.Answer;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;

import java.util.Set;

/**
 * Tests for {@link FinalJudge}.
 *
 * @author Joshua Gleitze
 */
@PrepareForTest(FinalJudge.class)
public class FinalJudgeTest {

	/**
	 * A factory for {@link Blackboard}s to easily obtain new instances from.
	 */
	private static final BlackboardFactory BLACKBOARD_FACTORY = new BlackboardFactory();

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
	 * Rule loading PowerMock (to mock static methods).
	 */
	@Rule
	public PowerMockRule loadPowerMock = new PowerMockRule();

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
	 * A mocked fitness function that it set on {@link #testBlackboard}.
	 */
	private EvaluableExpressionFitnessFunction mockFitnessFunction;

	/**
	 * Creates default instances for the test methods.
	 */
	@Before
	public void createObjects() {
		this.testedJudge = new FinalJudge();
		this.mockFitnessFunction = mock(EvaluableExpressionFitnessFunction.class);
		this.testBlackboard = BLACKBOARD_FACTORY.getWithFewElements();
		this.testBlackboard = BLACKBOARD_FACTORY.setFitnessFunction(this.testBlackboard, this.mockFitnessFunction);
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
	public void endsWhenRunningTooLong() {
		final int daysToWait = 5;

		mockStatic(System.class);
		given(System.currentTimeMillis()).willReturn(0L);

		this.testedJudge.init(this.testBlackboard);

		given(System.currentTimeMillis()).willReturn(daysToWait * 24L * 60L * 60L * 1000L);

		assertThat("The final judge should end the analysis if it lasts too long",
			this.testedJudge.judge(this.testBlackboard), ENDS_ANALYSIS);

		assertThat("The test for running to long should be performed statelessly",
			new FinalJudge().judge(this.testBlackboard), ENDS_ANALYSIS);
	}

	/**
	 * Asserts that the final judge does not end the analysis while there is still a lot
	 * of improvement.
	 */
	@Test
	public void doesNotEndWhileThereIsImprovement() {
		final int numberOfIterations = 100;
		final double startValue =
			Math.pow(2, Math.floor(Math.log(FinalJudge.MAX_CONSIDERED_FITNESS_VALUE) / Math.log(2)));
		this.testedJudge.init(this.testBlackboard);

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
		for (int i = 0; i < 500; i++) {
			for (final MeasurableSeffElement seffElement : this.allSeffElements) {
				this.testBlackboard.addProposedExpressionFor(seffElement, ConstantExpression.forValue(i));
			}
			decision = this.testedJudge.judge(this.testBlackboard);
			if (decision) {
				break;
			}
		}
		assertThat("The final judge must end the analysis if the results do not improve significantly", decision,
			is(ENDS_ANALYSIS));

		this.createObjects();
		new FinalJudge().init(this.testBlackboard);

		// the value the final judge returned the last time it was called
		decision = false;
		for (int i = 0; i < 500; i++) {
			for (final MeasurableSeffElement seffElement : this.allSeffElements) {
				this.testBlackboard.addProposedExpressionFor(seffElement, ConstantExpression.forValue(i));
			}
			decision = new FinalJudge().judge(this.testBlackboard);
			if (decision) {
				break;
			}
		}
		assertThat("Ending the analysis must be done statelessly", decision, is(ENDS_ANALYSIS));
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
		assertThat("The final judge should propose the fittest expression",
			this.testBlackboard.getFinalExpressionFor(seffElements[0]), is(testExpressions[0]));
		assertThat("The final judge should not ‘cross-propose’ expressions",
			this.testBlackboard.getFinalExpressionFor(seffElements[1]), is(nullValue()));

		this.testBlackboard.addProposedExpressionFor(seffElements[1], testExpressions[1]);
		given(this.mockFitnessFunction.gradeFor(eq(seffElements[1]), eq(testExpressions[1]), any())).willReturn(24d);
		assertThat("The final judge should propose the fittest expression",
			this.testBlackboard.getFinalExpressionFor(seffElements[0]), is(testExpressions[0]));
		assertThat("The final judge should propose the fittest expression",
			this.testBlackboard.getFinalExpressionFor(seffElements[1]), is(testExpressions[1]));

		this.testBlackboard.addProposedExpressionFor(seffElements[0], testExpressions[2]);
		this.testBlackboard.addProposedExpressionFor(seffElements[0], testExpressions[3]);
		this.testBlackboard.addProposedExpressionFor(seffElements[0], testExpressions[4]);
		given(this.mockFitnessFunction.gradeFor(eq(seffElements[0]), eq(testExpressions[2]), any())).willReturn(48d);
		given(this.mockFitnessFunction.gradeFor(eq(seffElements[0]), eq(testExpressions[3]), any())).willReturn(6d);
		given(this.mockFitnessFunction.gradeFor(eq(seffElements[0]), eq(testExpressions[4]), any())).willReturn(12d);
		assertThat("The final judge should propose the fittest expression",
			this.testBlackboard.getFinalExpressionFor(seffElements[0]), is(testExpressions[3]));
		assertThat("The final judge should propose the fittest expression",
			this.testBlackboard.getFinalExpressionFor(seffElements[1]), is(testExpressions[1]));
	}
}
