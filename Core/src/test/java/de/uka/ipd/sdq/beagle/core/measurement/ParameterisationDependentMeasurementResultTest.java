package de.uka.ipd.sdq.beagle.core.measurement;

import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static de.uka.ipd.sdq.beagle.core.testutil.ToStringMatcher.hasOverriddenToString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

import de.uka.ipd.sdq.beagle.core.testutil.ThrowingMethod;

import org.junit.Test;

/**
 * Tests {@link ParameterisationDependentMeasurementResult} and contains all test cases
 * needed to check every method.
 *
 * @author Annika Berger
 */
public class ParameterisationDependentMeasurementResultTest {

	/**
	 * Test method for
	 * {@link ParameterisationDependentMeasurementResult#ParameterisationDependentMeasurementResult()}
	 * .
	 */
	@Test
	public void constructor() {
		new ParameterisationDependentMeasurementResult() {
		};
	}

	/**
	 * Test method for
	 * {@link ParameterisationDependentMeasurementResult#ParameterisationDependentMeasurementResult(Parameterisation)}
	 * .
	 */
	@Test
	public void constructorWithParameterisation() {
		final Parameterisation parameterisation = mock(Parameterisation.class);
		new ParameterisationDependentMeasurementResult(parameterisation) {
		};
		final ThrowingMethod method = () -> {
			new ParameterisationDependentMeasurementResult(null) {
			};
		};
		assertThat("Parameterisation must not be null.", method, throwsException(NullPointerException.class));
	}

	/**
	 * Test method for
	 * {@link ParameterisationDependentMeasurementResult#getParameterisation()} .
	 *
	 * <p>Asserts that correct parameterisation is returned for valid parameterisation and
	 * that {@code null} is returned if no parameterisation is defined.
	 */
	@Test
	public void getParameterisation() {
		final Parameterisation parameterisation = mock(Parameterisation.class);
		final ParameterisationDependentMeasurementResult measurementResult =
			new ParameterisationDependentMeasurementResult(parameterisation) {
			};
		assertThat(measurementResult.getParameterisation(), is(parameterisation));
		final ParameterisationDependentMeasurementResult measurementResultWithoutParamerterisation =
			new ParameterisationDependentMeasurementResult() {
			};
		assertThat(measurementResultWithoutParamerterisation.getParameterisation(), is(nullValue()));
	}

	/**
	 * Test method for {@link ParameterisationDependentMeasurementResult#toString()} .
	 */
	@Test
	public void toStringT() {
		final Parameterisation parameterisation = mock(Parameterisation.class);
		final ParameterisationDependentMeasurementResult measurementResult =
			new ParameterisationDependentMeasurementResult() {
			};
		final ParameterisationDependentMeasurementResult measurementResultP =
			new ParameterisationDependentMeasurementResult(parameterisation) {
			};
		assertThat(measurementResult, hasOverriddenToString());
		assertThat(measurementResultP, hasOverriddenToString());
	}

}
