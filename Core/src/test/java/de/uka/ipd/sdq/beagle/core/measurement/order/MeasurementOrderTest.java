package de.uka.ipd.sdq.beagle.core.measurement.order;

import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static de.uka.ipd.sdq.beagle.core.testutil.NullHandlingMatchers.notAcceptingNull;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.uka.ipd.sdq.beagle.core.CodeSection;
import de.uka.ipd.sdq.beagle.core.testutil.factories.CodeSectionFactory;
import de.uka.ipd.sdq.beagle.core.testutil.factories.LaunchConfigurationFactory;

import org.junit.Test;

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
	 * A {@link LaunchConfigurationFactory} providing methods to get launch configurations
	 * to use for tests.
	 */
	private static final LaunchConfigurationFactory LAUNCH_CONFIGURATION_FACTORY = new LaunchConfigurationFactory();

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
		final Set<LaunchConfiguration> launchConfigurations = LAUNCH_CONFIGURATION_FACTORY.getAllAsSet();
		final ParameterCharacteriser parameterCharacteriser = new ParameterCharacteriser();

		new MeasurementOrder(parameterValueSection, resourceDemandSections, executionSections, launchConfigurations,
			parameterCharacteriser);

		assertThat(
			"parameterCharacteriser must not be null", () -> new MeasurementOrder(parameterValueSection,
				resourceDemandSections, executionSections, launchConfigurations, null),
			throwsException(NullPointerException.class));

		assertThat((withNull) -> new MeasurementOrder(new HashSet<>(withNull), resourceDemandSections,
			executionSections, launchConfigurations, parameterCharacteriser),
			is(notAcceptingNull(parameterValueSection)));

		assertThat((withNull) -> new MeasurementOrder(parameterValueSection, new HashSet<>(withNull), executionSections,
			launchConfigurations, parameterCharacteriser), is(notAcceptingNull(resourceDemandSections)));

		assertThat((withNull) -> new MeasurementOrder(parameterValueSection, resourceDemandSections,
			new HashSet<>(withNull), launchConfigurations, parameterCharacteriser),
			is(notAcceptingNull(executionSections)));

		assertThat((withNull) -> new MeasurementOrder(parameterValueSection, resourceDemandSections, executionSections,
			new HashSet<>(withNull), parameterCharacteriser), is(notAcceptingNull(launchConfigurations)));
	}

	/**
	 * Test method for {@link MeasurementOrder#getParameterValueSection()} .
	 */
	@Test
	public void getParameterValueSection() {
		final Set<CodeSection> parameterValueSection = CODE_SECTION_FACTORY.getAllAsSet();
		final Set<CodeSection> resourceDemandSections = CODE_SECTION_FACTORY.getAllAsSet();
		final Set<CodeSection> executionSections = CODE_SECTION_FACTORY.getAllAsSet();
		final Set<LaunchConfiguration> launchConfigurations = LAUNCH_CONFIGURATION_FACTORY.getAllAsSet();
		final ParameterCharacteriser parameterCharacteriser = new ParameterCharacteriser();

		final MeasurementOrder measurementOrder = new MeasurementOrder(parameterValueSection, resourceDemandSections,
			executionSections, launchConfigurations, parameterCharacteriser);

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
		final Set<LaunchConfiguration> launchConfigurations = LAUNCH_CONFIGURATION_FACTORY.getAllAsSet();
		final ParameterCharacteriser parameterCharacteriser = new ParameterCharacteriser();

		final MeasurementOrder measurementOrder = new MeasurementOrder(parameterValueSection, resourceDemandSections,
			executionSections, launchConfigurations, parameterCharacteriser);

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
		final Set<LaunchConfiguration> launchConfigurations = LAUNCH_CONFIGURATION_FACTORY.getAllAsSet();
		final ParameterCharacteriser parameterCharacteriser = new ParameterCharacteriser();

		final MeasurementOrder measurementOrder = new MeasurementOrder(parameterValueSection, resourceDemandSections,
			executionSections, launchConfigurations, parameterCharacteriser);

		assertThat(measurementOrder.getExecutionSections(), is(equalTo(executionSections)));

		executionSections.remove(CODE_SECTION_FACTORY.getOne());
		assertThat(measurementOrder.getExecutionSections(), is(not(equalTo(executionSections))));
	}

	/**
	 * Test method for {@link MeasurementOrder#getLaunchConfigurations()} .
	 */
	@Test
	public void getLaunchConfigurations() {
		final Set<CodeSection> parameterValueSection = CODE_SECTION_FACTORY.getAllAsSet();
		final Set<CodeSection> resourceDemandSections = CODE_SECTION_FACTORY.getAllAsSet();
		final Set<CodeSection> executionSections = CODE_SECTION_FACTORY.getAllAsSet();
		final Set<LaunchConfiguration> launchConfigurations = LAUNCH_CONFIGURATION_FACTORY.getAllAsSet();
		final ParameterCharacteriser parameterCharacteriser = new ParameterCharacteriser();

		final MeasurementOrder measurementOrder = new MeasurementOrder(parameterValueSection, resourceDemandSections,
			executionSections, launchConfigurations, parameterCharacteriser);

		assertThat(measurementOrder.getLaunchConfigurations(), is(equalTo(launchConfigurations)));

		launchConfigurations.remove(LAUNCH_CONFIGURATION_FACTORY.getOne());
		assertThat(measurementOrder.getLaunchConfigurations(), is(not(equalTo(launchConfigurations))));
	}

	/**
	 * Test method for {@link MeasurementOrder#getParameterCharacteriser()} .
	 */
	@Test
	public void getParameterCharacteriser() {
		final Set<CodeSection> parameterValueSection = CODE_SECTION_FACTORY.getAllAsSet();
		final Set<CodeSection> resourceDemandSections = CODE_SECTION_FACTORY.getAllAsSet();
		final Set<CodeSection> executionSections = CODE_SECTION_FACTORY.getAllAsSet();
		final Set<LaunchConfiguration> launchConfigurations = LAUNCH_CONFIGURATION_FACTORY.getAllAsSet();
		ParameterCharacteriser parameterCharacteriser = new ParameterCharacteriser();

		final MeasurementOrder measurementOrder = new MeasurementOrder(parameterValueSection, resourceDemandSections,
			executionSections, launchConfigurations, parameterCharacteriser);

		assertThat(measurementOrder.getParameterCharacteriser(), is(equalTo(parameterCharacteriser)));

		parameterCharacteriser = new ParameterCharacteriser();
		assertThat(measurementOrder.getParameterCharacteriser(), is(not(parameterCharacteriser)));
	}

}
