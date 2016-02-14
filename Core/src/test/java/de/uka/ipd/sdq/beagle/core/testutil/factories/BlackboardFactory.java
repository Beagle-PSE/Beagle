package de.uka.ipd.sdq.beagle.core.testutil.factories;

import de.uka.ipd.sdq.beagle.core.Blackboard;
import de.uka.ipd.sdq.beagle.core.ExternalCallParameter;
import de.uka.ipd.sdq.beagle.core.MeasurableSeffElement;
import de.uka.ipd.sdq.beagle.core.ResourceDemandingInternalAction;
import de.uka.ipd.sdq.beagle.core.SeffBranch;
import de.uka.ipd.sdq.beagle.core.SeffLoop;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpression;
import de.uka.ipd.sdq.beagle.core.judge.EvaluableExpressionFitnessFunction;
import de.uka.ipd.sdq.beagle.core.measurement.BranchDecisionMeasurementResult;
import de.uka.ipd.sdq.beagle.core.measurement.LoopRepetitionCountMeasurementResult;
import de.uka.ipd.sdq.beagle.core.measurement.ParameterChangeMeasurementResult;
import de.uka.ipd.sdq.beagle.core.measurement.ResourceDemandMeasurementResult;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
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

	/**
	 * A {@link EvaluableExpressionFitnessFunction} factory to easily obtain new instances
	 * from.
	 */
	private static final EvaluableExpressionFitnessFunctionFactory FITNESS_FUNCTION_FACTORY =
		new EvaluableExpressionFitnessFunctionFactory();

	/**
	 * A {@link EvaluableExpression} factory to easily obtain new instances from.
	 */
	private static final EvaluableExpressionFactory EVALUABLE_EXPRESSION_FACTORY = new EvaluableExpressionFactory();

	/**
	 * A Measurement result factory to easily obtain new instances from.
	 */
	private static final MeasurementResultFactory MEAUSUREMENT_RESULT_FACTORY = new MeasurementResultFactory();

	/**
	 * A project information factory to easily obtain new instances from.
	 */
	private static final ProjectInformationFactory PROJECT_INFORMATION_FACTORY = new ProjectInformationFactory();

	/**
	 * Creates a new blackboard with nothing written on it.
	 *
	 * @return A new blackboard instance, without any data on it.
	 */
	public Blackboard getEmpty() {
		return new Blackboard(new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>(),
			FITNESS_FUNCTION_FACTORY.getOne(), PROJECT_INFORMATION_FACTORY.getOne());
	}

	/**
	 * Creates a new blackboard, filled with all MeasurableSeffElements.
	 *
	 * @return A new blackboard instance with data.
	 */
	public Blackboard getInitialBlackboard() {
		final Set<ResourceDemandingInternalAction> rdiaSet = RDIA_FACTORY.getAllAsSet();
		final Set<SeffBranch> seffBranchSet = SEFF_BRANCH_FACTORY.getAllAsSet();
		final Set<SeffLoop> seffLoopSet = SEFF_LOOP_FACTORY.getAllAsSet();
		final Set<ExternalCallParameter> externalCallParameterSet = EXTERNAL_CALL_PARAMETER_FACTORY.getAllAsSet();

		final Blackboard blackboard = new Blackboard(rdiaSet, seffBranchSet, seffLoopSet, externalCallParameterSet,
			FITNESS_FUNCTION_FACTORY.getOne());
		return blackboard;
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

		final Blackboard blackboard = new Blackboard(rdiaSet, seffBranchSet, seffLoopSet, externalCallParameterSet,
			FITNESS_FUNCTION_FACTORY.getOne(), PROJECT_INFORMATION_FACTORY.getOne());
		blackboard.addToBeMeasuredExternalCallParameters(externalCallParameterSet);
		blackboard.addToBeMeasuredRdias(rdiaSet);
		blackboard.addToBeMeasuredSeffBranches(seffBranchSet);
		blackboard.addToBeMeasuredSeffLoops(seffLoopSet);

		return blackboard;
	}

	/**
	 * Creates a new blackboard, filled with all possible data.
	 *
	 * @return A new blackboard instance with data.
	 */
	public Blackboard getFull() {
		final Blackboard blackboard = this.getWithToBeMeasuredContent();

		Iterator<ResourceDemandingInternalAction> blackboardRdias = blackboard.getAllRdias().iterator();
		for (final ResourceDemandMeasurementResult meausurementResult : MEAUSUREMENT_RESULT_FACTORY.getRdiaResults()) {
			blackboard.addMeasurementResultFor(blackboardRdias.next(), meausurementResult);
			if (!blackboardRdias.hasNext()) {
				blackboardRdias = blackboard.getAllRdias().iterator();
			}
		}

		Iterator<SeffLoop> blackboardLoops = blackboard.getAllSeffLoops().iterator();
		for (final LoopRepetitionCountMeasurementResult meausurementResult : MEAUSUREMENT_RESULT_FACTORY
			.getLoopResults()) {
			blackboard.addMeasurementResultFor(blackboardLoops.next(), meausurementResult);
			if (!blackboardLoops.hasNext()) {
				blackboardLoops = blackboard.getAllSeffLoops().iterator();
			}
		}

		Iterator<SeffBranch> blackboardBranches = blackboard.getAllSeffBranches().iterator();
		for (final BranchDecisionMeasurementResult meausurementResult : MEAUSUREMENT_RESULT_FACTORY
			.getBranchResults()) {
			blackboard.addMeasurementResultFor(blackboardBranches.next(), meausurementResult);
			if (!blackboardBranches.hasNext()) {
				blackboardBranches = blackboard.getAllSeffBranches().iterator();
			}
		}

		Iterator<ExternalCallParameter> blackboardParameters = blackboard.getAllExternalCallParameters().iterator();
		for (final ParameterChangeMeasurementResult meausurementResult : MEAUSUREMENT_RESULT_FACTORY
			.getParameterResults()) {
			blackboard.addMeasurementResultFor(blackboardParameters.next(), meausurementResult);
			if (!blackboardParameters.hasNext()) {
				blackboardParameters = blackboard.getAllExternalCallParameters().iterator();
			}
		}

		boolean first = true;
		for (final EvaluableExpression expression : EVALUABLE_EXPRESSION_FACTORY.getAll()) {
			for (final MeasurableSeffElement seffElement : this.getAllSeffElements(blackboard)) {
				blackboard.addProposedExpressionFor(seffElement, expression);
			}
			if (first) {
				for (final MeasurableSeffElement seffElement : this.getAllSeffElements(blackboard)) {
					blackboard.setFinalExpressionFor(seffElement, expression);
				}
				first = false;
			}
		}
		return blackboard;
	}

	/**
	 * Joins all Seff Elements stored on a blackboard.
	 *
	 * @param blackboard A blackboard to get seff elements from
	 * @return All seff elements stored on {@code blackboard}.
	 */
	public Set<MeasurableSeffElement> getAllSeffElements(final Blackboard blackboard) {
		final Set<MeasurableSeffElement> result = new HashSet<>();
		result.addAll(blackboard.getAllExternalCallParameters());
		result.addAll(blackboard.getAllRdias());
		result.addAll(blackboard.getAllSeffBranches());
		result.addAll(blackboard.getAllSeffLoops());
		return result;
	}

	/**
	 * Creates a new blackboard, filled only with few measurable seff elements.
	 *
	 * @return A new blackboard instance with few data.
	 */
	public Blackboard getWithFewElements() {
		return new Blackboard(some(RDIA_FACTORY.getAll(), 2), some(SEFF_BRANCH_FACTORY.getAll(), 2),
			some(SEFF_LOOP_FACTORY.getAll(), 2), some(EXTERNAL_CALL_PARAMETER_FACTORY.getAll(), 2),
			FITNESS_FUNCTION_FACTORY.getOne(), PROJECT_INFORMATION_FACTORY.getOne());
	}

	/**
	 * Creates a copy of the provided Blackboard that has the provided fitness function
	 * set.
	 *
	 * @param sourceBlackboard The Blackboard to copy.
	 * @param fitnessFunction The fitness function to use on the copy.
	 * @return A Blackboard with the same content, except that {@code fitnessFunction} is
	 *         set on it.
	 */
	public Blackboard setFitnessFunction(final Blackboard sourceBlackboard,
		final EvaluableExpressionFitnessFunction fitnessFunction) {
		final Blackboard copy = new Blackboard(sourceBlackboard.getAllRdias(), sourceBlackboard.getAllSeffBranches(),
			sourceBlackboard.getAllSeffLoops(), sourceBlackboard.getAllExternalCallParameters(), fitnessFunction,
			sourceBlackboard.getProjectInformation());
		this.copyAll(sourceBlackboard, copy);
		return copy;
	}

	/**
	 * Extracts {@code count} elements from an array.
	 *
	 * @param inputArray The array to get the elements from. Must contain at least
	 *            {@code count} elements.
	 * @param count How many elements the returned set should contain.
	 * @param <T> The elements’ type.
	 * @return A subset of {@code inputArray} containing {@code count} elements.
	 */
	private static <T> Set<T> some(final T[] inputArray, final int count) {
		return some(Arrays.asList(inputArray), count);
	}

	/**
	 * Extracts {@code count} elements from a set.
	 *
	 * @param inputSet The array to get the elements from. Must contain at least
	 *            {@code count} elements.
	 * @param count How many elements the returned set should contain.
	 * @param <T> The elements’ type.
	 * @return A subset of {@code inputSet} containing {@code count} elements.
	 */
	private static <T> Set<T> some(final Iterable<T> inputSet, final int count) {
		final Set<T> result = new HashSet<>();
		final Iterator<T> inputIterator = inputSet.iterator();
		for (int c = 0; c < count; c++) {
			result.add(inputIterator.next());
		}
		return result;
	}

	/**
	 * Copies all Blackboard content that can be acessed through public methods from
	 * {@code sourcBlackboard} to {@code targetBlackboard}. Note that this does
	 * <em>not</em> copy data written through
	 * {@link Blackboard#writeFor(Class, java.io.Serializable)}. Both blackboards must
	 * have the same seff elements on them.
	 *
	 * @param sourceBlackboard The blackboard to copy from. Must not be {@code null}.
	 * @param targetBlackboard The blackboard to copy to. Must not be {@code null}.
	 */
	public void copyAll(final Blackboard sourceBlackboard, final Blackboard targetBlackboard) {
		final Set<ResourceDemandingInternalAction> rdias = sourceBlackboard.getAllRdias();
		final Set<SeffBranch> branches = sourceBlackboard.getAllSeffBranches();
		final Set<SeffLoop> loops = sourceBlackboard.getAllSeffLoops();
		final Set<ExternalCallParameter> parameters = sourceBlackboard.getAllExternalCallParameters();
		final Set<MeasurableSeffElement> allSeffElements = new HashSet<>();
		allSeffElements.addAll(rdias);
		allSeffElements.addAll(branches);
		allSeffElements.addAll(loops);
		allSeffElements.addAll(parameters);

		for (final ResourceDemandingInternalAction rdia : rdias) {
			for (final ResourceDemandMeasurementResult result : sourceBlackboard.getMeasurementResultsFor(rdia)) {
				targetBlackboard.addMeasurementResultFor(rdia, result);
			}
		}
		for (final ExternalCallParameter parameter : parameters) {
			for (final ParameterChangeMeasurementResult result : sourceBlackboard.getMeasurementResultsFor(parameter)) {
				targetBlackboard.addMeasurementResultFor(parameter, result);
			}
		}
		for (final SeffBranch branch : branches) {
			for (final BranchDecisionMeasurementResult result : sourceBlackboard.getMeasurementResultsFor(branch)) {
				targetBlackboard.addMeasurementResultFor(branch, result);
			}
		}
		for (final SeffLoop loop : loops) {
			for (final LoopRepetitionCountMeasurementResult result : sourceBlackboard.getMeasurementResultsFor(loop)) {
				targetBlackboard.addMeasurementResultFor(loop, result);
			}
		}

		targetBlackboard
			.addToBeMeasuredExternalCallParameters(sourceBlackboard.getExternalCallParametersToBeMeasured());
		targetBlackboard.addToBeMeasuredRdias(sourceBlackboard.getRdiasToBeMeasured());
		targetBlackboard.addToBeMeasuredSeffBranches(sourceBlackboard.getSeffBranchesToBeMeasured());
		targetBlackboard.addToBeMeasuredSeffLoops(sourceBlackboard.getSeffLoopsToBeMeasured());

		for (final MeasurableSeffElement seffElement : allSeffElements) {
			for (final EvaluableExpression proposedExpression : sourceBlackboard
				.getProposedExpressionFor(seffElement)) {
				targetBlackboard.addProposedExpressionFor(seffElement, proposedExpression);
			}
			targetBlackboard.setFinalExpressionFor(seffElement, sourceBlackboard.getFinalExpressionFor(seffElement));
		}
	}
}
