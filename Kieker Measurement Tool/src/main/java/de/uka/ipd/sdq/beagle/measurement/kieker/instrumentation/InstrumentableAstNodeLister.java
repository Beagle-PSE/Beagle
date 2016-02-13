package de.uka.ipd.sdq.beagle.measurement.kieker.instrumentation;

import org.apache.commons.lang3.Validate;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.AnnotationTypeMemberDeclaration;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.ArrayAccess;
import org.eclipse.jdt.core.dom.ArrayCreation;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.AssertStatement;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BlockComment;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.CastExpression;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.Comment;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.ContinueStatement;
import org.eclipse.jdt.core.dom.CreationReference;
import org.eclipse.jdt.core.dom.Dimension;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EmptyStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.ExpressionMethodReference;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.Initializer;
import org.eclipse.jdt.core.dom.InstanceofExpression;
import org.eclipse.jdt.core.dom.IntersectionType;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.LabeledStatement;
import org.eclipse.jdt.core.dom.LambdaExpression;
import org.eclipse.jdt.core.dom.LineComment;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.MemberRef;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.MethodRef;
import org.eclipse.jdt.core.dom.MethodRefParameter;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.NameQualifiedType;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.NullLiteral;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.QualifiedType;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.SuperFieldAccess;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.eclipse.jdt.core.dom.SuperMethodReference;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.SynchronizedStatement;
import org.eclipse.jdt.core.dom.TagElement;
import org.eclipse.jdt.core.dom.TextElement;
import org.eclipse.jdt.core.dom.ThisExpression;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclarationStatement;
import org.eclipse.jdt.core.dom.TypeLiteral;
import org.eclipse.jdt.core.dom.TypeMethodReference;
import org.eclipse.jdt.core.dom.TypeParameter;
import org.eclipse.jdt.core.dom.UnionType;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;
import org.eclipse.jdt.core.dom.WildcardType;

import java.util.HashMap;
import java.util.Map;

/**
 * Lists all instrumentable AST nodes found in an AST with their source code position.
 *
 * <p>An AST node is considered instrumentable if it can either somehow be executed or
 * forms a branching or looping construct. This includes most
 *
 * {@linkplain Statement statements}, and also most {@link Expression expressions}.
 * Examples of AST nodes that are <em>not</em> considered executable:
 *
 * <ul>
 *
 * <li>{@link Annotation}
 *
 * <li>{@link Comment}
 *
 * <li>{@link BodyDeclaration}
 *
 * </ul>
 *
 * <p>Some statements and expressions don’t actually fit into the description above, but
 * are added to the result anyway. This is because they may easily be declared as “to be
 * instrumented” while in fact an inner expression is meant. An example is a
 * {@link VariableDeclarationExpression} or {@link VariableDeclarationFragment}. While the
 * actual assignement is instrumentable code, one might easily assigne “the whole line”
 * for instrumentation. Adding these to the returned map helps preventing such
 * instrumentation instructions to be wongly assigned to previous statements.
 *
 * <p>There may occur situations in which an instrumentable parent node shares the same
 * start character as an instrumentable child node. In such situations, the deepest
 * (a.k.a. “shortest”) node will be assigned for this character position.
 *
 * @author Joshua Gleitze
 */
class InstrumentableAstNodeLister {

	/**
	 * Will visit the AST to find all nodes.
	 */
	private final AstWalker astWalker = new AstWalker();

	/**
	 * Will contain the result.
	 */
	private Map<Integer, ASTNode> resultMap;

	/**
	 * Returns a mapping of character positions to instrumentable statements for all
	 * instrumentable statements found in the provided {@code compilationUnit}.
	 *
	 * @param compilationUnit The compilaltion unit to search for statements in. Must not
	 *            be {@code null}.
	 * @return The mapping from source file character positions to instrumentable
	 *         statements.
	 */
	public Map<Integer, ASTNode> getStatementIndicesIn(final CompilationUnit compilationUnit) {
		Validate.notNull(compilationUnit);
		this.resultMap = new HashMap<>();
		compilationUnit.accept(this.astWalker);
		return this.resultMap;
	}

