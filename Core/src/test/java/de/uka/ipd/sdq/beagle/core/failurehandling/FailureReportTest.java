package de.uka.ipd.sdq.beagle.core.failurehandling;

import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import de.uka.ipd.sdq.beagle.core.testutil.ThrowingMethod;

import org.junit.Test;

import java.util.function.Supplier;

/**
 * Tests {@link FailureReport} and contains all test cases needed to check every method.
 * 
 * @author Michael Vogt
 */
public class FailureReportTest {

	/**
	 * Message giving information about the failure.
	 */
	private String failureMessage;

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
	private Supplier<String> continueFunction;

	/**
	 * A routine to call if it is wished to retry the action the failure occurred at.
	 */
	private Supplier<String> retryFunction;

	/**
	 * Test method for {@link FailureReport#message()}.
	 */
	@Test
	public void message() {
		final String message = "failureMessage";
		final FailureReport<String> failReport = new FailureReport<>();
		assertThat(failReport.message("failureMessage"), is(message));
		final ThrowingMethod method = () -> {
			final FailureReport<String> failReport1 = new FailureReport<>();
			failReport1.message(null);
		};
		assertThat(method, throwsException(NullPointerException.class));
	}

	/**
	 * Test method for {@link FailureReport#details()}.
	 */
	@Test
	public void details() {
		final String message = "failureMessage";
		final FailureReport<String> failReport = new FailureReport<>();
		assertThat(failReport.details("failureMessage"), is(message));
		final ThrowingMethod method = () -> {
			final FailureReport<String> failReport1 = new FailureReport<>();
			failReport1.details(null);
		};
		assertThat(method, throwsException(NullPointerException.class));
		final FailureReport<String> failReport2 = new FailureReport<>();
		failReport2.details(message);
		assertThat(this.detailsProvided, is(true));
	}

	/**
	 * Test method for {@link FailureReport#cause()}.
	 */
	@Test
	public void cause() {
		final ThrowingMethod method = () -> {
			final FailureReport<String> failReport = new FailureReport<>();
			failReport.cause(null);
		};
		assertThat(method, throwsException(NullPointerException.class));
		final FailureReport<String> failReport1 = new FailureReport<>();
		failReport1.message(null);
		failReport1.cause(new IllegalArgumentException("illegal arg"));
		assertThat(failReport1.getFailureMessage(), is("illegal arg"));
		final FailureReport<String> failReport2 = new FailureReport<>();
		failReport2.message("failure message");
		failReport2.cause(new IllegalArgumentException("illegal arg"));
		assertThat(this.detailsProvided, (is(true)));

	}

	/**
	 * Test method for {@link FailureReport#recoverable()}.
	 */
	@Test
	public void recoverable() {
		this.continueFunction = null;
		final FailureReport<String> failReport = new FailureReport<>();
		failReport.recoverable();
		assertThat(failReport.getContinueRoutine().get(), is(nullValue()));
	}

	/**
	 * Test method for {@link FailureReport#continueWith()}.
	 */
	@Test
	public void continueWith() {
		final FailureReport<String> failReport = new FailureReport<>();
		final Runnable runTest = mock(Runnable.class);
		failReport.continueWith(runTest);
		assertThat(failReport.getContinueRoutine().get(), is(nullValue()));
		verify(runTest.run());

	}

	/**
	 * Test method for {@link FailureReport#retryWith()}.
	 */
	@Test
	public void retryWith() {
		final FailureReport<String> failReport = new FailureReport<>();
		final Runnable runTest = mock(Runnable.class);
		failReport.retryWith(runTest);
		assertThat(failReport.getContinueRoutine().get(), is(nullValue()));
		verify(runTest.run());
	}

	/**
	 * Test method for {@link FailureReport#getMessage(String, Object...)}.
	 */
	@Test
	public void getMessage() {
		final FailureReport<String> failReport = new FailureReport<>();
		assertThat(failReport.getFailureMessage(), is(this.failureMessage));

	}

	/**
	 * Test method for {@link FailureReport#getDetails()}.
	 */
	@Test
	public void getDetails() {
		this.detailsProvided = false;
		final FailureReport<String> failReport = new FailureReport<>();
		assertThat(failReport.getDetails(), is(this.detailsProvided));
		this.detailsProvided = true;
		final FailureReport<String> failReport1 = new FailureReport<>();
		assertThat(failReport1.getDetails(), is(this.detailsProvided));
	}

	/**
	 * Test method for {@link FailureReport#getFailureCause()}.
	 */
	@Test
	public void getFailureCause() {
		final FailureReport<String> failReport = new FailureReport<>();
		assertThat(failReport.getFailureCause(), is(this.failureCause));
	}

	/**
	 * Test method for {@link FailureReport#getContinueRoutine()}.
	 */
	@Test
	public void getContinueRoutine() {
		final FailureReport<String> failReport = new FailureReport<>();
		assertThat(failReport.getContinueRoutine(), is(this.continueFunction));
	}

	/**
	 * Test method for {@link FailureReport#getRetryRoutine()}.
	 */
	@Test
	public void getRetryRoutine() {
		final FailureReport<String> failReport = new FailureReport<>();
		assertThat(failReport.getRetryRoutine(), is(this.retryFunction));
	}

}
