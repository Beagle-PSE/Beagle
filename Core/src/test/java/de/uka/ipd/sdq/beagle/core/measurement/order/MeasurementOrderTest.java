package de.uka.ipd.sdq.beagle.core.measurement.order;

import static de.uka.ipd.sdq.beagle.core.testutil.EqualsMatcher.hasDefaultEqualsProperties;
import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static de.uka.ipd.sdq.beagle.core.testutil.NullHandlingMatchers.notAcceptingNull;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.theInstance;

import de.uka.ipd.sdq.beagle.core.CodeSection;
import de.uka.ipd.sdq.beagle.core.ProjectInformation;
import de.uka.ipd.sdq.beagle.core.testutil.factories.CodeSectionFactory;
import de.uka.ipd.sdq.beagle.core.testutil.factories.ProjectInformationFactory;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Tests {@link MeasurementOrder} and contains all test cases needed to check every
 * method.
 *
 * @author Annika Berger
 */
public class MeasurementOrderTest {

	/**
	 * A {@link CodeSectionFactory} providing methods to get code sections to use for
	 * tests.
	 */
	private static final CodeSectionFactory CODE_SECTION_FACTORY = new CodeSectionFactory();

	/**
	 * A {@link ProjectInformationFactory} providing methods to get project information
	 * instances to use for tests.
	 */
	private static final ProjectInformationFactory PROJECT_INFORMATION_FACTORY = new ProjectInformationFactory();

	/**
	 * Test method for
	 * {@link MeasurementOrder#MeasurementOrder(Set, Set, Set, Set, ParameterCharacteriser)}
	 * .
	 *
	 * <p>Asserts that neither the {@link ParameterCharacteriser} is {@code null} nor one
	 * of the sets contains {@code null}.
	 */
	@Test
	public void constructor() {
		final Set<CodeSection> parameterValueSection = CODE_SECTION_FACTORY.getAllAsSet();
		final Set<CodeSection> resourceDemandSections = CODE_SECTION_FACTORY.getAllAsSet();
		final Set<CodeSection> executionSections = CODE_SECTION_FACTORY.getAllAsSet();
		final ProjectInformation projectInformation = PROJECT_INFORMATION_FACTORY.getOne();
		final ParameterCharacteriser parameterCharacteriser = new ParameterCharacteriser();

		new MeasurementOrder(parameterValueSection, resourceDemandSections, executionSections, projectInformation,
			parameterCharacteriser);

		assertThat(
			"parameterCharacteriser must not be null", () -> new MeasurementOrder(parameterValueSection,
				resourceDemandSections, executionSections, projectInformation, null),
			throwsException(NullPointerException.class));

		assertThat((withNull) -> new MeasurementOrder(new HashSet<>(withNull), resourceDemandSections,
			executionSections, projectInformation, parameterCharacteriser),
			is(notAcceptingNull(parameterValueSection)));

		assertThat((withNull) -> new MeasurementOrder(parameterValueSection, new HashSet<>(withNull), executionSections,
			projectInformation, parameterCharacteriser), is(notAcceptingNull(resourceDemandSections)));

		assertThat((withNull) -> new MeasurementOrder(parameterValueSection, resourceDemandSections,
			new HashSet<>(withNull), projectInformation, parameterCharacteriser),
			is(notAcceptingNull(executionSections)));

		assertThat(() -> new MeasurementOrder(parameterValueSection, resourceDemandSections, executionSections, null,
			parameterCharacteriser), throwsException(NullPointerException.class));
	}

	/**
	 * Test method for {@link MeasurementOrder#getParameterValueSection()} .
	 */
	@Test
	public void getParameterValueSection() {
		final Set<CodeSection> parameterValueSection = CODE_SECTION_FACTORY.getAllAsSet();
		final Set<CodeSection> resourceDemandSections = CODE_SECTION_FACTORY.getAllAsSet();
		final Set<CodeSection> executionSections = CODE_SECTION_FACTORY.getAllAsSet();
		final ProjectInformation projectInformation = PROJECT_INFORMATION_FACTORY.getOne();
		final ParameterCharacteriser parameterCharacteriser = new ParameterCharacteriser();

		final MeasurementOrder measurementOrder = new MeasurementOrder(parameterValueSection, resourceDemandSections,
			executionSections, projectInformation, parameterCharacteriser);

		assertThat(measurementOrder.getParameterValueSection(), is(equalTo(parameterValueSection)));

		parameterValueSection.remove(CODE_SECTION_FACTORY.getOne());
		assertThat(measurementOrder.getParameterValueSection(), is(not(equalTo(parameterValueSection))));
	}

