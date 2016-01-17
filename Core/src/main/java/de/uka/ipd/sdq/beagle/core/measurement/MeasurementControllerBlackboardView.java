package de.uka.ipd.sdq.beagle.core.measurement;

import de.uka.ipd.sdq.beagle.core.Blackboard;
import de.uka.ipd.sdq.beagle.core.BlackboardStorer;
import de.uka.ipd.sdq.beagle.core.ExternalCallParameter;
import de.uka.ipd.sdq.beagle.core.ResourceDemandingInternalAction;
import de.uka.ipd.sdq.beagle.core.SeffBranch;
import de.uka.ipd.sdq.beagle.core.SeffLoop;
import de.uka.ipd.sdq.beagle.core.analysis.MeasurementResultAnalyser;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpression;
import de.uka.ipd.sdq.beagle.core.judge.EvaluableExpressionFitnessFunction;

import java.io.Serializable;
import java.util.Set;

/**
 * View of the {@link Blackboard} designed to be used by {@link MeasurementResultAnalyser}
 * . It allows reading access and adding access for
 * {@linkplain ResourceDemandingInternalAction resource demanding internal actions},
 * {@linkplain SeffBranch SEFF branches}, {@linkplain SeffLoop SEFF loops},
 * {@linkplain ExternalCallParameter external call parameters}, reading, and the fitness
 * function.
 *
 * @author Christoph Michelbach
 * @author Michael Vogt
 */
public class MeasurementControllerBlackboardView {

  private Blackboard blackboard;

  private MeasurementControllerBlackboardView(Blackboard blackboard) {
    this.blackboard = blackboard;
  }

  /**
   * Delegates to {@link de.uka.ipd.sdq.beagle.core.Blackboard#getRdiasToBeMeasured()}.
   *
   * @return All {@linkplain ResourceDemandingInternalAction resource demanding internal
   *         actions} to be measured. Changes to the returned set will not modify the
   *         blackboard content. Is never {@code null}.
   * @see de.uka.ipd.sdq.beagle.core.Blackboard#getRdiasToBeMeasured()
   */
  public Set<ResourceDemandingInternalAction> getRdiasToBeMeasured() {
    return blackboard.getAllRdias();
  }

  /**
   * Delegates to
   * {@link de.uka.ipd.sdq.beagle.core.Blackboard#getSeffBranchesToBeMeasured()}.
   *
   * @return All {@linkplain SeffBranch SEFF branches} to be measured. Changes to the
   *         returned set will not modify the blackboard content. Is never {@code null}.
   * @see de.uka.ipd.sdq.beagle.core.Blackboard#getSeffBranchesToBeMeasured()
   */
  public Set<SeffBranch> getSeffBranchesToBeMeasured() {
    return blackboard.getAllSeffBranches();
  }

  /**
   * Delegates to
   * {@link de.uka.ipd.sdq.beagle.core.Blackboard#getSeffLoopsToBeMeasured()}.
   *
   * @return All {@linkplain SeffLoop SEFF loops} to be measured. Changes to the
   *         returned set will not modify the blackboard content. Is never {@code null}.
   * @see de.uka.ipd.sdq.beagle.core.Blackboard#getSeffLoopsToBeMeasured()
   */
  public Set<SeffLoop> getSeffLoopsToBeMeasured() {
    return blackboard.getAllSeffLoops();
  }

  /**
   * Delegates to
   * {@link de.uka.ipd.sdq.beagle.core.Blackboard#getExternalCallParametersToBeMeasured()}
   * .
   *
   * @return All {@linkplain ExternalCallParameter external call parameters} which shall
   *         be measured. Is never {@code null}.
   * @see de.uka.ipd.sdq.beagle.core.Blackboard#getExternalCallParametersToBeMeasured()
   */
  public Set<SeffLoop> getExternalCallParametersToBeMeasured() {
    return blackboard.getAllSeffLoops();
  }

