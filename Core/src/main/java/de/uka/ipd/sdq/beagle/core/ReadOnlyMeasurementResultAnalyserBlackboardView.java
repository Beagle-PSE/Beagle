package de.uka.ipd.sdq.beagle.core;

import de.uka.ipd.sdq.beagle.core.analysis.MeasurementResultAnalyser;
import de.uka.ipd.sdq.beagle.core.measurement.BranchDecisionMeasurementResult;
import de.uka.ipd.sdq.beagle.core.measurement.LoopRepetitionCountMeasurementResult;
import de.uka.ipd.sdq.beagle.core.measurement.ResourceDemandMeasurementResult;

/**
 * Read-only view of the {@link Blackboard} designed to be used by
 * {@link MeasurementResultAnalyser}. It allows reading access and adding access for
 * {@linkplain ResourceDemandMeasurementResult ResourceDemandMeasurementResults},
 * {@linkplain BranchDecisionMeasurementResult BranchDecisionMeasurementResults} and
 * {@linkplain LoopRepetitionCountMeasurementResult LoopRepetitionCountMeasurementResults}
 * .
 *
 * @author Christoph Michelbach
 */
public class ReadOnlyMeasurementResultAnalyserBlackboardView {

}
