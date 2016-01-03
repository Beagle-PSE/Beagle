package de.uka.ipd.sdq.beagle.measurement;

/**
 * A measurement result that may depend on the state of variables while executing the
 * measured code section. That a result is a ParameterisationDependentMeasurementResult
 * does not imply that the {@link Parameterisation} was actually recorded while measuring
 * it.
 *
 * @author Joshua Gleitze
 */
public abstract class ParameterisationDependentMeasurementResult {

	/**
	 * Creates a result for a code section measurement for which no parameterisation was
	 * recorded.
	 */
	public ParameterisationDependentMeasurementResult() {
	}

	/**
	 * Creates a result for a code section measurement.
	 *
	 * @param parameterisation The state of variables during the measurement.
	 */
	public ParameterisationDependentMeasurementResult(final Parameterisation parameterisation) {
	}

	/**
	 * Gets the parameterisation, describing variables’ state when measuring this result.
	 *
	 * @return The parameterisation, or {@code null} if no parameterisation was recorded
	 *         when measuring this result.
	 */
	public Parameterisation getParameterisation() {
		return null;
	}
}
