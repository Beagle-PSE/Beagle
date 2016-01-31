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
 * Tests for {@link ConstantExpression}.
 *
 * @author Joshua Gleitze
 */
public class ComparisonExpressionTest {

	/**
	 * A factory for {@linkplain EvaluableExpression EvaluableExpressions} to easily
	 * obtain new instances from.
	 */
	private static final EvaluableExpressionFactory EVALUABLE_EXPRESSION_FACTORY = new EvaluableExpressionFactory();

	/**
	 * Test method for
	 * {@link ComparisonExpression#ComparisonExpression(EvaluableExpression, EvaluableExpression)}
	 * . Asserts that:
	 *
	 * <ul>
	 *
	 * <li>The constructor functions properly
	 *
	 * <li>{@code null} cannot be passed to it
	 *
	 * </ul>
	 */
	@Test
	public void testConstructor() {
		new ComparisonExpression(EVALUABLE_EXPRESSION_FACTORY.getOne(), EVALUABLE_EXPRESSION_FACTORY.getOne());

		assertThat("Creating with null must not be possible",
			() -> new ComparisonExpression(null, EVALUABLE_EXPRESSION_FACTORY.getOne()),
			throwsException(NullPointerException.class));
		assertThat("Creating with null must not be possible",
			() -> new ComparisonExpression(EVALUABLE_EXPRESSION_FACTORY.getOne(), null),
			throwsException(NullPointerException.class));
	}

	/**
	 * Test method for {@link ComparisonExpression#getGreater()}. Asserts that the
	 * expression from the constructor is returned.
	 */
	@Test
	public void testGetGreater() {
		final EvaluableExpression greater = EVALUABLE_EXPRESSION_FACTORY.getOne();
		assertThat("should return the instance passed in the constructor",
			new ComparisonExpression(EVALUABLE_EXPRESSION_FACTORY.getOne(), greater).getGreater(),
			is(theInstance(greater)));
	}

	/**
	 * Test method for {@link ComparisonExpression#getSmaller()} Asserts that the
	 * expression from the constructor is returned. .
	 */
	@Test
	public void testGetSmaller() {
		final EvaluableExpression smaller = EVALUABLE_EXPRESSION_FACTORY.getOne();
		assertThat("should return the instance passed in the constructor",
			new ComparisonExpression(smaller, EVALUABLE_EXPRESSION_FACTORY.getOne()).getSmaller(),
			is(theInstance(smaller)));
	}

	/**
	 * Test method for {@link ComparisonExpression#receive(EvaluableExpressionVisitor)}.
	 * Asserts that {@link EvaluableExpressionVisitor#visit(ComparisonExpression)} is
	 * called.
	 */
	@Test
	public void testReceive() {
		final EvaluableExpressionVisitor mockVisitor = mock(EvaluableExpressionVisitor.class);
		final ComparisonExpression testExpression =
			new ComparisonExpression(EVALUABLE_EXPRESSION_FACTORY.getOne(), EVALUABLE_EXPRESSION_FACTORY.getOne());

		assertThat(() -> testExpression.receive(null), throwsException(NullPointerException.class));
		testExpression.receive(mockVisitor);

		then(mockVisitor).should().visit(same(testExpression));
	}

