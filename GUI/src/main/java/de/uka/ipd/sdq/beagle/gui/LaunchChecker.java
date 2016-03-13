package de.uka.ipd.sdq.beagle.gui;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;

import java.util.List;

/**
 * Checks whether a launch configuration can be launched by Beagle. The results can be ob
 *
 * @author Joshua Gleitze
 */
public class LaunchChecker {

	/**
	 * The configuration we’re checking.
	 */
	private final ILaunchConfiguration launchConfiguration;

	/**
	 * Whether we found an error.
	 */
	private boolean hasError;

	/**
	 * The message of the error. Will be {@code null} iff {@link #hasError} is
	 * {@code true}.
	 */
	private String errorMessage;

	/**
	 * Creates a checker for the given {@code launchConfiguration}.
	 *
	 * @param launchConfiguration The configuration to check.
	 */
	public LaunchChecker(final ILaunchConfiguration launchConfiguration) {
		this.launchConfiguration = launchConfiguration;
	}

	/*
	 * This method has to perform checks. Thus, there are a lot of paths through it, but
	 * that makes sense at this point.
	 */
	/**
	 * Checks whether the launch configuration contains an error that could be configured
	 * on {@link ProjectTab}. The results can be obtained through {@link #hasError()} and
	 * {@link #getErrorMessage()}.
	 */
	// CHECKSTYLE:IGNORE NPath|CyclomaticComplexity
	public void checkForProjectError() {
		final IProject project;
		final String projectName;
		final String repositoryFilePath;
		final String sslFilePath;

		try {
			projectName = this.launchConfiguration.getAttribute(ProjectTab.BEAGLE_LAUNCH_CONFIGURATION_IJAVAPROJECT,
				(String) null);
		} catch (final CoreException coreException) {
			this.foundError("Malformed project configuration.");
			return;
		}
		if (projectName == null || "".equals(projectName)) {
			this.foundError("No project is configured");
			return;
		}
		project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		if (!project.exists()) {
			this.foundError("The configured project does not exist.");
			return;
		}

		try {
			repositoryFilePath = this.launchConfiguration
				.getAttribute(ProjectTab.BEAGLE_LAUNCH_CONFIGURATION_REPOSITORY_FILE, (String) null);
		} catch (final CoreException coreException) {
			this.foundError("Malformed repository file configuration.");
			return;
		}
		if (repositoryFilePath == null || "".equals(repositoryFilePath)) {
			this.foundError("No repository file is configured.");
		} else if (!project.getFile(repositoryFilePath).exists()) {
			this.foundError("The configured repository file cannot be found.");
			return;
		}

		try {
			sslFilePath = this.launchConfiguration
				.getAttribute(ProjectTab.BEAGLE_LAUNCH_CONFIGURATION_SOURCECODELINK_FILE, (String) null);
		} catch (final CoreException coreException) {
			this.foundError("Malformed source statement file configuration.");
			return;
		}
		if (sslFilePath == null || "".equals(sslFilePath)) {
			this.foundError("No source statement link model file is configured.");
		} else if (!project.getFile(sslFilePath).exists()) {
			this.foundError("The configured source statement link model file cannot be found.");
		}
	}

	/**
	 * Checks whether the launch configuration contains an error that could be configured
	 * on {@link TimeoutTab}. The results can be obtained through {@link #hasError()} and
	 * {@link #getErrorMessage()}.
	 */
	public void checkForTimeoutError() {
		final String timeout;
		try {
			timeout = this.launchConfiguration.getAttribute(TimeoutTab.BEAGLE_LAUNCH_CONFIGURATION_TIMEOUT_TYPE,
				TimeoutTab.BEAGLE_LAUNCH_CONFIGURATION_TIMEOUT_TYPE_DEFAULT_VALUE);
		} catch (final CoreException error) {
			this.foundError("Malformed timeout configuration.");
			return;
		}

		switch (timeout) {
			case TimeoutTab.BEAGLE_LAUNCH_CONFIGURATION_TIMEOUT_TYPE_VALUE_CONSTANT_TIMEOUT:
				final int timeoutValue;
				try {
					timeoutValue = this.launchConfiguration.getAttribute(
						TimeoutTab.BEAGLE_LAUNCH_CONFIGURATION_CONSTANT_TIMEOUT_VALUE,
						TimeoutTab.INVALID_TIMEOUT_SECONDS_VALUE);
				} catch (final CoreException error) {
					this.foundError("Malformed timeout value configuration.");
					return;
				}

				if (timeoutValue == TimeoutTab.INVALID_TIMEOUT_SECONDS_VALUE) {
					this.foundError("No timeout value configured.");
				} else if (timeoutValue <= 0) {
					this.foundError("Invalid timeout value configured. Must be greater than 0.");
				}
				return;

			case TimeoutTab.BEAGLE_LAUNCH_CONFIGURATION_TIMEOUT_TYPE_VALUE_ADAPTIVE_TIMEOUT:
			case TimeoutTab.BEAGLE_LAUNCH_CONFIGURATION_TIMEOUT_TYPE_VALUE_NO_TIMEOUT:
				return;

			default:
				this.foundError("Invalid timeout type configuration");
		}
	}

	/**
	 * Checks whether the launch configuration contains an error that could be configured
	 * on {@link LaunchConfigurationTab}. The results can be obtained through
	 * {@link #hasError()} and {@link #getErrorMessage()}.
	 */
	public void checkForLaunchConfigurationError() {
		final List<String> configurationNames;
		try {
			configurationNames = this.launchConfiguration.getAttribute(
				LaunchConfigurationTab.BEAGLE_LAUNCH_CONFIGURATION_LAUNCHCONFIGURATION, (List<String>) null);
		} catch (final CoreException error) {
			this.foundError("Malformed launch configurations configuration.");
			return;
		}

		final List<ILaunchConfiguration> configurations = ILaunchConfigurationHelper.getByNames(configurationNames);
		if (configurations.isEmpty()) {
			this.foundError("No launch configurations to executed the analysed softwar with are configured.");
		}
	}

	/**
	 * Reports that an error was found.
	 *
	 * @param message The message describing the error.
	 */
	private void foundError(final String message) {
		this.hasError = true;
		this.errorMessage = message;
	}

	/**
	 * Whether an error was detected while checking.
	 *
	 * @return {@code true} iff an error was found.
	 */
	public boolean hasError() {
		return this.hasError;
	}

	/**
	 * A message describing a found error.
	 *
	 * @return A message describing the error found. Will be {@code null} iff
	 *         {@link #hasError()} returns {@code true}.
	 */
	public String getErrorMessage() {
		return this.errorMessage;
	}
}