	/**
	 * Test method for {@link MeasurementOrder#getResourceDemandSections()} .
	 */
	@Test
	public void getResourceDemandSections() {
		final Set<CodeSection> parameterValueSection = CODE_SECTION_FACTORY.getAllAsSet();
		final Set<CodeSection> resourceDemandSections = CODE_SECTION_FACTORY.getAllAsSet();
		final Set<CodeSection> executionSections = CODE_SECTION_FACTORY.getAllAsSet();
		final ProjectInformation projectInformation = PROJECT_INFORMATION_FACTORY.getOne();
		final ParameterCharacteriser parameterCharacteriser = new ParameterCharacteriser();

		final MeasurementOrder measurementOrder = new MeasurementOrder(parameterValueSection, resourceDemandSections,
			executionSections, projectInformation, parameterCharacteriser);

		assertThat(measurementOrder.getResourceDemandSections(), is(equalTo(resourceDemandSections)));

		resourceDemandSections.remove(CODE_SECTION_FACTORY.getOne());
		assertThat(measurementOrder.getResourceDemandSections(), is(not(equalTo(resourceDemandSections))));
	}

	/**
	 * Test method for {@link MeasurementOrder#getExecutionSections()} .
	 */
	@Test
	public void getExecutionSections() {
		final Set<CodeSection> parameterValueSection = CODE_SECTION_FACTORY.getAllAsSet();
		final Set<CodeSection> resourceDemandSections = CODE_SECTION_FACTORY.getAllAsSet();
		final Set<CodeSection> executionSections = CODE_SECTION_FACTORY.getAllAsSet();
		final ProjectInformation projectInformation = PROJECT_INFORMATION_FACTORY.getOne();
		final ParameterCharacteriser parameterCharacteriser = new ParameterCharacteriser();

		final MeasurementOrder measurementOrder = new MeasurementOrder(parameterValueSection, resourceDemandSections,
			executionSections, projectInformation, parameterCharacteriser);

		assertThat(measurementOrder.getExecutionSections(), is(equalTo(executionSections)));

		executionSections.remove(CODE_SECTION_FACTORY.getOne());
		assertThat(measurementOrder.getExecutionSections(), is(not(equalTo(executionSections))));
	}

	/**
	 * Test method for {@link MeasurementOrder#getProjectInformation()} .
	 */
	@Test
	public void getProjectInformation() {
		final Set<CodeSection> parameterValueSection = CODE_SECTION_FACTORY.getAllAsSet();
		final Set<CodeSection> resourceDemandSections = CODE_SECTION_FACTORY.getAllAsSet();
		final Set<CodeSection> executionSections = CODE_SECTION_FACTORY.getAllAsSet();
		final ProjectInformation projectInformation = PROJECT_INFORMATION_FACTORY.getOne();
		final ParameterCharacteriser parameterCharacteriser = new ParameterCharacteriser();

		final MeasurementOrder measurementOrder = new MeasurementOrder(parameterValueSection, resourceDemandSections,
			executionSections, projectInformation, parameterCharacteriser);

		assertThat(measurementOrder.getProjectInformation(), is(theInstance(projectInformation)));
	}

