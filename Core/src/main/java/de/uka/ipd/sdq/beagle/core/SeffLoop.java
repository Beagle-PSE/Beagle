package de.uka.ipd.sdq.beagle.core;

/**
 * Models loops (like Java’s for , while and do - while statement) which affect the calls
 * a component makes to other components. Such loops are—contrary to loops that stay
 * within an internal action—modelled in a component’s SEFF.
 * 
 * @author Annika Berger
 *
 */
public class SeffLoop extends MeasurableSeffElement {

	/**
	 * Serialisation version UID, see {@link java.io.Serializable}.
	 */
	private static final long serialVersionUID = 116182090562332981L;

}
