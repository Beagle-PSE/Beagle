package de.uka.ipd.sdq.beagle.gui;

import de.uka.ipd.sdq.beagle.core.failurehandling.FailureHandler;
import de.uka.ipd.sdq.beagle.core.failurehandling.FailureReport;

import org.apache.commons.lang3.Validate;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jdt.core.IJavaProject;
import org.palladiosimulator.pcm.core.entity.Entity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles the creation of Beagle {@linkplain ILaunchConfiguration ILaunchConfigurations}
 * from data gathered by the context menus.
 *
 * @author Roman Langrehr
 */
public class BeagleLaunchConfigurationCreator {

	/**
	 * All elements to measure or {@code null} to indicate that everything in
	 * {@code repositoryFile} should be analysed.
	 */
	private List<String> elements;

	/**
	 * The repository file.
	 */
	private String repositoryFile;

	/**
	 * The {@link IJavaProject} to analyse.
	 */
	private final IJavaProject javaProject;

	/**
	 * Constructs a new {@link BeagleLaunchConfigurationCreator} from data gathered by the
	 * context menus.
	 *
	 * @param elements The elements to be measured or {@code null} to indicate that
	 *            everything in {@code repositoryFile} should be analysed.
	 * @param repositoryFile The repository file to use. Must not be {@code null}.
	 * @param javaProject the {@link IJavaProject} to analyse. Must not be {@code null}.
	 */
	public BeagleLaunchConfigurationCreator(final List<Entity> elements, final String repositoryFile,
		final IJavaProject javaProject) {
		Validate.notNull(repositoryFile);
		Validate.notNull(javaProject);
		Validate.notNull(repositoryFile);

		if (elements != null) {
			this.elements = this.entitiesToIds(elements);
		}

		this.repositoryFile = repositoryFile;
		this.javaProject = javaProject;
	}

	/**
	 * Creates, saves and launches a new Beagle {@link ILaunchConfiguration}.
	 */
	public void createLaunchConfiguration() {
		final ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
		final ILaunchConfigurationType type = manager
			.getLaunchConfigurationType(BeagleLaunchConfigurationDelegate.BEAGLE_LAUNCH_CONFIGURATION_IDENTIFIER);
		try {
			String content = "";
			if (this.elements != null) {
				for (final String element : this.elements) {
					content += element + ", ";
				}
				// Remove last ", ".
				content = content.substring(0, content.length() - 2);
			} else {
				content = "Whole Repository";
			}
			final String name = "Analyse " + content + " in " + this.javaProject.getProject().getName() + " "
				+ this.repositoryFile.substring(this.repositoryFile.lastIndexOf(File.separatorChar) + 1)
				+ " with Beagle";
			final ILaunchConfigurationWorkingCopy workingCopy = type.newInstance(null, name);

			workingCopy.setAttribute(ProjectTab.BEAGLE_LAUNCH_CONFIGURATION_IJAVAPROJECT,
				this.javaProject.getProject().getName());
			workingCopy.setAttribute(ProjectTab.BEAGLE_LAUNCH_CONFIGURATION_REPOSITORY_FILE, this.repositoryFile);
			if (this.elements == null) {
				workingCopy.setAttribute(SelectionOverviewTab.BEAGLE_LAUNCH_CONFIGURATION_SELECTION_TYPE,
					SelectionOverviewTab.BEAGLE_LAUNCH_CONFIGURATION_SELECTION_TYPE_VALUE_WHOLE_REPOSITORY);
			} else {
				workingCopy.setAttribute(SelectionOverviewTab.BEAGLE_LAUNCH_CONFIGURATION_SELECTION_TYPE,
					SelectionOverviewTab.BEAGLE_LAUNCH_CONFIGURATION_SELECTION_TYPE_VALUE_CUSTOM_REPOSITORY);
				workingCopy.setAttribute(SelectionOverviewTab.BEAGLE_LAUNCH_CONFIGURATION_CUSTOM_SELECTION,
					this.elements);
			}

			new TimeoutTab().setDefaults(workingCopy);
			new LaunchConfigurationTab().setDefaults(workingCopy);

			workingCopy.doSave();
			workingCopy.launch(ILaunchManager.RUN_MODE, null);
		} catch (final CoreException coreException) {
			FailureHandler.getHandler(this.getClass())
				.handle(new FailureReport<>().cause(coreException).retryWith(this::createLaunchConfiguration));
		}
	}

	/**
	 * Converts {@linkplain Entity Entities} to {@linkplain String Strings} containing
	 * their id.
	 *
	 * @param entities The {@linkplain Entity Entities} to convert.
	 * @return The ids of the {@code entities}.
	 */
	private List<String> entitiesToIds(final List<Entity> entities) {
		final List<String> result = new ArrayList<>();
		for (final Entity entity : entities) {
			result.add(entity.getId());
		}
		return result;
	}
}