  /**
   * Delegates to
   * {@link Blackboard#addMeasurementResultFor(ResourceDemandingInternalAction,
   *  ResourceDemandMeasurementResult)}
   * .
   *
   * @param rdia A resource demanding internal action that was measured. Must not be
   *            {@code null} .
   * @param results The result of that measurement. Must not be {@code null}.
   */
  public void addMeasurementResultFor(final ResourceDemandingInternalAction rdia,
      final ResourceDemandMeasurementResult results) {
    blackboard.addMeasurementResultFor(rdia, results);
  }

  /**
   * Delegates to
   * {@link Blackboard#addMeasurementResultFor(SeffLoop, LoopRepetitionCountMeasurementResult)}
   * .
   *
   * @param loop A SEFF Loop was measured. Must not be {@code null}.
   * @param results The result of that measurement. Must not be {@code null}.
   */
  public void addMeasurementResultFor(final SeffLoop loop,
      final LoopRepetitionCountMeasurementResult results) {
    blackboard.addMeasurementResultFor(loop, results);
  }

  /**
   * Delegates to
   * {@link Blackboard#addMeasurementResultFor(SeffBranch, BranchDecisionMeasurementResult)}
   * .
   *
   * @param branch A SEFF Branch that was measured. Must not be {@code null}.
   * @param results The result of that measurement. Must not be {@code null}.
   */
  public void addMeasurementResultFor(final SeffBranch branch, 
      final BranchDecisionMeasurementResult results) {
    blackboard.addMeasurementResultFor(branch, results);
  }

  /**
   * Delegates to
   * {@link Blackboard#addMeasurementResultFor(ExternalCallParameter, 
   * ParameterChangeMeasurementResult)}
   * .
   *
   * @param parameter An external call parameter which was measured. Must not be
   *            {@code null}.
   * @param results The result of that measurement. Must not be {@code null}.
   */
  public void addMeasurementResultFor(final ExternalCallParameter parameter,
      final ParameterChangeMeasurementResult results) {
    blackboard.addMeasurementResultFor(parameter, results);
  }

  /**
   * Delegates to {@link de.uka.ipd.sdq.beagle.core.Blackboard#getFitnessFunction()} .
   *
   * @return An object which holds and is responsible allows access to the fitness
   *         function grading {@linkplain EvaluableExpression evaluable expressions}
   *         regarding their fitness.
   * @see de.uka.ipd.sdq.beagle.core.Blackboard#getFitnessFunction()
   */
  public EvaluableExpressionFitnessFunction getFitnessFunction() {
    return blackboard.getFitnessFunction();
  }

  /**
   * Delegates to {@link de.uka.ipd.sdq.beagle.core.Blackboard#readFor(Class)} .
   *
   * @param writer The class the desired data was written for. Must not be {@code null}.
   * @param <WRITTEN_TYPE> The type of the data to be read.
   * @return The data written in the last call to
   *         {@linkplain de.uka.ipd.sdq.beagle.core.Blackboard#writeFor(Class, Serializable)}
   *         for {@code writer}. {@code null} if no data has been written for
   *         {@code writer} yet.
   * @see de.uka.ipd.sdq.beagle.core.Blackboard#readFor(Class)
   */
  public <WRITTEN_TYPE extends Serializable> WRITTEN_TYPE readFor(
      final Class<? extends BlackboardStorer<WRITTEN_TYPE>> writer) {
    return blackboard.readFor(writer);
  }

  /**
   * Delegates to
   * {@link de.uka.ipd.sdq.beagle.core.Blackboard#writeFor(Class, Serializable)} .
   *
   * @param writer The class the data should be written for. Must not be {@code null}.
   * @param written The data to write.
   * @param <WRITTEN_TYPE> {@code written}’s type.
   * @see de.uka.ipd.sdq.beagle.core.Blackboard#writeFor(Class, Serializable)
   */
  public <WRITTEN_TYPE extends Serializable> void writeFor(
      final Class<? extends BlackboardStorer<WRITTEN_TYPE>> writer, final WRITTEN_TYPE written) {
    blackboard.writeFor(writer, written);
  }
}
