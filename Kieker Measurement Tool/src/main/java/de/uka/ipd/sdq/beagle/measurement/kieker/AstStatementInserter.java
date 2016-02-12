package de.uka.ipd.sdq.beagle.measurement.kieker;

import org.apache.commons.lang3.Validate;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.StructuralPropertyDescriptor;

import java.util.List;

/**
 * Inserts statements into an Eclipse AST. To do so, it finds a parent AST node into which
 * statements can be added. If found, it adds a provided statement into that node, either
 * directly after or directly before the statement that contains the provided marker
 * statement.
 *
 * @author Joshua Gleitze
 */
class AstStatementInserter {

	/**
	 * A visitor performing the actual insertion.
	 */
	private final InsertionPositionFinder inserter = new InsertionPositionFinder();

	/**
	 * Inserts the provided {@code statement} before the provided {@code marker}. Tries to
	 * put the {@code statement} as close to the {@code marker} as possible.
	 *
	 * @param marker The statement before which {@code statement} shall be inserted.
	 * @param statement The statement to insert before {@code marker}.
	 * @return {@code true} if {@code statement} was inserted. {@code false} if there is
	 *         no point in the AST where {@code statement} could validly be inserted.
	 */
	boolean insertBefore(final ASTNode marker, final Statement statement) {
		Validate.notNull(marker);
		Validate.notNull(statement);
		// insert at the marker’s position.
		return this.inserter.insert(marker, statement, 0);
	}

	/**
	 * Inserts the provided {@code statement} after the provided {@code marker}. Tries to
	 * put the {@code statement} as close to the {@code marker} as possible.
	 *
	 * @param marker The statement after which {@code statement} shall be inserted.
	 * @param statement The statement to insert after {@code marker}.
	 * @return {@code true} if {@code statement} was inserted. {@code false} if there is
	 *         no point in the AST where {@code statement} could validly be inserted.
	 */
	boolean insertAfter(final ASTNode marker, final Statement statement) {
		Validate.notNull(marker);
		Validate.notNull(statement);
		// insert 1 after the marker’s position.
		return this.inserter.insert(marker, statement, 1);
	}

	/**
	 * Looks for the right position to insert the statement. Does so by traversing upwards
	 * from the marker statement until an AST node to insert at is success. Throws an
	 * exception if there is no such AST node.
	 *
	 * @author Joshua Gleitze
	 */
	private class InsertionPositionFinder extends NotRecursingAstVisitor {

		/**
		 * The node we’re momentary searching an inserting point for.
		 */
		private ASTNode childToSearch;

		/**
		 * The statement we’ll insert.
		 */
		private Statement statementToInsert;

		/**
		 * The offset for insertion.
		 */
		private int offset;

		/**
		 * Indicates whether we managed to insert.
		 */
		private boolean success;

		/**
		 * Inserts {@code statement} next to {@code marker}, as far off as defined by
		 * {@code offset}.
		 *
		 * @param marker The ast node indicationg where to insert.
		 * @param statement The statement to insert.
		 * @param offsetToMarker The offset, indicating how far off the statement as to be
		 *            inserted. 0 is directly before, 1 is directly after.
		 * @return Whether insertion was successful.
		 */
		private boolean insert(final ASTNode marker, final Statement statement, final int offsetToMarker) {
			this.success = false;
			this.childToSearch = marker;
			this.statementToInsert = statement;
			this.offset = offsetToMarker;
			marker.getParent().accept(this);
			return this.success;
		}

		@Override
		public void postVisit(final ASTNode node) {
			if (!this.success) {
				this.childToSearch = node;
				final ASTNode nextTry = node.getParent();
				if (nextTry != null) {
					nextTry.accept(this);
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

			listToInsertIn.add(listToInsertIn.indexOf(this.childToSearch) + this.offset, this.statementToInsert);

			this.success = true;
			return false;
		}
	}

}
