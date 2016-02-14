package de.uka.ipd.sdq.beagle.core.measurement;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.refEq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.uka.ipd.sdq.beagle.core.AnalysisControllerTest;
import de.uka.ipd.sdq.beagle.core.Blackboard;
import de.uka.ipd.sdq.beagle.core.CodeSection;
import de.uka.ipd.sdq.beagle.core.ExternalCallParameter;
import de.uka.ipd.sdq.beagle.core.ResourceDemandingInternalAction;
import de.uka.ipd.sdq.beagle.core.SeffBranch;
import de.uka.ipd.sdq.beagle.core.SeffLoop;
import de.uka.ipd.sdq.beagle.core.judge.EvaluableExpressionFitnessFunction;
import de.uka.ipd.sdq.beagle.core.measurement.order.CodeSectionEnteredEvent;
import de.uka.ipd.sdq.beagle.core.measurement.order.CodeSectionLeftEvent;
import de.uka.ipd.sdq.beagle.core.measurement.order.MeasurementEvent;
import de.uka.ipd.sdq.beagle.core.measurement.order.MeasurementOrder;
import de.uka.ipd.sdq.beagle.core.measurement.order.ParameterCharacteriser;
import de.uka.ipd.sdq.beagle.core.measurement.order.ResourceDemandCapturedEvent;
import de.uka.ipd.sdq.beagle.core.testutil.factories.EvaluableExpressionFitnessFunctionFactory;
import de.uka.ipd.sdq.beagle.core.testutil.factories.ExternalCallParameterFactory;
import de.uka.ipd.sdq.beagle.core.testutil.factories.ResourceDemandingInternalActionFactory;
import de.uka.ipd.sdq.beagle.core.testutil.factories.SeffBranchFactory;
import de.uka.ipd.sdq.beagle.core.testutil.factories.SeffLoopFactory;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * This class tests, whether the returned {@link MeasurementOrder} of
 * {@link MeasurementController#measure(MeasurementControllerBlackboardView)} for the
 * {@linkplain MeasurementTool MeasurementTools} is valid. All other tests for
 * {@link MeasurementController} are done with {@link AnalysisControllerTest}.
 *
 * @author Roman Langrehr
 */
public class MeasurementControllerTest {

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

	/**
	 * A {@link EvaluableExpressionFitnessFunction} factory to easily obtain new instances
	 * from.
	 */
	private static final EvaluableExpressionFitnessFunctionFactory FITNESS_FUNCTION_FACTORY =
		new EvaluableExpressionFitnessFunctionFactory();

	/**
	 * Asserts that the returned {@link MeasurementOrder} of
	 * {@link MeasurementController#measure(MeasurementControllerBlackboardView)} for the
	 * {@linkplain MeasurementTool MeasurementTools} is valid.
	 *
	 */
	@Test
	public void contribute() {
		final MeasurementTool tool = mock(MeasurementTool.class);

		final Set<ResourceDemandingInternalAction> rdiaSet = RDIA_FACTORY.getAllAsSet();
		final Set<SeffBranch> seffBranchSet = SEFF_BRANCH_FACTORY.getAllAsSet();
		final Set<SeffLoop> seffLoopSet = SEFF_LOOP_FACTORY.getAllAsSet();
		final Set<ExternalCallParameter> externalCallParameterSet = EXTERNAL_CALL_PARAMETER_FACTORY.getAllAsSet();
		final List<SeffLoop> loops = new ArrayList<>(seffLoopSet);
		when(tool.measure(anyObject())).thenAnswer(new Answer<Object>() {

			@Override
			public Object answer(final InvocationOnMock invocation) throws Throwable {

				final List<MeasurementEvent> measurementEvents = new ArrayList<>();
				measurementEvents.add(new CodeSectionEnteredEvent(loops.get(0).getLoopBody()));
				measurementEvents.add(new CodeSectionEnteredEvent(loops.get(1).getLoopBody()));
				measurementEvents.add(new CodeSectionLeftEvent(loops.get(1).getLoopBody()));
				measurementEvents.add(new ResourceDemandCapturedEvent(rdiaSet.iterator().next().getAction(),
					rdiaSet.iterator().next().getResourceType(), 100d));
				measurementEvents.add(new CodeSectionEnteredEvent(loops.get(1).getLoopBody()));
				measurementEvents.add(new CodeSectionLeftEvent(loops.get(1).getLoopBody()));
				measurementEvents.add(new CodeSectionLeftEvent(loops.get(0).getLoopBody()));
				return measurementEvents;
			}
		});

		final Blackboard blackboard = new Blackboard(rdiaSet, seffBranchSet, seffLoopSet, externalCallParameterSet,
			FITNESS_FUNCTION_FACTORY.getOne());
		blackboard.addToBeMeasuredSeffBranches(seffBranchSet.iterator().next());
		blackboard.addToBeMeasuredRdias(blackboard.getAllRdias().iterator().next());
		blackboard.addToBeMeasuredSeffBranches(seffBranchSet.iterator().next());
		final Set<CodeSection> parameterValueSections = new HashSet<>();
		final Set<CodeSection> resourceDemandSections = new HashSet<>();
		final Set<CodeSection> executionSections = new HashSet<>();
		blackboard.addToBeMeasuredRdias(rdiaSet.iterator().next());
		resourceDemandSections.add(rdiaSet.iterator().next().getAction());
		blackboard.addToBeMeasuredSeffBranches(seffBranchSet.iterator().next());
		executionSections.addAll(seffBranchSet.iterator().next().getBranches());
		blackboard.addToBeMeasuredSeffLoops(seffLoopSet.iterator().next());
		executionSections.add(seffLoopSet.iterator().next().getLoopBody());
		blackboard.addToBeMeasuredExternalCallParameters(externalCallParameterSet.iterator().next());
		parameterValueSections.add(externalCallParameterSet.iterator().next().getCallCodeSection());
		final Set<MeasurementTool> oneMeasurementTool = new HashSet<>();
		oneMeasurementTool.add(tool);

		final MeasurementController measurementController = new MeasurementController(oneMeasurementTool);
		assertTrue("There is something to measure, the measurementController must return true in canMeasure",
			measurementController.canMeasure(new ReadOnlyMeasurementControllerBlackboardView(blackboard)));
		measurementController.measure(new MeasurementControllerBlackboardView(blackboard));
		final MeasurementOrder expectedMeasurementOrder = new MeasurementOrder(parameterValueSections,
			resourceDemandSections, executionSections, new HashSet<>(), new ParameterCharacteriser());
		verify(tool).measure(refEq(expectedMeasurementOrder, "launchConfigurations", "parameterCharacteriser"));

		// Check blackboard
		final Set<LoopRepetitionCountMeasurementResult> results = blackboard.getMeasurementResultsFor(loops.get(0));
		final List<Integer> resultValues =
			results.stream().map(new Function<LoopRepetitionCountMeasurementResult, Integer>() {

				@Override
				public Integer apply(final LoopRepetitionCountMeasurementResult result) {
					return result.getCount();
				}
			}).collect(Collectors.toList());
		assertThat(resultValues, containsInAnyOrder(1));
		final Set<ResourceDemandMeasurementResult> rdiaResults =
			blackboard.getMeasurementResultsFor(rdiaSet.iterator().next());
		final List<Double> rdiaResultValues =
			rdiaResults.stream().map(new Function<ResourceDemandMeasurementResult, Double>() {

				@Override
				public Double apply(final ResourceDemandMeasurementResult result) {
					return result.getValue();
				}
			}).collect(Collectors.toList());
		assertThat(rdiaResultValues, containsInAnyOrder(100d));
	}
}
