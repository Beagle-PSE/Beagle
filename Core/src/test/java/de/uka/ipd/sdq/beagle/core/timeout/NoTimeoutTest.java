package de.uka.ipd.sdq.beagle.core.timeout;

import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.uka.ipd.sdq.beagle.core.testutil.ThrowingMethod;

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
		timeout.init();
		assertThat(timeout.isReached(), is(equals(false)));

		final ThrowingMethod method = () -> {
			new NoTimeout();
		};
		assertThat("init() must be called once.", method, throwsException(IllegalStateException.class));
	}

	/**
	 * Test method for {@link NoTimeout#reportOneStepProgress()}.
	 */
	@Test
	public void reportOneStepProgress() {

		final NoTimeout timeout = new NoTimeout();
		timeout.init();
		timeout.reportOneStepProgress();

		final ThrowingMethod method = () -> {
			new NoTimeout();
		};
		assertThat("init() must be called once.", method, throwsException(IllegalStateException.class));

	}

}
