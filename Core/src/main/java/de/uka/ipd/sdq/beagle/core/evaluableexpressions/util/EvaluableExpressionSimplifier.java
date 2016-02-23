package de.uka.ipd.sdq.beagle.core.evaluableexpressions.util;

import de.uka.ipd.sdq.beagle.core.evaluableexpressions.AdditionExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.ConstantExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableVariableAssignment;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.SubtractionExpression;

import org.apache.commons.lang3.Validate;

import java.util.LinkedList;
import java.util.List;

/**
 * Produces shorter versions of unnecessarily complex {@linkplain EvaluableExpression
 * EvaluableExpressions}. The class aims to reduce the number of an expressions’s inner
 * expressions on a best effort basis. It does not guarantee that it will find the
 * shortest version. In other words, if {@code |e|} describes the number of {@code e}’s
 * inner expressions, {@code this.simplify(e)} will return an expression {@code e1}, such
 * that {@code |e1| ≤ |e|} and for any {@link EvaluableVariableAssignment} {@code x}
 * {@code e.evaluate(x) = e1.evaluate(x)}.
 *
 * @author Joshua Gleitze
 */
public class EvaluableExpressionSimplifier {

	/**
	 * The instance that will do the actual work.
	 */
	private final ActualSimplifier sipmlifier = new ActualSimplifier();


	/**
	 * Simplifies {@code expression}.
	 *
	 * @param expression An evaluable expression. Must not be {@code null}.
	 * @return An equivalent, potentially shorter version of {@code expression}. See the
	 *         class description for details. Might me {@code expression} itself.
	 */
	public EvaluableExpression simplify(final EvaluableExpression expression) {
		Validate.notNull("The expression passed to simplify was null!");
		return this.sipmlifier.modifyRecursively(expression);
	}

	/*
	 * This outer visitor recursively walks the tree. In the after hooks, the matching
	 * simplifier for the current exrpession is invoked. We thus simlify bottom up.
	 */
	/**
	 * The class actually performing the simplification. Realised as private inner class
	 * to hide the visitor interface.
	 *
	 * @author Joshua Gleitze
	 */
	private class ActualSimplifier extends ModifyingEvaluableExpressionVisitor {

		/**
		 * Handles simplifying of an {@link AdditionExpression}.
		 */
		private final AdditionSimplifier additionSimplifier = new AdditionSimplifier();

		@Override
		protected void afterAddition(final AdditionExpression expression) {
			this.additionSimplifier.simplify(expression);
		}
	}

	/**
	 * Simplifies {@link AdditionExpression AdditionExpressions}.
	 *
	 * @author Joshua Gleitze
	 */
	private class AdditionSimplifier extends AbstractEvaluableExpressionVisitor {

		/**
		 * Merges the subtrahends of contained {@linkplain SubtractionExpression
		 * SubtractionExpressions}.
		 */
		private final SubtrahendMerger subtrahendMerger = new SubtrahendMerger();

		/**
		 * The summands that will form the addition. Will be modified to reflect changes.
		 */
		private List<EvaluableExpression> summands;

		/**
		 * All expression that are added negatively to this addition. If this contains an
		 * element, the addition will be put in a subtraction.
		 */
		private final List<EvaluableExpression> negativePart = new LinkedList<>();

		/**
		 * {@code true} if any modification was made to {@link #summands}.
		 */
		private boolean modified;

		/**
		 * The index at which we’re currently iterating.
		 */
		private int index;

		/**
		 * Index of the only constant summand in the new expression. Will be {@code -1} if
		 * no constant has been seen yet.
		 */
		private int constantIndex;

		/**
		 * Sum of all inner constants seen so far.
		 */
		private double constantSum;

		/**
		 * {@code true} if the last visited elements was removed from the list of
		 * summands.
		 */
		private boolean removed;

		/**
		 * Removes the currently visited summand from the list. Asserts that the next
		 * element (which will be at {@link #index} after this operation) will be read in
		 * in the next iteration. Does not modify {@link #index}.
		 */
		private void remove() {
			this.modified = true;
			this.removed = true;
			this.summands.remove(this.index);
		}

		/**
		 * Simplifies an {@link AdditionExpression}.
		 *
		 * @param expression the expression to simplify.
		 */
		private void simplify(final AdditionExpression expression) {
			this.constantIndex = -1;
			this.summands = new LinkedList<>(expression.getSummands());
			this.modified = false;
			this.negativePart.clear();

			// Note: This must be a vanilla loop because we might modify the iterated
			// list!
			for (this.index = 0; this.index < this.summands.size(); this.index += this.removed ? 0 : 1) {
				this.removed = false;
				this.summands.get(this.index).receive(this);
			}

			if (this.modified) {
				EvaluableExpression newSum;
				if (this.summands.size() > 1) {
					newSum = new AdditionExpression(this.summands);
				} else {
					assert this.summands.size() == 1;
					newSum = this.summands.get(0);
				}
				if (this.negativePart.size() > 0) {
					if (this.negativePart.size() == 1) {
						newSum = new SubtractionExpression(newSum, this.negativePart.get(0));
					} else {
						newSum = new SubtractionExpression(newSum, new AdditionExpression(this.negativePart));
					}
				}
				EvaluableExpressionSimplifier.this.sipmlifier.replaceCurrentExpressionWith(newSum);
			}
		}

		@Override
		public void visit(final AdditionExpression expression) {
			this.remove();
			this.summands.addAll(this.index, expression.getSummands());
		}

		@Override
		public void visit(final ConstantExpression constant) {
			if (this.constantIndex == -1) {
				this.constantIndex = this.index;
				this.constantSum = constant.getValue();
			} else {
				this.constantSum += constant.getValue();
				this.summands.set(this.constantIndex, ConstantExpression.forValue(this.constantSum));
				this.remove();
			}
		}

		@Override
		public void visit(final SubtractionExpression expression) {
			this.remove();
			this.summands.add(expression.getMinuend());
			expression.getSubtrahend().receive(this.subtrahendMerger);
		}

		/**
		 * Handles Subtrahends in the addition.
		 *
		 * @author Joshua Gleitze
		 */
		private class SubtrahendMerger extends AbstractEvaluableExpressionVisitor {

			@Override
			protected void visitOther(final EvaluableExpression expression) {
				AdditionSimplifier.this.negativePart.add(expression);
			}

			@Override
			public void visit(final AdditionExpression expression) {
				// the addition expression must be flat (⇔ contain no other
				// AdditionExpression) at this point.
				AdditionSimplifier.this.negativePart.addAll(expression.getSummands());
			}

			@Override
			public void visit(final ConstantExpression constant) {
				if (AdditionSimplifier.this.constantIndex != -1) {
					AdditionSimplifier.this.constantSum -= constant.getValue();
					AdditionSimplifier.this.summands.set(AdditionSimplifier.this.constantIndex,
						ConstantExpression.forValue(AdditionSimplifier.this.constantSum));
				} else {
					AdditionSimplifier.this.constantSum = -constant.getValue();
					AdditionSimplifier.this.summands.add(0, ConstantExpression.forValue(-constant.getValue()));
					AdditionSimplifier.this.index++;
				}
			}
		}
	}
}
