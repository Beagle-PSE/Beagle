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
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.SubstractionExpression;

import java.util.Iterator;

/**
 * Visitor of {@link EvaluableExpression EvaluableExpressions}, recursively visiting all
 * their inner expressions.
 *
 * <p>This class offers multiple hooks for implementors. All hooks are offered for each
 * implementation of {@linkplain EvaluableExpression} as well as for a general
 * {@link EvaluableExpression}, called for all implementations. The general hook will
 * always be called before the specific one. Implementors do not have to call the
 * {@code super} method on hooks. The following hooks are offered:
 *
 * <ul>
 *
 * <li>{@code at}: called when visiting an expression, before visiting its inner
 * expressions.
 *
 * <li>{@code after}: called when leaving an expression, after visiting its inner
 * expressions.
 *
 * </ul>
 *
 * <p>This visitor will start at an expression and call the general {@code at} hook and
 * the specific {@code at} hook. Afterwards, it will recursively visit all inner
 * expressions and then call the general {@code after} and the specific {@code after}
 * hook. It thus realises a depth-first traversal of the tree formed by each expression.
 * If {@link #stopTraversal()} is called, the traversal will no longer visit inner
 * expressions. Instead, it will go “up” the tree again, whilst calling the {@code after}
 * hooks, until it reached the initial expression and then terminate.
 *
 * <p>This visitor is not thread safe. It may only be used to traverse one expression at a
 * time, meaning that its results are undefined if
 * {@link #visitRecursively(EvaluableExpression)} is called when
 * {@link #getTraversalDepth()} does not return {@code -1}. There is however no limitation
 * on how many expression trees can be visited by an instance of this class.
 *
 * @author Joshua Gleitze
 */
public abstract class RecursiveEvaluableExpressionVisitor implements EvaluableExpressionVisitor {

	/**
	 * As long as this is true, we’ll traverse further. We’ll immediately stop to traverse
	 * deeper as soon as this is false.
	 */
	private boolean doTraverse = true;

	/**
	 * How deeply we have the traversed so far. Will be 0 at the root.
	 */
	private int depth;

	/**
	 * How many expressions we’ve seen so far. Will be 1 at the root.
	 */
	private int count;

	/**
	 * Starts visiting {@code expression} recursively.
	 *
	 * @param expression The expression that forms the root of tree this visitor shall
	 *            traverse.
	 */
	public void visitRecursively(final EvaluableExpression expression) {
		this.depth = -1;
		this.count = 0;
		this.doTraverse = true;
		expression.receive(this);
	}

	/**
	 * Private hook called when starting to visit an expression.
	 *
	 * @param expression The momentary visited expression.
	 */
	private void atExpressionPrivate(final EvaluableExpression expression) {
		this.depth++;
		this.count++;
		this.atExpression(expression);
	}

	/**
	 * Called when leaving any {@link EvaluableExpression}. Will be called after its inner
	 * expressions were visited and before it specific {@code after*} hook will be called.
	 *
	 * @param expression The momentary visited expression.
	 */
	private void afterExpressionPrivate(final EvaluableExpression expression) {
		this.afterExpression(expression);
		this.depth--;
	}

	/**
	 * Stops visiting of inner expressions. As soon as this method is called, this visitor
	 * will no longer visit inner expressions. This means that only {@code after} hooks of
	 * already visited expressions will be called until the root is reached. This setting
	 * persists only until the next call of {@link #visitRecursively(EvaluableExpression)}
	 * .
	 */
	protected void stopTraversal() {
		this.doTraverse = false;
	}

	/**
	 * Continues visiting of inner expressions after it has been stopped through
	 * {@link #stopTraversal()}. This returns to the visitor’s default behaviour.
	 */
	protected void continueTraversal() {
		this.doTraverse = true;
	}

	/**
	 * Queries whether the visitor will visit inner expressions. Will return {@code true}
	 * unless {@link #stopTraversal()} is called. The value will be reset when calling
	 * {@link #visitRecursively(EvaluableExpression)}.
	 *
	 * @return Whether inner expressions will be examined for the momentary tree.
	 */
	protected boolean willTraverse() {
		return this.doTraverse;
	}

