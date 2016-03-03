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

import java.util.Collection;

/**
 * A visitor allowing to create a new {@linkplain EvaluableExpression} out of an existing
 * one. Evaluable Expressions are immutable and can thus not simply be modified. However,
 * it may often be wished to create a new expression out of an existing one that only
 * differs from the initial one in some details.
 *
 * <p>This visitor follows the same traversal strategy as
 * {@link RecursiveEvaluableExpressionVisitor}. This means it will perform a depth-first
 * traversal on the tree formed by the expression passed to {@link #modifyRecursively}.
 * Implementors may call {@link #replaceCurrentExpressionWith(EvaluableExpression)} in any
 * hook. This will replace the currently visited expression and continue traversal in the
 * replacement. Replacing an expression will lead to a replacement of all expressions that
 * have the replaced expression as an inner expression. These “parent” expressions will be
 * replaced by a new expression of the same type containing the same inner expressions
 * except for the ones that were initially replaced.
 *
 * <p>This visitor will start at the expression passed to
 * {@link #modifyRecursively(EvaluableExpression)} and call the according {@code at} hook.
 * Afterwards, it will recursively visit all inner expressions and then call the
 * {@code after} hook. If {@link #stopTraversal()} is called, the traversal will no longer
 * visit inner expressions. Instead, it will go “up” the tree again, whilst calling the
 * {@code after} hooks, until it reached the initial expression and then terminate. Given
 * that no expression is thrown,the traversal of one expression tree that only contains
 * pairwise different inner expressions has these properties:
 *
 * <ul>
 *
 * <li> the number of all called {@code at} hooks will equal the number of all called
 * {@code after} hooks. However, the numbers may differ for a type of specific hooks
 * because the expression was changed during the visit. For example
 * {@link #atSubstraction} may be called on an expression that is replaced by an
 * {@linkplain AdditionExpression} in this hook. Thus {@link #afterAddition} will be
 * called instead of {@link #afterSubstraction} at this position when traversing upwards.
 *
 * <li> if for two expression {@code e1} and {@code e2}, {@code atExpression(e1)} was
 * called before {@code atExpression(e2)} was, {@code afterExpression(e2)} will be called
 * before {@code afterExpression(e1)} will be called.
 *
 * </ul>
 *
 * <p>Please note that evaluable expressions may often contain an instance of one
 * evaluable expression multiple times. Replacements do however only apply to the current
 * position in the traversal tree (and not all occurences of the currently visited
 * expression). For every new visit, traversing of inner expressions will be enabled.
 *
 * <p>This visitor is not thread safe. It may only be used to traverse one expression at a
 * time, meaning that its results are undefined if
 * {@link #modifyRecursively(EvaluableExpression)} is called when
 * {@link #getTraversalDepth()} does not return {@code -1}. There is however no limitation
 * on how many expression trees can be visited by an instance of this class.
 *
 * @author Joshua Gleitze
 * @see ExpressionTreeWalker
 */
public abstract class ModifyingEvaluableExpressionVisitor extends PartialExpressionTreeWalker {

	/**
	 * The momentarily visited expression. It can change multiple times at the same tree
	 * node!
	 */
	private EvaluableExpression currentExpression;

	/**
	 * How deeply we have the traversed so far. Will be 0 at the root.
	 */
	private int depth;

	/**
	 * How many expressions we’ve seen so far. Will be 1 at the root.
	 */
	private int count;

	/**
	 * The visitor handling the calls to the {@code at} hooks.
	 */
	private final AtHookHandler atHookHandler = new AtHookHandler();

	/**
	 * The visitor handling the calls to the {@code after} hooks.
	 */
	private final AfterHookHandler afterHookHandler = new AfterHookHandler();

	/**
	 * The iterator momentarily iterating over the inner expressions of the last
	 * expression visited on one level above this one. Will be {@code null} at the root
	 * expression.
	 */
	private InnerExpressionIterator lastInnerExpressionIterator;

	/**
	 * The modifier that will adapt the current expression if one of its inner expressions
	 * was modified.
	 */
	private final OuterExpressionModifier currentExpressionModifier = new OuterExpressionModifier();

