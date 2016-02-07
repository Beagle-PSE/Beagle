package de.uka.ipd.sdq.beagle.core;

import de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpression;
import de.uka.ipd.sdq.beagle.core.judge.EvaluableExpressionFitnessFunction;
import de.uka.ipd.sdq.beagle.core.measurement.BranchDecisionMeasurementResult;
import de.uka.ipd.sdq.beagle.core.measurement.LoopRepetitionCountMeasurementResult;
import de.uka.ipd.sdq.beagle.core.measurement.ParameterChangeMeasurementResult;
import de.uka.ipd.sdq.beagle.core.measurement.ResourceDemandMeasurementResult;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Central and only storage of all knowledge gained by Beagle. Implements, together with
 * {@link AnalysisController}, the Blackboard pattern from POSA I. The Blackboard’s
 * vocabularies are: {@link ResourceDemandingInternalAction}, {@link SeffBranch},
 * {@link SeffLoop}, {@link ResourceDemandMeasurementResult},
 * {@link BranchDecisionMeasurementResult} , {@link LoopRepetitionCountMeasurementResult}
 * and {@link EvaluableExpression}. It further allows classes to store custom data.
 *
 * <p>The Blackboard is typically not accessed directly by its using classes, but through
 * <em>blackboard views</em> (recognisable by having the {@code BlackboardView} suffix).
 * These are surrogates for the blackboard. They don’t modify its contents but only
 * restrict access to it.
 *
 * @author Christoph Michelbach
 * @author Joshua Gleitze
 * @author Roman Langrehr
 * @author Michael Vogt
 * @see AnalysisController
 */
public class Blackboard implements Serializable {

	/**
	 * Serialisation version UID, see {@link java.io.Serializable}.
	 */
	private static final long serialVersionUID = 6382577321150787599L;

	/**
	 * All measurable SEFF elements.
	 */
	private final Set<MeasurableSeffElement> allSeffElements = new HashSet<>();

	/**
	 * All resource demanding internal actions.
	 */
	private final Set<ResourceDemandingInternalAction> rdias;

	/**
	 * All SEFF branches.
	 */
	private final Set<SeffBranch> branches;

	/**
	 * All SEFF Loops.
	 */
	private final Set<SeffLoop> loops;

	/**
	 * All external call parameter.
	 */
	private final Set<ExternalCallParameter> externalCalls;

	/**
	 * All resource demanding internal actions which are to be measured.
	 */
	private Set<ResourceDemandingInternalAction> rdiasToBeMeasured = new HashSet<>();

	/**
	 * All SEFF branches which are to be measured.
	 */
	private Set<SeffBranch> branchesToBeMeasured = new HashSet<>();

	/**
	 * All SEFF loops which are to be count.
	 */
	private Set<SeffLoop> loopsToBeMeasured = new HashSet<>();

	/**
	 * All external call parameter which are to be measured.
	 */
	private Set<ExternalCallParameter> externalCallParameterToBeMeasured = new HashSet<>();

	/**
	 * All resource demanding internal results.
	 */
	private Set<ResourceDemandMeasurementResult> rdiasMeasurementResult = new HashSet<>();

	/**
	 * All SEFF branches results.
	 */
	private Set<BranchDecisionMeasurementResult> branchDecisionMeasurementResult = new HashSet<>();

	/**
	 * All SEFF loop count results.
	 */
	private Set<LoopRepetitionCountMeasurementResult> loopRepititionCountMeasurementResult = new HashSet<>();

	/**
	 * All parameter change results.
	 */
	private Set<ParameterChangeMeasurementResult> parameterChangeMeasurementResult = new HashSet<>();

	/**
	 * All evaluable expressions.
	 */
	private Map<MeasurableSeffElement, Set<EvaluableExpression>> proposedExpressions = new HashMap<>();

	/**
	 * Is the final expression.
	 */
	private Map<MeasurableSeffElement, EvaluableExpression> finalExpressions = new HashMap<>();

	/**
	 * Is the function to get a better evaluable expression result.
	 */
	private EvaluableExpressionFitnessFunction fitnessFunction;

