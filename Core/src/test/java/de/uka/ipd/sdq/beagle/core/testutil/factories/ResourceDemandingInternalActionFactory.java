package de.uka.ipd.sdq.beagle.core.testutil.factories;

import de.uka.ipd.sdq.beagle.core.CodeSection;
import de.uka.ipd.sdq.beagle.core.ResourceDemandType;
import de.uka.ipd.sdq.beagle.core.ResourceDemandingInternalAction;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Factory for pre-initialised RDIAs to be used by tests.
 *
 * @author Joshua Gleitze
 * @author Ansgar Spiegler
 * @author Annika Berger
 */
public class ResourceDemandingInternalActionFactory {

	/**
	 * A {@link CodeSectionFactory}, which is able to generate {@link CodeSection}s.
	 */
	private static final CodeSectionFactory CODE_SECTION_FACTORY = new CodeSectionFactory();

	/**
	 * Creates a newly instanciated RDIA.
	 *
	 * @return A newly instanciated RDIA (you may not make any assumptions about).
	 */
	public ResourceDemandingInternalAction getOne() {
		return new ResourceDemandingInternalAction(ResourceDemandType.RESOURCE_TYPE_CPU, CODE_SECTION_FACTORY.getOne());
	}

	/**
	 * Creates an array of newly initialised rdias.
	 *
	 * @return newly initialised rdias.
	 */
	public ResourceDemandingInternalAction[] getAll() {
		final CodeSection[] codeSections = CODE_SECTION_FACTORY.getAll();
		final ResourceDemandType[] types =
		{ResourceDemandType.RESOURCE_TYPE_CPU, ResourceDemandType.RESOURCE_TYPE_CPU_NS,
			ResourceDemandType.RESOURCE_TYPE_HDD, ResourceDemandType.RESOURCE_TYPE_HDD_NS,
			ResourceDemandType.RESOURCE_TYPE_NETWORK, ResourceDemandType.RESOURCE_TYPE_NETWORK_NS};
		final int numberOfInternalActions = codeSections.length;
		final ResourceDemandingInternalAction[] internalActions =
			new ResourceDemandingInternalAction[numberOfInternalActions];

		for (int i = 0; i < numberOfInternalActions; i++) {
			internalActions[i] = new ResourceDemandingInternalAction(types[i % types.length],
				codeSections[i % codeSections.length]);
		}

		return internalActions;
	}

	/**
	 * Creates a set of newly initialised rdias.
	 *
	 * @return newly initialised rdias.
	 */
	public Set<ResourceDemandingInternalAction> getAllAsSet() {
		return new HashSet<>(Arrays.asList(this.getAll()));
	}
}
