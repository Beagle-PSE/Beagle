package de.uka.ipd.sdq.beagle.core.testutil.factories;

import de.uka.ipd.sdq.beagle.core.CodeSection;
import de.uka.ipd.sdq.beagle.core.SeffLoop;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Factory for pre-initialised Seff Loops to be used by tests.
 *
 * @author Joshua Gleitze
 * @author Ansgar Spiegler
 * @author Annika Berger
 */
public class SeffLoopFactory {

	/**
	 * A {@link CodeSectionFactory} providing {@link CodeSection}s.
	 */
	private static final CodeSectionFactory CODE_SECTION_FACTORY = new CodeSectionFactory();

	/**
	 * Creates a new seff loop.
	 *
	 * @return A newly instantiated seff loop (you may not make any assumptions about).
	 */
	public SeffLoop getOne() {
		return new SeffLoop(CODE_SECTION_FACTORY.getOne());
	}

	/**
	 * Creates an array of newly initialised seff loops.
	 *
	 * @return newly initialised seff loops.
	 */
	public SeffLoop[] getAll() {
		final CodeSection[] codeSections = CODE_SECTION_FACTORY.getAll();
		final int numberOfLoops = codeSections.length;
		final SeffLoop[] seffLoops = new SeffLoop[numberOfLoops];

		for (int i = 0; i < numberOfLoops; i++) {
			seffLoops[i] = new SeffLoop(codeSections[i % codeSections.length]);
		}

		return seffLoops;
	}

	/**
	 * Creates a set of newly initialised seff loops.
	 *
	 * @return newly initialised seff loops.
	 */
	public Set<SeffLoop> getAllAsSet() {
		return new HashSet<>(Arrays.asList(this.getAll()));
	}
}
