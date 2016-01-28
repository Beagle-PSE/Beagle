package de.uka.ipd.sdq.beagle.core.evaluableexpressions;

import static de.uka.ipd.sdq.beagle.core.testutil.EqualsMatcher.hasDefaultEqualsProperties;
import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static de.uka.ipd.sdq.beagle.core.testutil.ToStringMatcher.hasOverriddenToString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.theInstance;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;

import de.uka.ipd.sdq.beagle.core.testutil.factories.EvaluableExpressionFactory;

import org.junit.Test;

/**
 * Tests for {@link DivisionExpression}.
 *
 * @author Joshua Gleitze
 */
public class DivisionExpressionTest {

	/**
	 * A factory for {@linkplain EvaluableExpression EvaluableExpressions} to easily
	 * obtain new instances from.
	 */
	private static final EvaluableExpressionFactory EVALUABLE_EXPRESSION_FACTORY = new EvaluableExpressionFactory();

	/**
	 * Test method for
	 * {@link DivisionExpression#DivisionExpression(EvaluableExpression, EvaluableExpression)}
	 * . Asserts that {@code null} cannot be passed and creation is possible.
	 */
	@Test
	public void testConstructor() {
		new DivisionExpression(EVALUABLE_EXPRESSION_FACTORY.getOne(), EVALUABLE_EXPRESSION_FACTORY.getOne());

		assertThat(() -> new DivisionExpression(EVALUABLE_EXPRESSION_FACTORY.getOne(), null),
			throwsException(NullPointerException.class));
		assertThat(() -> new DivisionExpression(null, EVALUABLE_EXPRESSION_FACTORY.getOne()),
			throwsException(NullPointerException.class));
	}

	/**
	 * Test method for {@link DivisionExpression#getDivisor()}. Assert that it returns the
	 * divisor.
	 */
	@Test
	public void testGetDivisor() {
		final EvaluableExpression divisor = EVALUABLE_EXPRESSION_FACTORY.getOne();
		assertThat("should return the instance passed in the constructor!",
			new DivisionExpression(EVALUABLE_EXPRESSION_FACTORY.getOne(), divisor).getDivisor(),
			is(theInstance(divisor)));
	}

	/**
	 * Test method for {@link DivisionExpression#getDividend()}. Asserts that in returns
	 * the dividend.
	 */
	@Test
	public void testGetDividend() {
		final EvaluableExpression dividend = EVALUABLE_EXPRESSION_FACTORY.getOne();
		assertThat("should return the instance passed in the constructor!",
			new DivisionExpression(dividend, EVALUABLE_EXPRESSION_FACTORY.getOne()).getDividend(),
			is(theInstance(dividend)));
	}

	/**
	 * Test method for {@link DivisionExpression#receive(dEvaluableExpressionVisitor)}.
	 * Asserts that {@link EvaluableExpressionVisitor#visit(DivisionExpression)} is
	 * called.
	 */
	@Test
	public void testReceive() {
		final EvaluableExpressionVisitor mockVisitor = mock(EvaluableExpressionVisitor.class);
		final DivisionExpression testedExpression =
			new DivisionExpression(EVALUABLE_EXPRESSION_FACTORY.getOne(), EVALUABLE_EXPRESSION_FACTORY.getOne());

		assertThat(() -> testedExpression.receive(null), throwsException(NullPointerException.class));
		testedExpression.receive(mockVisitor);

		then(mockVisitor).should().visit(same(testedExpression));
	}

