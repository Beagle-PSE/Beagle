package de.uka.ipd.sdq.beagle.measurement.kieker.instrumentation;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.ContinueStatement;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.ThrowStatement;

/**
 * Determines whether an AST node contains a statement that could leave the current
 * control flow, like a {@link ReturnStatement} or {@link ThrowStatement}.
 *
 * @author Joshua Gleitze
 */
public class BreakingStatementDetector {

	/**
	 * The {@link Detector} we’ll use.
	 */
	private final Detector detector = new Detector();

	/**
	 * Determines wether the given {@code node} contains a statement that would break the
	 * control flow.
	 *
	 * @param node The node to check.
	 * @return {@code true} iff {@code node} has a child node that could prevent the
	 *         statement coming after it from being executed.
	 */
	public boolean containsControlFlowBreakingStatement(final ASTNode node) {
		this.detector.found = false;
		node.accept(this.detector);
		return this.detector.found;
	}

	/**
	 * Visits the node to be analysed in order to find control flow breaking statements.
	 * Populates its {@link #found} field to indicate whether such a statement was found.
	 * The following statements are considered to break the control flow:
	 *
	 * <ul>
	 *
	 * <li>{@link ReturnStatement}
	 *
	 * <li>{@link ThrowStatement}
	 *
	 * <li>{@link BreakStatement}
	 *
	 * <li>{@link ContinueStatement}
	 *
	 * </ul>
	 *
	 * @author Joshua Gleitze
	 */
	private class Detector extends ASTVisitor {

		/**
		 * Will be set to {@code true} if a control flow breaking statement was found.
		 */
		private boolean found;

		@Override
		public boolean preVisit2(final ASTNode node) {
			// only keep looking while we did not find anything yet
			return !this.found;
		}

		/**
		 * Called by a visit method of a control flow breaking statement.
		 *
		 * @return {@code false}, to not visit child nodes.
		 */
		private boolean found() {
			this.found = true;
			return false;
		}

		@Override
		public boolean visit(final ReturnStatement node) {
			return this.found();
		}

		@Override
		public boolean visit(final ThrowStatement node) {
			return this.found();
		}

		@Override
		public boolean visit(final ContinueStatement node) {
			return this.found();
		}
	}
}
