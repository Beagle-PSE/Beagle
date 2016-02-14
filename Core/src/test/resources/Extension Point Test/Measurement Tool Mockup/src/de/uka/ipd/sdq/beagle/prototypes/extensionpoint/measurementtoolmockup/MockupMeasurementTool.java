package de.uka.ipd.sdq.beagle.prototypes.extensionpoint.measurementtoolmockup;

import de.uka.ipd.sdq.beagle.core.measurement.MeasurementTool;
import de.uka.ipd.sdq.beagle.core.measurement.order.MeasurementEvent;
import de.uka.ipd.sdq.beagle.core.measurement.order.MeasurementOrder;

import java.util.List;

/**
 * An (empty) implementation of {@link de.uka.sdq.beagle.measurement.MeasurementTool}.
 *
 * @author Roman Langrehr
 */
public class MockupMeasurementTool implements MeasurementTool {

	@Override
	public List<MeasurementEvent> measure(final MeasurementOrder measurements) {
		for (int i = 0; i < 1000; i++) {
			System.out.println("I'm measuring");
		}
		return null;

	}

}
