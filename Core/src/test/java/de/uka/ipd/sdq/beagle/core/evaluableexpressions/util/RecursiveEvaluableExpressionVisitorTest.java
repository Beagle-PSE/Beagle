package de.uka.ipd.sdq.beagle.core.evaluableexpressions.util;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.stringContainsInOrder;
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

		final RecursiveEvaluableExpressionVisitor visitor = new TestRecursiveEvaluableExpressionVisitor();
		visitor.visitRecursively(expression);

		// getVisitedCount
	}

	/**
	 * Class used to test {@link RecursiveEvaluableExpressionVisitor}.
	 *
	 * @author Annika Berger
	 */
	private class TestRecursiveEvaluableExpressionVisitor extends RecursiveEvaluableExpressionVisitor {

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
				fail(String.format("There must be no constant with value %s in the visited expression", expression.getValue()));
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
					fail(String.format("There must be no division with %s as dividend in the visited expression", dividend));
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
					fail(String.format("There must be no sine with %s as argument in the visited expression", argument));
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
			// TODO Auto-generated method stub
			super.afterOther(expression);
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
			// TODO Auto-generated method stub
			super.afterExpression(expression);
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
			// TODO Auto-generated method stub
			super.afterAddition(expression);
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
			// TODO Auto-generated method stub
			super.afterMultiplication(expression);
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
			// TODO Auto-generated method stub
			super.afterVariable(expression);
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
			// TODO Auto-generated method stub
			super.afterComparison(expression);
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
			// TODO Auto-generated method stub
			super.afterConstant(expression);
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
			// TODO Auto-generated method stub
			super.afterDivision(expression);
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
			// TODO Auto-generated method stub
			super.afterExponentation(expression);
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
			// TODO Auto-generated method stub
			super.afterExponentialFunction(expression);
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
			// TODO Auto-generated method stub
			super.afterIfThenElse(expression);
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
			// TODO Auto-generated method stub
			super.afterLogarithm(expression);
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
			// TODO Auto-generated method stub
			super.afterNaturalLogarithm(expression);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see de.uka.ipd.sdq.beagle.core.evaluableexpressions.util.ExpressionTreeWalker#
		 * afterSine(de.uka.ipd.sdq.beagle.core.evaluableexpressions.SineExpression)
		 */
		@Override
		protected void afterSine(final SineExpression expression) {
			// TODO Auto-generated method stub
			super.afterSine(expression);
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
			// TODO Auto-generated method stub
			super.afterSubstraction(expression);
		}

	}

}
