package de.uka.ipd.sdq.beagle.core.testutil.factories;

import de.uka.ipd.sdq.beagle.core.CodeSection;
import de.uka.ipd.sdq.beagle.core.ResourceDemandType;
import de.uka.ipd.sdq.beagle.core.measurement.order.CodeSectionEnteredEvent;
import de.uka.ipd.sdq.beagle.core.measurement.order.CodeSectionLeftEvent;
import de.uka.ipd.sdq.beagle.core.measurement.order.MeasurementEvent;
import de.uka.ipd.sdq.beagle.core.measurement.order.ResourceDemandCapturedEvent;

import java.util.HashSet;
import java.util.Set;

/**
 * Creates {@link MeasurementEvent}s and provides methods to get them.
 * 
 * @author Annika Berger
 */
public final class MeasurementEventFactory {

	/**
	 * A {@link CodeSectionFactory}, which is able to generate {@link CodeSection}s.
	 */
	private static final CodeSectionFactory CODE_SECTION_FACTORY = new CodeSectionFactory();

	/**
	 * Creates a {@link MeasurementEvent}.
	 *
	 * @return one created {@link MeasurementEvent}
	 */
	public MeasurementEvent getOne() {
		return this.getAll()[0];
	}

	/**
	 * Creates an {@link CodeSectionEnteredEvent}.
	 *
	 * @return one created {@link CodeSectionEnteredEvent}.
	 */
	public CodeSectionEnteredEvent getOneCodeSectionEnteredEvent() {
		return this.getAllCodeSectionEnteredEvents()[0];
	}

	/**
	 * Creates an {@link CodeSectionLeftEvent}.
	 *
	 * @return one created {@link CodeSectionLeftEvent}.
	 */
	public CodeSectionLeftEvent getOneCodeSectionLeftEvent() {
		return this.getAllCodeSectionLeftEvents()[0];
	}

	/**
	 * Creates an {@link ResourceDemandCapturedEvent}.
	 *
	 * @return one created {@link ResourceDemandCapturedEvent}.
	 */
	public ResourceDemandCapturedEvent getOneResourceDemandCapturedEvent() {
		return this.getAllResourceDemandCapturedEvents()[0];
	}

	/**
	 * Creates {@link MeasurementEvent}s.
	 *
	 * @return an array containing the created {@link MeasurementEvent}s
	 */
	public MeasurementEvent[] getAll() {
		return this.getAllAsSet().toArray(new MeasurementEvent[0]);
	}

	/**
	 * Creates {@link CodeSectionEnteredEvent}s.
	 *
	 * @return an array containing the created {@link CodeSectionEnteredEvent}s
	 */
	public CodeSectionEnteredEvent[] getAllCodeSectionEnteredEvents() {
		return this.getAllCodeSectionEnteredEventsAsSet().toArray(new CodeSectionEnteredEvent[0]);
	}

	/**
	 * Creates {@link CodeSectionLeftEvent}s.
	 *
	 * @return an array containing the created {@link CodeSectionLeftEvent}s
	 */
	public CodeSectionLeftEvent[] getAllCodeSectionLeftEvents() {
		return this.getAllCodeSectionLeftEventsAsSet().toArray(new CodeSectionLeftEvent[0]);
	}

	/**
	 * Creates {@link ResourceDemandCapturedEvent}s.
	 *
	 * @return an array containing the created {@link ResourceDemandCapturedEvent}s
	 */
	public ResourceDemandCapturedEvent[] getAllResourceDemandCapturedEvents() {
		return this.getAllResourceDemandCapturedEventsAsSet().toArray(new ResourceDemandCapturedEvent[0]);
	}

	/**
	 * Creates {@link MeasurementEvent}s.
	 *
	 * @return an set containing the created {@link MeasurementEvent}s
	 */
	public Set<MeasurementEvent> getAllAsSet() {
		final Set<MeasurementEvent> events = new HashSet<>();
		events.addAll(this.getAllCodeSectionEnteredEventsAsSet());
		events.addAll(this.getAllCodeSectionLeftEventsAsSet());
		events.addAll(this.getAllResourceDemandCapturedEventsAsSet());
		return events;
	}

	/**
	 * Creates {@link CodeSectionLeftEvent}s.
	 *
	 * @return an set containing the created {@link CodeSectionLeftEvent}s
	 */
	public Set<CodeSectionLeftEvent> getAllCodeSectionLeftEventsAsSet() {
		final Set<CodeSection> codeSections = CODE_SECTION_FACTORY.getAllAsSet();
		final Set<CodeSectionLeftEvent> events = new HashSet<>();
		for (CodeSection codeSection : codeSections) {
			events.add(new CodeSectionLeftEvent(codeSection));
		}
		return events;
	}

	/**
	 * Creates {@link CodeSectionEnteredEvent}s.
	 *
	 * @return an set containing the created {@link CodeSectionEnteredEvent}s
	 */
	public Set<CodeSectionEnteredEvent> getAllCodeSectionEnteredEventsAsSet() {
		final Set<CodeSection> codeSections = CODE_SECTION_FACTORY.getAllAsSet();
		final Set<CodeSectionEnteredEvent> events = new HashSet<>();
		for (CodeSection codeSection : codeSections) {
			events.add(new CodeSectionEnteredEvent(codeSection));
		}
		return events;
	}

	/**
	 * Creates {@link ResourceDemandCapturedEvent}s.
	 *
	 * @return an set containing the created {@link ResourceDemandCapturedEvent}s
	 */
	public Set<ResourceDemandCapturedEvent> getAllResourceDemandCapturedEventsAsSet() {
		final Set<CodeSection> codeSections = CODE_SECTION_FACTORY.getAllAsSet();
		final Set<ResourceDemandCapturedEvent> events = new HashSet<>();
		for (CodeSection codeSection : codeSections) {
			events.add(new ResourceDemandCapturedEvent(codeSection, ResourceDemandType.RESOURCE_TYPE_CPU, 2.3));
		}
		return events;
	}

}
