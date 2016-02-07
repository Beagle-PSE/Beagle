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
 * Tests for {@link SubtractionExpression}.
 *
 * @author Joshua Gleitze
 */
public class SubtractionExpressionTest {

	/**
	 * A factory for {@linkplain EvaluableExpression EvaluableExpressions} to easily
	 * obtain new instances from.
	 */
	private static final EvaluableExpressionFactory EVALUABLE_EXPRESSION_FACTORY = new EvaluableExpressionFactory();

	/**
	 * Test method for
	 * {@link SubtractionExpression#SubtractionExpression(EvaluableExpression,EvaluableExpression)}
	 * . Asserts that construction is possible and {@code null} cannot be passed.
	 */
	@Test
	public void constructor() {
		new SubtractionExpression(EVALUABLE_EXPRESSION_FACTORY.getOne(), EVALUABLE_EXPRESSION_FACTORY.getOne());

		assertThat(() -> new SubtractionExpression(EVALUABLE_EXPRESSION_FACTORY.getOne(), null),
			throwsException(NullPointerException.class));
		assertThat(() -> new SubtractionExpression(null, EVALUABLE_EXPRESSION_FACTORY.getOne()),
			throwsException(NullPointerException.class));
	}

	/**
	 * Test method for {@link SubtractionExpression#getSubtrahend()}. Asserts that the
	 * instance passed in the constructor is returned.
	 */
	@Test
	public void getSubtrahend() {
		final EvaluableExpression subtrahend = EVALUABLE_EXPRESSION_FACTORY.getOne();
		assertThat(new SubtractionExpression(EVALUABLE_EXPRESSION_FACTORY.getOne(), subtrahend).getSubtrahend(),
			is(theInstance(subtrahend)));
	}

	/**
	 * Test method for {@link SubtractionExpression#getMinuend()}. Asserts that the
	 * instance passed in the constructor is returned.
	 */
	@Test
	public void getMinuend() {
		final EvaluableExpression minuend = EVALUABLE_EXPRESSION_FACTORY.getOne();
		assertThat(new SubtractionExpression(minuend, EVALUABLE_EXPRESSION_FACTORY.getOne()).getMinuend(),
			is(theInstance(minuend)));
	}

	/**
	 * Test method for {@link SubtractionExpression#receive(EvaluableExpressionVisitor)}.
	 * Asserts that {@link EvaluableExpressionVisitor#visit(SubtractionExpression)} is
	 * called.
	 */
	@Test
	public void receive() {
		final EvaluableExpressionVisitor mockVisitor = mock(EvaluableExpressionVisitor.class);
		final SubtractionExpression testedExpression =
			new SubtractionExpression(EVALUABLE_EXPRESSION_FACTORY.getOne(), EVALUABLE_EXPRESSION_FACTORY.getOne());

		assertThat(() -> testedExpression.receive(null), throwsException(NullPointerException.class));
		testedExpression.receive(mockVisitor);

		then(mockVisitor).should().visit(same(testedExpression));
	}

