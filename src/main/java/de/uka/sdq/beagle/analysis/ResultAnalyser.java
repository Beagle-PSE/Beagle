package de.uka.sdq.beagle.analysis;

/**
 * TODO Document this type.
 *
 */
public interface ResultAnalyser {
	void canContribute(ReadOnlyBlackboardView blackboard);

	void contribute(AnalyserBlackboardView blackboard);
}
