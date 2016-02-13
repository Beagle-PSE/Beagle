package de.uka.ipd.sdq.beagle.core.failurehandling;

import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static org.junit.Assert.assertThat;

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
	 * Test method for {@link ExceptionThrowingFailureHandlerTest#getDetails()}.
	 */
	@Test
	public void constructor() {
		final String clientName = "clientBob";
		final ThrowingMethod method = () -> {
			new ExceptionThrowingFailureHandler(null);
		};
		assertThat("clientName must not be null.", method, throwsException(NullPointerException.class));

		new ExceptionThrowingFailureHandler(clientName);
	}

}
