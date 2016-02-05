package de.uka.ipd.sdq.beagle.core.evaluableexpressions.util;

import de.uka.ipd.sdq.beagle.core.evaluableexpressions.AdditionExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.ComparisonExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.ConstantExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.DivisionExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableVariable;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.ExponentationExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.ExponentialFunctionExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.IfThenElseExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.LogarithmExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.MultiplicationExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.NaturalLogarithmExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.SineExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.SubtractionExpression;

/**
 * Interface for visitors traversing an {@linkplain EvaluableExpression
 * EvaluableExpression’s} inner expressions. This class does not define the logic of the
 * traversal but only a common interface with hooks to interact with it.
 *
 * <p>Implementors will define an algorithm resulting in some
 * {@linkplain EvaluableExpression inner expressions} of an
 * {@linkplain EvaluableExpression} (hereafter to be called the “root expression”) being
 * visited and left in a certain order. The following hook types are offered:
 *
 * <ul>
 *
 * <li>{@code at}: called when the traversal first reaches an expression.
 *
 * <li>{@code after}: called when leaving an expression.
 *
 * </ul>
 *
 * <p>Every hook type is offered for every {@linkplain EvaluableExpression} in
 * {@link de.uka.ipd.sdq.beagle.core.evaluableexpressions}. Additionally, there is an
 * {@code other} hook for each type. It is called for every expression type no hook was
 * defined for. If a hook is to be defined for an expression type but the {@code other}
 * hook is to be called for the expression anyway, the {@code super} hook can be called.
 * Example:
 *
 * <pre>
 * <code>
 * class MyWalker extends ExpressionTreeWalker {
 *
 * 	&#64;Override
 * 	void atAddition(AdditionExpression expression) {
 * 		// some code
 * 	}
 *
 * 	&#64;Override
 * 	void atMultiplication(MultiplicationExpression expression) {
 * 		// some code
 * 		supre.atMultiplication(expression);
 * 		// more code
 * 	}
 *
 * 	&#64;Override
 * 	void atOther(EvaluableExpression expression) {
 * 		// will be called for every expression but AdditionExpressions
 * 	}
 * }
 * </code>
 * </pre>
 *
 * {@code MyWalker#atOther} will be called when first visiting any
 * {@linkplain EvaluableExpression}, except when visiting an
 * {@linkplain AdditionExpression}.
 *
 * @author Joshua Gleitze
 */
public abstract class ExpressionTreeWalker {

	/**
	 * Called when first visiting any {@link EvaluableExpression} no hook was defined for.
	 * See the class description for details.
	 *
	 * @param expression The momentarily visited expression.
	 */
	protected void atOther(final EvaluableExpression expression) {
		// may be implemented by implementor.
	}

	/**
	 * Called when first visiting an {@link AdditionExpression}.
	 *
	 * @param expression The momentary visited expression.
	 */
	protected void atAddition(final AdditionExpression expression) {
		this.atOther(expression);
	}

	/**
	 * Called when first visiting a {@link MultiplicationExpression}.
	 *
	 * @param expression The momentary visited expression.
	 */
	protected void atMultiplication(final MultiplicationExpression expression) {
		this.atOther(expression);
	}

	/**
	 * Called when first visiting an {@link EvaluableVariable}.
	 *
	 * @param expression The momentary visited expression.
	 */
	protected void atVariable(final EvaluableVariable expression) {
		this.atOther(expression);
	}

	/**
	 * Called when first visiting a {@link ComparisonExpression}.
	 *
	 * @param expression The momentary visited expression.
	 */
	protected void atComparison(final ComparisonExpression expression) {
		this.atOther(expression);
	}

	/**
	 * Called when first visiting a {@link ConstantExpression}.
	 *
	 * @param expression The momentary visited expression.
	 */
	protected void atConstant(final ConstantExpression expression) {
		this.atOther(expression);
	}

	/**
	 * Called when first visiting a {@link DivisionExpression}.
	 *
	 * @param expression The momentary visited expression.
	 */
	protected void atDivision(final DivisionExpression expression) {
		this.atOther(expression);
	}

	/**
	 * Called when first visiting an {@link ExponentationExpression}.
	 *
	 * @param expression The momentary visited expression.
	 */
	protected void atExponentation(final ExponentationExpression expression) {
		this.atOther(expression);
	}

	/**
	 * Called when first visiting an {@link ExponentialFunctionExpression}.
	 *
	 * @param expression The momentary visited expression.
	 */
	protected void atExponentialFunction(final ExponentialFunctionExpression expression) {
		this.atOther(expression);
	}

