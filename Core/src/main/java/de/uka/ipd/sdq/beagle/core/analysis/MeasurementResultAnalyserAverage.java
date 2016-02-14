package de.uka.ipd.sdq.beagle.core.analysis;

import de.uka.ipd.sdq.beagle.core.Blackboard;
import de.uka.ipd.sdq.beagle.core.BlackboardStorer;
import de.uka.ipd.sdq.beagle.core.ExternalCallParameter;
import de.uka.ipd.sdq.beagle.core.MeasurableSeffElement;
import de.uka.ipd.sdq.beagle.core.ResourceDemandingInternalAction;
import de.uka.ipd.sdq.beagle.core.SeffBranch;
import de.uka.ipd.sdq.beagle.core.SeffLoop;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.ConstantExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpression;
import de.uka.ipd.sdq.beagle.core.measurement.LoopRepetitionCountMeasurementResult;
import de.uka.ipd.sdq.beagle.core.measurement.ResourceDemandMeasurementResult;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * This implementation of {@link MeasurementResultAnalyser} will contribute with static
 * created ProposedExpressions. It will take all SeffElements and use respectively
 * all their MeasurementResults on the {@link Blackboard} and calculate thereby an
 * expression that fits best to the average expectancy value. Its implementation of
 * {@link #canContribute(ReadOnlyMeasurementResultAnalyserBlackboardView)} checks if there
 * are any MeasurementResults for a SeffElement, that have not been flown into the average
 * proposed Expression so far.
 * 
 * 
 * @author Ansgar Spiegler
 */
public class MeasurementResultAnalyserAverage
	implements MeasurementResultAnalyser, BlackboardStorer<HashMap<MeasurableSeffElement, Integer>> {

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

			final Integer numberOfMeasurements =
				this.numberOfMeasurementResultsForSeffElement(blackboard, measurableSeffElement);

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
	 * Scanning all {@link MeasurableSeffElement} for given MeasurementResults. If there
	 * are more results than the number of results for a previous propose have been used,
	 * a new ProposedExpression is added by
	 * {@link #addMeasurementResultFor(MeasurementResultAnalyserBlackboardView, MeasurableSeffElement)}
	 *
	 * @param blackboard the {@link Blackboard} to read and write from
	 */
	private void scanAllMeasurableSeffElements(final MeasurementResultAnalyserBlackboardView blackboard) {

		final HashMap<MeasurableSeffElement, Integer> measurableSeffContributions =
			blackboard.readFor(MeasurementResultAnalyserAverage.class);

		for (MeasurableSeffElement measurableSeffElement : measurableSeffContributions.keySet()) {

			Integer numberOfMeasurements;

			if (measurableSeffElement.getClass() == ResourceDemandingInternalAction.class) {
				numberOfMeasurements =
					blackboard.getMeasurementResultsFor((ResourceDemandingInternalAction) measurableSeffElement).size();
				if (measurableSeffContributions.get(measurableSeffElement) < numberOfMeasurements) {
					this.addMeasurementResultFor(blackboard, (ResourceDemandingInternalAction) measurableSeffElement);
				}
			} else if (measurableSeffElement.getClass() == SeffBranch.class) {
				numberOfMeasurements = blackboard.getMeasurementResultsFor((SeffBranch) measurableSeffElement).size();
				if (measurableSeffContributions.get(measurableSeffElement) < numberOfMeasurements) {
					this.addMeasurementResultFor(blackboard, (SeffBranch) measurableSeffElement);
				}
			} else if (measurableSeffElement.getClass() == SeffLoop.class) {
				numberOfMeasurements = blackboard.getMeasurementResultsFor((SeffLoop) measurableSeffElement).size();
				if (measurableSeffContributions.get(measurableSeffElement) < numberOfMeasurements) {
					this.addMeasurementResultFor(blackboard, (SeffLoop) measurableSeffElement);
				}
			} else if (measurableSeffElement.getClass() == ExternalCallParameter.class) {
				numberOfMeasurements =
					blackboard.getMeasurementResultsFor((ExternalCallParameter) measurableSeffElement).size();
				if (measurableSeffContributions.get(measurableSeffElement) < numberOfMeasurements) {
					this.addMeasurementResultFor(blackboard, (ExternalCallParameter) measurableSeffElement);
				}
			}

		}

	}

	/**
	 * This method creates an averaging {@link EvaluableExpression} over all given
	 * measurement results for this {@link MeasurableSeffElement} and adds it to the given
	 * {@link Blackboard}. At the end, this test MUST Call
	 * {@link #mapCurrentNumberOfMeasurementResultsToSeffElement(MeasurementResultAnalyserBlackboardView, MeasurableSeffElement)}
	 * so that this class knows, that is has contributed. Mistakes may cause an endless
	 * running behavior of Beagle!
	 *
	 * @param blackboard To read and write from
	 * @param rdia MeasurableSeff for which this method should calculate the average
	 *            {@link EvaluableExpression}
	 */
	private void addMeasurementResultFor(final MeasurementResultAnalyserBlackboardView blackboard,
		final ResourceDemandingInternalAction rdia) {

		final Set<ResourceDemandMeasurementResult> measurementResults = blackboard.getMeasurementResultsFor(rdia);
		int sumOverAllMeasurementResultValues = 0;
		final int numberOfAllMeasurementResults = measurementResults.size();

		for (ResourceDemandMeasurementResult rdmr : measurementResults) {
			sumOverAllMeasurementResultValues += rdmr.getValue();
		}

		final ConstantExpression expression =
			ConstantExpression.forValue((double) sumOverAllMeasurementResultValues / numberOfAllMeasurementResults);
		blackboard.addProposedExpressionFor(rdia, expression);
		this.mapCurrentNumberOfMeasurementResultsToSeffElement(blackboard, rdia);
	}

	/**
	 * Note: Not yet implemented. This method creates an averaging
	 * {@link EvaluableExpression} over all given measurement results for this
	 * {@link MeasurableSeffElement} and adds it to the given {@link Blackboard}. At the
	 * end, this test MUST Call
	 * {@link #mapCurrentNumberOfMeasurementResultsToSeffElement(MeasurementResultAnalyserBlackboardView, MeasurableSeffElement)}
	 * so that this class knows, that is has contributed. Mistakes may cause an endless
	 * running behavior of Beagle!
	 *
	 * @param blackboard To read and write from
	 * @param seffBranch MeasurableSeff for which this method should calculate the average
	 *            {@link EvaluableExpression}
	 */
	private void addMeasurementResultFor(final MeasurementResultAnalyserBlackboardView blackboard,
		final SeffBranch seffBranch) {

		// final Set<BranchDecisionMeasurementResult> measurementResults =
		// blackboard.getMeasurementResultsFor(seffBranch);
		// final int numberOfBranchDecisions[] = new int[seffBranch.getBranches().size()];
		// final int numberOfAllMeasurementResults = measurementResults.size();

		// for (BranchDecisionMeasurementResult bdmr : measurementResults) {
		// numberOfBranchDecisions[bdmr.getBranchIndex()]++;
		// }

		// HERE SHOULD THE EVALUABLEEXPRESSION FOR BRANCHES BE CREATED AND ADDED TO THE
		// BLACKBOARD

		this.mapCurrentNumberOfMeasurementResultsToSeffElement(blackboard, seffBranch);
	}

	/**
	 * This method creates an averaging {@link EvaluableExpression} over all given
	 * measurement results for this {@link MeasurableSeffElement} and adds it to the given
	 * {@link Blackboard}. At the end, this test MUST Call
	 * {@link #mapCurrentNumberOfMeasurementResultsToSeffElement(MeasurementResultAnalyserBlackboardView, MeasurableSeffElement)}
	 * so that this class knows, that is has contributed. Mistakes may cause an endless
	 * running behavior of Beagle!
	 *
	 * @param blackboard To read and write from
	 * @param seffLoop MeasurableSeff for which this method should calculate the average
	 *            {@link EvaluableExpression}
	 */
	private void addMeasurementResultFor(final MeasurementResultAnalyserBlackboardView blackboard,
		final SeffLoop seffLoop) {

		final Set<LoopRepetitionCountMeasurementResult> loopCountResults =
			blackboard.getMeasurementResultsFor(seffLoop);
		int sumOverAllLoopCounts = 0;
		final int numberOfAllMeasurementResults = loopCountResults.size();

		for (LoopRepetitionCountMeasurementResult loopCountResult : loopCountResults) {
			sumOverAllLoopCounts += loopCountResult.getCount();
		}

		final ConstantExpression expression =
			ConstantExpression.forValue((double) sumOverAllLoopCounts / numberOfAllMeasurementResults);
		blackboard.addProposedExpressionFor(seffLoop, expression);
		this.mapCurrentNumberOfMeasurementResultsToSeffElement(blackboard, seffLoop);
	}

	/**
	 * Note: Not yet implemented. This method creates an averaging This method creates an
	 * averaging {@link EvaluableExpression} over all given measurement results for this
	 * {@link MeasurableSeffElement} and adds it to the given {@link Blackboard}. At the
	 * end, this test MUST Call
	 * {@link #mapCurrentNumberOfMeasurementResultsToSeffElement(MeasurementResultAnalyserBlackboardView, MeasurableSeffElement)}
	 * so that this class knows, that is has contributed. Mistakes may cause an endless
	 * running behavior of Beagle!
	 *
	 * @param blackboard To read and write from
	 * @param exParam MeasurableSeff for which this method should calculate the average
	 *            {@link EvaluableExpression}
	 */
	private void addMeasurementResultFor(final MeasurementResultAnalyserBlackboardView blackboard,
		final ExternalCallParameter exParam) {

		this.mapCurrentNumberOfMeasurementResultsToSeffElement(blackboard, exParam);
	}

	/**
	 * This method is very important to be called right after this Analyser-class has
	 * contributed. It makes the annotation on its blackboard stored HashMap, that he
	 * contributed for a {@link MeasurableSeffElement} with a specific number of
	 * MeasurementResults that have flown into its ProposedExpression.
	 *
	 * @param blackboard To read and write from
	 * @param measurableSeffElement MeasurableSeff for which a ProposedExpression has been
	 *            added
	 */
	private void mapCurrentNumberOfMeasurementResultsToSeffElement(
		final MeasurementResultAnalyserBlackboardView blackboard, final MeasurableSeffElement measurableSeffElement) {

		final HashMap<MeasurableSeffElement, Integer> measurableSeffContributions =
			blackboard.readFor(MeasurementResultAnalyserAverage.class);
		measurableSeffContributions.put(measurableSeffElement,
			new Integer(this.numberOfMeasurementResultsForSeffElement(blackboard, measurableSeffElement)));
	}

	/**
	 * Returning the number of MeasurementResults for a given
	 * {@link MeasurableSeffElement}.
	 *
	 * @param blackboard the blackboard to read from
	 * @param measurableSeffElement Checking the MeasruementResults for this blackboard
	 *            contained element
	 * @return number of MeasurementResults
	 */
	private int numberOfMeasurementResultsForSeffElement(
		final ReadOnlyMeasurementResultAnalyserBlackboardView blackboard,
		final MeasurableSeffElement measurableSeffElement) {
		int numberOfMeasurements = 0;

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

		return numberOfMeasurements;
	}

	/**
	 * Returning the number of MeasurementResults for a given
	 * {@link MeasurableSeffElement}.
	 *
	 * @param blackboard the blackboard to read from
	 * @param measurableSeffElement Checking the MeasruementResults for this blackboard
	 *            contained element
	 * @return number of MeasurementResults
	 */
	private int numberOfMeasurementResultsForSeffElement(final MeasurementResultAnalyserBlackboardView blackboard,
		final MeasurableSeffElement measurableSeffElement) {
		int numberOfMeasurements = 0;

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

		return numberOfMeasurements;
	}

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
