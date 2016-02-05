package de.uka.ipd.sdq.beagle.core.measurement;

import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

import de.uka.ipd.sdq.beagle.core.testutil.ThrowingMethod;

import org.junit.Test;

/**
 * Tests {@link LoopRepetitionCountMeasurementResult} and contains all test cases needed
 * to check every method.
 *
 * @author Annika Berger
 */
public class LoopRepetitionCountMeasurementResultTest {

	/**
	 * Test method for
	 * {@link LoopRepetitionCountMeasurementResult#LoopRepetitionCountMeasurementResult(int)}
	 * .
	 *
	 * <p>Asserts that an {@link IllegalArgumentException} is thrown if the value is
	 * smaller than 0. Asserts that there are no exceptions for valid inputs.
	 */
	@Test
	public void constructor() {
		final int count = 0;
		new LoopRepetitionCountMeasurementResult(count);
		final int count1 = 3;
		new LoopRepetitionCountMeasurementResult(count1);
		final int negativeValue = -2;
		final ThrowingMethod method = () -> {
			new LoopRepetitionCountMeasurementResult(negativeValue);
		};
		assertThat("Count must be non-negative.", method, throwsException(IllegalArgumentException.class));
	}

	/**
	 * Test method for
	 * {@link LoopRepetitionCountMeasurementResult#LoopRepetitionCountMeasurementResult(Parameterisation, int)}
	 * .
	 *
	 * <p>Asserts that an {@link IllegalArgumentException} is thrown if the value is
	 * smaller than 0 and an {@link NullPointerException} is thrown if the
	 * parameterisation is null. Asserts that there are no exceptions for valid inputs.
	 */
	@Test
	public void constructorWithParameterisation() {
		final Parameterisation parameterisation = mock(Parameterisation.class);
		final int count = 0;
		new LoopRepetitionCountMeasurementResult(parameterisation, count);
		final int count1 = 3;
		new LoopRepetitionCountMeasurementResult(parameterisation, count1);
		final int negativeValue = -2;
		ThrowingMethod method = () -> {
			new LoopRepetitionCountMeasurementResult(parameterisation, negativeValue);
		};
		assertThat("Count must be non-negative.", method, throwsException(IllegalArgumentException.class));
		method = () -> {
			new LoopRepetitionCountMeasurementResult(null, count);
		};
		assertThat("Parameterisation must not be null.", method, throwsException(NullPointerException.class));
	}

	/**
	 * Test method for {@link LoopRepetitionCountMeasurementResult#getCount()}.
	 *
	 * <p>Asserts that correct results are returned for the value and that changing the
	 * count after instantiation does not change the output value.
	 */
	@Test
	public void getCount() {
		final Parameterisation parameterisation = mock(Parameterisation.class);
		final int count = 0;
		final LoopRepetitionCountMeasurementResult result = new LoopRepetitionCountMeasurementResult(count);
		final int count1 = 3;
		final LoopRepetitionCountMeasurementResult result1 = new LoopRepetitionCountMeasurementResult(count1);
		final LoopRepetitionCountMeasurementResult resultWithP =
			new LoopRepetitionCountMeasurementResult(parameterisation, count);

		assertThat(result.getCount(), is(equalTo(count)));
		assertThat(result1.getCount(), is(equalTo(count1)));
		assertThat(resultWithP.getCount(), is(equalTo(count)));
	}

	/**
	 * Test method for {@link LoopRepetitionCountMeasurementResult#toString()} .
	 */
	@Test
	public void toStringT() {
		final Parameterisation parameterisation = mock(Parameterisation.class);
		final int value = 2;
		final LoopRepetitionCountMeasurementResult measurementResult = new LoopRepetitionCountMeasurementResult(value) {
		};
		final LoopRepetitionCountMeasurementResult measurementResultP =
			new LoopRepetitionCountMeasurementResult(parameterisation, value) {
			};
		assertThat(measurementResult.toString(), not(startsWith("LoopRepetitionCountMeasurementResult@")));
		assertThat(measurementResultP.toString(), not(startsWith("LoopRepetitionCountMeasurementResult@")));
	}

}