	/**
	 * Private data of tools, written through {@link #writeFor(Class, Serializable)}.
	 */
	private final Map<Class<? extends BlackboardStorer<? extends Serializable>>, Object> privateWrittenData =
		new HashMap<>();

	/**
	 * Creates a new blackboard that can be used to analyse the given elements.
	 *
	 * @param rdias All resource demanding internal action to be known to analysers.
	 * @param branches All SEFF branches to be known to analysers.
	 * @param loops All SEFF loops to be known to analysers.
	 * @param externalCalls All external call parameter to be known to analysers.
	 * @param fitnessFunction The function to get better evaluable expression results.
	 */
	public Blackboard(final Set<ResourceDemandingInternalAction> rdias, final Set<SeffBranch> branches,
		final Set<SeffLoop> loops, final Set<ExternalCallParameter> externalCalls,
		final EvaluableExpressionFitnessFunction fitnessFunction) {
		Validate.noNullElements(rdias);
		Validate.noNullElements(branches);
		Validate.noNullElements(loops);
		Validate.noNullElements(externalCalls);
		Validate.notNull(fitnessFunction);
		this.rdias = rdias;
		this.branches = branches;
		this.loops = loops;
		this.externalCalls = externalCalls;
		this.fitnessFunction = fitnessFunction;

		this.allSeffElements.addAll(rdias);
		this.allSeffElements.addAll(branches);
		this.allSeffElements.addAll(loops);
		this.allSeffElements.addAll(externalCalls);

		for (final MeasurableSeffElement element : this.allSeffElements) {
			this.proposedExpressions.put(element, new HashSet<>());
		}
	}

	/**
	 * All {@linkplain ResourceDemandingInternalAction resource demanding internal
	 * actions} known to Beagle.
	 *
	 * @return all {@linkplain ResourceDemandingInternalAction resource demanding internal
	 *         actions} known to Beagle. Changes to the returned set will not modify the
	 *         blackboard content. Is never {@code null}.
	 */
	public Set<ResourceDemandingInternalAction> getAllRdias() {
		return this.rdias;
	}

	/**
	 * All {@linkplain SeffBranch SEFF branches} known to Beagle.
	 *
	 * @return all {@linkplain SeffBranch SEFF branches} known to Beagle. Changes to the
	 *         returned set will not modify the blackboard content. Is never {@code null}.
	 */
	public Set<SeffBranch> getAllSeffBranches() {
		return this.branches;
	}

	/**
	 * All {@linkplain SeffLoop SEFF loops} known to Beagle.
	 *
	 * @return all {@linkplain SeffLoop SEFF loops} known to Beagle. Changes to the
	 *         returned set will not modify the blackboard content. Is never {@code null}.
	 */
	public Set<SeffLoop> getAllSeffLoops() {
		return this.loops;
	}

	/**
	 * Returns all {@linkplain ExternalCallParameter external call parameters} known to
	 * Beagle.
	 *
	 * @return All {@linkplain ExternalCallParameter external call parameters} known to
	 *         Beagle. Is never {@code null}.
	 */
	public Set<ExternalCallParameter> getAllExternalCallParameters() {
		return this.externalCalls;
	}

	/**
	 * {@linkplain ResourceDemandingInternalAction RDIAs} that shall be measured for their
	 * resource demands.
	 *
	 * @return All {@linkplain ResourceDemandingInternalAction resource demanding internal
	 *         actions} to be measured. Changes to the returned set will not modify the
	 *         blackboard content. Is never {@code null}.
	 */
	public Set<ResourceDemandingInternalAction> getRdiasToBeMeasured() {
		return this.rdiasToBeMeasured;
	}

	/**
	 * {@linkplain SeffBranch SEFF branches} that shall be measured for their branch
	 * decisions.
	 *
	 * @return All {@linkplain SeffBranch SEFF branches} to be measured. Changes to the
	 *         returned set will not modify the blackboard content. Is never {@code null}.
	 */
	public Set<SeffBranch> getSeffBranchesToBeMeasured() {
		return this.branchesToBeMeasured;
	}

