package de.uka.ipd.sdq.beagle.core.timeout;

import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

import de.uka.ipd.sdq.beagle.core.testutil.ThrowingMethod;

import org.junit.Test;

/**
 * Tests {@link ConstantTimeout} and contains all test cases needed to check every method.
 *
 * @author Michael Vogt
 */
public class ConstantTimeoutTest {

	/**
	 * Test method for the constructor.
	 */
	@Test
	public void constructorConstantTimeout() {
		final int negTimeout = -100;
		final int valideTimeout = 100;
		ThrowingMethod method = () -> {
			final ConstantTimeout constTimeout1 = new ConstantTimeout(negTimeout);
			constTimeout1.init();
		};
		assertThat("timeout must not be negative.", method, throwsException(IllegalArgumentException.class));

		method = () -> {
			final ConstantTimeout constTimeout2 = new ConstantTimeout(valideTimeout);
			constTimeout2.init();
			constTimeout2.init();
		};
		assertThat("constTimeout must not be allowed to call init() more than one time.", method,
			throwsException(IllegalStateException.class));

		method = () -> {
			new ConstantTimeout(valideTimeout);
		};
		assertThat("init() must be called once.", method, throwsException(IllegalStateException.class));

	}

	/**
	 * Test method for {@link ConstantTimeout#isReached()}.
	 */
	@Test
	public void isReached() {
		final int timeout = 100;

		final ConstantTimeout constTimeout = new ConstantTimeout(timeout);
		constTimeout.init();
		assertThat(constTimeout.isReached(), is(equalTo(false)));

		final ConstantTimeout constTimeout1 = new ConstantTimeout(timeout);
		given(System.currentTimeMillis()).willReturn(486487484886446L);
		constTimeout1.init();
		given(System.currentTimeMillis()).willReturn(486487484886446L + timeout);
		assertThat(constTimeout1.isReached(), is(equalTo(true)));
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

}
