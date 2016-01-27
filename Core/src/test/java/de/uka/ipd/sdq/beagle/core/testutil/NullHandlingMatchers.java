package de.uka.ipd.sdq.beagle.core.testutil;

import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;

import org.apache.commons.lang3.ArrayUtils;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

/**
 * Matchers asserting that methods handle {@code null} correctly if it’s passed to them in
 * an array or collection argument.
 *
 * @author Joshua Gleitze
 */
public final class NullHandlingMatchers {

	/**
	 * Enclosing instance.
	 */
	private static final NullHandlingMatchers THIZ = new NullHandlingMatchers();

	/**
	 * A matcher matching if a method throws a NullPointerException.
	 */
	private static final Matcher<ThrowingMethod> THROWS_NPE = throwsException(NullPointerException.class);

	/**
	 * A matcher matching if a method throws a IllegalArgumentException.
	 */
	private static final Matcher<ThrowingMethod> THROWS_IAE = throwsException(IllegalArgumentException.class);

	/**
	 * This is a utility class and not meant to be instantiated.
	 */
	private NullHandlingMatchers() {
	}

	/**
	 * Creates a matcher that matches if the examined lambda does no accept {@code null}
	 * in its input values. The examined lambda takes an array as only argument and is
	 * expected to throw a {@link NullPointerException} if this array is {@code null} and
	 * an {@link IllegalArgumentException} if the array contains {@code null}.
	 *
	 * @param <CONSUMED_TYPE> type of the elements of the array the examined lambda takes
	 *            as its argument.
	 * @param testValues An array of valid values that can be passed to the examined
	 *            lambda without it throwing an exception.
	 * @return A matcher, as described above.
	 */
	public static <CONSUMED_TYPE> Matcher<Consumer<CONSUMED_TYPE[]>> notAcceptingNull(
		final CONSUMED_TYPE[] testValues) {
		final NotAcceptingNullInListMatcher<CONSUMED_TYPE> listMatcher =
			THIZ.new NotAcceptingNullInListMatcher<>(Arrays.asList(testValues));
		// hack to get an empty array of CONSUMED_TYPE without SurpressWarnings
		final CONSUMED_TYPE[] copyInstance = ArrayUtils.clone(testValues);
		ArrayUtils.removeElements(copyInstance, copyInstance);

		return new TypeSafeDiagnosingMatcher<Consumer<CONSUMED_TYPE[]>>() {

			@Override
			public void describeTo(final Description description) {
				listMatcher.describeTo(description);
			}

			@Override
			protected boolean matchesSafely(final Consumer<CONSUMED_TYPE[]> item,
				final Description mismatchDescription) {
				return listMatcher.matchesSafely((list) -> item.accept(list.toArray(copyInstance)),
					mismatchDescription);
			}
		};
	}

	/**
	 * Creates a matcher that matches if the examined lambda does no accept {@code null}
	 * in its input values. The examined lambda takes a collection as only argument and is
	 * expected to throw a {@link NullPointerException} if this collection is {@code null}
	 * and an {@link IllegalArgumentException} if the collection contains {@code null}.
	 *
	 * @param <CONSUMED_TYPE> type of the elements of the collection the examined lambda
	 *            takes as its argument.
	 * @param testValues A collection of valid values that can be passed to the examined
	 *            lambda without it throwing an exception.
	 * @return A matcher, as described above.
	 */
	public static <CONSUMED_TYPE> Matcher<Consumer<Collection<CONSUMED_TYPE>>> notAcceptingNull(
		final Collection<CONSUMED_TYPE> testValues) {
		return THIZ.new NotAcceptingNullInListMatcher<CONSUMED_TYPE>(new ArrayList<>(testValues));
	}

	/**
	 * Returns a copy of {@code list} that contains {@code null} at {@code index}.
	 *
	 * @param list A list.
	 * @param index The index to insert {@code null} at.
	 * @param <T> The type of the elements in {@code list}.
	 * @return For {@code list = [a(1), …, a(n)]}:
	 *         {@code [a(1), …, a(index - 1), null, a(index),
	 *         …, a(n)]}.
	 */
	private static <T> List<T> withNull(final List<T> list, final int index) {
		final List<T> result = new ArrayList<>(list);
		result.add(index, null);
		return result;
	}

	/**
	 * Returns a copy of {@code list} that contains {@code null} at its end.
	 *
	 * @param list A list.
	 * @param <T> The type of the elements in {@code list}.
	 * @return For {@code list = [a(1), …, a(n)]}: {@code [a(1), …, a(n), null]}.
	 */
	private static <T> List<T> withNull(final List<T> list) {
		final List<T> result = new ArrayList<>(list);
		result.add(null);
		return result;
	}

	/**
	 * A matcher that matches if the examined lambda does no accept {@code null} in its
	 * input values. The examined lambda takes a list as only argument and is expected to
	 * throw a {@link NullPointerException} if this list is {@code null} and a
	 * {@link IllegalArgumentException} if the list contains {@code null}.
	 *
	 * @author Joshua Gleitze
	 * @param <CONSUMED_TYPE> type of the elements of the array the examined lambda takes
	 *            as its argument.
	 */
	private final class NotAcceptingNullInListMatcher<CONSUMED_TYPE>
		extends TypeSafeDiagnosingMatcher<Consumer<Collection<CONSUMED_TYPE>>> {

		/**
		 * A list of values that are accepted by the examined lambda.
		 */
		private final List<CONSUMED_TYPE> testValues;

		/**
		 * Creates this matcher with values that can safely be passed to the examined
		 * lambda.
		 *
		 *
		 * @param testValues A list of valid values that can be passed to the examined
		 *            lambda without it throwing an exception.
		 */
		private NotAcceptingNullInListMatcher(final List<CONSUMED_TYPE> testValues) {
			this.testValues = testValues;
		}

		@Override
		public void describeTo(final Description description) {
			description.appendText("a method throwing a NullPointerException"
				+ " if an element in its argument or the argument itself is null");
		}

		@Override
		protected boolean matchesSafely(final Consumer<Collection<CONSUMED_TYPE>> consumer,
			final Description mismatchDescription) {
			ThrowingMethod nullApplied;

			// feed null
			nullApplied = () -> consumer.accept(null);
			if (!THROWS_NPE.matches(nullApplied)) {
				THROWS_NPE.describeMismatch(nullApplied, mismatchDescription);
				mismatchDescription.appendText(" when passing null as argument");
				return false;
			}

			// insert null at start, middle and end
			final List<List<CONSUMED_TYPE>> inputsWithNull = Arrays.asList(withNull(this.testValues, 0),
				withNull(this.testValues, this.testValues.size() / 2), withNull(this.testValues));

			// feed the lists containing null
			for (final List<CONSUMED_TYPE> testInputsWithNull : inputsWithNull) {
				nullApplied = () -> consumer.accept(testInputsWithNull);
				if (!THROWS_IAE.matches(nullApplied)) {
					THROWS_IAE.describeMismatch(nullApplied, mismatchDescription);
					mismatchDescription.appendText(" when passing null in the argument");
					return false;
				}
			}

			return true;
		}
	}
}
