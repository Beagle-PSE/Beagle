package de.uka.ipd.sdq.beagle.measurement.kieker.instrumentation;

import de.uka.ipd.sdq.beagle.core.CodeSection;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Statement;

/**
 * Defines how to instrument a code section by providing AST statements to insert before
 * and after the section.
 *
 * @author Joshua Gleitze
 */
public interface EclipseAstInstrumentationStrategy {

	/**
	 * Provides a node to be inserted before the given {@code codeSection}. The statement
	 * returned is to be inserted before the first statement of the given
	 * {@code codeSection}.
	 *
	 * @param codeSection The section being instrumented.
	 * @param nodeFactory The {@linkplain AST} instance that must be used to create the
	 *            instrumentation statement.
	 * @return The statement to add directly before the code section. The returned
	 *         statement’s {@link Statement#getAST()} must return {@code nodeFactory}.
	 *         {@code null} to not insert anything.
	 */
	Statement instrumentStart(CodeSection codeSection, AST nodeFactory);

	/**
	 * Provides a node to be inserted after the given {@code codeSection}. The statement
	 * returned is to be inserted after the last statement of the given
	 * {@code codeSection}.
	 *
	 * @param codeSection The section being instrumented.
	 * @param nodeFactory The {@linkplain AST} instance that must be used to create the
	 *            instrumentation statement.
	 * @return The statement to add directly after the code section. The returned
	 *         statement’s {@link Statement#getAST()} must return {@code nodeFactory}.
	 *         {@code null} to not insert anything.
	 */
	Statement instrumentEnd(CodeSection codeSection, AST nodeFactory);
}
