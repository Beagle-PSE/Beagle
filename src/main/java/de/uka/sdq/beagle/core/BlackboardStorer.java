package de.uka.sdq.beagle.core;

import java.io.Serializable;

/**
 * Interface for classes that wish to write something to the Blackboard.
 *
 * @author Joshua Gleitze
 * @param <WRITTEN_TYPE>
 *            The type of data the BlackboardStorer wants to write to the blackboard.
 */
public interface BlackboardStorer<WRITTEN_TYPE extends Serializable> {
}
