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
 * Tests {@link ResourceDemandMeasurementResult} and contains all test cases needed to
 * check every method.
 * 
 * @author Annika Berger
 */
public class ResourceDemandMeasurementResultTest {

	/**
	 * Test method for
	 * {@link ResourceDemandMeasurementResult#ResourceDemandMeasurementResult(double)}.
	 * 
	 * <p>Asserts that an {@link IllegalArgumentException} is thrown if the value is
	 * smaller than 0. Asserts that there are no exceptions for valid inputs.
	 */
	@Test
	public void testResourceDemandMeasurementResultDouble() {
		final double value = 2.3;
		new ResourceDemandMeasurementResult(value);
		final double value2 = 0;
		new ResourceDemandMeasurementResult(value2);
		final double negativeValue = -2;
		final ThrowingMethod method = () -> {
			new ResourceDemandMeasurementResult(negativeValue);
		};
		assertThat("Value must not be smaller than zero.", method, throwsException(IllegalArgumentException.class));

	}

	/**
	 * Test method for
	 * {@link ResourceDemandMeasurementResult#ResourceDemandMeasurementResult(Parameterisation, double)}
	 * .
	 * 
	 * <p>Asserts that an {@link IllegalArgumentException} is thrown if the value is
	 * smaller than 0 and an {@link NullPointerException} is thrown if the
	 * parameterisation is null. Asserts that there are no exceptions for valid inputs.
	 */
	@Test
	public void testResourceDemandMeasurementResultParameterisationDouble() {
		final Parameterisation parameterisation = mock(Parameterisation.class);
		final double value = 2.3;
		new ResourceDemandMeasurementResult(parameterisation, value);
		final double negativeValue = -2;
		ThrowingMethod method = () -> {
			new ResourceDemandMeasurementResult(parameterisation, negativeValue);
		};
		assertThat("Value must not be smaller than zero.", method, throwsException(IllegalArgumentException.class));
		method = () -> {
			new ResourceDemandMeasurementResult(null, value);
		};
		assertThat("Parameterisation must not be null.", method, throwsException(NullPointerException.class));
	}

	/**
	 * Test method for {@link ResourceDemandMeasurementResult#getValue()}.
	 * 
	 * <p>Asserts that correct results are returned for the value and that changing the
	 * value after instantiation does not change the output value.
	 */
	@Test
	public void testGetValue() {
		double value = 2.3;
		final ResourceDemandMeasurementResult result1 = new ResourceDemandMeasurementResult(value);
		final double value2 = 0;
		final ResourceDemandMeasurementResult result2 = new ResourceDemandMeasurementResult(value2);
		final Parameterisation parameterisation = mock(Parameterisation.class);
		final ResourceDemandMeasurementResult resultWithParameterisation =
			new ResourceDemandMeasurementResult(parameterisation, value);

		assertThat(result1.getValue(), is(equalTo(value)));
		assertThat(result2.getValue(), is(equalTo(value2)));
		assertThat(resultWithParameterisation, is(equalTo(value)));
		value = 4.5;
		assertThat("The returned value must not be influenced by a later change.", result1.getValue(),
			is(not(equalTo(value))));
		assertThat("The returned value must not be influenced by a later change.", result2.getValue(),
			is(not(equalTo(value2))));

	}

}
