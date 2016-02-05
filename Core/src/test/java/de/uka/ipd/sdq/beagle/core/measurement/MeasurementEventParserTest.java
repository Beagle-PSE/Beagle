package de.uka.ipd.sdq.beagle.core.measurement;

import static de.uka.ipd.sdq.beagle.core.testutil.NullHandlingMatchers.notAcceptingNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;

import de.uka.ipd.sdq.beagle.core.CodeSection;
import de.uka.ipd.sdq.beagle.core.ExternalCallParameter;
import de.uka.ipd.sdq.beagle.core.ResourceDemandingInternalAction;
import de.uka.ipd.sdq.beagle.core.SeffBranch;
import de.uka.ipd.sdq.beagle.core.SeffLoop;
import de.uka.ipd.sdq.beagle.core.measurement.order.CodeSectionExecutedEvent;
import de.uka.ipd.sdq.beagle.core.measurement.order.MeasurementEvent;
import de.uka.ipd.sdq.beagle.core.measurement.order.ResourceDemandCapturedEvent;
import de.uka.ipd.sdq.beagle.core.testutil.factories.CodeSectionFactory;
import de.uka.ipd.sdq.beagle.core.testutil.factories.ExternalCallParameterFactory;
import de.uka.ipd.sdq.beagle.core.testutil.factories.MeasurementEventFactory;
import de.uka.ipd.sdq.beagle.core.testutil.factories.ResourceDemandingInternalActionFactory;
import de.uka.ipd.sdq.beagle.core.testutil.factories.SeffBranchFactory;
import de.uka.ipd.sdq.beagle.core.testutil.factories.SeffLoopFactory;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Tests {@link MeasurementEventParser} and contains the test cases needed to check all
 * methods.
 *
 * @author Annika Berger
 */
public class MeasurementEventParserTest {

	/**
	 * A {@link MeasurementEventFactory}, which is able to generate
	 * {@link MeasurementEvent}s.
	 */
	private static final MeasurementEventFactory MEASUREMENT_EVENT_FACTORY = new MeasurementEventFactory();

	/**
	 * A {@link SeffBranchFactory}, which is able to generate {@link SeffBranch}s.
	 */
	private static final SeffBranchFactory SEFF_BRANCH_FACTORY = new SeffBranchFactory();
	
	/**
	 * A {@link SeffLoopFactory}, which is able to generate {@link SeffLoops}s.
	 */
	private static final SeffLoopFactory SEFF_LOOP_FACTORY = new SeffLoopFactory();

	/**
	 * A {@link ResourceDemandingInternalActionFactory}, which is able to generate
	 * {@link ResourceDemandingInternalAction}s.
	 */
	private static final ResourceDemandingInternalActionFactory RESOURCE_DEMANDING_INTERNAL_ACTION_FACTORY =
		new ResourceDemandingInternalActionFactory();
	
	/**
	 * A {@link ExternalCallParameterFactory}, which is able to generate
	 * {@link ExternalCallParameter}s.
	 */
	private static final ExternalCallParameterFactory EXTERNAL_CALL_PARAMETER_FACTORY =
		new ExternalCallParameterFactory();

	/**
	 * A {@link CodeSectionFactory}, which is able to generate {@link CodeSection}s.
	 */
	private static final CodeSectionFactory CODE_SECTION_FACTORY = new CodeSectionFactory();

	/**
	 * Test method for
	 * {@link MeasurementEventParser#MeasurementEventParser(MeasurementEvent[])} .
	 */
	@Test
	public void constructor() {
		final MeasurementEvent[] measurementEvents = MEASUREMENT_EVENT_FACTORY.getAll();
		new MeasurementEventParser(measurementEvents);

		assertThat((withNull) -> new MeasurementEventParser(withNull), is(notAcceptingNull(measurementEvents)));
	}

