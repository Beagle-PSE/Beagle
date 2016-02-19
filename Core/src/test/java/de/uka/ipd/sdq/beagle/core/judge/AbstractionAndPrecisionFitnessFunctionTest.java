package de.uka.ipd.sdq.beagle.core.judge;

import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThan;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;

import de.uka.ipd.sdq.beagle.core.ExternalCallParameter;
import de.uka.ipd.sdq.beagle.core.ResourceDemandingInternalAction;
import de.uka.ipd.sdq.beagle.core.SeffBranch;
import de.uka.ipd.sdq.beagle.core.SeffLoop;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpression;
import de.uka.ipd.sdq.beagle.core.measurement.BranchDecisionMeasurementResult;
import de.uka.ipd.sdq.beagle.core.measurement.LoopRepetitionCountMeasurementResult;
import de.uka.ipd.sdq.beagle.core.measurement.ParameterChangeMeasurementResult;
import de.uka.ipd.sdq.beagle.core.measurement.ResourceDemandMeasurementResult;
import de.uka.ipd.sdq.beagle.core.testutil.ThrowingMethod;
import de.uka.ipd.sdq.beagle.core.testutil.factories.ExternalCallParameterFactory;
import de.uka.ipd.sdq.beagle.core.testutil.factories.ResourceDemandingInternalActionFactory;
import de.uka.ipd.sdq.beagle.core.testutil.factories.SeffBranchFactory;
import de.uka.ipd.sdq.beagle.core.testutil.factories.SeffLoopFactory;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * Tests {@link AbstractionAndPrecisionFitnessFunction} and contains all test cases needed
 * to check every method.
 *
 * @author Annika Berger
 */
public class AbstractionAndPrecisionFitnessFunctionTest {

	/**
	 * A {@link ResourceDemandingInternalActionFactory} to easily create
	 * {@link ResourceDemandingInternalAction}s.
	 */
	private static final ResourceDemandingInternalActionFactory RESOURCE_DEMANDING_INTERNAL_ACTION_FACTORY =
		new ResourceDemandingInternalActionFactory();

	/**
	 * A {@link SeffBranchFactory} to easily create {@link SeffBranch}s.
	 */
	private static final SeffBranchFactory SEFF_BRANCH_FACTORY = new SeffBranchFactory();

	/**
	 * A {@link SeffLoopFactory} to easily create {@link SeffLoop}s.
	 */
	private static final SeffLoopFactory SEFF_LOOP_FACTORY = new SeffLoopFactory();

	/**
	 * A {@link ExternalCallParameterFactory} to easily create
	 * {@link ExternalCallParameter}s.
	 */
	private static final ExternalCallParameterFactory EXTERNAL_CALL_PARAMETER_FACTORY =
		new ExternalCallParameterFactory();

	//@formatter:off
	/**
	 * Test method for
	 * {@link AbstractionAndPrecisionFitnessFunction#gradeFor(ResourceDemandingInternalAction, EvaluableExpression,
	 * EvaluableExpressionFitnessFunctionBlackboardView)}.
	 *
	 * <p>Asserts that {@link NullPointerException}s are thrown if one of the arguments is null, infinity is returned
	 * if there are no results and an expression nearer to the result returns a smaller double than one farer away.
	 */
	//@formatter:on
	@Test
	public void gradeForResourceDemandingInternalActionEvaluableExpressionEvaluableExpressionFitnessFunctionBlackboardView() {
		final AbstractionAndPrecisionFitnessFunction function = new AbstractionAndPrecisionFitnessFunction();
		final ResourceDemandingInternalAction nullRdia = null;
		final EvaluableExpression expression = mock(EvaluableExpression.class);
		final EvaluableExpressionFitnessFunctionBlackboardView blackboardView =
			mock(EvaluableExpressionFitnessFunctionBlackboardView.class);
		ThrowingMethod method = () -> {
			function.gradeFor(nullRdia, expression, blackboardView);
		};
		assertThat("Resource Demanding Internal Action must not be null.", method,
			throwsException(NullPointerException.class));

		final ResourceDemandingInternalAction rdia = RESOURCE_DEMANDING_INTERNAL_ACTION_FACTORY.getOne();
		method = () -> {
			function.gradeFor(rdia, null, blackboardView);
		};
		assertThat("Evaluable Expression must not be null.", method, throwsException(NullPointerException.class));

		method = () -> {
			function.gradeFor(rdia, expression, null);
		};
		assertThat("Blackboard view must not be null.", method, throwsException(NullPointerException.class));

		Set<ResourceDemandMeasurementResult> results = new HashSet<>();
		results.add(new ResourceDemandMeasurementResult(2.0));
		final EvaluableExpression expression2 = mock(EvaluableExpression.class);
		given(blackboardView.getMeasurementResultsFor(rdia)).willReturn(results);
		given(expression.evaluate(any())).willReturn(2.0);
		given(expression2.evaluate(any())).willReturn(10.32);
		assertThat(function.gradeFor(rdia, expression, blackboardView),
			is(lessThan(function.gradeFor(rdia, expression2, blackboardView))));

		results = new HashSet<>();
		results.add(new ResourceDemandMeasurementResult(2.0));
		results.add(new ResourceDemandMeasurementResult(20.32));
		given(blackboardView.getMeasurementResultsFor(rdia)).willReturn(results);
		assertThat(function.gradeFor(rdia, expression2, blackboardView),
			is(lessThan(function.gradeFor(rdia, expression, blackboardView))));

		results = new HashSet<>();
		given(blackboardView.getMeasurementResultsFor(rdia)).willReturn(results);
		given(expression.evaluate(any())).willReturn(2.0);
		given(expression2.evaluate(any())).willReturn(10.32);
		assertThat(function.gradeFor(rdia, expression, blackboardView), is(Double.POSITIVE_INFINITY));
	}

