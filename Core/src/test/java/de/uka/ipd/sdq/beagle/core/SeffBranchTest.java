package de.uka.ipd.sdq.beagle.core;

import static de.uka.ipd.sdq.beagle.core.testutil.EqualsMatcher.hasDefaultEqualsProperties;
import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static de.uka.ipd.sdq.beagle.core.testutil.ToStringMatcher.hasOverriddenToString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.fail;

import de.uka.ipd.sdq.beagle.core.testutil.ThrowingMethod;
import de.uka.ipd.sdq.beagle.core.testutil.factories.CodeSectionFactory;

import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Tests {@link SeffBranch} and contains all test cases needed to check every method.
 *
 * @author Annika Berger
 */
public class SeffBranchTest {

	/**
	 * A {@link CodeSectionFactory}, which is able to generate {@link CodeSection}s.
	 */
	private static final CodeSectionFactory CODE_SECTION_FACTORY = new CodeSectionFactory();

	/**
	 * Test method for {@link SeffBranch#SeffBranch(java.util.Set)}.
	 *
	 * <p>Asserts that an {@link IllegalArgumentException} is thrown when the set of
	 * {@link CodeSection} does not contain any CodeSections. Asserts that input Set of
	 * {@link CodeSection} is the same instance as the one returned with
	 * {@link SeffBranch#getBranches()}. Tests behaviour of Constructor for a set
	 * containing {@code null} and assures that changing the Set after inizialising a new
	 * branch does not affect the Branch.
	 */
	@Test
	public void constructor() {
		final Set<CodeSection> noCodeSections = new HashSet<CodeSection>();
		ThrowingMethod method = () -> {
			new SeffBranch(noCodeSections);
		};
		assertThat("Set must not be empty.", method, throwsException(IllegalArgumentException.class));

		final Set<CodeSection> codeSections = new HashSet<CodeSection>();
		final CodeSection[] codeSecs = CODE_SECTION_FACTORY.getAll();
		codeSections.add(codeSecs[0]);

		method = () -> {
			new SeffBranch(codeSections);
		};
		assertThat("Set must contain at least two code sections.", method,
			throwsException(IllegalArgumentException.class));

		codeSections.add(codeSecs[1]);
		final SeffBranch branch = new SeffBranch(codeSections);
		final int amountBranches = 2;

		for (int i = amountBranches; i < codeSecs.length; i++) {
			codeSections.add(codeSecs[i]);
		}
		assertThat(
			"Adding code sections after initialisation must not influence number of codesections in seff branch.",
			branch.getBranches().size(), is(equalTo(amountBranches)));

		codeSections.add(null);
		method = () -> {
			new SeffBranch(codeSections);
		};
		assertThat("Set must not contain null.", method, throwsException(IllegalArgumentException.class));

		method = () -> {
			new SeffBranch(null);
		};
		assertThat("Set must not be null.", method, throwsException(NullPointerException.class));

	}

	/**
	 * Test method for {@link SeffBranch#equals()} and {@link SeffBranch#hashCode()}.
	 *
	 * <p>Asserts that two SeffBranches inizialised with a Set containing the same code
	 * sections are equal while inizialised with Sets containing different code sections
	 * are different. This test fails if there is only one code section defined in the
	 * {@link CodeSectionFactory}.
	 */
	@Test
	public void equalsAndHashCode() {
		assertThat(new SeffBranch(CODE_SECTION_FACTORY.getAllAsSet()), hasDefaultEqualsProperties());
		final Set<CodeSection> codeSectionsA = new HashSet<>();
		final Set<CodeSection> codeSectionsB = new HashSet<>();
		final Set<CodeSection> codeSectionsC = new HashSet<>();
		final Set<CodeSection> codeSectionsD = new HashSet<>();
		final CodeSection[] codeSecs = CODE_SECTION_FACTORY.getAll();
		if (codeSecs.length > 2) {
			for (final CodeSection codeSection : codeSecs) {
				codeSectionsA.add(codeSection);
				codeSectionsB.add(codeSection);
			}
			codeSectionsC.add(CODE_SECTION_FACTORY.getAll()[0]);
			codeSectionsC.add(CODE_SECTION_FACTORY.getAll()[1]);
			codeSectionsD.add(CODE_SECTION_FACTORY.getAll()[codeSecs.length - 1]);
			codeSectionsD.add(CODE_SECTION_FACTORY.getAll()[codeSecs.length - 2]);

			final SeffBranch branchA = new SeffBranch(codeSectionsA);
			final SeffBranch branchB = new SeffBranch(codeSectionsB);
			final SeffBranch branchC = new SeffBranch(codeSectionsC);
			final SeffBranch branchD = new SeffBranch(codeSectionsD);

			assertThat(branchB, is(equalTo(branchA)));
			assertThat(branchB.hashCode(), is(equalTo(branchA.hashCode())));
			assertThat(branchC, is(not(equalTo(branchA))));
			assertThat(branchD, is(not(equalTo(branchC))));
		} else {
			fail("There have to be minimum three CodeSections in the CodeSectionFactory to test this method properly.");
		}
	}

	/**
	 * Test method for {@link SeffBranch#getBranches()}.
	 */
	@Test
	public void getBranches() {
		final Set<CodeSection> codeSectionSet = CODE_SECTION_FACTORY.getAllAsSet();
		final CodeSection testSection = CODE_SECTION_FACTORY.getOne();
		codeSectionSet.remove(testSection);
		final CodeSection[] codeSections = codeSectionSet.toArray(new CodeSection[codeSectionSet.size()]);
		final SeffBranch branch = new SeffBranch(codeSectionSet);
		assertThat(branch.getBranches(), contains(codeSections));

		// make sure branches are a copy.
		final List<CodeSection> branches = branch.getBranches();
		branches.add(testSection);
		assertThat("List returned must be a copy.", branch.getBranches(), contains(codeSections));
	}

	/**
	 * Test method for {@link SeffBranch#toString()}.
	 */
	@Test
	public void toStringT() {
		final Set<CodeSection> codeSections = CODE_SECTION_FACTORY.getAllAsSet();
		final SeffBranch branch = new SeffBranch(codeSections);

		assertThat(branch, hasOverriddenToString());
	}

}
