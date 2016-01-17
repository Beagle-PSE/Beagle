package de.uka.ipd.sdq.beagle.prototypes.contextmenus.handlers;

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
 * Handles the context menu entries, that start an analysis of a component.
 *
 * @author Roman Langrehr
 */
public class DemoBeagleContextMenuEntryHandlerForComponents extends AbstractHandler {

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final ISelection selection = HandlerUtil.getActiveMenuSelection(event);
		final IStructuredSelection structuredSelection = (IStructuredSelection) selection;
		final BasicComponentEditPart basicComponentEditPart = (BasicComponentEditPart) structuredSelection.getFirstElement();
		final IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		MessageDialog.openInformation(window.getShell(), "Beagle is alive!",
				"Belive it, or not. But Beagle ist alive!\n" + "You want to analyse: A single component: "
						+ basicComponentEditPart.getPrimaryShape().getFigureBasicComponent_Name_LabelFigure()
								.getText());
		return null;
	}
}
