package de.uka.ipd.sdq.beagle.core.failurehandling;

/**
 * {@linkplain FailureResolver} throwing an exception for any reported failure.
 *
 * @author Joshua Gleitze
 */
public class ExceptionThrowingFailureResolver implements FailureResolver {

	@Override
	public <RECOVER_TYPE> RECOVER_TYPE handle(final FailureReport<RECOVER_TYPE> report, final String clientName) {
		String failureDescription = String.format("%s reported a failure!", clientName);
		if (report.getFailureMessage() != null) {
			failureDescription += "\n\n" + report.getFailureMessage();
		}
		if (report.getDetails() != null) {
			failureDescription += "\n\nDetails:\n" + report.getDetails();
		}
		throw new FailureException(failureDescription, report.getFailureCause());
	}

	/**
	 * Thrown by this handler for reported failures.
	 *
	 * @author Joshua Gleitze
	 */
	public final class FailureException extends RuntimeException {

		/**
		 * Serialisation version UID, see {@link java.io.Serializable}.
		 */
		private static final long serialVersionUID = 1653063728785911804L;

		/**
		 * Creates the exception using the provided {@code message} and {@code cause}.
		 *
		 * @param message A message describing the failures.
		 * @param cause The throwable causing or indicating the failure.
		 */
		private FailureException(final String message, final Throwable cause) {
			super(message, cause);
		}

	}

}
