package de.uka.ipd.sdq.beagle.core.measurement;
/**
 * ATTENTION: Test coverage check turned off. Remove this comments block when implementing
 * this class!
 * 
 * <p>COVERAGE:OFF
 */

import de.uka.ipd.sdq.beagle.core.AnalysisController;
import de.uka.ipd.sdq.beagle.core.Blackboard;
import de.uka.ipd.sdq.beagle.core.BlackboardStorer;
import de.uka.ipd.sdq.beagle.core.ExternalCallParameter;
import de.uka.ipd.sdq.beagle.core.ResourceDemandingInternalAction;
import de.uka.ipd.sdq.beagle.core.SeffBranch;
import de.uka.ipd.sdq.beagle.core.SeffLoop;
import de.uka.ipd.sdq.beagle.core.analysis.MeasurementResultAnalyser;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpression;
import de.uka.ipd.sdq.beagle.core.judge.EvaluableExpressionFitnessFunction;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Set;

/**
 * View of the {@link Blackboard} designed to be used by {@link MeasurementResultAnalyser}
 * . It allows reading access for {@linkplain ResourceDemandingInternalAction resource
 * demanding internal actions}, {@linkplain SeffBranch SEFF branches},
 * {@linkplain SeffLoop SEFF loops}, {@linkplain ExternalCallParameter external call
 * parameters}, reading, and the fitness function.
 *
 * @author Christoph Michelbach
 * @author Michael Vogt
 */
public final class ReadOnlyMeasurementControllerBlackboardView {

	/**
	 * Blackboard instance committed from the {@link AnalysisController}.
	 */
	private Blackboard blackboard;

	/**
	 * Set the blackboard instance from the {@link AnalysisController} to the private
	 * blackboard attribute.
	 *
	 * @param blackboard The blackboard given from the {@link AnalysisController}.
	 */
	public ReadOnlyMeasurementControllerBlackboardView(final Blackboard blackboard) {
		Validate.notNull(blackboard);
		this.blackboard = blackboard;
	}

	@Override
	public boolean equals(final Object object) {
		if (object == null) {
			return false;
		}
		if (object == this) {
			return true;
		}
		final ReadOnlyMeasurementControllerBlackboardView other = (ReadOnlyMeasurementControllerBlackboardView) object;
		return new EqualsBuilder().append(this.blackboard, other.blackboard).isEquals();
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
		return this.blackboard.getRdiasToBeMeasured();
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
		return this.blackboard.getSeffBranchesToBeMeasured();
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
		return this.blackboard.getSeffLoopsToBeMeasured();
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
	public Set<ExternalCallParameter> getExternalCallParametersToBeMeasured() {
		return this.blackboard.getExternalCallParametersToBeMeasured();
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
		return this.blackboard.getFitnessFunction();
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
		return this.blackboard.readFor(writer);
	}

	@Override
	public int hashCode() {
		// you pick a hard-coded, randomly chosen, non-zero, odd number
		// ideally different for each class
		return new HashCodeBuilder(135, 153).append(this.blackboard).toHashCode();
	}
}
