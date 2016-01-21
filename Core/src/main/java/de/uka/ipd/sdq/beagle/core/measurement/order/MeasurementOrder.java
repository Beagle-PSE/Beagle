package de.uka.ipd.sdq.beagle.core.measurement.order;

import de.uka.ipd.sdq.beagle.core.Blackboard;
import de.uka.ipd.sdq.beagle.core.CodeSection;
import de.uka.ipd.sdq.beagle.core.measurement.MeasurementTool;

import org.apache.commons.lang3.Validate;

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
	 * Launch configurations for the code under test. Must contain at least one element.
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
	 * @param parameterValueSection The code sections where parameters need to be
	 *            characterised. Must not be {@code null}. May not contain {@code null}
	 *            elements.
	 * @param resourceDemandSections The code sections, where all resource demands need to
	 *            be captured. Must not be {@code null}. May not contain {@code null}
	 *            elements.
	 * @param executionSections The code sections, where an {@link MeasurementEvent}
	 *            should be created each time it gets executed. Must not be {@code null}.
	 *            May not contain {@code null} elements.
	 * @param launchConfigurations Launch configurations for the code under test. Must
	 *            contain at least one element. Must not be {@code null}. May not contain
	 *            {@code null} elements.
	 * @param parameterCharacteriser The parameter characteriser, that all measurement
	 *            tools must use for parameter characterisation. Must not be {@code null}.
	 */
	public MeasurementOrder(final Set<CodeSection> parameterValueSection, final Set<CodeSection> resourceDemandSections,
		final Set<CodeSection> executionSections, final Set<LaunchConfiguration> launchConfigurations,
		final ParameterCharacteriser parameterCharacteriser) {
		Validate.noNullElements(parameterValueSection);
		Validate.noNullElements(resourceDemandSections);
		Validate.noNullElements(resourceDemandSections);
		Validate.noNullElements(executionSections);
		Validate.notNull(parameterCharacteriser);
		this.parameterValueSection = parameterValueSection;
		this.resourceDemandSections = resourceDemandSections;
		this.executionSections = executionSections;
		this.launchConfigurations = launchConfigurations;
		this.parameterCharacteriser = parameterCharacteriser;
	}

	/**
	 * Gives the code sections where parameters need to be characterised.
	 *
	 * @return the code sections where parameters need to be characterised. Is never
	 *         {@code null}. Does not contain {@code null} elements.
	 */
	public Set<CodeSection> getParameterValueSection() {
		return this.parameterValueSection;
	}

	/**
	 * Gives the code sections, where all resource demands need to be captured.
	 *
	 * @return the resourceDemandSections The code sections, where all resource demands
	 *         need to be captured. Is never {@code null}. Does not contain {@code null}
	 *         elements.
	 */
	public Set<CodeSection> getResourceDemandSections() {
		return this.resourceDemandSections;
	}

	/**
	 * Gives the code sections, where an {@link MeasurementEvent} should be created each
	 * time it gets executed.
	 *
	 * @return The code sections, where an {@link MeasurementEvent} should be created each
	 *         time it gets executed. Is never {@code null}. Does not contain {@code null}
	 *         elements.
	 */
	public Set<CodeSection> getExecutionSections() {
		return this.executionSections;
	}

	/**
	 * Gives launch configurations for the code under test. Contains at least one element.
	 *
	 * @return Launch configurations for the code under test. Contains at least one
	 *         element. Is never {@code null}. Does not contain {@code null} elements.
	 */
	public Set<LaunchConfiguration> getLaunchConfigurations() {
		return this.launchConfigurations;
	}

	/**
	 * Gives the parameter characteriser, that all measurement tools must use for
	 * parameter characterisation.
	 *
	 * @return the parameter characteriser, that all measurement tools must use for
	 *         parameter characterisation. Is never {@code null}.
	 */
	public ParameterCharacteriser getParameterCharacteriser() {
		return this.parameterCharacteriser;
	}

}
