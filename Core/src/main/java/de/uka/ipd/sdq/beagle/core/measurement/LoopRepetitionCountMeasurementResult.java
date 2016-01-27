package de.uka.ipd.sdq.beagle.core.measurement;
/**
 * ATTENTION: Test coverage check turned off. Remove this comments block when implementing
 * this class!
 * 
 * <p>COVERAGE:OFF
 */

import org.apache.commons.lang3.Validate;

/**
 * A result of counting repetitions of a loop. It expresses that a loop construct’s body
 * was completely executed this count result’s times when the loop was being started with
 * the given {@code Parameterisation}.
 *
 * @author Joshua Gleitze
 * @author Roman Langrehr
 */
public class LoopRepetitionCountMeasurementResult extends ParameterisationDependentMeasurementResult {

	/**
	 * How many times the loop's body was executed.
	 */
	private int count;

	/**
	 * Creates a result for a loop measurement for which no parameterisation was recorded.
	 *
	 * @param count How many times the loop's body was executed. May not be negative.
	 */
	public LoopRepetitionCountMeasurementResult(final int count) {
		Validate.isTrue(count >= 0, "The measured loop count value was negative: %d", count);
		this.count = count;
	}

	/**
	 * Creates a result for a loop measurement.
	 *
	 * @param parameterisation The state of variables during measurement.
	 * @param count How many times the loop's body was executed. May not be negative.
	 */
	public LoopRepetitionCountMeasurementResult(final Parameterisation parameterisation, final int count) {
		Validate.isTrue(count >= 0, "The measured loop count value was negative: %d", count);
		this.count = count;
	}

	/**
	 * Gets the loop repetition count.
	 *
	 * @return How often the measured loop was executed. A positive Integer or 0.
	 */
	public int getCount() {
		return this.count;
	}
}
