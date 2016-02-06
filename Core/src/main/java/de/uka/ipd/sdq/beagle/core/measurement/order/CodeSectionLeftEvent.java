package de.uka.ipd.sdq.beagle.core.measurement.order;

import de.uka.ipd.sdq.beagle.core.CodeSection;

/**
 * An event for the fact that a code section was being <em>left</em> to be executed. This
 * does imply that the <em>full</em> section was executed and a
 * {@link CodeSectionEnteredEvent} was created <em>before</em>. However, a code section
 * should usually be defined in a way that does not allow the control flow to enter but
 * not completely execute it.
 *
 * @author Joshua Gleitze
 * @author Roman Langrehr
 */
public class CodeSectionLeftEvent extends AbstractMeasurementEvent {

	/**
	 * Creates an event for the fact that {@code codeSection} was stopped to be executed.
	 *
	 * @param codeSection The section that was executed. Must not be {@code null}.
	 */
	public CodeSectionLeftEvent(final CodeSection codeSection) {
		super(codeSection);
	}

	@Override
	public void receive(final MeasurementEventVisitor visitor) {
		visitor.visit(this);
	}

}
