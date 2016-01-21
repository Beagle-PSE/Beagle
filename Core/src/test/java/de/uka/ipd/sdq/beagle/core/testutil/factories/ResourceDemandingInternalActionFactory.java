package de.uka.ipd.sdq.beagle.core.testutil.factories;

import de.uka.ipd.sdq.beagle.core.ResourceDemandingInternalAction;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Factory for pre-initialised RDIAs to be used by tests.
 *
 * @author Joshua Gleitze
 * @author Ansgar Spiegler
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
	 * Creates an array of newly initialised rdias.
	 *
	 * @return 3 newly initialised rdias.
	 */
	public ResourceDemandingInternalAction[] getAll() {
		// will be done right once the proper ResourceDemandingInternalAction constructor
		// is there.
		return new ResourceDemandingInternalAction[] {new ResourceDemandingInternalAction(),
			new ResourceDemandingInternalAction(), new ResourceDemandingInternalAction()};
	}

	/**
	 * Creates a set of newly initialised rdias.
	 *
	 * @return 3 newly initialised rdias.
	 */
	public Set<ResourceDemandingInternalAction> getAllAsSet() {
		return new HashSet<>(Arrays.asList(this.getAll()));
	}
}
