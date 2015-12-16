package de.uka.ipd.sdq.beagle.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 *
 * TODO Document class
 *
 * @author Roman Langrehr
 * @version 0
 */
public class DemoBeagleContextMenuEntryHandlerRepository extends AbstractHandler {
	private static final String FILE_EXTENSION_MATCHER = "repository|repository_diagram";

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		Shell shell = HandlerUtil.getActiveShell(event);
		ISelection sel = HandlerUtil.getActiveMenuSelection(event);
		IStructuredSelection selection = (IStructuredSelection) sel;

		Object firstElement = selection.getFirstElement();
		if (firstElement instanceof IFile) {
			IFile clickedFile = (IFile) firstElement;
			IPath clickedFilePath = clickedFile.getFullPath();
			if (clickedFilePath.getFileExtension().matches(FILE_EXTENSION_MATCHER)) {
				IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
				MessageDialog.openInformation(window.getShell(), "Beagle is alive!",
						"Belive it, or not. But Beagle ist alive!\n" + "You want to analyse: The whole repository: "
								+ clickedFilePath.toString());
			} else {
				MessageDialog.openInformation(shell, "Error",
						"The handler was not executet with a file with a proper file extension.\n"
								+ "This should not be possible!\n" + "selection.getFirstElement() returned a path to: "
								+ clickedFilePath.toString());
			}
		} else {
			MessageDialog.openInformation(shell, "Error",
					"The handler was not executet with a file.\n" + "This should not be possible!\n"
							+ "selection.getFirstElement() returned " + firstElement.toString());
		}
		return null;
	}

}
