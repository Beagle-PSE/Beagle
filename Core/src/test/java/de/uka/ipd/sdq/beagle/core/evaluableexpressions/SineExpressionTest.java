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
 * Tests for {@link SineExpression}.
 *
 * @author Joshua Gleitze
 */
public class SineExpressionTest {

	/**
	 * A factory for {@linkplain EvaluableExpression EvaluableExpressions} to easily
	 * obtain new instances from.
	 */
	private static final EvaluableExpressionFactory EVALUABLE_EXPRESSION_FACTORY = new EvaluableExpressionFactory();

	/**
	 * Test method for {@link SineExpression#SineExpression(EvaluableExpression)}. Asserts
	 * that construction is possible and {@code null} cannot be passed.
	 */
	@Test
	public void constructor() {
		assertThat(() -> new SineExpression(null), throwsException(NullPointerException.class));
		new SineExpression(EVALUABLE_EXPRESSION_FACTORY.getOne());
	}

	/**
	 * Test method for {@link SineExpression#getArgument()}. Asserts that the instance
	 * passed in the constructor is returned.
	 */
	@Test
	public void getArgument() {
		final EvaluableExpression argument = EVALUABLE_EXPRESSION_FACTORY.getOne();
		assertThat("should return the argument from the constructor", new SineExpression(argument).getArgument(),
			is(theInstance(argument)));
	}

	/**
	 * Test method for {@link SineExpression#receive(EvaluableExpressionVisitor)}. Asserts
	 * that {@link EvaluableExpressionVisitor#visit(SineExpression)} is called.
	 */
	@Test
	public void receive() {
		final EvaluableExpressionVisitor mockVisitor = mock(EvaluableExpressionVisitor.class);
		final SineExpression testedExpression = new SineExpression(EVALUABLE_EXPRESSION_FACTORY.getOne());

		assertThat(() -> testedExpression.receive(null), throwsException(NullPointerException.class));
		testedExpression.receive(mockVisitor);

		then(mockVisitor).should().visit(same(testedExpression));
	}

	/**
	 * Test method for {@link SineExpression#evaluate(EvaluableVariableAssignment)}.
	 * Asserts that:
	 *
	 * <ul>
	 *
	 * <li> The method passes the assignment to the inner expression.
	 *
	 * <li> It returns the sine of the {@code argument}.
	 *
	 * <li>{@code NaN} and infinity values are handled like specified in IEEE 754.
	 *
	 * <li>Passing {@code null} throws an exception.
	 *
	 * </ul>
	 */
	@Test
	public void evaluate() {
		final EvaluableExpression argument = mock(EvaluableExpression.class);
		final EvaluableVariableAssignment assignment = new EvaluableVariableAssignment();
		final SineExpression testedExpression = new SineExpression(argument);

		assertThat(() -> testedExpression.evaluate(null), throwsException(NullPointerException.class));

		given(argument.evaluate(same(assignment))).willReturn(0d);
		assertThat(testedExpression.evaluate(assignment), is(0d));

		given(argument.evaluate(same(assignment))).willReturn(3 * Math.PI / 2);
		assertThat(testedExpression.evaluate(assignment), is(-1d));

		given(argument.evaluate(same(assignment))).willReturn(Double.NaN);
		assertThat(testedExpression.evaluate(assignment), is(notANumber()));

		given(argument.evaluate(same(assignment))).willReturn(Double.NEGATIVE_INFINITY);
		assertThat(testedExpression.evaluate(assignment), is(notANumber()));

		given(argument.evaluate(same(assignment))).willReturn(Double.POSITIVE_INFINITY);
		assertThat(testedExpression.evaluate(assignment), is(notANumber()));
	}

	/**
	 * Test method for {@link SineExpression#toString()}. Asserts that the method was
	 * overridden.
	 */
	@Test
	public void toStringT() {
		assertThat(new SineExpression(EVALUABLE_EXPRESSION_FACTORY.getOne()), hasOverriddenToString());
	}

	/**
	 * Test method for {@link SineExpression#equals(java.lang.Object)}. Asserts that:
	 *
	 * <ul>
	 *
	 * <li>All and only expressions with equal arguments are equal
	 *
	 * <li>{@code hashCode} is implemented accordingly
	 *
	 * </ul>
	 */
	@Test
	public void equalsObject() {
		final SineExpression equalOne = new SineExpression(EVALUABLE_EXPRESSION_FACTORY.getAll()[0]);
		final SineExpression equalTwo = new SineExpression(EVALUABLE_EXPRESSION_FACTORY.getAll()[0]);
		final SineExpression different = new SineExpression(EVALUABLE_EXPRESSION_FACTORY.getAll()[1]);

		assertThat(equalOne, hasDefaultEqualsProperties());
		assertThat("two expressions with an equal argument must be equal", equalOne, is(equalTo(equalTwo)));
		assertThat(equalOne.hashCode(), is(equalTwo.hashCode()));
		assertThat("two expressions with different arguments must not be equal", equalOne, is(not(equalTo(different))));
	}

}