	/**
	 * Queries how many expressions have been visited during the momentary traversal. This
	 * value is only reset when starting a new visit a new expression, it can thus be used
	 * to determine how many expressions were visited after a traversal.
	 *
	 * @return The amount of called general {@code at} hooks since the last call of
	 *         {@link #atFirstExpression}.
	 */
	protected int getVisitedCount() {
		return this.count;
	}

	/**
	 * Queries how “deep” the currently visited expression is in the visited tree.
	 *
	 * @return how many {@code at} hooks have been called - how many {@code after} hooks
	 *         have been called - 1. Will be {@code 0} at the root expression and
	 *         {@code -1} at the before and after a traversal.
	 */
	protected int getTraversalDepth() {
		return this.depth;
	}

	/**
	 * Called when visiting any {@link EvaluableExpression}. Will be called before its
	 * inner expressions will be visited and before it specific {@code at*} hook will be
	 * called.
	 *
	 * @param expression The momentary visited expression.
	 */
	protected void atExpression(final EvaluableExpression expression) {
		// may be implemented by implementor.
	}

	/**
	 * Called when visiting an expression and the amount of ever called {@code after}
	 * hooks equals the amount of called {@code at} hooks. {@code expression} can thus be
	 * regarded as the “root” of the momentary traversed expression tree. This hook will
	 * be called before the general and specific hook will be called for this expression.
	 *
	 * @param expression The first expression of the expression tree that will now be
	 *            traversed.
	 */
	protected void atFirstExpression(final EvaluableExpression expression) {
		// may be implemented by implementor.
	}

	/**
	 * Called when visiting an {@link AdditionExpression}, before its inner expressions
	 * will be visited.
	 *
	 * @param expression The momentary visited expression.
	 */
	protected void atAddition(final AdditionExpression expression) {
		// may be implemented by implementor.
	}

	/**
	 * Called when visiting a {@link MultiplicationExpression}, before its inner
	 * expressions will be visited.
	 *
	 * @param expression The momentary visited expression.
	 */
	protected void atMultiplication(final MultiplicationExpression expression) {
		// may be implemented by implementor.
	}

	/**
	 * Called when visiting an {@link EvaluableVariable}, before its inner expressions
	 * will be visited.
	 *
	 * @param expression The momentary visited expression.
	 */
	protected void atVariable(final EvaluableVariable expression) {
		// may be implemented by implementor.
	}

	/**
	 * Called when visiting a {@link ComparisonExpression}, before its inner expressions
	 * will be visited.
	 *
	 * @param expression The momentary visited expression.
	 */
	protected void atComparison(final ComparisonExpression expression) {
		// may be implemented by implementor.
	}

	/**
	 * Called when visiting a {@link ConstantExpression}, before its inner expressions
	 * will be visited.
	 *
	 * @param expression The momentary visited expression.
	 */
	protected void atConstant(final ConstantExpression expression) {
		// may be implemented by implementor.
	}

	/**
	 * Called when visiting a {@link DivisionExpression}, before its inner expressions
	 * will be visited.
	 *
	 * @param expression The momentary visited expression.
	 */
	protected void atDivision(final DivisionExpression expression) {
		// may be implemented by implementor.
	}

	/**
	 * Called when visiting an {@link ExponentationExpression}, before its inner
	 * expressions will be visited.
	 *
	 * @param expression The momentary visited expression.
	 */
	protected void atExponentation(final ExponentationExpression expression) {
		// may be implemented by implementor.
	}

	/**
	 * Called when visiting an {@link ExponentialFunctionExpression}, before its inner
	 * expressions will be visited.
	 *
	 * @param expression The momentary visited expression.
	 */
	protected void atExponentialFunction(final ExponentialFunctionExpression expression) {
		// may be implemented by implementor.
	}

	/**
	 * Called when visiting an {@link IfThenElseExpression}, before its inner expressions
	 * will be visited.
	 *
	 * @param expression The momentary visited expression.
	 */
	protected void atIfThenElse(final IfThenElseExpression expression) {
		// may be implemented by implementor.
	}

