package de.uka.ipd.sdq.beagle.core;

import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.theInstance;
import static org.junit.Assert.*;

import de.uka.ipd.sdq.beagle.core.testutil.ThrowingMethod;

import org.junit.Test;

import java.io.File;
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
	 * {@link SeffBranch#getBranches()}. Test behaviour of Constructor for a set
	 * containing {@code null}.
	 */
	@Test
	public void testConstructor() {
		final Set<CodeSection> noCodeSections = new HashSet<CodeSection>();
		ThrowingMethod method = () -> {
			new SeffBranch(noCodeSections);
		};
		assertThat("IllegalArgumentException expected for invalid input.", method,
			throwsException(IllegalArgumentException.class));

		final File file = new File(SeffLoopTest.class.getResource("de/uka/ipd/sdq/beale/core/TestFile.java").getPath());
		final int startCodeLine = 4;
		final int endCodeLine = 15;
		final Set<CodeSection> codeSections = new HashSet<CodeSection>();
		codeSections.add(new CodeSection(file, startCodeLine, file, endCodeLine));
		final SeffBranch branch = new SeffBranch(codeSections);
		assertThat(branch.getBranches(), is(theInstance(codeSections)));

		codeSections.add(null);
		method = () -> {
			new SeffBranch(codeSections);
		};
		assertThat("Null Pointer Exception expected as input contained null.", method,
			throwsException(NullPointerException.class));

	}

	/**
	 * Test method for {@link de.uka.ipd.sdq.beagle.core.SeffBranch#getBranches()}.
	 */
	@Test
	public void testGetBranches() {
		final File file = new File(SeffLoopTest.class.getResource("de/uka/ipd/sdq/beale/core/TestFile.java").getPath());
		final int startCodeLine = 4;
		final int endCodeLine = 15;
		final Set<CodeSection> codeSections = new HashSet<CodeSection>();
		codeSections.add(new CodeSection(file, startCodeLine, file, endCodeLine));
		final SeffBranch branch = new SeffBranch(codeSections);
		assertThat(branch.getBranches(), is(theInstance(codeSections)));
	}

}
