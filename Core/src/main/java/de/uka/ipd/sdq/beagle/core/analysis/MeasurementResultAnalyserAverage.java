package de.uka.ipd.sdq.beagle.core.analysis;

import de.uka.ipd.sdq.beagle.core.Blackboard;
import de.uka.ipd.sdq.beagle.core.BlackboardStorer;
import de.uka.ipd.sdq.beagle.core.ExternalCallParameter;
import de.uka.ipd.sdq.beagle.core.MeasurableSeffElement;
import de.uka.ipd.sdq.beagle.core.ResourceDemandingInternalAction;
import de.uka.ipd.sdq.beagle.core.SeffBranch;
import de.uka.ipd.sdq.beagle.core.SeffLoop;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * This implementation of {@link MeasurementResultAnalyser} will contribute
 * 
 * 
 * @author Ansgar Spiegler
 */
public class MeasurementResultAnalyserAverage implements MeasurementResultAnalyser, BlackboardStorer<HashMap<MeasurableSeffElement, Integer>> {

	@Override
	public boolean canContribute(final ReadOnlyMeasurementResultAnalyserBlackboardView blackboard) {

		final HashMap<MeasurableSeffElement, Integer> measurableSeffContributions =
			blackboard.readFor(MeasurementResultAnalyserAverage.class);

		if (measurableSeffContributions == null) {
			return true;
		}

		if (measurableSeffContributions.isEmpty()) {
			return false;
		}

		for (MeasurableSeffElement measurableSeffElement : measurableSeffContributions.keySet()) {

			Integer numberOfMeasurements = 0;

			if (measurableSeffElement.getClass() == ResourceDemandingInternalAction.class) {
				numberOfMeasurements =
					blackboard.getMeasurementResultsFor((ResourceDemandingInternalAction) measurableSeffElement).size();
			} else if (measurableSeffElement.getClass() == SeffBranch.class) {
				numberOfMeasurements = blackboard.getMeasurementResultsFor((SeffBranch) measurableSeffElement).size();
			} else if (measurableSeffElement.getClass() == SeffLoop.class) {
				numberOfMeasurements = blackboard.getMeasurementResultsFor((SeffLoop) measurableSeffElement).size();
			} else if (measurableSeffElement.getClass() == ExternalCallParameter.class) {
				numberOfMeasurements =
					blackboard.getMeasurementResultsFor((ExternalCallParameter) measurableSeffElement).size();
			}

			if (measurableSeffContributions.get(measurableSeffElement) < numberOfMeasurements) {
				return true;
			}
		}

		return false;
	}

	@Override
	public void contribute(final MeasurementResultAnalyserBlackboardView blackboard) {

		final HashMap<MeasurableSeffElement, Integer> writtenContent =
			blackboard.readFor(MeasurementResultAnalyserAverage.class);

		if (writtenContent == null) {
			final HashMap<MeasurableSeffElement, Integer> measurableSeffContributions = this.initializeMap(blackboard);
			blackboard.writeFor(MeasurementResultAnalyserAverage.class, measurableSeffContributions);
		}

		this.scanAllMeasurableSeffElements(blackboard);

	}

	/**
	 * Javadoc.
	 *
	 * @param blackboard blackboard
	 */
	private void scanAllMeasurableSeffElements(final MeasurementResultAnalyserBlackboardView blackboard) {
		blackboard.add

		final HashMap<MeasurableSeffElement, Integer> measurableSeffContributions =
			blackboard.readFor(MeasurementResultAnalyserAverage.class);
		
		for (MeasurableSeffElement measurableSeffElement : measurableSeffContributions.keySet()) {

			Integer numberOfMeasurements = measurableSeffContributions.get(measurableSeffElement);
			
			
			if (measurableSeffElement.getClass() == ResourceDemandingInternalAction.class) {
				numberOfMeasurements =
					blackboard.getMeasurementResultsFor((ResourceDemandingInternalAction) measurableSeffElement).size();
				if (measurableSeffContributions.get(measurableSeffElement) < numberOfMeasurements) {
					this.
				}
			} else if (measurableSeffElement.getClass() == SeffBranch.class) {
				numberOfMeasurements = blackboard.getMeasurementResultsFor((SeffBranch) measurableSeffElement).size();
			} else if (measurableSeffElement.getClass() == SeffLoop.class) {
				numberOfMeasurements = blackboard.getMeasurementResultsFor((SeffLoop) measurableSeffElement).size();
			} else if (measurableSeffElement.getClass() == ExternalCallParameter.class) {
				numberOfMeasurements =
					blackboard.getMeasurementResultsFor((ExternalCallParameter) measurableSeffElement).size();
			}


			blackboard.getM
		}
		
	}

	private void addMeasurementResultFor()

	/**
	 * This will create a {@link HashMap}, mapping all {@link MeasurableSeffElement} to
	 * the Integer value 0.
	 *
	 * @param blackboard The {@link Blackboard} to get the SeffElements from.
	 * @return A Map of all blackboard contained SeffElements to Integer value 0.
	 */
	private HashMap<MeasurableSeffElement, Integer> initializeMap(
		final MeasurementResultAnalyserBlackboardView blackboard) {
		final HashMap<MeasurableSeffElement, Integer> measurableSeffContributions =
			new HashMap<MeasurableSeffElement, Integer>();
		final Set<MeasurableSeffElement> allMeasurableSeffElementsOnBlackboard = new HashSet<MeasurableSeffElement>();

		for (MeasurableSeffElement measurableSeffElement : blackboard.getAllRdias()) {
			allMeasurableSeffElementsOnBlackboard.add(measurableSeffElement);
		}

		for (MeasurableSeffElement measurableSeffElement : blackboard.getAllSeffBranches()) {
			allMeasurableSeffElementsOnBlackboard.add(measurableSeffElement);
		}

		for (MeasurableSeffElement measurableSeffElement : blackboard.getAllSeffLoops()) {
			allMeasurableSeffElementsOnBlackboard.add(measurableSeffElement);
		}

		for (MeasurableSeffElement measurableSeffElement : blackboard.getAllExternalCallParameters()) {
			allMeasurableSeffElementsOnBlackboard.add(measurableSeffElement);
		}

		for (MeasurableSeffElement measurableSeffElement : allMeasurableSeffElementsOnBlackboard) {
			measurableSeffContributions.put(measurableSeffElement, new Integer(0));
		}

		return measurableSeffContributions;
	}

}
