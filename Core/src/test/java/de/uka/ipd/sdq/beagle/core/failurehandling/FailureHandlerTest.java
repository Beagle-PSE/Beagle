package de.uka.ipd.sdq.beagle.core.failurehandling;

import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

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
		assertThat(FailureHandler.getHandler(clientName), is("clientAllice"));

		final Class<String> clientType = String.class;
		assertThat(FailureHandler.getHandler(clientType.getName()), is(String.class));
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
	}
}
