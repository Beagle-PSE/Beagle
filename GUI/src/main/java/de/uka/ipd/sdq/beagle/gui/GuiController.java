package de.uka.ipd.sdq.beagle.gui;

import de.uka.ipd.sdq.beagle.core.facade.BeagleConfiguration;
import de.uka.ipd.sdq.beagle.core.facade.BeagleController;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*
 * This class is involved in creating a Graphical User Interface. Its funtionality cannot
 * reasonably be tested by automated unit tests.
 *
 * COVERAGE:OFF
 */

/**
 * Controls the Graphical User Interface (GUI). One {@code GuiController} corresponds to
 * exactly one instance of the GUI. Opening it several times is not possible. Note that
 * it's possible for a GUI instance to exist while not being open.
 *
 * @author Christoph Michelbach
 */
public class GuiController {

	/**
	 * Describes the state of the {@link GuiController}. {@link GuiController} goes
	 * through the states {@code unopened} → {@code wizardOpen} → {@code dialogOpen} →
	 * {@code terminated} with no way to go backwards but the option to skip states.
	 *
	 * @author Christoph Michelbach
	 */
	private enum GuiControllerState {
		/**
		 * The states of an {@link GuiController}. {@link GuiController} goes through the
		 * states {@code unopened} → {@code wizardOpen} → {@code dialogOpen} →
		 * {@code terminated} with no way to go backwards but the option to skip states.
		 */
		unopened, wizardOpen, dialogOpen, terminated
	};

	/**
	 * Describes the state of the {@link GuiController}. {@link GuiController} goes
	 * through the states {@code unopened} → {@code wizardOpen} → {@code dialogOpen} →
	 * {@code terminated} which no way to go backwards but the option to skip states.
	 */
	private GuiControllerState state;

	/**
	 * {@code true} if the wizard finished successfully (user pressed "finish"};
	 * {@code false} otherwise.
	 */
	private boolean wizardFinishedSuccessfully;

	/**
	 * The shell the GUI plugin will use.
	 */
	private final Shell shell;

	/**
	 * The {@link BeagleConfiguration} this {@link GuiController} and therefore everything
	 * linked to it uses.
	 */
	private final BeagleConfiguration beagleConfiguration;

	/**
	 * The wizard allowing the user to configure Beagle’s behaviour during the analysis.
	 */
	private BeagleAnalysisWizard beagleAnalysisWizard;

	/**
	 * Is used to display the actions "pause", "continue", and "abort" to the user. These
	 * actions are regarding the analysis.
	 */
	private MessageDialog messageDialog;

	/**
	 * Constructs a new {@link GuiController} using {@code components} as the default
	 * components to be measured.
	 *
	 * @param beagleConfiguration The {@link BeagleConfiguration} to use.
	 */
	public GuiController(final BeagleConfiguration beagleConfiguration) {

		this.beagleConfiguration = beagleConfiguration;
		this.state = GuiControllerState.unopened;
		this.shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
	}

	/**
	 * Opens the GUI, meaning that from the point of time this method is called, the user
	 * can see and interact with it. Calls after the first call of this function (per
	 * {@link GuiController} object) are ignored. Returns after the user closed the
	 * wizard. This includes finished the wizard as well as aborting it in any way.
	 */
	public void open() {
		this.engageWizard();
	}

	/**
	 * Opens up the wizard allowing the user to configure Beagle’s behaviour during the
	 * analysis. Calls after the first call of this function (per {@link GuiController}
	 * object) are ignored.
	 */
	private void engageWizard() {
		this.wizardFinishedSuccessfully = false;

		if (this.state == GuiControllerState.unopened) {
			this.state = GuiControllerState.wizardOpen;

			final ActionListener wizardFinished = new ActionListener() {

				@Override
				public void actionPerformed(final ActionEvent event) {
					GuiController.this.wizardFinishedSuccessfully = true;
				}
			};

			this.beagleAnalysisWizard = new BeagleAnalysisWizard(this.beagleConfiguration, wizardFinished);
			final WizardDialog wizardDialog = new WizardDialog(this.shell, this.beagleAnalysisWizard);
			this.state = GuiControllerState.wizardOpen;
			wizardDialog.open();

			if (this.wizardFinishedSuccessfully) {

				// If the wizard finished successfully, indicate to the user that the
				// analysis will start ...
				this.state = GuiControllerState.dialogOpen;
				// ... and let it actually start.
				this.startAnalysis();
				this.engageDialog();

			} else {
				this.state = GuiControllerState.terminated;
			}
		}
	}

	/**
	 * Opens up the dialog displaying the actions "pause", "continue", and "abort" to the
	 * user. These actions are regarding the analysis.
	 */
	private void engageDialog() {
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
		// equals a click on button "Continue" (continuing and starting the analysis
		// always have the same behaviour regarding the dialog)
		int buttonClick = 1;

		while (buttonClick != 0) {
			switch (buttonClick) {
				case 1:
					if (analysisRunning) {
						// analysis has been paused by the user
						analysisRunning = false;

						this.messageDialog = new MessageDialog(this.shell, dialogTitlePaused, null, dialogMessagePaused,
							MessageDialog.INFORMATION, buttonLabelsPaused, 0);
					} else {
						// analysis has been started or continued by the user
						analysisRunning = true;
						this.messageDialog = new MessageDialog(this.shell, dialogTitleRunning, null,
							dialogMessageRunning, MessageDialog.INFORMATION, buttonLabelsRunning, 0);
					}
					break;
				default:
					// what is done when no button has been clicked but the dialog has
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
	private void startAnalysis() {
		new Thread() {

			/**
			 * Calls {@link BeagleController} to start the analysis. This happens in a
			 * different thread so the GUI remains responsive.
			 */
			@Override
			public void run() {
				final BeagleController beagleController = new BeagleController(GuiController.this.beagleConfiguration);
				beagleController.startAnalysis();

				// when {@code beagleController.startAnalysis()} returns, close the dialog
				GuiController.this.state = GuiControllerState.terminated;
				GuiController.this.messageDialog.close();
			}
		}.start();
	}
}