	/**
	 * Test method for
	 * {@link MeasurementEventParser#MeasurementEventParser(java.lang.Iterable)} .
	 */
	@Test
	public void constructorIterableOfMeasurementEvent() {
		final Set<MeasurementEvent> measurementEvents = MEASUREMENT_EVENT_FACTORY.getAllAsSet();
		new MeasurementEventParser(measurementEvents);

		assertThat((withNull) -> new MeasurementEventParser(new HashSet<>(withNull)),
			is(notAcceptingNull(measurementEvents)));
	}

	/**
	 * Test method for
	 * {@link MeasurementEventParser#getMeasurementResultsFor(ResourceDemandingInternalAction)}
	 * .
	 */
	@Test
	public void getMeasurementResultsForResourceDemandingInternalAction() {
		final Set<MeasurementEvent> measurementEvents = new HashSet<>();
		final ResourceDemandingInternalAction[] rdias = RESOURCE_DEMANDING_INTERNAL_ACTION_FACTORY.getAll();

		measurementEvents.add(new ResourceDemandCapturedEvent(rdias[0].getAction(), rdias[0].getResourceType(), 0.3));
		measurementEvents.add(new ResourceDemandCapturedEvent(rdias[0].getAction(), rdias[0].getResourceType(), 3.3));
		measurementEvents.add(new ResourceDemandCapturedEvent(rdias[0].getAction(), rdias[0].getResourceType(), 4.5));
		measurementEvents.add(new ResourceDemandCapturedEvent(rdias[1].getAction(), rdias[1].getResourceType(), 3.4));
		measurementEvents.add(new ResourceDemandCapturedEvent(rdias[1].getAction(), rdias[1].getResourceType(), 3.3));
		measurementEvents.add(new ResourceDemandCapturedEvent(rdias[2].getAction(), rdias[2].getResourceType(), 4.5));
		measurementEvents.add(new ResourceDemandCapturedEvent(rdias[2].getAction(), rdias[2].getResourceType(), 0.6));
		measurementEvents.add(new ResourceDemandCapturedEvent(rdias[2].getAction(), rdias[2].getResourceType(), 6.9));
		measurementEvents.add(new ResourceDemandCapturedEvent(rdias[2].getAction(), rdias[2].getResourceType(), 1.5));

		final MeasurementEventParser parser = new MeasurementEventParser(measurementEvents);

		Set<ResourceDemandMeasurementResult> results = parser.getMeasurementResultsFor(rdias[0]);
		Set<Double> resultValues = results.stream().map((result) -> result.getValue()).collect(Collectors.toSet());
		assertThat(resultValues, containsInAnyOrder(0.3, 3.3, 4.5));

		results = parser.getMeasurementResultsFor(rdias[1]);
		resultValues = results.stream().map((result) -> result.getValue()).collect(Collectors.toSet());
		assertThat(resultValues, containsInAnyOrder(3.4, 3.3));

		results = parser.getMeasurementResultsFor(rdias[2]);
		resultValues = results.stream().map((result) -> result.getValue()).collect(Collectors.toSet());
		assertThat(resultValues, containsInAnyOrder(4.5, 0.6, 6.9, 1.5));

		measurementEvents.add(new ResourceDemandCapturedEvent(rdias[3].getAction(), rdias[3].getResourceType(), 2.2));
		assertThat("Adding Events after inizialisation must not have an effect on returned results",
			parser.getMeasurementResultsFor(rdias[3]), is(empty()));

	}

