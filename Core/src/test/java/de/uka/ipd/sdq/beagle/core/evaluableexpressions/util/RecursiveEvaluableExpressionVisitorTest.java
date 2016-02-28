package de.uka.ipd.sdq.beagle.core.evaluableexpressions.util;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.fail;

import de.uka.ipd.sdq.beagle.core.evaluableexpressions.AdditionExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.ComparisonExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.ConstantExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.DivisionExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableVariable;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.ExponentationExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.ExponentialFunctionExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.IfThenElseExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.LogarithmExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.MultiplicationExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.NaturalLogarithmExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.SineExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.SubtractionExpression;

import org.junit.Test;

/**
 * Tests {@link RecursiveEvaluableExpressionVisitor} and contains the test methods needed
 * to check all methods.
 *
 * @author Annika Berger
 */
public class RecursiveEvaluableExpressionVisitorTest {

	/**
	 * Tests if the {@link RecursiveEvaluableExpressionVisitor} works correctly for one
	 * specific expression. This is done by implementing a private
	 * {@link RecursiveEvaluableExpressionVisitor} which has assertions to check the
	 * result.
	 *
	 */
	@Test
	public void allMethods() {
		final EvaluableExpression expression = new SubtractionExpression(
			new DivisionExpression(new EvaluableVariable("a"),
				new LogarithmExpression(ConstantExpression.forValue(2),
					new SineExpression(
						new DivisionExpression(ConstantExpression.forValue(4.32), new EvaluableVariable("c"))))),
			new ExponentationExpression(ConstantExpression.forValue(393.123),
				new DivisionExpression(ConstantExpression.forValue(-1),
					new IfThenElseExpression(
						new ComparisonExpression(new EvaluableVariable("z"), ConstantExpression.forValue(0)),
						new NaturalLogarithmExpression(new SineExpression(ConstantExpression.forValue(1))),
						new ExponentialFunctionExpression(new EvaluableVariable("g"))))));

		final TestRecursiveEvaluableExpressionVisitor visitor = new TestRecursiveEvaluableExpressionVisitor();
		visitor.visitRecursively(expression);
		assertThat(visitor.getCounterAt(), is(22));
		assertThat(visitor.getCounterAfter(), is(22));
		
		assertThat(visitor.willTraverse(), is(true));
		visitor.findExpression(ConstantExpression.forValue(2), expression);
		final int visitedExpressions = visitor.getVisitedCount();
		assertThat(visitor.willTraverse(), is(false));
		visitor.continueTraversal();
		assertThat(visitor.willTraverse(), is(true));
		assertThat(visitor.getVisitedCount(), is(greaterThan(visitedExpressions)));
	}

	/**
	 * Class used to test {@link RecursiveEvaluableExpressionVisitor}.
	 *
	 * @author Annika Berger
	 */
	private class TestRecursiveEvaluableExpressionVisitor extends RecursiveEvaluableExpressionVisitor {
		
		/**
		 * {@link EvaluableExpression} at which traversal should stop.
		 */
		private EvaluableExpression searchedExpression;

		/**
		 * Counter used to assert that every expression is visited.
		 */
		private int counterAt;

		/**
		 * Counter used to assert that every expression is visited.
		 */
		private int counterAfter;

		/**
		 * Counter which is increase for every
		 * {@link RecursiveEvaluableExpressionVisitor#atExpression(EvaluableExpression)}
		 * call.
		 *
		 * @return number of visited Expressions.
		 */
		public int getCounterAt() {
			return this.counterAt;
		}

		/**
		 * Counter which is increase for every
		 * {@link RecursiveEvaluableExpressionVisitor#afterExpression(EvaluableExpression)}
		 * call.
		 *
		 * @return number of visited Expressions.
		 */
		public int getCounterAfter() {
			return this.counterAfter;
		}
		
