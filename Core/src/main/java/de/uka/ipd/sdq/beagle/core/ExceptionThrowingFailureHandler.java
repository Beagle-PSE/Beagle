package de.uka.ipd.sdq.beagle.core;

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
	 *            handler.
	 */
	public ExceptionThrowingFailureHandler(final String clientName) {
		this.clientName = clientName;
	}

	@Override
	public void handle(final FailureReport report) {
		String failureDescription = String.format("%s reported a failure!", this.clientName);
		if (report.getFailureCause() == null && report.getFailureMessage() != null) {
			failureDescription += "\n\n" + report.getFailureMessage();
		}
		throw new RuntimeException(failureDescription, report.getFailureCause());
	}

}
