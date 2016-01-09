package de.uka.ipd.sdq.beagle.gui;

import java.util.Set;

/**
 * Controls the Graphical User Interface (GUI). One {@code GuiController} corresponds to
 * exactly one instance of the GUI. Opening it several times is not possible. Note that
 * it's possible for a GUI instance to exist while not being open.
 * 
 * @author Christoph Michelbach
 */
public class GuiController {

	/**
	 * Creates a new {@link GuiController} with the given components. It's not possible to
	 * change the set of components but the user can choose that certain components will
	 * not be measured even though they are in the set {@code components}.
	 *
	 * @param components The components this {@link GuiController} will use.
	 */
	public GuiController(final Set<Object> components) {
		// refine type
	}

	/**
	 * Opens the GuiController.
	 *
	 */
	public void open() {

	}
}
