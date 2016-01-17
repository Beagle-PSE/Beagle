package de.uka.ipd.sdq.beagle.core;

import de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpression;
import de.uka.ipd.sdq.beagle.core.judge.EvaluableExpressionFitnessFunction;
import de.uka.ipd.sdq.beagle.core.measurement.BranchDecisionMeasurementResult;
import de.uka.ipd.sdq.beagle.core.measurement.LoopRepetitionCountMeasurementResult;
import de.uka.ipd.sdq.beagle.core.measurement.ParameterChangeMeasurementResult;
import de.uka.ipd.sdq.beagle.core.measurement.ResourceDemandMeasurementResult;

import java.io.Serializable;
import java.util.Collection;
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
  private Set<ResourceDemandingInternalAction> rdias;
  private Set<SeffBranch> branches;
  private Set<SeffLoop> loops;
  private Set<ExternalCallParameter> externalCalls;
  private Set<ResourceDemandingInternalAction> rdiasToBeMeasured;
  private Set<SeffBranch> branchesToBeMeasured;
  private Set<SeffLoop> loopsToBeMeasured;
  private Set<ExternalCallParameter> externalCallParameterToBeMeasured;
  private Set<ResourceDemandMeasurementResult> rdiasMeasurementResult;
  private Set<BranchDecisionMeasurementResult> branchDecisionMeasurementResult;
  private Set<LoopRepetitionCountMeasurementResult> loopRepititionCountMeasurementResult;
  private Set<ParameterChangeMeasurementResult> parameterChangeMeasurementResult;
  private Set<EvaluableExpression> evaluableExpression;
  private EvaluableExpression finalExpression;
  private EvaluableExpressionFitnessFunction fitnissFunction;

  /**
   * Creates a new blackboard that can be used to analyse the given elements.
   *
   * @param rdias All resource demanding internal action to be known to analysers.
   * @param branches All SEFF branches to be known to analysers.
   * @param loops All SEFF loops to be known to analysers.
   */
  public Blackboard(final Set<ResourceDemandingInternalAction> rdias,
      final Set<SeffBranch> branches,final Set<SeffLoop> loops) {
    this.rdias = rdias;
    this.branches = branches;
    this.loops = loops;
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
    return rdias;
  }

  /**
   * All {@linkplain SeffBranch SEFF branches} known to Beagle.
   *
   * @return all {@linkplain SeffBranch SEFF branches} known to Beagle. Changes to the
   *         returned set will not modify the blackboard content. Is never {@code null}.
   */
  public Set<SeffBranch> getAllSeffBranches() {
    return branches;
  }

  /**
   * All {@linkplain SeffLoop SEFF loops} known to Beagle.
   *
   * @return all {@linkplain SeffLoop SEFF loops} known to Beagle. Changes to the
   *         returned set will not modify the blackboard content. Is never {@code null}.
   */
  public Set<SeffLoop> getAllSeffLoops() {
    return loops;
  }

  /**
   * Returns all {@linkplain ExternalCallParameter external call parameters} known to
   * Beagle.
   *
   * @return All {@linkplain ExternalCallParameter external call parameters} known to
   *         Beagle. Is never {@code null}.
   */
  public Set<ExternalCallParameter> getAllExternalCallParameters() {
    return externalCalls;
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
    return rdiasToBeMeasured;
  }

  /**
   * {@linkplain SeffBranch SEFF branches} that shall be measured for their branch
   * decisions.
   *
   * @return All {@linkplain SeffBranch SEFF branches} to be measured. Changes to the
   *         returned set will not modify the blackboard content. Is never {@code null}.
   */
  public Set<SeffBranch> getSeffBranchesToBeMeasured() {
    return branchesToBeMeasured;
  }

  /**
   * {@linkplain SeffLoop SEFF loops} that shall be measured for their repetitions.
   *
   * @return All {@linkplain SeffLoop SEFF loops} to be measured. Changes to the
   *         returned set will not modify the blackboard content. Is never {@code null}.
   */
  public Set<SeffLoop> getSeffLoopsToBeMeasured() {
    return loopsToBeMeasured;
  }

  /**
   * Returns all {@linkplain ExternalCallParameter external call parameters} which shall
   * be measured.
   *
   * @return All {@linkplain ExternalCallParameter external call parameters} which shall
   *         be measured. Is never {@code null}.
   */
  public Set<ExternalCallParameter> getExternalCallParametersToBeMeasured() {
    return externalCallParameterToBeMeasured;
  }

  /**
   * Reports that {@code rdias} shall be measured for its resource demands.
   *
   * @param rdias Resource demanding internal actions that shall be measured. Must not
   *            be {@code null} and must be known to this blackboard.
   * @see #addToBeMeasuredRdias(Collection)
   */
  public void addToBeMeasuredRdias(final ResourceDemandingInternalAction... rdias) {
  }

  /**
   * Reports that {@code rdias} shall be measured for its resource demands.
   *
   * @param rdias Resource demanding internal actions that shall be measured. Must not
   *            be {@code null} and must be known to this blackboard.
   * @see #addToBeMeasuredRdias(ResourceDemandingInternalAction...)
   */
  public void addToBeMeasuredRdias(final Collection<ResourceDemandingInternalAction> rdias) {
    rdiasToBeMeasured.addAll(rdias);
  }

  /**
   * Reports that {@code branches} shall be measured for its branch decisions.
   *
   * @param branches SEFF branches that shall be measured. Must not be {@code null} and
   *            must be known to this blackboard.
   * @see #addToBeMeasuredSeffBranches(Collection)
   */
  public void addToBeMeasuredSeffBranches(final SeffBranch... branches) {
  }

  /**
   * Reports that {@code branches} shall be measured for its branch decisions.
   *
   * @param branches SEFF branches that shall be measured. Must not be {@code null} and
   *            must be known to this blackboard.
   * @see #addToBeMeasuredSeffBranches(SeffBranch...)
   */
  public void addToBeMeasuredSeffBranches(final Collection<SeffBranch> branches) {
    branchesToBeMeasured.addAll(branches);
  }

  /**
   * Reports that {@code loops} shall be measured for its repetitions.
   *
   * @param loops SEFF Loops that shall be measured. Must not be {@code null} and must
   *            be known to this blackboard.
   * @see #addToBeMeasuredSeffLoops(Collection)
   */
  public void addToBeMeasuredSeffLoops(final SeffLoop... loops) {
  }

  /**
   * Reports that {@code loops} shall be measured for its repetitions.
   *
   * @param loops SEFF Loops that shall be measured. Must not be {@code null} and must
   *            be known to this blackboard.
   * @see #addToBeMeasuredSeffLoops(SeffLoop...)
   */
  public void addToBeMeasuredSeffLoops(final Collection<SeffLoop> loops) {
    loopsToBeMeasured.addAll(loops);
  }

  /**
   * Reports that {@code parameters} shall be measured.
   *
   * @param parameters external call parameters that shall be measured. Must not be
   *            {@code null} and must be known to this blackboard.
   * @see #addToBeMeasuredExternalCallParameters(Collection)
   */
  public void addToBeMeasuredExternalCallParameters(final ExternalCallParameter... parameters) {
  }

  /**
   * Reports that {@code parameters} shall be measured.
   *
   * @param parameters external call parameters that shall be measured. Must not be
   *            {@code null} and must be known to this blackboard.
   * @see #addToBeMeasuredExternalCallParameters(ExternalCallParameter...)
   */
  public void addToBeMeasuredExternalCallParameters(
      final Collection<ExternalCallParameter> parameters) {
    externalCallParameterToBeMeasured.addAll(parameters);
  }

  /**
   * Gets all results yet measured for the resource demands of {@code rdia}.
   *
   * @param rdia An resource demanding internal action to get the measurement results
   *            of. Must not be {@code null}.
   * @return All measurement results reported for {@code rdia}. Changes to the returned
   *         set will not modify the blackboard content. Is never {@code null}.
   */
  public Set<ResourceDemandMeasurementResult> getMeasurementResultsFor(
      final ResourceDemandingInternalAction rdia) {
    return rdiasMeasurementResult;
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
    return branchDecisionMeasurementResult;
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
    return loopRepititionCountMeasurementResult;
  }

  /**
   * Gets all results yet measured for the external parameter
   * {@code externalCallParameter}.
   *
   * @param externalCallParameter An external parameter to get the measurement results
   *            of. Must not be {@code null}.
   * @return All measurement results reported for {@code loexternalCallParameterop}.
   *         Changes to the returned set will not modify the blackboard content. Is
   *         never {@code null}.
   */
  public Set<ParameterChangeMeasurementResult> getMeasurementResultsFor(
      final ExternalCallParameter externalCallParameter) {
    return parameterChangeMeasurementResult;
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
    rdiasMeasurementResult.add(results);
  }

  /**
   * Adds a measurement result for the provided {@code branch}.
   *
   * @param branch A SEFF Branch which was measured. Must not be {@code null}.
   * @param results The result of that measurement. Must not be {@code null}.
   */
  public void addMeasurementResultFor(final SeffBranch branch,
      final BranchDecisionMeasurementResult results) {
    branchDecisionMeasurementResult.add(results);
  }

  /**
   * Adds a measurement result for the provided {@code loop}.
   *
   * @param loop A SEFF Loop which was measured. Must not be {@code null}.
   * @param results The result of that measurement. Must not be {@code null}.
   */
  public void addMeasurementResultFor(final SeffLoop loop,
      final LoopRepetitionCountMeasurementResult results) {
    loopRepititionCountMeasurementResult.add(results);
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
    parameterChangeMeasurementResult.add(results);
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
    return evaluableExpression;
  }

  /**
   * Adds {@code expression} as a proposal.
   *
   * @param element A SEFF element. Must not be {@code null}.
   * @param expression An evaluable expression proposed to describe {@code element}’s
   *            measurement results. Must not be {@code null}.
   */
  public void addProposedExpressionFor(final MeasurableSeffElement element,
      final EvaluableExpression expression) {
    evaluableExpression.add(expression);
  }

  /**
   * Returns the final expression set for {@code element}. The return value of this
   * method may change if
   * {@link #setFinalExpressionFor(MeasurableSeffElement, EvaluableExpression)} is
   * called with the same {@code element} between calls to
   * {@code addProposedExpressionFor} with this element as parameter.
   *
   * @param element A SEFF element. Must not be {@code null}.
   */
  public EvaluableExpression getFinalExpressionFor(final MeasurableSeffElement element) {
    return finalExpression;
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
  public void setFinalExpressionFor(final MeasurableSeffElement element,
      final EvaluableExpression expression) {
    finalExpression = expression;
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
    return fitnissFunction;
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
   */
  public <WRITTEN_TYPE extends Serializable> void writeFor(
    final Class<? extends BlackboardStorer<WRITTEN_TYPE>> writer, final WRITTEN_TYPE written) {
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
  public <WRITTEN_TYPE extends Serializable> WRITTEN_TYPE readFor(
    final Class<? extends BlackboardStorer<WRITTEN_TYPE>> writer) {
    return null;
  }
}