	/**
	 * Test method for {@link SubtractionExpression#evaluate(EvaluableVariableAssignment)}
	 * . Asserts that:
	 *
	 * <ul>
	 *
	 * <li>The method passes the assignment to the inner expressions
	 *
	 * <li>It returns the difference between the minuend and subtrahend.
	 *
	 * <li>{@code NaN} and infinity values are handled like specified in IEEE 754.
	 *
	 * <li>Passing {@code null} throws an exception.
	 *
	 * </ul>
	 */
	@Test
	public void evaluate() {
		final EvaluableExpression minuend = mock(EvaluableExpression.class);
		final EvaluableExpression subtrahend = mock(EvaluableExpression.class);
		final EvaluableVariableAssignment assignment = new EvaluableVariableAssignment();
		final SubtractionExpression testedExpression = new SubtractionExpression(minuend, subtrahend);

		assertThat(() -> testedExpression.evaluate(null), throwsException(NullPointerException.class));

		given(minuend.evaluate(same(assignment))).willReturn(50d);
		given(subtrahend.evaluate(same(assignment))).willReturn(30d);
		assertThat(testedExpression.evaluate(assignment), is(20d));

		given(minuend.evaluate(same(assignment))).willReturn(2d);
		given(subtrahend.evaluate(same(assignment))).willReturn(-4d);
		assertThat(testedExpression.evaluate(assignment), is(6d));

		given(minuend.evaluate(same(assignment))).willReturn(0d);
		given(subtrahend.evaluate(same(assignment))).willReturn(Double.POSITIVE_INFINITY);
		assertThat(testedExpression.evaluate(assignment), is(Double.NEGATIVE_INFINITY));

		given(minuend.evaluate(same(assignment))).willReturn(Double.POSITIVE_INFINITY);
		given(subtrahend.evaluate(same(assignment))).willReturn(100000d);
		assertThat(testedExpression.evaluate(assignment), is(Double.POSITIVE_INFINITY));

		given(minuend.evaluate(same(assignment))).willReturn(Double.POSITIVE_INFINITY);
		given(subtrahend.evaluate(same(assignment))).willReturn(Double.POSITIVE_INFINITY);
		assertThat(testedExpression.evaluate(assignment), is(notANumber()));

		given(minuend.evaluate(same(assignment))).willReturn(Double.NaN);
		given(subtrahend.evaluate(same(assignment))).willReturn(1d);
		assertThat(testedExpression.evaluate(assignment), is(notANumber()));

		given(minuend.evaluate(same(assignment))).willReturn(5d);
		given(subtrahend.evaluate(same(assignment))).willReturn(Double.NaN);
		assertThat(testedExpression.evaluate(assignment), is(notANumber()));

		given(minuend.evaluate(same(assignment))).willReturn(Double.NaN);
		given(subtrahend.evaluate(same(assignment))).willReturn(Double.NaN);
		assertThat(testedExpression.evaluate(assignment), is(notANumber()));

		given(minuend.evaluate(same(assignment))).willReturn(Double.NaN);
		given(subtrahend.evaluate(same(assignment))).willReturn(Double.NaN);
		assertThat(testedExpression.evaluate(assignment), is(notANumber()));
	}

	/**
	 * Test method for {@link SubtractionExpression#toString()}. Asserts that the method
	 * was overridden.
	 */
	@Test
	public void toStringT() {
		assertThat(
			new SubtractionExpression(EVALUABLE_EXPRESSION_FACTORY.getOne(), EVALUABLE_EXPRESSION_FACTORY.getOne()),
			hasOverriddenToString());
	}

	/**
	 * Test method for {@link SubtractionExpression#equals(java.lang.Object)}.
	 *
	 * <ul>
	 *
	 * <li>All and only expressions with equal minuend and subtrachend are equal
	 *
	 * <li>{@code hashCode} is implemented accordingly
	 *
	 * </ul>
	 */
	@Test
	public void equalsObject() {
		final SubtractionExpression equalOne = new SubtractionExpression(EVALUABLE_EXPRESSION_FACTORY.getAll()[0],
			EVALUABLE_EXPRESSION_FACTORY.getAll()[1]);
		final SubtractionExpression equalTwo = new SubtractionExpression(EVALUABLE_EXPRESSION_FACTORY.getAll()[0],
			EVALUABLE_EXPRESSION_FACTORY.getAll()[1]);
		final SubtractionExpression differentBase = new SubtractionExpression(EVALUABLE_EXPRESSION_FACTORY.getAll()[2],
			EVALUABLE_EXPRESSION_FACTORY.getAll()[1]);
		final SubtractionExpression differentAntilogarithm = new SubtractionExpression(
			EVALUABLE_EXPRESSION_FACTORY.getAll()[0], EVALUABLE_EXPRESSION_FACTORY.getAll()[2]);
		final SubtractionExpression swapped = new SubtractionExpression(EVALUABLE_EXPRESSION_FACTORY.getAll()[1],
			EVALUABLE_EXPRESSION_FACTORY.getAll()[0]);

		assertThat(equalOne, hasDefaultEqualsProperties());
		assertThat("expressions with equal minuend and subtrahend should be equal", equalOne, is(equalTo(equalTwo)));
		assertThat(equalOne.hashCode(), is(equalTwo.hashCode()));
		assertThat("expressions with different minuends must not be equal", equalOne, is(not(equalTo(differentBase))));
		assertThat("expressions with different subtrahends must not be equal", equalOne,
			is(not(equalTo(differentAntilogarithm))));
		assertThat("expressions with different minuends and subtrahends must not be equal", equalOne,
			is(not(equalTo(swapped))));
	}

}
