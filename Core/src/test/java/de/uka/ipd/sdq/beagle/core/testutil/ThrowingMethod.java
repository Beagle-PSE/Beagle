package de.uka.ipd.sdq.beagle.core.testutil;

/**
 * Functional Interface for a method expected to throw an exception.
 *
 * @author Joshua Gleitze
 */
public interface ThrowingMethod {

	/**
	 * Executes a method call that is expected to throw an exception.
	 *
	 * @throws Exception The method is expected to throw an exception.
	 */
	void throwException() throws Exception;
}
