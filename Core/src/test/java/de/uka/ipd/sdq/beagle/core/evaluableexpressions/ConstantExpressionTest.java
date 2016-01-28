package de.uka.ipd.sdq.beagle.core.evaluableexpressions;

import static de.uka.ipd.sdq.beagle.core.testutil.EqualsMatcher.hasDefaultEqualsProperties;
import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static de.uka.ipd.sdq.beagle.core.testutil.ToStringMatcher.hasOverriddenToString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.theInstance;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;

import org.junit.Test;

/**
 * Tests for {@link ConstantExpression}.
 *
 * @author Joshua Gleitze
 */
public class ConstantExpressionTest {

	/**
	 * Test method for {@link ConstantExpression#getValue()}. Asserts that the method
	 * returns the passed value.
	 */
	@Test
	public void testGetValue() {
		assertThat(ConstantExpression.forValue(0).getValue(), is(0d));
	}

	/**
	 * Test method for {@link ConstantExpression#forValue(double)}. Asserts that the
	 * method realises the flyweight pattern.
	 */
	@Test
	public void testForValue() {
		assertThat("The flyweight must always return the same instance for the same vaule",
			ConstantExpression.forValue(4), is(theInstance(ConstantExpression.forValue(4))));
		assertThat("The flyweight must return different instances for different vaules",
			ConstantExpression.forValue(-4), is(not(theInstance(ConstantExpression.forValue(4)))));
	}

	/**
	 * Test method for {@link ConstantExpression#receive(EvaluableExpressionVisitor)}.
	 * Asserts that {@link EvaluableExpressionVisitor#visit(ConstantExpression)} is
	 * called.
	 */
	@Test
	public void testReceive() {
		final EvaluableExpressionVisitor mockVisitor = mock(EvaluableExpressionVisitor.class);
		final ConstantExpression testExpression = ConstantExpression.forValue(3);

		assertThat(() -> testExpression.receive(null), throwsException(NullPointerException.class));
		testExpression.receive(mockVisitor);

		then(mockVisitor).should().visit(same(testExpression));
	}

	/**
	 * Test method for {@link ConstantExpression#evaluate(EvaluableVariableAssignment)}.
	 * Asserts that the constant’s value is returned.
	 */
	@Test
	public void testEvaluate() {
		assertThat(() -> ConstantExpression.forValue(-100).evaluate(null), throwsException(NullPointerException.class));
		assertThat(ConstantExpression.forValue(5).evaluate(new EvaluableVariableAssignment()), is(5d));
	}

	/**
	 * Test method for {@link ConstantExpression#toString()}. Asserts that the method was
	 * overridden.
	 */
	@Test
	public void testToString() {
		assertThat(ConstantExpression.forValue(-13), hasOverriddenToString());
	}

	/**
	 * Test method for {@link ConstantExpression#equals(java.lang.Object)} and
	 * {@link ComparisonExpression#hashCode()}.
	 */
	@Test
	public void testEqualsAndHashCode() {
		assertThat(ConstantExpression.forValue(5), hasDefaultEqualsProperties());
		assertThat(ConstantExpression.forValue(7), is(equalTo(ConstantExpression.forValue(7))));
		assertThat(ConstantExpression.forValue(9).hashCode(), is(ConstantExpression.forValue(9).hashCode()));
		assertThat(ConstantExpression.forValue(13), is(not(equalTo(ConstantExpression.forValue(14)))));
	}

}
