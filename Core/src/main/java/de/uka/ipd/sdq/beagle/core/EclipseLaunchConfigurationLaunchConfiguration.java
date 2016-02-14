package de.uka.ipd.sdq.beagle.core;

import org.apache.commons.lang3.Validate;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;

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
	private final ILaunchConfiguration launchConfiguration;

	/**
	 * Creates a Beagle launch configuration from an eclipse launch configuration.
	 *
	 * @param launchConfiguration The eclipse launch configuration which should be
	 *            launched to execute the code under test.
	 */
	public EclipseLaunchConfigurationLaunchConfiguration(final ILaunchConfiguration launchConfiguration) {
		Validate.notNull(launchConfiguration);
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

	@Override
	public LaunchConfiguration prependClasspath(final String classPathEntry) {
		ILaunchConfigurationWorkingCopy newLaunchConfiguration = null;
		try {
			// Calling .getWorkingCopy() two times assures, that nobody can modify the
			// original LaunchConfiguraion.
			newLaunchConfiguration = this.launchConfiguration.getWorkingCopy().getWorkingCopy();
		} catch (final CoreException coreException) {
			FailureHandler.getHandler(this.getClass()).handle(new FailureReport<>().cause(coreException));
		}
		try {
			newLaunchConfiguration.setAttribute(IJavaLaunchConfigurationConstants.ATTR_CLASSPATH, classPathEntry + ";"
				+ this.launchConfiguration.getAttribute(IJavaLaunchConfigurationConstants.ATTR_CLASSPATH, ""));
		} catch (final CoreException coreException) {
			FailureHandler.getHandler(this.getClass()).handle(new FailureReport<>().cause(coreException));
		}
		return new EclipseLaunchConfigurationLaunchConfiguration(newLaunchConfiguration);
	}

	@Override
	public LaunchConfiguration appendJvmArgument(final String argument) {
		ILaunchConfigurationWorkingCopy newLaunchConfiguration = null;
		try {
			// Calling .getWorkingCopy() two times assures, that nobody can modify the
			// original LaunchConfiguraion.
			newLaunchConfiguration = this.launchConfiguration.getWorkingCopy().getWorkingCopy();
		} catch (final CoreException coreException) {
			FailureHandler.getHandler(this.getClass()).handle(new FailureReport<>().cause(coreException));
		}
		try {
			newLaunchConfiguration.setAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS,
				this.launchConfiguration.getAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS, ""));
		} catch (final CoreException coreException) {
			FailureHandler.getHandler(this.getClass()).handle(new FailureReport<>().cause(coreException));
		}
		return new EclipseLaunchConfigurationLaunchConfiguration(newLaunchConfiguration);
	}

}
