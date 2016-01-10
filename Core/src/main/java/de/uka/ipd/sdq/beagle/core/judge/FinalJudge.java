package de.uka.ipd.sdq.beagle.core.judge;

import de.uka.ipd.sdq.beagle.core.Blackboard;

/**
 * As last running entity of Beagle's core, the FinalJudge adopts two tasks.
 * Firstly, it decides for each {@link MeasureableSeffElement} which available {@link EvaluableExpression} from the
 * {@link Blackboard} will be taken as final version, marking it as the currently best fitting expression.
 * Secondly, it gives response if the analysing process has reached an end or should keep going on.
 * @author Ansgar Spiegler
 */
public class FinalJudge {

	/**
	 * The judge function is responsible for setting the final expression of
	 * {@link MeasurableSeffElement MeasurableSeffElements}.
	 * Therefore it calls {@link EvaluableExpressionFitnessFunction#grade} on the blackboard, with SEFF elements and
	 * EvaluableExpressions as parameters and chooses the one that has the lowest return value as final Expression.
	 *
	 * @param blackboard The current Blackboard
	 * @return {@code True} if the analysing process should keep going on, {@code false} if not.
	 */
	public boolean judge(final Blackboard blackboard) {
		return false;
	}
	
	
}
