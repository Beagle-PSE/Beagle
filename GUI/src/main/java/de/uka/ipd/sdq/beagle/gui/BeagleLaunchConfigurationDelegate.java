package de.uka.ipd.sdq.beagle.gui;

import org.apache.commons.lang3.Validate;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;

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
	}

}
