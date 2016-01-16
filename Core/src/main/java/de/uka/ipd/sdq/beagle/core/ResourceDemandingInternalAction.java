package de.uka.ipd.sdq.beagle.core;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Models an internal action demanding resources of a specific type when being executed.
 *
 * <p>The internal action applies to a specific <em>resource type</em>. The resource type
 * describes both the resource that is demanded and the unit the demand’s value is
 * expressed in.
 *
 * @author Christoph Michelbach
 * @author Annika Berger
 * @author Roman Langrehr
 * @author Joshua Gleitze
 *
 * @see ResourceDemandType
 */
public class ResourceDemandingInternalAction implements MeasurableSeffElement {

	/**
	 * Serialisation version UID, see {@link java.io.Serializable}.
	 */
	private static final long serialVersionUID = -8743471676122273889L;

	/**
	 * The type of resource that should be measured in this code section.
	 */
	private ResourceDemandType resourceType;

	/**
	 * The code section for this resource demand.
	 */
	private CodeSection action;

	/**
	 * Creates a ResourceDemandingInternalAction for a specific resource type using a
	 * given code section.
	 *
	 * @param resourceType The resource type of this internal action that should be
	 *            measured. Must not be {@code null}. Must not be empty.
	 * @param action A valid code section for this resource demanding internal action. The
	 *            first line of the code section marks the beginning of this internal
	 *            action (inclusive). The last line of the code section marks the end of
	 *            this internal action (inclusive). Must not be {@code null}.
	 */
	public ResourceDemandingInternalAction(final ResourceDemandType resourceType, final CodeSection action) {
		this.resourceType = resourceType;
		this.action = action;
	}

	/**
	 * Gets this internal action's <em>result type</em>.
	 *
	 * @return This result’s <em>result type</em>, as defined in the class description. Is
	 *         never {@code null}.
	 */
	public ResourceDemandType getResourceType() {
		return this.resourceType;
	}

	/**
	 * Gets this internal action's <em>code section</em>.
	 *
	 * @return the valid code section for this resource demand. The first line of the code
	 *         section marks the beginning of this internal action (inclusive). The last
	 *         line of the code section marks the end of this internal action (inclusive).
	 *         Is never {@code null}.
	 */
	public CodeSection getAction() {
		return this.action;
	}

	@Override
	public boolean equals(final Object object) {
		if (object == null) {
			return false;
		}
		if (object == this) {
			return true;
		}
		if (object.getClass() != getClass()) {
			return false;
		}
		final ResourceDemandingInternalAction other = (ResourceDemandingInternalAction) object;
		return new EqualsBuilder().appendSuper(super.equals(object)).append(this.resourceType, other.resourceType)
			.append(this.action, other.action).isEquals();
	}

	@Override
	public int hashCode() {
		// you pick a hard-coded, randomly chosen, non-zero, odd number
		// ideally different for each class
		return new HashCodeBuilder(19, 41).appendSuper(super.hashCode()).append(this.resourceType).append(this.action)
			.toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("resourceType", this.resourceType).append("action", this.action)
			.toString();
	}
}
