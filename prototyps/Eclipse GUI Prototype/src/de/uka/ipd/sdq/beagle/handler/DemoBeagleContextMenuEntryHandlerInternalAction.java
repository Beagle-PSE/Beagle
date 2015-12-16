package de.uka.ipd.sdq.beagle.handler;

import de.uka.ipd.sdq.pcm.gmf.seff.edit.parts.InternalAction2EditPart;
import de.uka.ipd.sdq.pcm.gmf.seff.edit.parts.InternalActionEditPart;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 *
 * TODO Document class
 *
 * @author Roman Langrehr
 * @version 0
 */
public class DemoBeagleContextMenuEntryHandlerInternalAction extends AbstractHandler {

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		ISelection sel = HandlerUtil.getActiveMenuSelection(event);
		IStructuredSelection selection = (IStructuredSelection) sel;
		// BasicComponentEditPart basicComponentEditPart =
		// (BasicComponentEditPart) selection.getFirstElement();
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		Object guiObject = selection.getFirstElement();
		String displayString = null;
		if (guiObject instanceof InternalActionEditPart) {
			InternalActionEditPart internalActionEditPart = (InternalActionEditPart) guiObject;
			displayString = internalActionEditPart.getPrimaryShape().getFigureInternalActionFigureNameLabel()
					.toString();
		} else {
			assert guiObject instanceof InternalAction2EditPart;
			InternalAction2EditPart internalAction2EditPart = (InternalAction2EditPart) guiObject;
			displayString = internalAction2EditPart.getPrimaryShape().getFigureInternalActionFigureNameLabel()
					.toString();
		}
		MessageDialog.openInformation(window.getShell(), "Beagle is alive!",
				"Belive it, or not. But Beagle ist alive!\n" + "You want to analyse: A single component: "
						+ displayString);
		return null;
	}
}
