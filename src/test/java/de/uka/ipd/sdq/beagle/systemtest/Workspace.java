package de.uka.ipd.sdq.beagle.systemtest;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.ILaunchesListener2;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.progress.UIJob;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

/**
 * Represents an Eclipse workspace for usage in a system test. The class offers methods
 * often used when setting up the environment of a system test.
 *
 * <p>In order to keep the testing code simple, this class throws Runtime Exceptions
 * whenever it encounters errors.
 *
 * @author Joshua Gleitze
 */
public class Workspace {

	/**
	 * The Eclipse Workspace we’re acting in.
	 */
	private static final IWorkspace ECLIPSE_WORKSPACE = ResourcesPlugin.getWorkspace();

	/**
	 * Eclipse’s launch manager.
	 */
	private static final ILaunchManager LAUNCH_MANAGER = DebugPlugin.getDefault().getLaunchManager();

	/**
	 * The location to copy temporary files to.
	 */
	private final File copyLocation;

	/**
	 * Eclipse project used to import certain files.
	 */
	private final IProject tmpProject;

	/**
	 * The file {@link #tmpProject} lives in.
	 */
	private final File tmpProjectFolder;

	/**
	 * Initialises a new workspace instance. Should usually only be called once per test.
	 */
	public Workspace() {
		try {
			this.copyLocation = Files.createTempDirectory("beagle-systemtest").toFile();
		} catch (final IOException createError) {
			throw new RuntimeException(createError);
		}

		try {
			final IProjectDescription tmpProjectDescription =
				ECLIPSE_WORKSPACE.newProjectDescription("Beagle System Test");
			this.tmpProjectFolder = new File(this.copyLocation, "Beagle System Test Project");
			this.tmpProjectFolder.mkdir();
			tmpProjectDescription.setLocation(new Path(this.tmpProjectFolder.getAbsolutePath()));

			this.tmpProject = ECLIPSE_WORKSPACE.getRoot().getProject(tmpProjectDescription.getName());
			this.tmpProject.create(tmpProjectDescription, null);
			this.tmpProject.open(null);
		} catch (final CoreException projectCreationError) {
			throw new RuntimeException(projectCreationError);
		}

		// Show the console view because that makes sense for tests
		new UIJob("show console view") {

			@Override
			public IStatus runInUIThread(final IProgressMonitor monitor) {
				try {
					PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow()
						.getActivePage()
						.showView(IConsoleConstants.ID_CONSOLE_VIEW);
				} catch (final PartInitException guiException) {
					throw new RuntimeException(guiException);
				}
				return Status.OK_STATUS;
			}

		}.schedule();

		// Show error windows
		ErrorDialog.AUTOMATED_MODE = false;
	}

	/**
	 * Imports the given {@code projectFolder} into the workspace. This method copies the
	 * provided folder so any the test might do to it will not be reflected.
	 *
	 * @param projectFolder The folder to an Eclipse project.
	 * @return The location the project was copied to before being imported into Eclipse.
	 */
	public File importProject(final File projectFolder) {
		final File newLocation = new File(this.copyLocation, projectFolder.getName());
		try {
			FileUtils.copyDirectory(projectFolder, newLocation);
		} catch (final IOException copyError) {
			throw new RuntimeException(copyError);
		}

		final File projectDescriptionFile = new File(newLocation, ".project");

		try {
			final IProjectDescription projectDescription =
				ECLIPSE_WORKSPACE.loadProjectDescription(new Path(projectDescriptionFile.getAbsolutePath()));
			final IProject project = ECLIPSE_WORKSPACE.getRoot().getProject(projectDescription.getName());
			project.create(projectDescription, null);
			project.open(null);
		} catch (final CoreException loadException) {
			throw new RuntimeException(loadException);
		}

		return newLocation;
	}

	/**
	 * Launches the Eclipse launch configuration denoted by the given
	 * {@code launchConfigurationFile}.
	 *
	 * @param launchConfigurationFile The file configuring the launch.
	 */
	public void launchConfiguration(final File launchConfigurationFile) {
		final ILaunchConfiguration launchConfiguration = this.loadLaunchConfigurationFile(launchConfigurationFile);
		final LaunchFinishWaiter finishWaiter = new LaunchFinishWaiter(launchConfiguration);
		LAUNCH_MANAGER.addLaunchListener(finishWaiter);

		try {
			launchConfiguration.launch(ILaunchManager.RUN_MODE, null);
		} catch (final CoreException launchError) {
			throw new RuntimeException(launchError);
		}
		synchronized (finishWaiter) {
			while (!finishWaiter.finished) {
				try {
					finishWaiter.wait();
				} catch (final InterruptedException interrupted) {
					// we wait and ignore interrupts.
				}
			}
		}

		LAUNCH_MANAGER.removeLaunchListener(finishWaiter);
	}

	/**
	 * Loads the given {@code launchConfigurationFile} and returns the corresponding
	 * {@link ILaunchConfiguration} object.
	 *
	 * @param launchConfigurationFile The file configuring the run configuration to load.
	 * @return The launch configuration object.
	 */
	private ILaunchConfiguration loadLaunchConfigurationFile(final File launchConfigurationFile) {
		final String configurationFileName = launchConfigurationFile.getName();
		try {
			FileUtils.copyFile(launchConfigurationFile, new File(this.tmpProjectFolder, configurationFileName));
			this.tmpProject.refreshLocal(IResource.DEPTH_ONE, null);
		} catch (final IOException | CoreException copyError) {
			throw new RuntimeException(copyError);
		}
		final IFile launchConfigurationEclipseFile = this.tmpProject.getFile(configurationFileName);
		assert launchConfigurationEclipseFile.exists();
		return LAUNCH_MANAGER.getLaunchConfiguration(launchConfigurationEclipseFile);
	}

	/**
	 * Loads the provided {@code launchConfigurationFile} so it’s known to the workspace.
	 * The launch configuration will be named like the file is.
	 *
	 * @param launchConfigurationFile The configuration file of the launch configuration
	 *            to load.
	 */
	public void loadLaunchConfiguration(final File launchConfigurationFile) {
		this.loadLaunchConfigurationFile(launchConfigurationFile);
	}

	/**
	 * Monitors a launched configurations for being terminated. Notifies itself once the
	 * launch is finished.
	 *
	 * @author Joshua Gleitze
	 */
	private final class LaunchFinishWaiter implements ILaunchesListener2 {

		/**
		 * The configuration we’re waiting for.
		 */
		private final ILaunchConfiguration launchConfiguration;

		/**
		 * Whether the configuration is finished.
		 */
		private boolean finished;

		/**
		 * Creates a waiter that waits for the given {@code launchConfiguration} to
		 * finish.
		 *
		 * @param launchedConfiguration The configuration to wait for.
		 */
		private LaunchFinishWaiter(final ILaunchConfiguration launchedConfiguration) {
			this.launchConfiguration = launchedConfiguration;
		}

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
				.anyMatch((launch) -> launch.getLaunchConfiguration() == this.launchConfiguration)) {
				synchronized (this) {
					this.finished = true;
					this.notifyAll();
				}
			}
		}
	}
}
