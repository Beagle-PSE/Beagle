package de.uka.ipd.sdq.beagle.gui.handlers;

import de.uka.ipd.sdq.beagle.gui.GuiController;
import de.uka.ipd.sdq.pcm.gmf.repository.edit.parts.BasicComponentEditPart;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import java.util.LinkedList;
import java.util.List;

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

		final BasicComponentEditPart basicComponentEditPart =
			(BasicComponentEditPart) structuredSelection.getFirstElement();

		// prepare the list of components
		final String component =
			basicComponentEditPart.getPrimaryShape().getFigureBasicComponent_Name_LabelFigure().getText();
		final List<String> components = new LinkedList<String>();
		components.add(component);

		// create a new GUI and open it
		final GuiController guiController = new GuiController(components);
		guiController.open();

		return null;
	}
}
