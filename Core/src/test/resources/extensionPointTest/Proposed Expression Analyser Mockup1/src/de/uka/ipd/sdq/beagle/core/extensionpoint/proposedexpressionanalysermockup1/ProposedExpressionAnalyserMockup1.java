package de.uka.ipd.sdq.beagle.core.extensionpoint.proposedexpressionanalysermockup1;

import de.uka.ipd.sdq.beagle.core.analysis.ProposedExpressionAnalyser;
import de.uka.ipd.sdq.beagle.core.analysis.ProposedExpressionAnalyserBlackboardView;
import de.uka.ipd.sdq.beagle.core.analysis.ReadOnlyProposedExpressionAnalyserBlackboardView;

/**
 * An (empty) implementation of
 * {@link de.uka.ipd.sdq.beagle.core.analysis.ProposedExpressionAnalyser}.
 * 
 * @author Michael Vogt
 *
 */

public class ProposedExpressionAnalyserMockup1 implements ProposedExpressionAnalyser {

	@Override
	public boolean canContribute(final ReadOnlyProposedExpressionAnalyserBlackboardView blackboard) {
		return false;
	}

	@Override
	public void contribute(final ProposedExpressionAnalyserBlackboardView blackboard) {
	}

}
