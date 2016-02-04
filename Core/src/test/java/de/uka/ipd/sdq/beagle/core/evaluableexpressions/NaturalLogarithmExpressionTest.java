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
 * Tests for {@link NaturalLogarithmExpression}.
 *
 * @author Joshua Gleitze
 */
public class NaturalLogarithmExpressionTest {

	/**
	 * A factory for {@linkplain EvaluableExpression EvaluableExpressions} to easily
	 * obtain new instances from.
	 */
	private static final EvaluableExpressionFactory EVALUABLE_EXPRESSION_FACTORY = new EvaluableExpressionFactory();

	/**
	 * Test method for
	 * {@link NaturalLogarithmExpression#NaturalLogarithmExpression(EvaluableExpression)}
	 * . Asserts that construction is possible and {@code null} cannot be passed.
	 */
	@Test
	public void constructor() {
		assertThat(() -> new NaturalLogarithmExpression(null), throwsException(NullPointerException.class));
		new NaturalLogarithmExpression(EVALUABLE_EXPRESSION_FACTORY.getOne());
	}

	/**
	 * Test method for {@link NaturalLogarithmExpression#getAntilogarithm()}. Asserts that
	 * the instance from the constructor is returned.
	 */
	@Test
	public void getAntilogarithm() {
		final EvaluableExpression antilogarithm = EVALUABLE_EXPRESSION_FACTORY.getOne();
		assertThat("should return the antilogarithm from the constructor",
			new NaturalLogarithmExpression(antilogarithm).getAntilogarithm(), is(theInstance(antilogarithm)));
	}

	/**
	 * Test method for
	 * {@link NaturalLogarithmExpression#receive(EvaluableExpressionVisitor)}. Asserts
	 * that {@link EvaluableExpressionVisitor#visit(NaturalLogarithmExpression)} is
	 * called.
	 */
	@Test
	public void receive() {
		final EvaluableExpressionVisitor mockVisitor = mock(EvaluableExpressionVisitor.class);
		final NaturalLogarithmExpression testedExpression =
			new NaturalLogarithmExpression(EVALUABLE_EXPRESSION_FACTORY.getOne());

		assertThat(() -> testedExpression.receive(null), throwsException(NullPointerException.class));
		testedExpression.receive(mockVisitor);

		then(mockVisitor).should().visit(same(testedExpression));
	}

	/**
	 * Test method for
	 * {@link NaturalLogarithmExpression#evaluate(EvaluableVariableAssignment)}. Asserts
	 * that:
	 *
	 * <ul>
	 *
	 * <li> The method passes the assignment to the inner expressions
	 *
	 * <li> It returns the logarithm of {@code antilogarithm} to base {@link Math#E}.
	 *
	 * <li>{@code NaN} and infinity values are handled like specified in IEEE 754.
	 *
	 * <li>Passing {@code null} throws an exception.
	 *
	 * </ul>
	 */
	@Test
	public void evaluate() {
		final EvaluableExpression antilogarithm = mock(EvaluableExpression.class);
		final EvaluableVariableAssignment assignment = new EvaluableVariableAssignment();
		final NaturalLogarithmExpression testedExpression = new NaturalLogarithmExpression(antilogarithm);

		assertThat(() -> testedExpression.evaluate(null), throwsException(NullPointerException.class));

		given(antilogarithm.evaluate(same(assignment))).willReturn(Math.E);
		assertThat(testedExpression.evaluate(assignment), is(1d));

		given(antilogarithm.evaluate(same(assignment))).willReturn(Double.POSITIVE_INFINITY);
		assertThat(testedExpression.evaluate(assignment), is(Double.POSITIVE_INFINITY));

		given(antilogarithm.evaluate(same(assignment))).willReturn(Double.NaN);
		assertThat(testedExpression.evaluate(assignment), is(notANumber()));
	}

	/**
	 * Test method for {@link NaturalLogarithmExpression#toString()}. Asserts that the
	 * method was overridden.
	 */
	@Test
	public void toStringT() {
		assertThat(new NaturalLogarithmExpression(EVALUABLE_EXPRESSION_FACTORY.getOne()), hasOverriddenToString());
	}

	/**
	 * Test method for {@link NaturalLogarithmExpression#equals(java.lang.Object)}.
	 * Asserts that:
	 *
	 * <ul>
	 *
	 * <li>All and only expressions with equal antilogarithms are equal
	 *
	 * <li>{@code hashCode} is implemented accordingly
	 *
	 * </ul>
	 */
	@Test
	public void equalsObject() {
		final NaturalLogarithmExpression equalOne =
			new NaturalLogarithmExpression(EVALUABLE_EXPRESSION_FACTORY.getAll()[0]);
		final NaturalLogarithmExpression equalTwo =
			new NaturalLogarithmExpression(EVALUABLE_EXPRESSION_FACTORY.getAll()[0]);
		final NaturalLogarithmExpression different =
			new NaturalLogarithmExpression(EVALUABLE_EXPRESSION_FACTORY.getAll()[1]);

		assertThat(equalOne, hasDefaultEqualsProperties());
		assertThat("two expressions with an equal antilogarithm must be equal", equalOne, is(equalTo(equalTwo)));
		assertThat(equalOne.hashCode(), is(equalTwo.hashCode()));
		assertThat("two expressions with different antilogarithms must not be equal", equalOne,
			is(not(equalTo(different))));
	}

}
