package de.uka.ipd.sdq.beagle.gui.contextmenus;

import de.uka.ipd.sdq.beagle.core.facade.BeagleConfiguration;
import de.uka.ipd.sdq.beagle.gui.GuiController;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;
import org.palladiosimulator.pcm.core.entity.Entity;

import java.io.File;
import java.util.LinkedList;

/*
 * This class is involved in creating a Graphical User Interface. Its funtionality cannot
 * reasonably be tested by automated unit tests.
 *
 * COVERAGE:OFF
 */

/**
 * Handles the context menu entries, that start an analysis of the whole project.
 *
 * @author Roman Langrehr
 * @author Christoph Michelbach
 */
public class ContextMenuEntryHandlerForRepositories extends AbstractHandler {

	/**
	 * The ID stating that an entire repository is ought to be analysed.
	 */
	public static final String SPECIAL_ID_COMPLETE_REPOSITORY = "Analyse complete repository";

	/**
	 * File extension for repository files.
	 */
	private static final String FILE_EXTENSION_REPOSITORY = "repository";

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final ISelection selection = HandlerUtil.getActiveMenuSelection(event);

		// The cast is safe, because our context menus use IStructuredSelections.
		final IStructuredSelection structuredSelection = (IStructuredSelection) selection;

		final Object firstElement = structuredSelection.getFirstElement();

		// This cast is safe because this context menu entry is only shown on IFiles.
		final IFile clickedFile = (IFile) firstElement;

		final File fileToAnalyse;
		if (clickedFile.getFileExtension().equals(FILE_EXTENSION_REPOSITORY)) {
			fileToAnalyse = clickedFile.getRawLocation().toFile();
		} else {
			fileToAnalyse = null;
		}

		// create a new GUI and open it
		final BeagleConfiguration beagleConfiguration =
			new BeagleConfiguration(new LinkedList<Entity>(), fileToAnalyse);
		final GuiController guiController = new GuiController(beagleConfiguration);
		guiController.open();
		return null;
	}

}
