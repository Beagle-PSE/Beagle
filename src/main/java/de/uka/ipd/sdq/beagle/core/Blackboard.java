package de.uka.ipd.sdq.beagle.core;

import java.io.Serializable;
import java.util.Set;

/*
 * ATTENTION: Checkstyle turned off!
 * remove this comment block when implementing this class!
 *
 * CHECKSTYLE:OFF
 *
 * TODO
 */

/**
 * Central and only storage of all knowledge gained by Beagle. Implements, together with
 * {@link BeagleController}, the Blackboard pattern from POSA I. The Blackboard’s
 * vocabulary consists of TODO: reference vocabulary types. It further allows classes to
 * store custom data.
 * <p>
 * The Blackboard is typically not accessed directly by its using classes, but through
 * <em>blackboard views</em> (recognisable by having the {@code BlackboardView} suffix).
 * These are surrogates for the blackboard. They don’t modify its contents but only
 * restrict access to it.
 *
 * @author Joshua Gleitze
 * @see BeagleController
 */
public class Blackboard implements Serializable {
	/**
	 * Serialisation version UID, see {@link java.io.Serializable}.
	 */
	private static final long serialVersionUID = 6382577321150787599L;

	/**
	 * All {@linkplain SEFFLoop SEFF loops} known to Beagle.
	 *
	 * @return all {@linkplain SEFFLoop SEFF loops} known to Beagle.
	 */
	public Set<SEFFLoop> getAllSEFFLoops() {
		// TODO implement this method
		return null;
	}

	/**
	 * All {@linkplain SEFFBranch SEFF branches} known to Beagle.
	 *
	 * @return all {@linkplain SEFFBranch SEFF branches} known to Beagle.
	 */
	public Set<SEFFBranch> getAllSEFFBranches() {
		// TODO implement this method
		return null;
	}

	/**
	 * All {@linkplain ResourceDemandingInternalAction resource demanding internal
	 * actions} known to Beagle.
	 *
	 * @return all {@linkplain ResourceDemandingInternalAction resource demanding internal
	 *         actions} known to Beagle.
	 */
	public Set<ResourceDemandingInternalAction> getAllRDIAs() {
		// TODO implement this method
		return null;
	}

	/**
	 * Writes data for {@code writer} to the blackboard. This method serves as a type safe
	 * mean for tools to store data that is not part of their results. Values stored here
	 * will never contribute to Beagle’s results. Any class calling this method should
	 * always pass its own {@linkplain Class} instance as {@code writer}. {@code writer}
	 * has to implement {@link BlackboardStorer} and thereby declare the type of values
	 * they write. Calling this method will override any data potentially stored
	 * previously for the given {@code writer}.
	 *
	 * @param writer
	 *            The class the data should be written for.
	 * @param written
	 *            The data to write.
	 * @param <WRITTEN_TYPE>
	 *            {@code written}’s type.
	 */
	public <WRITTEN_TYPE extends Serializable> void writeFor(
			final Class<? extends BlackboardStorer<WRITTEN_TYPE>> writer, final WRITTEN_TYPE written) {
		// TODO implement this method
	}

	/**
	 * Reads data previously written for {@code writer} through
	 * {@link #writeFor(Class, Serializable)}.
	 *
	 * @param writer
	 *            The class the desired data was written for.
	 * @param <WRITTEN_TYPE>
	 *            The type of the data to be read.
	 * @return The data written in the last call to {@linkplain #writeFor} for
	 *         {@code writer}. {@code null} if no data has been written for {@code writer}
	 *         yet.
	 * @see #writeFor(Class, Serializable)
	 */
	public <WRITTEN_TYPE extends Serializable> WRITTEN_TYPE readFor(
			final Class<? extends BlackboardStorer<WRITTEN_TYPE>> writer) {
		// TODO implement this method
		return null;
	}
}
