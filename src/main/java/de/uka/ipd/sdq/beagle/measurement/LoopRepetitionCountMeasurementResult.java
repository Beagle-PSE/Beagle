package de.uka.ipd.sdq.beagle.measurement;

/**
 * A result of counting repetitions of a loop. It expresses that a loop construct’s body
 * was completely executed this count result’s times when the loop was being started with
 * the given {@code Parameterisation}.
 *
 * @author Joshua Gleitze
 */
public class LoopRepetitionCountMeasurementResult extends ParameterisationDependentMeasurementResult {

	/**
	 * Creates a result for a loop measurement for which no parameterisation was recorded.
	 */
	public LoopRepetitionCountMeasurementResult() {
	}

	/**
	 * Creates a result for a loop measurement.
	 *
	 * @param parameterisation The state of variables during measurement.
	 */
	public LoopRepetitionCountMeasurementResult(final Parameterisation parameterisation) {
	}

	/**
	 * Gets the loop repetition count.
	 *
	 * @return How often the measured loop was executed. A positive Integer or 0.
	 */
	public int getCount() {
		return 0;
	}

	/**
	 * Sets the loop repetition count.
	 *
	 * @param count How often the measured loop was executed. Must not be smaller than 0.
	 * @throws IllegalArgumentException If {@code count < 0}.
	 */
	public void setCount(final int count) {
	}
}
