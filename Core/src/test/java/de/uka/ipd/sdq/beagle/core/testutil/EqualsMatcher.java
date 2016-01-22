package de.uka.ipd.sdq.beagle.core.testutil;

import org.hamcrest.Description;
import org.hamcrest.DiagnosingMatcher;
import org.hamcrest.Matcher;

/**
 * Matcher that asserts that equals behaves correctly for {@code null}, same object and
 * new object.
 *
 * @author Annika Berger
 */
public final class EqualsMatcher extends DiagnosingMatcher<Object> {

	/**
	 * A matcher that matches if the examined object’s {@code equals} follows certain
	 * default properties. The following attributes are asserted:
	 * 
	 * <ul>
	 * 
	 * <li>{@code equals} returns {@code false} for {@code null}
	 * 
	 * <li>{@code equals} returns {@code true} for the same instance
	 * 
	 * <li>{@code equals} return {@code false} for an object of another class
	 *
	 * </ul>
	 * 
	 * @return a matcher, as described above.
	 */
	public static Matcher<Object> hasDefaultEqualsProperties() {
		return new EqualsMatcher();
	}

	@Override
	public void describeTo(final Description description) {
		description.appendText("equals to follow standard contract");

	}

	@Override
	protected boolean matches(final Object examined, final Description mismatchDescription) {
		if (examined.equals(null)) {
			mismatchDescription.appendText("the object was equal to null");
			return false;
		}
		if (!examined.equals(examined)) {
			mismatchDescription.appendText("the object was not equal to itself");
		}
		if (examined.equals(new Object())) {
			mismatchDescription.appendText("the object is equal to a new object");
			return false;
		}
		return true;
	}

}