	/**
	 * Moves the traversal to the next expression which must have already been put into
	 * {@link #currentExpression}. Because {@link #currentExpression} can change after
	 * every hook, this cannot be realised by one visitor. Instead, this method uses
	 * multiple visitors that newly resolve the current expression’s type to act
	 * accordingly.
	 */
	private void next() {
		this.depth++;
		this.count++;

		// call at-hooks
		this.atExpression(this.currentExpression);
		this.currentExpression.receive(this.atHookHandler);

		// visit inner expressions
		final InnerExpressionIterator oldInnerExpressionIterator = this.lastInnerExpressionIterator;
		this.lastInnerExpressionIterator = new InnerExpressionIterator();
		this.lastInnerExpressionIterator.recurse();
		this.lastInnerExpressionIterator = oldInnerExpressionIterator;

		// call after-hooks
		this.afterExpression(this.currentExpression);
		this.currentExpression.receive(this.afterHookHandler);
		this.depth--;
	}

	/**
	 * Starts visiting {@code expression}.
	 *
	 * @param expression The expression forming the root of the expression tree that shall
	 *            be traversed. Must not be {@code null}.
	 * @return The modified expression. This is the expression that was build to reflect
	 *         all replacements. Will be {@code expression} if no substantial call to
	 *         {@link #replaceCurrentExpressionWith(EvaluableExpression)} was made.
	 */
	protected EvaluableExpression modifyRecursively(final EvaluableExpression expression) {
		Validate.notNull(expression, "Cannot traverse null.");

		this.currentExpression = expression;
		this.lastInnerExpressionIterator = null;
		this.count = 0;
		this.depth = -1;
		this.startTraversingInnerExpressions();

		this.next();

		return this.currentExpression;
	}

