package de.uka.ipd.sdq.beagle.core;

import de.uka.ipd.sdq.beagle.gui.UserConfiguration;

/**
 * 
 * Controls the execution of the Beagle Analysis. {@code BeagleController} can start,
 * pause, continue, and abort an Analysis.
 * 
 * @author Christoph Michelbach
 */
public class BeagleController {

	/**
	 * Constructs a new {@code BeagleController} with the given {@link UserConfiguration}.
	 *
	 * @param userConfiguration The {@link UserConfiguration} this BeagleController has
	 *            permanently. It cannot be changed.
	 */
	public BeagleController(final UserConfiguration userConfiguration) {

	}

	/**
	 * Starts the analysis. This method can only be used once per {@link BeagleController}
	 * . Further calls will be ignored.
	 *
	 */
	public void startAnalysis() {

	}

	/**
	 * Pauses the analysis. If the analysis is already paused, calls to this method are
	 * ignored.
	 *
	 */
	public void pauseAnalysis() {

	}

	/**
	 * Continues the analysis if it is paused. If it's running, calls to this method are
	 * ignored.
	 *
	 */
	public void continueAnalysis() {

	}

	/**
	 * Aborts the analysis. If it already is aborted, calls to this method are ignored.
	 * Aborting the analysis is also possible if it wasn't started yet. In this case it
	 * will never be possible to start it.
	 *
	 */
	public void abortAnalysis() {

	}
}
