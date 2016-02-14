package de.uka.ipd.sdq.beagle.core.analysis;

import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;

import de.uka.ipd.sdq.beagle.core.Blackboard;
import de.uka.ipd.sdq.beagle.core.ExternalCallParameter;
import de.uka.ipd.sdq.beagle.core.MeasurableSeffElement;
import de.uka.ipd.sdq.beagle.core.ResourceDemandingInternalAction;
import de.uka.ipd.sdq.beagle.core.SeffBranch;
import de.uka.ipd.sdq.beagle.core.SeffLoop;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpression;
import de.uka.ipd.sdq.beagle.core.measurement.BranchDecisionMeasurementResult;
import de.uka.ipd.sdq.beagle.core.measurement.LoopRepetitionCountMeasurementResult;
import de.uka.ipd.sdq.beagle.core.measurement.ParameterChangeMeasurementResult;
import de.uka.ipd.sdq.beagle.core.measurement.ResourceDemandMeasurementResult;
import de.uka.ipd.sdq.beagle.core.testutil.factories.BlackboardFactory;

import org.apache.commons.collections4.MultiSet;
import org.apache.commons.collections4.multiset.HashMultiSet;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * Tests {@link MeasurementResultAnalyserAverage} and contains the test cases needed to
 * check all methods.
 *
 * @author Annika Berger
 */
public class MeasurementResultAnalyserAverageTest {

	/**
	 * {@link BlackboardFactory} to easily create {@link Blackboard}s.
	 */
	private static final BlackboardFactory BLACKBOARD_FACTORY = new BlackboardFactory();

	/**
	 * Test method for
	 * {@link MeasurementResultAnalyserAverage#canContribute(ReadOnlyMeasurementResultAnalyserBlackboardView)}
	 * .
	 *
	 * <p>Asserts that a {@link NullPointerException} is thrown if the
	 * {@link ReadOnlyMeasurementResultAnalyserBlackboardView} is {@code null}. Asserts
	 * that same value is returned for views of the same blackboard. Asserts that [
	 * {@code false} is returned if there are no Measurement results and if the
	 * {@link MeasurementResultAnalyserAverage} was the last one to contribute on the
	 * {@link Blackboard}.
	 */
	@Test
	public void canContribute() {
		final MeasurementResultAnalyserAverage analyser = new MeasurementResultAnalyserAverage();

		assertThat("BlackboardView must not be null", () -> analyser.canContribute(null),
			throwsException(NullPointerException.class));

		final Blackboard blackboard = BLACKBOARD_FACTORY.getFull();
		final ReadOnlyMeasurementResultAnalyserBlackboardView blackboardView =
			new ReadOnlyMeasurementResultAnalyserBlackboardView(blackboard);
		final ReadOnlyMeasurementResultAnalyserBlackboardView blackboardView2 =
			new ReadOnlyMeasurementResultAnalyserBlackboardView(blackboard);
		assertThat(analyser.canContribute(blackboardView), is(analyser.canContribute(blackboardView2)));

		final Blackboard mockedBlackboard = mock(Blackboard.class);
		final Set<ResourceDemandMeasurementResult> resourceResults = new HashSet<>();
		given(mockedBlackboard.getMeasurementResultsFor(any(ResourceDemandingInternalAction.class)))
			.willReturn(resourceResults);
		final Set<BranchDecisionMeasurementResult> branchResults = new HashSet<>();
		given(mockedBlackboard.getMeasurementResultsFor(any(SeffBranch.class))).willReturn(branchResults);
		final Set<LoopRepetitionCountMeasurementResult> loopResults = new HashSet<>();
		given(mockedBlackboard.getMeasurementResultsFor(any(SeffLoop.class))).willReturn(loopResults);
		final Set<ParameterChangeMeasurementResult> parameterResults = new HashSet<>();
		given(mockedBlackboard.getMeasurementResultsFor(any(ExternalCallParameter.class))).willReturn(parameterResults);
		ReadOnlyMeasurementResultAnalyserBlackboardView mockedView =
			new ReadOnlyMeasurementResultAnalyserBlackboardView(mockedBlackboard);
		assertThat(analyser.canContribute(mockedView), is(false));

		resourceResults.add(new ResourceDemandMeasurementResult(2.3));
		given(mockedBlackboard.getMeasurementResultsFor(any(ResourceDemandingInternalAction.class)))
			.willReturn(resourceResults);
		branchResults.add(new BranchDecisionMeasurementResult(2));
		given(mockedBlackboard.getMeasurementResultsFor(any(SeffBranch.class))).willReturn(branchResults);
		loopResults.add(new LoopRepetitionCountMeasurementResult(2));
		given(mockedBlackboard.getMeasurementResultsFor(any(SeffLoop.class))).willReturn(loopResults);
		parameterResults.add(new ParameterChangeMeasurementResult());
		given(mockedBlackboard.getMeasurementResultsFor(any(ExternalCallParameter.class))).willReturn(parameterResults);
		mockedView = new ReadOnlyMeasurementResultAnalyserBlackboardView(mockedBlackboard);
		assertThat(analyser.canContribute(mockedView), is(true));

		final MeasurementResultAnalyserBlackboardView analyserView =
			new MeasurementResultAnalyserBlackboardView(mockedBlackboard);
		analyser.contribute(analyserView);
		assertThat(analyser.canContribute(mockedView), is(false));

	}

	/**
	 * Test method for
	 * {@link MeasurementResultAnalyserAverage#contribute(MeasurementResultAnalyserBlackboardView)}
	 * .
	 *
	 * <p>Asserts that no Exceptions are thrown for valid input and something was written
	 * on the blackboard.
	 */
	@Test
	public void contribute() {
		final MeasurementResultAnalyserAverage analyser = new MeasurementResultAnalyserAverage();

		final Blackboard blackboard = BLACKBOARD_FACTORY.getFull();
		final Set<MeasurableSeffElement> allElements = BLACKBOARD_FACTORY.getAllSeffElements(blackboard);
		final MeasurementResultAnalyserBlackboardView blackboardView =
			new MeasurementResultAnalyserBlackboardView(blackboard);
		final MultiSet<EvaluableExpression> proposedExpressions = new HashMultiSet<>();
		for (MeasurableSeffElement element : allElements) {
			proposedExpressions.addAll(blackboard.getProposedExpressionFor(element));
		}
		if (analyser.canContribute(new ReadOnlyMeasurementResultAnalyserBlackboardView(blackboard))) {
			analyser.contribute(blackboardView);
			final MultiSet<EvaluableExpression> newProposedExpressions = new HashMultiSet<>();
			for (MeasurableSeffElement element : allElements) {
				newProposedExpressions.addAll(blackboard.getProposedExpressionFor(element));
			}
			assertThat(newProposedExpressions, is(not(proposedExpressions)));
		} else {
			fail("This is no correct test case.");
		}

	}

}
