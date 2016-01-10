package de.uka.ipd.sdq.beagle.core;

/**
 * Models branches in a component's SEFF, originating from conditional constructs.
 *
 * <p>SEFF conditions are conditions (like Java’s if , if - else and switch - case
 * statements) which affect the calls a component makes to other components. Such
 * conditions are—contrary to conditions that stay within an internal action—modelled in a
 * component’s SEFF.
 *
 * @author Annika Berger
 */
public class SeffBranch implements MeasurableSeffElement {

	/**
	 * Serialisation version UID, see {@link java.io.Serializable}.
	 */
	private static final long serialVersionUID = -1525692970296450080L;

}
