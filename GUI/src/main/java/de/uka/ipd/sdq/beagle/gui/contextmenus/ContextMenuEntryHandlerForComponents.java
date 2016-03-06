package de.uka.ipd.sdq.beagle.gui.contextmenus;

import de.uka.ipd.sdq.beagle.gui.BeagleLaunchConfigurationCreator;

import de.uka.ipd.sdq.pcm.gmf.repository.edit.parts.BasicComponentEditPart;

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
import org.palladiosimulator.pcm.repository.BasicComponent;

import java.util.LinkedList;
import java.util.List;

/*
 * This class is involved in creating a Graphical User Interface. Its functionality cannot
 * reasonably be tested by automated unit tests.
 *
 * COVERAGE:OFF
 */

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

		final List<Entity> components = new LinkedList<Entity>();
		String repositoryFile = null;
		IJavaProject javaProject = null;
		for (final Object clickObject : structuredSelection.toList()) {

			// Those casts are safe because this context menu entry is only shown on
			// BasicComponentEditParts
			final BasicComponentEditPart basicComponentEditPart = (BasicComponentEditPart) clickObject;

			// prepare the list of components
			final BasicComponent basicComponent =
				(BasicComponent) ((View) basicComponentEditPart.getModel()).getElement();
			components.add(basicComponent);

			repositoryFile = ResourcesPlugin.getWorkspace()
				.getRoot()
				.getFile(new Path(basicComponent.eResource().getURI().toPlatformString(true)))
				.getProjectRelativePath()
				.toFile()
				.getPath();
			javaProject = JavaCore.create(ResourcesPlugin.getWorkspace()
				.getRoot()
				.getFile(new Path(basicComponent.eResource().getURI().toPlatformString(true)))
				.getProject());
		}
		// create a new GUI and open it
		new BeagleLaunchConfigurationCreator(components, repositoryFile, javaProject).createLaunchConfiguration();
		return null;
	}
}
