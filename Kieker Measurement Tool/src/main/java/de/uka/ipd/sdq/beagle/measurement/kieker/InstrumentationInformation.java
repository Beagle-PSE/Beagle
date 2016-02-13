package de.uka.ipd.sdq.beagle.measurement.kieker;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Statement;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Data class for instrumentation instruction functions. Stores functions that, given an
 * {@link AST} return a {@link Statement} that shall be used for instrumentation. The
 * class stores the statements to insert before and after an AST node.
 *
 * <p>The class stores the provided function in the order they were added. To do so, it
 * puts the before functions in a queue, but the after functions on a stack. This assures
 * that pairs of instrumentation instructions are executed in the desired order: The
 * instrumentation that started first will be ended last.
 *
 * <p>The provided functions are evaluated as lazily as possible.
 *
 * @author Joshua Gleitze
 */
class InstrumentationInformation {

	/**
	 * Suppliers for the nodes to be inserted before an instrumented code section. The
	 * nodes shall be inserted in the order they are in the list.
	 */
	private final Queue<Function<AST, Statement>> beforeStatement = new LinkedList<>();

	/**
	 * Suppliers for the nodes to be inserted after an instrumented code section. The
	 * nodes shall be inserted in the order they are on the stack.
	 */
	private final Stack<Function<AST, Statement>> afterStatement = new Stack<>();

	/**
	 * Combines two instances of instrumentation information into one. This process means
	 * that both information instances apply to the same node. Merging is done in a way
	 * that the instructions of {@code first} will be at the start of the before list / at
	 * the end of the after stack and {@code last} will be at the other end, respectively.
	 *
	 * @param first The information instance whose instructions come first. May be
	 *            {@code null}.
	 * @param second The information instance whose instructions come last. May not be
	 *            {@code null}.
	 * @return A new information instance that merges {@code first} and {@code last}.
	 */
	static InstrumentationInformation merge(final InstrumentationInformation first,
		final InstrumentationInformation second) {
		if (first == null) {
			return second;
		}
		final InstrumentationInformation result = new InstrumentationInformation();
		result.beforeStatement.addAll(first.beforeStatement);
		result.beforeStatement.addAll(second.beforeStatement);
		result.afterStatement.addAll(first.afterStatement);
		result.afterStatement.addAll(second.afterStatement);
		return result;
	}

	/**
	 * Registers {@code beforeStatementProvider} as a provider of statements that shall be
	 * used to instrument before an AST node.
	 *
	 * @param beforeStatementProvider A function that returns a statement to instrument
	 *            before an AST node.
	 */
	void addBeforeStatementFunction(final Function<AST, Statement> beforeStatementProvider) {
		this.beforeStatement.add(beforeStatementProvider);
	}

	/**
	 * Registers {@code afterStatementProvider} as a provider of statements that shall be
	 * used to instrument after an AST node.
	 *
	 * @param afterStatementProvider A function that returns a statement to instrument
	 *            after an AST node.
	 */
	void addAfterStatementFunction(final Function<AST, Statement> afterStatementProvider) {
		this.beforeStatement.add(afterStatementProvider);
	}

	/**
	 * Gets a stream for the statements that are provided by the registered functions for
	 * instrumentation before a node. The stream will never contain {@code null}, but
	 * might be empty even if there are before functions registered.
	 *
	 * @param functionAstFactory The {@link AST} to pass to the statement providing
	 *            functions.
	 * @return A stream for the statements the registered functions provided for
	 *         instrumentation before a node.
	 */
	Stream<Statement> getBeforeStatements(final AST functionAstFactory) {
		return this.resolve(this.beforeStatement, functionAstFactory);
	}

	/**
	 * Gets a stream for the statements that are provided by the registered functions for
	 * instrumentation after a node. The stream will never contain {@code null}, but might
	 * be empty even if there are after functions registered.
	 *
	 * @param functionAstFactory The {@link AST} to pass to the statement providing
	 *            functions.
	 * @return A stream for the statements the registered functions provided for
	 *         instrumentation after a node.
	 */
	Stream<Statement> getAfterStatements(final AST functionAstFactory) {
		return this.resolve(this.afterStatement, functionAstFactory);
	}

	/**
	 * Resolves {@code providers} to the statements they provide.
	 *
	 * @param providers A collection of functions that provide statements when being
	 *            passed an {@link AST}.
	 * @param functionAstFactory The {@link AST} to give to the {@code providers}.
	 * @return A stream for the statements the {@code providers} provided.
	 */
	private Stream<Statement> resolve(final Collection<Function<AST, Statement>> providers,
		final AST functionAstFactory) {
		return providers.stream()
			.map((function) -> function.apply(functionAstFactory))
			.filter((statement) -> statement != null);
	}
}