	/**
	 * {@linkplain SeffLoop SEFF loops} that shall be measured for their repetitions.
	 *
	 * @return All {@linkplain SeffLoop SEFF loops} to be measured. Changes to the
	 *         returned set will not modify the blackboard content. Is never {@code null}.
	 */
	public Set<SeffLoop> getSeffLoopsToBeMeasured() {
		return this.loopsToBeMeasured;
	}

	/**
	 * Returns all {@linkplain ExternalCallParameter external call parameters} which shall
	 * be measured.
	 *
	 * @return All {@linkplain ExternalCallParameter external call parameters} which shall
	 *         be measured. Is never {@code null}.
	 */
	public Set<ExternalCallParameter> getExternalCallParametersToBeMeasured() {
		return this.externalCallParameterToBeMeasured;
	}

	/**
	 * Reports that {@code rdias} shall be measured for its resource demands.
	 *
	 * @param toMeasureRdias Resource demanding internal actions that shall be measured.
	 *            Must not be {@code null} and must be known to this blackboard.
	 * @see #addToBeMeasuredRdias(Collection)
	 */
	public void addToBeMeasuredRdias(final ResourceDemandingInternalAction... toMeasureRdias) {
		Validate.noNullElements(toMeasureRdias);
		Validate.isTrue(this.rdias.containsAll(Arrays.asList(toMeasureRdias)),
			"toMeasureRdias may only contain elements that are already on the blackboard");
		this.rdiasToBeMeasured.addAll(Arrays.asList(toMeasureRdias));
	}

	/**
	 * Reports that {@code rdias} shall be measured for its resource demands.
	 *
	 * @param toMeasureRdias Resource demanding internal actions that shall be measured.
	 *            Must not be {@code null} and must be known to this blackboard.
	 * @see #addToBeMeasuredRdias(ResourceDemandingInternalAction...)
	 */
	public void addToBeMeasuredRdias(final Collection<ResourceDemandingInternalAction> toMeasureRdias) {
		Validate.noNullElements(toMeasureRdias);
		Validate.isTrue(this.rdias.containsAll(toMeasureRdias),
			"toMeasureRdias may only contain elements that are already on the blackboard");
		this.rdiasToBeMeasured.addAll(toMeasureRdias);
	}

	/**
	 * Reports that {@code branches} shall be measured for its branch decisions.
	 *
	 * @param toMeasureBranches SEFF branches that shall be measured. Must not be
	 *            {@code null} and must be known to this blackboard.
	 * @see #addToBeMeasuredSeffBranches(Collection)
	 */
	public void addToBeMeasuredSeffBranches(final SeffBranch... toMeasureBranches) {
		Validate.noNullElements(toMeasureBranches);
		Validate.isTrue(this.branches.containsAll(Arrays.asList(toMeasureBranches)),
			"toMeasureBranches may only contain elements that are already on the blackboard");
		this.branchesToBeMeasured.addAll(Arrays.asList(toMeasureBranches));
	}

	/**
	 * Reports that {@code branches} shall be measured for its branch decisions.
	 *
	 * @param toMeasureBranches SEFF branches that shall be measured. Must not be
	 *            {@code null} and must be known to this blackboard.
	 * @see #addToBeMeasuredSeffBranches(SeffBranch...)
	 */
	public void addToBeMeasuredSeffBranches(final Collection<SeffBranch> toMeasureBranches) {
		Validate.noNullElements(toMeasureBranches);
		Validate.isTrue(this.branches.containsAll(toMeasureBranches),
			"toMeasureBranches may only contain elements that are already on the blackboard");
		this.branchesToBeMeasured.addAll(toMeasureBranches);
	}

	/**
	 * Reports that {@code loops} shall be measured for its repetitions.
	 *
	 * @param toMeasureLoops SEFF Loops that shall be measured. Must not be {@code null}
	 *            and must be known to this blackboard.
	 * @see #addToBeMeasuredSeffLoops(Collection)
	 */
	public void addToBeMeasuredSeffLoops(final SeffLoop... toMeasureLoops) {
		Validate.noNullElements(toMeasureLoops);
		Validate.isTrue(this.loops.containsAll(Arrays.asList(toMeasureLoops)),
			"toMeasureLoops may only contain elements that are already on the blackboard");
		this.loopsToBeMeasured.addAll(Arrays.asList(toMeasureLoops));
	}

