package de.uka.ipd.sdq.beagle.core.facade;

import de.uka.ipd.sdq.beagle.core.failurehandling.FailureHandler;
import de.uka.ipd.sdq.beagle.core.failurehandling.FailureReport;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jdt.core.IJavaProject;

import java.util.HashSet;
import java.util.Set;

/**
 * Helper class to get the launch configurations for the project under analysis.
 *
 * @author Roman Langrehr
 */
public class LauchConfigurationProvider {

	/**
	 * The project under analysis.
	 */
	private final IJavaProject javaProject;

	/**
	 * A new launch configuration provider for a specific project.
	 *
	 * @param javaProject the project under analysis
	 */
	public LauchConfigurationProvider(final IJavaProject javaProject) {
		super();
		this.javaProject = javaProject;
	}

	/**
	 * Gives all launch configurations for the project of this object.
	 *
	 * @return all launch configurations for the project of this object
	 */
	public Set<ILaunchConfiguration> getAllSuitableJUnitLaunchConfigurations() {
		final ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
		final ILaunchConfigurationType type = manager.getLaunchConfigurationType("org.eclipse.jdt.junit.launchconfig");
		ILaunchConfiguration[] jUnitLaunchConfigurations = null;
		try {
			jUnitLaunchConfigurations = manager.getLaunchConfigurations(type);
		} catch (final CoreException exception) {
			FailureHandler.getHandler(this.getClass()).handle(new FailureReport<>().cause(exception));
		}
		final Set<ILaunchConfiguration> launchConfigurations = new HashSet<>();
		for (final ILaunchConfiguration launchConfiguration : jUnitLaunchConfigurations) {
			try {
				if (launchConfiguration.getAttribute("org.eclipse.jdt.launching.PROJECT_ATTR", "")
					.equals(this.javaProject.getProject().getName())) {
					launchConfigurations.add(launchConfiguration);
				}
			} catch (final CoreException exception) {
				FailureHandler.getHandler(this.getClass()).handle(new FailureReport<>().cause(exception));
			}
		}
		return launchConfigurations;
	}
}
