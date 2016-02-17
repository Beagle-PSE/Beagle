package de.uka.ipd.sdq.beagle.core.evaluableexpressions.util;

import de.uka.ipd.sdq.beagle.core.evaluableexpressions.AdditionExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.ComparisonExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.ConstantExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.DivisionExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpressionVisitor;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableVariable;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.ExponentationExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.ExponentialFunctionExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.IfThenElseExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.LogarithmExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.MultiplicationExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.NaturalLogarithmExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.SineExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.SubtractionExpression;

/**
 * Abstract {@linkplain EvaluableExpressionVisitor} offering a convenient interface. All
 * {@code visit} methods redirect the call to {@link #visitOther(EvaluableExpression)}.
 * Implementors can thus override only certain {@code visit} overloads and handle all
 * other cases in {@link #visitOther(EvaluableExpression)}. If the implementor want
 * {@link #visitOther} to be called for expressions he overrode the {@code visit} method,
 * he can call the matching overload of {@code super.visit}.
 *
 * @author Joshua Gleitze
 */
public class AbstractEvaluableExpressionVisitor implements EvaluableExpressionVisitor {

	/**
	 * Called for any expression the implementor did not write {@code visit} (or called
	 * {@code super.visit} for.
	 *
	 * @param expression The currently visited expression.
	 */
	protected void visitOther(final EvaluableExpression expression) {

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpressionVisitor#visit(de
	 * .uka.ipd.sdq.beagle.core.evaluableexpressions.AdditionExpression)
	 */
	@Override
	public void visit(final AdditionExpression expression) {
		this.visitOther(expression);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpressionVisitor#visit(de
	 * .uka.ipd.sdq.beagle.core.evaluableexpressions.MultiplicationExpression)
	 */
	@Override
	public void visit(final MultiplicationExpression expression) {
		this.visitOther(expression);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpressionVisitor#visit(de
	 * .uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableVariable)
	 */
	@Override
	public void visit(final EvaluableVariable variable) {
		this.visitOther(variable);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpressionVisitor#visit(de
	 * .uka.ipd.sdq.beagle.core.evaluableexpressions.ComparisonExpression)
	 */
	@Override
	public void visit(final ComparisonExpression expression) {
		this.visitOther(expression);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpressionVisitor#visit(de
	 * .uka.ipd.sdq.beagle.core.evaluableexpressions.ConstantExpression)
	 */
	@Override
	public void visit(final ConstantExpression constant) {
		this.visitOther(constant);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpressionVisitor#visit(de
	 * .uka.ipd.sdq.beagle.core.evaluableexpressions.DivisionExpression)
	 */
	@Override
	public void visit(final DivisionExpression expression) {
		this.visitOther(expression);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpressionVisitor#visit(de
	 * .uka.ipd.sdq.beagle.core.evaluableexpressions.ExponentationExpression)
	 */
	@Override
	public void visit(final ExponentationExpression expression) {
		this.visitOther(expression);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpressionVisitor#visit(de
	 * .uka.ipd.sdq.beagle.core.evaluableexpressions.ExponentialFunctionExpression)
	 */
	@Override
	public void visit(final ExponentialFunctionExpression expression) {
		this.visitOther(expression);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpressionVisitor#visit(de
	 * .uka.ipd.sdq.beagle.core.evaluableexpressions.IfThenElseExpression)
	 */
	@Override
	public void visit(final IfThenElseExpression expression) {
		this.visitOther(expression);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpressionVisitor#visit(de
	 * .uka.ipd.sdq.beagle.core.evaluableexpressions.LogarithmExpression)
	 */
	@Override
	public void visit(final LogarithmExpression expression) {
		this.visitOther(expression);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpressionVisitor#visit(de
	 * .uka.ipd.sdq.beagle.core.evaluableexpressions.NaturalLogarithmExpression)
	 */
	@Override
	public void visit(final NaturalLogarithmExpression expression) {
		this.visitOther(expression);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpressionVisitor#visit(de
	 * .uka.ipd.sdq.beagle.core.evaluableexpressions.SineExpression)
	 */
	@Override
	public void visit(final SineExpression expression) {
		this.visitOther(expression);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpressionVisitor#visit(de
	 * .uka.ipd.sdq.beagle.core.evaluableexpressions.SubstractionExpression)
	 */
	@Override
	public void visit(final SubtractionExpression expression) {
		this.visitOther(expression);
	}
}
