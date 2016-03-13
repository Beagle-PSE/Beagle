package de.uka.ipd.sdq.beagle.core.failurehandling;

import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static org.hamcrest.MatcherAssert.assertThat;

import de.uka.ipd.sdq.beagle.core.testutil.ThrowingMethod;

import org.junit.Test;

/**
 * Tests {@link FailureHandler} and contains all test cases needed to check every method.
 *
 * @author Michael Vogt
 */
public class FailureHandlerTest {

	/**
	 * Test method for {@link FailureHandler#getHandler()}.
	 */
	@Test
	public void getHandler() {
		final String clientName = "clientAllice";
		FailureHandler.getHandler(clientName);

		assertThat(() -> FailureHandler.getHandler((String) null), throwsException(NullPointerException.class));

		final Class<String> clientType = String.class;
		FailureHandler.getHandler(clientType);

		assertThat(() -> FailureHandler.getHandler((Class<?>) null), throwsException(NullPointerException.class));
	}

	/**
	 * Test method for {@link FailureHandler#setProvider()}.
	 */
	@Test
	public void setProvider() {
		final ThrowingMethod method = () -> {
			FailureHandler.setProvider(null);
		};
		assertThat(method, throwsException(NullPointerException.class));

		FailureHandler.setProvider(() -> null);

		// Reset the provider after the test!
		FailureHandler.setProvider(ExceptionThrowingFailureResolver::new);
	}
}
