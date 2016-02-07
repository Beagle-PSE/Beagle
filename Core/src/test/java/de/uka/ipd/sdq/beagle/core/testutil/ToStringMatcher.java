package de.uka.ipd.sdq.beagle.core.testutil;

import org.hamcrest.Description;
import org.hamcrest.DiagnosingMatcher;
import org.hamcrest.Matcher;

/**
 * Matcher matching if the examined object does not return the default value for
 * {@link Object#equals(Object)}.
 *
 * @author Joshua Gleitze
 */
public class ToStringMatcher extends DiagnosingMatcher<Object> {

	/**
	 * Matcher matching if the examined object has overridden its
	 * {@link Object#toString()} method.
	 *
	 * @return A matcher, as described above.
	 */
	public static Matcher<Object> hasOverriddenToString() {
		return new ToStringMatcher();
	}

	@Override
	public void describeTo(final Description description) {
		description.appendText("an object not returning the default value for toString");
	}

	@Override
	protected boolean matches(final Object item, final Description mismatchDescription) {
		if (item.toString().equals(item.getClass().getName() + '@' + Integer.toHexString(item.hashCode()))) {
			mismatchDescription.appendValue(item.getClass().getSimpleName())
				.appendText("has the default implementation of toString");
			return false;
		}
		return true;
	}

}
