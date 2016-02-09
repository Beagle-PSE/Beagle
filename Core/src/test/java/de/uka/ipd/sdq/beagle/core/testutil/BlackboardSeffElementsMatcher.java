package de.uka.ipd.sdq.beagle.core.testutil;

import de.uka.ipd.sdq.beagle.core.Blackboard;

import org.eclipse.net4j.util.collection.Pair;
import org.hamcrest.Description;
import org.hamcrest.DiagnosingMatcher;
import org.hamcrest.Matcher;

/**
 * Matcher asserting that two {@link Blackboard}s only contain equal seff elements.
 *
 * @author Christoph Michelbach
 */
public final class BlackboardSeffElementsMatcher extends DiagnosingMatcher<Object> {

	/**
	 * Matcher asserting that two {@link Blackboard}s only contain equal seff elements.
	 *
	 *
	 * @return A matcher as described above.
	 */
	public static Matcher<Object> areEqualRegardingSeffElements() {
		return new BlackboardSeffElementsMatcher();
	}

	@Override
	public void describeTo(final Description description) {
		description.appendText("equals to follow standard contract");
	}

	@Override
	protected boolean matches(final Object blackboardsParam, final Description mismatchDescription) {
		if (blackboardsParam.equals(null)) {
			mismatchDescription.appendText("the object was equal to null");
			return false;
		}

		if (!(blackboardsParam instanceof Pair)) {
			mismatchDescription.appendText("the object was not a pair");
			return false;
		}

		@SuppressWarnings("unchecked")
		final Pair<Blackboard, Blackboard> blackboards = (Pair<Blackboard, Blackboard>) blackboardsParam;

		if (!(blackboards.getElement1() instanceof Blackboard)) {
			mismatchDescription.appendText("object 1 was not a blackboard");
			return false;
		}

		if (!(blackboards.getElement2() instanceof Blackboard)) {
			mismatchDescription.appendText("object 2 was not a blackboard");
			return false;
		}

		final Blackboard blackboard1 = (Blackboard) blackboards.getElement1();
		final Blackboard blackboard2 = (Blackboard) blackboards.getElement2();

		if (!blackboard1.getAllSeffBranches().equals(blackboard2.getAllSeffBranches())) {
			mismatchDescription.appendText("blackboards are not equal");
			return false;
		}

		if (!blackboard1.getAllSeffLoops().equals(blackboard2.getAllSeffLoops())) {
			mismatchDescription.appendText("blackboards are not equal");
			return false;
		}

		if (!blackboard1.getAllRdias().equals(blackboard2.getAllRdias())) {
			mismatchDescription.appendText("blackboards are not equal");
			return false;
		}

		if (!blackboard1.getAllExternalCallParameters().equals(blackboard2.getAllExternalCallParameters())) {
			mismatchDescription.appendText("blackboards are not equal");
			return false;
		}

		if (!blackboard1.getSeffBranchesToBeMeasured().equals(blackboard2.getSeffBranchesToBeMeasured())) {
			mismatchDescription.appendText("blackboards are not equal");
			return false;
		}

		if (!blackboard1.getSeffLoopsToBeMeasured().equals(blackboard2.getSeffLoopsToBeMeasured())) {
			mismatchDescription.appendText("blackboards are not equal");
			return false;
		}

		if (!blackboard1.getRdiasToBeMeasured().equals(blackboard2.getRdiasToBeMeasured())) {
			mismatchDescription.appendText("blackboards are not equal");
			return false;
		}

		if (!blackboard1.getExternalCallParametersToBeMeasured()
			.equals(blackboard2.getExternalCallParametersToBeMeasured())) {
			mismatchDescription.appendText("blackboards are not equal");
			return false;
		}

		return true;
	}
}
