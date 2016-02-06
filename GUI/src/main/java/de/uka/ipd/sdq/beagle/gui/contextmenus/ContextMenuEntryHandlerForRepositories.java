package de.uka.ipd.sdq.beagle.gui.contextmenus;

import de.uka.ipd.sdq.beagle.core.UserConfiguration;
import de.uka.ipd.sdq.beagle.gui.GuiController;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;
import org.palladiosimulator.pcm.repository.BasicComponent;
import org.palladiosimulator.pcm.seff.InternalAction;

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
	 * RegEx for the file extensions, where the context menu entry should be displayed.
	 */
	private static final String FILE_EXTENSION_MATCHER = "repository|repository_diagram";

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final ISelection selection = HandlerUtil.getActiveMenuSelection(event);

		// The cast is safe, because our context menus use IStructuredSelections.
		final IStructuredSelection structuredSelection = (IStructuredSelection) selection;

		final Object firstElement = structuredSelection.getFirstElement();

		// This cast is safe because this context menu entry is only shown on IFiles.
		final IFile clickedFile = (IFile) firstElement;

		final IPath clickedFilePath = clickedFile.getFullPath();
		assert clickedFilePath.getFileExtension().matches(FILE_EXTENSION_MATCHER);

		// final List<File> toAnalyseIds = new ArrayList<>();
		// toAnalyseIds.add(new File(clickedFilePath.toString()));

		final File fileToAnalyse = new File(clickedFilePath.toString());

		// create a new GUI and open it
		final UserConfiguration userConfiguration =
			new UserConfiguration(new LinkedList<BasicComponent>(), new LinkedList<InternalAction>(), fileToAnalyse);
		final GuiController guiController = new GuiController(userConfiguration);
		guiController.open();
		return null;
	}

}
