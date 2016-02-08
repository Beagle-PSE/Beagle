package de.uka.ipd.sdq.beagle.core.measurement;

import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.startsWith;
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
	 * Test method for
	 * {@link ParameterisationDependentMeasurementResult#equals(java.lang.Object)} and
	 * {@link ParameterisationDependentMeasurementResult#hashCode()}.
	 *
	 * <p>Asserts that equals has the default properties, a
	 * {@link ParameterisationDependentMeasurementResult} with {@link Parameterisation} is
	 * not equal to one without, and that if the {@link Parameterisation} is (not) equal
	 * to the one of the other {@link ParameterisationDependentMeasurementResult} these
	 * are (not) equal to.
	 *
	 * <p>Asserts that if the {@link ParameterisationDependentMeasurementResult} are equal
	 * the hashCode is the same.
	 */
	@Test
	public void equalsObject() {
		/*
		 * This simulates parameterisation1.equals(paramertisation2) returns true, while
		 * parameterisation3.equals(parameterisation4) returns false.
		 */
		final ParameterisationMock parameterisation1 = new ParameterisationMock();
		final ParameterisationMock parameterisation2 = new ParameterisationMock();
		final ParameterisationMock parameterisation3 = new ParameterisationMock();
		final ParameterisationMock parameterisation4 = new ParameterisationMock();
		parameterisation1.setEquals(true);
		parameterisation2.setEquals(true);
		parameterisation3.setEquals(false);
		parameterisation4.setEquals(false);
		final ParameterisationDependentMeasurementResult measurementResult =
			new ParameterisationDependentMeasurementResult() {
			};
		final ParameterisationDependentMeasurementResult measurementResultP1 =
			new ParameterisationDependentMeasurementResult(parameterisation1) {
			};
		final ParameterisationDependentMeasurementResult measurementResultP2 =
			new ParameterisationDependentMeasurementResult(parameterisation2) {
			};
		final ParameterisationDependentMeasurementResult measurementResultP3 =
			new ParameterisationDependentMeasurementResult(parameterisation3) {
			};
		final ParameterisationDependentMeasurementResult measurementResultP4 =
			new ParameterisationDependentMeasurementResult(parameterisation4) {
			};

		assertThat(measurementResult, hasDefaultEqualsProperties());
		assertThat(measurementResult, is(not(equalTo(measurementResultP1))));
		assertThat(measurementResultP1, is(not(equalTo(measurementResultP2))));
		assertThat(measurementResultP3, is(not(equalTo(measurementResultP4))));
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
		assertThat(measurementResult.toString(), not(startsWith("ParameterisationDependentMeasurementResult@")));
		assertThat(measurementResultP.toString(), not(startsWith("ParameterisationDependentMeasurementResult@")));
	}

}
