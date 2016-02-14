package de.uka.ipd.sdq.beagle.measurement.kieker.instrumentation;

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
public class EclipseStatementCreationHelper {

	/**
	 * The {@link AST} instance implementors must use to generate AST nodes.
	 */
	private final AST factory;

	/**
	 * Create a helper that will create nodes using the provided {@code factory}.
	 *
	 * @param factory The AST to use.
	 */
	public EclipseStatementCreationHelper(final AST factory) {
		this.factory = factory;
	}

	/**
	 * Returns a {@link Name} that represents the qualified name consisting of the given
	 * {@code parts}.
	 *
	 * @param parts The parts building the name (each dot-separated element is one
	 *            parameter). Must contain at least one element.
	 * @return The constructed name.
	 */
	public Name getName(final String... parts) {
		Name lastName = this.factory.newSimpleName(parts[0]);

		for (int i = 1; i < parts.length; i++) {
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
}
