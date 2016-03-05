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

import org.apache.commons.lang3.Validate;

import java.util.Iterator;

/**
 * Visitor of {@link EvaluableExpression EvaluableExpressions}, recursively visiting all
 * their inner expressions.
 *
 * <p>This visitor will start at the expression passed to
 * {@link #visitRecursively(EvaluableExpression)} and call the according {@code at} hook.
 * Afterwards, it will recursively visit all inner expressions and then call the
 * {@code after} hook. It thus realises a depth-first traversal of the tree formed by each
 * expression. If {@link #stopTraversingInnerExpressions()} is called, the traversal will
 * no longer visit inner expressions. Instead, it will go “up” the tree again, whilst
 * calling the {@code after} hooks, until it reached the initial expression and then
 * terminate. Given that no exception is thrown, the traversal of one expression tree that
 * only contains pairwise different inner expressions has these properties:
 *
 * <ul>
 *
 * <li> for each type, the number of called {@code at} hooks will equal the number of
 * called {@code after} hooks.
 *
 * <li> if for two expression {@code e1} and {@code e2}, {@code atExpression(e1)} was
 * called before {@code atExpression(e2)} was, {@code afterExpression(e2)} will be called
 * before {@code afterExpression(e1)} will be called.
 *
 * </ul>
 *
 * <p>Please note that evaluable expressions may often contain an instance of one
 * evaluable expression multiple times. For every new visit, traversing of inner
 * expressions will be enabled.
 *
 * <p>This visitor is not thread safe. It may only be used to traverse one expression at a
 * time, meaning that its results are undefined if
 * {@link #visitRecursively(EvaluableExpression)} is called when
 * {@link #getTraversalDepth()} does not return {@code -1}. There is however no limitation
 * on how many expression trees can be visited by an instance of this class.
 *
 * @author Joshua Gleitze
 * @see ExpressionTreeWalker
 */
public abstract class RecursiveEvaluableExpressionVisitor extends PartialExpressionTreeWalker {