	// @formatter:off
	/**
	 * Test method for
	 * {@link AbstractionAndPrecisionFitnessFunction#gradeFor(SeffBranch, EvaluableExpression,
	 * EvaluableExpressionFitnessFunctionBlackboardView)}.
	 *
	 * <p>Asserts that {@link NullPointerException}s are thrown if one of the arguments is null, infinity is returned
	 * if there are no results and an expression nearer to the result returns a smaller double than one farer away.
	 */
	// @formatter:on
	@Test
	public void gradeForSeffBranchEvaluableExpressionEvaluableExpressionFitnessFunctionBlackboardView() {
		final AbstractionAndPrecisionFitnessFunction function = new AbstractionAndPrecisionFitnessFunction();
		final SeffBranch nullBranch = null;
		final EvaluableExpression expression = mock(EvaluableExpression.class);
		final EvaluableExpressionFitnessFunctionBlackboardView blackboardView =
			mock(EvaluableExpressionFitnessFunctionBlackboardView.class);
		ThrowingMethod method = () -> {
			function.gradeFor(nullBranch, expression, blackboardView);
		};
		assertThat("Seff branch must not be null.", method, throwsException(NullPointerException.class));

		final SeffBranch branch = SEFF_BRANCH_FACTORY.getOne();
		method = () -> {
			function.gradeFor(branch, null, blackboardView);
		};
		assertThat("Evaluable Expression must not be null.", method, throwsException(NullPointerException.class));

		method = () -> {
			function.gradeFor(branch, expression, null);
		};
		assertThat("Blackboard view must not be null.", method, throwsException(NullPointerException.class));

		Set<BranchDecisionMeasurementResult> results = new HashSet<>();
		results.add(new BranchDecisionMeasurementResult(2));
		final EvaluableExpression expression2 = mock(EvaluableExpression.class);
		given(blackboardView.getMeasurementResultsFor(branch)).willReturn(results);
		given(expression.evaluate(any())).willReturn(2.0);
		given(expression2.evaluate(any())).willReturn(10.32);
		assertThat(function.gradeFor(branch, expression, blackboardView),
			is(lessThan(function.gradeFor(branch, expression2, blackboardView))));

		results = new HashSet<>();
		results.add(new BranchDecisionMeasurementResult(2));
		results.add(new BranchDecisionMeasurementResult(20));
		given(blackboardView.getMeasurementResultsFor(branch)).willReturn(results);
		assertThat(function.gradeFor(branch, expression2, blackboardView),
			is(lessThan(function.gradeFor(branch, expression, blackboardView))));

		results = new HashSet<>();
		given(blackboardView.getMeasurementResultsFor(branch)).willReturn(results);
		given(expression.evaluate(any())).willReturn(2.0);
		given(expression2.evaluate(any())).willReturn(10.32);
		assertThat(function.gradeFor(branch, expression, blackboardView), is(Double.POSITIVE_INFINITY));
	}

