package de.uka.ipd.sdq.beagle.gui;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.swt.widgets.Composite;

/**
 * A tab of Beagle's launch configuration for setting up the project to analyse.
 *
 * @author Roman Langrehr
 */
public class ProjectTab extends AbstractLaunchConfigurationTab {

	/**
	 * The key in the Beagle Launch Configuration determining the name of the
	 * {@link IJavaProject} to analyse.
	 */
	public static final String BEAGLE_LAUNCH_CONFIGURATION_IJAVAPROJECT = "IJAVAPROJECT";

	/**
	 * The key in the Beagle Launch Configuration determining the path of the repository
	 * file to analyse.
	 */
	public static final String BEAGLE_LAUNCH_CONFIGURATION_REPOSITORY_FILE = "REPOSITORY_FILE";

	/**
	 * The title of this tab.
	 */
	private static final String TITLE = "Project";

	@Override
	public void createControl(final Composite parent) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDefaults(final ILaunchConfigurationWorkingCopy configuration) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initializeFrom(final ILaunchConfiguration configuration) {
		// TODO Auto-generated method stub

	}

	@Override
	public void performApply(final ILaunchConfigurationWorkingCopy configuration) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getName() {
		return TITLE;
	}

}
