package de.uka.ipd.sdq.beagle.gui;

import de.uka.ipd.sdq.beagle.core.failurehandling.ExceptionThrowingFailureResolver;
import de.uka.ipd.sdq.beagle.core.failurehandling.FailureReport;
import de.uka.ipd.sdq.beagle.core.failurehandling.FailureResolver;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.UIJob;

import java.util.function.Supplier;

/**
 * A resolver for {@link FailureReport FailureReports} that prompts the user for which
 * action to take.
 *
 * @author Joshua Gleitze
 */
public class GraphicalFailureHandler implements FailureResolver {

	@Override
	public <RECOVER_TYPE> RECOVER_TYPE handle(final FailureReport<RECOVER_TYPE> failureReport,
		final String reporterName) {
		// main logic is in FailurePrompter#runInUIThread
		final FailurePrompter<RECOVER_TYPE> prompter = new FailurePrompter<>(failureReport, reporterName);

		prompter.schedule();

		boolean answered = false;
		while (!answered) {
			try {
				prompter.join();
				answered = true;
			} catch (final InterruptedException interrupted) {
				// The user must select an option. So we try again.
			}
		}
		return prompter.resolver.get();
	}

	/**
	 * The UI job to show the {@link FailureDialog}.
	 *
	 * @author Joshua Gleitze
	 *
	 * @param <RECOVER_TYPE> The recover type of the report being handled.
	 */
	private final class FailurePrompter<RECOVER_TYPE> extends UIJob {

		/**
		 * String representing a bullet point.
		 */
		private static final String BULLET_POINT = "  • ";

		/**
		 * The reported failure.
		 */
		private final FailureReport<RECOVER_TYPE> failureReport;

		/**
		 * The reporter’s name.
		 */
		private final String reporterName;

		/**
		 * The supplier that has been determined to resolve the failure.
		 */
		private Supplier<RECOVER_TYPE> resolver;

		/**
		 * Creates a failure prompt job for the given {@code failureReport} reported by an
		 * instanced called {@code reporterName}.
		 *
		 * @param failureReport The report to handle.
		 * @param reporterName The reporter’s name.
		 */
		private FailurePrompter(final FailureReport<RECOVER_TYPE> failureReport, final String reporterName) {
			super(Display.getDefault(), "Beagle failure");
			this.failureReport = failureReport;
			this.reporterName = reporterName;
		}

		@Override
		public IStatus runInUIThread(final IProgressMonitor monitor) {
			final IStatus reportStatus = new Status(Status.ERROR, "Beagle", this.failureReport.getDetails());

			final boolean canRetry = this.failureReport.getRetryRoutine() != null;
			final boolean canContinue = this.failureReport.getContinueRoutine() != null;
			String message = String.format("“%s” encountered a problem:\n\n%s\n\n", this.reporterName,
				this.failureReport.getFailureMessage());
			if (canRetry) {
				message += String.format("%s“%s” will make Beagle execute the failed action again.\n", BULLET_POINT,
					FailureDialog.RETRY_BUTTON_TEXT);
			}
			if (canContinue) {
				message += String.format("%s“%s” will make Beagle try to continue without the failed action.\n",
					BULLET_POINT, FailureDialog.CONTINUE_BUTTON_TEXT);
			}
			message +=
				String.format("%s“%s” will cause Beagle to abort the analysis. All gathered data will be lost.\n",
					BULLET_POINT, FailureDialog.ABORT_BUTTON_TEXT);

			final Shell ourShell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
			final FailureDialog dialog = new FailureDialog(ourShell, message, reportStatus, canContinue, canRetry);

			switch (dialog.open()) {

				case FailureDialog.ABORT:
					MessageDialog.openInformation(ourShell, "Beagle was aborted",
						"Beagle was aborted because the problem could not be solved.");
					this.resolver = () -> {
						new ExceptionThrowingFailureResolver().handle(this.failureReport, this.reporterName);
						return null;
					};
					break;

				case FailureDialog.RETRY:
					assert this.failureReport.getRetryRoutine() != null;
					this.resolver = this.failureReport.getRetryRoutine();
					break;

				case FailureDialog.CONTINUE:
					assert this.failureReport.getContinueRoutine() != null;
					this.resolver = this.failureReport.getContinueRoutine();
					break;

				default:
					assert false;
			}
			return Status.OK_STATUS;
		}

	}

