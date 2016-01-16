package de.uka.ipd.sdq.beagle.core;

import de.uka.ipd.sdq.beagle.core.analysis.ProposedExpressionAnalyser;
import de.uka.ipd.sdq.beagle.core.analysis.ReadOnlyProposedExpressionAnalyserBlackboardView;
import de.uka.ipd.sdq.beagle.core.analysis.ProposedExpressionAnalyserBlackboardView;


/**
 * Implements ProposedExpressionAnalyser interface.
 *
 * @author Michael Vogt
 * @see ProposedExpressionAnalyser
 */
public abstract class AbstractProposedExpressionAnalyser implements ProposedExpressionAnalyser {

    /**
	 *
	 *
	 *
	 */
	public boolean canContribute(final ReadOnlyProposedExpressionAnalyserBlackboardView blackboard) {
		//implement method
		return false;
	}

    /**
	 *
	 *
	 *
	 */
	public void contribute(final ProposedExpressionAnalyserBlackboardView blackboard) {
		//implement method
	}
}