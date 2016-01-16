package de.uka.ipd.sdq.beagle.core;

import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.startsWith;
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

		final File file = new File(SeffBranchTest.class.getResource("de/uka/ipd/sdq/beale/core/TestFile.java").getPath());
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
		final File file = new File(SeffBranchTest.class.getResource("de/uka/ipd/sdq/beale/core/TestFile.java").getPath());
		final int startCodeLine = 4;
		final int endCodeLine = 15;
		final Set<CodeSection> codeSections = new HashSet<CodeSection>();
		codeSections.add(new CodeSection(file, startCodeLine, file, endCodeLine));
		final SeffBranch branch = new SeffBranch(codeSections);
		assertThat(branch.getBranches(), is(theInstance(codeSections)));
	}
	
	/**
	 * Test method for {@link de.uka.ipd.sdq.beagle.core.SeffBranch#equals()}.
	 */
	@Test
	public void testEquals() {
		final File file = new File(SeffBranchTest.class.getResource("de/uka/ipd/sdq/beale/core/TestFile.java").getPath());
		final int startCodeLine = 4;
		final int endCodeLine = 15;
		final Set<CodeSection> codeSections = new HashSet<CodeSection>();
		codeSections.add(new CodeSection(file, startCodeLine, file, endCodeLine));
		final SeffBranch branch = new SeffBranch(codeSections);
		
		final File fileB = new File(SeffBranchTest.class.getResource("de/uka/ipd/sdq/beale/core/TestFile.java").getPath());
		final int startCodeLineB = 4;
		final int endCodeLineB = 15;
		final Set<CodeSection> codeSectionsB = new HashSet<CodeSection>();
		codeSections.add(new CodeSection(fileB, startCodeLineB, fileB, endCodeLineB));
		final SeffBranch branchB = new SeffBranch(codeSectionsB);
		
		final File fileC = new File(SeffBranchTest.class.getResource("de/uka/ipd/sdq/beale/core/PingPong.java").getPath());
		final int startCodeLineC = 4;
		final int endCodeLineC = 15;
		final Set<CodeSection> codeSectionsC = new HashSet<CodeSection>();
		codeSections.add(new CodeSection(fileC, startCodeLineC, fileC, endCodeLineC));
		final SeffBranch branchC = new SeffBranch(codeSectionsC);
		
		assertThat(branchC.equals(branch), is(equalTo(false)));
		assertThat(branchB.equals(branch), is(equalTo(true)));
	}
	
	/**
	 * Test method for {@link de.uka.ipd.sdq.beagle.core.SeffBranch#toString()}.
	 */
	@Test
	public void testToString() {
		final File file = new File(SeffBranchTest.class.getResource("de/uka/ipd/sdq/beale/core/TestFile.java").getPath());
		final int startCodeLine = 4;
		final int endCodeLine = 15;
		final Set<CodeSection> codeSections = new HashSet<CodeSection>();
		codeSections.add(new CodeSection(file, startCodeLine, file, endCodeLine));
		final SeffBranch branch = new SeffBranch(codeSections);
		
		assertThat(branch.toString(), not(startsWith("SeffBranch@")));
	}

}