	/**
	 * The dialog that will be shown to the user in order to handle the failure.
	 *
	 * @author Joshua Gleitze
	 */
	private final class FailureDialog extends ErrorDialog {

		/**
		 * The title that will be shown on every dialog.
		 */
		private static final String TITLE = "Beagle found a problem";

		/**
		 * The button index corresponding to aborting the analysis.
		 */
		private static final int ABORT = 0;

		/**
		 * The button index corresponding to retrying the failed action.
		 */
		private static final int RETRY = 1;

		/**
		 * The button index corresponding to skipping the failed action.
		 */
		private static final int CONTINUE = 2;

		/**
		 * The button label text that will be shown on the abort button.
		 */
		private static final String ABORT_BUTTON_TEXT = "Abort Analysis";

		/**
		 * The button label text that will be shown on the continue button.
		 */
		private static final String CONTINUE_BUTTON_TEXT = "Skip Action";

		/**
		 * The button label text that will be shown on the retry button.
		 */
		private static final String RETRY_BUTTON_TEXT = "Retry Action";

		/**
		 * Whether continuing without the failed action is possible.
		 */
		private final boolean canContinue;

		/**
		 * Whether retrying without the failed action is possible.
		 */
		private final boolean canRetry;

		/**
		 * Redirects to
		 * {@link ErrorDialog#ErrorDialog(Shell, String, String, IStatus, int)}.
		 * Additionally takes flags that give information about the possible resolutions.
		 *
		 * @param parentShell See
		 *            {@link ErrorDialog#ErrorDialog(Shell, String, String, IStatus, int)}
		 *            .
		 * @param message See
		 *            {@link ErrorDialog#ErrorDialog(Shell, String, String, IStatus, int)}
		 *            .
		 * @param status See
		 *            {@link ErrorDialog#ErrorDialog(Shell, String, String, IStatus, int)}
		 *            .
		 * @param canContinue Whether continuing without the failed action is possible.
		 * @param canRetry Whether retrying without the failed action is possible.
		 *
		 */
		private FailureDialog(final Shell parentShell, final String message, final IStatus status,
			final boolean canContinue, final boolean canRetry) {
			super(parentShell, TITLE, null, new Status(Status.ERROR, " ", message), Status.ERROR);
			this.setStatus(status);
			this.canRetry = canRetry;
			this.canContinue = canContinue;
		}

		@Override
		protected void createButtonsForButtonBar(final Composite parent) {
			// these different cases are needed because of the strange way buttons are
			// ordered in a MessageDialog. The order makes “Details” the left most button,
			// then comes abort, then continue, then retry.

			if (this.canRetry && this.canContinue) {
				this.createDetailsButton(parent);
				this.createButton(parent, ABORT, ABORT_BUTTON_TEXT, false);
				this.createButton(parent, CONTINUE, CONTINUE_BUTTON_TEXT, false);
				this.createButton(parent, RETRY, RETRY_BUTTON_TEXT, true);
			} else if (this.canRetry || this.canContinue) {
				if (this.canContinue) {
					this.createButton(parent, CONTINUE, CONTINUE_BUTTON_TEXT, true);
				} else if (this.canRetry) {
					this.createButton(parent, RETRY, RETRY_BUTTON_TEXT, true);
				}
				this.createDetailsButton(parent);
				this.createButton(parent, ABORT, ABORT_BUTTON_TEXT, false);
			} else {
				this.createButton(parent, ABORT, ABORT_BUTTON_TEXT, true);
				this.createDetailsButton(parent);
			}
		}

		@Override
		protected void handleShellCloseEvent() {
			// ignore the close event. The user must select an option.
		}

		@Override
		protected void buttonPressed(final int buttonId) {
			if (buttonId == IDialogConstants.DETAILS_ID) {
				super.buttonPressed(buttonId);
			} else {
				this.setReturnCode(buttonId);
				this.close();
			}
		}
	}
}
