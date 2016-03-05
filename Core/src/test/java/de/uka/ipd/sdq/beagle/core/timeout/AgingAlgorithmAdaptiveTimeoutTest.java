package de.uka.ipd.sdq.beagle.core.timeout;

import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import de.uka.ipd.sdq.beagle.core.testutil.ThrowingMethod;

import org.junit.Rule;
import org.junit.Test;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;

/**
 * Tests {@link AgingAlgorithmAdaptiveTimeout} and contains all test cases needed to check
 * every method.
 *
 * @author Michael Vogt
 */
@PrepareForTest(AgingAlgorithmAdaptiveTimeout.class)
public class AgingAlgorithmAdaptiveTimeoutTest {

	/**
	 * Loads PowerMockito to mock the static final class {@link System}.
	 */
	@Rule
	public final PowerMockRule loadPowerMockito = new PowerMockRule();

	/**
	 * Test method for {@link AgingAlgorithmAdaptiveTimeout#isReached()}.
	 */
	@Test
	public void isReached() {
		final AgingAlgorithmAdaptiveTimeout adaptTimeout = new AgingAlgorithmAdaptiveTimeout();
		mockStatic(System.class);
		given(System.currentTimeMillis()).willReturn(0L);
		adaptTimeout.implementationInit();
		assertThat(adaptTimeout.isReached(), equalTo(false));

		final AgingAlgorithmAdaptiveTimeout adaptTimeout2 = new AgingAlgorithmAdaptiveTimeout();
		adaptTimeout2.implementationInit();
		given(System.currentTimeMillis()).willReturn(2L * (3600 * 1000));
		assertThat(adaptTimeout2.isReached(), equalTo(true));
	}

	/**
	 * Test method for {@link AgingAlgorithmAdaptiveTimeout#reportOneStepProgress()}.
	 */
	@Test
	public void reportOneStepProgress() {
		final AgingAlgorithmAdaptiveTimeout adaptTimeout = new AgingAlgorithmAdaptiveTimeout();
		adaptTimeout.implementationInit();
		adaptTimeout.reportOneStepProgress();
	}

	/**
	 * Test that init() is called at first method and only once.
	 */
	@Test
	public void init() {
		final AgingAlgorithmAdaptiveTimeout adaptTimeout1 = new AgingAlgorithmAdaptiveTimeout();
		adaptTimeout1.implementationInit();
		final ThrowingMethod method = () -> {
			adaptTimeout1.implementationInit();
		};
		assertThat("adaptTimeout1 must not be allowed to call init() more than one time.", method,
			throwsException(IllegalStateException.class));

		final AgingAlgorithmAdaptiveTimeout adaptTimeout = new AgingAlgorithmAdaptiveTimeout();
		adaptTimeout.implementationInit();
	}

}
