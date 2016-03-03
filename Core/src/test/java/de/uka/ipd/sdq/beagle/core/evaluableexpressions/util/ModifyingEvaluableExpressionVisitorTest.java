package de.uka.ipd.sdq.beagle.core.evaluableexpressions.util;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
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
 * Tests {@link ModifyingEvaluableExpressionVisitor} and contains all needed test cases.
 * 
 * @author Annika Berger
 */
public class ModifyingEvaluableExpressionVisitorTest {

	/**
	 * Builds an {@link EvaluableExpression} which can be used in the other test methods.
	 *
	 * @return the {@link EvaluableExpression}.
	 */
	private EvaluableExpression buildExpression() {
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
		return expression;
	}

	/**
	 * Tests if {@link ModifyingEvaluableExpressionVisitor} visits the expression
	 * recursively.
	 */
	@Test
	public void visit() {
		final EvaluableExpression expression = this.buildExpression();
		final TestModifyingExpressionVisitor visitor = new TestModifyingExpressionVisitor();
		visitor.modifyRecursively(expression);
		assertThat(visitor.getCounterAt(), is(22));
		assertThat(visitor.getCounterAfter(), is(22));

		// ersetze aktuelle Expression mit replace (mit anderem Visitor)
		// equals zu erwarteten
		// weiterhin richtige traversierung
	}

	/**
	 * Asserts that modifying works correctly.
	 */
	@Test
	public void modify() {
		final EvaluableExpression expression = this.buildExpression();

		final ReplaceDivisionVisitor divVisitor = new ReplaceDivisionVisitor();
		EvaluableExpression correctModified = new SubtractionExpression(new EvaluableVariable("a"),
			new ExponentationExpression(ConstantExpression.forValue(393.123), ConstantExpression.forValue(-1)));
		assertThat(divVisitor.modifyRecursively(expression), is(correctModified));

		final RenameVariablesVisitor varVisitor = new RenameVariablesVisitor();
		correctModified = new SubtractionExpression(
			new DivisionExpression(new EvaluableVariable("x"),
				new LogarithmExpression(ConstantExpression.forValue(2),
					new SineExpression(
						new DivisionExpression(ConstantExpression.forValue(4.32), new EvaluableVariable("x"))))),
			new ExponentationExpression(ConstantExpression.forValue(393.123),
				new DivisionExpression(ConstantExpression.forValue(-1),
					new IfThenElseExpression(
						new ComparisonExpression(new EvaluableVariable("x"), ConstantExpression.forValue(0)),
						new NaturalLogarithmExpression(new SineExpression(ConstantExpression.forValue(1))),
						new ExponentialFunctionExpression(new EvaluableVariable("x"))))));
		assertThat(varVisitor.modifyRecursively(expression), is(correctModified));

		// ersetze aktuelle Expression mit replace (mit anderem Visitor)
		// equals zu erwarteten
		// weiterhin richtige traversierung

	}

	/**
	 * Tests if stopping and continuing traversal of inner Expressions works as expected.
	 *
	 */
	@Test
	public void modifyTraversal() {
		final EvaluableExpression expression = this.buildExpression();

		final TraversalTestRecursiveEvaluableExpressionVisitor traversalTestVisitor =
			new TraversalTestRecursiveEvaluableExpressionVisitor();
		traversalTestVisitor.visitRecursively(expression);
		assertThat(traversalTestVisitor.getVisitedCount(), is(17));
	}

	/**
	 * Implementation of {@link ModifyingEvaluableExpressionVisitor} used to test it.
	 * 
	 * @author Annika Berger
	 */
	private class TestModifyingExpressionVisitor extends ModifyingEvaluableExpressionVisitor {

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

		/*
		 * (non-Javadoc)
		 *
		 * @see de.uka.ipd.sdq.beagle.core.evaluableexpressions.util.ExpressionTreeWalker#
		 * atOther(de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpression)
		 */
		@Override
		protected void atOther(final EvaluableExpression expression) {
			fail("This method should not be called as all occuring expressions are overwritten.");
		}

