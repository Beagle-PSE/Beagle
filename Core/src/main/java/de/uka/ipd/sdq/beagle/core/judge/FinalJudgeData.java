package de.uka.ipd.sdq.beagle.core.judge;

import org.apache.commons.collections4.MultiSet;
import org.apache.commons.collections4.multiset.HashMultiSet;

import java.io.Serializable;
import java.util.stream.DoubleStream;

/**
 * Stores all data of {@link FinalJudge}.
 *
 * @author Christoph Michelbach
 */
class FinalJudgeData implements Serializable {

	/**
	 * serialVersionUID to make sure serialisation works fine.
	 */
	private static final long serialVersionUID = 9038094699907524618L;

	/**
	 * The number of generations with significant improvement (see
	 * {@code FinalJugde#SIGNIFICANT_IMPROVEMENT} passed.
	 */
	private int numberOfGenerationsWithoutSignificantImprovementPassed;

	/**
	 * Contains the fittest value of each seff element (no mapping needed).
	 */
	private MultiSet<Double> fittestValues;

	/**
	 * The latest fitness value which came with good enough improvement to set
	 * {@code numberOfGenerationsWithoutSignificantImprovementPassed} to {@code 0}.
	 */
	private double fitnessBaselineValue = Double.POSITIVE_INFINITY;

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
	public double getFitnessBaselineValue() {
		return this.fitnessBaselineValue;
	}

	/**
	 * Announces that a new generation is being judged. Increments the number of passed
	 * generations and sets the fittest values to an empty set.
	 */
	public void newGeneration() {
		this.fittestValues = new HashMultiSet<>();
	}

	/**
	 * Sets {@code fitnessBaselineValue}.
	 *
	 * @param fitnessBaselineValue The {@code fitnessBaselineValue} to set.
	 */
	public void setFitnessBaselineValue(final double fitnessBaselineValue) {
		this.fitnessBaselineValue = fitnessBaselineValue;
	}

	/**
	 * Adds {@code value} to the collection of the fittest value.
	 *
	 * @param value The fittest value of a seff element.
	 */
	public void addFittestValue(final double value) {
		this.fittestValues.add(value);
	}

	/**
	 * Returns the fittest values of this generation.
	 *
	 * @return The fittness values of the fittest proposed expressions of the momentary
	 *         generation. The stream will contain as many {@code double}s as there are
	 *         seff elements on the blackboard.
	 */
	public DoubleStream getFittestValues() {
		return this.fittestValues.stream().mapToDouble(value -> value);
	}

	/**
	 * Returns whether {@link FinalJudge#judge(de.uka.ipd.sdq.beagle.core.Blackboard)}
	 * will return {@code true} for sure.
	 *
	 * @return {@code true} if
	 *         {@link FinalJudge#judge(de.uka.ipd.sdq.beagle.core.Blackboard)} will return
	 *         {@code true} for sure; {@code false} otherwise.
	 */
	public boolean isWillReturnTrue() {
		return this.willReturnTrue;
	}

	/**
	 * Sets that {@link FinalJudge#judge(de.uka.ipd.sdq.beagle.core.Blackboard)} will
	 * return {@code true} for sure.
	 */
	public void setWillReturnTrue() {
		this.willReturnTrue = true;
	}
}
