package de.uka.ipd.sdq.beagle.core.testutil;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

/**
 * Hamcrest Matcher asserting that an exception of a certain type was thrown.
 *
 * @author Joshua Gleitze
 * @see #throwsException(Class)
 */
public final class ExceptionThrownMatcher extends TypeSafeDiagnosingMatcher<ThrowingMethod> {

	/**
	 * Creates a matcher matching if the examined method throws an exception that’s an
	 * instance of {@code exceptionClass}.
	 *
	 * <p>The matcher can be used like this:
	 *
	 * <pre>
	 *
	 * <code>
	 *
	 * assertThat("Error Message", () -> {
	 * 		myInstance.myMethod(myArgument1, myArgument2);
	 * }, throwsException(NullPointerException.class));
	 *
	 * </code>
	 *
	 * </pre>
	 *
	 * @param exceptionClass The expected class of the thrown exception. Should be as
	 *            specific as possible, must not be {@code null}.
	 * @return A matcher for throwing an exception of type {@code exceptionClass}.
	 */
	public static Matcher<ThrowingMethod> throwsException(final Class<? extends Exception> exceptionClass) {
		return new ExceptionThrownMatcher(exceptionClass);
	}

	/**
	 * The thrown exception’s expected class.
	 */
	private final Class<? extends Exception> expectedExceptionClass;

	/**
	 * Creates this matcher expecting an instance of {@code exceptionClass} to be thrown.
	 *
	 * @param exceptionClass The expected class of the thrown exception. Should be as
	 *            specific as possible, must not be {@code null}.
	 */
	private ExceptionThrownMatcher(final Class<? extends Exception> exceptionClass) {
		this.expectedExceptionClass = exceptionClass;
	}

	@Override
	public void describeTo(final Description description) {
		description.appendText("a ").appendValue(this.expectedExceptionClass.getSimpleName())
			.appendText(" to be thrown");
	}

	@Override
	protected boolean matchesSafely(final ThrowingMethod method, final Description mismatchDescription) {
		try {
			method.throwException();
			mismatchDescription.appendText("no exception was thrown at all");
			/*
			 * Catching a general Exception is both necessary and okay at this special
			 * point.
			 *
			 * CHECKSTYLE:OFF
			 */
		} catch (final Exception thrownException) {
			/*
			 * CHECKSTYLE:ON
			 */
			if (this.expectedExceptionClass.isInstance(thrownException)) {
				return true;
			}
			mismatchDescription.appendText("a ").appendValue(thrownException.getClass().getSimpleName())
				.appendText(" was thrown");
		}
		return false;
	}
}
