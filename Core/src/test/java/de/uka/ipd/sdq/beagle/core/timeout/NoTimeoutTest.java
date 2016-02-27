package de.uka.ipd.sdq.beagle.core.timeout;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

/**
 * Tests {@link NoTimeout} and contains all test cases needed to check every method.
 *
 * @author Michael Vogt
 */
public class NoTimeoutTest {

	/**
	 * Test method for {@link NoTimeout#isReached()}.
	 */
	@Test
	public void isReached() {

		final NoTimeout timeout = new NoTimeout();

		assertThat(timeout.isReached(), is(equals(false)));

	}

	/**
	 * Test method for {@link NoTimeout#reportOneStepProgress()}.
	 */
	@Test
	public void reportOneStepProgress() {

		final NoTimeout timeout = new NoTimeout();

		timeout.reportOneStepProgress();

	}

}
