package de.uka.ipd.sdq.beagle.core.failurehandling;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.function.Supplier;

/**
 * Information about a failure that occurred while running Beagle. Besides a message and
 * information about the causing (or indicating) exception, a failure report may continue
 * information on how to recover from a failure. The failure reported through this report
 * is considered non-recoverable until a recover function has be set through
 * {@link #recoverable()}, {@link #retryWith} or {@link #continueWith}.
 *
 * @author Joshua Gleitze
 *
 * @param <RECOVER_TYPE> The return type of the recover functions.
 */
public class FailureReport<RECOVER_TYPE> {

	/**
	 * Message giving information about the failure.
	 */
	private String failureMessage;

	/**
	 * Details about the failure.
	 */
	private final StringBuilder failureDetails = new StringBuilder();

	/**
	 * Wheter details about the failure have yet been provided.
	 */
	private boolean detailsProvided;

	/**
	 * Exception that caused or indicated the failure.
	 */
	private Exception failureCause;

	/**
	 * A routine to call if it is wished to continue by ignoring the failure.
	 */
	private Supplier<RECOVER_TYPE> continueFunction;

	/**
	 * A routine to call if it is wished to retry the action the failure occurred at.
	 */
	private Supplier<RECOVER_TYPE> retryFunction;

	/**
	 * Supplies a message describing the failure. The message will be evaluated through
	 * {@link String#format(String, Object...)}.
	 *
	 * @param message A message describing the failure. Must not be {@code null}.
	 * @param values Format values, will be passed to
	 *            {@link String#format(String, Object...)}.
	 * @return {@code this}.
	 */
	public FailureReport<RECOVER_TYPE> message(final String message, final Object... values) {
		Validate.notNull(message);
		this.failureMessage = String.format(message, values);
		return this;
	}

	/**
	 * Supplies a message giving details about the failure. The message will be evaluated
	 * through {@link String#format(String, Object...)} and be <em>perepended</em> to
	 * potentally already existing details.
	 *
	 * @param message Details about the reported failure.
	 * @param values Format values, will be passed to
	 *            {@link String#format(String, Object...)}.
	 * @return {@code this}.
	 */
	public FailureReport<RECOVER_TYPE> details(final String message, final Object... values) {
		this.detailsProvided = true;
		this.failureDetails.insert(0, String.format(message, values));
		return this;
	}

	/**
	 * Supplies the exception that caused or indicated the failure. If no message has been
	 * set on this report yet, the report’s message will be set to
	 * {@code cause.getMessage()}. The exception’s stacktrace will be appnended to the
	 * report’s {@linkplain #details(String, Object...) details}. A new report’s cause is
	 * {@code null}.
	 *
	 * @param cause The exception that caused or indicated the failure. Must not be
	 *            {@code null}.
	 * @return {@code this}.
	 */
	public FailureReport<RECOVER_TYPE> cause(final Exception cause) {
		Validate.notNull(cause);

		this.failureCause = cause;
		if (this.failureMessage == null) {
			this.failureMessage = cause.getMessage();
		}
		this.detailsProvided = true;
		this.failureDetails.append(ExceptionUtils.getStackTrace(cause));
		return this;
	}

	/**
	 * Reports that the failure is recoverable by continuing the normal control flow after
	 * the failure handling. The failure reporter will receive {@code null} as the result
	 * of handling this report.
	 *
	 * @return {@code this}.
	 */
	public FailureReport<RECOVER_TYPE> recoverable() {
		this.continueFunction = () -> {
			return null;
		};
		return this;
	}

	/**
	 * Reports that the failure is recoverable by continuing by running {@code continueAt}
	 * .
	 *
	 * @param continueAt A function defining how to continue while skipping the failed
	 *            action. Supplies the value the failure reporter should use instead of
	 *            the one that was expected to be generated skipped action.
	 * @return {@code this}.
	 */
	public FailureReport<RECOVER_TYPE> continueWith(final Supplier<RECOVER_TYPE> continueAt) {
		this.continueFunction = continueAt;
		return this;
	}

	/**
	 * Reports that the failure is recoverable by continuing by running {@code continueAt}
	 * . The failure reporter will receive {@code null} as the result of handling this
	 * report.
	 *
	 * @param continueAt A runnable defining how to continue while skipping the failed
	 *            action.
	 * @return {@code this}.
	 */
	public FailureReport<RECOVER_TYPE> continueWith(final Runnable continueAt) {
		this.continueFunction = () -> {
			continueAt.run();
			return null;
		};
		return this;
	}

	/**
	 * Reports that the failure is recoverable by retrying the failed action by running
	 * {@code retryAt}.
	 *
	 * @param retryAt A function defining how to retry the failed action. Supplies the
	 *            value the reporter should use instead of the one that was expected to be
	 *            generated by the failed action.
	 * @return {@code this}.
	 */
	public FailureReport<RECOVER_TYPE> retryWith(final Supplier<RECOVER_TYPE> retryAt) {
		this.retryFunction = retryAt;
		return this;
	}

	/**
	 * Reports that the failure is recoverable by retrying the failed action by running
	 * {@code retryAt}. The failure reporter will receive {@code null} as the result of
	 * handling this report.
	 *
	 * @param retryAt A runnable defining how to retry the failed action.
	 * @return {@code this}.
	 */
	public FailureReport<RECOVER_TYPE> retryWith(final Runnable retryAt) {
		this.retryFunction = () -> {
			retryAt.run();
			return null;
		};
		return this;
	}

	/**
	 * Gets the report’s failure description.
	 *
	 * @return A description of the failure. {@code null} indicates that there’s no
	 *         information available.
	 *
	 * @see #message(String, Object...)
	 */
	public String getFailureMessage() {
		return this.failureMessage;
	}

	/**
	 * Gets the report’s failure details.
	 *
	 * @return Details about the failure. {@code null} indicates that there’s no
	 *         information available.
	 *
	 * @see #details(String, Object...)
	 */
	public String getDetails() {
		return (this.detailsProvided) ? this.failureDetails.toString() : null;
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
	public Supplier<RECOVER_TYPE> getContinueRoutine() {
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
	public Supplier<RECOVER_TYPE> getRetryRoutine() {
		return this.retryFunction;
	}

}
