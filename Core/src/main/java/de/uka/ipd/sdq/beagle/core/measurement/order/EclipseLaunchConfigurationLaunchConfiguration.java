package de.uka.ipd.sdq.beagle.core.measurement.order;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;

/**
 * A launch configuration that starts in its {@link #execute()} method an eclipse launch
 * configuration.
 *
 * @author Roman Langrehr
 */
public class EclipseLaunchConfigurationLaunchConfiguration implements LaunchConfiguration {

	/**
	 * The eclipse launch configuration to run.
	 */
	private ILaunchConfiguration launchConfiguration;

	/**
	 * Creates a Beagle launch configuration from an eclipse launch configuration.
	 *
	 * @param launchConfiguration The eclipse launch configuration which should be
	 *            launched to execute the code under test.
	 */
	public EclipseLaunchConfigurationLaunchConfiguration(final ILaunchConfiguration launchConfiguration) {
		this.launchConfiguration = launchConfiguration;
	}

	@Override
	public void execute() {
		try {
			this.launchConfiguration.launch(ILaunchManager.RUN_MODE, null);
		} catch (final CoreException exception) {
			throw new RuntimeException(exception);
		}
	}

}