	/**
	 * Reports that {@code loops} shall be measured for its repetitions.
	 *
	 * @param toMeasureLoops SEFF Loops that shall be measured. Must not be {@code null}
	 *            and must be known to this blackboard.
	 * @see #addToBeMeasuredSeffLoops(SeffLoop...)
	 */
	public void addToBeMeasuredSeffLoops(final Collection<SeffLoop> toMeasureLoops) {
		Validate.noNullElements(toMeasureLoops);
		Validate.isTrue(this.loops.containsAll(toMeasureLoops),
			"toMeasureLoops may only contain elements that are already on the blackboard");
		this.loopsToBeMeasured.addAll(toMeasureLoops);
	}

	/**
	 * Reports that {@code parameters} shall be measured.
	 *
	 * @param parameters external call parameters that shall be measured. Must not be
	 *            {@code null} and must be known to this blackboard.
	 * @see #addToBeMeasuredExternalCallParameters(Collection)
	 */
	public void addToBeMeasuredExternalCallParameters(final ExternalCallParameter... parameters) {
		Validate.noNullElements(parameters);
		Validate.isTrue(this.externalCalls.containsAll(Arrays.asList(parameters)),
			"parameters may only contain elements that are already on the blackboard");
		this.externalCallParameterToBeMeasured.addAll(Arrays.asList(parameters));
	}

	/**
	 * Reports that {@code parameters} shall be measured.
	 *
	 * @param parameters external call parameters that shall be measured. Must not be
	 *            {@code null} and must be known to this blackboard.
	 * @see #addToBeMeasuredExternalCallParameters(ExternalCallParameter...)
	 */
	public void addToBeMeasuredExternalCallParameters(final Collection<ExternalCallParameter> parameters) {
		Validate.noNullElements(parameters);
		Validate.isTrue(this.externalCalls.containsAll(parameters),
			"parameters may only contain elements that are already on the blackboard");
		this.externalCallParameterToBeMeasured.addAll(parameters);
	}

	/**
	 * Gets all results yet measured for the resource demands of {@code rdia}.
	 *
	 * @param rdia An resource demanding internal action to get the measurement results
	 *            of. Must not be {@code null}.
	 * @return All measurement results reported for {@code rdia}. Changes to the returned
	 *         set will not modify the blackboard content. Is never {@code null}.
	 */
	public Set<ResourceDemandMeasurementResult> getMeasurementResultsFor(final ResourceDemandingInternalAction rdia) {
		Validate.notNull(rdia);
		Validate.isTrue(this.rdias.contains(rdia));
		return this.rdiasMeasurementResult;
	}

	/**
	 * Gets all results yet measured for branch decisions of {@code branch}.
	 *
	 * @param branch A SEFF Branch to get the measurement results of. Must not be
	 *            {@code null}.
	 * @return All measurement results reported for {@code branch}. Changes to the
	 *         returned set will not modify the blackboard content. Is never {@code null}.
	 */
	public Set<BranchDecisionMeasurementResult> getMeasurementResultsFor(final SeffBranch branch) {
		Validate.notNull(branch);
		Validate.isTrue(this.branches.contains(branch));
		return this.branchDecisionMeasurementResult;
	}

	/**
	 * Gets all results yet measured for the loop repetitions of {@code loop}.
	 *
	 * @param loop A SEFF Loop to get the measurement results of. Must not be {@code null}
	 *            .
	 * @return All measurement results reported for {@code loop}. Changes to the returned
	 *         set will not modify the blackboard content. Is never {@code null}.
	 */
	public Set<LoopRepetitionCountMeasurementResult> getMeasurementResultsFor(final SeffLoop loop) {
		Validate.notNull(loop);
		Validate.isTrue(this.loops.contains(loop));
		return this.loopRepititionCountMeasurementResult;
	}

