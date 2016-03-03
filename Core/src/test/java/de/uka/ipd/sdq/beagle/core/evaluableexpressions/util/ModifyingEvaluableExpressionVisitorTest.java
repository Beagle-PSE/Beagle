package de.uka.ipd.sdq.beagle.core.evaluableexpressions.util;

import de.uka.ipd.sdq.beagle.core.evaluableexpressions.ComparisonExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.ConstantExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.DivisionExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableVariable;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.ExponentationExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.ExponentialFunctionExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.IfThenElseExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.LogarithmExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.NaturalLogarithmExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.SineExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.SubtractionExpression;

import org.junit.Test;

/**
 * Tests {@link ModifyingEvaluableExpressionVisitor} and contains all needed test cases.
 * 
 * @author Annika Berger
 */
public class ModifyingEvaluableExpressionVisitorTest {

	/**
	 * Builds an {@link EvaluableExpression} which can be used in the other test methods.
	 *
	 * @return the {@link EvaluableExpression}.
	 */
	private EvaluableExpression buildExpression() {
		final EvaluableExpression expression = new SubtractionExpression(
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
		return expression;
	}

	/**
	 * Tests if {@link ModifyingEvaluableExpressionVisitor} works as expected.
	 */
	@Test
	public void mainWork() {
		final EvaluableExpression expression = this.buildExpression();
		final TestModifyingExpressionVisitor visitor = new TestModifyingExpressionVisitor();
		visitor.modifyRecursively(expression);
		
	}

	/**
	 * Tests if stopping and continuing traversal of inner Expressions works as expected.
	 *
	 */
	@Test
	public void modifyTraversal() {
		final ModifyTraversalVisitor visitor = new ModifyTraversalVisitor();
		visitor.getVisitedCount();
	}

	/**
	 * Implementation of {@link ModifyingEvaluableExpressionVisitor} used to test it.
	 * 
	 * @author Annika Berger
	 */
	private class TestModifyingExpressionVisitor extends ModifyingEvaluableExpressionVisitor {

	}

	/**
	 * Implementation of {@link ModifyingEvaluableExpressionVisitor} used to test if
	 * modifying traversal of inner expression works as expected.
	 * 
	 * @author Annika Berger
	 */
	private class ModifyTraversalVisitor extends ModifyingEvaluableExpressionVisitor {

	}

}
