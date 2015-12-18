package de.uka.sdq.beagle.core;

/**
 * TODO Document class
 *
 */
public class BeagleController {
	/**
	 * This controller’s blackboard, passed to all interacting jobs to communicate. Any
	 * data exchange in any action invoked by this controller must happen exclusively via
	 * this blackboard instance.
	 */
	private final Blackboard blackboard = new Blackboard();
}
