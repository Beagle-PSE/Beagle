package de.uka.ipd.sdq.beagle.core.measurement;

import static de.uka.ipd.sdq.beagle.core.testutil.EqualsMatcher.hasDefaultEqualsProperties;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

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
	public void testParameterisationDependentMeasurementResult() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link ParameterisationDependentMeasurementResult#ParameterisationDependentMeasurementResult(Parameterisation)}
	 * .
	 */
	@Test
	public void testParameterisationDependentMeasurementResultParameterisation() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link ParameterisationDependentMeasurementResult#getParameterisation()} .
	 */
	@Test
	public void testGetParameterisation() {
		fail("Not yet implemented");
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
	public void testEqualsObject() {
		final Parameterisation parameterisation1 = mock(Parameterisation.class);
		final Parameterisation parameterisation2 = mock(Parameterisation.class);
		final Parameterisation parameterisation3 = mock(Parameterisation.class);
		given(parameterisation1.equals(parameterisation2)).willReturn(true);
		given(parameterisation2.equals(parameterisation3)).willReturn(false);
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
		assertThat(measurementResult, hasDefaultEqualsProperties());
		assertThat(measurementResult, is(not(equalTo(measurementResultP1))));
		assertThat(measurementResultP1, is(equalTo(measurementResultP2)));
		assertThat(measurementResultP1.hashCode(), is(equalTo(measurementResultP2.hashCode())));
		assertThat(measurementResultP2, is(not(equalTo(measurementResultP3))));
	}

	/**
	 * Test method for {@link ParameterisationDependentMeasurementResult#toString()} .
	 */
	@Test
	public void testToString() {
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
