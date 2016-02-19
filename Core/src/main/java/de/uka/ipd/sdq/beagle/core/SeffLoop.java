package de.uka.ipd.sdq.beagle.core;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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
	private final CodeSection loopBody;

	/**
	 * Creates a SeffBranch using a given code section for the loop's body.
	 *
	 * @param loopBody A valid code sections representing the body of this SeffBranch's
	 *            loop. Must not be {@code null}.
	 */
	public SeffLoop(final CodeSection loopBody) {
		Validate.notNull(loopBody);
		this.loopBody = loopBody;
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
		final SeffLoop other = (SeffLoop) object;
		return new EqualsBuilder().append(this.loopBody, other.loopBody).isEquals();
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

	@Override
	public int hashCode() {
		// you pick a hard-coded, randomly chosen, non-zero, odd number
		// ideally different for each class
		return new HashCodeBuilder(19, 51).append(this.loopBody).toHashCode();
	}

	@Override
	public String toString() {
		return String.format("SeffLoop@%4.4s<%s>", Integer.toHexString(this.hashCode()), this.loopBody);
	}
}
