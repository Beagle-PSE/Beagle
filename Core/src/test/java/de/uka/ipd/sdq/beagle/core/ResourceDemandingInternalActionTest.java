package de.uka.ipd.sdq.beagle.core;

import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.CoreMatchers.theInstance;
import static org.junit.Assert.assertThat;

import de.uka.ipd.sdq.beagle.core.testutil.CodeSectionFactory;
import de.uka.ipd.sdq.beagle.core.testutil.ThrowingMethod;

import org.junit.Test;

/**
 * Tests {@link de.uka.ipd.sdq.beage.core.ResourceDemandingInternalAction} and contains
 * all test cases needed to check every method.
 *
 * @author Annika Berger
 */
public class ResourceDemandingInternalActionTest {

	/**
	 * Test method for
	 * {@link de.uka.ipd.sdq.beage.core.ResourceDemandingInternalAction#equals()}.
	 *
	 */
	@Test
	public void testEquals() {
		final CodeSection[] codeSections = CodeSectionFactory.getAllCodeSections();
		final CodeSection codeSection = codeSections[0];
		final ResourceDemandingInternalAction rdia1 =
			new ResourceDemandingInternalAction(ResourceDemandType.RESOURCE_TYPE_CPU, codeSection);

		final CodeSection secCodeSection = codeSections[1];
		final ResourceDemandingInternalAction rdia =
			new ResourceDemandingInternalAction(ResourceDemandType.RESOURCE_TYPE_CPU_NS, secCodeSection);

		final CodeSection codeSec = codeSections[0];
		final ResourceDemandingInternalAction rdia2 =
			new ResourceDemandingInternalAction(ResourceDemandType.RESOURCE_TYPE_CPU, codeSec);

		assertThat(rdia.equals(rdia1), is(equalTo(false)));
		assertThat(rdia1.equals(rdia2), is(equalTo(true)));
	}

	/**
	 * Test method for
	 * {@link de.uka.ipd.sdq.beage.core.ResourceDemandingInternalAction#getAction()}.
	 *
	 */
	@Test
	public void testGetAction() {
		final CodeSection[] codeSections = CodeSectionFactory.getAllCodeSections();
		final CodeSection codeSection = codeSections[0];
		ResourceDemandingInternalAction rdia =
			new ResourceDemandingInternalAction(ResourceDemandType.RESOURCE_TYPE_CPU, codeSection);
		assertThat(rdia.getAction(), is(theInstance(codeSection)));

		final CodeSection secCodeSection = codeSections[1];
		rdia = new ResourceDemandingInternalAction(ResourceDemandType.RESOURCE_TYPE_CPU_NS, secCodeSection);
		assertThat(rdia.getAction(), is(theInstance(secCodeSection)));

	}

	/**
	 * Test method for
	 * {@link de.uka.ipd.sdq.beage.core.ResourceDemandingInternalAction#getResourceType()}
	 * .
	 *
	 */
	@Test
	public void testGetResourceType() {
		final CodeSection[] codeSections = CodeSectionFactory.getAllCodeSections();
		final CodeSection codeSection = codeSections[0];
		ResourceDemandingInternalAction rdia =
			new ResourceDemandingInternalAction(ResourceDemandType.RESOURCE_TYPE_CPU, codeSection);
		assertThat(rdia.getResourceType(), is(equalTo(ResourceDemandType.RESOURCE_TYPE_CPU)));

		rdia = new ResourceDemandingInternalAction(ResourceDemandType.RESOURCE_TYPE_HDD, codeSection);
		assertThat(rdia.getResourceType(), is(equalTo(ResourceDemandType.RESOURCE_TYPE_HDD)));

		rdia = new ResourceDemandingInternalAction(ResourceDemandType.RESOURCE_TYPE_NETWORK, codeSection);
		assertThat(rdia.getResourceType(), is(equalTo(ResourceDemandType.RESOURCE_TYPE_NETWORK)));

		final CodeSection secCodeSection = codeSections[1];
		rdia = new ResourceDemandingInternalAction(ResourceDemandType.RESOURCE_TYPE_CPU_NS, secCodeSection);
		assertThat(rdia.getResourceType(), is(equalTo(ResourceDemandType.RESOURCE_TYPE_CPU_NS)));

		rdia = new ResourceDemandingInternalAction(ResourceDemandType.RESOURCE_TYPE_HDD_NS, secCodeSection);
		assertThat(rdia.getResourceType(), is(equalTo(ResourceDemandType.RESOURCE_TYPE_HDD_NS)));

		rdia = new ResourceDemandingInternalAction(ResourceDemandType.RESOURCE_TYPE_NETWORK_NS, secCodeSection);
		assertThat(rdia.getResourceType(), is(equalTo(ResourceDemandType.RESOURCE_TYPE_NETWORK_NS)));
	}

	/**
	 * Asserts that the constructor is working correctly.
	 *
	 */
	@Test
	public void testResourceDemandingInternalAction() {
		final CodeSection[] codeSections = CodeSectionFactory.getAllCodeSections();
		final CodeSection codeSection = codeSections[0];
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

		final CodeSection secCodeSection = codeSections[1];
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
	 * {@link de.uka.ipd.sdq.beage.core.ResourceDemandingInternalAction#toString()}.
	 *
	 */
	@Test
	public void testToString() {
		final CodeSection[] codeSections = CodeSectionFactory.getAllCodeSections();
		for (CodeSection codeSection : codeSections) {
			final ResourceDemandingInternalAction rdia =
				new ResourceDemandingInternalAction(ResourceDemandType.RESOURCE_TYPE_CPU, codeSection);
			assertThat(rdia.toString(), not(startsWith("ResourceDemandingInternalAction@")));
		}
	}

}
