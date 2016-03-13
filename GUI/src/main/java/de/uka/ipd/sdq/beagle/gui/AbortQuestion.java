package de.uka.ipd.sdq.beagle.gui;

import org.apache.commons.lang3.Validate;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.UIJob;

/**
 * Asks the user whether they are sure that they want to abort the analysis. They are
 * shown a dialog asking that question when {@link #ask()} is called. Asking can be
 * aborted by calling {@link #nevermind()}.
 *
 * @author Joshua Gleitze
 */
public class AbortQuestion {

	/**
	 * The dialog’s title.
	 */
	private static final String QUESTION_TITLE = "Abort Beagle";

	/**
	 * The dialog’s message.
	 */
	private static final String QUESTION_MESSAGE =
		"Are you sure you want to abort Beagle? You will lose all data gathered by it!";

	/**
	 * The thread that’s waiting for the user’s answer.
	 */
	private Thread askingThread;

	/**
	 * The dialog presented to the user.
	 */
	private MessageDialog questionDialog;

	/**
	 * Shows a dialog to the user, asking whether the analysis should really be aborted.
	 * Blocks the caller.
	 *
	 * @return {@code true} if the analysis should be aborted.
	 */
	public boolean ask() {
		final AbortConfirmJob askJob = new AbortConfirmJob();
		this.askingThread = Thread.currentThread();
		askJob.schedule();
		try {
			askJob.join();
		} catch (final InterruptedException interrupted) {
			new UIJob(Display.getDefault(), "Close question dialog") {

				@Override
				public IStatus runInUIThread(final IProgressMonitor monitor) {
					AbortQuestion.this.questionDialog.close();
					return Status.OK_STATUS;
				}
			}.schedule();

			return false;
		} finally {
			this.askingThread = null;
		}
		return askJob.wantsToAbort;
	}

	/**
	 * Closes the dialog shown to the user. Unblocks the caller of {@link #ask()} and
	 * makes it return {@code false}.
	 */
	public void nevermind() {
		Validate.validState(this.askingThread != null, "The user is currently not being asked!");
		this.askingThread.interrupt();
	}

	/**
	 * UI job that asks the user if they really want to abort the analysis. Puts the
	 * answer into {@link #wantsToAbort}
	 *
	 * @author Joshua Gleitze
	 */
	private final class AbortConfirmJob extends UIJob {

		/**
		 * Button index corresponding to the yes button.
		 */
		private static final int DO_ABORT = 0;

		/**
		 * Button index corresponding to the no button.
		 */
		private static final int DO_NOT_ABORT = 1;

		/**
		 * Contains the user’s answer after the job has finished.
		 */
		private boolean wantsToAbort;

		/**
		 * The labels showing “yes” and “no” for the buttons.
		 */
		private final String[] yesNoLabels = {
			IDialogConstants.YES_LABEL, IDialogConstants.NO_LABEL
		};

		/**
		 * Creates the job.
		 */
		private AbortConfirmJob() {
			super(Display.getDefault(), "Beagle abort confirmation");
		}

		@Override
		public IStatus runInUIThread(final IProgressMonitor monitor) {
			if (AbortQuestion.this.questionDialog == null) {
				final Shell ourShell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
				AbortQuestion.this.questionDialog = new MessageDialog(ourShell, QUESTION_TITLE, null, QUESTION_MESSAGE,
					MessageDialog.QUESTION, this.yesNoLabels, DO_NOT_ABORT);
			}
			this.wantsToAbort = AbortQuestion.this.questionDialog.open() == DO_ABORT;
			return Status.OK_STATUS;
		}
	}
}
