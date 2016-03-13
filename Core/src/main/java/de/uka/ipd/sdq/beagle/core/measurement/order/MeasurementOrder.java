package de.uka.ipd.sdq.beagle.core.measurement.order;

import de.uka.ipd.sdq.beagle.core.Blackboard;
import de.uka.ipd.sdq.beagle.core.CodeSection;
import de.uka.ipd.sdq.beagle.core.ProjectInformation;
import de.uka.ipd.sdq.beagle.core.measurement.MeasurementTool;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.HashSet;
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
	private final Set<CodeSection> parameterValueSection;

	/**
	 * The code sections, where all resource demands need to be captured.
	 */
	private final Set<CodeSection> resourceDemandSections;

	/**
	 * The code sections, where an {@link MeasurementEvent} should be created each time it
	 * gets executed.
	 */
	private final Set<CodeSection> executionSections;

	/**
	 * Information about the measured project.
	 */
	private final ProjectInformation projectInformation;

	/**
	 * The parameter characteriser, that all measurement tools must use for parameter
	 * characterisation.
	 */
	private final ParameterCharacteriser parameterCharacteriser;

	/**
	 * Create a measurement order for {@linkplain MeasurementTool MeasurementTools}.
	 *
	 * @param parameterValueSections The code sections where parameters need to be
	 *            characterised. Must not be {@code null}. May not contain {@code null}
	 *            elements.
	 * @param resourceDemandSections The code sections, where all resource demands need to
	 *            be captured. Must not be {@code null}. May not contain {@code null}
	 *            elements.
	 * @param executionSections The code sections, where an {@link MeasurementEvent}
	 *            should be created each time it gets executed. Must not be {@code null}.
	 *            May not contain {@code null} elements.
	 * @param projectInformation The measured project’s configuration. Must not be
	 *            {@code null}.
	 * @param parameterCharacteriser The parameter characteriser, that all measurement
	 *            tools must use for parameter characterisation. Must not be {@code null}.
	 */
	public MeasurementOrder(final Set<CodeSection> parameterValueSections,
		final Set<CodeSection> resourceDemandSections, final Set<CodeSection> executionSections,
		final ProjectInformation projectInformation, final ParameterCharacteriser parameterCharacteriser) {
		Validate.noNullElements(parameterValueSections);
		Validate.noNullElements(resourceDemandSections);
		Validate.noNullElements(resourceDemandSections);
		Validate.noNullElements(executionSections);
		Validate.notNull(projectInformation);
		Validate.notNull(parameterCharacteriser);
		this.parameterValueSection = new HashSet<>(parameterValueSections);
		this.resourceDemandSections = new HashSet<>(resourceDemandSections);
		this.executionSections = new HashSet<>(executionSections);
		this.projectInformation = projectInformation;
		this.parameterCharacteriser = parameterCharacteriser;
	}

	/**
	 * Gives the code sections where parameters need to be characterised.
	 *
	 * @return the code sections where parameters need to be characterised. Is never
	 *         {@code null}. Does not contain {@code null} elements.
	 */
	public Set<CodeSection> getParameterValueSection() {
		return new HashSet<>(this.parameterValueSection);
	}

	/**
	 * Gives the code sections, where all resource demands need to be captured.
	 *
	 * @return the resourceDemandSections The code sections, where all resource demands
	 *         need to be captured. Is never {@code null}. Does not contain {@code null}
	 *         elements.
	 */
	public Set<CodeSection> getResourceDemandSections() {
		return new HashSet<>(this.resourceDemandSections);
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
		return new HashSet<>(this.executionSections);
	}

	/**
	 * Gives information about the measured project.
	 *
	 * @return Information about the measured project. Is never {@code null}.
	 */
	public ProjectInformation getProjectInformation() {
		return this.projectInformation;
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

	@Override
	public boolean equals(final Object object) {
		if (object == null) {
			return false;
		}
		if (object == this) {
			return true;
		}
		if (object.getClass() != this.getClass()) {
			return false;
		}
		final MeasurementOrder other = (MeasurementOrder) object;
		return new EqualsBuilder().append(this.parameterValueSection, other.parameterValueSection)
			.append(this.resourceDemandSections, other.resourceDemandSections)
			.append(this.executionSections, other.executionSections)
			.append(this.projectInformation, other.projectInformation)
			.append(this.parameterCharacteriser, other.parameterCharacteriser)
			.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(this.parameterValueSection)
			.append(this.resourceDemandSections)
			.append(this.executionSections)
			.append(this.projectInformation)
			.append(this.projectInformation)
			.toHashCode();
	}

}
