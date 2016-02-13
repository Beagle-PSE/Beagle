package de.uka.ipd.sdq.beagle.core.testutil.factories;

import static org.mockito.Mockito.mock;

import de.uka.ipd.sdq.beagle.core.EclipseLaunchConfigurationLaunchConfiguration;
import de.uka.ipd.sdq.beagle.core.LaunchConfiguration;

import org.eclipse.debug.core.ILaunchConfiguration;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Builds LaunchConfiguration and provides methods to get them.
 * 
 * @author Annika Berger
 */
public final class LaunchConfigurationFactory {

	/**
	 * Creates a Launch Configuration.
	 *
	 * @return the created launch configuration
	 */
	public LaunchConfiguration getOne() {
		final ILaunchConfiguration launchConfig = mock(ILaunchConfiguration.class);
		final EclipseLaunchConfigurationLaunchConfiguration launchConfiguration =
			new EclipseLaunchConfigurationLaunchConfiguration(launchConfig);
		return launchConfiguration;
	}
	
	/**
	 * Creates Launch Configurations.
	 *
	 * @return an array containing all created launch configurations
	 */
	public LaunchConfiguration[] getAll() {
		final ILaunchConfiguration launchConfig = mock(ILaunchConfiguration.class);
		final EclipseLaunchConfigurationLaunchConfiguration launchConfiguration =
			new EclipseLaunchConfigurationLaunchConfiguration(launchConfig);
		
		final LaunchConfiguration[] launchConfigurations = {
			launchConfiguration
		};
		return launchConfigurations;
	}
	
	/**
	 * Creates Launch Configurations.
	 *
	 * @return an set containing all created launch configurations
	 */
	public Set<LaunchConfiguration> getAllAsSet() {
		return new HashSet<>(Arrays.asList(this.getAll()));
	}

}
