package de.uka.ipd.sdq.beagle.core;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * A parameter of an external call.
 *
 * @author Christoph Michelbach
 * @author Roman Langrehr
 */
public class ExternalCallParameter implements MeasurableSeffElement {

	/**
	 * Serialisation version UID, see {@link java.io.Serializable}.
	 */
	private static final long serialVersionUID = 9085530983738219258L;

	/**
	 * The code section where the external call will be made.
	 */
	private final CodeSection call;

	/**
	 * The parameters index in the parameter list. Starting with {@code 0}.
	 */
	private final int index;

	/**
	 * Creates a parameter of an external call.
	 *
	 * @param call The code section where the external call will be made. Must not be
	 *            {@code null}.
	 * @param index The parameters index in the parameter list. Starting with {@code 0}.
	 */
	public ExternalCallParameter(final CodeSection call, final int index) {
		Validate.isTrue(index >= 0, "The index must be non-neagtive, but was %d", index);
		Validate.notNull(call);
		this.call = call;
		this.index = index;
	}

	/**
	 * Gives the code section where the external call will be made.
	 *
	 * @return the code section where the external call will be made.
	 */
	public CodeSection getCallCodeSection() {
		return this.call;
	}

	/**
	 * Gives the parameters index in the parameter list. Starting with {@code 0}.
	 *
	 * @return the parameters index in the parameter list. Starting with {@code 0}.
	 */
	public int getIndex() {
		return this.index;
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
		final ExternalCallParameter other = (ExternalCallParameter) object;
		return new EqualsBuilder().append(this.call, other.call).append(this.index, other.index).isEquals();
	}

	@Override
	public int hashCode() {
		// you pick a hard-coded, randomly chosen, non-zero, odd number
		// ideally different for each class
		return new HashCodeBuilder(49, 151).append(this.call).append(this.index).toHashCode();
	}

	@Override
	public String toString() {
		return String.format("ExCall@%4.4s<%d,%s>", Integer.toHexString(this.hashCode()), this.index, this.call);
	}
}
