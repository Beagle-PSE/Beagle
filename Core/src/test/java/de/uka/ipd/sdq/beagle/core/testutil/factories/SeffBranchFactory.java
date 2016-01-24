package de.uka.ipd.sdq.beagle.core.testutil.factories;

import de.uka.ipd.sdq.beagle.core.CodeSection;
import de.uka.ipd.sdq.beagle.core.SeffBranch;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Factory for pre-initialised Seff Branches to be used by tests.
 *
 * @author Joshua Gleitze
 * @author Ansgar Spiegler
 * @author Annika Berger
 */
public class SeffBranchFactory {

	/**
	 * A {@link CodeSectionFactory} providing {@link CodeSection}s.
	 */
	private static final CodeSectionFactory CODE_SECTION_FACTORY = new CodeSectionFactory();

	/**
	 * Creates a new seff branch.
	 *
	 * @return A newly instantiated seff branch (you may not make any assumptions about).
	 */
	public SeffBranch getOne() {
		return new SeffBranch(CODE_SECTION_FACTORY.getAllAsSet());
	}

	/**
	 * Creates an array of newly initialised seff branches containing as different sets of
	 * {@link CodeSection}s as possible.
	 *
	 * @return newly initialised seff branches.
	 */
	public SeffBranch[] getAll() {
		final CodeSection[] codeSections = CODE_SECTION_FACTORY.getAll();
		final int numberOfBranches = codeSections.length;
		final SeffBranch[] seffBranches = new SeffBranch[numberOfBranches];
		final Set<CodeSection> codeSectionSet = new HashSet<>(Arrays.asList(codeSections[0], codeSections[1]));

		// This is creating as different as possible Sets to use for the seffBranches.
		for (int i = 0; i < numberOfBranches; i++) {
			seffBranches[i] = new SeffBranch(codeSectionSet);
			codeSectionSet.add(codeSections[2 * (i + 1) % codeSections.length]);
			codeSectionSet.add(codeSections[(2 * (i + 1) + 1) % codeSections.length]);
			codeSectionSet.remove(codeSections[i % codeSections.length]);
			codeSectionSet.remove(codeSections[7 * i % codeSections.length]);
		}
		return seffBranches;
	}

	/**
	 * Creates a set of newly initialised seff branches containing as different sets of
	 * {@link CodeSection}s as possible.
	 *
	 * @return newly initialised seff branches.
	 */
	public Set<SeffBranch> getAllAsSet() {
		return new HashSet<>(Arrays.asList(this.getAll()));
	}
}