	/**
	 * Replaces the currently visited expression with {@code expression} in the expression
	 * tree. This will lead the traversal to continue in {@code expression} and all
	 * expressions for which no {@code after} hooks have been called yet to be replaced by
	 * an expression reflecting this replacement. A call to this method has no effect if
	 * {@code expression} equals the momentarily visited expression.
	 *
	 * @param expression The new expression to be inserted at the point in the expression
	 *            tree at which the current expression is found. Must not be {@code null}.
	 *
	 * @throws IllegalStateException If this visitor is currently not visiting an
	 *             expression tree.
	 */
	protected void replaceCurrentExpressionWith(final EvaluableExpression expression) {
		Validate.notNull(expression, "The current expression can only be replaced by a new one.");
		Validate.validState(this.depth >= 0, "This visitor does no visit any expression in the moment. "
			+ "Please call this method only from a visiting hook!");

		if (this.currentExpression.equals(expression)) {
			return;
		}

		this.currentExpression = expression;
		if (this.lastInnerExpressionIterator != null) {
			this.lastInnerExpressionIterator.notifyNew();
		}
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
	 * Visitor responsible to iterate over an expression’s inner expressions. It first
	 * resolves the current expression by visiting it. Afterwards, it translates the
	 * expression’s inner expressions into an expression array and iterates over it to
	 * call the recursion for them. If any of the inner expressions change while doing so,
	 * the class will call
	 * {@link ModifyingEvaluableExpressionVisitor#currentExpressionModifier} to adapt the
	 * current expression accordingly.
	 *
	 * @author Joshua Gleitze
	 */
	private final class InnerExpressionIterator implements EvaluableExpressionVisitor {

		/**
		 * The InnerExpressionIterator momentarily iterating at one level “above” this one
		 * in the traversal tree. Will be notified if the current expression is modified.
		 */
		private final InnerExpressionIterator parent;

		/**
		 * {@code true} if any inner expression was modified while iterating over inner
		 * expressions.
		 */
		private boolean innerExpressionWasModified;

		/**
		 * The current expression’s inner expressions to iterate over.
		 */
		private EvaluableExpression[] innerExpressions;

		/**
		 * Index of the expression which we’re currently iterating over in
		 * {@link #innerExpressions}.
		 */
		private int innerExpressionIndex;

		/**
		 * Creates this recursor. A new recursor must be created for every expression that
		 * is visited during traversal.
		 */
		private InnerExpressionIterator() {
			this.parent = ModifyingEvaluableExpressionVisitor.this.lastInnerExpressionIterator;
		}

		/**
		 * Notifies this recursor that an inner expression was modified. It will thus
		 * modify the current expression at the end of the momentary recursion.
		 */
		private void notifyNew() {
			this.innerExpressions[this.innerExpressionIndex] =
				ModifyingEvaluableExpressionVisitor.this.currentExpression;
			this.innerExpressionWasModified = true;
		}

		/**
		 * Starts iterating over the current expression’s inner expressions.
		 */
		private void recurse() {
			this.innerExpressionIndex = 0;
			this.innerExpressionWasModified = false;
			final EvaluableExpression oldExpression = ModifyingEvaluableExpressionVisitor.this.currentExpression;

			ModifyingEvaluableExpressionVisitor.this.currentExpression.receive(this);

			ModifyingEvaluableExpressionVisitor.this.currentExpression = oldExpression;
			if (this.innerExpressionWasModified) {
				ModifyingEvaluableExpressionVisitor.this.currentExpressionModifier.modify(this.innerExpressions);
				if (this.parent != null) {
					this.parent.notifyNew();
				}
			}
		}

		/**
		 * Makes the {@link ModifyingEvaluableExpressionVisitor} visit all
		 * {@code currentInnerExpressions}.
		 *
		 * @param currentInnerExpressions All innerExpressions of
		 *            {@link ModifyingEvaluableExpressionVisitor#currentExpression}.
		 */
		private void visitInner(final Collection<EvaluableExpression> currentInnerExpressions) {
			this.visitInner(currentInnerExpressions.toArray(new EvaluableExpression[currentInnerExpressions.size()]));
		}

		/**
		 * Makes the {@link ModifyingEvaluableExpressionVisitor} visit all
		 * {@code currentInnerExpressions}.
		 *
		 * @param currentInnerExpressions All innerExpressions of
		 *            {@link ModifyingEvaluableExpressionVisitor#currentExpression}.
		 */
		private void visitInner(final EvaluableExpression... currentInnerExpressions) {
			this.innerExpressions = currentInnerExpressions;

			// iterate over all as long further traversing is not stopped
			for (this.innerExpressionIndex = 0; this.innerExpressionIndex < this.innerExpressions.length
				&& ModifyingEvaluableExpressionVisitor.this
					.willTraverseInnerExpressions(); this.innerExpressionIndex++) {
				ModifyingEvaluableExpressionVisitor.this.currentExpression =
					this.innerExpressions[this.innerExpressionIndex];
				ModifyingEvaluableExpressionVisitor.this.next();
			}
		}

		@Override
		public void visit(final AdditionExpression expression) {
			this.visitInner(expression.getSummands());
		}

		@Override
		public void visit(final MultiplicationExpression expression) {
			this.visitInner(expression.getFactors());
		}

		@Override
		public void visit(final EvaluableVariable variable) {
			// No inner expressions—nothing to do here
		}

		@Override
		public void visit(final ComparisonExpression expression) {
			this.visitInner(expression.getSmaller(), expression.getGreater());
		}

		@Override
		public void visit(final ConstantExpression constant) {
			// No inner expressions—nothing to do here
		}

		@Override
		public void visit(final DivisionExpression expression) {
			this.visitInner(expression.getDividend(), expression.getDivisor());
		}

		@Override
		public void visit(final ExponentationExpression expression) {
			this.visitInner(expression.getBase(), expression.getExponent());
		}

		@Override
		public void visit(final ExponentialFunctionExpression expression) {
			this.visitInner(expression.getExponent());
		}

		@Override
		public void visit(final IfThenElseExpression expression) {
			this.visitInner(expression.getIfStatement(), expression.getThenStatement(), expression.getElseStatement());
		}

		@Override
		public void visit(final LogarithmExpression expression) {
			this.visitInner(expression.getBase(), expression.getAntilogarithm());
		}

		@Override
		public void visit(final NaturalLogarithmExpression expression) {
			this.visitInner(expression.getAntilogarithm());
		}

		@Override
		public void visit(final SineExpression expression) {
			this.visitInner(expression.getArgument());
		}

		@Override
		public void visit(final SubtractionExpression expression) {
			this.visitInner(expression.getMinuend(), expression.getSubtrahend());
		}

	}

	/**
	 * Rebuilds {@link ModifyingEvaluableExpressionVisitor#currentExpression} to reflect
	 * that one of its inner expressions was modified. To do so, it reverts
	 * {@link ModifyingEvaluableExpressionVisitor.InnerExpressionIterator}’s conversion of
	 * inner expressions into an array. This means that the new expression will be of the
	 * same type as the old one and behave equally, except that the inner expressions that
	 * were changed will be replaced accordingly.
	 *
	 * @author Joshua Gleitze
	 */
	private class OuterExpressionModifier implements EvaluableExpressionVisitor {

		/**
		 * The maximal number of arguments any expression constructor takes.
		 */
		private final int three = 3;

		/**
		 * Will hold the new curren expression.
		 */
		private EvaluableExpression result;

		/**
		 * Holds the array of inner expressions, as costructed by
		 * {@link ModifyingEvaluableExpressionVisitor.InnerExpressionIterator}.
		 */
		private EvaluableExpression[] inner;

		/**
		 * Sets {@link ModifyingEvaluableExpressionVisitor#currentExpression} to an
		 * expression built out of {@code newInnerExpressions}.
		 *
		 * @param newInnerExpressions The inner expressions of the newly created
		 *            expressions. Must be in the order the responsible constructor
		 *            expects them to be.
		 */
		public void modify(final EvaluableExpression[] newInnerExpressions) {
			this.inner = newInnerExpressions;
			ModifyingEvaluableExpressionVisitor.this.currentExpression.receive(this);
			ModifyingEvaluableExpressionVisitor.this.currentExpression = this.result;
		}

		@Override
		public void visit(final AdditionExpression expression) {
			this.result = new AdditionExpression(this.inner);
		}

		@Override
		public void visit(final MultiplicationExpression expression) {
			this.result = new MultiplicationExpression(this.inner);

		}

		@Override
		public void visit(final EvaluableVariable variable) {
			// Will never be called
			assert false;
		}

		@Override
		public void visit(final ComparisonExpression expression) {
			this.result = new ComparisonExpression(this.inner[0], this.inner[1]);
		}

		@Override
		public void visit(final ConstantExpression constant) {
			// Will never be called
			assert false;
		}

		@Override
		public void visit(final DivisionExpression expression) {
			this.result = new DivisionExpression(this.inner[0], this.inner[1]);
		}

		@Override
		public void visit(final ExponentationExpression expression) {
			this.result = new ExponentationExpression(this.inner[0], this.inner[1]);
		}

		@Override
		public void visit(final ExponentialFunctionExpression expression) {
			this.result = new ExponentialFunctionExpression(this.inner[0]);
		}

		@Override
		public void visit(final IfThenElseExpression expression) {
			this.result = new IfThenElseExpression(this.inner[0], this.inner[1], this.inner[this.three]);
		}

		@Override
		public void visit(final LogarithmExpression expression) {
			this.result = new LogarithmExpression(this.inner[0], this.inner[1]);
		}

		@Override
		public void visit(final NaturalLogarithmExpression expression) {
			this.result = new NaturalLogarithmExpression(this.inner[0]);
		}

		@Override
		public void visit(final SineExpression expression) {
			this.result = new SineExpression(this.inner[0]);
		}

		@Override
		public void visit(final SubtractionExpression expression) {
			this.result = new SubtractionExpression(this.inner[0], this.inner[1]);
		}
	}

	/**
	 * Resolves the {@link ModifyingEvaluableExpressionVisitor#currentExpression}’s type
	 * to call the right {@code at} hook.
	 *
	 * @author Joshua Gleitze
	 */
	private class AtHookHandler implements EvaluableExpressionVisitor {

		@Override
		public void visit(final AdditionExpression expression) {
			ModifyingEvaluableExpressionVisitor.this.atAddition(expression);
		}

		@Override
		public void visit(final MultiplicationExpression expression) {
			ModifyingEvaluableExpressionVisitor.this.atMultiplication(expression);
		}

		@Override
		public void visit(final EvaluableVariable variable) {
			ModifyingEvaluableExpressionVisitor.this.atVariable(variable);
		}

		@Override
		public void visit(final ComparisonExpression expression) {
			ModifyingEvaluableExpressionVisitor.this.atComparison(expression);
		}

		@Override
		public void visit(final ConstantExpression constant) {
			ModifyingEvaluableExpressionVisitor.this.atConstant(constant);
		}

		@Override
		public void visit(final DivisionExpression expression) {
			ModifyingEvaluableExpressionVisitor.this.atDivision(expression);
		}

		@Override
		public void visit(final ExponentationExpression expression) {
			ModifyingEvaluableExpressionVisitor.this.atExponentation(expression);
		}

		@Override
		public void visit(final ExponentialFunctionExpression expression) {
			ModifyingEvaluableExpressionVisitor.this.atExponentialFunction(expression);
		}

		@Override
		public void visit(final IfThenElseExpression expression) {
			ModifyingEvaluableExpressionVisitor.this.atIfThenElse(expression);
		}

		@Override
		public void visit(final LogarithmExpression expression) {
			ModifyingEvaluableExpressionVisitor.this.atLogarithm(expression);
		}

		@Override
		public void visit(final NaturalLogarithmExpression expression) {
			ModifyingEvaluableExpressionVisitor.this.atNaturalLogarithm(expression);
		}

		@Override
		public void visit(final SineExpression expression) {
			ModifyingEvaluableExpressionVisitor.this.atSine(expression);
		}

		@Override
		public void visit(final SubtractionExpression expression) {
			ModifyingEvaluableExpressionVisitor.this.atSubtraction(expression);
		}
	}

	/**
	 * Resolves the {@link ModifyingEvaluableExpressionVisitor#currentExpression}’s type
	 * to call the right {@code after} hook.
	 *
	 * @author Joshua Gleitze
	 */
	private class AfterHookHandler implements EvaluableExpressionVisitor {

		@Override
		public void visit(final AdditionExpression expression) {
			ModifyingEvaluableExpressionVisitor.this.afterAddition(expression);
		}

		@Override
		public void visit(final MultiplicationExpression expression) {
			ModifyingEvaluableExpressionVisitor.this.afterMultiplication(expression);
		}

		@Override
		public void visit(final EvaluableVariable variable) {
			ModifyingEvaluableExpressionVisitor.this.afterVariable(variable);
		}

		@Override
		public void visit(final ComparisonExpression expression) {
			ModifyingEvaluableExpressionVisitor.this.afterComparison(expression);
		}

		@Override
		public void visit(final ConstantExpression constant) {
			ModifyingEvaluableExpressionVisitor.this.afterConstant(constant);
		}

		@Override
		public void visit(final DivisionExpression expression) {
			ModifyingEvaluableExpressionVisitor.this.afterDivision(expression);
		}

		@Override
		public void visit(final ExponentationExpression expression) {
			ModifyingEvaluableExpressionVisitor.this.afterExponentation(expression);
		}

		@Override
		public void visit(final ExponentialFunctionExpression expression) {
			ModifyingEvaluableExpressionVisitor.this.afterExponentialFunction(expression);
		}

		@Override
		public void visit(final IfThenElseExpression expression) {
			ModifyingEvaluableExpressionVisitor.this.afterIfThenElse(expression);
		}

		@Override
		public void visit(final LogarithmExpression expression) {
			ModifyingEvaluableExpressionVisitor.this.afterLogarithm(expression);
		}

		@Override
		public void visit(final NaturalLogarithmExpression expression) {
			ModifyingEvaluableExpressionVisitor.this.afterNaturalLogarithm(expression);
		}

		@Override
		public void visit(final SineExpression expression) {
			ModifyingEvaluableExpressionVisitor.this.afterSine(expression);
		}

		@Override
		public void visit(final SubtractionExpression expression) {
			ModifyingEvaluableExpressionVisitor.this.afterSubtraction(expression);
		}
	}
}
