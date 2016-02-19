package de.uka.ipd.sdq.beagle.core.testutil;

import de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.util.RecursiveEvaluableExpressionVisitor;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

/**
 * Matcher that asserts that the length of the input {@link EvaluableExpression} is same
 * or less as the length of the other.
 *
 * <p>The length of an evaluable expression describes the amount of recursively contained
 * {@link EvaluableExpression}s.
 *
 * @author Annika Berger
 */
public final class EvaluableExpressionLengthMatcher extends TypeSafeDiagnosingMatcher<EvaluableExpression> {

	/**
	 * Maximum length the expression may have.
	 */
	private final int length;

	/**
	 * Builds a new {@link EvaluableExpressionLengthMatcher} with a specific maximum
	 * length.
	 *
	 * @param length which is the maximum the compared expression may have.
	 */
	private EvaluableExpressionLengthMatcher(final int length) {
		this.length = length;
	}

	/**
	 * A matcher that matches that the length of an {@link EvaluableExpression} is no
	 * longer than the length of the given {@link EvaluableExpression}.
	 *
	 * @param expression which is used to determine the maximum length
	 * @return a new matcher as described above.
	 */
	public static Matcher<EvaluableExpression> isNoLongerThan(final EvaluableExpression expression) {
		final LengthAnalyser analyser = new LengthAnalyser();
		return new EvaluableExpressionLengthMatcher(analyser.getLengthOf(expression));
	}

	@Override
	public void describeTo(final Description description) {
		description.appendText("an evaluable expression no longer than ").appendValue(this.length);
	}

	@Override
	protected boolean matchesSafely(final EvaluableExpression item, final Description mismatchDescription) {
		final LengthAnalyser analyser = new LengthAnalyser();
		final int itemLength = analyser.getLengthOf(item);
		if (itemLength > this.length) {
			mismatchDescription.appendText("length was ").appendValue(itemLength);
			return false;
		}
		return true;
	}

	/**
	 * Provides a method to get the length of an {@link EvaluableExpression}.
	 *
	 * @author Annika Bergers
	 */
	private static final class LengthAnalyser extends RecursiveEvaluableExpressionVisitor {

		/**
		 * Visits the {@link EvaluableExpression} and its contained expressions to get the
		 * length of the expression.
		 *
		 * @param expression whose length is wanted
		 * @return the length of the expression
		 */
		private int getLengthOf(final EvaluableExpression expression) {
			this.visitRecursively(expression);
			return this.getVisitedCount();
		}
	}

}