	/**
	 * Called when visiting a {@link LogarithmExpression}, before its inner expressions
	 * will be visited.
	 *
	 * @param expression The momentary visited expression.
	 */
	protected void atLogarithm(final LogarithmExpression expression) {
		// may be implemented by implementor.
	}

	/**
	 * Called when visiting a {@link NaturalLogarithmExpression}, before its inner
	 * expressions will be visited.
	 *
	 * @param expression The momentary visited expression.
	 */
	protected void atNaturalLogarithm(final NaturalLogarithmExpression expression) {
		// may be implemented by implementor.
	}

	/**
	 * Called when visiting a {@link SineExpression}, before its inner expressions will be
	 * visited.
	 *
	 * @param expression The momentary visited expression.
	 */
	protected void atSine(final SineExpression expression) {
		// may be implemented by implementor.
	}

	/**
	 * Called when visiting a {@link SubstractionExpression}, before its inner expressions
	 * will be visited.
	 *
	 * @param expression The momentary visited expression.
	 */
	protected void atSubstraction(final SubstractionExpression expression) {
		// may be implemented by implementor.
	}

	/**
	 * Called when leaving any {@link EvaluableExpression}. Will be called after its inner
	 * expressions were visited and before it specific {@code after*} hook will be called.
	 *
	 * @param expression The momentary visited expression.
	 */
	protected void afterExpression(final EvaluableExpression expression) {
		// may be implemented by implementor.
	}

	/**
	 * Called when leaving an expression and the (amount of ever called {@code after} - 1)
	 * hooks equals the amount of called {@code at} hooks. {@code expression} can thus be
	 * regarded as the “root” of the momentary traversed expression tree. This hook will
	 * be called after the general and specific hook were called for this expression.
	 *
	 * @param expression The first expression of the expression tree that was traversed.
	 */
	protected void afterFirstExpression(final EvaluableExpression expression) {
		// may be implemented by implementor.
	}

	/**
	 * Called when leaving an {@link AdditionExpression}, after its inner expressions were
	 * visited.
	 *
	 * @param expression The momentary visited expression.
	 */
	protected void afterAddition(final AdditionExpression expression) {
		// may be implemented by implementor.
	}

	/**
	 * Called when leaving a {@link MultiplicationExpression}, after its inner expressions
	 * were visited.
	 *
	 * @param expression The momentary visited expression.
	 */
	protected void afterMultiplication(final MultiplicationExpression expression) {
		// may be implemented by implementor.
	}

	/**
	 * Called when leaving an {@link EvaluableVariable}, after its inner expressions were
	 * visited.
	 *
	 * @param expression The momentary visited expression.
	 */
	protected void afterVariable(final EvaluableVariable expression) {
		// may be implemented by implementor.
	}

	/**
	 * Called when leaving a {@link ComparisonExpression}, after its inner expressions
	 * were visited.
	 *
	 * @param expression The momentary visited expression.
	 */
	protected void afterComparison(final ComparisonExpression expression) {
		// may be implemented by implementor.
	}

	/**
	 * Called when leaving a {@link ConstantExpression}, after its inner expressions were
	 * visited.
	 *
	 * @param expression The momentary visited expression.
	 */
	protected void afterConstant(final ConstantExpression expression) {
		// may be implemented by implementor.
	}

	/**
	 * Called when leaving a {@link DivisionExpression}, after its inner expressions were
	 * visited.
	 *
	 * @param expression The momentary visited expression.
	 */
	protected void afterDivision(final DivisionExpression expression) {
		// may be implemented by implementor.
	}

	/**
	 * Called when leaving an {@link ExponentationExpression}, after its inner expressions
	 * were visited.
	 *
	 * @param expression The momentary visited expression.
	 */
	protected void afterExponentation(final ExponentationExpression expression) {
		// may be implemented by implementor.
	}

