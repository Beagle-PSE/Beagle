package de.uka.ipd.sdq.beagle.core.timeout;

import org.junit.Test;

/**
 * Tests {@link AdaptiveTimeout} and contains all test cases needed to check every method.
 *
 * @author Michael Vogt
 */
public class AdaptiveTimeoutTest {

	/**
	 * Test method for {@link ConstantTimeout#isReached()}.
	 */
	@Test
	public void init() {
		final AdaptiveTimeout adaptTimeout = new AdaptiveTimeout();
		adaptTimeout.init();
	}

}
