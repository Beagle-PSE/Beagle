package de.uka.ipd.sdq.beagle.gui.contextmenus;

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
 * @author Christoph Michelbach
 */
public class ContextMenuEntryHandlerForComponents extends AbstractHandler {

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final ISelection selection = HandlerUtil.getActiveMenuSelection(event);

		// The cast is safe, because our context menus use IStructuredSelections.
		final IStructuredSelection structuredSelection = (IStructuredSelection) selection;

		final List<String> components = new LinkedList<String>();
		for (final Object clickObject : structuredSelection.toList()) {

			// This cast is safe because this context menu entry is only shown on
			// BasicComponentEditParts
			final BasicComponentEditPart basicComponentEditPart = (BasicComponentEditPart) clickObject;

			// prepare the list of components
			final String component =
				basicComponentEditPart.getPrimaryShape().getFigureBasicComponent_Name_LabelFigure().getText();
			components.add(component);
		}
		// create a new GUI and open it
		final GuiController guiController = new GuiController(components);
		guiController.open();

		return null;
	}
}
