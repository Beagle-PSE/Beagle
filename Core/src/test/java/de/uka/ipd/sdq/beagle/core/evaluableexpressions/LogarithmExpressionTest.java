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
 * Tests for {@link LogarithmExpression}.
 *
 * @author Joshua Gleitze
 */
public class LogarithmExpressionTest {

	/**
	 * A factory for {@linkplain EvaluableExpression EvaluableExpressions} to easily
	 * obtain new instances from.
	 */
	private static final EvaluableExpressionFactory EVALUABLE_EXPRESSION_FACTORY = new EvaluableExpressionFactory();

	/**
	 * Test method for
	 * {@link LogarithmExpression#LogarithmExpression(EvaluableExpression, EvaluableExpression)}
	 * . Asserts that construction is possible and {@code null} cannot be passed.
	 */
	@Test
	public void constructor() {
		new LogarithmExpression(EVALUABLE_EXPRESSION_FACTORY.getOne(), EVALUABLE_EXPRESSION_FACTORY.getOne());

		assertThat(() -> new LogarithmExpression(EVALUABLE_EXPRESSION_FACTORY.getOne(), null),
			throwsException(NullPointerException.class));
		assertThat(() -> new LogarithmExpression(null, EVALUABLE_EXPRESSION_FACTORY.getOne()),
			throwsException(NullPointerException.class));
	}

	/**
	 * Test method for {@link LogarithmExpression#getBase()}. Asserts that the base passed
	 * in the constructor is returned.
	 */
	@Test
	public void getBase() {
		final EvaluableExpression base = EVALUABLE_EXPRESSION_FACTORY.getOne();
		assertThat(new LogarithmExpression(base, EVALUABLE_EXPRESSION_FACTORY.getOne()).getBase(),
			is(theInstance(base)));
	}

	/**
	 * Test method for {@link LogarithmExpression#getAntilogarithm()}. Asserts that the
	 * antilogarithm passed in the constructor is returned. .
	 */
	@Test
	public void getAntilogarithm() {
		final EvaluableExpression antilogarithm = EVALUABLE_EXPRESSION_FACTORY.getOne();
		assertThat(new LogarithmExpression(EVALUABLE_EXPRESSION_FACTORY.getOne(), antilogarithm).getAntilogarithm(),
			is(theInstance(antilogarithm)));
	}

	/**
	 * Test method for {@link LogarithmExpression#receive(EvaluableExpressionVisitor)}.
	 * Asserts that {@link EvaluableExpressionVisitor#visit(LogarithmExpression)} is
	 * called.
	 */
	@Test
	public void receive() {
		final EvaluableExpressionVisitor mockVisitor = mock(EvaluableExpressionVisitor.class);
		final LogarithmExpression testedExpression =
			new LogarithmExpression(EVALUABLE_EXPRESSION_FACTORY.getOne(), EVALUABLE_EXPRESSION_FACTORY.getOne());

		assertThat(() -> testedExpression.receive(null), throwsException(NullPointerException.class));
		testedExpression.receive(mockVisitor);

		then(mockVisitor).should().visit(same(testedExpression));
	}