		/**
		 * Method used to find an Expression with the help of this visitor. Traversal stops if expression is found.
		 *
		 * @param expression {@link EvaluableExpression} to search for
		 * @param start {@link EvaluableExpression} which should contain the searched expression
		 */
		public void findExpression(final EvaluableExpression expression, final EvaluableExpression start) {
			this.searchedExpression = expression;
			this.visitRecursively(start);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see de.uka.ipd.sdq.beagle.core.evaluableexpressions.util.ExpressionTreeWalker#
		 * atOther(de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpression)
		 */
		@Override
		protected void atOther(final EvaluableExpression expression) {
			fail("This method should not be called as there is no AdditionExpression in this expression.");
		}

		@Override
		protected void atExpression(final EvaluableExpression expression) {
			this.counterAt++;
			if (expression == this.searchedExpression) {
				stopTraversal();
			}
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see de.uka.ipd.sdq.beagle.core.evaluableexpressions.util.ExpressionTreeWalker#
		 * atAddition(de.uka.ipd.sdq.beagle.core.evaluableexpressions.AdditionExpression)
		 */
		@Override
		protected void atAddition(final AdditionExpression expression) {
			fail("This method should not be called as there is no AdditionExpression in this expression.");
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see de.uka.ipd.sdq.beagle.core.evaluableexpressions.util.ExpressionTreeWalker#
		 * atMultiplication(de.uka.ipd.sdq.beagle.core.evaluableexpressions.
		 * MultiplicationExpression)
		 */
		@Override
		protected void atMultiplication(final MultiplicationExpression expression) {
			fail("This method should not be called as there is no MultiplicationExpression in this expression.");
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see de.uka.ipd.sdq.beagle.core.evaluableexpressions.util.ExpressionTreeWalker#
		 * atVariable(de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableVariable)
		 */
		@Override
		protected void atVariable(final EvaluableVariable expression) {
			switch (expression.getName()) {
				case "a":
					assertThat("Wrong Depth", this.getTraversalDepth(), is(2));
					assertThat("Wrong Count", this.getVisitedCount(), is(3));
					break;

				case "c":
					assertThat("Wrong Depth", this.getTraversalDepth(), is(5));
					assertThat("Wrong Count", this.getVisitedCount(), is(9));
					break;

				case "z":
					assertThat("Wrong Depth", this.getTraversalDepth(), is(5));
					assertThat("Wrong Count", this.getVisitedCount(), is(16));
					break;

				case "g":
					assertThat("Wrong Depth", this.getTraversalDepth(), is(5));
					assertThat("Wrong Count", this.getVisitedCount(), is(22));
					break;

				default:
					fail(String.format("There must be no variable %s in the visited expression", expression.getName()));
			}
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see de.uka.ipd.sdq.beagle.core.evaluableexpressions.util.ExpressionTreeWalker#
		 * atComparison(de.uka.ipd.sdq.beagle.core.evaluableexpressions.
		 * ComparisonExpression)
		 */
		@Override
		protected void atComparison(final ComparisonExpression expression) {
			assertThat("Wrong Depth", this.getTraversalDepth(), is(4));
			assertThat("Wrong Count", this.getVisitedCount(), is(15));
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see de.uka.ipd.sdq.beagle.core.evaluableexpressions.util.ExpressionTreeWalker#
		 * atConstant(de.uka.ipd.sdq.beagle.core.evaluableexpressions.ConstantExpression)
		 */
		@Override
		protected void atConstant(final ConstantExpression expression) {
			final double value = expression.getValue();
			if (value == 2) {
				assertThat("Wrong Depth", this.getTraversalDepth(), is(3));
				assertThat("Wrong Count", this.getVisitedCount(), is(5));

			} else if (value == 4.32) {
				assertThat("Wrong Depth", this.getTraversalDepth(), is(5));
				assertThat("Wrong Count", this.getVisitedCount(), is(8));

			} else if (value == 393.123) {
				assertThat("Wrong Depth", this.getTraversalDepth(), is(2));
				assertThat("Wrong Count", this.getVisitedCount(), is(11));

			} else if (value == -1) {
				assertThat("Wrong Depth", this.getTraversalDepth(), is(3));
				assertThat("Wrong Count", this.getVisitedCount(), is(13));

			} else if (value == 0) {
				assertThat("Wrong Depth", this.getTraversalDepth(), is(5));
				assertThat("Wrong Count", this.getVisitedCount(), is(17));

			} else if (value == 1) {
				assertThat("Wrong Depth", this.getTraversalDepth(), is(6));
				assertThat("Wrong Count", this.getVisitedCount(), is(20));

			} else {
				fail(String.format("There must be no constant with value %s in the visited expression",
					expression.getValue()));
			}
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see de.uka.ipd.sdq.beagle.core.evaluableexpressions.util.ExpressionTreeWalker#
		 * atDivision(de.uka.ipd.sdq.beagle.core.evaluableexpressions.DivisionExpression)
		 */
		@Override
		protected void atDivision(final DivisionExpression expression) {
			final String dividend = expression.getDividend().toString();
			switch (dividend) {
				case "a":
					assertThat("Wrong Depth", this.getTraversalDepth(), is(1));
					assertThat("Wrong Count", this.getVisitedCount(), is(2));
					break;

				case "4.32":
					assertThat("Wrong Depth", this.getTraversalDepth(), is(4));
					assertThat("Wrong Count", this.getVisitedCount(), is(7));
					break;

				case "-1.0":
					assertThat("Wrong Depth", this.getTraversalDepth(), is(2));
					assertThat("Wrong Count", this.getVisitedCount(), is(12));
					break;

				default:
					fail(String.format("There must be no division with %s as dividend in the visited expression",
						dividend));
			}
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see de.uka.ipd.sdq.beagle.core.evaluableexpressions.util.ExpressionTreeWalker#
		 * atExponentation(de.uka.ipd.sdq.beagle.core.evaluableexpressions.
		 * ExponentationExpression)
		 */
		@Override
		protected void atExponentation(final ExponentationExpression expression) {
			assertThat("Wrong Depth", this.getTraversalDepth(), is(1));
			assertThat("Wrong Count", this.getVisitedCount(), is(10));
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see de.uka.ipd.sdq.beagle.core.evaluableexpressions.util.ExpressionTreeWalker#
		 * atExponentialFunction(de.uka.ipd.sdq.beagle.core.evaluableexpressions.
		 * ExponentialFunctionExpression)
		 */
		@Override
		protected void atExponentialFunction(final ExponentialFunctionExpression expression) {
			assertThat("Wrong Depth", this.getTraversalDepth(), is(4));
			assertThat("Wrong Count", this.getVisitedCount(), is(21));
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see de.uka.ipd.sdq.beagle.core.evaluableexpressions.util.ExpressionTreeWalker#
		 * atIfThenElse(de.uka.ipd.sdq.beagle.core.evaluableexpressions.
		 * IfThenElseExpression)
		 */
		@Override
		protected void atIfThenElse(final IfThenElseExpression expression) {
			assertThat("Wrong Depth", this.getTraversalDepth(), is(3));
			assertThat("Wrong Count", this.getVisitedCount(), is(14));
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see de.uka.ipd.sdq.beagle.core.evaluableexpressions.util.ExpressionTreeWalker#
		 * atLogarithm(de.uka.ipd.sdq.beagle.core.evaluableexpressions.
		 * LogarithmExpression)
		 */
		@Override
		protected void atLogarithm(final LogarithmExpression expression) {
			assertThat("Wrong Depth", this.getTraversalDepth(), is(2));
			assertThat("Wrong Count", this.getVisitedCount(), is(4));
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see de.uka.ipd.sdq.beagle.core.evaluableexpressions.util.ExpressionTreeWalker#
		 * atNaturalLogarithm(de.uka.ipd.sdq.beagle.core.evaluableexpressions.
		 * NaturalLogarithmExpression)
		 */
		@Override
		protected void atNaturalLogarithm(final NaturalLogarithmExpression expression) {
			assertThat("Wrong Depth", this.getTraversalDepth(), is(4));
			assertThat("Wrong Count", this.getVisitedCount(), is(18));
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see de.uka.ipd.sdq.beagle.core.evaluableexpressions.util.ExpressionTreeWalker#
		 * atSine(de.uka.ipd.sdq.beagle.core.evaluableexpressions.SineExpression)
		 */
		@Override
		protected void atSine(final SineExpression expression) {
			final String argument = expression.getArgument().toString();
			switch (argument) {
				case "1.0":
					assertThat("Wrong Depth", this.getTraversalDepth(), is(5));
					assertThat("Wrong Count", this.getVisitedCount(), is(19));
					break;

				case "(4.32 / c)":
					assertThat("Wrong Depth", this.getTraversalDepth(), is(3));
					assertThat("Wrong Count", this.getVisitedCount(), is(6));
					break;

				default:
					fail(
						String.format("There must be no sine with %s as argument in the visited expression", argument));
			}
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see de.uka.ipd.sdq.beagle.core.evaluableexpressions.util.ExpressionTreeWalker#
		 * atSubstraction(de.uka.ipd.sdq.beagle.core.evaluableexpressions.
		 * SubtractionExpression)
		 */
		@Override
		protected void atSubstraction(final SubtractionExpression expression) {
			assertThat("Wrong Depth", this.getTraversalDepth(), is(0));
			assertThat("Wrong Count", this.getVisitedCount(), is(1));
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see de.uka.ipd.sdq.beagle.core.evaluableexpressions.util.ExpressionTreeWalker#
		 * afterOther(de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpression)
		 */
		@Override
		protected void afterOther(final EvaluableExpression expression) {
			fail("This method should not be called.");
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see de.uka.ipd.sdq.beagle.core.evaluableexpressions.util.ExpressionTreeWalker#
		 * afterExpression(de.uka.ipd.sdq.beagle.core.evaluableexpressions.
		 * EvaluableExpression)
		 */
		@Override
		protected void afterExpression(final EvaluableExpression expression) {
			this.counterAfter++;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see de.uka.ipd.sdq.beagle.core.evaluableexpressions.util.ExpressionTreeWalker#
		 * afterAddition(de.uka.ipd.sdq.beagle.core.evaluableexpressions.
		 * AdditionExpression)
		 */
		@Override
		protected void afterAddition(final AdditionExpression expression) {
			fail("This method should not be called as there is no Addition Expression.");
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see de.uka.ipd.sdq.beagle.core.evaluableexpressions.util.ExpressionTreeWalker#
		 * afterMultiplication(de.uka.ipd.sdq.beagle.core.evaluableexpressions.
		 * MultiplicationExpression)
		 */
		@Override
		protected void afterMultiplication(final MultiplicationExpression expression) {
			fail("This method should not be called as there is no multiplication expression.");
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see de.uka.ipd.sdq.beagle.core.evaluableexpressions.util.ExpressionTreeWalker#
		 * afterVariable(de.uka.ipd.sdq.beagle.core.evaluableexpressions.
		 * EvaluableVariable)
		 */
		@Override
		protected void afterVariable(final EvaluableVariable expression) {
			switch (expression.getName()) {
				case "a":
					assertThat("Wrong Depth", this.getTraversalDepth(), is(2));
					assertThat("Wrong Count", this.getVisitedCount(), is(3));
					break;

				case "c":
					assertThat("Wrong Depth", this.getTraversalDepth(), is(5));
					assertThat("Wrong Count", this.getVisitedCount(), is(9));
					break;

				case "z":
					assertThat("Wrong Depth", this.getTraversalDepth(), is(5));
					assertThat("Wrong Count", this.getVisitedCount(), is(16));
					break;

				case "g":
					assertThat("Wrong Depth", this.getTraversalDepth(), is(5));
					assertThat("Wrong Count", this.getVisitedCount(), is(22));
					break;

				default:
					fail(String.format("There must be no variable %s in the visited expression", expression.getName()));
			}
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see de.uka.ipd.sdq.beagle.core.evaluableexpressions.util.ExpressionTreeWalker#
		 * afterComparison(de.uka.ipd.sdq.beagle.core.evaluableexpressions.
		 * ComparisonExpression)
		 */
		@Override
		protected void afterComparison(final ComparisonExpression expression) {
			assertThat("Wrong Depth", this.getTraversalDepth(), is(4));
			assertThat("Wrong Count", this.getVisitedCount(), is(17));
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see de.uka.ipd.sdq.beagle.core.evaluableexpressions.util.ExpressionTreeWalker#
		 * afterConstant(de.uka.ipd.sdq.beagle.core.evaluableexpressions.
		 * ConstantExpression)
		 */
		@Override
		protected void afterConstant(final ConstantExpression expression) {
			final double value = expression.getValue();
			if (value == 2) {
				assertThat("Wrong Depth", this.getTraversalDepth(), is(3));
				assertThat("Wrong Count", this.getVisitedCount(), is(5));

			} else if (value == 4.32) {
				assertThat("Wrong Depth", this.getTraversalDepth(), is(5));
				assertThat("Wrong Count", this.getVisitedCount(), is(8));

			} else if (value == 393.123) {
				assertThat("Wrong Depth", this.getTraversalDepth(), is(2));
				assertThat("Wrong Count", this.getVisitedCount(), is(11));

			} else if (value == -1) {
				assertThat("Wrong Depth", this.getTraversalDepth(), is(3));
				assertThat("Wrong Count", this.getVisitedCount(), is(13));

			} else if (value == 0) {
				assertThat("Wrong Depth", this.getTraversalDepth(), is(5));
				assertThat("Wrong Count", this.getVisitedCount(), is(17));

			} else if (value == 1) {
				assertThat("Wrong Depth", this.getTraversalDepth(), is(6));
				assertThat("Wrong Count", this.getVisitedCount(), is(20));

			} else {
				fail(String.format("There must be no constant with value %s in the visited expression",
					expression.getValue()));
			}
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see de.uka.ipd.sdq.beagle.core.evaluableexpressions.util.ExpressionTreeWalker#
		 * afterDivision(de.uka.ipd.sdq.beagle.core.evaluableexpressions.
		 * DivisionExpression)
		 */
		@Override
		protected void afterDivision(final DivisionExpression expression) {
			final String dividend = expression.getDividend().toString();
			switch (dividend) {
				case "a":
					assertThat("Wrong Depth", this.getTraversalDepth(), is(1));
					assertThat("Wrong Count", this.getVisitedCount(), is(9));
					break;

				case "4.32":
					assertThat("Wrong Depth", this.getTraversalDepth(), is(4));
					assertThat("Wrong Count", this.getVisitedCount(), is(9));
					break;

				case "-1.0":
					assertThat("Wrong Depth", this.getTraversalDepth(), is(2));
					assertThat("Wrong Count", this.getVisitedCount(), is(22));
					break;

				default:
					fail(String.format("There must be no division with %s as dividend in the visited expression",
						dividend));
			}
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see de.uka.ipd.sdq.beagle.core.evaluableexpressions.util.ExpressionTreeWalker#
		 * afterExponentation(de.uka.ipd.sdq.beagle.core.evaluableexpressions.
		 * ExponentationExpression)
		 */
		@Override
		protected void afterExponentation(final ExponentationExpression expression) {
			assertThat("Wrong Depth", this.getTraversalDepth(), is(1));
			assertThat("Wrong Count", this.getVisitedCount(), is(22));
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see de.uka.ipd.sdq.beagle.core.evaluableexpressions.util.ExpressionTreeWalker#
		 * afterExponentialFunction(de.uka.ipd.sdq.beagle.core.evaluableexpressions.
		 * ExponentialFunctionExpression)
		 */
		@Override
		protected void afterExponentialFunction(final ExponentialFunctionExpression expression) {
			assertThat("Wrong Depth", this.getTraversalDepth(), is(4));
			assertThat("Wrong Count", this.getVisitedCount(), is(22));
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see de.uka.ipd.sdq.beagle.core.evaluableexpressions.util.ExpressionTreeWalker#
		 * afterIfThenElse(de.uka.ipd.sdq.beagle.core.evaluableexpressions.
		 * IfThenElseExpression)
		 */
		@Override
		protected void afterIfThenElse(final IfThenElseExpression expression) {
			assertThat("Wrong Depth", this.getTraversalDepth(), is(3));
			assertThat("Wrong Count", this.getVisitedCount(), is(22));
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see de.uka.ipd.sdq.beagle.core.evaluableexpressions.util.ExpressionTreeWalker#
		 * afterLogarithm(de.uka.ipd.sdq.beagle.core.evaluableexpressions.
		 * LogarithmExpression)
		 */
		@Override
		protected void afterLogarithm(final LogarithmExpression expression) {
			assertThat("Wrong Depth", this.getTraversalDepth(), is(2));
			assertThat("Wrong Count", this.getVisitedCount(), is(9));
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see de.uka.ipd.sdq.beagle.core.evaluableexpressions.util.ExpressionTreeWalker#
		 * afterNaturalLogarithm(de.uka.ipd.sdq.beagle.core.evaluableexpressions.
		 * NaturalLogarithmExpression)
		 */
		@Override
		protected void afterNaturalLogarithm(final NaturalLogarithmExpression expression) {
			assertThat("Wrong Depth", this.getTraversalDepth(), is(4));
			assertThat("Wrong Count", this.getVisitedCount(), is(20));
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see de.uka.ipd.sdq.beagle.core.evaluableexpressions.util.ExpressionTreeWalker#
		 * afterSine(de.uka.ipd.sdq.beagle.core.evaluableexpressions.SineExpression)
		 */
		@Override
		protected void afterSine(final SineExpression expression) {
			final String argument = expression.getArgument().toString();
			switch (argument) {
				case "1.0":
					assertThat("Wrong Depth", this.getTraversalDepth(), is(5));
					assertThat("Wrong Count", this.getVisitedCount(), is(20));
					break;

				case "(4.32 / c)":
					assertThat("Wrong Depth", this.getTraversalDepth(), is(3));
					assertThat("Wrong Count", this.getVisitedCount(), is(9));
					break;

				default:
					fail(
						String.format("There must be no sine with %s as argument in the visited expression", argument));
			}
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see de.uka.ipd.sdq.beagle.core.evaluableexpressions.util.ExpressionTreeWalker#
		 * afterSubstraction(de.uka.ipd.sdq.beagle.core.evaluableexpressions.
		 * SubtractionExpression)
		 */
		@Override
		protected void afterSubstraction(final SubtractionExpression expression) {
			assertThat("Wrong Depth", this.getTraversalDepth(), is(0));
			assertThat("Wrong Count", this.getVisitedCount(), is(22));
		}

	}

}
