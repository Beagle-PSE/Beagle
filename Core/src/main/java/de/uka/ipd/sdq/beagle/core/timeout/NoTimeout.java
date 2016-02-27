package de.uka.ipd.sdq.beagle.core.timeout;

import org.apache.commons.lang3.Validate;

/**
 * Always says that the timeout isn't reached.
 *
 * @author Christoph Michelbach
 */
public class NoTimeout extends Timeout {

	@Override
	public boolean isReached() {
		Validate.isTrue(this.initilised);

		return false;
	}

	@Override
	public void reportOneStepProgress() {
		Validate.isTrue(this.initilised);
	}

}
