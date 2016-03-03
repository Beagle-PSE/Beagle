package de.uka.ipd.sdq.beagle.core.evaluableexpressions.util;

import de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpression;

/**
 * An evaluable expression tree walker allowing to ignore certain subtrees. The class
 * offers a mechanism to stop visiting of inner expressions, forcing the visitor not to
 * traverse the expression tree any further.
 *
 * @author Joshua Gleitze
 */
public abstract class PartialExpressionTreeWalker extends ExpressionTreeWalker {

	/**
	 * Stores whether subtrees shall be traversed.
	 */
	private boolean doTraverseSubtrees;

	/**
	 * Stops visiting of inner expressions. As soon as this method is called, this visitor
	 * will no longer visit inner expressions. This means that only {@code after} hooks of
	 * already visited expressions will be called until the root is reached.
	 *
	 * @see #startTraversingInnerExpressions()
	 */
	protected void stopTraversingInnerExpressions() {
		this.doTraverseSubtrees = false;
	}

	/**
	 * Starts visiting of inner expressions. Inner expressions will be traverse
	 * depth-first as long as it is activated.
	 *
	 * @see #stopTraversingInnerExpressions()
	 */
	protected void startTraversingInnerExpressions() {
		this.doTraverseSubtrees = true;
	}

	/**
	 * Queries whether the visitor will visit inner expressions. Will return {@code true}
	 * unless {@link #stopTraversingInnerExpressions()} is called. The value will be reset
	 * when calling {@link #visitRecursively(EvaluableExpression)}.
	 *
	 * @return Whether inner expressions will be examined for the momentary tree.
	 */
	protected boolean willTraverseInnerExpressions() {
		return this.doTraverseSubtrees;
	}

}
