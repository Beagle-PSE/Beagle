package de.uka.ipd.sdq.beagle.gui.contextmenus;

import de.uka.ipd.sdq.beagle.gui.BeagleLaunchConfigurationCreator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import java.io.File;

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

		String fileToAnalyse;
		if (clickedFile.getFileExtension().equals(FILE_EXTENSION_REPOSITORY)) {
			// The "../" prefix is necessary, as the "full path" contains the project
			// name.
			fileToAnalyse = clickedFile.getFullPath().toOSString();
			// Remove "/"-Prefix.
			fileToAnalyse = fileToAnalyse.substring(1);
			fileToAnalyse = fileToAnalyse.substring(fileToAnalyse.indexOf(File.separatorChar));
		} else {
			fileToAnalyse = null;
		}

		// create a new GUI and open it
		final BeagleLaunchConfigurationCreator creator =
			new BeagleLaunchConfigurationCreator(null, fileToAnalyse, JavaCore.create(clickedFile.getProject()));
		new Thread(creator::createAndLaunchConfiguration).start();
		return null;
	}

}
