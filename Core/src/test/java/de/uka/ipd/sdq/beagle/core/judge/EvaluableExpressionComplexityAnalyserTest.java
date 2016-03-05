package de.uka.ipd.sdq.beagle.core.judge;

import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertThat;

import de.uka.ipd.sdq.beagle.core.evaluableexpressions.AdditionExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.ComparisonExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.ConstantExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.DivisionExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableVariable;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.ExponentationExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.ExponentialFunctionExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.IfThenElseExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.LogarithmExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.MultiplicationExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.NaturalLogarithmExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.SineExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.SubtractionExpression;

import org.junit.Test;

/**
 * Tests {@link EvaluableExpressionComplexityAnalyser} and contains the test cases needed
 * to check all methods.
 *
 * @author Annika Berger
 */
public class EvaluableExpressionComplexityAnalyserTest {

	/**
	 * Asserts that an Expression, which is simpler to read for humans is graded better
	 * than a more complex one.
	 *
	 */
	@Test
	public void humanComplexity() {
		final EvaluableExpressionComplexityAnalyser analyser = new EvaluableExpressionComplexityAnalyser();

		EvaluableExpression simple =
			new AdditionExpression(ConstantExpression.forValue(3.2), new EvaluableVariable("b"));
		EvaluableExpression complex =
			new AdditionExpression(new AdditionExpression(ConstantExpression.forValue(2.2), new EvaluableVariable("b")),
				ConstantExpression.forValue(1));
		analyser.determineComplexity(simple);
		double simpleValue = analyser.getHumanComprehensibilityComplexitySum();
		analyser.determineComplexity(complex);
		double complexValue = analyser.getHumanComprehensibilityComplexitySum();
		assertThat(simpleValue, is(lessThan(complexValue)));

		simple = new DivisionExpression(ConstantExpression.forValue(3),
			new LogarithmExpression(ConstantExpression.forValue(2), new SineExpression(
				new MultiplicationExpression(ConstantExpression.forValue(2.2), new EvaluableVariable("a")))));
		complex = new DivisionExpression(
			new MultiplicationExpression(ConstantExpression.forValue(3), ConstantExpression.forValue(1)),
			new LogarithmExpression(ConstantExpression.forValue(2), new SineExpression(
				new MultiplicationExpression(ConstantExpression.forValue(2.2), new EvaluableVariable("a")))));
		analyser.determineComplexity(simple);
		simpleValue = analyser.getHumanComprehensibilityComplexitySum();
		analyser.determineComplexity(complex);
		complexValue = analyser.getHumanComprehensibilityComplexitySum();
		assertThat(simpleValue, is(lessThan(complexValue)));

		analyser.determineComplexity(complex);
		final double complexValue2 = analyser.getHumanComprehensibilityComplexitySum();
		assertThat(complexValue, is(complexValue2));

		simple = new SubtractionExpression(
			new DivisionExpression(new EvaluableVariable("a"),
				new LogarithmExpression(ConstantExpression.forValue(2),
					new SineExpression(
						new DivisionExpression(ConstantExpression.forValue(4.32), new EvaluableVariable("c"))))),
			new ExponentationExpression(ConstantExpression.forValue(393.123),
				new DivisionExpression(ConstantExpression.forValue(-1),
					new IfThenElseExpression(
						new ComparisonExpression(new EvaluableVariable("z"), ConstantExpression.forValue(0)),
						new NaturalLogarithmExpression(new SineExpression(ConstantExpression.forValue(1))),
						new ExponentialFunctionExpression(new EvaluableVariable("g"))))));
		complex =
			new AdditionExpression(ConstantExpression.forValue(-1.0), ConstantExpression.forValue(1.0),
				new SubtractionExpression(
					new DivisionExpression(new EvaluableVariable("a"),
						new LogarithmExpression(ConstantExpression.forValue(2),
							new SineExpression(new DivisionExpression(ConstantExpression.forValue(4.32),
								new EvaluableVariable("c"))))),
				new ExponentationExpression(ConstantExpression.forValue(393.123),
					new DivisionExpression(ConstantExpression.forValue(-1),
						new IfThenElseExpression(
							new ComparisonExpression(new EvaluableVariable("z"), ConstantExpression.forValue(0)),
							new NaturalLogarithmExpression(new SineExpression(ConstantExpression.forValue(1))),
							new ExponentialFunctionExpression(new EvaluableVariable("g")))))));
		analyser.determineComplexity(simple);
		simpleValue = analyser.getHumanComprehensibilityComplexitySum();
		analyser.determineComplexity(complex);
		complexValue = analyser.getHumanComprehensibilityComplexitySum();
		assertThat(simpleValue, is(lessThan(complexValue)));
		
		final EvaluableExpressionComplexityAnalyser newAnalyser = new EvaluableExpressionComplexityAnalyser();
		assertThat(() -> newAnalyser.getHumanComprehensibilityComplexitySum(), throwsException(IllegalStateException.class));
	}

