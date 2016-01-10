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
 * <li> {@code CPU}: cycles performed on a CPU
 *
 * <li>{@code CPU_MS}: milliseconds of computation performed on a CPU
 *
 * <li>{@code HDD_READ}: bytes read from a non-volatile storage
 *
 * <li> {@code HDD_READ_MS}: milliseconds of reading operations performed on a
 * non-volatile storage
 *
 * <li> {@code HDD_WRITE}: bytes written to a non-volatile storage
 *
 * <li> {@code HDD_WRITE_MS}: milliseconds of writing operations performed on a
 * non-volatile storage
 *
 * <li> {@code HDD}: bytes transfered to or from a non-volatile storage
 *
 * <li> {@code HDD_MS}: milliseconds of reading or writing operations performed on a
 * non-volatile storage
 *
 * </ul>
 * 
 * @author Christoph Michelbach
 * @author Annika Berger
 *
 */
public class ResourceDemandingInternalAction extends MeasurableSeffElement {

	/**
	 * Serialisation version UID, see {@link java.io.Serializable}.
	 */
	private static final long serialVersionUID = -8743471676122273889L;
	
	/**
	 * Gets this internal action's <em>result type</em>.
	 *
	 * @return This result’s <em>result type</em>, as defined in the class description. Is
	 *         never {@code null}.
	 */
	public String getResourceType() {
		return null;
	}
	

	/**
	 * Sets this internal action’s <em>result type</em>.
	 *
	 * @param resourceType This internal actions's <em>result type</em>, as defined in the class
	 *            description. May not be {@code null}.
	 * @throws NullPointerException If {@code resourceType} is {@code null}.
	 */
	public void setResourceType(final String resourceType) {
	}
}
