package de.uka.ipd.sdq.beagle.core;

/**
 * Models loops (like Java’s for , while and do - while statement) which affect the calls
 * a component makes to other components. Such loops are—contrary to loops that stay
 * within an internal action—modelled in a component’s SEFF.
 *
 * @author Annika Berger
 * @author Roman Langrehr
 */
public class SeffLoop implements MeasurableSeffElement {

	/**
	 * Serialisation version UID, see {@link java.io.Serializable}.
	 */
	private static final long serialVersionUID = 116182090562332981L;

	/**
	 * The code section representing the loop body.
	 */
	private CodeSection loopBody;

	/**
	 * Creates a SeffBranch using a given code section for the loop's body.
	 *
	 * @param loopBody A valid code sections representing the body of this SeffBranch's
	 *            loop. Must not be {@code null}.
	 */
	public SeffLoop(final CodeSection loopBody) {
		this.loopBody = loopBody;
	}

	/**
	 * Gives a valid code sections representing the body of this SeffBranch's loop.
	 *
	 * @return the loopBody A valid code sections representing the body of this
	 *         SeffBranch's loop. Is never {@code null}.
	 */
	public CodeSection getLoopBody() {
		return this.loopBody;
	}
}
