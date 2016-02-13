package de.uka.ipd.sdq.beagle.core.measurement;

import de.uka.ipd.sdq.beagle.core.ExternalCallParameter;
import de.uka.ipd.sdq.beagle.core.MeasurableSeffElement;
import de.uka.ipd.sdq.beagle.core.ResourceDemandingInternalAction;
import de.uka.ipd.sdq.beagle.core.SeffBranch;
import de.uka.ipd.sdq.beagle.core.SeffLoop;
import de.uka.ipd.sdq.beagle.core.measurement.order.MeasurementEvent;
import de.uka.ipd.sdq.beagle.core.measurement.order.MeasurementEventVisitor;

import java.util.Set;

/**
 * Parses {@linkplain MeasurementEvent MeasurementEvents} in order to generate
 * {@linkplain ParameterisationDependentMeasurementResult
 * ParameterisationDependentMeasurementResults} out of them. It is created for a sequence
 * of events that occurred while measuring software (the “input events”). The parsed
 * results can be obtained by querying with
 *
 * {@linkplain MeasurableSeffElement MeasurableSeffElements} which will return all
 * measurement results found for that element.
 *
 * @author Joshua Gleitze
 */
public class MeasurementEventParser implements MeasurementEventVisitor {

	/**
	 * Creates a parser to parse {@code events}. The events must be provided in
	 * chronological order, starting with the event that occurred first.
	 *
	 * @param events The events to be parsed. Must not be {@code null} and all contained
	 *            events must not be {@code null}.
	 */
	public MeasurementEventParser(final MeasurementEvent... events) {
	}

	/**
	 * Creates a parser to parse {@code events}. The events must be provided in
	 * chronological order, starting with the event that occurred first.
	 *
	 * @param events The events to be parsed. Must not be {@code null} and all contained
	 *            events must not be {@code null}.
	 */
	public MeasurementEventParser(final Iterable<MeasurementEvent> events) {

	}

	/**
	 * Gets all results that could be parsed from the input events for {@code rdia}.
	 *
	 * @param rdia A resource demanding internal action to get the measurement results of.
	 *            Must not be {@code null} .
	 * @return All measurement results parsed for {@code rdia}. Is never {@code null}.
	 */
	public Set<ResourceDemandMeasurementResult> getMeasurementResultsFor(final ResourceDemandingInternalAction rdia) {
		return null;
	}

	/**
	 * Gets all results that could be parsed from the input events for {@code branch}.
	 *
	 * @param branch A SEFF Branch to get the measurement results of. Must not be
	 *            {@code null} .
	 * @return All measurement results parsed for {@code branch}. Is never {@code null}.
	 */
	public Set<BranchDecisionMeasurementResult> getMeasurementResultsFor(final SeffBranch branch) {
		return null;
	}

	/**
	 * Gets all results that could be parsed from the input events for {@code loop}.
	 *
	 * @param loop A SEFF Loop to get the measurement results of. Must not be {@code null}
	 *            .
	 * @return All measurement results parsed for {@code loop}. Is never {@code null}.
	 */
	public Set<LoopRepetitionCountMeasurementResult> getMeasurementResultsFor(final SeffLoop loop) {
		return null;
	}

	/**
	 * Gets all results that could be parsed from the input events for
	 * {@code externalCallParameter}.
	 *
	 * @param externalCallParameter An external parameter to get the measurement results
	 *            of. Must not be {@code null}.
	 * @return All measurement results parsed for {@code externalCallParameter}. Is never
	 *         {@code null}.
	 */
	public Set<ParameterChangeMeasurementResult> getMeasurementResultsFor(
		final ExternalCallParameter externalCallParameter) {
		return null;
	}

}
