package de.uka.ipd.sdq.beagle.core.failurehandling;

import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;

import de.uka.ipd.sdq.beagle.core.failurehandling.ExceptionThrowingFailureHandler.FailureException;
import de.uka.ipd.sdq.beagle.core.testutil.ThrowingMethod;

import org.junit.Test;

/**
 * Tests {@link ExeptionThrowingFailureHandler} and contains all test cases needed to
 * check every method.
 *
 * @author Michael Vogt
 */
public class ExeptionThrowingFailureHandlerTest {

	/**
	 * Test method for the constructor.
	 */
	@Test
	public void constructorExceptionThrowingFailureHandler() {
		final String clientName = "clientBob";
		final ThrowingMethod method = () -> {
			new ExceptionThrowingFailureHandler(null);
		};
		assertThat("clientName must not be null.", method, throwsException(NullPointerException.class));

		new ExceptionThrowingFailureHandler(clientName);
	}

	/**
	 * Test method for {@link ExceptionThrowingFailureHandler#handle()}.
	 */
	@Test
	public void handle() {
		final ExceptionThrowingFailureHandler exceptionHandler = new ExceptionThrowingFailureHandler("testClient");
		ThrowingMethod method = () -> {
			exceptionHandler.handle(null);
		};
		assertThat(method, throwsException(NullPointerException.class));

		final FailureReport<String> report = new FailureReport<>();
		method = () -> {
			exceptionHandler.handle(report);
		};
		assertThat(method, throwsException(FailureException.class));

		report.message("Test message");
		method = () -> {
			exceptionHandler.handle(report);
		};
		assertThat(method, throwsException(FailureException.class));
		try {
			exceptionHandler.handle(report);
		} catch (final FailureException failEx) {
			assertThat(failEx.getMessage(), containsString("Test message"));
		}

		report.cause(new IllegalArgumentException("Illegal argument."));
		report.message("Test message");
		method = () -> {
			exceptionHandler.handle(report);
		};
		assertThat(method, throwsException(FailureException.class));

		try {
			exceptionHandler.handle(report);
		} catch (final FailureException failEx) {
			assertThat(failEx.getMessage(), containsString("Test message"));
			assertThat(failEx.getMessage(), containsString("Illegal argument."));
		}
	}
}
