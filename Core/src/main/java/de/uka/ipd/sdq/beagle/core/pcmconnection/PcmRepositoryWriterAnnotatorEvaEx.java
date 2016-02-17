package de.uka.ipd.sdq.beagle.core.pcmconnection;

import de.uka.ipd.sdq.beagle.core.ResourceDemandType;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.ConstantExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpression;

import org.eclipse.emf.common.util.EList;
import org.palladiosimulator.pcm.core.CoreFactory;
import org.palladiosimulator.pcm.core.PCMRandomVariable;
import org.palladiosimulator.pcm.resourcetype.ProcessingResourceType;
import org.palladiosimulator.pcm.seff.impl.BranchActionImpl;
import org.palladiosimulator.pcm.seff.impl.ExternalCallActionImpl;
import org.palladiosimulator.pcm.seff.impl.InternalActionImpl;
import org.palladiosimulator.pcm.seff.impl.LoopActionImpl;
import org.palladiosimulator.pcm.seff.seff_performance.ParametricResourceDemand;
import org.palladiosimulator.pcm.seff.seff_performance.SeffPerformanceFactory;

import java.util.LinkedList;

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
		if (loopAction == null || evaEx == null) {
			throw new NullPointerException("No null arguments in annotateEvaExFor-method allowed!");
		}
		loopAction.getIterationCount_LoopAction().setSpecification(this.evaExToSpecification(evaEx));
	}

	/**
	 * Annotating the EvaluableExpression onto the given PCM element.
	 *
	 * @param branchAction The PCM element
	 * @param evaEx The Expression to annotate
	 */
	public void annotateEvaExFor(final BranchActionImpl branchAction, final EvaluableExpression evaEx) {
		if (branchAction == null || evaEx == null) {
			throw new NullPointerException("No null arguments in annotateEvaExFor-method allowed!");
		}
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
	 *            {@link ResourceDemandType#RESOURCE_TYPE_HDD_NS} are accepted so far. If
	 *            the suitable Type is found more than one time, it is deleted every
	 *            further time. If the suitable Type is not yet in this InternalAction, it
	 *            is created newly and added.
	 * @param evaEx The Expression to annotate
	 */
	public void annotateEvaExFor(final InternalActionImpl internalAction, final ResourceDemandType type,
		final EvaluableExpression evaEx) {
		if (internalAction == null || evaEx == null) {
			throw new NullPointerException("No null arguments in annotateEvaExFor-method allowed!");
		}
		// Other TYPES are not supported so far!
		if (!type.equals(ResourceDemandType.RESOURCE_TYPE_CPU_NS)
			&& !type.equals(ResourceDemandType.RESOURCE_TYPE_HDD_NS)) {
			return;
		}

		boolean hasWrittenEvaEx = false;
		final LinkedList<ParametricResourceDemand> duplicateResourceDemandsToRemove =
			new LinkedList<ParametricResourceDemand>();

		final EList<ParametricResourceDemand> parametricResourceDemands = internalAction.getResourceDemand_Action();
		for (final ParametricResourceDemand parametricResourceDemand : parametricResourceDemands) {
			final ProcessingResourceType processingResourceType =
				parametricResourceDemand.getRequiredResource_ParametricResourceDemand();

			if (type.equals(this.typeMappings.getBeagleType(processingResourceType))) {
				if (!hasWrittenEvaEx) {
					parametricResourceDemand.getSpecification_ParametericResourceDemand()
						.setSpecification(this.evaExToSpecification(evaEx));
					hasWrittenEvaEx = true;
				} else {
					duplicateResourceDemandsToRemove.add(parametricResourceDemand);
				}
			}
		}

		// In case there are more than one ResourceDemands of the same Type are found, the
		// duplicates are deleted.
		if (!duplicateResourceDemandsToRemove.isEmpty()) {
			parametricResourceDemands.removeAll(duplicateResourceDemandsToRemove);
		}

		// In case there is no such ResourceDemandType at this InternalAction a new
		// ResourceDemand with this Type is added.
		if (!hasWrittenEvaEx) {
			final ParametricResourceDemand prdToAdd = SeffPerformanceFactory.eINSTANCE.createParametricResourceDemand();
			final PCMRandomVariable randomVar = CoreFactory.eINSTANCE.createPCMRandomVariable();
			randomVar.setSpecification(this.evaExToSpecification(evaEx));
			prdToAdd.setSpecification_ParametericResourceDemand(randomVar);

			internalAction.getResourceDemand_Action().add(prdToAdd);
			prdToAdd.setRequiredResource_ParametricResourceDemand(this.typeMappings.getPcmType(type));
			prdToAdd.setAction_ParametricResourceDemand(internalAction);
		}

	}

	/**
	 * Annotating the EvaluableExpression onto the given PCM element.
	 *
	 * @param externalAction The PCM element
	 * @param evaEx The Expression to annotate
	 */
	public void annotateEvaExFor(final ExternalCallActionImpl externalAction, final EvaluableExpression evaEx) {
		if (externalAction == null || evaEx == null) {
			throw new NullPointerException("No null arguments in annotateEvaExFor-method allowed!");
		}
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
