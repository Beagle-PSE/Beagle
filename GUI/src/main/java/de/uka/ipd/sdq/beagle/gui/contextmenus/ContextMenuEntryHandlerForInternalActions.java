package de.uka.ipd.sdq.beagle.gui.contextmenus;

import de.uka.ipd.sdq.beagle.core.facade.BeagleConfiguration;
import de.uka.ipd.sdq.beagle.gui.GuiController;

import de.uka.ipd.sdq.pcm.gmf.seff.edit.parts.InternalAction2EditPart;
import de.uka.ipd.sdq.pcm.gmf.seff.edit.parts.InternalActionEditPart;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;
import org.palladiosimulator.pcm.core.entity.Entity;
import org.palladiosimulator.pcm.seff.InternalAction;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/*
 * This class is involved in creating a Graphical User Interface. Its funtionality cannot
 * reasonably be tested by automated unit tests.
 *
 * COVERAGE:OFF
 */

/**
 * Handles the context menu entries, that start an analysis of an internal action.
 *
 * @author Roman Langrehr
 * @author Christoph Michelbach
 */
public class ContextMenuEntryHandlerForInternalActions extends AbstractHandler {

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final ISelection selection = HandlerUtil.getActiveMenuSelection(event);

		// The cast is safe, because our context menus use IStructuredSelections.
		final IStructuredSelection structuredSelection = (IStructuredSelection) selection;

		// prepare the list of internal actions
		final List<Entity> internalActions = new LinkedList<Entity>();
		String fileName = null;
		for (final Object clickObject : structuredSelection.toList()) {

			// Those casts are safe because this context menu entry is only shown on
			// InternalActionEditParts and InternalAction2EditParts.
			if (clickObject instanceof InternalActionEditPart) {
				final InternalActionEditPart internalActionEditPart = (InternalActionEditPart) clickObject;
				final InternalAction internalAction =
					(InternalAction) ((View) internalActionEditPart.getModel()).getElement();
				fileName = internalAction.eResource().getURI().toFileString();
				internalActions.add(internalAction);
			} else {
				final InternalAction2EditPart internalAction2EditPart = (InternalAction2EditPart) clickObject;
				final InternalAction internalAction =
					(InternalAction) ((View) internalAction2EditPart.getModel()).getElement();
				fileName = internalAction.eResource().getURI().toFileString();
				internalActions.add(internalAction);
			}
		}

		// create a new GUI and open it
		final BeagleConfiguration beagleConfiguration = new BeagleConfiguration(internalActions, new File(fileName));
		final GuiController guiController = new GuiController(beagleConfiguration);
		guiController.open();
		return null;
	}
}
