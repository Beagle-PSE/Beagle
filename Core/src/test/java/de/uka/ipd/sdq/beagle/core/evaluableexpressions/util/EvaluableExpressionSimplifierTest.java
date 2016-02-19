package de.uka.ipd.sdq.beagle.core.evaluableexpressions.util;

import static de.uka.ipd.sdq.beagle.core.testutil.EvaluableExpressionLengthMatcher.isNoLongerThan;
import static org.hamcrest.MatcherAssert.assertThat;

import de.uka.ipd.sdq.beagle.core.evaluableexpressions.AdditionExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.ConstantExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableVariable;

import org.apache.commons.collections4.MultiSet;
import org.apache.commons.collections4.multiset.HashMultiSet;
import org.junit.Test;

/**
 * Tests {@link EvaluableExpressionSimplifier} and contains the test cases needed to check
 * all methods.
 *
 * @author Annika Berger
 */
public class EvaluableExpressionSimplifierTest {

	/**
	 * Test method for {@link EvaluableExpressionSimplifier#simplify(EvaluableExpression)}
	 * .
	 */
	@Test
	public void simplify() {
		final EvaluableExpressionSimplifier simplifier = new EvaluableExpressionSimplifier();

		final MultiSet<EvaluableExpression> summands = new HashMultiSet<>();
		summands.add(ConstantExpression.forValue(3));
		summands.add(ConstantExpression.forValue(4));
		summands.add(new EvaluableVariable("a"));
		final EvaluableExpression expression = new AdditionExpression(summands);
		final EvaluableExpression simplified = simplifier.simplify(expression);
		assertThat(simplified, isNoLongerThan(expression));
	}

}
