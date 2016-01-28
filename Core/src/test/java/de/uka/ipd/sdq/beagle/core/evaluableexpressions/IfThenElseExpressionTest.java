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
 * Tests for {@link IfThenElseExpression}.
 *
 * @author Joshua Gleitze
 */
public class IfThenElseExpressionTest {

	/**
	 * A factory for {@linkplain EvaluableExpression EvaluableExpressions} to easily
	 * obtain new instances from.
	 */
	private static final EvaluableExpressionFactory EVALUABLE_EXPRESSION_FACTORY = new EvaluableExpressionFactory();

	/**
	 * Test method for
	 * {@link IfThenElseExpression#IfThenElseExpression(EvaluableExpression, EvaluableExpression, EvaluableExpression)}
	 * . Asserts that construction is possible and {@code null} cannot be passed.
	 */
	@Test
	public void testConstructor() {
		new IfThenElseExpression(EVALUABLE_EXPRESSION_FACTORY.getOne(), EVALUABLE_EXPRESSION_FACTORY.getOne(),
			EVALUABLE_EXPRESSION_FACTORY.getOne());
		assertThat(() -> new IfThenElseExpression(null, EVALUABLE_EXPRESSION_FACTORY.getOne(),
			EVALUABLE_EXPRESSION_FACTORY.getOne()), throwsException(NullPointerException.class));
		assertThat(() -> new IfThenElseExpression(EVALUABLE_EXPRESSION_FACTORY.getOne(), null,
			EVALUABLE_EXPRESSION_FACTORY.getOne()), throwsException(NullPointerException.class));
		assertThat(() -> new IfThenElseExpression(EVALUABLE_EXPRESSION_FACTORY.getOne(),
			EVALUABLE_EXPRESSION_FACTORY.getOne(), null), throwsException(NullPointerException.class));
	}

	/**
	 * Test method for {@link IfThenElseExpression#getIfStatement()}. Asserts that the if
	 * expression passed in the constructor is returned.
	 */
	@Test
	public void testGetIfStatement() {
		final EvaluableExpression ifExpression = EVALUABLE_EXPRESSION_FACTORY.getOne();
		assertThat(
			"should return the instance passed in the constructor", new IfThenElseExpression(ifExpression,
				EVALUABLE_EXPRESSION_FACTORY.getOne(), EVALUABLE_EXPRESSION_FACTORY.getOne()).getIfStatement(),
			is(theInstance(ifExpression)));
	}

	/**
	 * Test method for {@link IfThenElseExpression#getElseStatement()}. Asserts that the
	 * else expression passed in the constructor is returned.
	 */
	@Test
	public void testGetElseStatement() {
		final EvaluableExpression elseExpression = EVALUABLE_EXPRESSION_FACTORY.getOne();
		assertThat("should return the instance passed in the constructor",
			new IfThenElseExpression(EVALUABLE_EXPRESSION_FACTORY.getOne(), EVALUABLE_EXPRESSION_FACTORY.getOne(),
				elseExpression).getElseStatement(),
			is(theInstance(elseExpression)));
	}

	/**
	 * Test method for {@link IfThenElseExpression#getThenStatement()}. Asserts that the
	 * then expression passed in the constructor is returned.
	 */
	@Test
	public void testGetThenStatement() {
		final EvaluableExpression thenExpression = EVALUABLE_EXPRESSION_FACTORY.getOne();
		assertThat("should return the instance passed in the constructor",
			new IfThenElseExpression(EVALUABLE_EXPRESSION_FACTORY.getOne(), thenExpression,
				EVALUABLE_EXPRESSION_FACTORY.getOne()).getThenStatement(),
			is(theInstance(thenExpression)));
	}

	/**
	 * Test method for {@link IfThenElseExpression#receive(EvaluableExpressionVisitor)}.
	 * Asserts that {@link EvaluableExpressionVisitor#visit(IfThenElseExpression)} is
	 * called.
	 */
	@Test
	public void testReceive() {
		final EvaluableExpressionVisitor mockVisitor = mock(EvaluableExpressionVisitor.class);
		final IfThenElseExpression testedExpression = new IfThenElseExpression(EVALUABLE_EXPRESSION_FACTORY.getOne(),
			EVALUABLE_EXPRESSION_FACTORY.getOne(), EVALUABLE_EXPRESSION_FACTORY.getOne());

		assertThat(() -> testedExpression.receive(null), throwsException(NullPointerException.class));
		testedExpression.receive(mockVisitor);

		then(mockVisitor).should().visit(same(testedExpression));
	}

