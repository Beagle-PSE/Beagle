package de.uka.ipd.sdq.beagle.core.measurement.order;

import de.uka.ipd.sdq.beagle.core.Blackboard;
import de.uka.ipd.sdq.beagle.core.CodeSection;
import de.uka.ipd.sdq.beagle.core.measurement.MeasurementTool;

import java.util.Set;

/**
 * The measurement order specifies what to measure for a {@link MeasurementTool}. The idea
 * behind this is, that {@linkplain MeasurementTool MeasurementTools} don't depend on the
 * {@link Blackboard} and don't need to know about it's structure.
 *
 * @author Roman Langrehr
 */
public class MeasurementOrder {

	/**
	 * The code sections where parameters need to be characterised.
	 */
	private Set<CodeSection> parameterValueSection;

	/**
	 * The code sections, where all resource demands need to be captured.
	 */
	private Set<CodeSection> resourceDemandSections;

	/**
	 * The code sections, where an {@link MeasurementEvent} should be created each time it
	 * gets executed.
	 */
	private Set<CodeSection> executionSections;

	/**
	 * Launch configurations for the code under test. Must be at least {@code 0}.
	 */
	private Set<LaunchConfiguration> launchConfigurations;

	/**
	 * The parameter characteriser, that all measurement tools must use for parameter
	 * characterisation.
	 */
	private ParameterCharacteriser parameterCharacteriser;

	/**
	 * Create a measurement order for {@linkplain MeasurementTool MeasurementTools}.
	 *
	 * @param parameterValueSection
	 * @param resourceDemandSections
	 * @param executionSections
	 * @param launchConfigurations
	 * @param parameterCharacteriser
	 */
	public MeasurementOrder(final Set<CodeSection> parameterValueSection, final Set<CodeSection> resourceDemandSections,
		final Set<CodeSection> executionSections, final Set<LaunchConfiguration> launchConfigurations,
		final ParameterCharacteriser parameterCharacteriser) {
		this.parameterValueSection = parameterValueSection;
		this.resourceDemandSections = resourceDemandSections;
		this.executionSections = executionSections;
		this.launchConfigurations = launchConfigurations;
		this.parameterCharacteriser = parameterCharacteriser;
	}

	/**
	 * TODO describe “parameterValueSection”.
	 *
	 * @return the parameterValueSection
	 */
	public Set<CodeSection> getParameterValueSection() {
		return this.parameterValueSection;
	}

	/**
	 * TODO describe “resourceDemandSections”.
	 *
	 * @return the resourceDemandSections
	 */
	public Set<CodeSection> getResourceDemandSections() {
		return this.resourceDemandSections;
	}

	/**
	 * TODO describe “executionSections”.
	 *
	 * @return the executionSections
	 */
	public Set<CodeSection> getExecutionSections() {
		return this.executionSections;
	}

	/**
	 * TODO describe “launchConfigurations”.
	 *
	 * @return the launchConfigurations
	 */
	public Set<LaunchConfiguration> getLaunchConfigurations() {
		return this.launchConfigurations;
	}

	/**
	 * TODO describe “parameterCharacteriser”.
	 *
	 * @return the parameterCharacteriser
	 */
	public ParameterCharacteriser getParameterCharacteriser() {
		return this.parameterCharacteriser;
	}

}