	/**
	 * Asserts that an Expression, which is simpler to evaluate for computers is graded
	 * better than a more complex one.
	 *
	 */
	@Test
	public void computerComplexity() {
		final EvaluableExpressionComplexityAnalyser analyser = new EvaluableExpressionComplexityAnalyser();

		EvaluableExpression simple =
			new AdditionExpression(ConstantExpression.forValue(3.2), new EvaluableVariable("b"));
		EvaluableExpression complex =
			new AdditionExpression(new AdditionExpression(ConstantExpression.forValue(2.2), new EvaluableVariable("b")),
				ConstantExpression.forValue(1));
		analyser.determineComplexity(simple);
		double simpleValue = analyser.getComputationalComplexitySum();
		analyser.determineComplexity(complex);
		double complexValue = analyser.getComputationalComplexitySum();
		assertThat(simpleValue, is(lessThan(complexValue)));

		simple = new DivisionExpression(ConstantExpression.forValue(3),
			new LogarithmExpression(ConstantExpression.forValue(2), new SineExpression(
				new MultiplicationExpression(ConstantExpression.forValue(2.2), new EvaluableVariable("a")))));
		complex = new DivisionExpression(
			new MultiplicationExpression(ConstantExpression.forValue(3), ConstantExpression.forValue(1)),
			new LogarithmExpression(ConstantExpression.forValue(2), new SineExpression(
				new MultiplicationExpression(ConstantExpression.forValue(2.2), new EvaluableVariable("a")))));
		analyser.determineComplexity(simple);
		simpleValue = analyser.getComputationalComplexitySum();
		analyser.determineComplexity(complex);
		complexValue = analyser.getComputationalComplexitySum();
		assertThat(simpleValue, is(lessThan(complexValue)));

		analyser.determineComplexity(complex);
		final double complexValue2 = analyser.getComputationalComplexitySum();
		assertThat(complexValue, is(complexValue2));
		
		complex =
			new AdditionExpression(ConstantExpression.forValue(-1.0), ConstantExpression.forValue(1.0),
				new SubtractionExpression(
					new DivisionExpression(new EvaluableVariable("a"),
						new LogarithmExpression(ConstantExpression.forValue(2),
							new SineExpression(new DivisionExpression(ConstantExpression.forValue(4.32),
								new EvaluableVariable("c"))))),
				new ExponentationExpression(ConstantExpression.forValue(393.123),
					new DivisionExpression(ConstantExpression.forValue(-1),
						new IfThenElseExpression(
							new ComparisonExpression(new EvaluableVariable("z"), ConstantExpression.forValue(0)),
							new NaturalLogarithmExpression(new SineExpression(ConstantExpression.forValue(1))),
							new ExponentialFunctionExpression(new EvaluableVariable("g")))))));
		analyser.determineComplexity(simple);
		simpleValue = analyser.getComputationalComplexitySum();
		analyser.determineComplexity(complex);
		complexValue = analyser.getComputationalComplexitySum();
		assertThat(simpleValue, is(lessThan(complexValue)));
		
		final EvaluableExpressionComplexityAnalyser newAnalyser = new EvaluableExpressionComplexityAnalyser();
		assertThat(() -> newAnalyser.getComputationalComplexitySum(), throwsException(IllegalStateException.class));
	}

}
