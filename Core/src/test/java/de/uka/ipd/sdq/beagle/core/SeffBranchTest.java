package de.uka.ipd.sdq.beagle.core;

import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.CoreMatchers.theInstance;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import de.uka.ipd.sdq.beagle.core.testutil.CodeSectionFactory;
import de.uka.ipd.sdq.beagle.core.testutil.ThrowingMethod;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * Tests {@link de.uka.ipd.sdq.beage.core.SeffBranch} and contains all test cases needed
 * to check every method.
 *
 * @author Annika Berger
 */
public class SeffBranchTest {

	/**
	 * Test method for
	 * {@link de.uka.ipd.sdq.beagle.core.SeffBranch#SeffBranch(java.util.Set)}.
	 *
	 * <p>Asserts that an {@link IllegalArgumentException} is thrown when the set of
	 * {@link CodeSection} does not contain any CodeSections. Asserts that input Set of
	 * {@link CodeSection} is the same instance as the one returned with
	 * {@link SeffBranch#getBranches()}. Tests behaviour of Constructor for a set
	 * containing {@code null} and assures that changing the Set after inizialising a new
	 * branch does not affect the Branch.
	 */
	@Test
	public void testConstructor() {
		final Set<CodeSection> noCodeSections = new HashSet<CodeSection>();
		ThrowingMethod method = () -> {
			new SeffBranch(noCodeSections);
		};
		assertThat("IllegalArgumentException expected for invalid input.", method,
			throwsException(IllegalArgumentException.class));

		final Set<CodeSection> codeSections = new HashSet<CodeSection>();
		final CodeSection[] codeSecs = CodeSectionFactory.getAllCodeSections();
		codeSections.add(codeSecs[0]);

		method = () -> {
			new SeffBranch(codeSections);
		};
		assertThat("IllegalArgumentException expected for invalid input.", method,
			throwsException(IllegalArgumentException.class));

		codeSections.add(codeSecs[1]);
		final SeffBranch branch = new SeffBranch(codeSections);
		final int amountBranches = 2;

		for (int i = amountBranches; i < codeSecs.length; i++) {
			codeSections.add(codeSecs[i]);
		}
		assertThat(branch.getBranches().size(), is(equalTo(amountBranches)));

		codeSections.add(null);
		method = () -> {
			new SeffBranch(codeSections);
		};
		assertThat("Null Pointer Exception expected as input contained null.", method,
			throwsException(NullPointerException.class));

	}

	/**
	 * Test method for {@link de.uka.ipd.sdq.beagle.core.SeffBranch#equals()}.
	 *
	 * <p>Asserts that two SeffBranches inizialised with a Set containing the same code
	 * sections are equal while inizialised with Sets containing different code sections
	 * are different. This test fails if there is only one code section defined in the
	 * {@link CodeSectionFactory}.
	 */
	@Test
	public void testEquals() {
		final Set<CodeSection> codeSectionsA = new HashSet<>();
		final Set<CodeSection> codeSectionsB = new HashSet<>();
		final Set<CodeSection> codeSectionsC = new HashSet<>();
		final CodeSection[] codeSecs = CodeSectionFactory.getAllCodeSections();
		final int minAmount = 2;
		if (codeSecs.length > minAmount) {
			for (final CodeSection codeSection : codeSecs) {
				codeSectionsA.add(codeSection);
				codeSectionsB.add(codeSection);
			}
			codeSectionsC.add(codeSecs[0]);
			codeSectionsC.add(codeSecs[1]);
			final SeffBranch branchA = new SeffBranch(codeSectionsA);
			final SeffBranch branchB = new SeffBranch(codeSectionsB);
			final SeffBranch branchC = new SeffBranch(codeSectionsC);

			assertThat(branchB, is(equalTo(branchA)));
			assertThat(branchC, is(not(equalTo(branchA))));
		} else {
			fail("There have to be minimum three CodeSections in the CodeSectionFactory to test this method properly.");
		}
	}

	/**
	 * Test method for {@link de.uka.ipd.sdq.beagle.core.SeffBranch#getBranches()}.
	 */
	@Test
	public void testGetBranches() {
		final Set<CodeSection> codeSections = new HashSet<CodeSection>();
		for (final CodeSection codeSection : CodeSectionFactory.getAllCodeSections()) {
			codeSections.add(codeSection);
		}
		final SeffBranch branch = new SeffBranch(codeSections);
		assertThat(branch.getBranches(), is(theInstance(codeSections)));
	}

	/**
	 * Test method for {@link de.uka.ipd.sdq.beagle.core.SeffBranch#toString()}.
	 */
	@Test
	public void testToString() {
		final Set<CodeSection> codeSections = new HashSet<CodeSection>();
		for (final CodeSection codeSection : CodeSectionFactory.getAllCodeSections()) {
			codeSections.add(codeSection);
		}
		final SeffBranch branch = new SeffBranch(codeSections);

		assertThat(branch.toString(), not(startsWith("SeffBranch@")));
	}

}
