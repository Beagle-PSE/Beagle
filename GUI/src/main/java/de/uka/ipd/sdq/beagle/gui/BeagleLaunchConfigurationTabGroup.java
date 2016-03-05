package de.uka.ipd.sdq.beagle.gui;

import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.CommonTab;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;
import org.eclipse.debug.ui.ILaunchConfigurationTabGroup;

/**
 * The {@link ILaunchConfigurationTabGroup} for Beagle launch configuration.
 *
 * @author Roman Langrehr
 */
public class BeagleLaunchConfigurationTabGroup extends AbstractLaunchConfigurationTabGroup {

	@Override
	public void createTabs(final ILaunchConfigurationDialog dialog, final String mode) {
		final ILaunchConfigurationTab[] tabs = new ILaunchConfigurationTab[] {
			// @formatter:off
			new SelectionOverviewTab(),
			new TimeoutTab(),
			new LaunchConfigurationTab(),
			new CommonTab()
			// @formatter:on
		};
		this.setTabs(tabs);
	}

}
