package de.uka.ipd.sdq.beagle.core.timeout;

import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

import de.uka.ipd.sdq.beagle.core.testutil.ThrowingMethod;

import org.junit.Test;

/**
 * Tests {@link ExecutionTimeBasedTimeout} and contains all test cases needed to check
 * every method.
 *
 * @author Michael Vogt
 */
public class ExecutionTimeBasedTimeoutTest {

	/**
	 * Implementation of a {@link ExecutionTimeBasedTimeout}, which only implements the
	 * methods to implement.
	 */
	private final ExecutionTimeBasedTimeoutDummy executionTimeBasedTimeout = new ExecutionTimeBasedTimeoutDummy();

	/**
	 * Test method for {@link ExecutionTimeBasedTimeout#init()}.
	 */
	@Test
	public void init() {
		this.executionTimeBasedTimeout.init();
		final ThrowingMethod method = () -> {
			this.executionTimeBasedTimeout.init();
		};
		assertThat("executionTimeBasedTimeout must not be allowed to call init() more than one time.", method,
			throwsException(IllegalStateException.class));
	}

	/**
	 * Test method for {@link ExecutionTimeBasedTimeout#registerCallback()}.
	 */
	@Test
	public void registerCallback() {
		final Runnable callback = mock(Runnable.class);
		this.executionTimeBasedTimeout.init();
		this.executionTimeBasedTimeout.registerCallback(callback);
		assertThat(this.executionTimeBasedTimeout.callbacks.contains(callback), is(true));
	}

	/**
	 * Test method for {@link ExecutionTimeBasedTimeout#unregisterCallback()}.
	 */
	@Test
	public void unregisterCallback() {
		final Runnable callback = mock(Runnable.class);
		this.executionTimeBasedTimeout.init();
		this.executionTimeBasedTimeout.registerCallback(callback);
		this.executionTimeBasedTimeout.unregisterCallback(callback);
		assertThat(this.executionTimeBasedTimeout.callbacks.contains(callback), is(false));
	}

	/**
	 * Class used to get a ExecutionTimeBasedTimeout instance.
	 *
	 * @author Michael Vogt
	 */
	private class ExecutionTimeBasedTimeoutDummy extends ExecutionTimeBasedTimeout {

		@Override
		public boolean isReached() {
			return false;
		}

		@Override
		public void reportOneStepProgress() {
		}

		@Override
		protected void implementationInit() {
		}

	}

}