	/**
	 * Test method for {@link MeasurementOrder#getParameterCharacteriser()} .
	 */
	@Test
	public void getParameterCharacteriser() {
		final Set<CodeSection> parameterValueSection = CODE_SECTION_FACTORY.getAllAsSet();
		final Set<CodeSection> resourceDemandSections = CODE_SECTION_FACTORY.getAllAsSet();
		final Set<CodeSection> executionSections = CODE_SECTION_FACTORY.getAllAsSet();
		final ProjectInformation projectInformation = PROJECT_INFORMATION_FACTORY.getOne();
		ParameterCharacteriser parameterCharacteriser = new ParameterCharacteriser();

		final MeasurementOrder measurementOrder = new MeasurementOrder(parameterValueSection, resourceDemandSections,
			executionSections, projectInformation, parameterCharacteriser);

		assertThat(measurementOrder.getParameterCharacteriser(), is(equalTo(parameterCharacteriser)));

		parameterCharacteriser = new ParameterCharacteriser();
		assertThat(measurementOrder.getParameterCharacteriser(), is(not(parameterCharacteriser)));
	}

	/**
	 * Test method for {@link MeasurementOrder#equals(Object)} and
	 * {@link MeasurementOrder#hashCode()}.
	 */
	@Test
	public void equalsAndHashCode() {
		final Set<CodeSection> parameterValueSection = CODE_SECTION_FACTORY.getAllAsSet();
		final Set<CodeSection> resourceDemandSections = CODE_SECTION_FACTORY.getAllAsSet();
		final Set<CodeSection> executionSections = CODE_SECTION_FACTORY.getAllAsSet();
		final ProjectInformation projectInformation = PROJECT_INFORMATION_FACTORY.getOne();
		final ParameterCharacteriser parameterCharacteriser = new ParameterCharacteriser();

		final Set<CodeSection> equalParameterValueSection = CODE_SECTION_FACTORY.getAllAsSet();
		final Set<CodeSection> equalResourceDemandSections = CODE_SECTION_FACTORY.getAllAsSet();
		final Set<CodeSection> equalExecutionSections = CODE_SECTION_FACTORY.getAllAsSet();
		final ProjectInformation equalProjectInformation = projectInformation;
		final ParameterCharacteriser equalParameterCharacteriser = parameterCharacteriser;

		final MeasurementOrder measurementOrder = new MeasurementOrder(parameterValueSection, resourceDemandSections,
			executionSections, projectInformation, parameterCharacteriser);
		final MeasurementOrder equalMeasurementOrder = new MeasurementOrder(equalParameterValueSection,
			equalResourceDemandSections, equalExecutionSections, equalProjectInformation, equalParameterCharacteriser);
		final MeasurementOrder other1 = new MeasurementOrder(
			new HashSet<>(Arrays.asList(CODE_SECTION_FACTORY.getOne())), equalResourceDemandSections,
			equalExecutionSections, equalProjectInformation, equalParameterCharacteriser);
		final MeasurementOrder other2 = new MeasurementOrder(equalParameterValueSection,
			new HashSet<>(Arrays.asList(CODE_SECTION_FACTORY.getOne())), equalExecutionSections,
			equalProjectInformation, equalParameterCharacteriser);
		final MeasurementOrder other3 = new MeasurementOrder(equalParameterValueSection, equalResourceDemandSections,
			new HashSet<>(Arrays.asList(CODE_SECTION_FACTORY.getOne())), equalProjectInformation,
			equalParameterCharacteriser);
		final MeasurementOrder other4 = new MeasurementOrder(equalParameterValueSection, equalResourceDemandSections,
			equalExecutionSections, PROJECT_INFORMATION_FACTORY.getOne(), equalParameterCharacteriser);
		final MeasurementOrder other5 = new MeasurementOrder(equalParameterValueSection, equalResourceDemandSections,
			equalExecutionSections, equalProjectInformation, new ParameterCharacteriser());
		assertThat(measurementOrder, hasDefaultEqualsProperties());

		assertThat(measurementOrder, is(equalTo(equalMeasurementOrder)));
		assertThat(measurementOrder.hashCode(), is(equalMeasurementOrder.hashCode()));

		assertThat(measurementOrder, is(not(equalTo(other1))));
		assertThat(measurementOrder, is(not(equalTo(other2))));
		assertThat(measurementOrder, is(not(equalTo(other3))));
		assertThat(measurementOrder, is(not(equalTo(other4))));
		assertThat(measurementOrder, is(not(equalTo(other5))));
	}
}