	/**
	 * Test method for {@link LogarithmExpression#evaluate(EvaluableVariableAssignment)}.
	 * Asserts that:
	 *
	 * <ul>
	 *
	 * <li>The method passes the assignment to the inner expressions
	 *
	 * <li>It returns the logarithm of {@code antilogarithm} to the base {@code base}.
	 *
	 * <li>{@code NaN} and infinity values are handled like specified in IEEE 754.
	 *
	 * <li>Passing {@code null} throws an exception.
	 *
	 * </ul>
	 */
	@Test
	public void evaluate() {
		final EvaluableExpression base = mock(EvaluableExpression.class);
		final EvaluableExpression antilogarithm = mock(EvaluableExpression.class);
		final EvaluableVariableAssignment assignment = new EvaluableVariableAssignment();
		final LogarithmExpression testedExpression = new LogarithmExpression(base, antilogarithm);

		assertThat(() -> testedExpression.evaluate(null), throwsException(NullPointerException.class));

		given(base.evaluate(same(assignment))).willReturn(2d);
		given(antilogarithm.evaluate(same(assignment))).willReturn(256d);
		assertThat(testedExpression.evaluate(assignment), is(8d));

		given(base.evaluate(same(assignment))).willReturn(2d);
		given(antilogarithm.evaluate(same(assignment))).willReturn(-4d);
		assertThat(testedExpression.evaluate(assignment), is(notANumber()));

		given(base.evaluate(same(assignment))).willReturn(0d);
		given(antilogarithm.evaluate(same(assignment))).willReturn(Double.POSITIVE_INFINITY);
		assertThat(testedExpression.evaluate(assignment), is(notANumber()));

		given(base.evaluate(same(assignment))).willReturn(3d);
		given(antilogarithm.evaluate(same(assignment))).willReturn(Double.POSITIVE_INFINITY);
		assertThat(testedExpression.evaluate(assignment), is(Double.POSITIVE_INFINITY));

		given(base.evaluate(same(assignment))).willReturn(Double.POSITIVE_INFINITY);
		given(antilogarithm.evaluate(same(assignment))).willReturn(1d);
		assertThat(testedExpression.evaluate(assignment), is(0d));

		given(base.evaluate(same(assignment))).willReturn(Double.NaN);
		given(antilogarithm.evaluate(same(assignment))).willReturn(2d);
		assertThat(testedExpression.evaluate(assignment), is(notANumber()));

		given(base.evaluate(same(assignment))).willReturn(2d);
		given(antilogarithm.evaluate(same(assignment))).willReturn(Double.NaN);
		assertThat(testedExpression.evaluate(assignment), is(notANumber()));

		given(base.evaluate(same(assignment))).willReturn(Double.NaN);
		given(antilogarithm.evaluate(same(assignment))).willReturn(Double.NaN);
		assertThat(testedExpression.evaluate(assignment), is(notANumber()));
	}

	/**
	 * Test method for {@link LogarithmExpression#toString()}. Asserts that the method was
	 * overridden.
	 */
	@Test
	public void toStringT() {
		assertThat(
			new LogarithmExpression(EVALUABLE_EXPRESSION_FACTORY.getOne(), EVALUABLE_EXPRESSION_FACTORY.getOne()),
			hasOverriddenToString());
	}

	/**
	 * Test method for {@link LogarithmExpression#equals(java.lang.Object)} and
	 * {@link LogarithmExpression#hashCode()}. Asserts that:
	 *
	 * <ul>
	 *
	 * <li>All and only expressions with equal base and antilogarithm are equal
	 *
	 * <li>{@code hashCode} is implemented accordingly
	 *
	 * </ul>
	 */
	@Test
	public void equalsAndHashCode() {
		final LogarithmExpression equalOne =
			new LogarithmExpression(EVALUABLE_EXPRESSION_FACTORY.getAll()[0], EVALUABLE_EXPRESSION_FACTORY.getAll()[1]);
		final LogarithmExpression equalTwo =
			new LogarithmExpression(EVALUABLE_EXPRESSION_FACTORY.getAll()[0], EVALUABLE_EXPRESSION_FACTORY.getAll()[1]);
		final LogarithmExpression differentBase =
			new LogarithmExpression(EVALUABLE_EXPRESSION_FACTORY.getAll()[2], EVALUABLE_EXPRESSION_FACTORY.getAll()[1]);
		final LogarithmExpression differentAntilogarithm =
			new LogarithmExpression(EVALUABLE_EXPRESSION_FACTORY.getAll()[0], EVALUABLE_EXPRESSION_FACTORY.getAll()[2]);
		final LogarithmExpression swapped =
			new LogarithmExpression(EVALUABLE_EXPRESSION_FACTORY.getAll()[1], EVALUABLE_EXPRESSION_FACTORY.getAll()[0]);

		assertThat(equalOne, hasDefaultEqualsProperties());
		assertThat("expressions with equal base and antilogarithm should be equal", equalOne, is(equalTo(equalTwo)));
		assertThat(equalOne.hashCode(), is(equalTwo.hashCode()));
		assertThat("expressions with different base must not be equal", equalOne, is(not(equalTo(differentBase))));
		assertThat("expressions with different antilogarithm must not be equal", equalOne,
			is(not(equalTo(differentAntilogarithm))));
		assertThat("expressions with different base and antilogarithm must not be equal", equalOne,
			is(not(equalTo(swapped))));
	}

}