	/**
	 * Called when first visiting an {@link IfThenElseExpression}.
	 *
	 * @param expression The momentary visited expression.
	 */
	protected void atIfThenElse(final IfThenElseExpression expression) {
		this.atOther(expression);
	}

	/**
	 * Called when first visiting a {@link LogarithmExpression}.
	 *
	 * @param expression The momentary visited expression.
	 */
	protected void atLogarithm(final LogarithmExpression expression) {
		this.atOther(expression);
	}

	/**
	 * Called when first visiting a {@link NaturalLogarithmExpression}.
	 *
	 * @param expression The momentary visited expression.
	 */
	protected void atNaturalLogarithm(final NaturalLogarithmExpression expression) {
		this.atOther(expression);
	}

	/**
	 * Called when first visiting a {@link SineExpression}.
	 *
	 * @param expression The momentary visited expression.
	 */
	protected void atSine(final SineExpression expression) {
		this.atOther(expression);
	}

	/**
	 * Called when first visiting a {@link SubtractionExpression}.
	 *
	 * @param expression The momentary visited expression.
	 */
	protected void atSubstraction(final SubtractionExpression expression) {
		this.atOther(expression);
	}

	/**
	 * Called when leaving any {@link EvaluableExpression} no hook was defined for. See
	 * the class description for details.
	 * 
	 * @param expression The momentary visited expression.
	 */
	protected void afterOther(final EvaluableExpression expression) {
		// may be implemented by implementor.
	}

	/**
	 * Called when leaving an {@link AdditionExpression}.
	 *
	 * @param expression The momentary visited expression.
	 */
	protected void afterAddition(final AdditionExpression expression) {
		this.afterOther(expression);
	}

	/**
	 * Called when leaving a {@link MultiplicationExpression}.
	 *
	 * @param expression The momentary visited expression.
	 */
	protected void afterMultiplication(final MultiplicationExpression expression) {
		this.afterOther(expression);
	}

	/**
	 * Called when leaving an {@link EvaluableVariable}.
	 *
	 * @param expression The momentary visited expression.
	 */
	protected void afterVariable(final EvaluableVariable expression) {
		this.afterOther(expression);
	}

	/**
	 * Called when leaving a {@link ComparisonExpression}.
	 *
	 * @param expression The momentary visited expression.
	 */
	protected void afterComparison(final ComparisonExpression expression) {
		this.afterOther(expression);
	}

	/**
	 * Called when leaving a {@link ConstantExpression}.
	 *
	 * @param expression The momentary visited expression.
	 */
	protected void afterConstant(final ConstantExpression expression) {
		this.afterOther(expression);
	}

	/**
	 * Called when leaving a {@link DivisionExpression}.
	 *
	 * @param expression The momentary visited expression.
	 */
	protected void afterDivision(final DivisionExpression expression) {
		this.afterOther(expression);
	}

	/**
	 * Called when leaving an {@link ExponentationExpression}.
	 *
	 * @param expression The momentary visited expression.
	 */
	protected void afterExponentation(final ExponentationExpression expression) {
		this.afterOther(expression);
	}

	/**
	 * Called when leaving an {@link ExponentialFunctionExpression}.
	 *
	 * @param expression The momentary visited expression.
	 */
	protected void afterExponentialFunction(final ExponentialFunctionExpression expression) {
		this.afterOther(expression);
	}

	/**
	 * Called when leaving an {@link IfThenElseExpression}.
	 *
	 * @param expression The momentary visited expression.
	 */
	protected void afterIfThenElse(final IfThenElseExpression expression) {
		this.afterOther(expression);
	}

	/**
	 * Called when leaving a {@link LogarithmExpression}.
	 *
	 * @param expression The momentary visited expression.
	 */
	protected void afterLogarithm(final LogarithmExpression expression) {
		this.afterOther(expression);
	}

	/**
	 * Called when leaving a {@link NaturalLogarithmExpression}.
	 *
	 * @param expression The momentary visited expression.
	 */
	protected void afterNaturalLogarithm(final NaturalLogarithmExpression expression) {
		this.afterOther(expression);
	}

	/**
	 * Called when leaving a {@link SineExpression}, after its inner expressions will be
	 * visited.
	 *
	 * @param expression The momentary visited expression.
	 */
	protected void afterSine(final SineExpression expression) {
		this.afterOther(expression);
	}

	/**
	 * Called when leaving a {@link SubtractionExpression}.
	 *
	 * @param expression The momentary visited expression.
	 */
	protected void afterSubstraction(final SubtractionExpression expression) {
		this.afterOther(expression);
	}

}
