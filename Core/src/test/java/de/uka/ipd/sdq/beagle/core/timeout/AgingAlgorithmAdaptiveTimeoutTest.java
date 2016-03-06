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
 * Tests {@link AgeingAlgorithmAdaptiveTimeout} and contains all test cases needed to check
 * every method.
 *
 * @author Michael Vogt
 */
@PrepareForTest(AgeingAlgorithmAdaptiveTimeout.class)
public class AgingAlgorithmAdaptiveTimeoutTest {

	/**
	 * Loads PowerMockito to mock the static final class {@link System}.
	 */
	@Rule
	public final PowerMockRule loadPowerMockito = new PowerMockRule();

	/**
	 * Test method for {@link AgeingAlgorithmAdaptiveTimeout#isReached()}.
	 */
	@Test
	public void isReached() {
		final AgeingAlgorithmAdaptiveTimeout adaptTimeout = new AgeingAlgorithmAdaptiveTimeout();
		mockStatic(System.class);
		given(System.currentTimeMillis()).willReturn(0L);
		adaptTimeout.init();
		assertThat(adaptTimeout.isReached(), equalTo(false));

		final AgeingAlgorithmAdaptiveTimeout adaptTimeout2 = new AgeingAlgorithmAdaptiveTimeout();
		adaptTimeout2.init();
		given(System.currentTimeMillis()).willReturn(2L * (3600 * 1000));
		assertThat(adaptTimeout2.isReached(), equalTo(true));
	}

	/**
	 * Test method for {@link AgeingAlgorithmAdaptiveTimeout#reportOneStepProgress()}.
	 */
	@Test
	public void reportOneStepProgress() {
		final AgeingAlgorithmAdaptiveTimeout adaptTimeout = new AgeingAlgorithmAdaptiveTimeout();
		adaptTimeout.init();
		adaptTimeout.reportOneStepProgress();
	}

	/**
	 * Test that init() is called at first method and only once.
	 */
	@Test
	public void init() {
		final AgeingAlgorithmAdaptiveTimeout adaptTimeout1 = new AgeingAlgorithmAdaptiveTimeout();
		adaptTimeout1.init();
		final ThrowingMethod method = () -> {
			adaptTimeout1.init();
		};
		assertThat("adaptTimeout1 must not be allowed to call init() more than one time.", method,
			throwsException(IllegalStateException.class));

		final AgeingAlgorithmAdaptiveTimeout adaptTimeout = new AgeingAlgorithmAdaptiveTimeout();
		adaptTimeout.init();
	}

	/**
	 * Test method for {@link AgeingAlgorithmAdaptiveTimeout#notifyOnReachedTimeout()}.
	 */
	@Test
	public void notifyOnReachedTimeout() {
		final Runnable callback1 = mock(Runnable.class);
		final AgeingAlgorithmAdaptiveTimeout adaptTimeout = new AgeingAlgorithmAdaptiveTimeout();
		adaptTimeout.registerCallback(callback1);
		adaptTimeout.init();

	}

}
