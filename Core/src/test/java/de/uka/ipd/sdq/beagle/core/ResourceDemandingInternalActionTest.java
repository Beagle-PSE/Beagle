package de.uka.ipd.sdq.beagle.core;

import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.theInstance;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import de.uka.ipd.sdq.beagle.core.testutil.ThrowingMethod;

import org.junit.Test;

import java.io.File;

/**
 * Tests {@link de.uka.ipd.sdq.beage.core.ResourceDemandingInternalAction} and contains
 * all test cases needed to check every method.
 * 
 * @author Annika Berger
 */
public class ResourceDemandingInternalActionTest {

	/**
	 * Asserts that the constructor is working correctly.
	 *
	 */
	@Test
	public void testResourceDemandingInternalAction() {
		final File file = new File(
			ResourceDemandingInternalAction.class.getResource("de/uka/ipd/sdq/beale/core/TestFile.java").getPath());
		final int startCodeLine = 4;
		final int endCodeLine = 15;
		final CodeSection codeSection = new CodeSection(file, startCodeLine, file, endCodeLine);
		ResourceDemandingInternalAction rdia =
			new ResourceDemandingInternalAction(ResourceDemandType.RESOURCE_TYPE_CPU, codeSection);
		assertThat(rdia.getResourceType(), is(equalTo(ResourceDemandType.RESOURCE_TYPE_CPU)));

		rdia = new ResourceDemandingInternalAction(ResourceDemandType.RESOURCE_TYPE_HDD, codeSection);
		assertThat(rdia.getResourceType(), is(equalTo(ResourceDemandType.RESOURCE_TYPE_HDD)));

		rdia = new ResourceDemandingInternalAction(ResourceDemandType.RESOURCE_TYPE_NETWORK, codeSection);
		assertThat(rdia.getResourceType(), is(equalTo(ResourceDemandType.RESOURCE_TYPE_NETWORK)));

		ThrowingMethod method = () -> {
			new ResourceDemandingInternalAction(null, codeSection);
		};
		assertThat("NullPointerException for input null.", method, throwsException(NullPointerException.class));

		final File secFile = new File(
			ResourceDemandingInternalAction.class.getResource("de/uka/ipd/sdq/beale/core/PingPong.java").getPath());
		final int startIndex = 60;
		final int endIndex = 68;
		final CodeSection secCodeSection = new CodeSection(secFile, startIndex, file, endIndex);
		rdia = new ResourceDemandingInternalAction(ResourceDemandType.RESOURCE_TYPE_CPU_NS, secCodeSection);
		assertThat(rdia.getResourceType(), is(equalTo(ResourceDemandType.RESOURCE_TYPE_CPU_NS)));

		rdia = new ResourceDemandingInternalAction(ResourceDemandType.RESOURCE_TYPE_HDD_NS, secCodeSection);
		assertThat(rdia.getResourceType(), is(equalTo(ResourceDemandType.RESOURCE_TYPE_HDD_NS)));

		rdia = new ResourceDemandingInternalAction(ResourceDemandType.RESOURCE_TYPE_NETWORK_NS, secCodeSection);
		assertThat(rdia.getResourceType(), is(equalTo(ResourceDemandType.RESOURCE_TYPE_NETWORK_NS)));

		method = () -> {
			new ResourceDemandingInternalAction(ResourceDemandType.RESOURCE_TYPE_NETWORK_NS, null);
		};
		assertThat("NullPointerException for input null.", method, throwsException(NullPointerException.class));
	}

	/**
	 * Test method for
	 * {@link de.uka.ipd.sdq.beage.core.ResourceDemandingInternalAction#getResourceType()}
	 * .
	 * 
	 */
	@Test
	public void testGetResourceType() {
		final File file = new File(
			ResourceDemandingInternalAction.class.getResource("de/uka/ipd/sdq/beale/core/TestFile.java").getPath());
		final int startCodeLine = 4;
		final int endCodeLine = 15;
		final CodeSection codeSection = new CodeSection(file, startCodeLine, file, endCodeLine);
		ResourceDemandingInternalAction rdia =
			new ResourceDemandingInternalAction(ResourceDemandType.RESOURCE_TYPE_CPU, codeSection);
		assertThat(rdia.getResourceType(), is(equalTo(ResourceDemandType.RESOURCE_TYPE_CPU)));

		rdia = new ResourceDemandingInternalAction(ResourceDemandType.RESOURCE_TYPE_HDD, codeSection);
		assertThat(rdia.getResourceType(), is(equalTo(ResourceDemandType.RESOURCE_TYPE_HDD)));

		rdia = new ResourceDemandingInternalAction(ResourceDemandType.RESOURCE_TYPE_NETWORK, codeSection);
		assertThat(rdia.getResourceType(), is(equalTo(ResourceDemandType.RESOURCE_TYPE_NETWORK)));

		final File secFile = new File(
			ResourceDemandingInternalAction.class.getResource("de/uka/ipd/sdq/beale/core/PingPong.java").getPath());
		final int startIndex = 60;
		final int endIndex = 68;
		final CodeSection secCodeSection = new CodeSection(secFile, startIndex, file, endIndex);
		rdia = new ResourceDemandingInternalAction(ResourceDemandType.RESOURCE_TYPE_CPU_NS, secCodeSection);
		assertThat(rdia.getResourceType(), is(equalTo(ResourceDemandType.RESOURCE_TYPE_CPU_NS)));

		rdia = new ResourceDemandingInternalAction(ResourceDemandType.RESOURCE_TYPE_HDD_NS, secCodeSection);
		assertThat(rdia.getResourceType(), is(equalTo(ResourceDemandType.RESOURCE_TYPE_HDD_NS)));

		rdia = new ResourceDemandingInternalAction(ResourceDemandType.RESOURCE_TYPE_NETWORK_NS, secCodeSection);
		assertThat(rdia.getResourceType(), is(equalTo(ResourceDemandType.RESOURCE_TYPE_NETWORK_NS)));
	}

	/**
	 * Test method for
	 * {@link de.uka.ipd.sdq.beage.core.ResourceDemandingInternalAction#getAction()}.
	 * 
	 */
	@Test
	public void testGetAction() {
		final File file = new File(
			ResourceDemandingInternalAction.class.getResource("de/uka/ipd/sdq/beale/core/TestFile.java").getPath());
		final int startCodeLine = 4;
		final int endCodeLine = 15;
		final CodeSection codeSection = new CodeSection(file, startCodeLine, file, endCodeLine);
		ResourceDemandingInternalAction rdia =
			new ResourceDemandingInternalAction(ResourceDemandType.RESOURCE_TYPE_CPU, codeSection);
		assertThat(rdia.getAction(), is(theInstance(codeSection)));

		final File secFile = new File(
			ResourceDemandingInternalAction.class.getResource("de/uka/ipd/sdq/beale/core/PingPong.java").getPath());
		final int startIndex = 60;
		final int endIndex = 68;
		final CodeSection secCodeSection = new CodeSection(secFile, startIndex, file, endIndex);
		rdia = new ResourceDemandingInternalAction(ResourceDemandType.RESOURCE_TYPE_CPU_NS, secCodeSection);
		assertThat(rdia.getAction(), is(theInstance(secCodeSection)));

	}

}
