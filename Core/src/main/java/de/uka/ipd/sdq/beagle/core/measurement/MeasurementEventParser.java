package de.uka.ipd.sdq.beagle.core.measurement;

import de.uka.ipd.sdq.beagle.core.CodeSection;
import de.uka.ipd.sdq.beagle.core.ExternalCallParameter;
import de.uka.ipd.sdq.beagle.core.MeasurableSeffElement;
import de.uka.ipd.sdq.beagle.core.ResourceDemandingInternalAction;
import de.uka.ipd.sdq.beagle.core.SeffBranch;
import de.uka.ipd.sdq.beagle.core.SeffLoop;
import de.uka.ipd.sdq.beagle.core.measurement.order.CodeSectionExecutedEvent;
import de.uka.ipd.sdq.beagle.core.measurement.order.MeasurementEvent;
import de.uka.ipd.sdq.beagle.core.measurement.order.MeasurementEventVisitor;
import de.uka.ipd.sdq.beagle.core.measurement.order.ParameterValueCapturedEvent;
import de.uka.ipd.sdq.beagle.core.measurement.order.ResourceDemandCapturedEvent;

import org.apache.commons.lang3.Validate;
import org.apache.commons.collections4.IteratorUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
 * @author Roman Langrehr
 */
public class MeasurementEventParser {

	/**
	 * All measurement events in chronological order.
	 */
	private List<MeasurementEvent> measurementEvents;

	/**
	 * Maps for each code sections to all occurrences of measurement events for this code
	 * section in {@link #measurementEvents}.
	 */
	private Map<CodeSection, Set<Integer>> codeSectionMapping;

	/**
	 * Creates a parser to parse {@code events}. The events must be provided in
	 * chronological order, starting with the event that occurred first.
	 *
	 * @param events The events to be parsed. Must not be {@code null} and all contained
	 *            events must not be {@code null}.
	 */
	public MeasurementEventParser(final MeasurementEvent... events) {
		Validate.noNullElements(events);
		this.measurementEvents = Arrays.asList(events);
		this.createCodeSectionMapping();
	}

	/**
	 * Creates a parser to parse {@code events}. The events must be provided in
	 * chronological order, starting with the event that occurred first.
	 *
	 * @param events The events to be parsed. Must not be {@code null} and all contained
	 *            events must not be {@code null}.
	 */
	public MeasurementEventParser(final Iterable<MeasurementEvent> events) {
		Validate.noNullElements(events);
		this.measurementEvents = IteratorUtils.toList(events);
		this.createCodeSectionMapping();
	}

	/**
	 * Generates a new {@link #codeSectionMapping} for the {@link #measurementEvents}.
	 */
	private void createCodeSectionMapping() {
		for (int i = 0; i < this.measurementEvents.size(); i++) {
			final CodeSection currentCodeSection = this.measurementEvents.get(i).getCodeSection();
			if (!this.codeSectionMapping.containsKey(currentCodeSection)) {
				this.codeSectionMapping.put(currentCodeSection, new HashSet<>());
			}
			this.codeSectionMapping.get(currentCodeSection).add(i);
		}
	}

	/**
	 * Gets all results that could be parsed from the input events for {@code rdia}.
	 *
	 * @param resourceDemandingInternalAction A resource demanding internal action to get
	 *            the measurement results of. Must not be {@code null} .
	 * @return All measurement results parsed for {@code rdia}. Is never {@code null}.
	 *         Contains never {@code null} elements.
	 */
	public Set<ResourceDemandMeasurementResult> getMeasurementResultsFor(
		final ResourceDemandingInternalAction resourceDemandingInternalAction) {
		Validate.notNull(resourceDemandingInternalAction);
		final Set<ResourceDemandMeasurementResult> resourceDemandMeasurementResults = new HashSet<>();
		final ResourceDemandingInternalActionMeasurementEventVisitor resourceDemandingInternalActionMeasurementEventVisitor =
			new ResourceDemandingInternalActionMeasurementEventVisitor(resourceDemandingInternalAction,
				resourceDemandMeasurementResults);
		for (final Integer index : this.codeSectionMapping.get(resourceDemandingInternalAction.getAction())) {
			final MeasurementEvent measurementEvent = this.measurementEvents.get(index);
			measurementEvent.receive(resourceDemandingInternalActionMeasurementEventVisitor);
		}
		return resourceDemandMeasurementResults;

	}

	/**
	 * Gets all results that could be parsed from the input events for {@code branch}.
	 *
	 * @param branch A SEFF Branch to get the measurement results of. Must not be
	 *            {@code null} .
	 * @return All measurement results parsed for {@code branch}. Is never {@code null}.
	 *         Contains never {@code null} elements.
	 */
	public Set<BranchDecisionMeasurementResult> getMeasurementResultsFor(final SeffBranch branch) {
		final Set<BranchDecisionMeasurementResult> branchDecisionMeasurementResults = new HashSet<>();
		final Set<Integer> codeSectionEventsForThisBranch = new HashSet<>();
		for (final CodeSection possibilyPickedBranch : branch.getBranches()) {
			for (final Integer index : this.codeSectionMapping.get(possibilyPickedBranch)) {
				codeSectionEventsForThisBranch.add(index);
			}
		}

		for (final Integer pickedBranch : codeSectionEventsForThisBranch) {
			final int branchIndex = branch.getBranches().indexOf(this.measurementEvents.get(pickedBranch));
			branchDecisionMeasurementResults.add(new BranchDecisionMeasurementResult(branchIndex));
		}
		return branchDecisionMeasurementResults;
	}

