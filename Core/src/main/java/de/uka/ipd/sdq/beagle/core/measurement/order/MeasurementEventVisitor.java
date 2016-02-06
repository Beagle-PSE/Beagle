package de.uka.ipd.sdq.beagle.core.measurement.order;

/**
 * A visitor for {@link MeasurementEvent MeasurementEvents} useful to iterate over a
 * sequence of different such events. Implements the well known visitor pattern.
 *
 * @author Joshua Gleitze
 * @author Roman Langrehr
 */
public interface MeasurementEventVisitor {

	/**
	 * Called if the visited measurement event is a {@link CodeSectionEnteredEvent}.
	 *
	 * @param codeSectionEnteredEvent the visited measurement event
	 */
	void visit(CodeSectionEnteredEvent codeSectionEnteredEvent);

	/**
	 * Called if the visited measurement event is a {@link CodeSectionLeftEvent}.
	 *
	 * @param codeSectionLeftEvent the visited measurement event
	 */
	void visit(CodeSectionLeftEvent codeSectionLeftEvent);

	/**
	 * Called if the visited measurement event is a {@link ResourceDemandCapturedEvent}.
	 *
	 * @param resourceDemandCapturedEvent the visited measurement event
	 */
	void visit(ResourceDemandCapturedEvent resourceDemandCapturedEvent);

	/**
	 * Called if the visited measurement event is a {@link ParameterValueCapturedEvent}.
	 *
	 * @param parameterValueCapturedEvent the visited measurement event
	 */
	void visit(ParameterValueCapturedEvent parameterValueCapturedEvent);
}
