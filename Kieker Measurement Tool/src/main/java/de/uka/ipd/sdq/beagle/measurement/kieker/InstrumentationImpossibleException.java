package de.uka.ipd.sdq.beagle.measurement.kieker;

/**
 * Thrown if instrumentation of source code is not logically possible.
 *
 * @author Joshua Gleitze
 */
public class InstrumentationImpossibleException extends Exception {

	/**
	 * Serialisation version UID, see {@link java.io.Serializable}.
	 */
	private static final long serialVersionUID = -2984834811894116037L;

	/**
	 * Creates an {@code InstrumentationImpossibleException}.
	 *
	 * @param message A message desribing why instrumentation was not possible. Will
	 *            evaluated through {@link String#format(String, Object...)}.
	 * @param values The values to pass to {@link String#format(String, Object...)}.
	 */
	public InstrumentationImpossibleException(final String message, final Object... values) {
		super(String.format(message, values));
	}

}
