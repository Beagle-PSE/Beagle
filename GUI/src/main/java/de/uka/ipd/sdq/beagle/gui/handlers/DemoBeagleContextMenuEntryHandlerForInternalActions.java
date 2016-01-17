package de.uka.ipd.sdq.beagle.gui.handlers;

import de.uka.ipd.sdq.beagle.gui.GuiController;
import de.uka.ipd.sdq.pcm.gmf.seff.edit.parts.InternalAction2EditPart;
import de.uka.ipd.sdq.pcm.gmf.seff.edit.parts.InternalActionEditPart;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import java.util.LinkedList;
import java.util.List;

/**
 * Handles the context menu entries, that start an analysis of an internal action.
 *
 * @author Roman Langrehr
 */
public class DemoBeagleContextMenuEntryHandlerForInternalActions extends AbstractHandler {

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final ISelection selection = HandlerUtil.getActiveMenuSelection(event);
		final IStructuredSelection stucteredSelection = (IStructuredSelection) selection;

		// prepare the list of components
		final List<String> components = new LinkedList<String>();
		for (final Object guiObject : stucteredSelection.toList()) {
			String displayString = null;
			if (guiObject instanceof InternalActionEditPart) {
				final InternalActionEditPart internalActionEditPart = (InternalActionEditPart) guiObject;
				displayString =
					internalActionEditPart.getPrimaryShape().getFigureInternalActionFigureNameLabel().toString();
			} else {
				assert guiObject instanceof InternalAction2EditPart;
				final InternalAction2EditPart internalAction2EditPart = (InternalAction2EditPart) guiObject;
				displayString =
					internalAction2EditPart.getPrimaryShape().getFigureInternalActionFigureNameLabel().toString();
			}
			final String component = displayString;
			components.add(component);
		}

		// create a new GUI and open it
		final GuiController guiController = new GuiController(components);
		guiController.open();
		return null;
	}
}