	/**
	 * The visitor actually doing the recursive visiting.
	 */
	private final RecursiveWalker recursiveWalker = new RecursiveWalker();

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
	 * @param expression The expression that forms the root of the tree this visitor shall
	 *            traverse. Must not be {@code null}.
	 */
	protected void visitRecursively(final EvaluableExpression expression) {
		Validate.notNull(expression, "Cannot visit null.");

		this.startTraversingInnerExpressions();
		this.depth = -1;
		this.count = 0;
		expression.receive(this.recursiveWalker);
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
	 * expressions were visited and after the specific {@code after*} hook will was
	 * called.
	 *
	 * @param expression The momentary visited expression.
	 */
	private void afterExpressionPrivate(final EvaluableExpression expression) {
		this.depth--;
	}

	@Override
	protected int getVisitedCount() {
		return this.count;
	}

	@Override
	protected int getTraversalDepth() {
		return this.depth;
	}

	/**
	 * The actual visitor passed to the expressions. Does the actual walking. Realised as
	 * private nested class to hide the {@code visit} methods from clients.
	 *
	 * @author Joshua Gleitze
	 */
	private class RecursiveWalker implements EvaluableExpressionVisitor {

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpressionVisitor#
		 * visit(de .uka.ipd.sdq.beagle.core.evaluableexpressions.AdditionExpression)
		 */
		@Override
		public final void visit(final AdditionExpression expression) {
			RecursiveEvaluableExpressionVisitor.this.atExpressionPrivate(expression);
			RecursiveEvaluableExpressionVisitor.this.atAddition(expression);

			final Iterator<EvaluableExpression> innerExpressions = expression.getSummands().iterator();
			while (RecursiveEvaluableExpressionVisitor.this.willTraverseInnerExpressions()
				&& innerExpressions.hasNext()) {
				innerExpressions.next().receive(this);
			}

			RecursiveEvaluableExpressionVisitor.this.afterExpression(expression);
			RecursiveEvaluableExpressionVisitor.this.afterAddition(expression);
			RecursiveEvaluableExpressionVisitor.this.afterExpressionPrivate(expression);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpressionVisitor#
		 * visit(de
		 * .uka.ipd.sdq.beagle.core.evaluableexpressions.MultiplicationExpression)
		 */
		@Override
		public final void visit(final MultiplicationExpression expression) {
			RecursiveEvaluableExpressionVisitor.this.atExpressionPrivate(expression);
			RecursiveEvaluableExpressionVisitor.this.atMultiplication(expression);

			final Iterator<EvaluableExpression> innerExpressions = expression.getFactors().iterator();
			while (RecursiveEvaluableExpressionVisitor.this.willTraverseInnerExpressions()
				&& innerExpressions.hasNext()) {
				innerExpressions.next().receive(this);
			}

			RecursiveEvaluableExpressionVisitor.this.afterExpression(expression);
			RecursiveEvaluableExpressionVisitor.this.afterMultiplication(expression);
			RecursiveEvaluableExpressionVisitor.this.afterExpressionPrivate(expression);

		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpressionVisitor#
		 * visit(de .uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableVariable)
		 */
		@Override
		public final void visit(final EvaluableVariable variable) {
			RecursiveEvaluableExpressionVisitor.this.atExpressionPrivate(variable);
			RecursiveEvaluableExpressionVisitor.this.atVariable(variable);

			// no inner expressions to visit

			RecursiveEvaluableExpressionVisitor.this.afterExpression(variable);
			RecursiveEvaluableExpressionVisitor.this.afterVariable(variable);
			RecursiveEvaluableExpressionVisitor.this.afterExpressionPrivate(variable);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpressionVisitor#
		 * visit(de .uka.ipd.sdq.beagle.core.evaluableexpressions.ComparisonExpression)
		 */
		@Override
		public final void visit(final ComparisonExpression expression) {
			RecursiveEvaluableExpressionVisitor.this.atExpressionPrivate(expression);
			RecursiveEvaluableExpressionVisitor.this.atComparison(expression);

			if (RecursiveEvaluableExpressionVisitor.this.willTraverseInnerExpressions()) {
				expression.getSmaller().receive(this);
			}
			if (RecursiveEvaluableExpressionVisitor.this.willTraverseInnerExpressions()) {
				expression.getGreater().receive(this);
			}

			RecursiveEvaluableExpressionVisitor.this.afterExpression(expression);
			RecursiveEvaluableExpressionVisitor.this.afterComparison(expression);
			RecursiveEvaluableExpressionVisitor.this.afterExpressionPrivate(expression);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpressionVisitor#
		 * visit(de .uka.ipd.sdq.beagle.core.evaluableexpressions.ConstantExpression)
		 */
		@Override
		public final void visit(final ConstantExpression constant) {
			RecursiveEvaluableExpressionVisitor.this.atExpressionPrivate(constant);
			RecursiveEvaluableExpressionVisitor.this.atConstant(constant);

			// no inner expressions to visit

			RecursiveEvaluableExpressionVisitor.this.afterExpression(constant);
			RecursiveEvaluableExpressionVisitor.this.afterConstant(constant);
			RecursiveEvaluableExpressionVisitor.this.afterExpressionPrivate(constant);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpressionVisitor#
		 * visit(de .uka.ipd.sdq.beagle.core.evaluableexpressions.DivisionExpression)
		 */
		@Override
		public final void visit(final DivisionExpression expression) {
			RecursiveEvaluableExpressionVisitor.this.atExpressionPrivate(expression);
			RecursiveEvaluableExpressionVisitor.this.atDivision(expression);

			if (RecursiveEvaluableExpressionVisitor.this.willTraverseInnerExpressions()) {
				expression.getDividend().receive(this);
			}
			if (RecursiveEvaluableExpressionVisitor.this.willTraverseInnerExpressions()) {
				expression.getDivisor().receive(this);
			}

			RecursiveEvaluableExpressionVisitor.this.afterExpression(expression);
			RecursiveEvaluableExpressionVisitor.this.afterDivision(expression);
			RecursiveEvaluableExpressionVisitor.this.afterExpressionPrivate(expression);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpressionVisitor#
		 * visit(de .uka.ipd.sdq.beagle.core.evaluableexpressions.ExponentationExpression)
		 */
		@Override
		public final void visit(final ExponentationExpression expression) {
			RecursiveEvaluableExpressionVisitor.this.atExpressionPrivate(expression);
			RecursiveEvaluableExpressionVisitor.this.atExponentation(expression);

			if (RecursiveEvaluableExpressionVisitor.this.willTraverseInnerExpressions()) {
				expression.getBase().receive(this);
			}
			if (RecursiveEvaluableExpressionVisitor.this.willTraverseInnerExpressions()) {
				expression.getExponent().receive(this);
			}

			RecursiveEvaluableExpressionVisitor.this.afterExpression(expression);
			RecursiveEvaluableExpressionVisitor.this.afterExponentation(expression);
			RecursiveEvaluableExpressionVisitor.this.afterExpressionPrivate(expression);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpressionVisitor#
		 * visit(de
		 * .uka.ipd.sdq.beagle.core.evaluableexpressions.ExponentialFunctionExpression)
		 */
		@Override
		public final void visit(final ExponentialFunctionExpression expression) {
			RecursiveEvaluableExpressionVisitor.this.atExpressionPrivate(expression);
			RecursiveEvaluableExpressionVisitor.this.atExponentialFunction(expression);

			if (RecursiveEvaluableExpressionVisitor.this.willTraverseInnerExpressions()) {
				expression.getExponent().receive(this);
			}

			RecursiveEvaluableExpressionVisitor.this.afterExpression(expression);
			RecursiveEvaluableExpressionVisitor.this.afterExponentialFunction(expression);
			RecursiveEvaluableExpressionVisitor.this.afterExpressionPrivate(expression);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpressionVisitor#
		 * visit(de .uka.ipd.sdq.beagle.core.evaluableexpressions.IfThenElseExpression)
		 */
		@Override
		public final void visit(final IfThenElseExpression expression) {
			RecursiveEvaluableExpressionVisitor.this.atExpressionPrivate(expression);
			RecursiveEvaluableExpressionVisitor.this.atIfThenElse(expression);

			if (RecursiveEvaluableExpressionVisitor.this.willTraverseInnerExpressions()) {
				expression.getIfStatement().receive(this);
			}
			if (RecursiveEvaluableExpressionVisitor.this.willTraverseInnerExpressions()) {
				expression.getThenStatement().receive(this);
			}
			if (RecursiveEvaluableExpressionVisitor.this.willTraverseInnerExpressions()) {
				expression.getElseStatement().receive(this);
			}

			RecursiveEvaluableExpressionVisitor.this.afterExpression(expression);
			RecursiveEvaluableExpressionVisitor.this.afterIfThenElse(expression);
			RecursiveEvaluableExpressionVisitor.this.afterExpressionPrivate(expression);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpressionVisitor#
		 * visit(de .uka.ipd.sdq.beagle.core.evaluableexpressions.LogarithmExpression)
		 */
		@Override
		public final void visit(final LogarithmExpression expression) {
			RecursiveEvaluableExpressionVisitor.this.atExpressionPrivate(expression);
			RecursiveEvaluableExpressionVisitor.this.atLogarithm(expression);

			if (RecursiveEvaluableExpressionVisitor.this.willTraverseInnerExpressions()) {
				expression.getBase().receive(this);
			}
			if (RecursiveEvaluableExpressionVisitor.this.willTraverseInnerExpressions()) {
				expression.getAntilogarithm().receive(this);
			}

			RecursiveEvaluableExpressionVisitor.this.afterExpression(expression);
			RecursiveEvaluableExpressionVisitor.this.afterLogarithm(expression);
			RecursiveEvaluableExpressionVisitor.this.afterExpressionPrivate(expression);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpressionVisitor#
		 * visit(de
		 * .uka.ipd.sdq.beagle.core.evaluableexpressions.NaturalLogarithmExpression)
		 */
		@Override
		public final void visit(final NaturalLogarithmExpression expression) {
			RecursiveEvaluableExpressionVisitor.this.atExpressionPrivate(expression);
			RecursiveEvaluableExpressionVisitor.this.atNaturalLogarithm(expression);

			if (RecursiveEvaluableExpressionVisitor.this.willTraverseInnerExpressions()) {
				expression.getAntilogarithm().receive(this);
			}

			RecursiveEvaluableExpressionVisitor.this.afterExpression(expression);
			RecursiveEvaluableExpressionVisitor.this.afterNaturalLogarithm(expression);
			RecursiveEvaluableExpressionVisitor.this.afterExpressionPrivate(expression);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpressionVisitor#
		 * visit(de .uka.ipd.sdq.beagle.core.evaluableexpressions.SineExpression)
		 */
		@Override
		public final void visit(final SineExpression expression) {
			RecursiveEvaluableExpressionVisitor.this.atExpressionPrivate(expression);
			RecursiveEvaluableExpressionVisitor.this.atSine(expression);

			if (RecursiveEvaluableExpressionVisitor.this.willTraverseInnerExpressions()) {
				expression.getArgument().receive(this);
			}

			RecursiveEvaluableExpressionVisitor.this.afterExpression(expression);
			RecursiveEvaluableExpressionVisitor.this.afterSine(expression);
			RecursiveEvaluableExpressionVisitor.this.afterExpressionPrivate(expression);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpressionVisitor#
		 * visit(de .uka.ipd.sdq.beagle.core.evaluableexpressions.SubstractionExpression)
		 */
		@Override
		public final void visit(final SubtractionExpression expression) {
			RecursiveEvaluableExpressionVisitor.this.atExpressionPrivate(expression);
			RecursiveEvaluableExpressionVisitor.this.atSubtraction(expression);

			if (RecursiveEvaluableExpressionVisitor.this.willTraverseInnerExpressions()) {
				expression.getMinuend().receive(this);
			}
			if (RecursiveEvaluableExpressionVisitor.this.willTraverseInnerExpressions()) {
				expression.getSubtrahend().receive(this);
			}

			RecursiveEvaluableExpressionVisitor.this.afterExpression(expression);
			RecursiveEvaluableExpressionVisitor.this.afterSubtraction(expression);
			RecursiveEvaluableExpressionVisitor.this.afterExpressionPrivate(expression);
		}
	}

}