	/**
	 * Gets all results yet measured for the external parameter
	 * {@code externalCallParameter}.
	 *
	 * @param externalCallParameter An external parameter to get the measurement results
	 *            of. Must not be {@code null}.
	 * @return All measurement results reported for {@code externalCallParameter}. Changes
	 *         to the returned set will not modify the blackboard content. Is never
	 *         {@code null}.
	 */
	public Set<ParameterChangeMeasurementResult> getMeasurementResultsFor(
		final ExternalCallParameter externalCallParameter) {
		Validate.notNull(externalCallParameter);
		Validate.isTrue(this.externalCalls.contains(externalCallParameter));
		return this.parameterChangeMeasurementResult;
	}

	/**
	 * Adds a measurement result for the provided {@code rdia}.
	 *
	 * @param rdia A resource demanding internal action that was measured. Must not be
	 *            {@code null} .
	 * @param results The result of that measurement. Must not be {@code null}.
	 */
	public void addMeasurementResultFor(final ResourceDemandingInternalAction rdia,
		final ResourceDemandMeasurementResult results) {
		Validate.notNull(rdia);
		Validate.notNull(results);
		Validate.isTrue(this.rdias.contains(rdia), "rdia must already be on the blackboard");
		this.rdiasMeasurementResult.add(results);
	}

	/**
	 * Adds a measurement result for the provided {@code branch}.
	 *
	 * @param branch A SEFF Branch which was measured. Must not be {@code null}.
	 * @param results The result of that measurement. Must not be {@code null}.
	 */
	public void addMeasurementResultFor(final SeffBranch branch, final BranchDecisionMeasurementResult results) {
		Validate.notNull(branch);
		Validate.notNull(results);
		Validate.isTrue(this.branches.contains(branch), "branch must already be on the blackboard");
		this.branchDecisionMeasurementResult.add(results);
	}

	/**
	 * Adds a measurement result for the provided {@code loop}.
	 *
	 * @param loop A SEFF Loop which was measured. Must not be {@code null}.
	 * @param results The result of that measurement. Must not be {@code null}.
	 */
	public void addMeasurementResultFor(final SeffLoop loop, final LoopRepetitionCountMeasurementResult results) {
		Validate.notNull(loop);
		Validate.notNull(results);
		Validate.isTrue(this.loops.contains(loop), "loop must already be on the blackboard");
		this.loopRepititionCountMeasurementResult.add(results);
	}

	/**
	 * Adds a measurement result for the provided {@code loop}.
	 *
	 * @param parameter An external call parameter which was measured. Must not be
	 *            {@code null}.
	 * @param results The result of that measurement. Must not be {@code null}.
	 */
	public void addMeasurementResultFor(final ExternalCallParameter parameter,
		final ParameterChangeMeasurementResult results) {
		Validate.notNull(parameter);
		Validate.notNull(results);
		Validate.isTrue(this.externalCalls.contains(parameter), "parameter must already be on the blackboard");
		this.parameterChangeMeasurementResult.add(results);
	}

	/**
	 * Returns a set of all {@linkplain EvaluableExpression evaluable expressions}
	 * proposed for {@code element}.
	 *
	 * @param element A SEFF element. Must not be {@code null}.
	 * @return A set of all {@linkplain EvaluableExpression evaluable expressions}
	 *         proposed for {@code element}.
	 */
	public Set<EvaluableExpression> getProposedExpressionFor(final MeasurableSeffElement element) {
		Validate.notNull(element);
		Validate.isTrue(this.allSeffElements.contains(element), "element must already be on the blackboard");
		return this.proposedExpressions.get(element);
	}

	/**
	 * Adds {@code expression} as a proposal.
	 *
	 * @param element A SEFF element. Must not be {@code null}.
	 * @param expression An evaluable expression proposed to describe {@code element}’s
	 *            measurement results. Must not be {@code null}.
	 */
	public void addProposedExpressionFor(final MeasurableSeffElement element, final EvaluableExpression expression) {
		Validate.notNull(element);
		Validate.notNull(expression);
		Validate.isTrue(this.allSeffElements.contains(element), "element must already be on the blackboard");
		this.proposedExpressions.get(element).add(expression);
	}

