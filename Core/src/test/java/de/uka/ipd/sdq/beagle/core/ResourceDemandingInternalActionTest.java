package de.uka.ipd.sdq.beagle.core;

import static de.uka.ipd.sdq.beagle.core.testutil.EqualsMatcher.hasDefaultEqualsProperties;
import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static de.uka.ipd.sdq.beagle.core.testutil.ToStringMatcher.hasOverriddenToString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.IsSame.sameInstance;
import static org.junit.Assert.assertThat;

import de.uka.ipd.sdq.beagle.core.testutil.ThrowingMethod;
import de.uka.ipd.sdq.beagle.core.testutil.factories.CodeSectionFactory;

import org.junit.Test;

/**
 * Tests {@link ResourceDemandingInternalAction} and contains all test cases needed to
 * check every method.
 *
 * @author Annika Berger
 */
public class ResourceDemandingInternalActionTest {

	/**
	 * A {@link CodeSectionFactory}, which is able to generate {@link CodeSection}s.
	 */
	private static final CodeSectionFactory CODE_SECTION_FACTORY = new CodeSectionFactory();

	/**
	 * Test method for {@link ResourceDemandingInternalAction#equals()} and
	 * {@link ResourceDemandingInternalAction#hashCode()}.
	 *
	 */
	@Test
	public void equalsAndHashCode() {
		assertThat(
			new ResourceDemandingInternalAction(ResourceDemandType.RESOURCE_TYPE_CPU, CODE_SECTION_FACTORY.getOne()),
			hasDefaultEqualsProperties());
		final CodeSection[] codeSections = CODE_SECTION_FACTORY.getAll();
		final ResourceDemandingInternalAction rdia =
			new ResourceDemandingInternalAction(ResourceDemandType.RESOURCE_TYPE_CPU, codeSections[0]);

		final ResourceDemandingInternalAction rdia1 =
			new ResourceDemandingInternalAction(ResourceDemandType.RESOURCE_TYPE_CPU_NS, codeSections[0]);

		final ResourceDemandingInternalAction rdia2 =
			new ResourceDemandingInternalAction(ResourceDemandType.RESOURCE_TYPE_CPU, CODE_SECTION_FACTORY.getAll()[0]);

		final ResourceDemandingInternalAction rdia4 =
			new ResourceDemandingInternalAction(ResourceDemandType.RESOURCE_TYPE_CPU, codeSections[1]);

		assertThat(rdia, is(equalTo(rdia2)));
		assertThat(rdia.hashCode(), is(equalTo(rdia2.hashCode())));
		assertThat(rdia, is(not(equalTo(rdia1))));
		assertThat(rdia, is(not(equalTo(rdia4))));
	}

	/**
	 * Test method for {@link ResourceDemandingInternalAction#getAction()}.
	 *
	 */
	@Test
	public void getAction() {
		final CodeSection[] codeSections = CODE_SECTION_FACTORY.getAll();
		final CodeSection codeSection = codeSections[0];
		ResourceDemandingInternalAction rdia =
			new ResourceDemandingInternalAction(ResourceDemandType.RESOURCE_TYPE_CPU, codeSection);
		assertThat(rdia.getAction(), is(sameInstance(codeSection)));

		final CodeSection secCodeSection = codeSections[1];
		rdia = new ResourceDemandingInternalAction(ResourceDemandType.RESOURCE_TYPE_CPU_NS, secCodeSection);
		assertThat(rdia.getAction(), is(sameInstance(secCodeSection)));

	}

	/**
	 * Test method for {@link ResourceDemandingInternalAction#getResourceType()} .
	 *
	 */
	@Test
	public void getResourceType() {
		final CodeSection[] codeSections = CODE_SECTION_FACTORY.getAll();
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
	public void constructor() {
		final CodeSection[] codeSections = CODE_SECTION_FACTORY.getAll();
		final CodeSection codeSection = codeSections[0];
		new ResourceDemandingInternalAction(ResourceDemandType.RESOURCE_TYPE_CPU, codeSection);
		new ResourceDemandingInternalAction(ResourceDemandType.RESOURCE_TYPE_HDD, codeSection);
		new ResourceDemandingInternalAction(ResourceDemandType.RESOURCE_TYPE_NETWORK, codeSection);
		final CodeSection secCodeSection = codeSections[1];
		new ResourceDemandingInternalAction(ResourceDemandType.RESOURCE_TYPE_CPU_NS, secCodeSection);
		new ResourceDemandingInternalAction(ResourceDemandType.RESOURCE_TYPE_HDD_NS, secCodeSection);
		new ResourceDemandingInternalAction(ResourceDemandType.RESOURCE_TYPE_NETWORK_NS, secCodeSection);

		ThrowingMethod method = () -> {
			new ResourceDemandingInternalAction(null, codeSection);
		};
		assertThat("Resouce demand type must not be null.", method, throwsException(NullPointerException.class));

		method = () -> {
			new ResourceDemandingInternalAction(ResourceDemandType.RESOURCE_TYPE_NETWORK_NS, null);
		};
		assertThat("Code section must not be null.", method, throwsException(NullPointerException.class));
	}

	/**
	 * Test method for {@link ResourceDemandingInternalAction#toString()}.
	 *
	 */
	@Test
	public void toStringT() {
		final CodeSection[] codeSections = CODE_SECTION_FACTORY.getAll();
		for (final CodeSection codeSection : codeSections) {
			final ResourceDemandingInternalAction rdia =
				new ResourceDemandingInternalAction(ResourceDemandType.RESOURCE_TYPE_CPU, codeSection);
			assertThat(rdia, hasOverriddenToString());
		}
	}

}
