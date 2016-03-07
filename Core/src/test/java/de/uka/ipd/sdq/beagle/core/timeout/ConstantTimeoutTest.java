package de.uka.ipd.sdq.beagle.core.timeout;

import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import de.uka.ipd.sdq.beagle.core.testutil.ThrowingMethod;

import org.junit.Rule;
import org.junit.Test;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;

/**
 * Tests {@link ConstantTimeout} and contains all test cases needed to check every method.
 *
 * @author Michael Vogt
 */
@PrepareForTest(ConstantTimeout.class)
public class ConstantTimeoutTest {

	/**
	 * Loads PowerMockito to mock the static final class {@link System}.
	 */
	@Rule
	public final PowerMockRule loadPowerMockito = new PowerMockRule();

	/**
	 * Test method for the constructor.
	 */
	@Test
	public void constructorConstantTimeout() {
		final int negTimeout = -100;
		final ThrowingMethod method = () -> {
			final ConstantTimeout constTimeout1 = new ConstantTimeout(negTimeout);
			constTimeout1.implementationInit();
		};
		assertThat("timeout must not be negative.", method, throwsException(IllegalArgumentException.class));
	}

	/**
	 * Test method for {@link ConstantTimeout#isReached()}.
	 */
	@Test
	public void isReached() {
		final int timeout = 100;
		final long longerThanTimeout = 200;
		mockStatic(System.class);

		given(System.currentTimeMillis()).willReturn(0L);
		final ConstantTimeout constTimeout = new ConstantTimeout(timeout);
		constTimeout.init();
		assertThat(constTimeout.isReached(), is(equalTo(false)));

		final ConstantTimeout constTimeout1 = new ConstantTimeout(timeout);
		constTimeout1.init();
		given(System.currentTimeMillis()).willReturn(longerThanTimeout);
		assertThat(constTimeout1.isReached(), is(equalTo(true)));

		final ConstantTimeout constTimeout3 = new ConstantTimeout(timeout);
		assertThat(constTimeout3.initialised, is(equalTo(false)));
	}

	/**
	 * Test method for {@link ConstantTimeout#getTimeout()}.
	 */
	@Test
	public void getTimeout() {
		final int timeout = 100;
		final ConstantTimeout constTimeout = new ConstantTimeout(timeout);
		constTimeout.init();
		assertThat(constTimeout.getTimeout(), is((long) timeout));
		final ConstantTimeout constTimeout3 = new ConstantTimeout(timeout);

		assertThat(constTimeout3.initialised, is(equalTo(false)));

	}

	/**
	 * Test method for {@link ConstantTimeout#reportOneStepProgress()}.
	 */
	@Test
	public void reportOneStepProgress() {
		final int timeout = 100;
		final ConstantTimeout constTimeout = new ConstantTimeout(timeout);
		constTimeout.init();
		constTimeout.reportOneStepProgress();
	}

	/**
	 * Test method for {@link ConstantTimeout#implementationInit()}.
	 */
	@Test
	public void init() {
		final int valideTimeout = 100;
		final ConstantTimeout constTimeout = new ConstantTimeout(valideTimeout);
		constTimeout.init();
		final ThrowingMethod method = () -> {
			constTimeout.init();
		};
		assertThat("constTimeout must not be allowed to call init() more than one time.", method,
			throwsException(IllegalStateException.class));

		final ConstantTimeout constTimeout1 = new ConstantTimeout(valideTimeout);
		assertThat(constTimeout1.initialised, is(equalTo(false)));
	}

	/**
	 * Test method for {@link ConstantTimeout#notifyOnReachedTimeout()}.
	 */
	@Test
	public void notifyOnReachedTimeout() {
		final int valideTimeout = 100;
		final Runnable callback1 = mock(Runnable.class);
		final ConstantTimeout constTimeout = new ConstantTimeout(valideTimeout);
		constTimeout.registerCallback(callback1);
		constTimeout.init();

	}
}
