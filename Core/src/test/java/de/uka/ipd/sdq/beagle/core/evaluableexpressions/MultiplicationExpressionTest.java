package de.uka.ipd.sdq.beagle.core.evaluableexpressions;

import static de.uka.ipd.sdq.beagle.core.testutil.EqualsMatcher.hasDefaultEqualsProperties;
import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static de.uka.ipd.sdq.beagle.core.testutil.NullHandlingMatchers.notAcceptingNull;
import static de.uka.ipd.sdq.beagle.core.testutil.ToStringMatcher.hasOverriddenToString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;

import de.uka.ipd.sdq.beagle.core.testutil.factories.EvaluableExpressionFactory;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * Tests for {@link MultiplicationExpression}.
 *
 * @author Joshua Gleitze
 */
public class MultiplicationExpressionTest {

	/**
	 * A factory for {@linkplain EvaluableExpression EvaluableExpressions} to easily
	 * obtain new instances from.
	 */
	private static final EvaluableExpressionFactory EVALUABLE_EXPRESSION_FACTORY = new EvaluableExpressionFactory();

	/**
	 * Test method for
	 * {@link MultiplicationExpression#MultiplicationExpression(java.util.Collection)} and
	 * {@link MultiplicationExpression#MultiplicationExpression(EvaluableExpression...)}.
	 * Asserts that:
	 *
	 * <ul>
	 *
	 * <li>The constructor cannot be called with {@code null} as or in the passed
	 * expressions.
	 *
	 * <li>The constructor cannot be called with less than 2 expressions.
	 *
	 * <li>The constructor can be called with valid arguments.
	 *
	 * <li>The constructor copies the passed collection / array.
	 *
	 * </ul>
	 */
	@Test
	public void testConstructor() {
		assertThat("It must not be possible to create with null", MultiplicationExpression::new,
			is(notAcceptingNull(EVALUABLE_EXPRESSION_FACTORY.getAll())));
		assertThat("It must not be possible to create with null", MultiplicationExpression::new,
			is(notAcceptingNull(EVALUABLE_EXPRESSION_FACTORY.getAllAsSet())));

		assertThat("It must not be possible to create for an empty collection",
			() -> new MultiplicationExpression(new HashSet<>()), throwsException(IllegalArgumentException.class));
		assertThat("It must not be possible to create for no expression", MultiplicationExpression::new,
			throwsException(IllegalArgumentException.class));
		assertThat("It must not be possible to create for a collection containing only one expression",
			() -> new MultiplicationExpression(Arrays.asList(EVALUABLE_EXPRESSION_FACTORY.getOne())),
			throwsException(IllegalArgumentException.class));
		assertThat("It must not be possible to create for only one expression",
			() -> new MultiplicationExpression(EVALUABLE_EXPRESSION_FACTORY.getOne()),
			throwsException(IllegalArgumentException.class));

		new MultiplicationExpression(EVALUABLE_EXPRESSION_FACTORY.getAll());
		new MultiplicationExpression(EVALUABLE_EXPRESSION_FACTORY.getAllAsSet());

		final EvaluableExpression[] testInput = EVALUABLE_EXPRESSION_FACTORY.getAll();
		final List<EvaluableExpression> listInput = new ArrayList<>(Arrays.asList(testInput));
		final EvaluableExpression[] arrayInput = ArrayUtils.clone(testInput);
		assert !testInput[0].equals(EVALUABLE_EXPRESSION_FACTORY.getOne());

		final MultiplicationExpression fromList = new MultiplicationExpression(listInput);
		final MultiplicationExpression fromArray = new MultiplicationExpression(arrayInput);
		listInput.set(0, EVALUABLE_EXPRESSION_FACTORY.getOne());
		arrayInput[0] = EVALUABLE_EXPRESSION_FACTORY.getOne();
		assertThat("Modifying the collection the expression was created from must not modify its content",
			fromList.getFactors(), containsInAnyOrder(testInput));
		assertThat("Modifying the array the expression was created from must not modify its content",
			fromArray.getFactors(), containsInAnyOrder(testInput));
	}

	/**
	 * Test method for {@link MultiplicationExpression#getFactors()}. Asserts that:
	 *
	 * <ul>
	 *
	 * <li>The method returns all expressions passed in the constructor.
	 *
	 * <li>Modifying the returned collection does not modify the expression.
	 *
	 * <li>The same expression can be returned multiple times.
	 *
	 * </ul>
	 */
	@Test
	public void testGetFactors() {
		final EvaluableExpression[] testInput = EVALUABLE_EXPRESSION_FACTORY.getAll();
		assertThat("The getters should return all expressions (from array)",
			new MultiplicationExpression(testInput).getFactors(), containsInAnyOrder(testInput));
		assertThat("The getters should return all expressions (from collection)",
			new MultiplicationExpression(Arrays.asList(testInput)).getFactors(), containsInAnyOrder(testInput));

		final EvaluableExpression[] doubleTestInput = ArrayUtils.addAll(testInput, testInput);
		assertThat("The expression may contain the same expression multiple times (from array)",
			new MultiplicationExpression(doubleTestInput).getFactors(), containsInAnyOrder(doubleTestInput));
		assertThat("The expression may contain the same expression multiple times (from collection)",
			new MultiplicationExpression(Arrays.asList(doubleTestInput)).getFactors(),
			containsInAnyOrder(doubleTestInput));

		final MultiplicationExpression testExpressionFromArray = new MultiplicationExpression(testInput);
		testExpressionFromArray.getFactors().add(EVALUABLE_EXPRESSION_FACTORY.getOne());
		assertThat("Modifying the returned collection must not modify the expression (from array)",
			testExpressionFromArray.getFactors(), containsInAnyOrder(testInput));
		final MultiplicationExpression testExpressionFromCollection =
			new MultiplicationExpression(Arrays.asList(testInput));
		testExpressionFromCollection.getFactors().add(EVALUABLE_EXPRESSION_FACTORY.getOne());
		assertThat("Modifying the returned collection must not modify the expression (from collection)",
			testExpressionFromCollection.getFactors(), containsInAnyOrder(testInput));
	}