	/**
	 * Test method for {@link DivisionExpression#evaluate(EvaluableVariableAssignment)}.
	 * Asserts that:
	 *
	 * <ul>
	 *
	 * <li> The method passes the assignment to the inner expressions
	 *
	 * <li> It returns the quotients of its inner expressions.
	 *
	 * <li>{@code NaN} and infinity values are handled like specified in IEEE 754.
	 *
	 * <li>Passing {@code null} throws an exception.
	 *
	 * </ul>
	 */
	@Test
	public void testEvaluate() {
		final EvaluableExpression dividend = mock(EvaluableExpression.class);
		final EvaluableExpression divisor = mock(EvaluableExpression.class);
		final EvaluableVariableAssignment assignment = new EvaluableVariableAssignment();
		final DivisionExpression testedExpression = new DivisionExpression(dividend, divisor);

		assertThat(() -> testedExpression.evaluate(null), throwsException(NullPointerException.class));

		given(dividend.evaluate(same(assignment))).willReturn(15d);
		given(divisor.evaluate(same(assignment))).willReturn(3d);
		assertThat(testedExpression.evaluate(assignment), is(5d));

		given(dividend.evaluate(same(assignment))).willReturn(15d);
		given(divisor.evaluate(same(assignment))).willReturn(0d);
		assertThat(testedExpression.evaluate(assignment), is(Double.POSITIVE_INFINITY));

		given(dividend.evaluate(same(assignment))).willReturn(-15d);
		given(divisor.evaluate(same(assignment))).willReturn(0d);
		assertThat(testedExpression.evaluate(assignment), is(Double.NEGATIVE_INFINITY));

		given(dividend.evaluate(same(assignment))).willReturn(Double.POSITIVE_INFINITY);
		given(divisor.evaluate(same(assignment))).willReturn(Double.POSITIVE_INFINITY);
		assertThat(testedExpression.evaluate(assignment), is(Double.NaN));

		given(dividend.evaluate(same(assignment))).willReturn(0d);
		given(divisor.evaluate(same(assignment))).willReturn(0d);
		assertThat(testedExpression.evaluate(assignment), is(Double.NaN));
	}

	/**
	 * Test method for {@link DivisionExpression#toString()}. Asserts that the method was
	 * overridden.
	 */
	@Test
	public void testToString() {
		assertThat(new DivisionExpression(EVALUABLE_EXPRESSION_FACTORY.getOne(), EVALUABLE_EXPRESSION_FACTORY.getOne()),
			hasOverriddenToString());
	}

	/**
	 * Test method for {@link DivisionExpression#equals(java.lang.Object)} and
	 * {@link DivisionExpression#hashCode()}. Asserts that:
	 *
	 * <ul>
	 *
	 * <li>All and only expressions with equal divisor and dividend are equal
	 *
	 * <li>{@code hashCode} is implemented accordingly
	 *
	 * </ul>
	 */
	@Test
	public void testEqualsAndHashCode() {
		final DivisionExpression equalOne =
			new DivisionExpression(EVALUABLE_EXPRESSION_FACTORY.getAll()[0], EVALUABLE_EXPRESSION_FACTORY.getAll()[1]);
		final DivisionExpression equalTwo =
			new DivisionExpression(EVALUABLE_EXPRESSION_FACTORY.getAll()[0], EVALUABLE_EXPRESSION_FACTORY.getAll()[1]);
		final DivisionExpression differentDivident =
			new DivisionExpression(EVALUABLE_EXPRESSION_FACTORY.getAll()[2], EVALUABLE_EXPRESSION_FACTORY.getAll()[1]);
		final DivisionExpression differentDivisor =
			new DivisionExpression(EVALUABLE_EXPRESSION_FACTORY.getAll()[0], EVALUABLE_EXPRESSION_FACTORY.getAll()[2]);
		final DivisionExpression swapped =
			new DivisionExpression(EVALUABLE_EXPRESSION_FACTORY.getAll()[1], EVALUABLE_EXPRESSION_FACTORY.getAll()[0]);

		assertThat(equalOne, hasDefaultEqualsProperties());
		assertThat("expressions with equal divisor and divident should be equal", equalOne, is(equalTo(equalTwo)));
		assertThat(equalOne.hashCode(), is(equalTwo.hashCode()));
		assertThat("expressions with different dividents should not be equal", equalOne,
			is(not(equalTo(differentDivident))));
		assertThat("expressions with different divisors should not be equal", equalOne,
			is(not(equalTo(differentDivisor))));
		assertThat("expressions with different dividends and divisiors should not be equal", equalOne,
			is(not(equalTo(swapped))));
	}

}
