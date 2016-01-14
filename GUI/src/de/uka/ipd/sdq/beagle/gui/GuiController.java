package de.uka.ipd.sdq.beagle.gui;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * The class controlling the GUI.
 * 
 * @author Christoph Michelbach
 */
public class GuiController {

	/**
	 * Describes the state of the {@link GuiController}. {@link GuiController} goes
	 * through the states {@code unopened} → {@code wizardOpen} → {@code dialogOpen} →
	 * {@code terminated} witch no way to go backwards but the option to skip states.
	 * 
	 * @author Christoph Michelbach
	 */
	private enum GuiControllerState {
		/**
		 * The states of an {@link GuiController}. {@link GuiController} goes through the
		 * states {@code unopened} → {@code wizardOpen} → {@code dialogOpen} →
		 * {@code terminated} witch no way to go backwards but the option to skip states.
		 */
		unopened, wizardOpen, dialogOpen, terminated
	};

	/**
	 * Describes the state of the {@link GuiController}. {@link GuiController} goes
	 * through the states {@code unopened} → {@code wizardOpen} → {@code dialogOpen} →
	 * {@code terminated} witch no way to go backwards but the option to skip states.
	 */
	private GuiControllerState state;

	/**
	 * The shell the GUI plugin will use.
	 */
	private Shell shell;

	/**
	 * The {@link UserConfiguration} this {@link GuiController} and therefore everything
	 * linked to it uses.
	 */
	private UserConfiguration userConfiguration;

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
	 * @param components The default components to be measured.
	 */
	public GuiController(final List<String> components) {
		this.userConfiguration = new UserConfiguration(components);
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
		System.out.println("open() returned");
	}

	/**
	 * Opens up the wizard allowing the user to configure Beagle’s behaviour during the
	 * analysis. Calls after the first call of this function (per {@link GuiController}
	 * object) are ignored.
	 */
	private void engageWizard() {
		if (this.state == GuiControllerState.unopened) {
			this.state = GuiControllerState.wizardOpen;
			ActionListener wizardFinished = new ActionListener() {

				@Override
				public void actionPerformed(final ActionEvent wizardFinished) {
					GuiController.this.engageDialog();
				}
			};

			this.beagleAnalysisWizard = new BeagleAnalysisWizard(wizardFinished);
			final WizardDialog wizardDialog = new WizardDialog(this.shell, this.beagleAnalysisWizard);
			wizardDialog.open();
		}
	}

	/**
	 * Opens up the dialog displaying the actions "pause", "continue", and "abort" to the
	 * user. These actions are regarding the analysis.
	 */
	private void engageDialog() {
		final String dialogTitle = "Beagle Analysis is Running";
		final String dialogMessage = "Beagle Analysis is running.";
		final String[] buttonLabels = {"Abort", "Pause"};
		final MessageDialog dialog =
			new MessageDialog(this.shell, dialogTitle, null, dialogMessage, MessageDialog.INFORMATION, buttonLabels, 0);
		final int buttonClick = dialog.open();

		if (buttonClick == 0) {
			System.out.println("User clicked 'Abort'.");
		}

		if (buttonClick == 1) {
			System.out.println("User clicked 'Pause'.");
		}
	}
}