	/**
	 * Test method for
	 * {@link MeasurementEventParser#getMeasurementResultsFor(de.uka.ipd.sdq.beagle.core.SeffBranch)}
	 * .
	 */
	@Test
	public void getMeasurementResultsForSeffBranch() {
		final Set<MeasurementEvent> measurementEvents = new HashSet<>();
		final ResourceDemandingInternalAction[] rdias = RESOURCE_DEMANDING_INTERNAL_ACTION_FACTORY.getAll();
		final CodeSection[] codeSections = CODE_SECTION_FACTORY.getAll();
		final SeffBranch branch = SEFF_BRANCH_FACTORY.getOne();

		measurementEvents.add(new ResourceDemandCapturedEvent(rdias[0].getAction(), rdias[0].getResourceType(), 0.3));
		measurementEvents.add(new ResourceDemandCapturedEvent(rdias[0].getAction(), rdias[0].getResourceType(), 3.3));
		measurementEvents.add(new ResourceDemandCapturedEvent(rdias[0].getAction(), rdias[0].getResourceType(), 4.5));
		measurementEvents.add(new CodeSectionExecutedEvent(codeSections[0]));
		measurementEvents.add(new CodeSectionExecutedEvent(codeSections[1]));
		final MeasurementEventParser parser = new MeasurementEventParser(measurementEvents);
		assertThat("There are no Measurement Events for Seff Branches.", parser.getMeasurementResultsFor(branch),
			is(empty()));
	}

	/**
	 * Test method for
	 * {@link MeasurementEventParser#getMeasurementResultsFor(de.uka.ipd.sdq.beagle.core.SeffLoop)}
	 * .
	 */
	@Test
	public void getMeasurementResultsForSeffLoop() {
		final Set<MeasurementEvent> measurementEvents = new HashSet<>();
		final ResourceDemandingInternalAction[] rdias = RESOURCE_DEMANDING_INTERNAL_ACTION_FACTORY.getAll();
		final CodeSection[] codeSections = CODE_SECTION_FACTORY.getAll();
		final SeffLoop loop = SEFF_LOOP_FACTORY.getOne();

		measurementEvents.add(new ResourceDemandCapturedEvent(rdias[0].getAction(), rdias[0].getResourceType(), 0.3));
		measurementEvents.add(new ResourceDemandCapturedEvent(rdias[0].getAction(), rdias[0].getResourceType(), 3.3));
		measurementEvents.add(new ResourceDemandCapturedEvent(rdias[0].getAction(), rdias[0].getResourceType(), 4.5));
		measurementEvents.add(new CodeSectionExecutedEvent(codeSections[0]));
		measurementEvents.add(new CodeSectionExecutedEvent(codeSections[1]));
		final MeasurementEventParser parser = new MeasurementEventParser(measurementEvents);
		assertThat("There are no Measurement Events for Seff Loops.", parser.getMeasurementResultsFor(loop),
			is(empty()));
	}

	/**
	 * Test method for
	 * {@link MeasurementEventParser#getMeasurementResultsFor(ExternalCallParameter)} .
	 */
	@Test
	public void getMeasurementResultsForExternalCallParameter() {
		final Set<MeasurementEvent> measurementEvents = new HashSet<>();
		final ResourceDemandingInternalAction[] rdias = RESOURCE_DEMANDING_INTERNAL_ACTION_FACTORY.getAll();
		final CodeSection[] codeSections = CODE_SECTION_FACTORY.getAll();
		final ExternalCallParameter parameter = EXTERNAL_CALL_PARAMETER_FACTORY.getOne();

		measurementEvents.add(new ResourceDemandCapturedEvent(rdias[0].getAction(), rdias[0].getResourceType(), 0.3));
		measurementEvents.add(new ResourceDemandCapturedEvent(rdias[0].getAction(), rdias[0].getResourceType(), 3.3));
		measurementEvents.add(new ResourceDemandCapturedEvent(rdias[0].getAction(), rdias[0].getResourceType(), 4.5));
		measurementEvents.add(new CodeSectionExecutedEvent(codeSections[0]));
		measurementEvents.add(new CodeSectionExecutedEvent(codeSections[1]));
		final MeasurementEventParser parser = new MeasurementEventParser(measurementEvents);
		assertThat("There are no Measurement Events for External Call Parameters.", parser.getMeasurementResultsFor(parameter),
			is(empty()));
	}

}
