package de.uka.ipd.sdq.beagle.core;

import static de.uka.ipd.sdq.beagle.core.testutil.EqualsMatcher.hasDefaultEqualsProperties;
import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static de.uka.ipd.sdq.beagle.core.testutil.ToStringMatcher.hasOverriddenToString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import de.uka.ipd.sdq.beagle.core.testutil.ThrowingMethod;

import org.junit.Test;

/**
 * Tests {@link ResourceDemandType} and contains all test cases needed to check every
 * method.
 *
 * @author Annika Berger
 */
public class ResourceDemandTypeTest {

	/**
	 * Test method for
	 * {@link ResourceDemandType#ResourceDemandType(java.lang.String, boolean)} .
	 */
	@Test
	public void constructor() {
		new ResourceDemandType("nsTrue", true);
		new ResourceDemandType("nsFalse", false);

		ThrowingMethod method = () -> {
			new ResourceDemandType(null, true);
		};
		assertThat("Name must not be null.", method, throwsException(NullPointerException.class));

		method = () -> {
			new ResourceDemandType("", true);
		};
		assertThat("Name must not be empty.", method, throwsException(IllegalArgumentException.class));
	}

	/**
	 * Test method for {@link ResourceDemandType#equals(java.lang.Object)} and
	 * {@link ResourceDemandType#hashCode()}.
	 */
	@Test
	public void equalsAndHashCode() {
		assertThat(ResourceDemandType.RESOURCE_TYPE_CPU, hasDefaultEqualsProperties());
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
	 * Test method for {@link ResourceDemandType#getName()}.
	 */
	@Test
	public void getName() {
		final ResourceDemandType test = new ResourceDemandType("test", true);
		final ResourceDemandType newTest = new ResourceDemandType("newTest", true);
		assertThat(test.getName(), is("test"));
		assertThat(newTest.getName(), is("newTest"));

	}

	/**
	 * Test method for {@link ResourceDemandType#isNs()}.
	 */
	@Test
	public void isNs() {
		final ResourceDemandType testF = new ResourceDemandType("test", false);
		final ResourceDemandType testT = new ResourceDemandType("test", true);
		assertThat(testF.isNs(), is(false));
		assertThat(testT.isNs(), is(true));
	}

	/**
	 * Test method for {@link ResourceDemandType#toString()}.
	 */
	@Test
	public void toStringT() {
		final ResourceDemandType type = new ResourceDemandType("test", true);
		assertThat(type, hasOverriddenToString());
	}

}
