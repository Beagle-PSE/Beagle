package de.uka.ipd.sdq.beagle.core.measurement;

import de.uka.ipd.sdq.beagle.core.AnalysisController;
import de.uka.ipd.sdq.beagle.core.Blackboard;
import de.uka.ipd.sdq.beagle.core.CodeSection;
import de.uka.ipd.sdq.beagle.core.ExternalCallParameter;
import de.uka.ipd.sdq.beagle.core.LaunchConfiguration;
import de.uka.ipd.sdq.beagle.core.ResourceDemandingInternalAction;
import de.uka.ipd.sdq.beagle.core.SeffBranch;
import de.uka.ipd.sdq.beagle.core.SeffLoop;
import de.uka.ipd.sdq.beagle.core.measurement.order.MeasurementEvent;
import de.uka.ipd.sdq.beagle.core.measurement.order.MeasurementOrder;
import de.uka.ipd.sdq.beagle.core.measurement.order.ParameterCharacteriser;

import org.apache.commons.lang3.Validate;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Controls which measurement tool is working.
 *
 * <p>There is always at most one measurement tool working.
 *
 * @author Roman Langrehr
 * @author Christoph Michelbach
 * @see AnalysisController
 */
public class MeasurementController {

	/**
	 * All {@link MeasurementTool}s this {@link MeasurementController} knows.
	 */
	private Set<MeasurementTool> measurementTools;

	/**
	 * Constructs a new {@link MeasurementController}.
	 *
	 * @param measurementTools The {@link MeasurementTool}s to use. Must not be
	 *            {@code null} and must not contain {@code null}.
	 */
	public MeasurementController(final Set<MeasurementTool> measurementTools) {
		Validate.notNull(measurementTools);
		Validate.noNullElements(measurementTools);

		this.measurementTools = new HashSet<>(measurementTools);
	}

	/**
	 * Determines whether a {@link MeasurementTool} can contribute to the
	 * {@link Blackboard}.
	 *
	 * @param blackboard The blackboard. Must not be {@code null}.
	 * @return Whether a {@link MeasurementTool} can measure something which is marked as
	 *         'to be measured'. When {@code true} is returned, this is no guarantee that
	 *         at least one new measurement result will be added.
	 */
	public boolean canMeasure(final ReadOnlyMeasurementControllerBlackboardView blackboard) {
		Validate.notNull(blackboard);

		int elementsToBeMeasuredInTotal = 0;
		elementsToBeMeasuredInTotal += blackboard.getSeffBranchesToBeMeasured().size();
		elementsToBeMeasuredInTotal += blackboard.getSeffLoopsToBeMeasured().size();
		elementsToBeMeasuredInTotal += blackboard.getRdiasToBeMeasured().size();
		elementsToBeMeasuredInTotal += blackboard.getExternalCallParametersToBeMeasured().size();

		return elementsToBeMeasuredInTotal != 0;
	}

	/**
	 * Instructs all available {@link MeasurementTool MeasurementTools} to measure all
	 * items marked as “to be measured”. {@link MeasurementTool MeasurementTools} may not
	 * produce results for every item but will report results for all items they were able
	 * to measure.
	 *
	 * <p>This method may only be called, when {@link #canMeasure} returned {@code true}
	 * before and the {@link Blackboard} wasn't changed between this call. Otherwise the
	 * behaviour of this method is undefined.
	 *
	 * @param blackboard The blackboard. Must not be {@code null}.
	 */
	public void measure(final MeasurementControllerBlackboardView blackboard) {
		Validate.notNull(blackboard);

		// Read from the blackboard.
		final Set<SeffBranch> seffBranches = blackboard.getSeffBranchesToBeMeasured();
		final Set<SeffLoop> seffLoops = blackboard.getSeffLoopsToBeMeasured();
		final Set<ResourceDemandingInternalAction> rdias = blackboard.getRdiasToBeMeasured();
		final Set<ExternalCallParameter> externalCallParameters = blackboard.getExternalCallParametersToBeMeasured();

		// Initialise everything needed to create a measurement order.
		final Set<CodeSection> resourceDemandSections = new HashSet<CodeSection>();
		final Set<CodeSection> executionSections = new HashSet<CodeSection>();
		final Set<CodeSection> parameterValueSections = new HashSet<CodeSection>();
		final Set<LaunchConfiguration> launchConfigurations = new HashSet<LaunchConfiguration>();
		final ParameterCharacteriser parameterCharacteriser = new ParameterCharacteriser();

		// Fill {@code executionSections}.
		for (final SeffBranch seffBranch : seffBranches) {
			final List<CodeSection> codeSections = seffBranch.getBranches();

			for (final CodeSection codeSection : codeSections) {
				executionSections.add(codeSection);
			}
		}

		// Fill {@code executionSections}.
		for (final SeffLoop seffLoop : seffLoops) {
			final CodeSection codeSection = seffLoop.getLoopBody();
			executionSections.add(codeSection);
		}

		// Fill {@code resourceDemandSections}.
		for (final ResourceDemandingInternalAction rdia : rdias) {
			final CodeSection codeSection = rdia.getAction();
			resourceDemandSections.add(codeSection);
		}

		// Fill {@code parameterValueSection}.
		for (final ExternalCallParameter externalCallParameter : externalCallParameters) {
			final CodeSection codeSection = externalCallParameter.getCallCodeSection();
			parameterValueSections.add(codeSection);
		}

		for (final MeasurementTool measurementTool : this.measurementTools) {
			// Give every measurement tool a measurement order.
			final MeasurementOrder measurementOrder = new MeasurementOrder(parameterValueSections,
				resourceDemandSections, executionSections, blackboard.getProjectInformation(), parameterCharacteriser);

			// Get the measurement results.
			final List<MeasurementEvent> measurementEvents = measurementTool.measure(measurementOrder);

			// Construct a measurement event parser.
			final MeasurementEventParser measurementEventParser = new MeasurementEventParser(measurementEvents);

			// Add the measurement results to the blackboard.
			this.addMeasurementResultsOfSeffBranchesToBlackboard(seffBranches, blackboard, measurementEventParser);
			this.addMeasurementResultsOfSeffLoopsToBlackboard(seffLoops, blackboard, measurementEventParser);
			this.addMeasurementResultsOfRdiasToBlackboard(rdias, blackboard, measurementEventParser);
			this.addMeasurementResultsOfExternalCallParametersToBlackboard(externalCallParameters, blackboard,
				measurementEventParser);
		}
	}