	// @formatter:off
	/**
	 * Test method for
	 * {@link AbstractionAndPrecisionFitnessFunction#gradeFor(SeffLoop, EvaluableExpression,
	 * EvaluableExpressionFitnessFunctionBlackboardView)}.
	 *
	 * <p>Asserts that {@link NullPointerException}s are thrown if one of the arguments is null, infinity is returned
	 * if there are no results and an expression nearer to the result returns a smaller double than one farer away.
	 */
	// @formatter:on
	@Test
	public void gradeForSeffLoopEvaluableExpressionEvaluableExpressionFitnessFunctionBlackboardView() {
		final AbstractionAndPrecisionFitnessFunction function = new AbstractionAndPrecisionFitnessFunction();
		final SeffLoop nullLoop = null;
		final EvaluableExpression expression = mock(EvaluableExpression.class);
		final EvaluableExpressionFitnessFunctionBlackboardView blackboardView =
			mock(EvaluableExpressionFitnessFunctionBlackboardView.class);
		ThrowingMethod method = () -> {
			function.gradeFor(nullLoop, expression, blackboardView);
		};
		assertThat("Seff loop must not be null.", method, throwsException(NullPointerException.class));

		final SeffLoop loop = SEFF_LOOP_FACTORY.getOne();
		method = () -> {
			function.gradeFor(loop, null, blackboardView);
		};
		assertThat("Evaluable Expression must not be null.", method, throwsException(NullPointerException.class));

		method = () -> {
			function.gradeFor(loop, expression, null);
		};
		assertThat("Blackboard view must not be null.", method, throwsException(NullPointerException.class));

		Set<LoopRepetitionCountMeasurementResult> results = new HashSet<>();
		results.add(new LoopRepetitionCountMeasurementResult(2));
		final EvaluableExpression expression2 = mock(EvaluableExpression.class);
		given(blackboardView.getMeasurementResultsFor(loop)).willReturn(results);
		given(expression.evaluate(any())).willReturn(2.0);
		given(expression2.evaluate(any())).willReturn(10.32);
		assertThat(function.gradeFor(loop, expression, blackboardView),
			is(lessThan(function.gradeFor(loop, expression2, blackboardView))));

		results = new HashSet<>();
		results.add(new LoopRepetitionCountMeasurementResult(2));
		results.add(new LoopRepetitionCountMeasurementResult(21));
		given(blackboardView.getMeasurementResultsFor(loop)).willReturn(results);
		assertThat(function.gradeFor(loop, expression2, blackboardView),
			is(lessThan(function.gradeFor(loop, expression, blackboardView))));

		results = new HashSet<>();
		given(blackboardView.getMeasurementResultsFor(loop)).willReturn(results);
		given(expression.evaluate(any())).willReturn(2.0);
		given(expression2.evaluate(any())).willReturn(10.32);
		assertThat(function.gradeFor(loop, expression, blackboardView), is(Double.POSITIVE_INFINITY));
	}

	// @formatter:off
	/**
	 * Test method for
	 * {@link AbstractionAndPrecisionFitnessFunction#gradeFor(ExternalCallParameter, EvaluableExpression,
	 * EvaluableExpressionFitnessFunctionBlackboardView)}.
	 *
	 * <p>Asserts that {@link NullPointerException}s are thrown if one of the arguments is null, infinity is returned
	 * if there are no results and an expression nearer to the result returns a smaller double than one farer away.
	 */
	// @formatter:on
	@Test
	public void gradeForExternalCallParameterEvaluableExpressionEvaluableExpressionFitnessFunctionBlackboardView() {
		final AbstractionAndPrecisionFitnessFunction function = new AbstractionAndPrecisionFitnessFunction();
		final ExternalCallParameter nullParameter = null;
		final EvaluableExpression expression = mock(EvaluableExpression.class);
		final EvaluableExpressionFitnessFunctionBlackboardView blackboardView =
			mock(EvaluableExpressionFitnessFunctionBlackboardView.class);
		ThrowingMethod method = () -> {
			function.gradeFor(nullParameter, expression, blackboardView);
		};
		assertThat("External Call Parameter must not be null.", method, throwsException(NullPointerException.class));

		final ExternalCallParameter parameter = EXTERNAL_CALL_PARAMETER_FACTORY.getOne();
		method = () -> {
			function.gradeFor(parameter, null, blackboardView);
		};
		assertThat("Evaluable Expression must not be null.", method, throwsException(NullPointerException.class));

		method = () -> {
			function.gradeFor(parameter, expression, null);
		};
		assertThat("Blackboard view must not be null.", method, throwsException(NullPointerException.class));

		Set<ParameterChangeMeasurementResult> results = new HashSet<>();
		ParameterChangeMeasurementResult parameterChangeResult = mock(ParameterChangeMeasurementResult.class);
		given(parameterChangeResult.getCount()).willReturn(2);
		results.add(parameterChangeResult);
		final EvaluableExpression expression2 = mock(EvaluableExpression.class);
		given(blackboardView.getMeasurementResultsFor(parameter)).willReturn(results);
		given(expression.evaluate(any())).willReturn(2.0);
		given(expression2.evaluate(any())).willReturn(10.32);
		assertThat(function.gradeFor(parameter, expression, blackboardView),
			is(lessThan(function.gradeFor(parameter, expression2, blackboardView))));

		results = new HashSet<>();
		parameterChangeResult = mock(ParameterChangeMeasurementResult.class);
		given(parameterChangeResult.getCount()).willReturn(2);
		results.add(parameterChangeResult);
		final ParameterChangeMeasurementResult parameterChangeResult2 = mock(ParameterChangeMeasurementResult.class);
		given(parameterChangeResult2.getCount()).willReturn(22);
		results.add(parameterChangeResult2);
		given(blackboardView.getMeasurementResultsFor(parameter)).willReturn(results);
		assertThat(function.gradeFor(parameter, expression2, blackboardView),
			is(lessThan(function.gradeFor(parameter, expression, blackboardView))));

		results = new HashSet<>();
		given(blackboardView.getMeasurementResultsFor(parameter)).willReturn(results);
		given(expression.evaluate(any())).willReturn(2.0);
		given(expression2.evaluate(any())).willReturn(10.32);
		assertThat(function.gradeFor(parameter, expression, blackboardView), is(Double.POSITIVE_INFINITY));
	}

}
