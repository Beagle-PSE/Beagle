package de.uka.ipd.sdq.beagle.core.timeout;

public class NoTimeout implements Timeout {

	@Override
	public boolean isReached() {
		return false;
	}

}
