package de.uka.ipd.sdq.beagle.core;

import de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpression;
import de.uka.ipd.sdq.beagle.core.judge.EvaluableExpressionFitnessFunction;
import de.uka.ipd.sdq.beagle.core.measurement.BranchDecisionMeasurementResult;
import de.uka.ipd.sdq.beagle.core.measurement.LoopRepetitionCountMeasurementResult;
import de.uka.ipd.sdq.beagle.core.measurement.ParameterChangeMeasurementResult;
import de.uka.ipd.sdq.beagle.core.measurement.ResourceDemandMeasurementResult;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * ATTENTION: Test coverage check turned off. Remove this comments block when implementing
 * this class!
 *
 * <p>COVERAGE:OFF
 */

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
	 * {@code rdias} all resource demanding internal actions.
	 */
	private final Set<ResourceDemandingInternalAction> rdias;

	/**
	 * {@code branches} all SEFF branches.
	 */
	private final Set<SeffBranch> branches;

	/**
	 * {@code loops} all SEFF Loops.
	 */
	private final Set<SeffLoop> loops;

	/**
	 * {@code externalCalls} all external call parameter.
	 */
	private final Set<ExternalCallParameter> externalCalls;

	/**
	 * {@code rdiasToBeMeasured} all resource demanding internal actions which are to be
	 * measured.
	 */
	private Set<ResourceDemandingInternalAction> rdiasToBeMeasured;

	/**
	 * {@code branchesToBeMeasured} all SEFF branches which are to be measured.
	 */
	private Set<SeffBranch> branchesToBeMeasured;

	/**
	 * {@code loopsToBeMeasured} all SEFF loops which are to be count.
	 */
	private Set<SeffLoop> loopsToBeMeasured;

	/**
	 * {@code externalCallParameterToBeMeasured} all external call parameter which are to
	 * be measured.
	 */
	private Set<ExternalCallParameter> externalCallParameterToBeMeasured;

	/**
	 * {@code rdiasMeasurementResult} all resource demanding internal results.
	 */
	private Set<ResourceDemandMeasurementResult> rdiasMeasurementResult;

	/**
	 * {@code branchDecisionMeasurementResult} all SEFF branches results.
	 */
	private Set<BranchDecisionMeasurementResult> branchDecisionMeasurementResult;

	/**
	 * {@code loopRepititionCountMeasurementResult} all SEFF loop count results.
	 */
	private Set<LoopRepetitionCountMeasurementResult> loopRepititionCountMeasurementResult;

	/**
	 * {@code parameterChangeMeasurementResult} all parameter change results.
	 */
	private Set<ParameterChangeMeasurementResult> parameterChangeMeasurementResult;

	/**
	 * {@code evaluableExpression} all evaluable expressions.
	 */
	private Set<EvaluableExpression> evaluableExpression;

	/**
	 * {@code finalExpression} is the final expression.
	 */
	private EvaluableExpression finalExpression;

	/**
	 * {@code fitnissFunction} is the function to get a better evaluable expression
	 * result.
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
	 * @param rdiasToBeMeasured All resource demanding internal action to be measured.
	 * @param seffBranch All SEFF branches to be measured.
	 * @param loopsToBeMeasured All SEFF loops to be measured.
	 * @param externalCallParameterToBeMeasured All external call parameter to be
	 *            measured.
	 */
	public Blackboard(final Set<ResourceDemandingInternalAction> rdias, final Set<SeffBranch> branches,
		final Set<SeffLoop> loops, final Set<ExternalCallParameter> externalCalls,
		final Set<ResourceDemandingInternalAction> rdiasToBeMeasured, final Set<SeffBranch> seffBranch,
		final Set<SeffLoop> loopsToBeMeasured, final Set<ExternalCallParameter> externalCallParameterToBeMeasured) {
		Validate.notNull(rdias);
		Validate.notNull(branches);
		Validate.notNull(loops);
		Validate.notNull(externalCalls);
		Validate.noNullElements(rdias);
		Validate.noNullElements(branches);
		Validate.noNullElements(loops);
		Validate.noNullElements(externalCalls);
		this.rdias = rdias;
		this.branches = branches;
		this.loops = loops;
		this.externalCalls = externalCalls;
	}

	@Override
	public boolean equals(final Object object) {
		if (object == null) {
			return false;
		}
		if (object == this) {
			return true;
		}
		if (object.getClass() != this.getClass()) {
			return false;
		}
		final Blackboard other = (Blackboard) object;
		return new EqualsBuilder().append(this.rdias, other.rdias).append(this.branches, other.branches)
			.append(this.loops, other.loops).append(this.externalCalls, other.externalCalls).isEquals();
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
		Validate.notNull(this.rdiasToBeMeasured);
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
		Validate.notNull(this.branchesToBeMeasured);
		return this.branchesToBeMeasured;
	}

	/**
	 * {@linkplain SeffLoop SEFF loops} that shall be measured for their repetitions.
	 *
	 * @return All {@linkplain SeffLoop SEFF loops} to be measured. Changes to the
	 *         returned set will not modify the blackboard content. Is never {@code null}.
	 */
	public Set<SeffLoop> getSeffLoopsToBeMeasured() {
		Validate.notNull(this.loopsToBeMeasured);
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
		Validate.notNull(this.externalCallParameterToBeMeasured);
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
		Validate.notNull(toMeasureRdias);
		Validate.noNullElements(toMeasureRdias);
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
		Validate.notNull(toMeasureBranches);
		Validate.noNullElements(toMeasureBranches);
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
		Validate.notNull(toMeasureBranches);
		Validate.noNullElements(toMeasureBranches);
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
		Validate.notNull(toMeasureLoops);
		Validate.noNullElements(toMeasureLoops);
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
		Validate.notNull(toMeasureLoops);
		Validate.noNullElements(toMeasureLoops);
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
		Validate.notNull(parameters);
		Validate.noNullElements(parameters);
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
		Validate.notNull(parameters);
		Validate.noNullElements(parameters);
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
		Validate.notNull(this.rdiasMeasurementResult);
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
		Validate.notNull(this.branchDecisionMeasurementResult);
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
		Validate.notNull(this.loopRepititionCountMeasurementResult);
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
		Validate.notNull(this.parameterChangeMeasurementResult);
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
		Validate.notNull(results);
		this.rdiasMeasurementResult.add(results);
	}

	/**
	 * Adds a measurement result for the provided {@code branch}.
	 *
	 * @param branch A SEFF Branch which was measured. Must not be {@code null}.
	 * @param results The result of that measurement. Must not be {@code null}.
	 */
	public void addMeasurementResultFor(final SeffBranch branch, final BranchDecisionMeasurementResult results) {
		Validate.notNull(results);
		this.branchDecisionMeasurementResult.add(results);
	}

	/**
	 * Adds a measurement result for the provided {@code loop}.
	 *
	 * @param loop A SEFF Loop which was measured. Must not be {@code null}.
	 * @param results The result of that measurement. Must not be {@code null}.
	 */
	public void addMeasurementResultFor(final SeffLoop loop, final LoopRepetitionCountMeasurementResult results) {
		Validate.notNull(results);
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
		Validate.notNull(results);
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
		Validate.notNull(this.evaluableExpression);
		return this.evaluableExpression;
	}

	/**
	 * Adds {@code expression} as a proposal.
	 *
	 * @param element A SEFF element. Must not be {@code null}.
	 * @param expression An evaluable expression proposed to describe {@code element}’s
	 *            measurement results. Must not be {@code null}.
	 */
	public void addProposedExpressionFor(final MeasurableSeffElement element, final EvaluableExpression expression) {
		Validate.notNull(expression);
		this.evaluableExpression.add(expression);
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
		Validate.notNull(this.finalExpression);
		return this.finalExpression;
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
		Validate.notNull(expression);
		this.finalExpression = expression;
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
		Validate.notNull(this.fitnessFunction);
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
