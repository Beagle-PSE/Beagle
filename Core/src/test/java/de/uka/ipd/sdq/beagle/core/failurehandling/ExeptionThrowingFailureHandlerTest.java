package de.uka.ipd.sdq.beagle.core.failurehandling;

import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;

import de.uka.ipd.sdq.beagle.core.failurehandling.ExceptionThrowingFailureResolver.FailureException;
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
		new ExceptionThrowingFailureResolver();
	}

	/**
	 * Test method for {@link ExceptionThrowingFailureResolver#handle()}.
	 */
	@Test
	public void handle() {
		final ExceptionThrowingFailureResolver exceptionHandler = new ExceptionThrowingFailureResolver();

		final FailureReport<String> report = new FailureReport<>();
		ThrowingMethod method = () -> {
			exceptionHandler.handle(report, "clientName");
		};
		assertThat(method, throwsException(FailureException.class));

		report.message("Test message");
		method = () -> {
			exceptionHandler.handle(report, "clientName");
		};
		assertThat(method, throwsException(FailureException.class));
		try {
			exceptionHandler.handle(report, "clientName");
		} catch (final FailureException failEx) {
			assertThat(failEx.getMessage(), containsString("Test message"));
		}

		report.cause(new IllegalArgumentException("Illegal argument."));
		report.message("Test message");
		method = () -> {
			exceptionHandler.handle(report, "clientName");
		};
		assertThat(method, throwsException(FailureException.class));

		try {
			exceptionHandler.handle(report, "clientName");
		} catch (final FailureException failEx) {
			assertThat(failEx.getMessage(), containsString("Test message"));
			assertThat(failEx.getMessage(), containsString("Illegal argument."));
		}
	}
}
