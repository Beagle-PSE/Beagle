package de.uka.ipd.sdq.beagle.gui;

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
		}
	}
}
