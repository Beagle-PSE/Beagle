package de.uka.ipd.sdq.beagle.core.testutil;

import de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableVariable;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableVariableAssignment;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.util.RecursiveEvaluableExpressionVisitor;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.HashSet;
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
	private final EvaluableExpression expression;

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
		this.expression = expression;
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
		description.appendText("An expression producing equals values as ");
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
	protected boolean matchesSafely(final EvaluableExpression item) {
		final VariableFinder variableFinder = new VariableFinder();
		final Set<EvaluableVariable> variablesOfExpression = variableFinder.getVariables(this.expression);
		final Set<EvaluableVariable> variablesOfItem = variableFinder.getVariables(item);

		if (!variablesOfExpression.containsAll(variablesOfItem)) {
			variablesOfItem.removeAll(variablesOfExpression);
			this.errorMessage = String
				.format("The examined expression contains the variables <%s>, which are not expected", variablesOfItem);
			return false;
		}

		final int primeOne = 100;
		final int primeTwo = 100;
		final int amountOfCombinations = 100;
		final EvaluableVariable[] variables =
			variablesOfExpression.toArray(new EvaluableVariable[variablesOfExpression.size()]);
		for (int i = 0; i < amountOfCombinations; i++) {
			final EvaluableVariableAssignment assignment = new EvaluableVariableAssignment();
			for (int j = 0; j < variables.length; j++) {
				assignment.setValueFor(variables[j], ((i + j) * primeOne) % primeTwo);
			}
			final double valueOfItem = item.evaluate(assignment);
			final double valueOfExpression = item.evaluate(assignment);
			if (valueOfItem != valueOfExpression) {
				this.errorMessage = String.format("<%f> expected for <%s>, but was <%d>", valueOfExpression,
					assignment.toString(), valueOfItem);
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
		private final Set<EvaluableVariable> variables = new HashSet<>();

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
			this.variables.clear();
			this.visitRecursively(expression);
			return this.variables;
		}
	}

}
