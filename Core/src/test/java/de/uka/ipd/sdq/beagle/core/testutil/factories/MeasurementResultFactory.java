package de.uka.ipd.sdq.beagle.core.testutil.factories;

import static org.mockito.Mockito.mock;

import de.uka.ipd.sdq.beagle.core.measurement.BranchDecisionMeasurementResult;
import de.uka.ipd.sdq.beagle.core.measurement.LoopRepetitionCountMeasurementResult;
import de.uka.ipd.sdq.beagle.core.measurement.ParameterChangeMeasurementResult;
import de.uka.ipd.sdq.beagle.core.measurement.Parameterisation;
import de.uka.ipd.sdq.beagle.core.measurement.ResourceDemandMeasurementResult;

/**
 * Factory for pre-initialised measurement results to be used by tests.
 *
 * @author Joshua Gleitze
 */
public class MeasurementResultFactory {

	/**
	 * Creates new resource demand measurement results.
	 *
	 * @return Some resource demand measurement results.
	 */
	public ResourceDemandMeasurementResult[] getRdiaResults() {
		return new ResourceDemandMeasurementResult[] {
			new ResourceDemandMeasurementResult(1), new ResourceDemandMeasurementResult(10),
			new ResourceDemandMeasurementResult(10045235),
			new ResourceDemandMeasurementResult(4362398623094861984058104358031495d),
			new ResourceDemandMeasurementResult(0.000000000000000000000000000000001),
			new ResourceDemandMeasurementResult(431967146.14323), new ResourceDemandMeasurementResult(134.6161),
			new ResourceDemandMeasurementResult(12343.999999999999999999999),
			new ResourceDemandMeasurementResult(1415.124),
			new ResourceDemandMeasurementResult(mock(Parameterisation.class), 411325),
			new ResourceDemandMeasurementResult(mock(Parameterisation.class), 12571),
			new ResourceDemandMeasurementResult(mock(Parameterisation.class), 1),
			new ResourceDemandMeasurementResult(mock(Parameterisation.class), 145185143341251233d),
			new ResourceDemandMeasurementResult(mock(Parameterisation.class), 3524351.00000000001315),
		};
	}

	/**
	 * Creates new branch decisison measurement results.
	 *
	 * @return Some branch decisison measurement results.
	 */
	public BranchDecisionMeasurementResult[] getBranchResults() {
		return new BranchDecisionMeasurementResult[] {
			new BranchDecisionMeasurementResult(1), new BranchDecisionMeasurementResult(1),
			new BranchDecisionMeasurementResult(1498798725), new BranchDecisionMeasurementResult(0),
			new BranchDecisionMeasurementResult(0), new BranchDecisionMeasurementResult(1564),
			new BranchDecisionMeasurementResult(1), new BranchDecisionMeasurementResult(0),
			new BranchDecisionMeasurementResult(1),
			new BranchDecisionMeasurementResult(mock(Parameterisation.class), 25),
			new BranchDecisionMeasurementResult(mock(Parameterisation.class), 1000),
			new BranchDecisionMeasurementResult(mock(Parameterisation.class), 1145),
			new BranchDecisionMeasurementResult(mock(Parameterisation.class), 3),
			new BranchDecisionMeasurementResult(mock(Parameterisation.class), 1),
		};
	}

	/**
	 * Creates new loop count measurement results.
	 *
	 * @return Some loop count measurement results.
	 */
	public LoopRepetitionCountMeasurementResult[] getLoopResults() {
		return new LoopRepetitionCountMeasurementResult[] {
			new LoopRepetitionCountMeasurementResult(1), new LoopRepetitionCountMeasurementResult(1),
			new LoopRepetitionCountMeasurementResult(1498798725), new LoopRepetitionCountMeasurementResult(2123),
			new LoopRepetitionCountMeasurementResult(654654987), new LoopRepetitionCountMeasurementResult(1564),
			new LoopRepetitionCountMeasurementResult(1), new LoopRepetitionCountMeasurementResult(87971321),
			new LoopRepetitionCountMeasurementResult(1),
			new LoopRepetitionCountMeasurementResult(mock(Parameterisation.class), 25),
			new LoopRepetitionCountMeasurementResult(mock(Parameterisation.class), 1000),
			new LoopRepetitionCountMeasurementResult(mock(Parameterisation.class), 1145),
			new LoopRepetitionCountMeasurementResult(mock(Parameterisation.class), 3),
			new LoopRepetitionCountMeasurementResult(mock(Parameterisation.class), 1),
		};
	}

	/**
	 * Creates new parameter change measurement results.
	 *
	 * @return Some parameter change measurement results.
	 */
	public ParameterChangeMeasurementResult[] getParameterResults() {
		return new ParameterChangeMeasurementResult[] {
			new ParameterChangeMeasurementResult(), new ParameterChangeMeasurementResult(),
			new ParameterChangeMeasurementResult(), new ParameterChangeMeasurementResult(),
			new ParameterChangeMeasurementResult(), new ParameterChangeMeasurementResult(),
			new ParameterChangeMeasurementResult(), new ParameterChangeMeasurementResult(),
			new ParameterChangeMeasurementResult(), new ParameterChangeMeasurementResult(),
			new ParameterChangeMeasurementResult(mock(Parameterisation.class)),
			new ParameterChangeMeasurementResult(mock(Parameterisation.class)),
			new ParameterChangeMeasurementResult(mock(Parameterisation.class)),
			new ParameterChangeMeasurementResult(mock(Parameterisation.class)),
			new ParameterChangeMeasurementResult(mock(Parameterisation.class)),
		};
	}
}
