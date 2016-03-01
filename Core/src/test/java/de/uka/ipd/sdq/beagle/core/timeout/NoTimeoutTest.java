package de.uka.ipd.sdq.beagle.core.timeout;

import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
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
			final NoTimeout timeout1 = new NoTimeout();
			timeout1.init();
			timeout1.init();
		};
		assertThat("timeout must not be allowed to call init() more than one time.", method,
			throwsException(IllegalStateException.class));

		final NoTimeout timeout2 = new NoTimeout();
		assertThat(timeout2.initialised, is(equalTo(false)));
	}

	/**
	 * Test method for {@link NoTimeout#reportOneStepProgress()}.
	 */
	@Test
	public void reportOneStepProgress() {

		final NoTimeout timeout = new NoTimeout();
		timeout.init();
		timeout.reportOneStepProgress();

		final NoTimeout timeout1 = new NoTimeout();
		timeout1.init();

		final ThrowingMethod method = () -> {
			timeout1.init();
		};
		assertThat("timeout must not be allowed to call init() more than one time.", method,
			throwsException(IllegalStateException.class));

		final NoTimeout timeout2 = new NoTimeout();
		assertThat(timeout2.initialised, is(equalTo(false)));

	}

}
