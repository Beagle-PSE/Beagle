package de.uka.ipd.sdq.beagle.core;

import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertThat;

import de.uka.ipd.sdq.beagle.core.testutil.ThrowingMethod;

import org.junit.Test;

/**
 * Tests {@link de.uka.ipd.sdq.beagle.core.SeffBranch} and contains all test cases needed
 * to check every method.
 *
 * @author Annika Berger
 */
public class ResourceDemandTypeTest {

	/**
	 * Test method for
	 * {@link de.uka.ipd.sdq.beagle.core.ResourceDemandType#ResourceDemandType(java.lang.String, boolean)}
	 * .
	 */
	@Test
	public void testResourceDemandType() {
		new ResourceDemandType("nsTrue", true);
		new ResourceDemandType("nsFalse", false);

		ThrowingMethod method = () -> {
			new ResourceDemandType(null, true);
		};
		assertThat("Name must not be null.", method, throwsException(IllegalArgumentException.class));
		
		method = () -> {
			new ResourceDemandType("", true);
		};
		assertThat("Name must not be empty.", method, throwsException(IllegalArgumentException.class));
	}

	/**
	 * Test method for
	 * {@link de.uka.ipd.sdq.beagle.core.ResourceDemandType#equals(java.lang.Object)} and
	 * {@link de.uka.ipd.sdq.beagle.core.ResourceDemandType#hashCode()}.
	 */
	@Test
	public void testEqualsAndHashCode() {
		final ResourceDemandType test = new ResourceDemandType("test", true);
		final ResourceDemandType testF = new ResourceDemandType("test", false);
		final ResourceDemandType testT = new ResourceDemandType("test", true);
		final ResourceDemandType newTest = new ResourceDemandType("newTest", true);
		
		assertThat(test, is(equalTo(testT)));
		assertThat(test.hashCode(), is(equalTo(testT.hashCode())));
		assertThat(test, is(not(equalTo(testF))));
		assertThat(test, is(not(equalTo(newTest))));
	}

	/**
	 * Test method for {@link de.uka.ipd.sdq.beagle.core.ResourceDemandType#getName()}.
	 */
	@Test
	public void testGetName() {
		final ResourceDemandType test = new ResourceDemandType("test", true);
		final ResourceDemandType newTest = new ResourceDemandType("newTest", true);
		assertThat(test.getName(), is("test"));
		assertThat(newTest.getName(), is("newTest"));
		
	}

	/**
	 * Test method for {@link de.uka.ipd.sdq.beagle.core.ResourceDemandType#isNs()}.
	 */
	@Test
	public void testIsNs() {
		final ResourceDemandType testF = new ResourceDemandType("test", false);
		final ResourceDemandType testT = new ResourceDemandType("test", true);
		assertThat(testF.isNs(), is(false));
		assertThat(testT.isNs(), is(true));
	}

	/**
	 * Test method for {@link de.uka.ipd.sdq.beagle.core.ResourceDemandType#toString()}.
	 */
	@Test
	public void testToString() {
		final ResourceDemandType type = new ResourceDemandType("test", true);
		assertThat(type.toString(), not(startsWith("ResourceDemandType@")));
	}

}
