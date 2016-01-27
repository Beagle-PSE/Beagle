package de.uka.ipd.sdq.beagle.core.testutil.factories;

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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Factory for prepared Evaluable Expression instances to be used by tests.
 *
 * @author Joshua Gleitze
 * @author Ansgar Spiegler
 */
public class EvaluableExpressionFactory {

	/**
	 * Creates an new evaluable expression.
	 *
	 * @return An evaluable expression. You may not make any assumptions about it except
	 *         that consecutive calls will create equal expressions.
	 */
	public EvaluableExpression getOne() {
		return new EvaluableVariable("x");
	}

	/**
	 * Creates an array of newly initialised evaluable expressions.
	 *
	 * @return 6 newly initialised evaluable expressions, covering a lot of different
	 *         nesting cases.
	 */
	public EvaluableExpression[] getAll() {
		final EvaluableVariable varA = new EvaluableVariable("a");
		final EvaluableVariable varB = new EvaluableVariable("b");
		final EvaluableVariable varC = new EvaluableVariable("c");
		final EvaluableVariable varX = new EvaluableVariable("d");

		return new EvaluableExpression[] {
			varA,

			new ExponentationExpression(ConstantExpression.forValue(2),
				new SubtractionExpression(new EvaluableVariable("n"), ConstantExpression.forValue(1))),

			new MultiplicationExpression(new IfThenElseExpression(new ComparisonExpression(varA, varB), varA, varB),
				new DivisionExpression(new NaturalLogarithmExpression(varC),
					new NaturalLogarithmExpression(ConstantExpression.forValue(10))),
				varC, new ExponentialFunctionExpression(varA),
				new MultiplicationExpression(ConstantExpression.forValue(3), varA, varB),
				ConstantExpression.forValue(11)),

			new AdditionExpression(
				new MultiplicationExpression(varA, new SineExpression(new MultiplicationExpression(varB,
					ConstantExpression.forValue(2), ConstantExpression.forValue(Math.PI)))),

				new SineExpression(new AdditionExpression(varX, ConstantExpression.forValue(Math.PI / 2)))),

			new LogarithmExpression(varB, varX),

			ConstantExpression.forValue(2)
		};
	}

	/**
	 * Creates a set of newly initialised evaluable expressions.
	 *
	 * @return 6 newly initialised evaluable expressions, covering a lot of different
	 *         nesting cases.
	 */
	public Set<EvaluableExpression> getAllAsSet() {
		return new HashSet<>(Arrays.asList(this.getAll()));
	}
}
