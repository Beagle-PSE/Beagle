package de.uka.ipd.sdq.beagle.core.testutil.factories;

import de.uka.ipd.sdq.beagle.core.CodeSection;
import de.uka.ipd.sdq.beagle.core.ResourceDemandType;
import de.uka.ipd.sdq.beagle.core.measurement.order.CodeSectionExecutedEvent;
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
	 * Creates {@link MeasurementEvent}s.
	 *
	 * @return an array containing the created {@link MeasurementEvent}s
	 */
	public MeasurementEvent[] getAll() {
		return this.getAllAsSet().toArray(new MeasurementEvent[0]);
	}

	/**
	 * Creates {@link MeasurementEvent}s.
	 *
	 * @return an set containing the created {@link MeasurementEvent}s
	 */
	public Set<MeasurementEvent> getAllAsSet() {
		final Set<CodeSection> codeSections = CODE_SECTION_FACTORY.getAllAsSet();
		final Set<MeasurementEvent> events = new HashSet<>();
		for (CodeSection codeSection : codeSections) {
			events.add(new CodeSectionExecutedEvent(codeSection));

			events.add(new ResourceDemandCapturedEvent(codeSection, ResourceDemandType.RESOURCE_TYPE_CPU, 2.3));
		}

		return events;
	}

}
