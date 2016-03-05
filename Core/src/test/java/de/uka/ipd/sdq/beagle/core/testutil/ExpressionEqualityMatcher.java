package de.uka.ipd.sdq.beagle.core.testutil;

import de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableVariable;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableVariableAssignment;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.util.RecursiveEvaluableExpressionVisitor;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Matcher that asserts that the result of an {@link EvaluableExpression} with
 * {@link EvaluableVariable}s is the same than another one.
 *
 * @author Annika Berger
 */
public final class ExpressionEqualityMatcher extends TypeSafeMatcher<EvaluableExpression> {

	/**
	 * The {@link EvaluableExpression} to whose value the value of the other must be
	 * equal.
	 */
	private final EvaluableExpression reference;

	/**
	 * Filled if the matcher returns false to give a proper error message.
	 */
	private String errorMessage;

	/**
	 * Creates a new Matcher for the given expression.
	 *
	 * @param expression to which values should be compared
	 */
	private ExpressionEqualityMatcher(final EvaluableExpression expression) {
		this.reference = expression;
	}

	/**
	 * A matcher that asserts that the values of an {@link EvaluableExpression} is equal
	 * to the values of the given {@link EvaluableExpression}.
	 *
	 * @param expression to which values the values should be equal
	 * @return a new matcher as described above.
	 */
	public static Matcher<EvaluableExpression> producingTheSameValuesAs(final EvaluableExpression expression) {
		return new ExpressionEqualityMatcher(expression);
	}

	@Override
	public void describeTo(final Description description) {
		description.appendText("an expression producing equal values as ").appendValue(this.reference);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.hamcrest.TypeSafeMatcher#describeMismatchSafely(java.lang.Object,
	 * org.hamcrest.Description)
	 */
	@Override
	protected void describeMismatchSafely(final EvaluableExpression item, final Description mismatchDescription) {
		mismatchDescription.appendText(this.errorMessage);
	}

	@Override
	protected boolean matchesSafely(final EvaluableExpression examined) {
		final VariableFinder variableFinder = new VariableFinder();
		final Set<EvaluableVariable> variablesOfReference = variableFinder.getVariables(this.reference);
		final Set<EvaluableVariable> variablesOfExamined = variableFinder.getVariables(examined);

		if (!variablesOfReference.containsAll(variablesOfExamined)) {
			variablesOfExamined.removeAll(variablesOfReference);
			this.errorMessage = String.format(
				"the examined expression contains the variables <%s>, which are not expected", variablesOfExamined);
			return false;
		}

		/*
		 * The code below will generate pseudo-random numbers whose absolute value is
		 * equally distributed over the magnitudes to base 2 allowed in a double. The
		 * values will always be within the range defined by magnitudeVariation to keep
		 * the calculation error in check.
		 */

		// Make it reasonable unlikely to let the result “escape” to infinity or zero
		// during the calculation.
		final int maxExponent = Double.MAX_EXPONENT - 30;
		final int minExponent = Double.MIN_EXPONENT + 30;
		// This will always produce the same values!
		final Random numberSource = new Random(12345678);
		final int amountOfCombinations = 1000;
		final double epsilon = Math.pow(2, -15);
		// the variation we allow for the generated values. The values will not leave the
		// magnitude defined by this value.
		final double magnitudeVariation = 10;

		for (int i = 0; i < amountOfCombinations; i++) {
			final EvaluableVariableAssignment assignment = new EvaluableVariableAssignment();

			// Defines the generated values’ minimum magnitude
			final int minimumMagnitude =
				numberSource.nextInt((int) (maxExponent - 1 - magnitudeVariation + Math.abs(minExponent)))
					+ minExponent;
			final double minimumValue = Math.pow(2, minimumMagnitude);
			final double maximumValue = Math.pow(2, minimumMagnitude + magnitudeVariation);
			for (final EvaluableVariable variable : variablesOfReference) {
				final double sign = numberSource.nextBoolean() ? -1 : 1;
				final double value = sign * (maximumValue - minimumValue) * numberSource.nextDouble() + minimumValue;
				assignment.setValueFor(variable, value);
			}

			final double resultValue = examined.evaluate(assignment);
			final double expectedValue = this.reference.evaluate(assignment);

			if (Math.abs(resultValue - expectedValue) > epsilon * Math.abs(expectedValue)) {
				this.errorMessage =
					String.format("<%s> produces <%s> instead of <%s> for <%s> (allowed deviation: <%s>", examined,
						resultValue, expectedValue, assignment.toString(), epsilon * Math.abs(expectedValue));
				return false;
			}
		}

		return true;
	}

	/**
	 * Visits an {@link EvaluableExpression} to find all {@link EvaluableVariable}s.
	 *
	 * @author Annika Berger
	 */
	private static final class VariableFinder extends RecursiveEvaluableExpressionVisitor {

		/**
		 * Set containing all {@link EvaluableVariable}s of the
		 * {@link EvaluableExpression}.
		 */
		private Set<EvaluableVariable> variables = new HashSet<>();

		@Override
		protected void atVariable(final EvaluableVariable variable) {
			this.variables.add(variable);
		}

		/**
		 * Searches {@link EvaluableExpression} for all contained
		 * {@link EvaluableVariable}s and returns them as set.
		 *
		 * @param expression which should be searched trough.
		 * @return an set containing all {@link EvaluableVariable} in the
		 *         {@link EvaluableExpression}
		 */
		private Set<EvaluableVariable> getVariables(final EvaluableExpression expression) {
			this.variables = new HashSet<>();
			this.visitRecursively(expression);
			return this.variables;
		}
	}

}
