package de.uka.ipd.sdq.beagle.core.testutil.factories;

import de.uka.ipd.sdq.beagle.core.CodeSection;
import de.uka.ipd.sdq.beagle.core.ExternalCallParameter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Factory for pre-initialised External Call Parameters to be used by tests.
 *
 * @author Joshua Gleitze
 * @author Ansgar Spiegler
 * @author Annika Berger
 */
public class ExternalCallParameterFactory {

	/**
	 * A {@link CodeSectionFactory} to generate {@link CodeSection}s.
	 */
	private static final CodeSectionFactory CODE_SECTION_FACTORY = new CodeSectionFactory();

	/**
	 * Creates an new external call parameter.
	 *
	 * @return An external call parameter (you may not make any assumptions about).
	 */
	public ExternalCallParameter getOne() {
		return new ExternalCallParameter(CODE_SECTION_FACTORY.getOne(), 0);
	}

	/**
	 * Creates an array of newly initialised external call parameters.
	 *
	 * @return newly initialised external call parameters.
	 */
	public ExternalCallParameter[] getAll() {
		final CodeSection[] codeSections = CODE_SECTION_FACTORY.getAll();
		final int numberOfParameters = codeSections.length;
		final ExternalCallParameter[] externalCallParameters = new ExternalCallParameter[numberOfParameters];

		for (int i = 0; i < numberOfParameters; i++) {
			externalCallParameters[i] = new ExternalCallParameter(codeSections[i % codeSections.length], i);
		}
		return externalCallParameters;
	}

	/**
	 * Creates a set of newly initialised external call parameters.
	 *
	 * @return newly initialised external call parameters.
	 */
	public Set<ExternalCallParameter> getAllAsSet() {
		return new HashSet<>(Arrays.asList(this.getAll()));
	}

}
