package de.uka.ipd.sdq.beagle.gui;

import org.eclipse.jface.dialogs.MessageDialog;

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
	}

	/**
	 * Opens the GUI, meaning that from the point of time this method is called, the user
	 * can see and interact with it. Calls after the first call of this function (per
	 * {@link GuiController} object) are ignored.
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
		if (this.state == GuiControllerState.unopened) {
			this.state = GuiControllerState.wizardOpen;
			ActionListener wizardFinished = new ActionListener() {

				@Override
				public void actionPerformed(final ActionEvent wizardFinished) {
					GuiController.this.engageDialog();
				}
			};

		}
	}

	/**
	 * Opens up the dialog displaying the actions "pause", "continue", and "abort" to the
	 * user. These actions are regarding the analysis.
	 */
	private void engageDialog() {

	}
}
