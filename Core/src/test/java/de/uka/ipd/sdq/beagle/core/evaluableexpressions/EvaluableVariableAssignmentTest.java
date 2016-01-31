package de.uka.ipd.sdq.beagle.core.evaluableexpressions;

import static de.uka.ipd.sdq.beagle.core.testutil.EqualsMatcher.hasDefaultEqualsProperties;
import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static de.uka.ipd.sdq.beagle.core.testutil.ToStringMatcher.hasOverriddenToString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.number.IsNaN.notANumber;

import org.junit.Test;

/**
 * Tests for {@link EvaluableVariableAssignment}.
 *
 * @author Joshua Gleitze
 */
public class EvaluableVariableAssignmentTest {

	/**
	 * Test method for {@link EvaluableVariableAssignment#EvaluableVariableAssignment()}.
	 * Asserts that construction is possible.
	 */
	@Test
	public void testConstructor() {
		new EvaluableVariableAssignment();
	}

	/**
	 * Test method for {@link EvaluableVariableAssignment#getValueFor(EvaluableVariable)}.
	 * Asserts that:
	 *
	 * <ul>
	 *
	 * <li> It returns set values and supports overriding too.
	 *
	 * <li> {@code null} cannot be passed.
	 *
	 * </ul>
	 */
	@Test
	public void testGetValueFor() {
		final EvaluableVariableAssignment testAssignment = new EvaluableVariableAssignment();
		final EvaluableVariable testVariable = new EvaluableVariable("foo");

		assertThat("setting for null must not be possible!", () -> testAssignment.setValueFor(null, 14),
			throwsException(NullPointerException.class));
		assertThat(testAssignment.getValueFor(testVariable), is(nullValue()));
		testAssignment.setValueFor(testVariable, 14);
		assertThat(testAssignment.getValueFor(testVariable), is(14d));
		testAssignment.setValueFor(testVariable, -9);
		assertThat(testAssignment.getValueFor(testVariable), is(-9d));
		testAssignment.setValueFor(new EvaluableVariable("bar"), Double.NaN);
		assertThat(testAssignment.getValueFor(new EvaluableVariable("bar")), is(notANumber()));
		assertThat(testAssignment.getValueFor(testVariable), is(-9d));
	}

	/**
	 * Test method for
	 * {@link EvaluableVariableAssignment#setValueFor(EvaluableVariable, double)}. Asserts
	 * that:
	 *
	 * <ul>
	 *
	 * <li> Setting values is possible, overriding too.
	 *
	 * <li> {@code null} cannot be passed.
	 *
	 * </ul>
	 */
	@Test
	public void testSetValueFor() {
		final EvaluableVariableAssignment testAssignment = new EvaluableVariableAssignment();
		final EvaluableVariable testVariable = new EvaluableVariable("foo");

		assertThat("setting for null must not be possible!", () -> testAssignment.setValueFor(null, 14),
			throwsException(NullPointerException.class));
		testAssignment.setValueFor(testVariable, 14);
		testAssignment.setValueFor(testVariable, -9);
		testAssignment.setValueFor(new EvaluableVariable("bar"), Double.NaN);
	}

	/**
	 * Test method for
	 * {@link EvaluableVariableAssignment#isValueAssignedFor(EvaluableVariable)}. Asserts
	 * that results are correct and {@code null} cannot be passed.
	 */
	@Test
	public void testIsValueAssignedFor() {
		final EvaluableVariableAssignment testAssignment = new EvaluableVariableAssignment();
		final EvaluableVariable testVariable = new EvaluableVariable("foo");

		assertThat("setting for null must not be possible!", () -> testAssignment.isValueAssignedFor(null),
			throwsException(NullPointerException.class));
		assertThat("initially, there shouldn’t be an assignment", testAssignment.isValueAssignedFor(testVariable),
			is(false));
		testAssignment.setValueFor(testVariable, 14);
		assertThat(testAssignment.isValueAssignedFor(testVariable), is(true));
		testAssignment.setValueFor(testVariable, -9);
		assertThat(testAssignment.isValueAssignedFor(testVariable), is(true));
		assertThat("initially, there shouldn’t be an assignment",
			testAssignment.isValueAssignedFor(new EvaluableVariable("bar")), is(false));
	}

	/**
	 * Test method for {@link EvaluableVariableAssignment#toString()}. Asserts that the
	 * method is overridden.
	 */
	@Test
	public void testToString() {
		final EvaluableVariableAssignment testedAssignment = new EvaluableVariableAssignment();
		testedAssignment.setValueFor(new EvaluableVariable("x"), 2d);
		testedAssignment.setValueFor(new EvaluableVariable("y"), 8d);
		assertThat(testedAssignment, hasOverriddenToString());
	}

	/**
	 * Test method for {@link EvaluableVariableAssignment#equals(java.lang.Object)} and
	 * {@link EvaluableVariableAssignment#hashCode()}. Asserts that:
	 *
	 * <ul>
	 *
	 * <li>Two assignments are equal exactly if they realise the the same function.
	 *
	 * <li>{@code hashCode} follows its contract.
	 *
	 * </ul>
	 */
	@Test
	public void testEqualsAndHashCode() {
		final EvaluableVariableAssignment equalOne = new EvaluableVariableAssignment();
		final EvaluableVariableAssignment equalTwo = new EvaluableVariableAssignment();
		final EvaluableVariableAssignment reversed = new EvaluableVariableAssignment();
		final int count = 6;
		for (int i = 0; i < count; i++) {
			equalOne.setValueFor(new EvaluableVariable("" + i), i);
			equalTwo.setValueFor(new EvaluableVariable("" + i), i);
			reversed.setValueFor(new EvaluableVariable("" + i), count - i - 1);
		}

		assertThat(equalOne, hasDefaultEqualsProperties());
		assertThat("the same assignment should be equal", new EvaluableVariableAssignment(),
			is(equalTo(new EvaluableVariableAssignment())));
		assertThat("the same assignment should be equal", equalOne, is(equalTo(equalTwo)));
		assertThat(equalOne.hashCode(), is(equalTwo.hashCode()));
		equalTwo.setValueFor(new EvaluableVariable("foo"), 14);
		assertThat("different assignmenst must not be equal", equalOne, is(not(equalTo(equalTwo))));
		assertThat("different assignmenst must not be equal", equalOne, is(not(equalTo(reversed))));
	}

}
