package de.uka.ipd.sdq.beagle.core;

/**
 * Models an internal action demanding resources being executed.
 *
 * <p>The internal action applies to a specific <em>resource type</em>. The resource type
 * describes both the resource that was demanded and the unit the result’s value is
 * expressed in. To support extensibility, these types are represented as strings with no
 * inherent restrictions. However, the following Strings should be used per convention:
 *
 * <ul>
 *
 * <li> {@link #RESOURCE_TYPE_CPU}
 *
 * <li> {@link #RESOURCE_TYPE_CPU_MS}
 *
 * <li> {@link #RESOURCE_TYPE_HDD_READ}
 *
 * <li> {@link #RESOURCE_TYPE_HDD_READ_MS}
 *
 * <li> {@link #RESOURCE_TYPE_HDD_WRITE}
 *
 * <li> {@link #RESOURCE_TYPE_HDD_WRITE_MS}
 *
 * <li> {@link #RESOURCE_TYPE_HDD}
 *
 * <li> {@link #RESOURCE_TYPE_HDD_MS}
 *
 * <li> {@link #RESOURCE_TYPE_NETWORK}
 *
 * <li> {@link #RESOURCE_TYPE_NETWORK_MS}
 *
 * </ul> Resource types should use the suffix {@code "_MS"} exactly when they should be
 * measured device dependent in the time unit ms.
 *
 * @author Christoph Michelbach
 * @author Annika Berger
 * @author Roman Langrehr
 *
 */
public class ResourceDemandingInternalAction implements MeasurableSeffElement {

	/**
	 * Common resource type for cycles performed on a CPU.
	 */
	public static final String RESOURCE_TYPE_CPU = "CPU";

	/**
	 * Common resource type for milliseconds of computation performed on a CPU.
	 */
	public static final String RESOURCE_TYPE_CPU_MS = "CPU_MS";

	/**
	 * Common resource type for bytes read from a non-volatile storage.
	 */
	public static final String RESOURCE_TYPE_HDD_READ = "HDD_READ";

	/**
	 * Common resource type for milliseconds of reading operations performed on a
	 * non-volatile storage.
	 */
	public static final String RESOURCE_TYPE_HDD_READ_MS = "HDD_READ_MS";

	/**
	 * Common resource type for milliseconds of writing operations performed on a
	 * non-volatile storage.
	 */
	public static final String RESOURCE_TYPE_HDD_WRITE = "HDD_WRITE";

	/**
	 * Common resource type for bytes transfered to or from a non-volatile storage.
	 */
	public static final String RESOURCE_TYPE_HDD_WRITE_MS = "HDD_WRITE_MS";

	/**
	 * Common resource type for bytes transfered to or from a non-volatile storage.
	 */
	public static final String RESOURCE_TYPE_HDD = "HDD";

	/**
	 * Common resource type for milliseconds of reading or writing operations performed on
	 * a non-volatile storage.
	 */
	public static final String RESOURCE_TYPE_HDD_MS = "HDD_MS";

	/**
	 * Common resource type for bytes send or received over a network interface.
	 */
	public static final String RESOURCE_TYPE_NETWORK = "NETWORK";

	/**
	 * Common resource type for milliseconds spend for network operations.
	 */
	public static final String RESOURCE_TYPE_NETWORK_MS = "NETWORK_MS";

	/**
	 * Serialisation version UID, see {@link java.io.Serializable}.
	 */
	private static final long serialVersionUID = -8743471676122273889L;

	/**
	 * The type of resource that should be measured in this code section.
	 */
	private String resourceType;

	/**
	 * The code section for this resource demand.
	 */
	private CodeSection action;

	/**
	 * Creates a {@link ResourceDemandingInternalAction} for a specific resource type.
	 *
	 * @param resourceType The resource type of this internal action that should be
	 *            measured. See class description for resource types, that should be used
	 *            per convention.
	 * @param action The code section for this resource demanding internal action. The
	 *            first line of the code section is the first line of this internal action
	 *            (inclusive). The last line of the code section is the last line of this
	 *            internal action (inclusive).
	 */
	public ResourceDemandingInternalAction(final String resourceType, final CodeSection action) {
		this.resourceType = resourceType;
		this.action = action;
	}

	/**
	 * Gets this internal action's <em>result type</em>.
	 *
	 * @return This result’s <em>result type</em>, as defined in the class description. Is
	 *         never {@code null}.
	 */
	public String getResourceType() {
		return this.resourceType;
	}

	/**
	 * Gets this internal action's <em>code section</em>.
	 *
	 * @return the code section for this resource demand.
	 */
	public CodeSection getAction() {
		return this.action;
	}
}