		@Override
		protected void atExpression(final EvaluableExpression expression) {
			this.counterAt++;
			assertThat(this.counterAt, is(this.getVisitedCount()));
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
		protected void atSubtraction(final SubtractionExpression expression) {
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
		protected void afterSubtraction(final SubtractionExpression expression) {
			assertThat("Wrong Depth", this.getTraversalDepth(), is(0));
			assertThat("Wrong Count", this.getVisitedCount(), is(22));
		}
	}

	/**
	 * Implementation of {@link ModifyingEvaluableExpressionVisitor}, which replaces
	 * Divisions with its Dividend.
	 * 
	 * @author Annika Berger
	 */
	private class ReplaceDivisionVisitor extends ModifyingEvaluableExpressionVisitor {

		@Override
		protected void atDivision(final DivisionExpression expression) {
			final EvaluableExpression dividend = expression.getDividend();
			this.replaceCurrentExpressionWith(dividend);
			switch (dividend.toString()) {
				case "a":
					assertThat("Wrong Depth", this.getTraversalDepth(), is(1));
					assertThat("Wrong Count", this.getVisitedCount(), is(2));
					break;
				case "4.32":
					fail("Should not be visited anymore.");
					break;
				case "-1.0":
					assertThat("Wrong Depth", this.getTraversalDepth(), is(2));
					assertThat("Wrong Count", this.getVisitedCount(), is(5));
					break;

				default:
					fail(String.format("There must be no division with %s as dividend in the visited expression",
						dividend));
			}
		}

		@Override
		protected void afterVariable(final EvaluableVariable expression) {
			switch (expression.getName()) {
				case "a":
					assertThat(this.getTraversalDepth(), is(1));
					assertThat(this.getVisitedCount(), is(2));
					break;
				default:
					fail("there should not be a variable called different.");
			}
		}

		@Override
		protected void atVariable(final EvaluableVariable expression) {
			fail("Should not be called.");
		}

		@Override
		protected void atOther(final EvaluableExpression expression) {
			fail("Expression was in replaced part.");
		}
		
		@Override
		protected void atConstant(final ConstantExpression expression) {
			if (expression.getValue() == 393.123) {
				assertThat(this.getTraversalDepth(), is(2));
				assertThat(this.getVisitedCount(), is(4));
			} else {
				fail("Should not be called");
			}
		}

		@Override
		protected void atSubtraction(final SubtractionExpression expression) {
			assertThat(this.getTraversalDepth(), is(0));
			assertThat(this.getVisitedCount(), is(1));
		}

		@Override
		protected void atExponentation(final ExponentationExpression expression) {
			assertThat(this.getTraversalDepth(), is(1));
			assertThat(this.getVisitedCount(), is(3));
		}

		@Override
		protected void afterDivision(final DivisionExpression expression) {
			fail("Should not be called");
		}
	}

	/**
	 * Implementation of a {@link ModifyingEvaluableExpressionVisitor}, which renames all
	 * {@link EvaluableVariable} to {@code x}.
	 * 
	 * @author Annika Berger
	 */
	private class RenameVariablesVisitor extends ModifyingEvaluableExpressionVisitor {

		@Override
		protected void atVariable(final EvaluableVariable expression) {
			switch (expression.getName()) {
				case "a":
					break;
				case "c":
					break;
				case "z":
					break;
				case "g":
					break;
				default:
					fail("there should not be a variable called different.");
			}
			this.replaceCurrentExpressionWith(new EvaluableVariable("x"));
		}
	}

	/**
	 * Implementation of {@link RecursiveEvaluableExpressionVisitor} used to test the
	 * traversal methods in the visitor.
	 * 
	 * @author Annika Berger
	 */
	private class TraversalTestRecursiveEvaluableExpressionVisitor extends RecursiveEvaluableExpressionVisitor {

		@Override
		protected void atLogarithm(final LogarithmExpression expression) {
			assertThat(willTraverseInnerExpressions(), is(true));
			stopTraversingInnerExpressions();
			assertThat(willTraverseInnerExpressions(), is(false));
		}

		@Override
		protected void afterDivision(final DivisionExpression expression) {
			if (expression.getDividend().toString().equals("a")) {
				assertThat(willTraverseInnerExpressions(), is(false));
				this.startTraversingInnerExpressions();
				assertThat(willTraverseInnerExpressions(), is(true));
			}
		}

		@Override
		protected void atVariable(final EvaluableVariable expression) {
			if (expression.getName().equals("c")) {
				fail("Should not be visited.");
				assertThat(willTraverseInnerExpressions(), is(false));
			}

		}
	}

}
