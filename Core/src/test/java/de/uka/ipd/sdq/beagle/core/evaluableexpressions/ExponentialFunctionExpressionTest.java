package de.uka.ipd.sdq.beagle.core.evaluableexpressions;

import static de.uka.ipd.sdq.beagle.core.testutil.EqualsMatcher.hasDefaultEqualsProperties;
import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static de.uka.ipd.sdq.beagle.core.testutil.ToStringMatcher.hasOverriddenToString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notANumber;
import static org.hamcrest.Matchers.theInstance;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;

import de.uka.ipd.sdq.beagle.core.testutil.factories.EvaluableExpressionFactory;

import org.junit.Test;

/**
 * Tests for {@link ExponentialFunctionExpression}.
 *
 * @author Joshua Gleitze
 */
public class ExponentialFunctionExpressionTest {

	/**
	 * A factory for {@linkplain EvaluableExpression EvaluableExpressions} to easily
	 * obtain new instances from.
	 */
	private static final EvaluableExpressionFactory EVALUABLE_EXPRESSION_FACTORY = new EvaluableExpressionFactory();

	/**
	 * Test method for
	 * {@link ExponentialFunctionExpression#ExponentialFunctionExpression(EvaluableExpression)}
	 * . Asserts that construction is possible and {@code null} cannot be passed.
	 */
	@Test
	public void testConstructor() {
		assertThat(() -> new ExponentialFunctionExpression(null), throwsException(NullPointerException.class));
		new ExponentialFunctionExpression(EVALUABLE_EXPRESSION_FACTORY.getOne());
	}

	/**
	 * Test method for {@link ExponentialFunctionExpression#getExponent()}. Asserts that
	 * the exponent from the constructor is returned.
	 */
	@Test
	public void testGetExponent() {
		final EvaluableExpression exponent = EVALUABLE_EXPRESSION_FACTORY.getOne();
		assertThat("should return the exponent from the constructor",
			new ExponentialFunctionExpression(exponent).getExponent(), is(theInstance(exponent)));
	}

	/**
	 * Test method for
	 * {@link ExponentialFunctionExpression#receive(EvaluableExpressionVisitor)}. Asserts
	 * that {@link EvaluableExpressionVisitor#visit(ExponentialFunctionExpression)} is
	 * called.
	 */
	@Test
	public void testReceive() {
		final EvaluableExpressionVisitor mockVisitor = mock(EvaluableExpressionVisitor.class);
		final ExponentialFunctionExpression testedExpression =
			new ExponentialFunctionExpression(EVALUABLE_EXPRESSION_FACTORY.getOne());

		assertThat(() -> testedExpression.receive(null), throwsException(NullPointerException.class));
		testedExpression.receive(mockVisitor);

		then(mockVisitor).should().visit(same(testedExpression));
	}

	/**
	 * Test method for
	 * {@link ExponentialFunctionExpression#evaluate(EvaluableVariableAssignment)}.
	 * Asserts that:
	 *
	 * <ul>
	 *
	 * <li> The method passes the assignment to the inner expressions
	 *
	 * <li> It returns {@link Math#E} to the power of the {@code exponent}.
	 *
	 * <li>{@code NaN} and infinity values are handled like specified in IEEE 754.
	 *
	 * <li>Passing {@code null} throws an exception.
	 *
	 * </ul>
	 */
	@Test
	public void testEvaluate() {
		final EvaluableExpression exponent = mock(EvaluableExpression.class);
		final EvaluableVariableAssignment assignment = new EvaluableVariableAssignment();
		final ExponentialFunctionExpression testedExpression = new ExponentialFunctionExpression(exponent);

		assertThat(() -> testedExpression.evaluate(null), throwsException(NullPointerException.class));

		given(exponent.evaluate(same(assignment))).willReturn(0d);
		assertThat(testedExpression.evaluate(assignment), is(1d));

		given(exponent.evaluate(same(assignment))).willReturn(Double.POSITIVE_INFINITY);
		assertThat(testedExpression.evaluate(assignment), is(Double.POSITIVE_INFINITY));

		given(exponent.evaluate(same(assignment))).willReturn(Double.NaN);
		assertThat(testedExpression.evaluate(assignment), is(notANumber()));
	}

	/**
	 * Test method for {@link ExponentialFunctionExpression#toString()}. Asserts that the
	 * method was overridden.
	 */
	@Test
	public void testToString() {
		assertThat(new ExponentialFunctionExpression(EVALUABLE_EXPRESSION_FACTORY.getOne()), hasOverriddenToString());
	}

	/**
	 * Test method for {@link ExponentialFunctionExpression#equals(java.lang.Object)} and
	 * {@link ExponentialFunctionExpression#hashCode()}.
	 *
	 * <ul>
	 *
	 * <li>All and only expressions with equal exponents are equal
	 *
	 * <li>{@code hashCode} is implemented accordingly
	 *
	 * </ul>
	 */
	@Test
	public void testEqualsAndHashCode() {
		final ExponentialFunctionExpression equalOne =
			new ExponentialFunctionExpression(EVALUABLE_EXPRESSION_FACTORY.getAll()[0]);
		final ExponentialFunctionExpression equalTwo =
			new ExponentialFunctionExpression(EVALUABLE_EXPRESSION_FACTORY.getAll()[0]);
		final ExponentialFunctionExpression different =
			new ExponentialFunctionExpression(EVALUABLE_EXPRESSION_FACTORY.getAll()[1]);

		assertThat(equalOne, hasDefaultEqualsProperties());
		assertThat("two expressions with an equal exponents must be equal", equalOne, is(equalTo(equalTwo)));
		assertThat(equalOne.hashCode(), is(equalTwo.hashCode()));
		assertThat("two expressions with different exponents must not be equal", equalOne, is(not(equalTo(different))));
	}

}