	/**
	 * Test method for
	 * {@link MultiplicationExpression#receive(EvaluableExpressionVisitor)}. Asserts that:
	 *
	 * <ul>
	 *
	 * <li>{@link EvaluableExpressionVisitor#visit(MultiplicationExpression)} is called.
	 *
	 * </ul>
	 */
	@Test
	public void testReceive() {
		final EvaluableExpressionVisitor mockVisitor = mock(EvaluableExpressionVisitor.class);
		final MultiplicationExpression testExpression =
			new MultiplicationExpression(EVALUABLE_EXPRESSION_FACTORY.getAll());

		testExpression.receive(mockVisitor);

		then(mockVisitor).should().visit(same(testExpression));
	}

	/**
	 * Test method for
	 * {@link MultiplicationExpression#evaluate(EvaluableVariableAssignment)}. Asserts
	 * that:
	 *
	 * <ul>
	 *
	 * <li>The method passes the received {@link EvaluableVariableAssignment} to its inner
	 * expressions
	 *
	 * <li>The method calculates the product of its inner expressions’ values.
	 *
	 * </ul>
	 */
	@Test
	public void testEvaluate() {
		final EvaluableExpression firstMock = mock(EvaluableExpression.class);
		final EvaluableExpression secondMock = mock(EvaluableExpression.class);
		final EvaluableExpression thirdMock = mock(EvaluableExpression.class);
		final EvaluableExpression fourthMock = mock(EvaluableExpression.class);
		final EvaluableVariableAssignment assignment = new EvaluableVariableAssignment();
		final MultiplicationExpression testExpressionTwo = new MultiplicationExpression(firstMock, secondMock);
		final MultiplicationExpression testExpressionFour =
			new MultiplicationExpression(firstMock, secondMock, thirdMock, fourthMock);

		given(firstMock.evaluate(same(assignment))).willReturn(10d);
		given(secondMock.evaluate(same(assignment))).willReturn(3d);

		assertThat(testExpressionTwo.evaluate(assignment), is(30d));

		given(firstMock.evaluate(same(assignment))).willReturn(1d);
		given(secondMock.evaluate(same(assignment))).willReturn(2d);
		given(thirdMock.evaluate(same(assignment))).willReturn(4d);
		given(fourthMock.evaluate(same(assignment))).willReturn(-.5d);

		assertThat(testExpressionFour.evaluate(assignment), is(-4d));
	}

	/**
	 * Test method for {@link MultiplicationExpression#toString()}.
	 */
	@Test
	public void testToString() {
		assertThat(new MultiplicationExpression(EVALUABLE_EXPRESSION_FACTORY.getAll()), hasOverriddenToString());
	}

	/**
	 * Test method for {@link MultiplicationExpression#equals(java.lang.Object)} and
	 * {@link MultiplicationExpression#hashCode()}. Asserts that:
	 *
	 * <ul>
	 *
	 * <li>{@code equals} has certain default properties.
	 *
	 * <li>Expressions containing {@code equal} exressions are {@code equal}, no matter of
	 * their order and way of creation.
	 *
	 * <li>{@code hashCode} matches {@code equals}.
	 *
	 * </ul>
	 */
	@Test
	public void testEqualsObject() {
		final MultiplicationExpression defaultOne = new MultiplicationExpression(EVALUABLE_EXPRESSION_FACTORY.getAll());
		final MultiplicationExpression defaultTwo = new MultiplicationExpression(EVALUABLE_EXPRESSION_FACTORY.getAll());
		final MultiplicationExpression defaultFromCollection =
			new MultiplicationExpression(EVALUABLE_EXPRESSION_FACTORY.getAllAsSet());
		final EvaluableExpression[] reversed = EVALUABLE_EXPRESSION_FACTORY.getAll();
		ArrayUtils.reverse(reversed);
		final MultiplicationExpression defaultReversed = new MultiplicationExpression(reversed);

		assertThat(defaultOne, hasDefaultEqualsProperties());
		assertThat("Two expressions with equal inner expressions should be equal", defaultOne, is(equalTo(defaultTwo)));
		assertThat("Equal expressions must have the same hashCode", defaultOne.hashCode(), is(defaultTwo.hashCode()));
		assertThat("Two expressions with equal inner expressions should be equal, regardless of how they were created.",
			defaultFromCollection, is(equalTo(defaultOne)));
		assertThat("Equal expressions must have the same hashCode", defaultFromCollection.hashCode(),
			is(defaultOne.hashCode()));
		assertThat("Two expressions with equal inner expressions should be equal, regardless of the order at creation",
			defaultReversed, is(equalTo(defaultOne)));
		assertThat("Equal expressions must have the same hashCode", defaultReversed.hashCode(),
			is(defaultOne.hashCode()));
		assertThat("Two expressions with equal inner expressions should be equal, regardless of the order at creation",
			defaultFromCollection, is(equalTo(defaultReversed)));
		assertThat("Equal expressions must have the same hashCode", defaultFromCollection.hashCode(),
			is(defaultReversed.hashCode()));
	}

}
