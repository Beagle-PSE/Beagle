package de.uka.ipd.sdq.beagle.core;

import de.uka.ipd.sdq.beagle.core.failurehandling.FailureHandler;
import de.uka.ipd.sdq.beagle.core.failurehandling.FailureReport;

import org.apache.commons.lang3.Validate;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.ILaunchesListener2;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jdt.launching.IRuntimeClasspathEntry;
import org.eclipse.jdt.launching.JavaRuntime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A launch configuration that starts in its {@link #execute()} method an eclipse launch
 * configuration.
 *
 * @author Roman Langrehr
 * @author Joshua Gleitze
 */
public class EclipseLaunchConfiguration implements LaunchConfiguration {

	/**
	 * Handler of failures.
	 */
	private static final FailureHandler FAILURE_HANDLER = FailureHandler.getHandler("Eclipse Launch Manager");

	/**
	 * The eclipse launch configuration to run.
	 */
	private final ILaunchConfiguration originalLaunchConfiguration;

	/**
	 * The project that will be launched when executing
	 * {@link #originalLaunchConfiguration}.
	 */
	private final IJavaProject launchedProject;

	/**
	 * The working copy we operate on to not modify the original configuration.
	 */
	private ILaunchConfigurationWorkingCopy workingCopy;

	/**
	 * When launched, indicates whether the launch has finished.
	 */
	private boolean done;

	/**
	 * Creates a Beagle launch configuration from an eclipse launch configuration.
	 *
	 * @param launchConfiguration The eclipse launch configuration which should be
	 *            launched to execute the code under test.
	 * @param launchedProject The project that will be launched when executing the
	 *            {@code launchConfiguration}.
	 */
	public EclipseLaunchConfiguration(final ILaunchConfiguration launchConfiguration,
		final IJavaProject launchedProject) {
		Validate.notNull(launchConfiguration);
		Validate.notNull(launchedProject);

		this.originalLaunchConfiguration = launchConfiguration;
		this.launchedProject = launchedProject;
		this.copy();

		DebugPlugin.getDefault().getLaunchManager().addLaunchListener(new FinishWaiter());
	}

	@Override
	public void execute() {
		this.done = false;
		try {
			this.workingCopy.launch(ILaunchManager.RUN_MODE, null);

			synchronized (this) {
				while (!this.done) {
					this.wait();
				}
			}
		} catch (final InterruptedException | CoreException runException) {
			final FailureReport<LaunchConfiguration> failure =
				new FailureReport<LaunchConfiguration>().cause(runException).retryWith(this::execute).recoverable();
			FAILURE_HANDLER.handle(failure);
		} finally {
			this.copy();
		}
	}

	@Override
	public LaunchConfiguration prependClasspath(final String classPathEntry) {
		final IRuntimeClasspathEntry newEntry = JavaRuntime.newArchiveRuntimeClasspathEntry(new Path(classPathEntry));
		try {
			this.workingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_DEFAULT_CLASSPATH, false);
			final List<String> entries =
				this.workingCopy.getAttribute(IJavaLaunchConfigurationConstants.ATTR_CLASSPATH, new ArrayList<>());
			if (entries.isEmpty()) {
				entries.add(JavaRuntime.newDefaultProjectClasspathEntry(this.launchedProject).getMemento());
			}
			entries.add(0, newEntry.getMemento());
			this.workingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_CLASSPATH, entries);
		} catch (final CoreException coreException) {
			final FailureReport<LaunchConfiguration> failure =
				new FailureReport<LaunchConfiguration>().cause(coreException);
			return FAILURE_HANDLER.handle(failure);
		}
		return this;
	}

	@Override
	public LaunchConfiguration appendJvmArgument(final String argument) {
		try {
			final String oldAttributes =
				this.workingCopy.getAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS, "");
			this.workingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS,
				oldAttributes + " " + argument);
		} catch (final CoreException coreException) {
			final FailureReport<LaunchConfiguration> failure =
				new FailureReport<LaunchConfiguration>().cause(coreException);
			return FAILURE_HANDLER.handle(failure);
		}
		return this;
	}

	/**
	 * Puts a copy of {@link #originalLaunchConfiguration} into {@link #workingCopy}.
	 */
	private void copy() {
		// Calling .getWorkingCopy() two times assures that nobody can modify the
		// original LaunchConfiguration.
		try {
			this.workingCopy = this.originalLaunchConfiguration.getWorkingCopy().getWorkingCopy();
		} catch (final CoreException coreException) {
			final FailureReport<LaunchConfiguration> failure =
				new FailureReport<LaunchConfiguration>().cause(coreException);
			FAILURE_HANDLER.handle(failure);
		}
	}

	/**
	 * Monitors the launched configurations for being terminated. Notifies this monitor
	 * once the launch finished.
	 *
	 * @author Joshua Gleitze
	 */
	private class FinishWaiter implements ILaunchesListener2 {

		/**
		 * Reference to “own” {@link EclipseLaunchConfiguration} instance.
		 */
		private final EclipseLaunchConfiguration parent = EclipseLaunchConfiguration.this;

		@Override
		public void launchesRemoved(final ILaunch[] launches) {
			// not interesting to us
		}

		@Override
		public void launchesAdded(final ILaunch[] launches) {
			// not interesting to us
		}

		@Override
		public void launchesChanged(final ILaunch[] launches) {
			// not interesting to us
		}

		@Override
		public void launchesTerminated(final ILaunch[] launches) {
			if (Arrays.stream(launches)
				.anyMatch((launch) -> launch.getLaunchConfiguration() == this.parent.workingCopy)) {
				synchronized (this.parent) {
					this.parent.done = true;
					this.parent.notifyAll();
				}
			}
		}
	}
}
