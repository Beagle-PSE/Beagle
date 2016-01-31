package de.uka.ipd.sdq.beagle.core.evaluableexpressions;

import static de.uka.ipd.sdq.beagle.core.testutil.EqualsMatcher.hasDefaultEqualsProperties;
import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static de.uka.ipd.sdq.beagle.core.testutil.ToStringMatcher.hasOverriddenToString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.theInstance;
import static org.hamcrest.number.IsNaN.notANumber;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;

import de.uka.ipd.sdq.beagle.core.testutil.factories.EvaluableExpressionFactory;

import org.junit.Test;

/**
 * Tests for {@link ExponentationExpression}.
 *
 * @author Joshua Gleitze
 */
public class ExponentationExpressionTest {

	/**
	 * A factory for {@linkplain EvaluableExpression EvaluableExpressions} to easily
	 * obtain new instances from.
	 */
	private static final EvaluableExpressionFactory EVALUABLE_EXPRESSION_FACTORY = new EvaluableExpressionFactory();

	/**
	 * Test method for
	 * {@link ExponentationExpression#ExponentationExpression(EvaluableExpression, EvaluableExpression)}
	 * . Asserts that construction is possible and {@code null} cannot be passed.
	 */
	@Test
	public void testConstructor() {
		assertThat(() -> new ExponentationExpression(null, EVALUABLE_EXPRESSION_FACTORY.getOne()),
			throwsException(NullPointerException.class));
		assertThat(() -> new ExponentationExpression(EVALUABLE_EXPRESSION_FACTORY.getOne(), null),
			throwsException(NullPointerException.class));
		new ExponentationExpression(EVALUABLE_EXPRESSION_FACTORY.getOne(), EVALUABLE_EXPRESSION_FACTORY.getOne());
	}

	/**
	 * Test method for {@link ExponentationExpression#getExponent()}. Asserts that the
	 * exponent passed in the constructor is returned.
	 */
	@Test
	public void testGetExponent() {
		final EvaluableExpression exponent = EVALUABLE_EXPRESSION_FACTORY.getOne();
		assertThat(new ExponentationExpression(EVALUABLE_EXPRESSION_FACTORY.getOne(), exponent).getExponent(),
			is(theInstance(exponent)));
	}

	/**
	 * Test method for {@link ExponentationExpression#getBase()}. Asserts that the base
	 * passed in the constructor is returned.
	 */
	@Test
	public void testGetBase() {
		final EvaluableExpression base = EVALUABLE_EXPRESSION_FACTORY.getOne();
		assertThat(new ExponentationExpression(base, EVALUABLE_EXPRESSION_FACTORY.getOne()).getBase(),
			is(theInstance(base)));
	}

	/**
	 * Test method for {@link ExponentationExpression#receive(EvaluableExpressionVisitor)}
	 * . Asserts that {@link EvaluableExpressionVisitor#visit(ExponentationExpression)} is
	 * called.
	 */
	@Test
	public void testReceive() {
		final EvaluableExpressionVisitor mockVisitor = mock(EvaluableExpressionVisitor.class);
		final ExponentationExpression testExpression =
			new ExponentationExpression(EVALUABLE_EXPRESSION_FACTORY.getOne(), EVALUABLE_EXPRESSION_FACTORY.getOne());

		assertThat(() -> testExpression.receive(null), throwsException(NullPointerException.class));
		testExpression.receive(mockVisitor);

		then(mockVisitor).should().visit(same(testExpression));
	}

