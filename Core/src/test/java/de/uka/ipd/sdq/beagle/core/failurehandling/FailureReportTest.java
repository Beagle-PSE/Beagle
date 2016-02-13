package de.uka.ipd.sdq.beagle.core.failurehandling;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
 * Tests {@link BeahgleConfiguration} and contains all test cases needed to check every
 * method.
 * 
 * @author Michael Vogt
 */
public class FailureReportTest {

	/**
	 * Message giving information about the failure.
	 */
	private String failureMessage;

	/**
	 * Wheter details about the failure have yet been provided.
	 */
	private boolean detailsProvided;

	/**
	 * Exception that caused or indicated the failure.
	 */
	private Exception failureCause;

	// /**
	// * A routine to call if it is wished to continue by ignoring the failure.
	// */
	// private Supplier<RECOVER_TYPE> continueFunction;
	//
	// /**
	// * A routine to call if it is wished to retry the action the failure occurred at.
	// */
	// private Supplier<RECOVER_TYPE> retryFunction;
	//
	// @Before
	// public void initialiseContinueFunction() {
	// this.continueFunction = Supplier<>();
	// this.continueFunction.add(mock(RECOVER_TYPE));
	// }

	/**
	 * Test method for {@link FailureReport#getMessage(String, Object...)}.
	 */
	@Test
	public void getMessageTest() {
		final FailureReport failReport = new FailureReport<>();
		assertThat(failReport.getFailureMessage(), is(this.failureMessage));

	}

	/**
	 * Test method for {@link FailureReport#getDetails()}.
	 */
	@Test
	public void getDetails() {
		this.detailsProvided = false;
		final FailureReport failReport = new FailureReport<>();
		assertThat(failReport.getDetails(), is(this.detailsProvided));
	}

	/**
	 * Test method for {@link FailureReport#getFailureCause()}.
	 */
	@Test
	public void getFailureCause() {
		final FailureReport failReport = new FailureReport<>();
		assertThat(failReport.getFailureCause(), is(this.failureCause));
	}

	/**
	 * Test method for {@link FailureReport#getContinueRoutine()}.
	 */
	// @Test
	// public void getContinueRoutine() {
	// final FailureReport failReport = new FailureReport<>();
	// assertThat(failReport.getContinueRoutine(), is(this.continueFunction));
	// }
	//
	// /**
	// * Test method for {@link FailureReport#getRetryRoutine()}.
	// */
	// @Test
	// public void getRetryRoutine() {
	// final FailureReport failReport = new FailureReport<>();
	// assertThat(failReport.getRetryRoutine(), is(this.retryFunction));
	// }
	//
}
