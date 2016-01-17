package de.uka.ipd.sdq.beagle.core;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Represents a type of a measured resource, like CPU usage or HDD usage.
 *
 * <p>For the typical types of a measured resources it is recommended to use one of the
 * constants <ul>
 *
 * <li> {@link #RESOURCE_TYPE_CPU}
 *
 * <li> {@link #RESOURCE_TYPE_CPU_NS}
 *
 * <li> {@link #RESOURCE_TYPE_HDD}
 *
 * <li> {@link #RESOURCE_TYPE_HDD_NS}
 *
 * <li> {@link #RESOURCE_TYPE_NETWORK}
 *
 * <li> {@link #RESOURCE_TYPE_NETWORK_NS}
 *
 * </ul> instead of creating its own instances.
 *
 * @author Roman Langrehr
 */
public class ResourceDemandType {

	/**
	 * Common resource type for cycles performed on a CPU.
	 */
	public static final ResourceDemandType RESOURCE_TYPE_CPU = new ResourceDemandType("CPU", false);

	/**
	 * Common resource type for nanoseconds of computation performed on a CPU.
	 */
	public static final ResourceDemandType RESOURCE_TYPE_CPU_NS = new ResourceDemandType("CPU", true);

	/**
	 * Common resource type for bytes transfered to or from a non-volatile storage.
	 */
	public static final ResourceDemandType RESOURCE_TYPE_HDD = new ResourceDemandType("HDD", false);

	/**
	 * Common resource type for nanoseconds of reading or writing operations performed on
	 * a non-volatile storage.
	 */
	public static final ResourceDemandType RESOURCE_TYPE_HDD_NS = new ResourceDemandType("HDD", true);

	/**
	 * Common resource type for bytes send or received over a network interface.
	 */
	public static final ResourceDemandType RESOURCE_TYPE_NETWORK = new ResourceDemandType("NETWORK", false);

	/**
	 * Common resource type for nanoseconds spend for network operations.
	 */
	public static final ResourceDemandType RESOURCE_TYPE_NETWORK_NS = new ResourceDemandType("NETWORK", true);

	/**
	 * A unique identifier for this resource type.
	 */
	private final String name;

	/**
	 * Whether this resource type is measured machine dependent or not. If it is measured
	 * machine dependent, the measurement unit must be {@code ns}.
	 */
	private final boolean isNs;

	/**
	 * Creates a custom resource type.
	 *
	 * @param name A name which is a unique identifier for this resource type. Must not be
	 *            {@code null}.
	 * @param isNs Whether this resource type is measured machine dependent or not. If it
	 *            is measured machine dependent, the measurement unit must be {@code ns}.
	 */
	public ResourceDemandType(final String name, final boolean isNs) {
		super();
		this.name = name;
		this.isNs = isNs;
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
		final ResourceDemandType other = (ResourceDemandType) object;
		return new EqualsBuilder().append(this.name, other.name).append(this.isNs, other.isNs).isEquals();
	}

	/**
	 * Gives a name which is a unique identifier for this resource type. Is never
	 * {@code null}.
	 *
	 * @return a name which is a unique identifier for this resource type. Is never
	 *         {@code null}.
	 */
	public String getName() {
		return this.name;
	}

	@Override
	public int hashCode() {
		// you pick a hard-coded, randomly chosen, non-zero, odd number
		// ideally different for each class
		return new HashCodeBuilder(27, 47).append(this.name).append(this.isNs).toHashCode();
	}

	/**
	 * Whether this resource type is measured machine dependent or not. If it is measured
	 * machine dependent, the measurement unit must be {@code ns}.
	 *
	 * @return the Whether this resource type is measured machine dependent or not. If it
	 *         is measured machine dependent, the measurement unit must be {@code ns}.
	 */
	public boolean isNs() {
		return this.isNs;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("name", this.name).append("isNs", this.isNs).toString();
	}
}
