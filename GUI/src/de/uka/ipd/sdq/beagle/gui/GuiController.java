package de.uka.ipd.sdq.beagle.gui;

import org.eclipse.jface.dialogs.MessageDialog;

/**
 * The class controlling the GUI.
 * 
 * @author Christoph Michelbach
 */
public class GuiController {
	/**
	 * {@code true} if and only if the GUI has been opened (has been made visible to the
	 * user).
	 */
	private boolean hasBeenOpened;
	/**
	 * The {@link UserConfiguration} this {@link GuiController} and therefore everything
	 * linked to it uses.
	 */
	private UserConfiguration userConfiguration;

	/**
	 * Is used to display the actions "pause", "continue", and "abort" to the user. These
	 * actions are regarding the analysis.
	 */
	private MessageDialog messageDialog;

	/**
	 * Constructs a new {@link GuiController} using {@code components} as the default
	 * components to be measured.
	 * 
	 * @param components
	 *            The default components to be measured.
	 */
	public GuiController(final String components) {
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