	/**
	 * Adds all measurement results of {@code seffBranches} to the blackboard.
	 *
	 * @param seffBranches The seff branches.
	 * @param blackboard The blackboard.
	 * @param measurementEventParser A measurement result parser.
	 */
	private void addMeasurementResultsOfSeffBranchesToBlackboard(final Set<SeffBranch> seffBranches,
		final MeasurementControllerBlackboardView blackboard, final MeasurementEventParser measurementEventParser) {

		for (final SeffBranch seffBranch : seffBranches) {
			final Set<BranchDecisionMeasurementResult> branchDecisionMeasurementResults =
				measurementEventParser.getMeasurementResultsFor(seffBranch);

			for (final BranchDecisionMeasurementResult branchDecisionMeasurementResult : branchDecisionMeasurementResults) {
				blackboard.addMeasurementResultFor(seffBranch, branchDecisionMeasurementResult);
			}

		}
	}

	/**
	 * Adds all measurement results of {@code seffLoops} to the blackboard.
	 *
	 * @param seffLoops The seff loops.
	 * @param blackboard The blackboard.
	 * @param measurementEventParser A measurement result parser.
	 */
	private void addMeasurementResultsOfSeffLoopsToBlackboard(final Set<SeffLoop> seffLoops,
		final MeasurementControllerBlackboardView blackboard, final MeasurementEventParser measurementEventParser) {

		for (final SeffLoop seffLoop : seffLoops) {
			final Set<LoopRepetitionCountMeasurementResult> loopRepetitionCountMeasurementResults =
				measurementEventParser.getMeasurementResultsFor(seffLoop);
			// @formatter:off
			for (final LoopRepetitionCountMeasurementResult loopRepetitionCountMeasurementResult
				: loopRepetitionCountMeasurementResults) {
				// @formatter:on

				blackboard.addMeasurementResultFor(seffLoop, loopRepetitionCountMeasurementResult);
			}
		}
	}

	/**
	 * Adds all measurement results of {@code rdias} to the blackboard.
	 *
	 * @param rdias The rdias.
	 * @param blackboard The blackboard.
	 * @param measurementEventParser A measurement result parser.
	 */
	private void addMeasurementResultsOfRdiasToBlackboard(final Set<ResourceDemandingInternalAction> rdias,
		final MeasurementControllerBlackboardView blackboard, final MeasurementEventParser measurementEventParser) {
		for (final ResourceDemandingInternalAction rdia : rdias) {
			final Set<ResourceDemandMeasurementResult> resourceDemandMeasurementResults =
				measurementEventParser.getMeasurementResultsFor(rdia);

			for (final ResourceDemandMeasurementResult resourceDemandMeasurementResult : resourceDemandMeasurementResults) {
				blackboard.addMeasurementResultFor(rdia, resourceDemandMeasurementResult);
			}
		}
	}

	/**
	 * Adds all measurement results of {@code externalCallParameters} to the blackboard.
	 *
	 * @param externalCallParameters The external call parameters.
	 * @param blackboard The blackboard.
	 * @param measurementEventParser A measurement result parser.
	 */
	private void addMeasurementResultsOfExternalCallParametersToBlackboard(
		final Set<ExternalCallParameter> externalCallParameters, final MeasurementControllerBlackboardView blackboard,
		final MeasurementEventParser measurementEventParser) {
		for (final ExternalCallParameter externalCallParameter : externalCallParameters) {
			final Set<ParameterChangeMeasurementResult> parameterChangeMeasurementResults =
				measurementEventParser.getMeasurementResultsFor(externalCallParameter);

			for (final ParameterChangeMeasurementResult parameterChangeMeasurementResult : parameterChangeMeasurementResults) {
				blackboard.addMeasurementResultFor(externalCallParameter, parameterChangeMeasurementResult);
			}
		}
	}
}
