package de.uka.ipd.sdq.beagle.core;

/**
 * The current state of the analysis.
 *
 * @author Christoph Michelbach
 */
public enum AnalysisState {
	/**
	 * The analysis is currently running.
	 */
	RUNNING,

	/**
	 * Stopping the analysis without trying to preserve data accumulated in the current
	 * phase.
	 */
	ABORTING,

	/**
	 * Stopping the analysis but trying to preserve data accumulated in the current phase.
	 */
	ENDING,

	/**
	 * The analysis isn't running.
	 */
	TERMINATED
}
