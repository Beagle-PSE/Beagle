package de.uka.ipd.sdq.beagle.core.failurehandling;

import org.apache.commons.lang3.Validate;

/**
 * {@linkplain FailureHandler} throwing an exception for any reported failure.
 *
 * @author Joshua Gleitze
 */
public class ExceptionThrowingFailureHandler extends FailureHandler {

	/**
	 * The reporter’s name.
	 */
	private final String clientName;

	/**
	 * Create a handler for a client called {@code clientName}.
	 *
	 * @param clientName Name identifying the client that may report failures through this
	 *            handler. Must not be {@code null}.
	 */
	public ExceptionThrowingFailureHandler(final String clientName) {
		Validate.notNull(clientName);
		this.clientName = clientName;
	}

	@Override
	public <RECOVER_TYPE> RECOVER_TYPE handle(final FailureReport<RECOVER_TYPE> report) {
		String failureDescription = String.format("%s reported a failure!", this.clientName);
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
