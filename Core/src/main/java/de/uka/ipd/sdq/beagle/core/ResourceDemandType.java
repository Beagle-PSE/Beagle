package de.uka.ipd.sdq.beagle.core;

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
	private String name;

	/**
	 * Whether this resource type is measured machine dependent or not. If it is measured
	 * machine dependent, the measurement unit must be {@code ns}.
	 */
	private boolean isNs;

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
}