	/**
	 * Called when leaving an {@link ExponentialFunctionExpression}, after its inner
	 * expressions were visited.
	 *
	 * @param expression The momentary visited expression.
	 */
	protected void afterExponentialFunction(final ExponentialFunctionExpression expression) {
		// may be implemented by implementor.
	}

	/**
	 * Called when leaving an {@link IfThenElseExpression}, after its inner expressions
	 * were visited.
	 *
	 * @param expression The momentary visited expression.
	 */
	protected void afterIfThenElse(final IfThenElseExpression expression) {
		// may be implemented by implementor.
	}

	/**
	 * Called when leaving a {@link LogarithmExpression}, after its inner expressions were
	 * visited.
	 *
	 * @param expression The momentary visited expression.
	 */
	protected void afterLogarithm(final LogarithmExpression expression) {
		// may be implemented by implementor.
	}

	/**
	 * Called when leaving a {@link NaturalLogarithmExpression}, after its inner
	 * expressions were visited.
	 *
	 * @param expression The momentary visited expression.
	 */
	protected void afterNaturalLogarithm(final NaturalLogarithmExpression expression) {
		// may be implemented by implementor.
	}

	/**
	 * Called when leaving a {@link SineExpression}, after its inner expressions will be
	 * visited.
	 *
	 * @param expression The momentary visited expression.
	 */
	protected void afterSine(final SineExpression expression) {
		// may be implemented by implementor.
	}

