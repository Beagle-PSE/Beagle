package de.uka.ipd.sdq.beagle.gui;

import de.uka.ipd.sdq.beagle.core.facade.BeagleConfiguration;
import de.uka.ipd.sdq.beagle.core.facade.BeagleController;
import de.uka.ipd.sdq.beagle.core.failurehandling.FailureHandler;
import de.uka.ipd.sdq.beagle.core.failurehandling.FailureReport;
import de.uka.ipd.sdq.beagle.core.timeout.AdaptiveTimeout;
import de.uka.ipd.sdq.beagle.core.timeout.ConstantTimeout;
import de.uka.ipd.sdq.beagle.core.timeout.NoTimeout;

import org.apache.commons.lang3.Validate;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * This class handles the launching of Beagle's {@linkplain ILaunchConfiguration
 * ILaunchConfigurations}.
 *
 * @author Roman Langrehr
 */
public class BeagleLaunchConfigurationDelegate implements ILaunchConfigurationDelegate {

	/**
	 * The unique id for Beagle's {@link ILaunchConfiguration}.
	 */
	public static final String BEAGLE_LAUNCH_CONFIGURATION_IDENTIFIER = "de.uka.ipd.sdq.beagle.launch";

	@Override
	public void launch(final ILaunchConfiguration configuration, final String mode, final ILaunch launch,
		final IProgressMonitor monitor) throws CoreException {
		Validate.isTrue(configuration.getType().getIdentifier().equals(BEAGLE_LAUNCH_CONFIGURATION_IDENTIFIER),
			"The launch configuration was from the type: " + configuration.getType().getIdentifier()
				+ " but supported is only " + BEAGLE_LAUNCH_CONFIGURATION_IDENTIFIER);
		Validate.isTrue(mode.equals(ILaunchManager.RUN_MODE),
			"Only run mode for the Beagle configuration is supported.");

		final BeagleConfiguration beagleConfiguration =
			this.convertBeagleLaunchConfigurationToBeagleConfiguration(configuration);
		beagleConfiguration.finalise();

		final BeagleController beagleController = new BeagleController(beagleConfiguration);
		final GuiController guiController = new GuiController(beagleController);

		final Thread executionThread = new Thread(() -> {
			try {
				beagleController.startAnalysis();
			} finally {
				guiController.analysisFinished();
			}
		});
		executionThread.setUncaughtExceptionHandler(Thread.currentThread().getUncaughtExceptionHandler());
		executionThread.start();
		guiController.analysisStarted();
	}

	/**
	 * Creates a {@link BeagleConfiguration} with the attributes of an Beagle
	 * {@link ILaunchConfiguration}.
	 *
	 * @param launchConfiguration The {@link ILaunchConfiguration} where the set up has
	 *            been made.
	 * @return The {@link BeagleConfiguration} for this setup.
	 */
	private BeagleConfiguration convertBeagleLaunchConfigurationToBeagleConfiguration(
		final ILaunchConfiguration launchConfiguration) {
		try {
			final IJavaProject javaProject =
				JavaCore.create(ResourcesPlugin.getWorkspace().getRoot().getProject(launchConfiguration.getAttribute(
					ProjectTab.BEAGLE_LAUNCH_CONFIGURATION_IJAVAPROJECT, "No project has been specified")));
			final BeagleConfiguration beagleConfiguration = new BeagleConfiguration(null,
				new File(javaProject.getProject().getLocation().toOSString() + "/"
					+ launchConfiguration.getAttribute(ProjectTab.BEAGLE_LAUNCH_CONFIGURATION_REPOSITORY_FILE,
						"No repository file has been set up in the launch configuration")),
				new File(javaProject.getProject().getLocation().toOSString() + "/"
					+ launchConfiguration.getAttribute(ProjectTab.BEAGLE_LAUNCH_CONFIGURATION_SOURCECODELINK_FILE,
						"No source code link file has been set up in the launch configuration")),
				javaProject);

			final String selectionType =
				launchConfiguration.getAttribute(SelectionOverviewTab.BEAGLE_LAUNCH_CONFIGURATION_SELECTION_TYPE,
					SelectionOverviewTab.BEAGLE_LAUNCH_CONFIGURATION_SELECTION_TYPE_DEFAULT_VALUE);
			if (selectionType
				.equals(SelectionOverviewTab.BEAGLE_LAUNCH_CONFIGURATION_SELECTION_TYPE_VALUE_CUSTOM_REPOSITORY)) {
				beagleConfiguration.setElements(
					launchConfiguration.getAttribute(SelectionOverviewTab.BEAGLE_LAUNCH_CONFIGURATION_CUSTOM_SELECTION,
						SelectionOverviewTab.BEAGLE_LAUNCH_CONFIGURATION_CUSTOM_SELECTION_DEFAULT_VALUE));
			}

			switch (launchConfiguration.getAttribute(TimeoutTab.BEAGLE_LAUNCH_CONFIGURATION_TIMEOUT_TYPE,
				TimeoutTab.BEAGLE_LAUNCH_CONFIGURATION_TIMEOUT_TYPE_DEFAULT_VALUE)) {
				case TimeoutTab.BEAGLE_LAUNCH_CONFIGURATION_TIMEOUT_TYPE_VALUE_ADAPTIVE_TIMEOUT:
					beagleConfiguration.setTimeout(new AdaptiveTimeout());
					break;
				case TimeoutTab.BEAGLE_LAUNCH_CONFIGURATION_TIMEOUT_TYPE_VALUE_CONSTANT_TIMEOUT:
					beagleConfiguration.setTimeout(new ConstantTimeout(
						launchConfiguration.getAttribute(TimeoutTab.BEAGLE_LAUNCH_CONFIGURATION_CONSTANT_TIMEOUT_VALUE,
							TimeoutTab.BEAGLE_LAUNCH_CONFIGURATION_CONSTANT_TIMEOUT_VALUE_DEFAULT_VALUE)));
					break;
				case TimeoutTab.BEAGLE_LAUNCH_CONFIGURATION_TIMEOUT_TYPE_VALUE_NO_TIMEOUT:
					beagleConfiguration.setTimeout(new NoTimeout());
					break;
				default:
					break;
			}

			final List<String> launchConfigurations = launchConfiguration.getAttribute(
				LaunchConfigurationTab.BEAGLE_LAUNCH_CONFIGURATION_LAUNCHCONFIGURATION, new ArrayList<>());
			beagleConfiguration
				.setLaunchConfigurations(new HashSet<>(ILaunchConfigurationHelper.toBeagleLaunchConfigurations(
					ILaunchConfigurationHelper.getByNames(launchConfigurations), javaProject)));

			return beagleConfiguration;
		} catch (final CoreException coreException) {
			FailureHandler.getHandler(this.getClass()).handle(new FailureReport<>().cause(coreException));
		}
		return null;
	}

}
