package de.uka.ipd.sdq.beagle.measurement.kieker;

import org.apache.commons.lang3.Validate;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.StructuralPropertyDescriptor;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

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
	 * The node marking where to insert.
	 */
	private final ASTNode marker;

	/**
	 * An optional function capable of executing the insertion. Takes the statement to
	 * insert and an offset defining how far off to insert (0 for directly before, 1 for
	 * directly after). {@code null} indicates that we did not yet search the function. If
	 * the optional is not present, there is no such function.
	 */
	private Optional<BiConsumer<Statement, Integer>> insertionStrategy;

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
		return this.insertionStrategy.isPresent();
	}

	/**
	 * Inserts the provided {@code statement} before the node this inserter was created
	 * for. Tries to put the {@code statement} as close to the node as possible.
	 *
	 * @param statement The statement to insert before the insertion node.
	 */
	void insertBefore(final Statement statement) {
		Validate.notNull(statement);
		// insert at the marker’s position.
		this.insert(statement, 0);
	}

	/**
	 * Inserts the provided {@code statement} after the provided the node this inserter
	 * was created for. Tries to put the {@code statement} as close to the {@code marker}
	 * as possible.
	 *
	 * @param statement The statement to insert after the insertion node.
	 */
	void insertAfter(final Statement statement) {
		Validate.notNull(statement);
		// insert 1 after the marker’s position.
		this.insert(statement, 1);
	}

	/**
	 * Executes the insertion by first finding the insertion strategy and then executing
	 * it.
	 *
	 * @param statement The statement to insert.
	 * @param offset how far off to insert (0 for directly before, 1 for directly after).
	 */
	private void insert(final Statement statement, final int offset) {
		Validate.validState(this.insertionStrategy != null,
			"Insertion is only possible after the inserter has been set up.");
		Validate.validState(this.insertionStrategy.isPresent(),
			"Insertion is only possible if setting up the inserter succeeded.");
		this.insertionStrategy.get().accept(statement, offset);
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
		 * The index of the marker if inserting into a list.
		 */
		private int markerIndex;

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
					AstStatementInserter.this.insertionStrategy = Optional.empty();
				}
			}
		}

		@Override
		public boolean visit(final Block node) {
			assert this.childToSearch.getParent() == node;

			final StructuralPropertyDescriptor propertyToInsertIn = this.childToSearch.getLocationInParent();
			assert propertyToInsertIn.isChildListProperty();

			@SuppressWarnings("unchecked")
			final List<ASTNode> listToInsertIn = (List<ASTNode>) node.getStructuralProperty(propertyToInsertIn);
			this.markerIndex = listToInsertIn.indexOf(this.childToSearch);

			AstStatementInserter.this.insertionStrategy = Optional.of((statementToInsert, offset) -> {
				listToInsertIn.add(this.markerIndex + offset, statementToInsert);
				if (offset <= 0) {
					this.markerIndex++;
				}
			});

			this.success = true;
			return false;
		}
	}

}
