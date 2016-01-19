package de.uka.ipd.sdq.beagle.core.testutil.factories;

import de.uka.ipd.sdq.beagle.core.ResourceDemandingInternalAction;

/**
 * Factory for pre-initialised RDIAs to be used by tests.
 *
 * @author Joshua Gleitze
 */
public class ResourceDemandingInternalActionFactory {

	/**
	 * Creates a newly instanciated RDIA.
	 *
	 * @return A newly instanciated RDIA (you may not make any assumptions about).
	 */
	public ResourceDemandingInternalAction getOne() {
		return new ResourceDemandingInternalAction();
	}

	/**
	 * Creates an array of newly initialised seff branches.
	 *
	 * @return 3 newly initialised seff branches.
	 */
	public ResourceDemandingInternalAction[] getAll() {
		// will be done right once the proper ResourceDemandingInternalAction constructor
		// is there.
		return new ResourceDemandingInternalAction[] {new ResourceDemandingInternalAction(),
			new ResourceDemandingInternalAction(), new ResourceDemandingInternalAction()};
	}
}
