package de.uka.ipd.sdq.beagle.gui;

import de.uka.ipd.sdq.beagle.core.LaunchConfiguration;
import de.uka.ipd.sdq.beagle.core.facade.EclipseLaunchConfiguration;
import de.uka.ipd.sdq.beagle.core.failurehandling.FailureHandler;
import de.uka.ipd.sdq.beagle.core.failurehandling.FailureReport;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jdt.core.IJavaProject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Helper methods to get and filter {@linkplain ILaunchConfiguration
 * ILaunchConfigurations}.
 *
 * @author Roman Langrehr
 */
public final class ILaunchConfigurationHelper {

	/**
	 * Hide constructor.
	 */
	private ILaunchConfigurationHelper() {
	}

	/**
	 * Converts {@linkplain ILaunchConfiguration ILaunchConfigurations} to
	 * {@linkplain String Strings} containing their name.
	 *
	 * @param launchConfigurations the {@linkplain ILaunchConfiguration
	 *            ILaunchConfigurations} to convert.
	 * @return the {@linkplain String Strings} containing their name
	 */
	public static List<String> toNameStrings(final List<ILaunchConfiguration> launchConfigurations) {
		final List<String> result = new ArrayList<>();
		for (final ILaunchConfiguration launchConfiguration : launchConfigurations) {
			result.add(launchConfiguration.getName());
		}
		return result;
	}

	/**
	 * Gives all {@linkplain ILaunchConfiguration ILaunchConfigurations} in the workspace.
	 *
	 * @return all {@linkplain ILaunchConfiguration ILaunchConfigurations} in the
	 *         workspace
	 */
	public static List<ILaunchConfiguration> getAllLaunchConfigurationsInWorkspace() {
		final ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
		try {
			return Arrays.asList(manager.getLaunchConfigurations());
		} catch (final CoreException coreException) {
			FailureHandler.getHandler(ILaunchConfigurationHelper.class)
				.handle(new FailureReport<>().cause(coreException));
		}
		return null;
	}

	/**
	 * Filters {@linkplain ILaunchConfiguration ILaunchConfigurations} by a specific
	 * {@link IProject}.
	 *
	 * @param launchConfigurations Some {@linkplain ILaunchConfiguration
	 *            ILaunchConfigurations}.
	 * @param project The name of an {@link IProject} for filtering.
	 * @return All {@linkplain ILaunchConfiguration ILaunchConfigurations} that were in
	 *         {@code launchConfigurations} and belong to {@link IProject}.
	 */
	public static List<ILaunchConfiguration> filterByProject(final List<ILaunchConfiguration> launchConfigurations,
		final String project) {
		final List<ILaunchConfiguration> result = new ArrayList<>();
		for (final ILaunchConfiguration launchConfiguration : launchConfigurations) {
			try {
				if (launchConfiguration.getAttribute("org.eclipse.jdt.launching.PROJECT_ATTR", "").equals(project)) {
					result.add(launchConfiguration);
				}
			} catch (final CoreException coreException) {
				FailureHandler.getHandler(ILaunchConfigurationHelper.class)
					.handle(new FailureReport<>().cause(coreException));
			}
		}
		return result;
	}

	/**
	 * Filters {@linkplain ILaunchConfiguration ILaunchConfigurations} by a specific type.
	 *
	 * @param launchConfigurations Some {@linkplain ILaunchConfiguration
	 *            ILaunchConfigurations}.
	 * @param type The launch configuration type id for filtering.
	 * @return All {@linkplain ILaunchConfiguration ILaunchConfigurations} that were in
	 *         {@code launchConfigurations} and their type is {@code type}.
	 */
	public static List<ILaunchConfiguration> filterByType(final List<ILaunchConfiguration> launchConfigurations,
		final String type) {
		final List<ILaunchConfiguration> result = new ArrayList<>();
		for (final ILaunchConfiguration launchConfiguration : launchConfigurations) {
			try {
				if (launchConfiguration.getType().getIdentifier().equals(type)) {
					result.add(launchConfiguration);
				}
			} catch (final CoreException coreException) {
				FailureHandler.getHandler(ILaunchConfigurationHelper.class)
					.handle(new FailureReport<>().cause(coreException));
			}
		}
		return result;
	}

	/**
	 * Gets {@linkplain ILaunchConfiguration ILaunchConfigurations} by their name.
	 *
	 * @param names The names of the {@linkplain ILaunchConfiguration
	 *            ILaunchConfigurations}. Invalid names are silently ignored. Must not be
	 *            {@code null}.
	 * @return All {@linkplain ILaunchConfiguration ILaunchConfigurations} with a name
	 *         that was in {@code names}.
	 */
	public static List<ILaunchConfiguration> getByNames(final List<String> names) {
		final List<ILaunchConfiguration> result = new ArrayList<>();
		for (final ILaunchConfiguration launchConfiguration : getAllLaunchConfigurationsInWorkspace()) {
			if (names.contains(launchConfiguration.getName())) {
				result.add(launchConfiguration);
			}
		}
		return result;
	}

	/**
	 * Converts some {@linkplain ILaunchConfiguration ILaunchConfigurations} to Beagle's
	 * {@linkplain LaunchConfiguration LaunchConfigurations}.
	 *
	 * @param launchConfigurations Some {@linkplain ILaunchConfiguration
	 *            ILaunchConfigurations} to convert.
	 * @param project The {@link IJavaProject} for the {@code launchConfigurations}.
	 * @return The {@linkplain LaunchConfiguration LaunchConfigurations} wrapping all
	 *         {@code launchConfigurations}.
	 */
	public static List<LaunchConfiguration> toBeagleLaunchConfigurations(
		final List<ILaunchConfiguration> launchConfigurations, final IJavaProject project) {
		final List<LaunchConfiguration> result = new ArrayList<>();
		for (final ILaunchConfiguration launchConfiguration : launchConfigurations) {
			result.add(new EclipseLaunchConfiguration(launchConfiguration, project));
		}
		return result;
	}
}
