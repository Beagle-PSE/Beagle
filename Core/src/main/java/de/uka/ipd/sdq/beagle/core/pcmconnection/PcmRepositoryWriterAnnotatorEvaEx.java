package de.uka.ipd.sdq.beagle.core.pcmconnection;

import de.uka.ipd.sdq.beagle.core.ResourceDemandType;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.ConstantExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpression;

import org.eclipse.emf.common.util.EList;
import org.palladiosimulator.pcm.resourcetype.ProcessingResourceType;
import org.palladiosimulator.pcm.seff.impl.BranchActionImpl;
import org.palladiosimulator.pcm.seff.impl.ExternalCallActionImpl;
import org.palladiosimulator.pcm.seff.impl.InternalActionImpl;
import org.palladiosimulator.pcm.seff.impl.LoopActionImpl;
import org.palladiosimulator.pcm.seff.seff_performance.ParametricResourceDemand;

/**
 * This class contains the logic for converting and annotating EvaluableExpressions.
 * 
 * @author Ansgar Spiegler
 */
public class PcmRepositoryWriterAnnotatorEvaEx {

	/**
	 * /** The Mappings of InternalAction's {@link ProcessingResourceType} to
	 * {@link ResourceDemandType} from Beagle.
	 */
	private final ResourceTypeMappings typeMappings;

	/**
	 * Constructor.
	 */
	public PcmRepositoryWriterAnnotatorEvaEx() {
		this.typeMappings = new ResourceTypeMappings();
		this.typeMappings.initialise();
	}

	/**
	 * Annotating the EvaluableExpression onto the given PCM element.
	 *
	 * @param loopAction The PCM element
	 * @param evaEx The Expression to annotate
	 */
	public void annotateEvaExFor(final LoopActionImpl loopAction, final EvaluableExpression evaEx) {
		loopAction.getIterationCount_LoopAction().setSpecification(this.evaExToSpecification(evaEx));
	}

	/**
	 * Annotating the EvaluableExpression onto the given PCM element.
	 *
	 * @param branchAction The PCM element
	 * @param evaEx The Expression to annotate
	 */
	public void annotateEvaExFor(final BranchActionImpl branchAction, final EvaluableExpression evaEx) {
		// CAN NOT BE DONE YET. THE IMPLEMENTATION OF SEFFBRANCH DOES NOT REFLECT
		// THE STRUCTURE OF A BRANCHACTION, AS A SEFFBRANCH (A MEASURABLESEFFELEMENT)
		// WILL ONLY BE ANNOTATED WITH ONE EXPRESSION, NOT FOR EACH BRACH A NEWLY ONE.
	}

	/**
	 * Annotating the EvaluableExpression onto the given PCM element.
	 *
	 * @param internalAction The PCM element
	 * @param type The ResourceDemandType of the InternalAction (
	 *            {@link ResourceDemandType#RESOURCE_TYPE_CPU_NS} and
	 *            {@link ResourceDemandType#RESOURCE_TYPE_HDD_NS} are accepted so far
	 * @param evaEx The Expression to annotate
	 */
	public void annotateEvaExFor(final InternalActionImpl internalAction, final ResourceDemandType type,
		final EvaluableExpression evaEx) {
		final EList<ParametricResourceDemand> parametricResourceDemands = internalAction.getResourceDemand_Action();
		for (ParametricResourceDemand parametricResourceDemand : parametricResourceDemands) {
			final ProcessingResourceType processingResourceType =
				parametricResourceDemand.getRequiredResource_ParametricResourceDemand();

			if (this.typeMappings.getBeagleType(processingResourceType) == type) {
				parametricResourceDemand.getSpecification_ParametericResourceDemand()
					.setSpecification(this.evaExToSpecification(evaEx));
			}

		}
	}

	/**
	 * Annotating the EvaluableExpression onto the given PCM element.
	 *
	 * @param externalAction The PCM element
	 * @param evaEx The Expression to annotate
	 */
	public void annotateEvaExFor(final ExternalCallActionImpl externalAction, final EvaluableExpression evaEx) {
		// MAY BE IMPLEMENTED AS NICE TO HAVE CRITERIA, TOGHETHER WITH THE GENETIC
		// APPROACH
	}

	/**
	 * Supports only Constant EvaluableExpressions right now. Converts EvaluableExpression
	 * into a String-specification of the PCM Stochastic Expression.
	 * 
	 *
	 * @param evaEx constant EvaluableExpression
	 * @return String specification of EvaluableExpression
	 */
	private String evaExToSpecification(final EvaluableExpression evaEx) {
		if (evaEx.getClass() == ConstantExpression.class) {
			return ((ConstantExpression) evaEx).getValue() + "";
		}
		return "";
	}
}
