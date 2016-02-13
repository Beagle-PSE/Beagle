package de.uka.ipd.sdq.beagle.core.testutil;

import de.uka.ipd.sdq.beagle.core.Blackboard;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

/**
 * Matcher asserting that two {@link Blackboard}s only contain equal seff elements.
 *
 * @author Christoph Michelbach
 * @author Joshua Gleitze
 */
public final class BlackboardSeffElementsMatcher extends TypeSafeDiagnosingMatcher<Blackboard> {

	/**
	 * The Blackboard to compare with.
	 */
	private final Blackboard comparisonBlackboard;

	/**
	 * Creates a matcher that will compare to {@code blackboard}.
	 *
	 * @param blackboard The blackboard to compare to.
	 */
	private BlackboardSeffElementsMatcher(final Blackboard blackboard) {
		this.comparisonBlackboard = blackboard;
	}

	/**
	 * Matcher asserting that the examined blackboard contains seff elements equal to the
	 * ones on {@code blackboard}.
	 *
	 * @param blackboard The blackboard to compare the examined object to.
	 * @return A matcher as described above.
	 */
	public static Matcher<Blackboard> equalToRegardingSeffElements(final Blackboard blackboard) {
		return new BlackboardSeffElementsMatcher(blackboard);
	}

	@Override
	public void describeTo(final Description description) {
		description.appendText("a Blackboard having content equal to ").appendValue(this.comparisonBlackboard);
	}

	@Override
	protected boolean matchesSafely(final Blackboard examined, final Description mismatchDescription) {
		if (!examined.getAllSeffBranches().equals(this.comparisonBlackboard.getAllSeffBranches())) {
			mismatchDescription.appendText("it has different seff branches");
			return false;
		}

		if (!examined.getAllSeffLoops().equals(this.comparisonBlackboard.getAllSeffLoops())) {
			mismatchDescription.appendText("it has different seff loops");
			return false;
		}

		if (!examined.getAllRdias().equals(this.comparisonBlackboard.getAllRdias())) {
			mismatchDescription.appendText("it has different rdias");
			return false;
		}

		if (!examined.getAllExternalCallParameters().equals(this.comparisonBlackboard.getAllExternalCallParameters())) {
			mismatchDescription.appendText("it has different external call parameters");
			return false;
		}

		if (!examined.getSeffBranchesToBeMeasured().equals(this.comparisonBlackboard.getSeffBranchesToBeMeasured())) {
			mismatchDescription.appendText("it has different seff branches to be measured");
			return false;
		}

		if (!examined.getSeffLoopsToBeMeasured().equals(this.comparisonBlackboard.getSeffLoopsToBeMeasured())) {
			mismatchDescription.appendText("it has different seff loops to be measured");
			return false;
		}

		if (!examined.getRdiasToBeMeasured().equals(this.comparisonBlackboard.getRdiasToBeMeasured())) {
			mismatchDescription.appendText("it has different rdias to be measured");
			return false;
		}

		if (!examined.getExternalCallParametersToBeMeasured()
			.equals(this.comparisonBlackboard.getExternalCallParametersToBeMeasured())) {
			mismatchDescription.appendText("it has different external call parameters to be measured");
			return false;
		}

		return true;
	}

}
