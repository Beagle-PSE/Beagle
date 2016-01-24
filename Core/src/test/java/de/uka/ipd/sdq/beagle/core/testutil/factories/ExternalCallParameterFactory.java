package de.uka.ipd.sdq.beagle.core.testutil.factories;

import de.uka.ipd.sdq.beagle.core.ExternalCallParameter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Factory for pre-initialised External Call Parameters to be used by tests.
 *
 * @author Joshua Gleitze
 * @author Ansgar Spiegler
 */
public class ExternalCallParameterFactory {

	/**
	 * Creates an new external call parameter.
	 *
	 * @return An external call parameter (you may not make any assumptions about).
	 */
	public ExternalCallParameter getOne() {
		return new ExternalCallParameter();
	}

	/**
	 * Creates an array of newly initialised external call parameters.
	 *
	 * @return 3 newly initialised external call parameters.
	 */
	public ExternalCallParameter[] getAll() {
		// will be done right once the proper ExternalCallParameter constructor is there.
		return new ExternalCallParameter[] {new ExternalCallParameter(), new ExternalCallParameter(),
			new ExternalCallParameter()};
	}

	/**
	 * Creates a set of newly initialised external call parameters.
	 *
	 * @return 3 newly initialised external call parameters.
	 */
	public Set<ExternalCallParameter> getAllAsSet() {
		return new HashSet<>(Arrays.asList(this.getAll()));
	}

}
