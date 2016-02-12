package de.uka.ipd.sdq.beagle.core.measurement;
/**
 * Test coverage check turned off, because this class needs no tests.
 *
 * <p>COVERAGE:OFF
 */

import de.uka.ipd.sdq.beagle.core.measurement.order.CodeSectionEnteredEvent;
import de.uka.ipd.sdq.beagle.core.measurement.order.CodeSectionLeftEvent;
import de.uka.ipd.sdq.beagle.core.measurement.order.MeasurementEventVisitor;
import de.uka.ipd.sdq.beagle.core.measurement.order.ParameterValueCapturedEvent;
import de.uka.ipd.sdq.beagle.core.measurement.order.ResourceDemandCapturedEvent;

/**
 * Convenient class for implementing {@link MeasurementEventVisitor}. It provides an empty
 * implementation of each visit method.
 *
 * @author Roman Langrehr
 */
public abstract class AbstractMeasurementEventVisitor implements MeasurementEventVisitor {

	@Override
	public void visit(final CodeSectionEnteredEvent codeSectionEnteredEvent) {
	}

	@Override
	public void visit(final CodeSectionLeftEvent codeSectionLeftEvent) {
	}

	@Override
	public void visit(final ResourceDemandCapturedEvent resourceDemandCapturedEvent) {
	}

	@Override
	public void visit(final ParameterValueCapturedEvent parameterValueCapturedEvent) {
	}

}
