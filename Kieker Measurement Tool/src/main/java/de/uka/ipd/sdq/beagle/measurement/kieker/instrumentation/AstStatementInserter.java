package de.uka.ipd.sdq.beagle.measurement.kieker.instrumentation;

import org.apache.commons.lang3.Validate;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.StructuralPropertyDescriptor;
import org.eclipse.jdt.core.dom.TryStatement;

import java.util.List;
import java.util.Optional;

/**
 * Inserts statements into an Eclipse AST. To do so, it finds a parent AST node into which
 * statements can be added. If found, it adds a provided statement into that node, either
 * directly after or directly before the node that contains node this inserter was created
 * for.
 *
 * <p>The inserter needs to be set up. Setting up determines all information required to
 * insert. Trying to insert before setting up or if setting up did not succeed results in
 * an {@link IllegalStateException} being thrown. No modifications may be made to the AST
 * through any other party between setting up and insertions (not even by another
 * inserter).
 *
 * @author Joshua Gleitze
 */
class AstStatementInserter {

	/**
	 * The node marking where to insert. We will insert around the statement containing
	 * this node.
	 */
	private final ASTNode marker;

	/**
	 * The statement containing {@code marker}, around which we’re going to insert.
	 */
	private Statement markedStatement;

	/**
	 * The list to insert statements in. Will be present iff an insertion point was found.
	 */
	private Optional<List<Statement>> insertionList;

	/**
	 * The list to insert statements in iff inserting after {@link #marker} and
	 * {@link #differentAfterList} is {@code true}.
	 */
	private List<Statement> afterList;

	/**
	 * Index of the statement containing {@link #marker} in {@link #insertionList}.
	 */
	private int markerIndex;

	/**
	 * Whether statements that shall be inserted after {@link #marker} have to be inserted
	 * into a different list.
	 */
	private boolean differentAfterList;

	/**
	 * Will be used to determine whether a statement could break the control flow.
	 */
	private final BreakingStatementDetector breakDetector = new BreakingStatementDetector();

	/**
	 * Creates an inserter that will insert statements around the provided {@code marker}.
	 *
	 * @param marker The node around which insertion is requested.
	 */
	AstStatementInserter(final ASTNode marker) {
		this.marker = marker;
	}

	/**
	 * Sets up this inserter. Setting up is required before statements can be inserted
	 * through it.
	 *
	 * @return {@code true} if setting up succeeded. {@code false} if there is no valid
	 *         insertion point above this inserter’s marker node.
	 */
	boolean setUp() {
		new InsertionPositionFinder().find();
		return this.insertionList.isPresent();
	}

	/**
	 * Inserts the provided {@code statement} before the node this inserter was created
	 * for. Tries to put the {@code statement} as close to the node as possible.
	 *
	 * @param statement The statement to insert before the insertion node.
	 */
	void insertBefore(final Statement statement) {
		Validate.notNull(statement);
		Validate.validState(this.insertionList != null,
			"Insertion is only possible after the inserter has been set up.");
		Validate.validState(this.insertionList.isPresent(),
			"Insertion is only possible if setting up the inserter succeeded.");

		this.insertionList.get().add(this.markerIndex, statement);
		this.markerIndex++;
	}

	/**
	 * Inserts the provided {@code statement} after the the node this inserter was created
	 * for. Tries to put the {@code statement} as close to the {@code marker} as possible.
	 *
	 * @param statement The statement to insert after the insertion node.
	 */
	void insertAfter(final Statement statement) {
		Validate.notNull(statement);
		Validate.validState(this.insertionList != null,
			"Insertion is only possible after the inserter has been set up.");
		Validate.validState(this.insertionList.isPresent(),
			"Insertion is only possible if setting up the inserter succeeded.");

		if (this.differentAfterList) {
			this.afterList.add(0, statement);
		} else if (this.breakDetector.containsControlFlowBreakingStatement(this.markedStatement)) {
			// we are trying to insert after a control flow breaking statement. That’s
			// dangerous, better surround with try…finally

			final AST factory = this.markedStatement.getAST();
			final TryStatement tryStatement = factory.newTryStatement();
			tryStatement.setFinally(factory.newBlock());

			@SuppressWarnings("unchecked")
			final List<Statement> tryBodyStatements = tryStatement.getBody().statements();
			@SuppressWarnings("unchecked")
			final List<Statement> finallyStatements = tryStatement.getFinally().statements();
			final Statement copy = (Statement) ASTNode.copySubtree(factory, this.markedStatement);
			tryBodyStatements.add(copy);
			finallyStatements.add(statement);
			this.insertionList.get().set(this.markerIndex, tryStatement);
			this.markedStatement = tryStatement;
			this.differentAfterList = true;
			this.afterList = finallyStatements;
		} else {
			this.insertionList.get().add(this.markerIndex, statement);
		}
	}

	/**
	 * Looks for the right position to insert statements. Does so by traversing upwards
	 * from the marker statement until an AST node to insert at is found.
	 *
	 * @author Joshua Gleitze
	 */
	private class InsertionPositionFinder extends NotRecursingAstVisitor {

		/**
		 * The node we’re momentary searching an inserting point for.
		 */
		private ASTNode childToSearch;

		/**
		 * Indicates whether we managed to insert.
		 */
		private boolean success;

		/**
		 * Searches for the insertion point and populates
		 * {@link AstStatementInserter#insertionStrategy}.
		 */
		private void find() {
			this.success = false;
			this.childToSearch = AstStatementInserter.this.marker;
			this.childToSearch.getParent().accept(this);
		}

		@Override
		public void postVisit(final ASTNode node) {
			if (!this.success) {
				this.childToSearch = node;
				final ASTNode nextTry = node.getParent();
				if (nextTry != null) {
					nextTry.accept(this);
				} else {
					// we did not find a point to insert and have nowhere else to look. We
					// failed.
					AstStatementInserter.this.insertionList = Optional.empty();
				}
			}
		}

		@Override
		public boolean visit(final Block node) {
			assert this.childToSearch.getParent() == node;

			final StructuralPropertyDescriptor propertyToInsertIn = this.childToSearch.getLocationInParent();
			assert propertyToInsertIn.isChildListProperty();

			@SuppressWarnings("unchecked")
			final List<Statement> listToInsertIn = (List<Statement>) node.getStructuralProperty(propertyToInsertIn);
			final int index = listToInsertIn.indexOf(this.childToSearch);

			// It’s a child of {@code node}, so it must be a Statement
			AstStatementInserter.this.markedStatement = (Statement) this.childToSearch;
			AstStatementInserter.this.insertionList = Optional.of(listToInsertIn);
			AstStatementInserter.this.markerIndex = index;

			this.success = true;
			return false;
		}
	}
}
