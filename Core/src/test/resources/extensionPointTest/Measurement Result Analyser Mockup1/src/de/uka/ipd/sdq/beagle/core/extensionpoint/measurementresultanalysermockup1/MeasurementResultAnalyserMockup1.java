package de.uka.ipd.sdq.beagle.core.extensionpoint.measurementresultanalysermockup1;

import de.uka.ipd.sdq.beagle.core.analysis.MeasurementResultAnalyser;
import de.uka.ipd.sdq.beagle.core.analysis.MeasurementResultAnalyserBlackboardView;
import de.uka.ipd.sdq.beagle.core.analysis.ReadOnlyMeasurementResultAnalyserBlackboardView;

/**
 * An (empty) implementation of
 * {@link de.uka.ipd.sdq.beagle.core.analysis.MeasurementResultAnalyser}.
 * 
 * @author Michael Vogt
 *
 */
public class MeasurementResultAnalyserMockup1 implements MeasurementResultAnalyser {

	@Override
	public boolean canContribute(final ReadOnlyMeasurementResultAnalyserBlackboardView blackboard) {
		return false;
	}

	@Override
	public void contribute(final MeasurementResultAnalyserBlackboardView blackboard) {
	}

}
