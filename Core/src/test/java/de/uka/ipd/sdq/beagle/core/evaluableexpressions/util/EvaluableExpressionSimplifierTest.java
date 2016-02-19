package de.uka.ipd.sdq.beagle.core.evaluableexpressions.util;

import static de.uka.ipd.sdq.beagle.core.testutil.EvaluableExpressionLengthMatcher.isNoLongerThan;
import static de.uka.ipd.sdq.beagle.core.testutil.ExpressionEqualityMatcher.producingTheSameValuesAs;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import de.uka.ipd.sdq.beagle.core.evaluableexpressions.AdditionExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.ConstantExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableVariable;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.MultiplicationExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.SubtractionExpression;

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
	 * 
	 * <p>Asserts that length of simplified expression is no longer than the length of the
	 * initial expression and that the result of the expression still is the same.
	 */
	@Test
	public void simplify() {
		final EvaluableExpressionSimplifier simplifier = new EvaluableExpressionSimplifier();

		MultiSet<EvaluableExpression> summands = new HashMultiSet<>();
		summands.add(ConstantExpression.forValue(3));
		summands.add(ConstantExpression.forValue(4));
		summands.add(new EvaluableVariable("a"));
		EvaluableExpression expression = new AdditionExpression(summands);
		EvaluableExpression simplified = simplifier.simplify(expression);
		assertThat(simplified, isNoLongerThan(expression));
		assertThat(simplified, is(producingTheSameValuesAs(expression)));

		summands = new HashMultiSet<>();
		summands.add(ConstantExpression.forValue(19));
		summands.add(new SubtractionExpression(ConstantExpression.forValue(2), ConstantExpression.forValue(5)));
		summands.add(new SubtractionExpression(ConstantExpression.forValue(3), new EvaluableVariable("b")));
		expression = new AdditionExpression(summands);
		simplified = simplifier.simplify(expression);
		assertThat(simplified, isNoLongerThan(expression));
		assertThat(simplified, is(producingTheSameValuesAs(expression)));

		summands = new HashMultiSet<>();
		summands.add(ConstantExpression.forValue(19));
		summands.add(new MultiplicationExpression(new EvaluableVariable("c"), ConstantExpression.forValue(2)));
		summands.add(new AdditionExpression(ConstantExpression.forValue(2), ConstantExpression.forValue(5)));
		summands.add(new AdditionExpression(new EvaluableVariable("c"), new EvaluableVariable("b")));
		summands.add(new SubtractionExpression(new EvaluableVariable("d"), ConstantExpression.forValue(5)));
		summands.add(new SubtractionExpression(ConstantExpression.forValue(3), new EvaluableVariable("b")));
		summands.add(new EvaluableVariable("d"));
		expression = new AdditionExpression(summands);
		simplified = simplifier.simplify(expression);
		assertThat(simplified, isNoLongerThan(expression));
		assertThat(simplified, is(producingTheSameValuesAs(expression)));
		
		summands = new HashMultiSet<>();
		summands.add(ConstantExpression.forValue(19));
		summands.add(ConstantExpression.forValue(2));
		summands.add(ConstantExpression.forValue(-4.6));
		summands.add(new SubtractionExpression(new EvaluableVariable("x"), ConstantExpression.forValue(3)));
		summands.add(new SubtractionExpression(ConstantExpression.forValue(10.34), ConstantExpression.forValue(-4.6)));
		summands.add(new MultiplicationExpression(new EvaluableVariable("c"), ConstantExpression.forValue(2)));
		summands.add(new AdditionExpression(ConstantExpression.forValue(2), ConstantExpression.forValue(5)));
		summands.add(new AdditionExpression(new EvaluableVariable("c"), new EvaluableVariable("b")));
		summands.add(new SubtractionExpression(new EvaluableVariable("d"), ConstantExpression.forValue(5)));
		summands.add(new SubtractionExpression(ConstantExpression.forValue(3), new EvaluableVariable("b")));
		summands.add(new EvaluableVariable("d"));
		expression = new AdditionExpression(summands);
		simplified = simplifier.simplify(expression);
		assertThat(simplified, isNoLongerThan(expression));
		assertThat(simplified, is(producingTheSameValuesAs(expression)));

		expression = new MultiplicationExpression(ConstantExpression.forValue(3), new EvaluableVariable("a"));
		simplified = simplifier.simplify(expression);
		assertThat(simplified, isNoLongerThan(expression));
		assertThat(simplified, is(producingTheSameValuesAs(expression)));
	}

}
