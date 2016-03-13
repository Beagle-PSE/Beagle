package de.uka.ipd.sdq.beagle.gui;

import org.apache.commons.lang3.Validate;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.UIJob;

/**
 * Shows Beagle’s progress to the user. The window reflects the three main states the
 * analysis can be in: <em>preparing</em>, <em>running</em>, <em>paused</em> and
 * <em>aborted</em>. These states can be set on it.
 *
 * <p>No methods may be called on this window befor it has been initialised through
 * {@link #initialise()}.
 *
 * @author Joshua Gleitze
 * @author Christoph Michelbach
 */
public class ProgressWindow {

	/**
	 * Whether the progress window should be hidden.
	 */
	private volatile boolean hidden = true;

	/**
	 * The GUI controller to report the user’s requests to.
	 */
	private final GuiController guiController;

	/**
	 * The GUI component that will be shown to the user.
	 */
	private ProgressMessageWindow progressWindow;

	/**
	 * Creates a progress window that will report the user’s requests to the given
	 * {@code guiController}.
	 *
	 * @param guiController The controller to report the user’s requests to.
	 */
	public ProgressWindow(final GuiController guiController) {
		this.guiController = guiController;
	}

	/**
	 * Initialises this window.
	 */
	public synchronized void initialise() {
		if (this.progressWindow == null) {
			final UIJob creationJob = inUI(() -> {
				final Shell ourShell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
				this.progressWindow = new ProgressMessageWindow(ourShell);
			});
			while (this.progressWindow == null) {
				try {
					creationJob.join();
				} catch (final InterruptedException interrupted) {
					// It’s crucial that the window has been created after this point. So
					// we try again.
				}
			}
		}
	}

	/**
	 * Shows the progress window in order to indicate that Beagle’s analysis is running.
	 * Has now effect if the window is already shown.
	 */
	public void show() {
		Validate.validState(this.progressWindow != null, "The progress window has not yet been initialised!");
		if (!this.hidden) {
			return;
		}

		inUI(() -> {
			this.progressWindow.open();
			this.progressWindow.restore();
			this.hidden = false;
		});
	}

	/**
	 * Hides the progress window. Has no effect if the window is already hidden.
	 */
	public void hide() {
		Validate.validState(this.progressWindow != null, "The progress window has not yet been initialised!");
		if (this.hidden) {
			return;
		}

		inUI(() -> {
			this.progressWindow.close();
			this.hidden = true;
		});
	}

	/**
	 * Updates the window to reflect that the analysis is being prepared.
	 */
	public void setPreparing() {
		Validate.validState(this.progressWindow != null, "The progress window has not yet been initialised!");
		inUI(this.progressWindow::setPreparing);
	}

	/**
	 * Updates the window to reflect that the analysis is running.
	 */
	public void setRunning() {
		Validate.validState(this.progressWindow != null, "The progress window has not yet been initialised!");
		inUI(this.progressWindow::setRunning);
	}

	/**
	 * Updates the window to reflect that the analysis is running.
	 */
	public void setAborting() {
		Validate.validState(this.progressWindow != null, "The progress window has not yet been initialised!");
		inUI(this.progressWindow::setAborting);
	}

	/**
	 * Updates the window to reflect that the analysis is paused.
	 *
	 */
	public void setPaused() {
		Validate.validState(this.progressWindow != null, "The progress window has not yet been initialised!");
		inUI(this.progressWindow::setPaused);
	}

	/**
	 * Schedules the given code to be executed in the UI thread.
	 *
	 * @param toExecute The code to execute in the UI thread.
	 * @return The job that was created to execute.
	 */
	private static UIJob inUI(final Runnable toExecute) {
		final UIJob uiJob = new UIJob(Display.getDefault(), "Beagle") {

			@Override
			public IStatus runInUIThread(final IProgressMonitor monitor) {
				toExecute.run();
				return Status.OK_STATUS;
			}
		};
		uiJob.schedule();
		return uiJob;
	}

	/**
	 * The actual GUI component that will be shown to the user.
	 *
	 * @author Joshua Gleitze
	 */
	private final class ProgressMessageWindow extends MessageDialog {

		/**
		 * The button index that corresponds to the abort button.
		 */
		private static final int BUTTON_ABORT = 0;

		/**
		 * The button index that corresponds to the pause or continue button.
		 */
		private static final int BUTTON_PAUSE_OR_CONTINUE = 1;

		/**
		 * Whether the analysis is paused.
		 */
		private boolean paused;

		/**
		 * The message to show on the window.
		 */
		private String message;

		/**
		 * Creates a progress message window.
		 *
		 * @param shell The shell the window belongs to.
		 */
		private ProgressMessageWindow(final Shell shell) {
			super(shell, "Beagle Analysis", null, "", MessageDialog.INFORMATION, new String[0], BUTTON_ABORT);
			this.setBlockOnOpen(false);
		}

		/**
		 * Updates the window.
		 *
		 * @param newMessage The new message to show.
		 * @param buttonLabels The new button labels to show.
		 */
		private void update(final String newMessage, final String[] buttonLabels) {
			this.message = newMessage;
			this.setButtonLabels(buttonLabels);
			this.restore();
		}

		/**
		 * Restores the window’s state.
		 */
		private void restore() {
			if (this.message == null || this.buttonBar == null || this.messageLabel.isDisposed()
				|| this.buttonBar.isDisposed()) {
				// we’re already disposed or not ready yet.
				return;
			}
			this.messageLabel.setText(this.message);
			this.messageLabel.getParent().layout();

			final Composite buttonBarParent = this.buttonBar.getParent();
			this.buttonBar.dispose();
			this.buttonBar = this.createButtonBar(buttonBarParent);
			buttonBarParent.layout();

			// if buttons were hidden or shown, we need to adjust the window’s size
			final Point oldSize = this.getShell().getSize();
			final Point newSize = this.getInitialSize();
			if (!newSize.equals(oldSize)) {
				this.getShell().setSize(newSize);
			}
		}

		/**
		 * Updates the window to reflect that the analysis is being prepared.
		 */
		private void setPreparing() {
			this.update("Beagle is preparing the analysis.", new String[0]);
		}

		/**
		 * Updates the window to reflect that the analysis is running.
		 */
		private void setRunning() {
			this.paused = false;
			this.update("Beagle is analysing the project.", new String[] {
				"Abort", "Pause"
			});
		}

		/**
		 * Updates the window to reflect that the analysis is running.
		 */
		private void setAborting() {
			this.update("Beagle is aborting the analysis.", new String[0]);
		}

		/**
		 * Updates the window to reflect that the analysis is paused.
		 *
		 */
		private void setPaused() {
			this.paused = true;
			this.update("Beagle’s analysis is paused. Select “Continue” to resume.", new String[] {
				"Abort", "Continue"
			});
		}

		@Override
		protected void handleShellCloseEvent() {
			// If the user closes the dialog, we interpret that as a request to abort.
			new Thread(ProgressWindow.this.guiController::abortRequested).start();
		}

		@Override
		protected void buttonPressed(final int buttonId) {
			switch (buttonId) {
				case BUTTON_ABORT:
					new Thread(ProgressWindow.this.guiController::abortRequested).start();
					break;

				case BUTTON_PAUSE_OR_CONTINUE:
					if (!this.paused) {
						new Thread(ProgressWindow.this.guiController::pauseRequested).start();
					} else {
						new Thread(ProgressWindow.this.guiController::continueRequested).start();
					}
					break;

				default:
					assert false;
			}
		}
	}
}
