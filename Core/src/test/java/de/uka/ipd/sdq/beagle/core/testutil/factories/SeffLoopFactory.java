package de.uka.ipd.sdq.beagle.core.testutil.factories;

import de.uka.ipd.sdq.beagle.core.SeffLoop;

<<<<<<< HEAD
=======
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

>>>>>>> origin/master
/**
 * Factory for pre-initialised Seff Loops to be used by tests.
 *
 * @author Joshua Gleitze
<<<<<<< HEAD
=======
 * @author Ansgar Spiegler
>>>>>>> origin/master
 */
public class SeffLoopFactory {

	/**
	 * Creates a new seff loop.
	 *
	 * @return A newly instantiated seff loop (you may not make any assumptions about).
	 */
	public SeffLoop getOne() {
		return new SeffLoop();
	}

	/**
	 * Creates an array of newly initialised seff loops.
	 *
	 * @return 3 newly initialised seff loops.
	 */
	public SeffLoop[] getAll() {
		// will be done right once the proper SeffBranch constructor is there.
		return new SeffLoop[] {new SeffLoop(), new SeffLoop(), new SeffLoop()};
	}
<<<<<<< HEAD
=======

	/**
	 * Creates a set of newly initialised seff loops.
	 *
	 * @return 3 newly initialised seff loops.
	 */
	public Set<SeffLoop> getAllAsSet() {
		return new HashSet<>(Arrays.asList(this.getAll()));
	}
>>>>>>> origin/master
}
