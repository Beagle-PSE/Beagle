package de.uka.ipd.sdq.beagle.core.timeout;

import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import de.uka.ipd.sdq.beagle.core.testutil.ThrowingMethod;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;

/**
 * Tests {@link LinearRegressionAdaptiveTimeout} and contains all test cases needed to check every method.
 *
 * @author Michael Vogt
 */
@PrepareForTest(LinearRegressionAdaptiveTimeout.class)
public class AdaptiveTimeoutTest {

	/**
	 * Loads PowerMockito to mock the static final class {@link System}.
	 */
	@Rule
	public final PowerMockRule loadPowerMockito = new PowerMockRule();

	/**
	 * Test that init() is called at first method and only once.
	 */
	@Test
	public void init() {
		final LinearRegressionAdaptiveTimeout adaptTimeout = new LinearRegressionAdaptiveTimeout();
		adaptTimeout.init();

		final LinearRegressionAdaptiveTimeout adaptTimeout1 = new LinearRegressionAdaptiveTimeout();
		adaptTimeout1.init();
		final ThrowingMethod method = () -> {
			adaptTimeout1.init();
		};
		assertThat("adaptTimeout1 must not be allowed to call init() more than one time.", method,
			throwsException(IllegalStateException.class));
	}

	/**
	 * Test method for {@link LinearRegressionAdaptiveTimeout#reportOneStepProgress()}.
	 */
	@Test
	public void reportOneStepProgress() {
		mockStatic(System.class);
		given(System.currentTimeMillis()).willReturn(0L);

		final LinearRegressionAdaptiveTimeout adaptTimeout = new LinearRegressionAdaptiveTimeout();
		adaptTimeout.init();
		for (int i = 0; i < 10; i++) {
			adaptTimeout.reportOneStepProgress();
		}

		final LinearRegressionAdaptiveTimeout adaptTimeout1 = new LinearRegressionAdaptiveTimeout();
		adaptTimeout1.init();
		adaptTimeout1.reportOneStepProgress();

		final LinearRegressionAdaptiveTimeout adaptTimeout2 = new LinearRegressionAdaptiveTimeout();
		adaptTimeout2.init();
		for (int i = 0; i < 10; i++) {
			adaptTimeout2.reportOneStepProgress();
		}
		given(System.currentTimeMillis()).willReturn(1000L);
		adaptTimeout2.reportOneStepProgress();

		final LinearRegressionAdaptiveTimeout adaptTimeout3 = new LinearRegressionAdaptiveTimeout();
		adaptTimeout3.init();
		for (int i = 0; i < 9; i++) {
			adaptTimeout3.reportOneStepProgress();
		}
		given(System.currentTimeMillis()).willReturn(2000L);
		adaptTimeout3.reportOneStepProgress();

	}

	/**
	 * Test method for {@link LinearRegressionAdaptiveTimeout#isReached()}.
	 */
	@Test
	public void isReached() {
		mockStatic(System.class);
		given(System.currentTimeMillis()).willReturn(0L);

		final LinearRegressionAdaptiveTimeout adaptTimeout = new LinearRegressionAdaptiveTimeout();
		adaptTimeout.init();
		assertThat(adaptTimeout.isReached(), equalTo(false));

		final LinearRegressionAdaptiveTimeout adaptTimeout1 = new LinearRegressionAdaptiveTimeout();
		adaptTimeout1.init();
		for (int i = 0; i < 10; i++) {
			adaptTimeout1.reportOneStepProgress();
		}
		assertThat(adaptTimeout1.isReached(), equalTo(false));

		final LinearRegressionAdaptiveTimeout adaptTimeout2 = new LinearRegressionAdaptiveTimeout();
		adaptTimeout2.init();
		for (int i = 0; i < 10; i++) {
			adaptTimeout2.reportOneStepProgress();
		}
		given(System.currentTimeMillis()).willReturn(1000L);
		assertThat(adaptTimeout2.isReached(), equalTo(true));

		final LinearRegressionAdaptiveTimeout adaptTimeout3 = new LinearRegressionAdaptiveTimeout();
		adaptTimeout3.init();
		for (int i = 0; i < 10; i++) {
			adaptTimeout3.reportOneStepProgress();
		}
		given(System.currentTimeMillis()).willReturn(2L * 1000);

		adaptTimeout3.reportOneStepProgress();

		assertThat(adaptTimeout3.isReached(), equalTo(true));
	}

	/**
	 * Test method for {@link LinearRegressionAdaptiveTimeout#notifyOnReachedTimeout()}.
	 */
	@Ignore
	// This test is not ready yet.
	@Test
	public void notifyOnReachedTimeout() {
		final LinearRegressionAdaptiveTimeout adaptTimeout = new LinearRegressionAdaptiveTimeout();
		adaptTimeout.init();
		for (int i = 0; i < 10; i++) {
			adaptTimeout.reportOneStepProgress();
		}

	}

}
