package de.uka.ipd.sdq.beagle.core.timeout;

import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import de.uka.ipd.sdq.beagle.core.testutil.ThrowingMethod;

import org.junit.Rule;
import org.junit.Test;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;

/**
 * Tests {@link AdaptiveTimeout} and contains all test cases needed to check
 * every method.
 *
 * @author Michael Vogt
 */
@PrepareForTest(AdaptiveTimeout.class)
public class AdaptiveTimeoutTest {

	/**
	 * Loads PowerMockito to mock the static final class {@link System}.
	 */
	@Rule
	public final PowerMockRule loadPowerMockito = new PowerMockRule();

	/**
	 * Test method for {@link AdaptiveTimeout#isReached()}.
	 */
	@Test
	public void isReached() {
		final AdaptiveTimeout adaptTimeout = new AdaptiveTimeout();
		mockStatic(System.class);
		given(System.currentTimeMillis()).willReturn(0L);
		adaptTimeout.init();
		assertThat(adaptTimeout.isReached(), equalTo(false));

		final AdaptiveTimeout adaptTimeout2 = new AdaptiveTimeout();
		adaptTimeout2.init();
		given(System.currentTimeMillis()).willReturn(2L * (3600 * 1000));
		assertThat(adaptTimeout2.isReached(), equalTo(true));
	}

	/**
	 * Test method for {@link AdaptiveTimeout#reportOneStepProgress()}.
	 */
	@Test
	public void reportOneStepProgress() {
		final AdaptiveTimeout adaptTimeout = new AdaptiveTimeout();
		adaptTimeout.init();
		adaptTimeout.reportOneStepProgress();
	}

	/**
	 * Test that init() is called at first method and only once.
	 */
	@Test
	public void init() {
		final AdaptiveTimeout adaptTimeout1 = new AdaptiveTimeout();
		adaptTimeout1.init();
		final ThrowingMethod method = () -> {
			adaptTimeout1.init();
		};
		assertThat("adaptTimeout1 must not be allowed to call init() more than one time.", method,
			throwsException(IllegalStateException.class));

		final AdaptiveTimeout adaptTimeout = new AdaptiveTimeout();
		adaptTimeout.init();
	}

	/**
	 * Test method for {@link AdaptiveTimeout#notifyOnReachedTimeout()}.
	 */
	@Test
	public void notifyOnReachedTimeout() {
		final Runnable callback1 = mock(Runnable.class);
		final AdaptiveTimeout adaptTimeout = new AdaptiveTimeout();
		adaptTimeout.registerCallback(callback1);
		adaptTimeout.init();

	}

}
