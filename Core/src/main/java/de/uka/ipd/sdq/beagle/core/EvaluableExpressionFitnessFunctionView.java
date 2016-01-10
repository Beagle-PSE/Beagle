package de.uka.ipd.sdq.beagle.core;

import de.uka.ipd.sdq.beagle.core.measurement.BranchDecisionMeasurementResult;
import de.uka.ipd.sdq.beagle.core.measurement.ParameterChangeMeasurementResult;
import de.uka.ipd.sdq.beagle.core.measurement.ResourceDemandMeasurementResult;

import java.io.Serializable;
import java.util.Set;

/**
 * Interface of {@link Blackboard} designed to be used by
 * {@link EvaluableExpressionFitnessFunction}. Provides reading and writing access for
 * data on the Blackboard as well as access to the {@code getMeasurementResultsFor}
 * methods.
 *
 * @author Christoph Michelbach
 */
public interface EvaluableExpressionFitnessFunctionView {

	/**
	 * Delegates to
	 * {@link de.uka.ipd.sdq.beagle.core.Blackboard#getMeasurementResultsFor(ResourceDemandingInternalAction)}
	 * .
	 *
	 * @param rdia An resource demanding internal action to get the measurement results
	 *            of. Must not be {@code null}.
	 * @return All measurement results reported for {@code rdia}. Changes to the returned
	 *         set will not modify the blackboard content. Is never {@code null}.
	 * @see de.uka.ipd.sdq.beagle.core.Blackboard#getMeasurementResultsFor(ResourceDemandingInternalAction)
	 */
	Set<ResourceDemandMeasurementResult> getMeasurementResultsFor(final ResourceDemandingInternalAction rdia);

	/**
	 * Delegates to
	 * {@link de.uka.ipd.sdq.beagle.core.Blackboard#getMeasurementResultsFor(SeffBranch)}.
	 *
	 * @param branch A SEFF Branch to get the measurement results of. Must not be
	 *            {@code null}.
	 * @return All measurement results reported for {@code branch}. Changes to the
	 *         returned set will not modify the blackboard content. Is never {@code null}.
	 * @see de.uka.ipd.sdq.beagle.core.Blackboard#getMeasurementResultsFor(SeffBranch)
	 */
	Set<BranchDecisionMeasurementResult> getMeasurementResultsFor(final SeffBranch branch);

	/**
	 * Delegates to
	 * {@link de.uka.ipd.sdq.beagle.core.Blackboard#getMeasurementResultsFor(SeffLoop)}.
	 *
	 * @param loop A SEFF Loop to get the measurement results of. Must not be {@code null}
	 *            .
	 * @return All measurement results reported for {@code loop}. Changes to the returned
	 *         set will not modify the blackboard content. Is never {@code null}.
	 * @see de.uka.ipd.sdq.beagle.core.Blackboard#getMeasurementResultsFor(SeffLoop)
	 */
	Set<ResourceDemandMeasurementResult> getMeasurementResultsFor(final SeffLoop loop);

	/**
	 * Delegates to
	 * {@link de.uka.ipd.sdq.beagle.core.Blackboard#getMeasurementResultsFor(ExternalCallParameter)}
	 * .
	 *
	 * @param externalCallParameter An external parameter to get the measurement results
	 *            of. Must not be {@code null}.
	 * @return All measurement results reported for {@code loexternalCallParameterop}.
	 *         Changes to the returned set will not modify the blackboard content. Is
	 *         never {@code null}.
	 * @see de.uka.ipd.sdq.beagle.core.Blackboard#getMeasurementResultsFor(ExternalCallParameter)
	 */
	Set<ParameterChangeMeasurementResult> getMeasurementResultsFor(final ExternalCallParameter externalCallParameter);

	/**
	 * Delegates to
	 * {@link de.uka.ipd.sdq.beagle.core.Blackboard#getProposedExpressionFor(MeasurableSeffElement)}
	 * .
	 *
	 * @param element A SEFF element. Must not be {@code null}.
	 */
	void getProposedExpressionFor(final MeasurableSeffElement element);

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
	<WRITTEN_TYPE extends Serializable> WRITTEN_TYPE readFor(
		final Class<? extends BlackboardStorer<WRITTEN_TYPE>> writer);

	/**
	 * Delegates to
	 * {@link de.uka.ipd.sdq.beagle.core.Blackboard#writeFor(Class, Serializable)} .
	 *
	 * @param writer The class the data should be written for. Must not be {@code null}.
	 * @param written The data to write.
	 * @param <WRITTEN_TYPE> {@code written}’s type.
	 * @see de.uka.ipd.sdq.beagle.core.Blackboard#writeFor(Class, Serializable)
	 */
	<WRITTEN_TYPE extends Serializable> void writeFor(final Class<? extends BlackboardStorer<WRITTEN_TYPE>> writer,
		final WRITTEN_TYPE written);

}
