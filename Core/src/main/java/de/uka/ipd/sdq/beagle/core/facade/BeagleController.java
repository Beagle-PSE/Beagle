package de.uka.ipd.sdq.beagle.core.facade;

import de.uka.ipd.sdq.beagle.core.AnalysisController;
import de.uka.ipd.sdq.beagle.core.BlackboardFactory;

/**
 * Controls the execution of the Beagle Analysis. {@code BeagleController} can start,
 * pause, continue, and abort an Analysis.
 *
 * @author Christoph Michelbach
 * @author Roman Langrehr
 */
public class BeagleController {

	/**
	 * The analysis controller used for this project.
	 */
	private AnalysisController analysisController;

	/**
	 * Constructs a new {@code BeagleController} with the given
	 * {@link BeagleConfiguration}.
	 *
	 * @param beagleConfiguration The {@link BeagleConfiguration} this BeagleController
	 *            has permanently. It cannot be changed.
	 */
	public BeagleController(final BeagleConfiguration beagleConfiguration) {
		this.analysisController = new AnalysisController(new BlackboardFactory(beagleConfiguration).createBlackboard());
	}

	/**
	 * Starts the analysis. This method can only be used once per {@link BeagleController}
	 * . Further calls will be ignored.
	 *
	 */
	public void startAnalysis() {
		this.analysisController.performAnalysis();
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
