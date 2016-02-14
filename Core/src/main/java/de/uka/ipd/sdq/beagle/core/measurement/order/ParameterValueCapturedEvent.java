package de.uka.ipd.sdq.beagle.core.measurement.order;

import de.uka.ipd.sdq.beagle.core.CodeSection;

/**
 * Note: Designing this class is out of the project’s first iteration’s scope.
 *
 * @author Roman Langrehr
 */
public class ParameterValueCapturedEvent extends AbstractMeasurementEvent {

	/**
	 * Note: Designing this class is out of the project’s first iteration’s scope.
	 *
	 * @param codeSection Note: Designing this class is out of the project’s first
	 *            iteration’s scope.
	 */
	protected ParameterValueCapturedEvent(final CodeSection codeSection) {
		super(codeSection);
	}

	@Override
	public void receive(final MeasurementEventVisitor visitor) {
		visitor.visit(this);
	}
}