	/**
	 * Test method for {@link ComparisonExpression#evaluate(EvaluableVariableAssignment)}.
	 * Asserts that:
	 *
	 * <ul>
	 *
	 * <li> The method passes the assignment to the inner expressions
	 *
	 * <li> It returns 0 if {@code smaller > greater}
	 *
	 * <li> It returns 0 if {@code smaller = greater}
	 *
	 * <li> It doesn’t return 0 if {@code smaller < greater}
	 *
	 * <li>{@code NaN} and infinity values are handled like specified in IEEE 754.
	 *
	 * <li>Passing {@code null} throws an exception.
	 *
	 * </ul>
	 */
	@Test
	public void testEvaluate() {
		final EvaluableExpression smaller = mock(EvaluableExpression.class);
		final EvaluableExpression greater = mock(EvaluableExpression.class);
		final EvaluableVariableAssignment assignment = mock(EvaluableVariableAssignment.class);
		final ComparisonExpression testedExpression = new ComparisonExpression(smaller, greater);

		assertThat(() -> testedExpression.evaluate(null), throwsException(NullPointerException.class));

		given(smaller.evaluate(same(assignment))).willReturn(-1d);
		given(greater.evaluate(same(assignment))).willReturn(0d);
		assertThat(testedExpression.evaluate(assignment), is(not(0d)));

		given(smaller.evaluate(same(assignment))).willReturn(5d);
		given(greater.evaluate(same(assignment))).willReturn(5d);
		assertThat(testedExpression.evaluate(assignment), is(0d));

		given(smaller.evaluate(same(assignment))).willReturn(10d);
		given(greater.evaluate(same(assignment))).willReturn(6d);
		assertThat(testedExpression.evaluate(assignment), is(0d));

		given(smaller.evaluate(same(assignment))).willReturn(Double.NaN);
		given(greater.evaluate(same(assignment))).willReturn(6d);
		assertThat(testedExpression.evaluate(assignment), is(0d));

		given(smaller.evaluate(same(assignment))).willReturn(6d);
		given(greater.evaluate(same(assignment))).willReturn(Double.NaN);
		assertThat(testedExpression.evaluate(assignment), is(0d));

		given(smaller.evaluate(same(assignment))).willReturn(Double.NaN);
		given(greater.evaluate(same(assignment))).willReturn(Double.NaN);
		assertThat(testedExpression.evaluate(assignment), is(0d));

		given(smaller.evaluate(same(assignment))).willReturn(Double.NEGATIVE_INFINITY);
		given(greater.evaluate(same(assignment))).willReturn(6d);
		assertThat(testedExpression.evaluate(assignment), is(not(0d)));

		given(smaller.evaluate(same(assignment))).willReturn(10d);
		given(greater.evaluate(same(assignment))).willReturn(Double.POSITIVE_INFINITY);
		assertThat(testedExpression.evaluate(assignment), is(not(0d)));
	}

	/**
	 * Test method for {@link ComparisonExpression#toString()}. Asserts that the method
	 * was overridden.
	 */
	@Test
	public void testToString() {
		final ComparisonExpression testExpression =
			new ComparisonExpression(EVALUABLE_EXPRESSION_FACTORY.getOne(), EVALUABLE_EXPRESSION_FACTORY.getOne());
		assertThat(testExpression, hasOverriddenToString());
	}

	/**
	 * Test method for {@link ComparisonExpression#equals(java.lang.Object)}. Asserts
	 * that:
	 *
	 * <ul>
	 *
	 * <li>Two expressions are equal exactly if they contain equal {@code smaller} and
	 * {@code greater} expressions.
	 *
	 * <li>{@code hashCode} is implemented accordingly.
	 *
	 * </ul>
	 */
	@Test
	public void testEqualsAndHashCode() {
		final ComparisonExpression testExpression =
			new ComparisonExpression(EVALUABLE_EXPRESSION_FACTORY.getOne(), EVALUABLE_EXPRESSION_FACTORY.getOne());
		assertThat(testExpression, hasDefaultEqualsProperties());

		final ComparisonExpression equalOne = new ComparisonExpression(EVALUABLE_EXPRESSION_FACTORY.getAll()[0],
			EVALUABLE_EXPRESSION_FACTORY.getAll()[1]);
		final ComparisonExpression equalTwo = new ComparisonExpression(EVALUABLE_EXPRESSION_FACTORY.getAll()[0],
			EVALUABLE_EXPRESSION_FACTORY.getAll()[1]);
		final ComparisonExpression otherOne = new ComparisonExpression(EVALUABLE_EXPRESSION_FACTORY.getAll()[0],
			EVALUABLE_EXPRESSION_FACTORY.getAll()[2]);
		final ComparisonExpression otherTwo = new ComparisonExpression(EVALUABLE_EXPRESSION_FACTORY.getAll()[2],
			EVALUABLE_EXPRESSION_FACTORY.getAll()[1]);
		final ComparisonExpression otherThree = new ComparisonExpression(EVALUABLE_EXPRESSION_FACTORY.getAll()[1],
			EVALUABLE_EXPRESSION_FACTORY.getAll()[0]);

		assertThat("Two expressions with equal inner expressions should be equal", equalOne, is(equalTo(equalTwo)));
		assertThat("Equal expressions must have the same hashCode", equalOne.hashCode(), is(equalTwo.hashCode()));
		assertThat("Two expressions with different inner expressions must not be equal", equalOne,
			is(not(equalTo(otherOne))));
		assertThat("Two expressions with different inner expressions must not be equal", equalOne,
			is(not(equalTo(otherTwo))));
		assertThat("Two expressions with different inner expressions must not be equal", equalOne,
			is(not(equalTo(otherThree))));
	}

}
