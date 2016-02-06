package de.uka.ipd.sdq.beagle.core.evaluableexpressions;

import static de.uka.ipd.sdq.beagle.core.testutil.EqualsMatcher.hasDefaultEqualsProperties;
import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static de.uka.ipd.sdq.beagle.core.testutil.ToStringMatcher.hasOverriddenToString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.theInstance;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;

import org.junit.Test;

/**
 * Tests for {@link EvaluableVariable}.
 *
 * @author Joshua Gleitze
 */
public class EvaluableVariableTest {

	/**
	 * Test method for {@link EvaluableVariable#EvaluableVariable(java.lang.String)}.
	 * Asserts that creation is possible and {@code null} or an empty strung cannot be
	 * passed.
	 */
	@Test
	public void constructor() {
		new EvaluableVariable("foo");

		assertThat(() -> new EvaluableVariable(null), throwsException(NullPointerException.class));
		assertThat(() -> new EvaluableVariable(""), throwsException(IllegalArgumentException.class));
	}

	/**
	 * Test method for {@link EvaluableVariable#getName()}. Asserts that the name passed
	 * in the constructor is returned.
	 */
	@Test
	public void getName() {
		assertThat("The name from the constructor should be returned!", new EvaluableVariable("foo").getName(),
			is("foo"));
	}

	/**
	 * Test method for {@link EvaluableVariable#receive(EvaluableExpressionVisitor)}.
	 * Asserts that {@link EvaluableExpressionVisitor#visit(EvaluableVariable)} is called.
	 */
	@Test
	public void receive() {
		final EvaluableExpressionVisitor mockVisitor = mock(EvaluableExpressionVisitor.class);
		final EvaluableVariable testedExpression = new EvaluableVariable("foo");

		assertThat(() -> testedExpression.receive(null), throwsException(NullPointerException.class));
		testedExpression.receive(mockVisitor);

		then(mockVisitor).should().visit(same(testedExpression));
	}

	/**
	 * Test method for {@link EvaluableVariable#evaluate(EvaluableVariableAssignment)}.
	 * Asserts that:
	 *
	 * <ul>
	 *
	 * <li>The value obtained from the assignment is returned.
	 *
	 * <li>An {@link UndefinedExpressionException} is thrown if the value cannot be
	 * obtained.
	 *
	 * <li>A {@link NullPointerException} is thrown if {@code null} is passed.
	 *
	 * </ul>
	 */
	@Test
	public void evaluate() {
		final EvaluableVariableAssignment assignment = new EvaluableVariableAssignment();
		final EvaluableVariable testedVariable = new EvaluableVariable("x");

		assertThat(() -> testedVariable.evaluate(null), throwsException(NullPointerException.class));
		assertThat(() -> testedVariable.evaluate(assignment), throwsException(UndefinedExpressionException.class));
		try {
			testedVariable.evaluate(assignment);
			fail("Please be consistent at least!");
		} catch (final UndefinedExpressionException undefinedInfo) {
			assertThat("should properly report the variable for which evaluation failed",
				undefinedInfo.getMissingVariable(), is(theInstance(testedVariable)));
			assertThat("should properly report the assignment missing the variable",
				undefinedInfo.getCausingAssignment(), is(theInstance(assignment)));
		}

		assignment.setValueFor(testedVariable, 9);
		assertThat("should return the value obtained from the assignement", testedVariable.evaluate(assignment),
			is(9d));
	}

	/**
	 * Test method for {@link EvaluableVariable#toString()}. Asserts that the method was
	 * overridden.
	 */
	@Test
	public void toStringT() {
		assertThat(new EvaluableVariable("foo"), hasOverriddenToString());
	}

	/**
	 * Test method for {@link EvaluableVariable#equals(java.lang.Object)}. Asserts that
	 * variables with the same name are equal, others not. Tests that {@code hashCode} is
	 * implemented accordingly.
	 */
	@Test
	public void equalsAndHashCode() {
		final EvaluableVariable equalOne = new EvaluableVariable("equal");
		final EvaluableVariable equalTwo = new EvaluableVariable(new String("equal"));
		final EvaluableVariable different = new EvaluableVariable("different");

		assertThat(equalOne, hasDefaultEqualsProperties());
		assertThat("variables with an equal name should be equal", equalOne, is(equalTo(equalTwo)));
		assertThat("hashCode should follow the contract", equalOne.hashCode(), is(equalTwo.hashCode()));
		assertThat("variables with different names must not be equal", equalOne, is(not(equalTo(different))));
	}

}
