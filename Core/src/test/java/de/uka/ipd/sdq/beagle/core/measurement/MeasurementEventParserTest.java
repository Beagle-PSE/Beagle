package de.uka.ipd.sdq.beagle.core.measurement;

import static de.uka.ipd.sdq.beagle.core.testutil.NullHandlingMatchers.notAcceptingNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;

import de.uka.ipd.sdq.beagle.core.CodeSection;
import de.uka.ipd.sdq.beagle.core.ExternalCallParameter;
import de.uka.ipd.sdq.beagle.core.ResourceDemandType;
import de.uka.ipd.sdq.beagle.core.ResourceDemandingInternalAction;
import de.uka.ipd.sdq.beagle.core.SeffBranch;
import de.uka.ipd.sdq.beagle.core.SeffLoop;
import de.uka.ipd.sdq.beagle.core.measurement.order.CodeSectionEnteredEvent;
import de.uka.ipd.sdq.beagle.core.measurement.order.CodeSectionLeftEvent;
import de.uka.ipd.sdq.beagle.core.measurement.order.MeasurementEvent;
import de.uka.ipd.sdq.beagle.core.measurement.order.ResourceDemandCapturedEvent;
import de.uka.ipd.sdq.beagle.core.testutil.factories.CodeSectionFactory;
import de.uka.ipd.sdq.beagle.core.testutil.factories.MeasurementEventFactory;
import de.uka.ipd.sdq.beagle.core.testutil.factories.ResourceDemandingInternalActionFactory;
import de.uka.ipd.sdq.beagle.core.testutil.factories.SeffLoopFactory;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
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
		final List<MeasurementEvent> measurementEvents = new ArrayList<>();
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
		measurementEvents.add(MEASUREMENT_EVENT_FACTORY.getOneCodeSectionEnteredEvent());
		measurementEvents.add(MEASUREMENT_EVENT_FACTORY.getOneCodeSectionLeftEvent());
		final MeasurementEventParser parser = new MeasurementEventParser(measurementEvents);

		Set<ResourceDemandMeasurementResult> results = parser.getMeasurementResultsFor(rdias[0]);
		List<Double> resultValues = results.stream().map((result) -> result.getValue()).collect(Collectors.toList());
		assertThat(resultValues, containsInAnyOrder(0.3, 3.3, 4.5));

		results = parser.getMeasurementResultsFor(rdias[1]);
		resultValues = results.stream().map((result) -> result.getValue()).collect(Collectors.toList());
		assertThat(resultValues, containsInAnyOrder(3.4, 3.3));

		results = parser.getMeasurementResultsFor(rdias[2]);
		resultValues = results.stream().map((result) -> result.getValue()).collect(Collectors.toList());
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
		List<MeasurementEvent> measurementEvents = new ArrayList<>();
		final Iterator<CodeSection> codeSections = CODE_SECTION_FACTORY.getAllAsSet().iterator();
		final CodeSection[][] allSections = {
			new CodeSection[4], new CodeSection[2]
		};

		for (final CodeSection[] sections : allSections) {
			for (int i = 0; i < sections.length; i++) {
				sections[i] = codeSections.next();
				codeSections.remove();
			}
		}

		final SeffBranch[] branches = new SeffBranch[allSections.length];
		for (int b = 0; b < branches.length; b++) {
			branches[b] = new SeffBranch(new HashSet<>(Arrays.asList(allSections[b])));
		}

		MeasurementEventParser parser =
			new MeasurementEventParser(MEASUREMENT_EVENT_FACTORY.getAllResourceDemandCapturedEvents());
		for (final SeffBranch branch : branches) {
			assertThat("There are no Measurement Events for Seff Branches.", parser.getMeasurementResultsFor(branch),
				is(empty()));
		}

		parser = new MeasurementEventParser(MEASUREMENT_EVENT_FACTORY.getAllCodeSectionLeftEvents());
		for (final SeffBranch branch : branches) {
			assertThat("CodeSectionLeftEvents should be ignored.", parser.getMeasurementResultsFor(branch),
				is(empty()));
		}

		measurementEvents = new ArrayList<>();
		measurementEvents.add(MEASUREMENT_EVENT_FACTORY.getOneResourceDemandCapturedEvent());
		measurementEvents.add(new CodeSectionEnteredEvent(branches[0].getBranches().get(0)));
		parser = new MeasurementEventParser(measurementEvents);
		Set<BranchDecisionMeasurementResult> results = parser.getMeasurementResultsFor(branches[0]);
		List<Integer> resultValues =
			results.stream().map((result) -> result.getBranchIndex()).collect(Collectors.toList());
		assertThat(resultValues, containsInAnyOrder(0));

		measurementEvents = new ArrayList<>();
		measurementEvents.add(MEASUREMENT_EVENT_FACTORY.getOneResourceDemandCapturedEvent());
		measurementEvents.add(new CodeSectionEnteredEvent(branches[0].getBranches().get(0)));
		measurementEvents.add(new CodeSectionEnteredEvent(branches[0].getBranches().get(0)));
		parser = new MeasurementEventParser(measurementEvents);
		results = parser.getMeasurementResultsFor(branches[0]);
		resultValues = results.stream().map((result) -> result.getBranchIndex()).collect(Collectors.toList());
		assertThat(resultValues, containsInAnyOrder(0, 0));

		measurementEvents = new ArrayList<>();
		measurementEvents.add(MEASUREMENT_EVENT_FACTORY.getOneResourceDemandCapturedEvent());
		measurementEvents.add(new CodeSectionEnteredEvent(branches[0].getBranches().get(0)));
		measurementEvents.add(new CodeSectionEnteredEvent(branches[0].getBranches().get(1)));
		parser = new MeasurementEventParser(measurementEvents);
		results = parser.getMeasurementResultsFor(branches[0]);
		resultValues = results.stream().map((result) -> result.getBranchIndex()).collect(Collectors.toList());
		assertThat(resultValues, containsInAnyOrder(0, 1));

		measurementEvents = new ArrayList<>();
		measurementEvents.add(new CodeSectionEnteredEvent(branches[0].getBranches().get(0)));
		measurementEvents.add(new CodeSectionEnteredEvent(branches[1].getBranches().get(1)));
		measurementEvents.add(new CodeSectionEnteredEvent(branches[1].getBranches().get(0)));
		measurementEvents.add(new CodeSectionEnteredEvent(branches[0].getBranches().get(1)));
		parser = new MeasurementEventParser(measurementEvents);
		results = parser.getMeasurementResultsFor(branches[0]);
		resultValues = results.stream().map((result) -> result.getBranchIndex()).collect(Collectors.toList());
		assertThat(resultValues, containsInAnyOrder(0, 1));
		results = parser.getMeasurementResultsFor(branches[1]);
		resultValues = results.stream().map((result) -> result.getBranchIndex()).collect(Collectors.toList());
		assertThat(resultValues, containsInAnyOrder(0, 1));
	}

	/**
	 * Test method for
	 * {@link MeasurementEventParser#getMeasurementResultsFor(de.uka.ipd.sdq.beagle.core.SeffLoop)}
	 * .
	 */
	@Test
	public void getMeasurementResultsForSeffLoop() {
		List<MeasurementEvent> measurementEvents = new ArrayList<>();
		final SeffLoop[] loops = SEFF_LOOP_FACTORY.getAll();
		MeasurementEventParser parser =
			new MeasurementEventParser(MEASUREMENT_EVENT_FACTORY.getAllResourceDemandCapturedEvents());
		for (final SeffLoop loop : loops) {
			assertThat("There are no Measurement Events for Seff Loops.", parser.getMeasurementResultsFor(loop),
				is(empty()));
		}

		parser = new MeasurementEventParser(MEASUREMENT_EVENT_FACTORY.getAllCodeSectionLeftEvents());
		for (final SeffLoop loop : loops) {
			assertThat("Only CodeSectionLeftEvents should not be counted.", parser.getMeasurementResultsFor(loop),
				is(empty()));
		}

		measurementEvents.add(new CodeSectionEnteredEvent(loops[0].getLoopBody()));
		parser = new MeasurementEventParser(measurementEvents);
		Set<LoopRepetitionCountMeasurementResult> results = parser.getMeasurementResultsFor(loops[0]);
		List<Integer> resultValues = results.stream().map((result) -> result.getCount()).collect(Collectors.toList());
		assertThat(resultValues, containsInAnyOrder(1));

		measurementEvents = new ArrayList<>();
		measurementEvents.add(new CodeSectionEnteredEvent(loops[0].getLoopBody()));
		measurementEvents.add(new CodeSectionLeftEvent(loops[0].getLoopBody()));
		measurementEvents.add(new CodeSectionLeftEvent(loops[0].getLoopBody()));
		parser = new MeasurementEventParser(measurementEvents);
		results = parser.getMeasurementResultsFor(loops[0]);
		resultValues = results.stream().map((result) -> result.getCount()).collect(Collectors.toList());
		assertThat(resultValues, containsInAnyOrder(1));
		
		measurementEvents = new ArrayList<>();
		measurementEvents.add(new CodeSectionEnteredEvent(loops[0].getLoopBody()));
		measurementEvents.add(new CodeSectionEnteredEvent(loops[0].getLoopBody()));
		measurementEvents.add(new CodeSectionEnteredEvent(loops[0].getLoopBody()));
		measurementEvents.add(new CodeSectionEnteredEvent(loops[0].getLoopBody()));
		measurementEvents.add(new CodeSectionLeftEvent(loops[0].getLoopBody()));
		measurementEvents.add(new CodeSectionLeftEvent(loops[0].getLoopBody()));
		measurementEvents.add(new CodeSectionLeftEvent(loops[0].getLoopBody()));
		measurementEvents.add(new CodeSectionLeftEvent(loops[0].getLoopBody()));
		parser = new MeasurementEventParser(measurementEvents);
		results = parser.getMeasurementResultsFor(loops[0]);
		resultValues = results.stream().map((result) -> result.getCount()).collect(Collectors.toList());
		assertThat(resultValues, containsInAnyOrder(4));

		measurementEvents = new ArrayList<>();
		measurementEvents.add(new CodeSectionEnteredEvent(loops[0].getLoopBody()));
		measurementEvents.add(new CodeSectionLeftEvent(loops[0].getLoopBody()));
		parser = new MeasurementEventParser(measurementEvents);
		results = parser.getMeasurementResultsFor(loops[0]);
		resultValues = results.stream().map((result) -> result.getCount()).collect(Collectors.toList());
		assertThat(resultValues, containsInAnyOrder(1));

		measurementEvents = new ArrayList<>();
		measurementEvents.add(new CodeSectionLeftEvent(loops[0].getLoopBody()));
		measurementEvents.add(new CodeSectionEnteredEvent(loops[0].getLoopBody()));
		parser = new MeasurementEventParser(measurementEvents);
		results = parser.getMeasurementResultsFor(loops[0]);
		resultValues = results.stream().map((result) -> result.getCount()).collect(Collectors.toList());
		assertThat(resultValues, containsInAnyOrder(1));

		measurementEvents = new ArrayList<>();
		measurementEvents.add(new CodeSectionEnteredEvent(loops[0].getLoopBody()));
		measurementEvents.add(MEASUREMENT_EVENT_FACTORY.getOneResourceDemandCapturedEvent());
		measurementEvents.add(new CodeSectionLeftEvent(loops[0].getLoopBody()));
		parser = new MeasurementEventParser(measurementEvents);
		results = parser.getMeasurementResultsFor(loops[0]);
		resultValues = results.stream().map((result) -> result.getCount()).collect(Collectors.toList());
		assertThat(resultValues, containsInAnyOrder(1));

		measurementEvents = new ArrayList<>();
		measurementEvents.add(new CodeSectionLeftEvent(loops[0].getLoopBody()));
		measurementEvents.add(new CodeSectionEnteredEvent(loops[0].getLoopBody()));
		measurementEvents.add(MEASUREMENT_EVENT_FACTORY.getOneResourceDemandCapturedEvent());
		measurementEvents.add(new CodeSectionLeftEvent(loops[0].getLoopBody()));
		measurementEvents.add(new CodeSectionEnteredEvent(loops[0].getLoopBody()));
		measurementEvents.add(new CodeSectionLeftEvent(loops[0].getLoopBody()));
		parser = new MeasurementEventParser(measurementEvents);
		results = parser.getMeasurementResultsFor(loops[0]);
		resultValues = results.stream().map((result) -> result.getCount()).collect(Collectors.toList());
		assertThat(resultValues, containsInAnyOrder(2));

		measurementEvents = new ArrayList<>();
		measurementEvents.add(new CodeSectionEnteredEvent(loops[0].getLoopBody()));
		measurementEvents.add(new CodeSectionLeftEvent(loops[0].getLoopBody()));
		measurementEvents.add(
			new ResourceDemandCapturedEvent(CODE_SECTION_FACTORY.getOne(), ResourceDemandType.RESOURCE_TYPE_CPU, 42));
		measurementEvents.add(new CodeSectionEnteredEvent(loops[0].getLoopBody()));
		measurementEvents.add(new CodeSectionLeftEvent(loops[0].getLoopBody()));
		parser = new MeasurementEventParser(measurementEvents);
		results = parser.getMeasurementResultsFor(loops[0]);
		resultValues = results.stream().map((result) -> result.getCount()).collect(Collectors.toList());
		assertThat(resultValues, containsInAnyOrder(1, 1));

		measurementEvents = new ArrayList<>();
		measurementEvents.add(new CodeSectionEnteredEvent(loops[0].getLoopBody()));
		measurementEvents.add(new CodeSectionLeftEvent(loops[0].getLoopBody()));
		measurementEvents.add(new CodeSectionEnteredEvent(loops[1].getLoopBody()));
		measurementEvents.add(new CodeSectionLeftEvent(loops[1].getLoopBody()));
		parser = new MeasurementEventParser(measurementEvents);
		results = parser.getMeasurementResultsFor(loops[0]);
		resultValues = results.stream().map((result) -> result.getCount()).collect(Collectors.toList());
		assertThat(resultValues, containsInAnyOrder(1));
		results = parser.getMeasurementResultsFor(loops[1]);
		resultValues = results.stream().map((result) -> result.getCount()).collect(Collectors.toList());
		assertThat(resultValues, containsInAnyOrder(1));

		measurementEvents = new ArrayList<>();
		measurementEvents.add(new CodeSectionEnteredEvent(loops[0].getLoopBody()));
		measurementEvents.add(new CodeSectionEnteredEvent(loops[1].getLoopBody()));
		measurementEvents.add(new CodeSectionLeftEvent(loops[1].getLoopBody()));
		measurementEvents.add(MEASUREMENT_EVENT_FACTORY.getOneResourceDemandCapturedEvent());
		measurementEvents.add(new CodeSectionEnteredEvent(loops[1].getLoopBody()));
		measurementEvents.add(new CodeSectionLeftEvent(loops[1].getLoopBody()));
		measurementEvents.add(new CodeSectionLeftEvent(loops[0].getLoopBody()));
		parser = new MeasurementEventParser(measurementEvents);
		results = parser.getMeasurementResultsFor(loops[0]);
		resultValues = results.stream().map((result) -> result.getCount()).collect(Collectors.toList());
		assertThat(resultValues, containsInAnyOrder(1));
		results = parser.getMeasurementResultsFor(loops[1]);
		resultValues = results.stream().map((result) -> result.getCount()).collect(Collectors.toList());
		assertThat(resultValues, containsInAnyOrder(1, 1));

		measurementEvents = new ArrayList<>();
		measurementEvents.add(new CodeSectionEnteredEvent(loops[0].getLoopBody()));
		measurementEvents.add(new CodeSectionEnteredEvent(loops[1].getLoopBody()));
		measurementEvents.add(new CodeSectionLeftEvent(loops[0].getLoopBody()));
		measurementEvents.add(new CodeSectionLeftEvent(loops[1].getLoopBody()));
		parser = new MeasurementEventParser(measurementEvents);
		results = parser.getMeasurementResultsFor(loops[0]);
		resultValues = results.stream().map((result) -> result.getCount()).collect(Collectors.toList());
		assertThat(resultValues, containsInAnyOrder(1));
		results = parser.getMeasurementResultsFor(loops[1]);
		resultValues = results.stream().map((result) -> result.getCount()).collect(Collectors.toList());
		assertThat(resultValues, containsInAnyOrder(1));
	}

	/**
	 * Test method for
	 * {@link MeasurementEventParser#getMeasurementResultsFor(ExternalCallParameter)} .
	 */
	@Test
	public void getMeasurementResultsForExternalCallParameter() {
		final Set<MeasurementEvent> measurementEvents = new HashSet<>();
		measurementEvents.addAll(MEASUREMENT_EVENT_FACTORY.getAllAsSet());
		final MeasurementEventParser parser = new MeasurementEventParser(measurementEvents);

		ExternalCallParameter parameter;
		for (final CodeSection codeSection : CODE_SECTION_FACTORY.getAll()) {
			parameter = new ExternalCallParameter(codeSection, 2);
			assertThat("There should be no measurement results for ExternalCallParameters.",
				parser.getMeasurementResultsFor(parameter), is(empty()));
		}
	}

}
