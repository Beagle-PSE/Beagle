package de.uka.ipd.sdq.beagle.gui;

import de.uka.ipd.sdq.beagle.core.facade.BeagleController;

import java.util.HashSet;
import java.util.Set;

/*
 * This class is involved in creating a Graphical User Interface. Its functionality cannot
 * reasonably be tested by automated unit tests.
 *
 * COVERAGE:OFF
 */

/**
 * Controls Beagle’s graphical user interface while Beagle is running. All UI events are
 * being dispatched from it. UI components reports to this controller. The controller in
 * turn performs the necessary actions on the {@link BeagleController} and updates the
 * GUI.
 *
 * @author Joshua Gleitze
 * @author Christoph Michelbach
 * @author Roman Langrehr
 */
public class GuiController {

	/**
	 * The {@link BeagleController} connected to this GUI.
	 */
	private final BeagleController beagleController;

	/**
	 * The window showing progress to the user.
	 */
	private final ProgressWindow progressWindow = new ProgressWindow(this);

	/**
	 * Callbacks for when the analysis has finished.
	 */
	private final Set<Runnable> finishListeners = new HashSet<>();

	/**
	 * Will be set when the analysis finished.
	 */
	private volatile boolean finished;

	/**
	 * Will be set when the analysis was requested to be aborted.
	 */
	private volatile boolean aborted;

	/**
	 * Constructs a new {@link GuiController} using {@code components} as the default
	 * components to be measured.
	 *
	 * @param beagleController The {@link BeagleController} to use.
	 */
	public GuiController(final BeagleController beagleController) {
		this.beagleController = beagleController;
	}

	/**
	 * Reports that the analysis has started.
	 */
	public synchronized void analysisStarted() {
		this.progressWindow.show();
	}

	/**
	 * Reports that the analysis has ended.
	 */
	public synchronized void analysisFinished() {
		this.finished = true;
		for (final Runnable endListener : this.finishListeners) {
			endListener.run();
		}
		this.progressWindow.hide();
	}

	/**
	 * Callback to report that the user requested to pause the analysis.
	 */
	public synchronized void pauseRequested() {
		this.beagleController.pauseAnalysis();
		this.progressWindow.setPaused();
	}

	/**
	 * Callback to report that the user requested to abort the analysis.
	 */
	public void abortRequested() {
		if (this.aborted || this.finished) {
			return;
		}

		this.progressWindow.hide();
		if (this.confirmAbort()) {
			this.aborted = true;
			this.progressWindow.setAborting();
			this.beagleController.abortAnalysis();
		}

		synchronized (this) {
			if (!this.finished) {
				this.progressWindow.show();
			}
		}
	}

	/**
	 * Callback to report that the user requested to resume the analysis.
	 */
	public synchronized void continueRequested() {
		this.progressWindow.setRunning();
		this.beagleController.continueAnalysis();
	}

	/**
	 * Shows a dialog asking the user if they really want to abort.
	 *
	 * @return {@code true} if the user confirmed to abort the analysis.
	 */
	private boolean confirmAbort() {
		final AbortQuestion question = new AbortQuestion();
		final Runnable finishCallback = question::nevermind;
		this.finishListeners.add(finishCallback);
		final boolean answer = question.ask();
		this.finishListeners.remove(finishCallback);
		return answer;
	}

}
