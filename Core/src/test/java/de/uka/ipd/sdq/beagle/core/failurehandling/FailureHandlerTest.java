package de.uka.ipd.sdq.beagle.core.failurehandling;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

/**
 * TODO Document this type.
 * 
 * @author Michael Vogt
 */
public class FailureHandlerTest {

	@Test
	public void getHandler() {
		final String clientName = "clientAllice";
		assertThat(FailureHandler.getHandler(clientName), is("clientAllice"));

		final Class<?> clientType;
		assertThat(FailureHandler.getHandler(clientType), is(clientType));
	}

	@Test
	public void FailureHandlerProvider() {

	}

	@Test
	public void setProvider() {

	}

}
