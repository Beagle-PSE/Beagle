package de.uka.ipd.sdq.beagle.core.measurement;

import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

import de.uka.ipd.sdq.beagle.core.testutil.ThrowingMethod;

import org.junit.Test;

/**
 * Tests {@link BranchDecisionMeasurementResult} and contains all test cases needed to
 * check every method.
 * 
 * @author Annika Berger
 */
public class BranchDecisionMeasurementResultTest {

	/**
	 * Test method for
	 * {@link BranchDecisionMeasurementResult#BranchDecisionMeasurementResult(int)}.
	 * 
	 * <p>Asserts that an {@link IllegalArgumentException} is thrown if the value is
	 * smaller than 0. Asserts that there are no exceptions for valid inputs.
	 */
	@Test
	public void testBranchDecisionMeasurementResultInt() {
		final int branchIndex = 0;
		new BranchDecisionMeasurementResult(branchIndex);
		final int branchIndex1 = 3;
		new BranchDecisionMeasurementResult(branchIndex1);
		final int negativeValue = -2;
		final ThrowingMethod method = () -> {
			new BranchDecisionMeasurementResult(negativeValue);
		};
		assertThat("Branch Index must be non-negative.", method, throwsException(IllegalArgumentException.class));
	}

	/**
	 * Test method for
	 * {@link BranchDecisionMeasurementResult#BranchDecisionMeasurementResult(Parameterisation, int)}
	 * .
	 * 
	 * <p>Asserts that an {@link IllegalArgumentException} is thrown if the value is
	 * smaller than 0 and an {@link NullPointerException} is thrown if the
	 * parameterisation is null. Asserts that there are no exceptions for valid inputs.
	 */
	@Test
	public void testBranchDecisionMeasurementResultParameterisationInt() {
		final Parameterisation parameterisation = mock(Parameterisation.class);
		final int branchIndex = 0;
		new BranchDecisionMeasurementResult(branchIndex);
		final int branchIndex1 = 3;
		new BranchDecisionMeasurementResult(branchIndex1);
		final int negativeValue = -2;
		ThrowingMethod method = () -> {
			new BranchDecisionMeasurementResult(parameterisation, negativeValue);
		};
		assertThat("Count must be non-negative.", method, throwsException(IllegalArgumentException.class));
		method = () -> {
			new BranchDecisionMeasurementResult(null, branchIndex);
		};
		assertThat("Parameterisation must not be null.", method, throwsException(NullPointerException.class));
	}

	/**
	 * Test method for {@link BranchDecisionMeasurementResult#getBranchIndex()}.
	 * 
	 * <p>Asserts that correct results are returned for the value and that changing the
	 * index after instantiation does not change the output value.
	 */
	@Test
	public void testGetBranchIndex() {
		final Parameterisation parameterisation = mock(Parameterisation.class);
		int branchIndex = 0;
		final BranchDecisionMeasurementResult result = new BranchDecisionMeasurementResult(branchIndex);
		final int branchIndex1 = 3;
		final BranchDecisionMeasurementResult result1 = new BranchDecisionMeasurementResult(branchIndex1);
		final BranchDecisionMeasurementResult resultWithP =
			new BranchDecisionMeasurementResult(parameterisation, branchIndex);

		assertThat(result.getBranchIndex(), is(equalTo(branchIndex)));
		assertThat(result1.getBranchIndex(), is(equalTo(branchIndex1)));
		assertThat(resultWithP, is(equalTo(branchIndex)));
		branchIndex = 4;
		assertThat("The returned index must not be influenced by a later change.", result.getBranchIndex(),
			is(not(equalTo(branchIndex))));
		assertThat("The returned index must not be influenced by a later change.", result1.getBranchIndex(),
			is(not(equalTo(branchIndex))));
	}

}
