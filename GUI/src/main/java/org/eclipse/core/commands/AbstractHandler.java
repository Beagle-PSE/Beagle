package org.eclipse.core.commands;

/**
 * Exists only for reasons of dependency. Will be replaced by Eclipse
 * {@code AbstractHandler} during implementation.
 *
 * @author Roman Langrehr
 */
public abstract class AbstractHandler {

	/**
	 * Exists only for reasons of dependency. Will be replaced by Eclipse
	 * {@code AbstractHandler#execute(ExecutionEvent)} during implementation.
	 *
	 * @param event Exists only for reasons of dependency.
	 * @return Exists only for reasons of dependency.
	 * @throws ExecutionException Exists only for reasons of dependency.
	 */
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		return null;
	}

}
