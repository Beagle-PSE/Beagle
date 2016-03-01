package de.uka.ipd.sdq.beagle.core.timeout;

import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;

import de.uka.ipd.sdq.beagle.core.testutil.ThrowingMethod;

import org.junit.Test;

/**
 * Tests {@link AdaptiveTimeout} and contains all test cases needed to check every method.
 *
 * @author Michael Vogt
 */

public class AdaptiveTimeoutTest {

	/**
	 * Test that init() is called at first method and only once.
	 */
	@Test
	public void init() {
		final AdaptiveTimeout adaptTimeout = new AdaptiveTimeout();
		adaptTimeout.init();

		final AdaptiveTimeout adaptTimeout1 = new AdaptiveTimeout();
		adaptTimeout1.init();
		final ThrowingMethod method = () -> {
			adaptTimeout1.init();
		};
		assertThat("adaptTimeout1 must not be allowed to call init() more than one time.", method,
			throwsException(IllegalStateException.class));
	}

	/**
	 * Test method for {@link AdaptiveTimeout#reportOneStepProgress()}.
	 */
	@Test
	public void reportOneStepProgress() {
		final AdaptiveTimeout adaptTimeout = new AdaptiveTimeout();
		adaptTimeout.init();
		for (int i = 0; i < 10; i++) {
			adaptTimeout.reportOneStepProgress();
		}

		final AdaptiveTimeout adaptTimeout1 = new AdaptiveTimeout();
		adaptTimeout1.init();
		adaptTimeout1.reportOneStepProgress();

		final AdaptiveTimeout adaptTimeout2 = new AdaptiveTimeout();
		adaptTimeout2.init();
		for (int i = 0; i < 10; i++) {
			adaptTimeout2.reportOneStepProgress();
		}
		given(System.currentTimeMillis()).willReturn((long) (5 * 60 * 1000 + 10));
		adaptTimeout2.reportOneStepProgress();

		final AdaptiveTimeout adaptTimeout3 = new AdaptiveTimeout();
		adaptTimeout3.init();
		for (int i = 0; i < 9; i++) {
			adaptTimeout3.reportOneStepProgress();
		}
		given(System.currentTimeMillis()).willReturn((long) (5 * 60 * 1000 * 10));
		adaptTimeout3.reportOneStepProgress();

	}

	/**
	 * Test method for {@link AdaptiveTimeout#isReached()}.
	 */
	@Test
	public void isReached() {

		final AdaptiveTimeout adaptTimeout = new AdaptiveTimeout();
		adaptTimeout.init();
		assertThat(adaptTimeout.isReached(), equalTo(false));

		final AdaptiveTimeout adaptTimeout1 = new AdaptiveTimeout();
		adaptTimeout1.init();
		for (int i = 0; i < 10; i++) {
			adaptTimeout1.reportOneStepProgress();

		}
		assertThat(adaptTimeout1.isReached(), equalTo(false));

		final AdaptiveTimeout adaptTimeout2 = new AdaptiveTimeout();
		adaptTimeout2.init();
		for (int i = 0; i < 10; i++) {
			adaptTimeout2.reportOneStepProgress();
		}
		given(System.currentTimeMillis()).willReturn((long) (5 * 60 * 1000 + 10));

		assertThat(adaptTimeout2.isReached(), equalTo(true));

		final AdaptiveTimeout adaptTimeout3 = new AdaptiveTimeout();
		adaptTimeout3.init();
		for (int i = 0; i < 10; i++) {
			adaptTimeout3.reportOneStepProgress();
		}
		given(System.currentTimeMillis()).willReturn((long) (5 * 60 * 1000 + 10));
		adaptTimeout3.reportOneStepProgress();
		assertThat(adaptTimeout3.isReached(), equalTo(true));
	}

}
