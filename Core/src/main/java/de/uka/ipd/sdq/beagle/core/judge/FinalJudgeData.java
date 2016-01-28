package de.uka.ipd.sdq.beagle.core.judge;

import de.uka.ipd.sdq.beagle.core.Blackboard;
import de.uka.ipd.sdq.beagle.core.MeasurableSeffElement;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Stores all data of {@link FinalJudge}.
 * 
 * @author Christoph Michelbach
 */
public class FinalJudgeData implements Serializable {

	/**
	 * The millisecond timestamp of the point in time when evolution of evaluable
	 * expressions started or {@code -1} to indicate that this field isn't initialised.
	 */
	private long startTime = -1;

	/**
	 * The number of generations passed. Will be the number of times a {@link FinalJugde}
	 * object received a call to {@link #judge(Blackboard)}.
	 */
	private int numberOfGenerationsPassed;

	/**
	 * The number of generations with significant improvement (see
	 * {@link #SIGNIFICANT_IMPROVEMENT} passed.
	 */
	private int numberOfGenerationsWithoutSignificantImprovementPassed;

	/**
	 * Maps {@link MeasurableSeffElement}s to the fitness value of their best known
	 * evaluable expression.
	 */
	private HashMap<MeasurableSeffElement, Double> currentFitnessValues = new HashMap<MeasurableSeffElement, Double>();

	/**
	 * The latest fitness value which came with good enough improvement to set
	 * {@link #numberOfGenerationsWithoutSignificantImprovementPassed} to {@code 0}.
	 */
	private double fitnessBaselineValue = Double.POSITIVE_INFINITY;

	/**
	 * The latest version of {@link #currentFitnessValues} which came with good enough
	 * improvement to set {@link #numberOfGenerationsWithoutSignificantImprovementPassed}
	 * to {@code 0}.
	 */
	private HashMap<MeasurableSeffElement, Double> fitnessBaseline = new HashMap<MeasurableSeffElement, Double>();

	/**
	 * Returns {@code startTime}.
	 * 
	 * @return The {@code startTime}.
	 */
	public long getStartTime() {
		return this.startTime;
	}

	/**
	 * Sets {@code startTime}.
	 *
	 * @param startTime The {@code startTime} to set.
	 */
	public void setStartTime(final long startTime) {
		this.startTime = startTime;
	}

	/**
	 * Returns {@code numberOfGenerationsPassed}.
	 * 
	 * @return The {@code numberOfGenerationsPassed}.
	 */
	public int getNumberOfGenerationsPassed() {
		return this.numberOfGenerationsPassed;
	}

	/**
	 * Returns {@code numberOfGenerationsPassed}.
	 *
	 * @param numberOfGenerationsPassed The {@code numberOfGenerationsPassed} to set.
	 */
	public void setNumberOfGenerationsPassed(final int numberOfGenerationsPassed) {
		this.numberOfGenerationsPassed = numberOfGenerationsPassed;
	}

	/**
	 * Returns {@code numberOfGenerationsWithoutSignificantImprovementPassed}.
	 * 
	 * @return The {@code numberOfGenerationsWithoutSignificantImprovementPassed}.
	 */
	public int getNumberOfGenerationsWithoutSignificantImprovementPassed() {
		return this.numberOfGenerationsWithoutSignificantImprovementPassed;
	}

	/**
	 * Sets {@code numberOfGenerationsWithoutSignificantImprovementPassed}.
	 *
	 * @param numberOfGenerationsWithoutSignificantImprovementPassed The
	 *            {@code numberOfGenerationsWithoutSignificantImprovementPassed} to set.
	 */
	public void setNumberOfGenerationsWithoutSignificantImprovementPassed(
		final int numberOfGenerationsWithoutSignificantImprovementPassed) {
		this.numberOfGenerationsWithoutSignificantImprovementPassed =
			numberOfGenerationsWithoutSignificantImprovementPassed;
	}

	/**
	 * Returns {@code fitnessBaselineValue}.
	 * 
	 * @return The {@code fitnessBaselineValue}.
	 */
	public double getFitnessBaseline() {
		return this.fitnessBaselineValue;
	}

	/**
	 * Sets {@code fitnessBaselineValue}.
	 *
	 * @param fitnessBaselineValue The {@code fitnessBaselineValue} to set.
	 */
	public void setFitnessBaseline(final double fitnessBaselineValue) {
		this.fitnessBaselineValue = fitnessBaselineValue;
	}

	/**
	 * Returns {@link currentFitnessValues}.
	 * 
	 * @return The {@link currentFitnessValues}.
	 */
	public HashMap<MeasurableSeffElement, Double> getCurrentFitnessValues() {
		return this.currentFitnessValues;
	}
}