	/**
	 * Called when leaving a {@link SubstractionExpression}, after its inner expressions
	 * were visited.
	 *
	 * @param expression The momentary visited expression.
	 */
	protected void afterSubstraction(final SubstractionExpression expression) {
		// may be implemented by implementor.
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpressionVisitor#visit(de
	 * .uka.ipd.sdq.beagle.core.evaluableexpressions.AdditionExpression)
	 */
	@Override
	public final void visit(final AdditionExpression expression) {
		this.atExpressionPrivate(expression);
		this.atAddition(expression);

		final Iterator<EvaluableExpression> innerExpressions = expression.getSummands().iterator();
		while (this.doTraverse && innerExpressions.hasNext()) {
			innerExpressions.next().receive(this);
		}

		this.afterExpressionPrivate(expression);
		this.afterAddition(expression);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpressionVisitor#visit(de
	 * .uka.ipd.sdq.beagle.core.evaluableexpressions.MultiplicationExpression)
	 */
	@Override
	public final void visit(final MultiplicationExpression expression) {
		this.atExpressionPrivate(expression);
		this.atMultiplication(expression);

		final Iterator<EvaluableExpression> innerExpressions = expression.getFactors().iterator();
		while (this.doTraverse && innerExpressions.hasNext()) {
			innerExpressions.next().receive(this);
		}

		this.afterExpressionPrivate(expression);
		this.afterMultiplication(expression);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpressionVisitor#visit(de
	 * .uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableVariable)
	 */
	@Override
	public final void visit(final EvaluableVariable variable) {
		this.atExpressionPrivate(variable);
		this.atVariable(variable);

		// no inner expressions to visit

		this.afterExpressionPrivate(variable);
		this.afterVariable(variable);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpressionVisitor#visit(de
	 * .uka.ipd.sdq.beagle.core.evaluableexpressions.ComparisonExpression)
	 */
	@Override
	public final void visit(final ComparisonExpression expression) {
		this.atExpressionPrivate(expression);
		this.atComparison(expression);

		if (this.doTraverse) {
			expression.getSmaller().receive(this);
		}
		if (this.doTraverse) {
			expression.getGreater().receive(this);
		}

		this.afterExpressionPrivate(expression);
		this.afterComparison(expression);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpressionVisitor#visit(de
	 * .uka.ipd.sdq.beagle.core.evaluableexpressions.ConstantExpression)
	 */
	@Override
	public final void visit(final ConstantExpression constant) {
		this.atExpressionPrivate(constant);
		this.atConstant(constant);

		// no inner expressions to visit

		this.afterExpressionPrivate(constant);
		this.afterConstant(constant);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpressionVisitor#visit(de
	 * .uka.ipd.sdq.beagle.core.evaluableexpressions.DivisionExpression)
	 */
	@Override
	public final void visit(final DivisionExpression expression) {
		this.atExpressionPrivate(expression);
		this.atDivision(expression);

		if (this.doTraverse) {
			expression.getDividend().receive(this);
		}
		if (this.doTraverse) {
			expression.getDivisor().receive(this);
		}

		this.afterExpressionPrivate(expression);
		this.afterDivision(expression);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpressionVisitor#visit(de
	 * .uka.ipd.sdq.beagle.core.evaluableexpressions.ExponentationExpression)
	 */
	@Override
	public final void visit(final ExponentationExpression expression) {
		this.atExpressionPrivate(expression);
		this.atExponentation(expression);

		if (this.doTraverse) {
			expression.getBase().receive(this);
		}
		if (this.doTraverse) {
			expression.getExponent().receive(this);
		}

		this.afterExpressionPrivate(expression);
		this.afterExponentation(expression);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpressionVisitor#visit(de
	 * .uka.ipd.sdq.beagle.core.evaluableexpressions.ExponentialFunctionExpression)
	 */
	@Override
	public final void visit(final ExponentialFunctionExpression expression) {
		this.atExpressionPrivate(expression);
		this.atExponentialFunction(expression);

		if (this.doTraverse) {
			expression.getExponent().receive(this);
		}

		this.afterExpressionPrivate(expression);
		this.afterExponentialFunction(expression);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpressionVisitor#visit(de
	 * .uka.ipd.sdq.beagle.core.evaluableexpressions.IfThenElseExpression)
	 */
	@Override
	public final void visit(final IfThenElseExpression expression) {
		this.atExpressionPrivate(expression);
		this.atIfThenElse(expression);

		if (this.doTraverse) {
			expression.getIfStatement().receive(this);
		}
		if (this.doTraverse) {
			expression.getThenStatement().receive(this);
		}
		if (this.doTraverse) {
			expression.getElseStatement().receive(this);
		}

		this.afterExpressionPrivate(expression);
		this.afterIfThenElse(expression);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpressionVisitor#visit(de
	 * .uka.ipd.sdq.beagle.core.evaluableexpressions.LogarithmExpression)
	 */
	@Override
	public final void visit(final LogarithmExpression expression) {
		this.atExpressionPrivate(expression);
		this.atLogarithm(expression);

		if (this.doTraverse) {
			expression.getBase().receive(this);
		}
		if (this.doTraverse) {
			expression.getAntilogarithm().receive(this);
		}

		this.afterExpressionPrivate(expression);
		this.afterLogarithm(expression);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpressionVisitor#visit(de
	 * .uka.ipd.sdq.beagle.core.evaluableexpressions.NaturalLogarithmExpression)
	 */
	@Override
	public final void visit(final NaturalLogarithmExpression expression) {
		this.atExpressionPrivate(expression);
		this.atNaturalLogarithm(expression);

		if (this.doTraverse) {
			expression.getAntilogarithm().receive(this);
		}

		this.afterExpressionPrivate(expression);
		this.afterNaturalLogarithm(expression);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpressionVisitor#visit(de
	 * .uka.ipd.sdq.beagle.core.evaluableexpressions.SineExpression)
	 */
	@Override
	public final void visit(final SineExpression expression) {
		this.atExpressionPrivate(expression);
		this.atSine(expression);

		if (this.doTraverse) {
			expression.getArgument().receive(this);
		}

		this.afterExpressionPrivate(expression);
		this.afterSine(expression);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpressionVisitor#visit(de
	 * .uka.ipd.sdq.beagle.core.evaluableexpressions.SubstractionExpression)
	 */
	@Override
	public final void visit(final SubstractionExpression expression) {
		this.atExpressionPrivate(expression);
		this.atSubstraction(expression);

		if (this.doTraverse) {
			expression.getMinuend().receive(this);
		}
		if (this.doTraverse) {
			expression.getSubstrahend().receive(this);
		}

		this.afterExpressionPrivate(expression);
		this.afterSubstraction(expression);
	}

}
