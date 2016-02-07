package de.uka.ipd.sdq.beagle.core.measurement;

import de.uka.ipd.sdq.beagle.core.CodeSection;
import de.uka.ipd.sdq.beagle.core.ExternalCallParameter;
import de.uka.ipd.sdq.beagle.core.MeasurableSeffElement;
import de.uka.ipd.sdq.beagle.core.ResourceDemandingInternalAction;
import de.uka.ipd.sdq.beagle.core.SeffBranch;
import de.uka.ipd.sdq.beagle.core.SeffLoop;
import de.uka.ipd.sdq.beagle.core.measurement.order.CodeSectionEnteredEvent;
import de.uka.ipd.sdq.beagle.core.measurement.order.CodeSectionLeftEvent;
import de.uka.ipd.sdq.beagle.core.measurement.order.MeasurementEvent;
import de.uka.ipd.sdq.beagle.core.measurement.order.MeasurementEventVisitor;
import de.uka.ipd.sdq.beagle.core.measurement.order.ParameterValueCapturedEvent;
import de.uka.ipd.sdq.beagle.core.measurement.order.ResourceDemandCapturedEvent;

import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.lang3.Validate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

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
	private final List<MeasurementEvent> measurementEvents;

	/**
	 * Maps for each code sections to all occurrences of measurement events for this code
	 * section in {@link #measurementEvents}.
	 */
	private Map<CodeSection, Set<Integer>> codeSectionMapping = new HashMap<>();

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
		this.measurementEvents = IteratorUtils.toList(events.iterator());
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
		final Set<Integer> indices = this.codeSectionMapping.get(resourceDemandingInternalAction.getAction());
		if (indices != null) {
			for (final Integer index : indices) {
				final MeasurementEvent measurementEvent = this.measurementEvents.get(index);
				measurementEvent.receive(resourceDemandingInternalActionMeasurementEventVisitor);
			}
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

		// Assemble all measurement events, that could fit for this branch according to
		// their code section.
		final Set<MeasurementEvent> codeSectionEventsForThisBranch = new HashSet<>();
		for (final CodeSection possibilyPickedBranch : branch.getBranches()) {
			final Set<Integer> indices = this.codeSectionMapping.get(possibilyPickedBranch);
			if (indices != null) {
				for (final Integer index : indices) {
					codeSectionEventsForThisBranch.add(this.measurementEvents.get(index));
				}
			}
		}

		final SeffBranchMeasurementEventVisitor seffBranchMeasurementEventVisitor =
			new SeffBranchMeasurementEventVisitor(branch, branchDecisionMeasurementResults);
		for (final MeasurementEvent measurementEvent : codeSectionEventsForThisBranch) {
			measurementEvent.receive(seffBranchMeasurementEventVisitor);
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
		final Set<LoopRepetitionCountMeasurementResult> loopRepetitionCountMeasurementResults = new HashSet<>();
		final SeffLoopMeasurementEventVisitor seffLoopMeasurementEventVisitor =
			new SeffLoopMeasurementEventVisitor(loop, loopRepetitionCountMeasurementResults);
		final Set<Integer> indices = this.codeSectionMapping.get(loop.getLoopBody());
		if (indices != null) {
			for (final Integer index : indices) {
				this.measurementEvents.get(index).receive(seffLoopMeasurementEventVisitor);
			}
		}
		seffLoopMeasurementEventVisitor.finalise();
		return loopRepetitionCountMeasurementResults;
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
		return new HashSet<>();
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
		private final ResourceDemandingInternalAction resourceDemandingInternalAction;

		/**
		 * The set, where the {@link ResourceDemandMeasurementResult} should be added.
		 */
		private final Set<ResourceDemandMeasurementResult> resourceDemandMeasurementResults;

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
		public void visit(final CodeSectionEnteredEvent codeSectionExecutedEvent) {
			// We don't care about them.
		}

		@Override
		public void visit(final CodeSectionLeftEvent codeSectionLeftEvent) {
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
	 *
	 * @author Roman Langrehr
	 */
	private class SeffBranchMeasurementEventVisitor implements MeasurementEventVisitor {

		/**
		 * The {@link SeffBranch} where we want to know the new measurement results.
		 */
		private final SeffBranch branch;

		/**
		 * The set, where the {@linkplain ResourceDemandMeasurementResult
		 * ResourceDemandMeasurementResults} should be added.
		 */
		private final Set<BranchDecisionMeasurementResult> branchDecisionMeasurementResults;

		/**
		 * Creates a visitor for a specific {@link SeffBranch}.
		 *
		 * @param branch The {@link SeffBranch} where we want to know the new measurement
		 *            results.
		 * @param branchDecisionMeasurementResults The set, where the
		 *            {@linkplain BranchDecisionMeasurementResult
		 *            BranchDecisionMeasurementResults} should be added.
		 */
		SeffBranchMeasurementEventVisitor(final SeffBranch branch,
			final Set<BranchDecisionMeasurementResult> branchDecisionMeasurementResults) {
			this.branch = branch;
			this.branchDecisionMeasurementResults = branchDecisionMeasurementResults;
		}

		@Override
		public void visit(final CodeSectionEnteredEvent codeSectionExecutedEvent) {
			final int branchIndex = this.branch.getBranches().indexOf(codeSectionExecutedEvent.getCodeSection());
			if (branchIndex != -1) {
				this.branchDecisionMeasurementResults.add(new BranchDecisionMeasurementResult(branchIndex));
			}
		}

		@Override
		public void visit(final CodeSectionLeftEvent codeSectionLeftEvent) {
			// We don't care about them, because we defined a SeffBranch to bexecuted,
			// exactly when it was entered.
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

	/**
	 * A {@link MeasurementEventVisitor} for a specific {@link SeffLoop}.
	 *
	 * <p>You must call {@linkplain MeasurementEvent#receive(MeasurementEventVisitor)
	 * receive} on the {@linkplain MeasurementEvent MeasurementEvents} in the correct
	 * order!
	 *
	 * @author Roman Langrehr
	 */
	private class SeffLoopMeasurementEventVisitor implements MeasurementEventVisitor {

		/**
		 * The {@link SeffBranch} where we want to know the new measurement results.
		 */
		private final SeffLoop loop;

		/**
		 * The set, where the {@linkplain ResourceDemandMeasurementResult
		 * ResourceDemandMeasurementResults} should be added.
		 */
		private final Set<LoopRepetitionCountMeasurementResult> loopRepetitionCountMeasurementResults;

		/**
		 * The number of loops measured in the current sequence of measurement events for
		 * one loop body. The stack contains all
		 */
		private Stack<LoopExecutionCounter> currentLoopCounts;

		/**
		 * Creates a visitor for a specific {@link SeffLoop}.
		 *
		 * @param loop The {@link SeffLoop} where we want to know the new measurement
		 *            results.
		 * @param loopRepetitionCountMeasurementResults The set, where the
		 *            {@linkplain BranchDecisionMeasurementResult
		 *            BranchDecisionMeasurementResults} should be added.
		 */
		SeffLoopMeasurementEventVisitor(final SeffLoop loop,
			final Set<LoopRepetitionCountMeasurementResult> loopRepetitionCountMeasurementResults) {
			this.loop = loop;
			this.loopRepetitionCountMeasurementResults = loopRepetitionCountMeasurementResults;
			this.currentLoopCounts = new Stack<>();
		}

		@Override
		public void visit(final CodeSectionEnteredEvent codeSectionEnteredEvent) {
			if (codeSectionEnteredEvent.getCodeSection().equals(this.loop.getLoopBody())) {
				if (this.currentLoopCounts.isEmpty() || this.currentLoopCounts.peek().isOpen) {
					// The current branch was not finished before a this, so we have a
					// recursive call, or there is no current execution of this loop.
					this.currentLoopCounts.push(new LoopExecutionCounter());
				}
				assert !this.currentLoopCounts.peek().isOpen;
				if (!(this.currentLoopCounts.peek().lastCodeSectionLeftEventIndex == -1
					|| this.currentLoopCounts.peek().lastCodeSectionLeftEventIndex
						+ 1 == MeasurementEventParser.this.measurementEvents.indexOf(codeSectionEnteredEvent))) {
					// We have not continous loop body executions, so we create a new
					// execution event.
					this.loopFinished();
					this.currentLoopCounts.push(new LoopExecutionCounter());
				}
				this.currentLoopCounts.peek().numberOfExecutions++;
				this.currentLoopCounts.peek().isOpen = true;
			}
		}

		@Override
		public void visit(final CodeSectionLeftEvent codeSectionLeftEvent) {
			if (codeSectionLeftEvent.getCodeSection().equals(this.loop.getLoopBody())) {
				if (!this.currentLoopCounts.isEmpty()) {
					if (this.currentLoopCounts.peek().isOpen) {
						// The current execution is finished.
						this.currentLoopCounts.peek().isOpen = false;
						this.currentLoopCounts.peek().lastCodeSectionLeftEventIndex =
							MeasurementEventParser.this.measurementEvents.indexOf(codeSectionLeftEvent);
					} else {
						// The current execution is already finished, so we need to close
						// the one "below" that.

						// The actual closing
						this.loopFinished();

						// The invariant for this stack
						assert this.currentLoopCounts.peek().isOpen;
						// allows us to just close the next layer.
						this.currentLoopCounts.peek().isOpen = false;
						this.currentLoopCounts.peek().lastCodeSectionLeftEventIndex =
							MeasurementEventParser.this.measurementEvents.indexOf(codeSectionLeftEvent);
					}
				}
				// If the currentLoopCounts stack is empty, we have a CodeSectionLeftEvent
				// event without an CodeSectionEnteredEvent and ignore this.
			}
		}

		@Override
		public void visit(final ResourceDemandCapturedEvent resourceDemandCapturedEvent) {
			// We don't care about them.
		}

		@Override
		public void visit(final ParameterValueCapturedEvent parameterValueCapturedEvent) {
			// We don't care about them.
		}

		/**
		 * This method must be called after the last measurement event was visited,
		 * because otherwise the resulting {@code loopRepetitionCountMeasurementResults}
		 * may be incomplete.
		 */
		public void finalise() {
			// Close all open executions. There {@link CodeSectionLeftEvent} is missing.
			while (!this.currentLoopCounts.isEmpty()) {
				this.loopFinished();
			}
		}

		/**
		 * Pops the top of {@link #currentLoopCounts} and adds the
		 * {@link LoopRepetitionCountMeasurementResult} for this execution.
		 */
		private void loopFinished() {
			this.loopRepetitionCountMeasurementResults
				.add(new LoopRepetitionCountMeasurementResult(this.currentLoopCounts.pop().numberOfExecutions));
		}

		/**
		 * Container for the information needed to save the state of an loop execution.
		 *
		 * @author Roman Langrehr
		 */
		private class LoopExecutionCounter {

			/**
			 * How many times the loop was executed.
			 */
			private int numberOfExecutions;

			/**
			 * Whether the last execution of the loop body was not finished. (An
			 * {@link CodeSectionEnteredEvent} was parsed, but not the corresponding
			 * {@link CodeSectionLeftEvent}.
			 */
			private boolean isOpen;

			/**
			 * If {@link #isOpen} is {@code false}, this field is the index of the last
			 * CodeSectionLeftEvent of an loop body for this branch or {@code -1}, if no
			 * CodeSectionLeftEvent was parsed for this loop execution.
			 */
			private int lastCodeSectionLeftEventIndex;

			/**
			 * Creates a new, empty LoopExecutionCounter.
			 */
			LoopExecutionCounter() {
				this.numberOfExecutions = 0;
				this.isOpen = false;
				this.lastCodeSectionLeftEventIndex = -1;
			}
		}
	}
}
