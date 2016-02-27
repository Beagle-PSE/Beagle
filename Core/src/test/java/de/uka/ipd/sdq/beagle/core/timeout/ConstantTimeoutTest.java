package de.uka.ipd.sdq.beagle.core.timeout;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
 * Tests {@link ConstantTimeout} and contains all test cases needed to check every method.
 *
 * @author Michael Vogt
 */
public class ConstantTimeoutTest {

	/**
	 * Test method for {@link ConstantTimeout#isReached()}.
	 */
	@Test
	public void isReached() {
		final int timeout = 100;
		final int longerThanTimeout = 200;
		final ConstantTimeout constTimeout = new ConstantTimeout(timeout);
		assertThat(constTimeout.isReached(), is(equalTo(false)));

		final ConstantTimeout constTimeout1 = new ConstantTimeout(timeout);
		constTimeout1.init();
		try {
			constTimeout1.wait(longerThanTimeout);
		} catch (final InterruptedException e) {
		}
		assertThat(constTimeout1.isReached(), is(equalTo(true)));
	}

	/**
	 * Test method for {@link ConstantTimeout#getTimeout()}.
	 */
	@Test
	public void getTimeout() {
		final int timeout = 100;
		final ConstantTimeout constTimeout = new ConstantTimeout(timeout);
		assertThat(constTimeout.getTimeout(), is((long) timeout));

	}

	/**
	 * Test method for {@link ConstantTimeout#reportOneStepProgress()}.
	 */
	@Test
	public void reportOneStepProgress() {
		final int timeout = 100;
		final ConstantTimeout constTimeout = new ConstantTimeout(timeout);
		constTimeout.reportOneStepProgress();
	}

}