	/**
	 * Gets all results that could be parsed from the input events for {@code loop}.
	 *
	 * @param loop A SEFF Loop to get the measurement results of. Must not be {@code null}
	 *            .
	 * @return All measurement results parsed for {@code loop}. Is never {@code null}.
	 *         Contains never {@code null} elements.
	 */
	public Set<LoopRepetitionCountMeasurementResult> getMeasurementResultsFor(final SeffLoop loop) {
		return null;
	}

	/**
	 * Note: this method is out of our projects scope and not yet implemented. Gets all
	 * results that could be parsed from the input events for
	 * {@code externalCallParameter}.
	 *
	 * @param externalCallParameter An external parameter to get the measurement results
	 *            of. Must not be {@code null}.
	 * @return All measurement results parsed for {@code externalCallParameter}. Is never
	 *         {@code null}. Contains never {@code null} elements.
	 */
	public Set<ParameterChangeMeasurementResult> getMeasurementResultsFor(
		final ExternalCallParameter externalCallParameter) {
		return null;
	}

	/**
	 * A {@link MeasurementEventVisitor} for a specific
	 * {@link ResourceDemandingInternalAction}.
	 *
	 * <p>Only {@linkplain MeasurementEvent MeasurementEvents} with the correct
	 * {@link CodeSection} may be visited with this visitor.
	 *
	 * @author Roman Langrehr
	 */
	private class ResourceDemandingInternalActionMeasurementEventVisitor implements MeasurementEventVisitor {

		/**
		 * The {@link ResourceDemandingInternalAction} where we want to know the new
		 * measurement results.
		 */
		private ResourceDemandingInternalAction resourceDemandingInternalAction;

		/**
		 * The set, where the {@link ResourceDemandMeasurementResult} should be added.
		 */
		private Set<ResourceDemandMeasurementResult> resourceDemandMeasurementResults;

		/**
		 * Creates a visitor for a specific {@link ResourceDemandingInternalAction}.
		 *
		 * @param resourceDemandingInternalAction The
		 *            {@link ResourceDemandingInternalAction} where we want to know the
		 *            new measurement results.
		 * @param resourceDemandMeasurementResults The set, where the
		 *            {@link ResourceDemandMeasurementResult} should be added.
		 */
		ResourceDemandingInternalActionMeasurementEventVisitor(
			final ResourceDemandingInternalAction resourceDemandingInternalAction,
			final Set<ResourceDemandMeasurementResult> resourceDemandMeasurementResults) {
			this.resourceDemandingInternalAction = resourceDemandingInternalAction;
			this.resourceDemandMeasurementResults = resourceDemandMeasurementResults;
		}

		@Override
		public void visit(final CodeSectionExecutedEvent codeSectionExecutedEvent) {
			// We don't care about them.
		}

		@Override
		public void visit(final ResourceDemandCapturedEvent resourceDemandCapturedEvent) {
			// Check if this measurement event is for the correct resource type.
			if (resourceDemandCapturedEvent.getType() == this.resourceDemandingInternalAction.getResourceType()) {
				// Check if it has a realistic value.
				if (resourceDemandCapturedEvent.getValue() >= 0) {
					this.resourceDemandMeasurementResults
						.add(new ResourceDemandMeasurementResult(resourceDemandCapturedEvent.getValue()));
				}
			}
		}

		@Override
		public void visit(final ParameterValueCapturedEvent parameterValueCapturedEvent) {
			// We don't care about them.
		}
	}

	/**
	 * A {@link MeasurementEventVisitor} for a specific {@link SeffBranch}.
	 *
	 * <p>Only {@linkplain MeasurementEvent MeasurementEvents} with the correct
	 * {@link CodeSection} may be visited with this visitor.
	 *
	 * @author Roman Langrehr
	 */
	private class SeffBranchMeasurementEventVisitor implements MeasurementEventVisitor {

		/**
		 * The {@link SeffBranch} where we want to know the new measurement results.
		 */
		private SeffBranch seffBranch;

		/**
		 * The set, where the {@link BranchDecisionMeasurementResult} should be added.
		 */
		private Set<BranchDecisionMeasurementResult> branchDecisionMeasurementResults;

		/**
		 * Creates a visitor for a specific {@link SeffBranch}.
		 *
		 * @param seffBranch The {@link ResourceDemandingInternalAction} where we want to
		 *            know the new measurement results.
		 * @param branchDecisionMeasurementResults The set, where the
		 *            {@link ResourceDemandMeasurementResult} should be added.
		 */
		SeffBranchMeasurementEventVisitor(final SeffBranch seffBranch,
			final Set<BranchDecisionMeasurementResult> branchDecisionMeasurementResults) {
			this.seffBranch = seffBranch;
			this.branchDecisionMeasurementResults = branchDecisionMeasurementResults;
		}

		@Override
		public void visit(final CodeSectionExecutedEvent codeSectionExecutedEvent) {
		}

		@Override
		public void visit(final ResourceDemandCapturedEvent resourceDemandCapturedEvent) {
			// We don't care about them.
		}

		@Override
		public void visit(final ParameterValueCapturedEvent parameterValueCapturedEvent) {
			// We don't care about them.
		}
	}
}
