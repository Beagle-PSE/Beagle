package de.uka.ipd.sdq.beagle.core.evaluableexpressions.util;

import static org.hamcrest.CoreMatchers.is;
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
 * Tests {@link AbstractEvaluableExpressionVisitor} and contains the test cases needed.
 *
 * @author Annika Berger
 */
public class AbstractEvaluableExpressionVisitorTest {

	/**
	 * Implementation of a {@link AbstractEvaluableExpressionVisitor}, which only
	 * overrides the visitOther-Method.
	 */
	private final VisitExpressionVisitor visitor = new VisitExpressionVisitor();

	/**
	 * Test method for
	 * {@link AbstractEvaluableExpressionVisitor#visit(AdditionExpression)}.
	 */
	@Test
	public void visitAdditionExpression() {
		final AdditionExpression expression =
			new AdditionExpression(ConstantExpression.forValue(3), new EvaluableVariable("a"));
		this.visitor.visit(expression);
		assertThat(this.visitor.lastExpression, is(expression));
	}

	/**
	 * Test method for
	 * {@link AbstractEvaluableExpressionVisitor#visit(MultiplicationExpression)}.
	 */
	@Test
	public void visitMultiplicationExpression() {
		final MultiplicationExpression expression =
			new MultiplicationExpression(ConstantExpression.forValue(3), new EvaluableVariable("a"));
		this.visitor.visit(expression);
		assertThat(this.visitor.lastExpression, is(expression));
	}

	/**
	 * Test method for {@link AbstractEvaluableExpressionVisitor#visit(EvaluableVariable)}
	 * .
	 */
	@Test
	public void visitEvaluableVariable() {
		final EvaluableVariable expression = new EvaluableVariable("a");
		this.visitor.visit(expression);
		assertThat(this.visitor.lastExpression, is(expression));
	}

	/**
	 * Test method for
	 * {@link AbstractEvaluableExpressionVisitor#visit(ComparisonExpression)}.
	 */
	@Test
	public void visitComparisonExpression() {
		final ComparisonExpression expression =
			new ComparisonExpression(ConstantExpression.forValue(3), new EvaluableVariable("a"));
		this.visitor.visit(expression);
		assertThat(this.visitor.lastExpression, is(expression));
	}

	/**
	 * Test method for
	 * {@link AbstractEvaluableExpressionVisitor#visit(ConstantExpression)}.
	 */
	@Test
	public void visitConstantExpression() {
		final ConstantExpression expression = ConstantExpression.forValue(2.3);
		this.visitor.visit(expression);
		assertThat(this.visitor.lastExpression, is(expression));
	}

	/**
	 * Test method for
	 * {@link AbstractEvaluableExpressionVisitor#visit(DivisionExpression)}.
	 */
	@Test
	public void visitDivisionExpression() {
		final DivisionExpression expression =
			new DivisionExpression(ConstantExpression.forValue(3), new EvaluableVariable("a"));
		this.visitor.visit(expression);
		assertThat(this.visitor.lastExpression, is(expression));
	}

	/**
	 * Test method for
	 * {@link AbstractEvaluableExpressionVisitor#visit(ExponentationExpression)}.
	 */
	@Test
	public void visitExponentationExpression() {
		final ExponentationExpression expression =
			new ExponentationExpression(ConstantExpression.forValue(3), new EvaluableVariable("a"));
		this.visitor.visit(expression);
		assertThat(this.visitor.lastExpression, is(expression));
	}

	/**
	 * Test method for
	 * {@link AbstractEvaluableExpressionVisitor#visit(ExponentialFunctionExpression)}.
	 */
	@Test
	public void visitExponentialFunctionExpression() {
		final ExponentialFunctionExpression expression =
			new ExponentialFunctionExpression(ConstantExpression.forValue(3));
		this.visitor.visit(expression);
		assertThat(this.visitor.lastExpression, is(expression));
	}

	/**
	 * Test method for
	 * {@link AbstractEvaluableExpressionVisitor#visit(IfThenElseExpression)}.
	 */
	@Test
	public void visitIfThenElseExpression() {
		final IfThenElseExpression expression = new IfThenElseExpression(
			new ComparisonExpression(ConstantExpression.forValue(3.2), new EvaluableVariable("b")),
			ConstantExpression.forValue(3), ConstantExpression.forValue(3.2));
		this.visitor.visit(expression);
		assertThat(this.visitor.lastExpression, is(expression));
	}

	/**
	 * Test method for
	 * {@link AbstractEvaluableExpressionVisitor#visit(LogarithmExpression)}.
	 */
	@Test
	public void visitLogarithmExpression() {
		final LogarithmExpression expression =
			new LogarithmExpression(ConstantExpression.forValue(3), new EvaluableVariable("a"));
		this.visitor.visit(expression);
		assertThat(this.visitor.lastExpression, is(expression));
	}

	/**
	 * Test method for
	 * {@link AbstractEvaluableExpressionVisitor#visit(NaturalLogarithmExpression)}.
	 */
	@Test
	public void visitNaturalLogarithmExpression() {
		final NaturalLogarithmExpression expression = new NaturalLogarithmExpression(ConstantExpression.forValue(3));
		this.visitor.visit(expression);
		assertThat(this.visitor.lastExpression, is(expression));
	}

	/**
	 * Test method for {@link AbstractEvaluableExpressionVisitor#visit(SineExpression)}.
	 */
	@Test
	public void visitSineExpression() {
		final SineExpression expression = new SineExpression(ConstantExpression.forValue(3));
		this.visitor.visit(expression);
		assertThat(this.visitor.lastExpression, is(expression));
	}

	/**
	 * Test method for
	 * {@link AbstractEvaluableExpressionVisitor#visit(SubtractionExpression)}.
	 */
	@Test
	public void visitSubtractionExpression() {
		final SubtractionExpression expression =
			new SubtractionExpression(ConstantExpression.forValue(3), new EvaluableVariable("a"));
		this.visitor.visit(expression);
		assertThat(this.visitor.lastExpression, is(expression));
	}

	/**
	 * Class used to test if the visit methods of
	 * {@link AbstractEvaluableExpressionVisitor} work as demanded.
	 * 
	 * @author Annika Berger
	 */
	private class VisitExpressionVisitor extends AbstractEvaluableExpressionVisitor {

		/**
		 * Last {@link EvaluableExpression} for which visitOther was called.
		 */
		private EvaluableExpression lastExpression;

		@Override
		protected void visitOther(final EvaluableExpression expression) {
			this.lastExpression = expression;
		}

	}

}
