package de.uka.ipd.sdq.beagle.gui.contextmenus;

import de.uka.ipd.sdq.beagle.gui.BeagleLaunchConfigurationCreator;

import de.uka.ipd.sdq.pcm.gmf.seff.edit.parts.InternalAction2EditPart;
import de.uka.ipd.sdq.pcm.gmf.seff.edit.parts.InternalActionEditPart;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;
import org.palladiosimulator.pcm.core.entity.Entity;
import org.palladiosimulator.pcm.seff.InternalAction;

import java.util.LinkedList;
import java.util.List;

/*
 * This class is involved in creating a Graphical User Interface. Its functionality cannot
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

		// something must have been selected for this handler to be called.
		assert structuredSelection.size() > 0;

		String repositoryFile = null;
		IJavaProject javaProject = null;
		for (final Object clickObject : structuredSelection.toList()) {

			// Those casts are safe because this context menu entry is only shown on
			// InternalActionEditParts and InternalAction2EditParts.
			final InternalAction internalAction;
			if (clickObject instanceof InternalActionEditPart) {
				final InternalActionEditPart internalActionEditPart = (InternalActionEditPart) clickObject;
				internalAction = (InternalAction) ((View) internalActionEditPart.getModel()).getElement();
				internalActions.add(internalAction);
			} else {
				final InternalAction2EditPart internalAction2EditPart = (InternalAction2EditPart) clickObject;
				internalAction = (InternalAction) ((View) internalAction2EditPart.getModel()).getElement();
				internalActions.add(internalAction);
			}

			repositoryFile = ResourcesPlugin.getWorkspace()
				.getRoot()
				.getFile(new Path(internalAction.eResource().getURI().toPlatformString(true)))
				.getProjectRelativePath()
				.toFile()
				.getPath();
			javaProject = JavaCore.create(ResourcesPlugin.getWorkspace()
				.getRoot()
				.getFile(new Path(internalAction.eResource().getURI().toPlatformString(true)))
				.getProject());
		}

		final BeagleLaunchConfigurationCreator creator =
			new BeagleLaunchConfigurationCreator(internalActions, repositoryFile, javaProject);
		new Thread(creator::createAndLaunchConfiguration).start();
		return null;
	}
}
