package de.uka.ipd.sdq.beagle.gui;

import de.uka.ipd.sdq.beagle.core.facade.BeagleController;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.UIJob;

import java.lang.Thread.UncaughtExceptionHandler;

/*
 * This class is involved in creating a Graphical User Interface. Its functionality cannot
 * reasonably be tested by automated unit tests.
 *
 * COVERAGE:OFF
 */

/**
 * Controls the dialog appearing when launching Beagle.
 *
 * @author Christoph Michelbach
 * @author Roman Langrehr
 */
public class ProgressDialogController {

	/**
	 * Is used to display the actions "pause", "continue", and "abort" to the user. These
	 * actions are regarding the analysis.
	 */
	private MessageDialog messageDialog;

	/**
	 * The {@link BeagleController} connected to this GUI.
	 */
	private final BeagleController beagleController;

	/**
	 * Constructs a new {@link ProgressDialogController} using {@code components} as the
	 * default components to be measured.
	 *
	 * @param beagleController The {@link BeagleController} to use.
	 */
	public ProgressDialogController(final BeagleController beagleController) {
		this.beagleController = beagleController;
	}

	/**
	 * Opens up the dialog displaying the actions "pause", "continue", and "abort" to the
	 * user. These actions are regarding the analysis.
	 *
	 * <p>Important: This method blocks until the analysis is finished.
	 */
	public void engageDialog() {
		new UIJob(Display.getDefault(), "Beagle is analysing") {

			@Override
			public IStatus runInUIThread(final IProgressMonitor monitor) {
				ProgressDialogController.this.createDialog();
				return Status.OK_STATUS;
			}
		}.schedule();
	}

	/**
	 * Creates and shows the actual dialog. Must be run in the UI Thread.
	 *
	 * <p>Important: This method blocks until the analysis is finished.
	 */
	private void createDialog() {
		final Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		final String dialogTitleRunning = "Beagle Analysis is Running";
		final String dialogMessageRunning = "Beagle Analysis is running.";
		final String[] buttonLabelsRunning = {
			"Abort", "Pause"
		};

		final String dialogTitlePaused = "Beagle Analysis is Paused";
		final String dialogMessagePaused = "Beagle Analysis is paused.";
		final String[] buttonLabelsPaused = {
			"Abort", "Continue"
		};

		boolean analysisRunning = false;
		// equals a click on button "Continue" (continuing and starting the
		// analysis
		// always have the same behaviour regarding the dialog)
		this.messageDialog = new MessageDialog(shell, dialogTitleRunning, null, dialogMessageRunning,
			MessageDialog.INFORMATION, buttonLabelsRunning, 0);
		int buttonClick = this.messageDialog.open();

		while (buttonClick != 0 && buttonClick != SWT.DEFAULT) {

			switch (buttonClick) {
				case 1:
					if (analysisRunning) {
						// analysis is being paused by the user
						analysisRunning = false;
						this.beagleController.pauseAnalysis();

						this.messageDialog = new MessageDialog(shell, dialogTitlePaused, null, dialogMessagePaused,
							MessageDialog.INFORMATION, buttonLabelsPaused, 0);
					} else {
						// analysis is being continued by the user
						analysisRunning = true;
						this.beagleController.continueAnalysis();

						this.messageDialog = new MessageDialog(shell, dialogTitleRunning, null, dialogMessageRunning,
							MessageDialog.INFORMATION, buttonLabelsRunning, 0);
					}
					break;
				default:
					// what is done when no button has been clicked but the dialog
					// has
					// been closed in any different way

					// just open the dialog again, so do nothing here
					break;
			}

			buttonClick = this.messageDialog.open();
		}
	}

	/**
	 * Calls {@link BeagleController} to start the analysis. This happens in a different
	 * thread so the GUI remains responsive.
	 */
	public void startAnalysis() {
		final UncaughtExceptionHandler uncaughtExceptionHandler = Thread.currentThread().getUncaughtExceptionHandler();

		/*
		 * Calls {@link BeagleController} to start the analysis. This happens in a
		 * different thread so the GUI remains responsive.
		 */
		new Thread(() -> ProgressDialogController.this.runAnalysis(uncaughtExceptionHandler)).start();
	}

	/**
	 * Runs the tasks that should be done asynchrony to the GUI progress dialog.
	 *
	 * @param uncaughtExceptionHandler The {@link UncaughtExceptionHandler} to notify,
	 *            when an exception occurs.
	 */
	private void runAnalysis(final UncaughtExceptionHandler uncaughtExceptionHandler) {
		try {
			ProgressDialogController.this.beagleController.startAnalysis();
			// CHECKSTYLE:IGNORE IllegalCatch
		} catch (final Exception exception) {
			uncaughtExceptionHandler.uncaughtException(Thread.currentThread(), exception);
			new UIJob(Display.getDefault(), "Close Beagle Dialog") {

				@Override
				public IStatus runInUIThread(final IProgressMonitor monitor) {
					ProgressDialogController.this.messageDialog.close();
					return Status.OK_STATUS;
				}
			}.schedule();
		}
		// when {@code beagleController.startAnalysis()} returns, close the
		// dialog
		new UIJob(Display.getDefault(), "Close Beagle Dialog") {

			@Override
			public IStatus runInUIThread(final IProgressMonitor monitor) {
				ProgressDialogController.this.messageDialog.close();
				return Status.OK_STATUS;
			}
		}.schedule();
	}
}
