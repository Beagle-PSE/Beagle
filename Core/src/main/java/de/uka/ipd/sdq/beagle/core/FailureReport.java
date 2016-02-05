package de.uka.ipd.sdq.beagle.core;

/**
 * Information about a failure that occurred while running Beagle. Besides a message and
 * information about the causing (or indicating) exception, a failure report may continue
 * information on how to recover from a failure. The failure reported through this report
 * is considered non-recoverable until a recover function has be set through
 * {@link #recoverable()}, {@link #retryWith} or {@link #continueWith}.
 *
 * @author Joshua Gleitze
 */
public class FailureReport {

	/**
	 * Message giving information about the failure.
	 */
	private String failureMessage;

	/**
	 * Exception that caused or indicated the failure.
	 */
	private Exception failureCause;

	/**
	 * A routine to call if it is wished to continue by ignoring the failure.
	 */
	private Runnable continueFunction;

	/**
	 * A routine to call if it is wished to retry the action the failure occurred at.
	 */
	private Runnable retryFunction;

	/**
	 * Supplies a message describing the failure. The message will be evaluated through
	 * {@link String#format(String, Object...)}.
	 *
	 * @param message A message giving details about the failure.
	 * @param values Format values, will be passed to
	 *            {@link String#format(String, Object...)}.
	 * @return {@code this}.
	 */
	public FailureReport message(final String message, final Object... values) {
		this.failureMessage = String.format(message, values);
		return this;
	}

	/**
	 * Supplies the exception that caused or indicated the failure. If no message has been
	 * set on this report yet, the report’s message will be set to
	 * {@code cause.getMessage()}. A new report’s cause is {@code null}.
	 *
	 * @param cause The exception that caused or indicated the failure.
	 * @return {@code this}.
	 */
	public FailureReport cause(final Exception cause) {
		this.failureCause = cause;
		if (this.failureMessage == null) {
			this.failureMessage = cause.getMessage();
		}
		return this;
	}

	/**
	 * Reports that the failure is recoverable by continuing the normal control flow after
	 * the failure handling.
	 *
	 * @return {@code this}.
	 */
	public FailureReport recoverable() {
		this.continueFunction = () -> {
		};
		return this;
	}

	/**
	 * Reports that the failure is recoverable by continuing by running {@code continueAt}
	 * .
	 *
	 * @param continueAt A runnable defining how to continue while skipping the failed
	 *            action.
	 * @return {@code this}.
	 */
	public FailureReport continueWith(final Runnable continueAt) {
		this.continueFunction = continueAt;
		return this;
	}

	/**
	 * Reports that the failure is recoverable by retrying the failed action by running
	 * {@code retryAt}.
	 *
	 * @param retryAt A runnable defining how to retry the failed action.
	 * @return {@code this}.
	 */
	public FailureReport retryWith(final Runnable retryAt) {
		this.retryFunction = retryAt;
		return this;
	}

	/**
	 * Gets the report’s failure description.
	 *
	 * @return Details about the failure. {@code null} indicates that there’s no
	 *         information available.
	 *
	 * @see #message(String, Object...)
	 */
	public String getFailureMessage() {
		return this.failureMessage;
	}

	/**
	 * Gets the exception that caused or indicated the reported failure.
	 *
	 * @return The causing exception. {@code null} indicates that the reported failure was
	 *         not caused by an exception.
	 *
	 * @see #cause(Exception)
	 */
	public Exception getFailureCause() {
		return this.failureCause;
	}

	/**
	 * Gets the function that lets Beagle continue the analysis by skipping the failed
	 * action.
	 *
	 * @return The function detailing how to continue. {@code null} indicates that Beagle
	 *         cannot continue without the failed action.
	 *
	 * @see #recoverable()
	 * @see #continueWith(Runnable)
	 */
	public Runnable getContinueRoutine() {
		return this.continueFunction;
	}

	/**
	 * Gets the function that lets Beagle retry the failed failed action.
	 *
	 * @return The function detailing how to retry the failed action. {@code null}
	 *         indicates that the failed action cannot be retried.
	 *
	 * @see #retryWith(Runnable)
	 */
	public Runnable getRetryRoutine() {
		return this.retryFunction;
	}

}