	/**
	 * Test method for {@link IfThenElseExpression#evaluate(EvaluableVariableAssignment)}.
	 * Asserts that:
	 *
	 * <ul>
	 *
	 * <li>If anything but 0 is returned for {@code if}, {@code then}, otherwise
	 * {@code else} is returned.
	 *
	 * <li>Passing {@code null} throws a {@link NullPointerException}.
	 *
	 * </ul>
	 */
	@Test
	public void testEvaluate() {
		final EvaluableExpression ifExpression = mock(EvaluableExpression.class);
		final EvaluableExpression thenExpression = mock(EvaluableExpression.class);
		final EvaluableExpression elseExpression = mock(EvaluableExpression.class);
		final EvaluableVariableAssignment assignment = new EvaluableVariableAssignment();
		final IfThenElseExpression testedExpression =
			new IfThenElseExpression(ifExpression, thenExpression, elseExpression);

		assertThat(() -> testedExpression.evaluate(null), throwsException(NullPointerException.class));

		given(ifExpression.evaluate(same(assignment))).willReturn(0d);
		given(thenExpression.evaluate(same(assignment))).willReturn(3d);
		given(elseExpression.evaluate(same(assignment))).willReturn(5d);
		assertThat(testedExpression.evaluate(assignment), is(5d));

		given(ifExpression.evaluate(same(assignment))).willReturn(1d);
		given(thenExpression.evaluate(same(assignment))).willReturn(3d);
		given(elseExpression.evaluate(same(assignment))).willReturn(5d);
		assertThat(testedExpression.evaluate(assignment), is(3d));

		given(ifExpression.evaluate(same(assignment))).willReturn(Double.NaN);
		given(thenExpression.evaluate(same(assignment))).willReturn(3d);
		given(elseExpression.evaluate(same(assignment))).willReturn(5d);
		assertThat(testedExpression.evaluate(assignment), is(3d));
	}

	/**
	 * Test method for {@link IfThenElseExpression#toString()}. Asserts that the method
	 * was overridden.
	 */
	@Test
	public void testToString() {
		assertThat(new IfThenElseExpression(EVALUABLE_EXPRESSION_FACTORY.getOne(),
			EVALUABLE_EXPRESSION_FACTORY.getOne(), EVALUABLE_EXPRESSION_FACTORY.getOne()), hasOverriddenToString());
	}

	/**
	 * Test method for {@link IfThenElseExpression#equals(java.lang.Object)} and
	 * {@link IfThenElseExpression#hashCode()}.
	 *
	 * <ul>
	 *
	 * <li>All and only expressions with equal {@code if}, {@code then} and {@code else}
	 * expressions are equal.
	 *
	 * <li>{@code hashCode} is implemented accordingly
	 *
	 * </ul>
	 */
	@Test
	public void testEqualsAndHashCode() {
		final IfThenElseExpression equalOne = new IfThenElseExpression(EVALUABLE_EXPRESSION_FACTORY.getAll()[0],
			EVALUABLE_EXPRESSION_FACTORY.getAll()[1], EVALUABLE_EXPRESSION_FACTORY.getAll()[2]);
		final IfThenElseExpression equalTwo = new IfThenElseExpression(EVALUABLE_EXPRESSION_FACTORY.getAll()[0],
			EVALUABLE_EXPRESSION_FACTORY.getAll()[1], EVALUABLE_EXPRESSION_FACTORY.getAll()[2]);
		final IfThenElseExpression differentIf = new IfThenElseExpression(EVALUABLE_EXPRESSION_FACTORY.getAll()[3],
			EVALUABLE_EXPRESSION_FACTORY.getAll()[1], EVALUABLE_EXPRESSION_FACTORY.getAll()[2]);
		final IfThenElseExpression differentThen = new IfThenElseExpression(EVALUABLE_EXPRESSION_FACTORY.getAll()[0],
			EVALUABLE_EXPRESSION_FACTORY.getAll()[3], EVALUABLE_EXPRESSION_FACTORY.getAll()[2]);
		final IfThenElseExpression differentElse = new IfThenElseExpression(EVALUABLE_EXPRESSION_FACTORY.getAll()[0],
			EVALUABLE_EXPRESSION_FACTORY.getAll()[1], EVALUABLE_EXPRESSION_FACTORY.getAll()[3]);
		final IfThenElseExpression swappedOne = new IfThenElseExpression(EVALUABLE_EXPRESSION_FACTORY.getAll()[1],
			EVALUABLE_EXPRESSION_FACTORY.getAll()[0], EVALUABLE_EXPRESSION_FACTORY.getAll()[3]);
		final IfThenElseExpression swappedTwo = new IfThenElseExpression(EVALUABLE_EXPRESSION_FACTORY.getAll()[0],
			EVALUABLE_EXPRESSION_FACTORY.getAll()[2], EVALUABLE_EXPRESSION_FACTORY.getAll()[1]);
		final IfThenElseExpression swappedThree = new IfThenElseExpression(EVALUABLE_EXPRESSION_FACTORY.getAll()[2],
			EVALUABLE_EXPRESSION_FACTORY.getAll()[1], EVALUABLE_EXPRESSION_FACTORY.getAll()[0]);

		assertThat(equalOne, hasDefaultEqualsProperties());
		assertThat("expressions with equal inner expressions should be equal", equalOne, is(equalTo(equalTwo)));
		assertThat(equalOne.hashCode(), is(equalTwo.hashCode()));
		assertThat("expressions with different inner expressions must not be equal", equalOne,
			is(not(equalTo(differentIf))));
		assertThat("expressions with different inner expressions must not be equal", equalOne,
			is(not(equalTo(differentThen))));
		assertThat("expressions with different inner expressions must not be equal", equalOne,
			is(not(equalTo(differentElse))));
		assertThat("expressions with different inner expressions must not be equal", equalOne,
			is(not(equalTo(swappedOne))));
		assertThat("expressions with different inner expressions must not be equal", equalOne,
			is(not(equalTo(swappedTwo))));
		assertThat("expressions with different inner expressions must not be equal", equalOne,
			is(not(equalTo(swappedThree))));
	}

}