	/*
	 * This visitor implements all methods possible. This is because the author thinks
	 * it’s relevant to document their thinking for every ASTNode type. Splitting the
	 * class is not possible (without harming fundamental OOP principles). Thus, the
	 * class’ fan out complexity is acceptable and the according Checkstyle rule was
	 * disabled.
	 */
	/**
	 * Visitor of the passed AST. Decides for each node whether it’s instrumentable and
	 * whether it may contain visitable nodes. Takes according action.
	 *
	 * @author Joshua Gleitze
	 */
	// CHECKSTYLE:IGNORE ClassFanOutComplexity
	private class AstWalker extends ASTVisitor {

		/**
		 * Common visit delegation method for all instrumentable nodes.
		 *
		 * @param node An instrumentable node.
		 * @return {@code true}.
		 */
		private boolean visitInstrumentable(final ASTNode node) {
			InstrumentableAstNodeLister.this.resultMap.put(node.getStartPosition(), node);
			return true;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
		 * AnnotationTypeDeclaration)
		 */
		@Override
		public boolean visit(final AnnotationTypeDeclaration node) {
			// not instrumentable, contains no instrumentable statements.
			return false;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
		 * AnnotationTypeMemberDeclaration)
		 */
		@Override
		public boolean visit(final AnnotationTypeMemberDeclaration node) {
			// will never be visited
			assert false;
			return false;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
		 * AnonymousClassDeclaration)
		 */
		@Override
		public boolean visit(final AnonymousClassDeclaration node) {
			// not instrumentable, may contain instrumentable statements.
			return true;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.ArrayAccess)
		 */
		@Override
		public boolean visit(final ArrayAccess node) {
			return this.visitInstrumentable(node);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
		 * ArrayCreation)
		 */
		@Override
		public boolean visit(final ArrayCreation node) {
			return this.visitInstrumentable(node);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
		 * ArrayInitializer)
		 */
		@Override
		public boolean visit(final ArrayInitializer node) {
			return this.visitInstrumentable(node);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.ArrayType)
		 */
		@Override
		public boolean visit(final ArrayType node) {
			// not instrumentable, contains no instrumentable statements.
			return false;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
		 * AssertStatement)
		 */
		@Override
		public boolean visit(final AssertStatement node) {
			return this.visitInstrumentable(node);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.Assignment)
		 */
		@Override
		public boolean visit(final Assignment node) {
			return this.visitInstrumentable(node);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.Block)
		 */
		@Override
		public boolean visit(final Block node) {
			// not instrumentable, may contain instrumentable nodes
			return true;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
		 * BlockComment)
		 */
		@Override
		public boolean visit(final BlockComment node) {
			// not instrumentable, contains no instrumentable nodes
			return false;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
		 * BooleanLiteral)
		 */
		@Override
		public boolean visit(final BooleanLiteral node) {
			// not instrumentable, contains no instrumentable nodes
			return false;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
		 * BreakStatement)
		 */
		@Override
		public boolean visit(final BreakStatement node) {
			return this.visitInstrumentable(node);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
		 * CastExpression)
		 */
		@Override
		public boolean visit(final CastExpression node) {
			// not instrumentable, may contain instrumentable nodes
			return true;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.CatchClause)
		 */
		@Override
		public boolean visit(final CatchClause node) {
			// not instrumentable, may contain instrumentable nodes
			return true;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
		 * CharacterLiteral)
		 */
		@Override
		public boolean visit(final CharacterLiteral node) {
			// not instrumentable, contains no instrumentable nodes
			return false;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
		 * ClassInstanceCreation)
		 */
		@Override
		public boolean visit(final ClassInstanceCreation node) {
			return this.visitInstrumentable(node);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
		 * CompilationUnit)
		 */
		@Override
		public boolean visit(final CompilationUnit node) {
			// not instrumentable, may contain instrumentable nodes
			return true;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
		 * ConditionalExpression)
		 */
		@Override
		public boolean visit(final ConditionalExpression node) {
			return this.visitInstrumentable(node);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
		 * ConstructorInvocation)
		 */
		@Override
		public boolean visit(final ConstructorInvocation node) {
			return this.visitInstrumentable(node);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
		 * ContinueStatement)
		 */
		@Override
		public boolean visit(final ContinueStatement node) {
			return this.visitInstrumentable(node);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
		 * CreationReference)
		 */
		@Override
		public boolean visit(final CreationReference node) {
			return this.visitInstrumentable(node);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.Dimension)
		 */
		@Override
		public boolean visit(final Dimension node) {
			// not instrumentable, contains no instrumentable nodes
			return false;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.DoStatement)
		 */
		@Override
		public boolean visit(final DoStatement node) {
			return this.visitInstrumentable(node);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
		 * EmptyStatement)
		 */
		@Override
		public boolean visit(final EmptyStatement node) {
			// not instrumentable, contains nothing
			return false;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
		 * EnhancedForStatement)
		 */
		@Override
		public boolean visit(final EnhancedForStatement node) {
			return this.visitInstrumentable(node);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
		 * EnumConstantDeclaration)
		 */
		@Override
		public boolean visit(final EnumConstantDeclaration node) {
			// not instrumentable, contains no instrumentable nodes
			return false;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
		 * EnumDeclaration)
		 */
		@Override
		public boolean visit(final EnumDeclaration node) {
			// not instrumentable,may contain instrumentable nodes
			return true;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
		 * ExpressionMethodReference)
		 */
		@Override
		public boolean visit(final ExpressionMethodReference node) {
			return this.visitInstrumentable(node);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
		 * ExpressionStatement)
		 */
		@Override
		public boolean visit(final ExpressionStatement node) {
			// We’ll use the contained expression instead
			return true;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.FieldAccess)
		 */
		@Override
		public boolean visit(final FieldAccess node) {
			return this.visitInstrumentable(node);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
		 * FieldDeclaration)
		 */
		@Override
		public boolean visit(final FieldDeclaration node) {
			// not instrumentable, may contain instrumentable nodes
			return true;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
		 * ForStatement)
		 */
		@Override
		public boolean visit(final ForStatement node) {
			return this.visitInstrumentable(node);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.IfStatement)
		 */
		@Override
		public boolean visit(final IfStatement node) {
			return this.visitInstrumentable(node);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
		 * ImportDeclaration)
		 */
		@Override
		public boolean visit(final ImportDeclaration node) {
			// not instrumentable, contains no instrumentable nodes
			return false;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
		 * InfixExpression)
		 */
		@Override
		public boolean visit(final InfixExpression node) {
			return this.visitInstrumentable(node);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.Initializer)
		 */
		@Override
		public boolean visit(final Initializer node) {
			// not instrumentable, may contain instrumemtable nodes
			return true;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
		 * InstanceofExpression)
		 */
		@Override
		public boolean visit(final InstanceofExpression node) {
			return this.visitInstrumentable(node);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
		 * IntersectionType)
		 */
		@Override
		public boolean visit(final IntersectionType node) {
			// not instrumentable, contains no instrumentable nodes
			return false;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.Javadoc)
		 */
		@Override
		public boolean visit(final Javadoc node) {
			// not instrumentable, contains no instrumentable nodes
			return false;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
		 * LabeledStatement)
		 */
		@Override
		public boolean visit(final LabeledStatement node) {
			// not instrumentable, may contain instrumentable nodes
			return true;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
		 * LambdaExpression)
		 */
		@Override
		public boolean visit(final LambdaExpression node) {
			return this.visitInstrumentable(node);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.LineComment)
		 */
		@Override
		public boolean visit(final LineComment node) {
			// not instrumentable, contains no instrumentable nodes
			return false;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
		 * MarkerAnnotation)
		 */
		@Override
		public boolean visit(final MarkerAnnotation node) {
			// not instrumentable, contains no instrumentable nodes
			return false;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.MemberRef)
		 */
		@Override
		public boolean visit(final MemberRef node) {
			// will never be visited
			assert false;
			return false;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
		 * MemberValuePair)
		 */
		@Override
		public boolean visit(final MemberValuePair node) {
			// will never be visited
			assert false;
			return false;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
		 * MethodDeclaration)
		 */
		@Override
		public boolean visit(final MethodDeclaration node) {
			// not instrumentable, may contain instrumentable nodes
			return true;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
		 * MethodInvocation)
		 */
		@Override
		public boolean visit(final MethodInvocation node) {
			return this.visitInstrumentable(node);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.MethodRef)
		 */
		@Override
		public boolean visit(final MethodRef node) {
			// will never be visited
			assert false;
			return false;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
		 * MethodRefParameter)
		 */
		@Override
		public boolean visit(final MethodRefParameter node) {
			// will never be visited
			assert false;
			return false;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.Modifier)
		 */
		@Override
		public boolean visit(final Modifier node) {
			// not instrumentable, contains no instrumentable nodes
			return false;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
		 * NameQualifiedType)
		 */
		@Override
		public boolean visit(final NameQualifiedType node) {
			// not instrumentable, contains no instrumentable nodes
			return false;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
		 * NormalAnnotation)
		 */
		@Override
		public boolean visit(final NormalAnnotation node) {
			// not instrumentable, contains no instrumentable nodes
			return false;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.NullLiteral)
		 */
		@Override
		public boolean visit(final NullLiteral node) {
			// not instrumentable, contains no instrumentable nodes
			return false;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
		 * NumberLiteral)
		 */
		@Override
		public boolean visit(final NumberLiteral node) {
			// not instrumentable, contains no instrumentable nodes
			return false;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
		 * PackageDeclaration)
		 */
		@Override
		public boolean visit(final PackageDeclaration node) {
			// not instrumentable, contains no instrumentable nodes
			return false;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
		 * ParameterizedType)
		 */
		@Override
		public boolean visit(final ParameterizedType node) {
			// not instrumentable, contains no instrumentable nodes
			return false;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
		 * ParenthesizedExpression)
		 */
		@Override
		public boolean visit(final ParenthesizedExpression node) {
			// not instrumentable, may contain instrumentable nodes
			return true;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
		 * PostfixExpression)
		 */
		@Override
		public boolean visit(final PostfixExpression node) {
			// not instrumentable, may contain instrumentable nodes
			return true;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
		 * PrefixExpression)
		 */
		@Override
		public boolean visit(final PrefixExpression node) {
			// not instrumentable, may contain instrumentable nodes
			return true;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
		 * PrimitiveType)
		 */
		@Override
		public boolean visit(final PrimitiveType node) {
			// not instrumentable, contains no instrumentable nodes
			return false;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
		 * QualifiedName)
		 */
		@Override
		public boolean visit(final QualifiedName node) {
			// not instrumentable, contains no instrumentable nodes
			return false;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
		 * QualifiedType)
		 */
		@Override
		public boolean visit(final QualifiedType node) {
			// not instrumentable, contains no instrumentable nodes
			return false;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
		 * ReturnStatement)
		 */
		@Override
		public boolean visit(final ReturnStatement node) {
			return this.visitInstrumentable(node);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.SimpleName)
		 */
		@Override
		public boolean visit(final SimpleName node) {
			// not instrumentable, contains no instrumentable nodes
			return false;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.SimpleType)
		 */
		@Override
		public boolean visit(final SimpleType node) {
			// not instrumentable, contains no instrumentable nodes
			return false;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
		 * SingleMemberAnnotation)
		 */
		@Override
		public boolean visit(final SingleMemberAnnotation node) {
			// not instrumentable, contains no instrumentable nodes
			return false;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
		 * SingleVariableDeclaration)
		 */
		@Override
		public boolean visit(final SingleVariableDeclaration node) {
			// not instrumentable, contains no instrumentable nodes
			return false;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
		 * StringLiteral)
		 */
		@Override
		public boolean visit(final StringLiteral node) {
			// not instrumentable, contains no instrumentable nodes
			return false;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
		 * SuperConstructorInvocation)
		 */
		@Override
		public boolean visit(final SuperConstructorInvocation node) {
			return this.visitInstrumentable(node);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
		 * SuperFieldAccess)
		 */
		@Override
		public boolean visit(final SuperFieldAccess node) {
			// not instrumentable, contains no instrumentable nodes
			return false;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
		 * SuperMethodInvocation)
		 */
		@Override
		public boolean visit(final SuperMethodInvocation node) {
			return this.visitInstrumentable(node);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
		 * SuperMethodReference)
		 */
		@Override
		public boolean visit(final SuperMethodReference node) {
			return this.visitInstrumentable(node);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.SwitchCase)
		 */
		@Override
		public boolean visit(final SwitchCase node) {
			return this.visitInstrumentable(node);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
		 * SwitchStatement)
		 */
		@Override
		public boolean visit(final SwitchStatement node) {
			return this.visitInstrumentable(node);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
		 * SynchronizedStatement)
		 */
		@Override
		public boolean visit(final SynchronizedStatement node) {
			// not instrumentable, may contain instrumentable nodes
			return true;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.TagElement)
		 */
		@Override
		public boolean visit(final TagElement node) {
			// will never be called
			assert false;
			return false;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.TextElement)
		 */
		@Override
		public boolean visit(final TextElement node) {
			// will never be called
			assert false;
			return false;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
		 * ThisExpression)
		 */
		@Override
		public boolean visit(final ThisExpression node) {
			// not instrumentable, contains no instrumentable nodes
			return false;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
		 * ThrowStatement)
		 */
		@Override
		public boolean visit(final ThrowStatement node) {
			return this.visitInstrumentable(node);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
		 * TryStatement)
		 */
		@Override
		public boolean visit(final TryStatement node) {
			// not instrumentable, may contain instrumentable nodes
			return true;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
		 * TypeDeclaration)
		 */
		@Override
		public boolean visit(final TypeDeclaration node) {
			// not instrumentable, may contain instrumentable nodes
			return true;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
		 * TypeDeclarationStatement)
		 */
		@Override
		public boolean visit(final TypeDeclarationStatement node) {
			// not instrumentable, may contain instrumentable nodes
			return true;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.TypeLiteral)
		 */
		@Override
		public boolean visit(final TypeLiteral node) {
			return this.visitInstrumentable(node);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
		 * TypeMethodReference)
		 */
		@Override
		public boolean visit(final TypeMethodReference node) {
			return this.visitInstrumentable(node);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
		 * TypeParameter)
		 */
		@Override
		public boolean visit(final TypeParameter node) {
			// not instrumentable, contains no instrumentable nodes
			return false;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.UnionType)
		 */
		@Override
		public boolean visit(final UnionType node) {
			// not instrumentable, contains no instrumentable nodes
			return false;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
		 * VariableDeclarationExpression)
		 */
		@Override
		public boolean visit(final VariableDeclarationExpression node) {
			return this.visitInstrumentable(node);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
		 * VariableDeclarationFragment)
		 */
		@Override
		public boolean visit(final VariableDeclarationFragment node) {
			return this.visitInstrumentable(node);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
		 * VariableDeclarationStatement)
		 */
		@Override
		public boolean visit(final VariableDeclarationStatement node) {
			return this.visitInstrumentable(node);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
		 * WhileStatement)
		 */
		@Override
		public boolean visit(final WhileStatement node) {
			return this.visitInstrumentable(node);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
		 * WildcardType)
		 */
		@Override
		public boolean visit(final WildcardType node) {
			// not instrumentable, contains no instrumentable nodes
			return false;
		}
	}

}
