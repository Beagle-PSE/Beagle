package de.uka.ipd.sdq.beagle.gui.handlers;

import de.uka.ipd.sdq.beagle.gui.GuiController;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the context menu entries, that start an analysis of the whole project.
 *
 * @author Roman Langrehr
 * @author Christoph Michelbach
 */
public class DemoBeagleContextMenuEntryHandlerForRepositories extends AbstractHandler {

	/**
	 * RegEx for the file extensions, where the context menu entry should be displayed.
	 */
	private static final String FILE_EXTENSION_MATCHER = "repository|repository_diagram";

	public static final String SPECIAL_ID_COMPLETE_REPOSITORY = "Analyse complete repository";

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final ISelection selection = HandlerUtil.getActiveMenuSelection(event);
		final IStructuredSelection structuredSelection = (IStructuredSelection) selection;

		final Object firstElement = structuredSelection.getFirstElement();
		assert firstElement instanceof IFile;
		final IFile clickedFile = (IFile) firstElement;
		final IPath clickedFilePath = clickedFile.getFullPath();
		assert clickedFilePath.getFileExtension().matches(FILE_EXTENSION_MATCHER);
		final IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);

		// create a new GUI and open it
		final List<String> ids = new ArrayList<>();
		ids.add(SPECIAL_ID_COMPLETE_REPOSITORY + " $" + clickedFilePath.toString());
		final GuiController guiController = new GuiController(ids);
		guiController.open();
		return null;
	}

}
