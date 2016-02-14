package de.uka.ipd.sdq.beagle.core.measurement.order;

import de.uka.ipd.sdq.beagle.core.CodeSection;

/**
 * An event for the fact that a code section was being <em>started</em> to be executed.
 * This does not necessarily imply that the <em>full</em> section was executed. However, a
 * code section should usually be defined in a way that does not allow the control flow to
 * enter but not completely execute it.
 *
 * @author Joshua Gleitze
 * @author Roman Langrehr
 */
public class CodeSectionEnteredEvent extends AbstractMeasurementEvent {

	/**
	 * Creates an event for the fact that {@code codeSection} was started to be executed.
	 *
	 * @param codeSection The section that was executed. Must not be {@code null}.
	 */
	public CodeSectionEnteredEvent(final CodeSection codeSection) {
		super(codeSection);
	}

	@Override
	public void receive(final MeasurementEventVisitor visitor) {
		visitor.visit(this);
	}

}
