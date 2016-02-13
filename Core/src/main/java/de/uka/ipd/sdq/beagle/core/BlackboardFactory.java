package de.uka.ipd.sdq.beagle.core;

import de.uka.ipd.sdq.beagle.core.facade.BeagleConfiguration;

/**
 * Creates a new {@link Blackboard} with the information from an
 * {@link BeagleConfiguration}.
 *
 * @author Roman Langrehr
 */
public class BlackboardFactory {

	/**
	 * The information about the project.
	 */
	private BeagleConfiguration beagleConfiguration;

	/**
	 * Creates a new BlackboardFactory.
	 *
	 * @param beagleConfiguration The information about the project.
	 */
	public BlackboardFactory(final BeagleConfiguration beagleConfiguration) {
		this.beagleConfiguration = beagleConfiguration;
	}

	/**
	 * Creates a new {@link Blackboard} from the {@link BeagleConfiguration} given in the
	 * constructor.
	 *
	 * @return A new {@link Blackboard} instance.
	 */
	public Blackboard createBlackboard() {
		return new Blackboard(rdias, branches, loops, externalCalls, fitnessFunction,
			new ProjectInformation(this.beagleConfiguration.getTimeout(), this.beagleConfiguration.getFileProvider()));
	}
}
