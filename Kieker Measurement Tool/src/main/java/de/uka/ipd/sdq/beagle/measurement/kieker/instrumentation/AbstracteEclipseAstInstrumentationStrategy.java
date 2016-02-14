package de.uka.ipd.sdq.beagle.measurement.kieker.instrumentation;

import de.uka.ipd.sdq.beagle.core.CodeSection;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.Statement;

import java.util.Arrays;
import java.util.List;

/**
 * Convenience class to easily generate instrumentation code.
 *
 * @author Joshua Gleitze
 */
public abstract class AbstracteEclipseAstInstrumentationStrategy implements EclipseAstInstrumentationStrategy {

	/**
	 * The {@link AST} instance implementors must use to generate AST nodes.
	 */
	protected AST factory;

	@Override
	public final Statement instrumentStart(final CodeSection codeSection, final AST nodeFactory) {
		this.factory = nodeFactory;
		return this.getBeforeStatement(codeSection);
	}

	@Override
	public final Statement instrumentEnd(final CodeSection codeSection, final AST nodeFactory) {
		this.factory = nodeFactory;
		return this.getAfterStatement(codeSection);
	}

	/**
	 * Returns a {@link Name} that represents the qualified name consisting of the given
	 * {@code parts}.
	 *
	 * @param parts The parts building the name (each dot-separated element is one
	 *            parameter). Must contain at least one element.
	 * @return The constructed name.
	 */
	protected Name getName(final String... parts) {
		Name lastName = this.factory.newSimpleName(parts[parts.length - 1]);

		// we subtract by 2 because we’re counting 0 based and already took the last
		// element.
		// CHECKSTYLE:IGNORE MagicNumber
		for (int i = parts.length - 2; i >= 0; i--) {
			lastName = this.getName(lastName, parts[i]);
		}
		return lastName;
	}

	/**
	 * Returns a {@link Name} that starts with an existing {@code name} and continues with
	 * {@code next}.
	 *
	 * @param name An exesting name.
	 * @param next The part to continue {@code name} with. Must not be {@code null}.
	 * @return The name that starts with {@code name} and continues with {@code next}.
	 *         Must not be {@code null}.
	 */
	protected Name getName(final Name name, final String next) {
		return this.factory.newQualifiedName(name, this.factory.newSimpleName(next));
	}

	/**
	 * Wraps the given {@code statements} into a {@link Block}.
	 *
	 * @param statements Statements to wrap.
	 * @return A block containing the {@code statements}. The provided statement if
	 *         {@code statements} contains only one statements. {@code null} if
	 *         {@code statements} is null.
	 */
	protected Statement wrap(final Statement[] statements) {
		if (statements == null || statements.length == 0) {
			return null;
		}
		return this.wrap(Arrays.asList(statements));
	}

	/**
	 * Wraps the given {@code statements} into a {@link Block}.
	 *
	 * @param statements Statements to wrap.
	 * @return A block containing the {@code statements}. The provided statement if
	 *         {@code statements} contains only one statements. {@code null} if
	 *         {@code statements} is null.
	 */
	@SuppressWarnings("unchecked")
	protected Statement wrap(final List<Statement> statements) {
		if (statements != null && statements.size() > 0) {
			final Block block = this.factory.newBlock();
			block.statements().addAll(statements);
			return block;
		} else if (statements.size() == 1) {
			return statements.get(0);
		}
		return null;
	}

	/**
	 * Method to be provided by implementor. Must use {@link #factory} to generate the
	 * statement that’s to be inserted before the provided {@code block}.
	 *
	 * @param codeSection The code section to be instrumented.
	 * @return The statement to insert before the {@code codeSection}.
	 */
	protected abstract Statement getBeforeStatement(final CodeSection codeSection);

	/**
	 * Method to be provided by implementor. Must use {@link #factory} to generate the
	 * statement that’s to be inserted after the provided {@code block}.
	 *
	 * @param codeSection The code section to be instrumented.
	 * @return The statement to insert after the {@code codeSection}.
	 */
	protected abstract Statement getAfterStatement(final CodeSection codeSection);

}
