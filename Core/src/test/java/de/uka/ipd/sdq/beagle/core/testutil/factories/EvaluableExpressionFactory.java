package de.uka.ipd.sdq.beagle.core.testutil.factories;

import de.uka.ipd.sdq.beagle.core.evaluableexpressions.AdditionExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.ComparisonExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.ConstantExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableVariable;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.ExponentationExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.IfThenElseExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.MultiplicationExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.NaturalLogarithmExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.SineExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.SubstractionExpression;

/**
 * Factory for prepared Evaluable Expression instances to be used by tests.
 *
 * @author Joshua Gleitze
 */
public class EvaluableExpressionFactory {

	/**
	 * Creates an new evaluable expression.
	 *
	 * @return An evaluable expression (you may not make any assumptions about).
	 */
	public EvaluableExpression getOne() {
		return ConstantExpression.forValue(2);
	}

	/**
	 * Creates an array of newly initialised evaluable expressions.
	 *
	 * @return 6 newly initialised evaluable expressions, covering a lot of different
	 *         nesting cases.
	 */
	public EvaluableExpression[] getAll() {
		// will be done right when the evaluable variable constructors are there.
		return new EvaluableExpression[] {new EvaluableVariable(), new ExponentationExpression(),
			new MultiplicationExpression(new IfThenElseExpression(), new ComparisonExpression(),
				new EvaluableVariable()),
			new AdditionExpression(new MultiplicationExpression(ConstantExpression.forValue(8), new EvaluableVariable(),
				new SineExpression()), new SubstractionExpression(), new NaturalLogarithmExpression()),
			new SineExpression(), ConstantExpression.forValue(2)};
	}
}
