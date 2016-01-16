package de.uka.ipd.sdq.beagle.core;

import de.uka.ipd.sdq.beagle.core.analysis.MeasurementResultAnalyser;
import de.uka.ipd.sdq.beagle.core.analysis.ReadOnlyProposedExpressionAnalyserBlackboardView;
import de.uka.ipd.sdq.beagle.core.analysis.ReadOnlyMeasurementResultAnalyserBlackboardView;
import de.uka.ipd.sdq.beagle.core.analysis.MeasurementResultAnalyserBlackboardView;

/**
 * Implements MeasurementResultAnalyser interface.
 *
 * @author Michael Vogt
 * @see MeasurementResultAnalyser
 */
public abstract class AbstractMeasurementResultAnalyser implements MeasurementResultAnalyser {

    /**
	 *
	 *
	 *
	 */
	public boolean canContribute(ReadOnlyMeasurementResultAnalyserBlackboardView blackboard) {
		//implement method
		return false;
	}

    /**
	 *
	 *
	 *
	 */
	public void contribute(final MeasurementResultAnalyserBlackboardView blackboard) {
		//implement method
	}
}