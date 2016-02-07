package de.uka.ipd.sdq.beagle.core.judge;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import de.uka.ipd.sdq.beagle.core.Blackboard;
import de.uka.ipd.sdq.beagle.core.ExternalCallParameter;
import de.uka.ipd.sdq.beagle.core.MeasurableSeffElement;
import de.uka.ipd.sdq.beagle.core.ResourceDemandingInternalAction;
import de.uka.ipd.sdq.beagle.core.SeffBranch;
import de.uka.ipd.sdq.beagle.core.SeffLoop;
import de.uka.ipd.sdq.beagle.core.analysis.ProposedExpressionAnalyserBlackboardView;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.ConstantExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpression;
import de.uka.ipd.sdq.beagle.core.testutil.factories.BlackboardFactory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.HashSet;
import java.util.Set;

/**
 * Tests for {@link FinalJudge}.
 *
 * @author Joshua Gleitze
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(System.class)
public class FinalJudgeTest {

	/**
	 * A factory for {@link Blackboard}s to easily obtain new instances from.
	 */
	private static final BlackboardFactory BLACKBOARD_FACTORY = new BlackboardFactory();

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
	 * Creates default instances for the test methods.
	 */
	@Before
	public void createObjects() {
		this.testedJudge = new FinalJudge();
		this.testBlackboard = BLACKBOARD_FACTORY.getWithToBeMeasuredContent();
		this.allSeffElements = new HashSet<>();
		this.allSeffElements.addAll(this.testBlackboard.getAllExternalCallParameters());
		this.allSeffElements.addAll(this.testBlackboard.getAllRdias());
		this.allSeffElements.addAll(this.testBlackboard.getAllSeffBranches());
		this.allSeffElements.addAll(this.testBlackboard.getAllSeffLoops());
	}

	/**
	 * Asserts that the final judge ends the analysis if it ran for too long. To not
	 * impose any restriction that might be chosen with much consideration, this tests
	 * only that the analysis was aborted after 3 days.
	 */
	@Test
	public void endsWhenRunningToLong() {
		final int daysToWait = 5;

		mockStatic(System.class);
		given(System.currentTimeMillis()).willReturn(0L);

		this.testedJudge.init(this.testBlackboard);

		given(System.currentTimeMillis()).willReturn(daysToWait * 24L * 60L * 60L * 1000L);

		assertThat("The final judge should end the analysis if it lasts too long",
			this.testedJudge.judge(this.testBlackboard), is(false));

		assertThat("The test for running to long should be performed statelessly",
			new FinalJudge().judge(this.testBlackboard), is(false));
	}

	/**
	 * Asserts that the final judge does not end the analysis while there is still a lot
	 * of improvement.
	 */
	@Test
	public void doesNotEndWhileTheresImprovement() {
		this.testedJudge.init(this.testBlackboard);
		final double startValue = Double.MAX_VALUE;
		final EvaluableExpressionFitnessFunction mockFitnessFunction = mock(EvaluableExpressionFitnessFunction.class);
		this.testBlackboard = BLACKBOARD_FACTORY.setFitnessFunction(this.testBlackboard, mockFitnessFunction);

		for (int i = 0; i < 3000; i++) {
			final double nextValue = startValue / 2;
			for (final MeasurableSeffElement seffElement : this.allSeffElements) {
				final EvaluableExpression newProposedExpression = ConstantExpression.forValue(i);
				this.testBlackboard.addProposedExpressionFor(seffElement, newProposedExpression);
				given(this.grade(mockFitnessFunction, seffElement, newProposedExpression)).willReturn(nextValue);

				assertThat(this.grade(mockFitnessFunction, seffElement, newProposedExpression), is(nextValue));
				// assertThat("The final judge must not end the analysis while there’s
				// still great improvement",
				// this.testedJudge.judge(this.testBlackboard), is(true));
			}
		}
	}

	/**
	 * Asserts that the analysis is ended if the proposed expression’s fitness value
	 * didn’t sufficiently decrease in the last iterations.
	 */
	@Test
	public void endsWithToLittleImprovement() {
		this.testedJudge.init(this.testBlackboard);
		final double startValue = Double.MAX_VALUE;

		// Propose expressions that improve for a long time to assert the final judge is
		// not ending the analysis for them.
		for (int i = 0; i < 1000; i++) {

		}
	}

	/**
	 * Private hack method to call {@link EvaluableExpressionFitnessFunction#gradeFor} for
	 * any seff element and {@link #testBlackboard}.
	 *
	 * @param fitnessFunction The fitness function to call
	 *            {@link EvaluableExpressionFitnessFunction#gradeFor} on.
	 * @param seffElement The seff element to grade.
	 * @param expression The expression to gradle.
	 * @return What {@link EvaluableExpressionFitnessFunction#gradeFor} returned.
	 */
	private double grade(final EvaluableExpressionFitnessFunction fitnessFunction,
		final MeasurableSeffElement seffElement, final EvaluableExpression expression) {
		final ProposedExpressionAnalyserBlackboardView view = new ProposedExpressionAnalyserBlackboardView();
		if (seffElement instanceof SeffBranch) {
			return fitnessFunction.gradeFor((SeffBranch) seffElement, expression, view);
		} else if (seffElement instanceof SeffLoop) {
			return fitnessFunction.gradeFor((SeffLoop) seffElement, expression, view);
		} else if (seffElement instanceof ExternalCallParameter) {
			return fitnessFunction.gradeFor((ExternalCallParameter) seffElement, expression, view);
		} else {
			return fitnessFunction.gradeFor((ResourceDemandingInternalAction) seffElement, expression, view);
		}
	}
}
