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

import org.apache.commons.lang3.Validate;

import java.util.Collection;

/**
 * A visitor allowing to create a new {@linkplain EvaluableExpression} out of an existing
 * one. Evaluable Expressions are immutable and can thus not simply be modified. However,
 * it may often be wished to create a new expression out of an existing one that only
 * differs from the initial one in some details.
 *
 * <p>This visitor follows the same traversal strategy as
 * {@link RecursiveEvaluableExpressionVisitor}. However, implementors may call
 * {@link #replaceCurrentExpressionWith(EvaluableExpression)} in any hook. This will
 * replace the currently visited expression and continue traversal in the replacement.
 * Replacing an expression will lead to a replacement of all expressions that have the
 * replaced expression as an inner expression. These “parent” expressions will be replaced
 * by a new expression of the same type containing the same inner expressions except for
 * the ones that were initially replaced.
 *
 * @author Joshua Gleitze
 */
public abstract class ModifyingEvaluableExpressionVisitor implements EvaluableExpressionVisitor {

	/**
	 * The momentarily visited expression. It can change multiple times at the same tree
	 * node!
	 */
	private EvaluableExpression currentExpression;

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
	}

	protected EvaluableExpression modifyRecursively(final EvaluableExpression expression) {
		Validate.notNull(expression, "Cannot traverse null.");

		this.currentExpression = expression;
		this.lastInnerExpressionIterator = null;
		this.next();

		return this.currentExpression;
	}

	protected void replaceCurrentExpressionWith(final EvaluableExpression expression) {
		Validate.notNull(expression, "The current expression can only be replaced by a new one.");

		this.currentExpression = expression;
		if (this.lastInnerExpressionIterator != null) {
			this.lastInnerExpressionIterator.notifyNew();
		}
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

			for (this.innerExpressionIndex =
				0; this.innerExpressionIndex < this.innerExpressions.length; this.innerExpressionIndex++) {
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
			this.visitInner(expression.getDivisor(), expression.getDividend());
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
		public void visit(final SubstractionExpression expression) {
			this.visitInner(expression.getSubstrahend(), expression.getMinuend());
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
		public void visit(final SubstractionExpression expression) {
			this.result = new SubstractionExpression(this.inner[0], this.inner[1]);
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
		public void visit(final SubstractionExpression expression) {
			ModifyingEvaluableExpressionVisitor.this.atSubstraction(expression);
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
		public void visit(final SubstractionExpression expression) {
			ModifyingEvaluableExpressionVisitor.this.afterSubstraction(expression);
		}
	}
}
