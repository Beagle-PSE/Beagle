package de.uka.ipd.sdq.beagle.handler;

import de.uka.ipd.sdq.pcm.gmf.repository.edit.parts.BasicComponentEditPart;

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
public class DemoBeagleContextMenuEntryHandlerComponent extends AbstractHandler {

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		ISelection sel = HandlerUtil.getActiveMenuSelection(event);
		IStructuredSelection selection = (IStructuredSelection) sel;
		BasicComponentEditPart basicComponentEditPart = (BasicComponentEditPart) selection.getFirstElement();
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		MessageDialog.openInformation(window.getShell(), "Beagle is alive!",
				"Belive it, or not. But Beagle ist alive!\n" + "You want to analyse: A single component: "
						+ basicComponentEditPart.getPrimaryShape().getFigureBasicComponent_Name_LabelFigure()
								.getText());
		return null;
	}
}
