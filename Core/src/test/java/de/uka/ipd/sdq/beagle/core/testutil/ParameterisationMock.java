package de.uka.ipd.sdq.beagle.core.testutil;

import de.uka.ipd.sdq.beagle.core.measurement.Parameterisation;

/**
 * Mocks Parametrisation as it is not implemented yet. This enables deciding whether
 * equals retuns {@code true} or {@code false}.
 * 
 * @author Annika Berger
 */
public class ParameterisationMock extends Parameterisation {

	/**
	 * Is {@code false} if instance.equals(ob) should return false, else {@code true}.
	 */
	private boolean equals;

	/**
	 * This method is used to set the result of the standard equals method.
	 *
	 * @param value which should be returned if equals is called.
	 */
	public void setEquals(final boolean value) {
		this.equals = value;
	}

	@Override
	public boolean equals(final Object object) {
		return this.equals;
	}

	@Override
	public int hashCode() {
		// 123 is returned as a hashCode() method is needed but the result of the equals
		// method is not dependent from the object
		return 123;
	}

}
