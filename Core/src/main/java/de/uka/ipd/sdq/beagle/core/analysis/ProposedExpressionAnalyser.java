package de.uka.ipd.sdq.beagle.core.analysis;

import de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpression;

/**
 * An analyser that contributes {@linkplain EvaluableExpression EvaluableExpressions} to
 * the {@link de.uka.ipd.sdq.beagle.core.Blackboard} by analysing
 * {@linkplain EvaluableExpression EvaluableExpressions}. This analyser can use
 * measurement results and already proposed {@linkplain EvaluableExpression
 * EvaluableExpressions} to propose new {@linkplain EvaluableExpression
 * EvaluableExpressions}. Analysers that only use measurement results should implement
 * {@link MeasurementResultAnalyser} instead of this interface. For more details, look for
 * the Blackboard layers in the Design and Architecture document in chapter 2. Tools
 * wishing to conduct measurements on software are advised to implement
 * {@link de.uka.ipd.sdq.beagle.core.measurement.MeasurementTool} instead of this
 * interface.
 *
 * @author Joshua Gleitze
 * @author Roman Langrehr
 */
public interface ProposedExpressionAnalyser {

	/**
	 * Determines whether this analyser can potentially contribute knowledge in the
	 * moment. If {@code true} is returned, the analyser can potentially contribute, but
	 * might still be unable to generate meaningful results when being executed. If
	 * {@code false} is returned, calling {@link #contribute(AnalyserBlackboardView)} will
	 * not yield any new results. Calling this method has no implications on whether or
	 * when {@link #contribute(AnalyserBlackboardView)} will be called. Passing a
	 * {@code blackboard} that was last modified by this analyser’s
	 * {@link #contribute(AnalyserBlackboardView)} must always result in {@code false}.
	 *
	 * <p>This method must behave entirely stateless, meaning that its result do not
	 * depend on any prior calls to any method but only on the passed {@code blackboard}.
	 * Furthermore, calls with equal blackboard contents must always result in the same
	 * value.
	 *
	 * @param blackboard The current blackboard.
	 */
	void canContribute(ReadOnlyProposedExpressionAnalyserBlackboardView blackboard);

	/**
	 * Contributes this analyser’s knowledge to the passed blackboard.
	 *
	 * <p>Calling this method with a blackboard for which
	 * {@link #canContribute(ReadOnlyBlackboardView)} returns {@code false} results in
	 * undefined behaviour. This method must
	 *
	 * <ul>
	 *
	 * <li>behave entirely stateless, meaning that its result do not depend on any prior
	 * calls to any method (especially not {@link #canContribute(ReadOnlyBlackboardView)})
	 * but only on the passed {@code blackboard}.
	 *
	 * <li>store all its results on the passed {@code blackboard}.
	 *
	 * <li>assure that if {@link #canContribute(ReadOnlyBlackboardView)} is called with
	 * the blackboard like its left by this method, it will return {@code false}.
	 *
	 * </ul>
	 *
	 * <p>Implementing {@link de.uka.ipd.sdq.beagle.core.BlackboardStorer} and using
	 * {@link de.uka.ipd.sdq.beagle.core.Blackboard#writeFor(Class, java.io.Serializable)}
	 * is, if needed, the recommended way to achieve the behaviour described above.
	 *
	 * @param blackboard The current blackboard.
	 */
	void contribute(AnalyserBlackboardView blackboard);
}
