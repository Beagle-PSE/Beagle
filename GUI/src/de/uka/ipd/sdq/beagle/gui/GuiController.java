package de.uka.ipd.sdq.beagle.gui;

import org.eclipse.jface.dialogs.MessageDialog;

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
		this.hasBeenOpened = false;
	}

	/**
	 * Opens the GUI, meaning that from the point of time this method is called, the user
	 * can see and interact with it. Further calls to this function are ignored.
	 */
	public void open() {
		if (!this.hasBeenOpened) {
			this.hasBeenOpened = true;
			this.engageWizard();
			// to do: start analysis here
			this.engageDialog();
		}
	}

	/**
	 * Opens up the wizard allowing the user to configure Beagle’s behaviour during the
	 * analysis.
	 */
	public void engageWizard() {

	}

	/**
	 * Opens up the dialog displaying the actions "pause", "continue", and "abort" to the
	 * user. These actions are regarding the analysis.
	 */
	public void engageDialog() {

	}
}