	/**
	 * Returns the final expression set for {@code element}. The return value of this
	 * method may change if
	 * {@link #setFinalExpressionFor(MeasurableSeffElement, EvaluableExpression)} is
	 * called with the same {@code element} between calls to
	 * {@code addProposedExpressionFor} with this element as parameter.
	 *
	 * @param element A SEFF element. Must not be {@code null}.
	 * @return The expression momentarily marked to be the final for {@code element}.
	 *         {@code null} if no expression has been marked yet.
	 */
	public EvaluableExpression getFinalExpressionFor(final MeasurableSeffElement element) {
		Validate.notNull(element);
		Validate.isTrue(this.allSeffElements.contains(element), "element must already be on the blackboard");
		return this.finalExpressions.get(element);
	}

	/**
	 * Sets {@code expression} as the final description of {@code element}’s measurement
	 * results. Consecutive calls to this method with the same {@code element} will
	 * override previous settings.
	 *
	 * @param element A SEFF element Must not be {@code null}.
	 * @param expression An evaluable expression describing {@code element}’s measurement
	 *            results. May be {@code null} to describe that no suitable expression was
	 *            found.
	 */
	public void setFinalExpressionFor(final MeasurableSeffElement element, final EvaluableExpression expression) {
		Validate.notNull(element);
		Validate.isTrue(this.allSeffElements.contains(element), "element must already be on the blackboard");
		this.finalExpressions.put(element, expression);
	}

	/**
	 * Returns an object which holds and is responsible allows access to the fitness
	 * function grading {@linkplain EvaluableExpression evaluable expressions} regarding
	 * their fitness.
	 *
	 * @return An object which holds and is responsible allows access to the fitness
	 *         function grading {@linkplain EvaluableExpression evaluable expressions}
	 *         regarding their fitness.
	 */
	public EvaluableExpressionFitnessFunction getFitnessFunction() {
		return this.fitnessFunction;
	}

	/**
	 * Writes data for {@code writer} to the blackboard. This method serves as a type safe
	 * mean for tools to store data that is not part of their results. Values stored here
	 * will never contribute to Beagle’s results. Any class calling this method should
	 * always pass its own {@linkplain Class} instance as {@code writer}. {@code writer}
	 * has to implement {@link BlackboardStorer} and thereby declare the type of values
	 * they write. Calling this method will override any data potentially stored
	 * previously for the given {@code writer}.
	 *
	 * @param writer The class the data should be written for. Must not be {@code null}.
	 * @param written The data to write.
	 * @param <WRITTEN_TYPE> {@code written}’s type.
	 * @see #readFor(Class)
	 */
	public <WRITTEN_TYPE extends Serializable> void writeFor(
		final Class<? extends BlackboardStorer<WRITTEN_TYPE>> writer, final WRITTEN_TYPE written) {
		Validate.notNull(writer);
		this.privateWrittenData.put(writer, written);
	}

	/**
	 * Reads data previously written for {@code writer} through
	 * {@link #writeFor(Class, Serializable)}.
	 *
	 * @param writer The class the desired data was written for. Must not be {@code null}.
	 * @param <WRITTEN_TYPE> The type of the data to be read.
	 * @return The data written in the last call to {@linkplain #writeFor} for
	 *         {@code writer}. {@code null} if no data has been written for {@code writer}
	 *         yet.
	 * @see #writeFor(Class, Serializable)
	 */
	@SuppressWarnings("unchecked")
	public <WRITTEN_TYPE extends Serializable> WRITTEN_TYPE readFor(
		final Class<? extends BlackboardStorer<WRITTEN_TYPE>> writer) {
		Validate.notNull(writer);
		/*
		 * This performs a cast based on generics. While the cast can not be checked by
		 * the JVM at runtime, type safety is assured by the signature of {@link
		 * #writeFor}, which is the only method writing data to {@link
		 * #privateWrittenData}.
		 */
		return (WRITTEN_TYPE) this.privateWrittenData.get(writer);
	}

	@Override
	public int hashCode() {
		// you pick a hard-coded, randomly chosen, non-zero, odd number
		// ideally different for each class
		return new HashCodeBuilder(13, 31).append(this.rdias).append(this.branches).append(this.loops)
			.append(this.externalCalls).toHashCode();
	}
}