	/**
	 * Test method for
	 * {@link ExponentationExpression#evaluate(EvaluableVariableAssignment)}. Asserts
	 * that:
	 *
	 * <ul>
	 *
	 * <li>The method passes the assignment to the inner expressions
	 *
	 * <li>It returns the {@code base} to the power of the {@code exponent}.
	 *
	 * <li>{@code NaN} and infinity values are handled like specified in IEEE 754.
	 *
	 * <li>Passing {@code null} throws an exception.
	 *
	 * </ul>
	 */
	@Test
	public void testEvaluate() {
		final EvaluableExpression base = mock(EvaluableExpression.class);
		final EvaluableExpression exponent = mock(EvaluableExpression.class);
		final EvaluableVariableAssignment assignment = new EvaluableVariableAssignment();
		final ExponentationExpression testedExpression = new ExponentationExpression(base, exponent);

		assertThat(() -> testedExpression.evaluate(null), throwsException(NullPointerException.class));

		given(base.evaluate(same(assignment))).willReturn(2d);
		given(exponent.evaluate(same(assignment))).willReturn(8d);
		assertThat(testedExpression.evaluate(assignment), is(256d));

		given(base.evaluate(same(assignment))).willReturn(-1d);
		given(exponent.evaluate(same(assignment))).willReturn(1d / 2);
		assertThat(testedExpression.evaluate(assignment), is(notANumber()));

		given(base.evaluate(same(assignment))).willReturn(-15d);
		given(exponent.evaluate(same(assignment))).willReturn(0d);
		assertThat(testedExpression.evaluate(assignment), is(1d));

		given(base.evaluate(same(assignment))).willReturn(0d);
		given(exponent.evaluate(same(assignment))).willReturn(Double.POSITIVE_INFINITY);
		assertThat(testedExpression.evaluate(assignment), is(0d));

		given(base.evaluate(same(assignment))).willReturn(Double.POSITIVE_INFINITY);
		given(exponent.evaluate(same(assignment))).willReturn(0d);
		assertThat(testedExpression.evaluate(assignment), is(1d));

		given(base.evaluate(same(assignment))).willReturn(Double.NaN);
		given(exponent.evaluate(same(assignment))).willReturn(2d);
		assertThat(testedExpression.evaluate(assignment), is(notANumber()));

		given(base.evaluate(same(assignment))).willReturn(2d);
		given(exponent.evaluate(same(assignment))).willReturn(Double.NaN);
		assertThat(testedExpression.evaluate(assignment), is(notANumber()));

		given(base.evaluate(same(assignment))).willReturn(Double.NaN);
		given(exponent.evaluate(same(assignment))).willReturn(Double.NaN);
		assertThat(testedExpression.evaluate(assignment), is(notANumber()));
	}

	/**
	 * Test method for {@link ExponentationExpression#toString()}. Asserts that the method
	 * was overridden.
	 */
	@Test
	public void testToString() {
		assertThat(
			new ExponentationExpression(EVALUABLE_EXPRESSION_FACTORY.getOne(), EVALUABLE_EXPRESSION_FACTORY.getOne()),
			hasOverriddenToString());
	}

	/**
	 * Test method for {@link ExponentationExpression#equals(java.lang.Object)} and
	 * {@link ExponentationExpression#hashCode()}. Asserts that:
	 *
	 * <ul>
	 *
	 * <li>Two expressions are equal exactly if they contain equal {@code base} and
	 * {@code exponent} expressions.
	 *
	 * <li>{@code hashCode} is implemented accordingly.
	 *
	 * </ul>
	 */
	@Test
	public void testEqualsObject() {
		final ExponentationExpression equalOne = new ExponentationExpression(EVALUABLE_EXPRESSION_FACTORY.getAll()[0],
			EVALUABLE_EXPRESSION_FACTORY.getAll()[1]);
		final ExponentationExpression equalTwo = new ExponentationExpression(EVALUABLE_EXPRESSION_FACTORY.getAll()[0],
			EVALUABLE_EXPRESSION_FACTORY.getAll()[1]);
		final ExponentationExpression differentBase = new ExponentationExpression(
			EVALUABLE_EXPRESSION_FACTORY.getAll()[2], EVALUABLE_EXPRESSION_FACTORY.getAll()[1]);
		final ExponentationExpression differentExponent = new ExponentationExpression(
			EVALUABLE_EXPRESSION_FACTORY.getAll()[0], EVALUABLE_EXPRESSION_FACTORY.getAll()[2]);
		final ExponentationExpression swapped = new ExponentationExpression(EVALUABLE_EXPRESSION_FACTORY.getAll()[1],
			EVALUABLE_EXPRESSION_FACTORY.getAll()[0]);

		assertThat(equalOne, hasDefaultEqualsProperties());
		assertThat("expressions with equal base and exponent should be equal", equalOne, is(equalTo(equalTwo)));
		assertThat(equalOne.hashCode(), is(equalTwo.hashCode()));
		assertThat("expressions with different bases should not be equal", equalOne, is(not(equalTo(differentBase))));
		assertThat("expressions with different exponents should not be equal", equalOne,
			is(not(equalTo(differentExponent))));
		assertThat("expressions with different bases and exponents should not be equal", equalOne,
			is(not(equalTo(swapped))));
	}

}
