package de.uka.ipd.sdq.beagle.core.measurement.order;

/**
 * Interface for classes that execute the code under test.
 *
 * @author Roman Langrehr
 */
public interface LaunchConfiguration {

	/**
	 * Executes the code under test.
	 */
	void execute();
}