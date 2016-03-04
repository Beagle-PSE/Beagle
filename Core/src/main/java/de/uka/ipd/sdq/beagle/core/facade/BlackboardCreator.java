package de.uka.ipd.sdq.beagle.core.facade;

import de.uka.ipd.sdq.beagle.core.Blackboard;
import de.uka.ipd.sdq.beagle.core.ExternalCallParameter;
import de.uka.ipd.sdq.beagle.core.ProjectInformation;
import de.uka.ipd.sdq.beagle.core.ResourceDemandingInternalAction;
import de.uka.ipd.sdq.beagle.core.SeffBranch;
import de.uka.ipd.sdq.beagle.core.SeffLoop;
import de.uka.ipd.sdq.beagle.core.judge.EvaluableExpressionFitnessFunction;
import de.uka.ipd.sdq.beagle.core.pcmconnection.PcmBeagleMappings;
import de.uka.ipd.sdq.beagle.core.pcmconnection.PcmRepositoryBlackboardFactoryAdder;

import org.apache.commons.lang3.Validate;
import org.palladiosimulator.pcm.usagemodel.Branch;

import java.util.Set;

/**
 * Creates a new {@link Blackboard}. Therefore you can add information for the blackboard
 * with the methods of this class and get a Blackboard containing all these information
 * with {@link #createBlackboard()}
 *
 * @author Roman Langrehr
 */
public class BlackboardCreator {

	/**
	 * The {@linkplain ResourceDemandingInternalAction ResourceDemandingInternalActions}
	 * for the {@link Blackboard}.
	 */
	private Set<ResourceDemandingInternalAction> rdias;

	/**
	 * The {@linkplain Branch Branches} for the {@link Blackboard}.
	 */
	private Set<SeffBranch> branches;

	/**
	 * The {@linkplain SeffLoop SeffLoops} for the {@link Blackboard}.
	 */
	private Set<SeffLoop> loops;

	/**
	 * The {@linkplain ExternalCallParameter ExternalCallParameter} for the
	 * {@link Blackboard}.
	 */
	private Set<ExternalCallParameter> externalCalls;

	/**
	 * The {@link EvaluableExpressionFitnessFunction} for the {@link Blackboard}.
	 */
	private EvaluableExpressionFitnessFunction fitnessFunction;

	/**
	 * The {@link ProjectInformation} for the {@link Blackboard}.
	 */
	private ProjectInformation projectInformation;

	/**
	 * The {@link PcmBeagleMappings} for the {@link Blackboard} to create.
	 */
	private PcmBeagleMappings pcmMappings;

	/**
	 * A blackboard with all information provided via this class.
	 *
	 * @return A new {@link Blackboard} instance with all information provided via this
	 *         class.
	 *
	 */
	public Blackboard createBlackboard() {
		if (this.rdias == null || this.branches == null || this.loops == null || this.externalCalls == null
			|| this.fitnessFunction == null || this.projectInformation == null || this.pcmMappings == null) {
			throw new IllegalStateException("Not everything has been setup yet.");
		}
		final Blackboard blackboard = new Blackboard(this.rdias, this.branches, this.loops, this.externalCalls,
			this.fitnessFunction, this.projectInformation);

		blackboard.writeFor(PcmRepositoryBlackboardFactoryAdder.class, this.pcmMappings);
		return blackboard;
	}

	/**
	 * Sets the {@link PcmBeagleMappings} for the {@link Blackboard} to create.
	 *
	 * @param pcmMappings the {@link PcmBeagleMappings} for the {@link Blackboard} to
	 *            create. May not be {@code null}.
	 */
	public void setPcmMappings(final PcmBeagleMappings pcmMappings) {
		Validate.notNull(pcmMappings);
		this.pcmMappings = pcmMappings;
	}

	/**
	 * Sets the {@linkplain ResourceDemandingInternalAction
	 * ResourceDemandingInternalActions} for the {@link Blackboard}.
	 *
	 * @param rdias the {@linkplain ResourceDemandingInternalAction
	 *            ResourceDemandingInternalActions} for the {@link Blackboard}. May not be
	 *            {@code null}.
	 */
	public void setRdias(final Set<ResourceDemandingInternalAction> rdias) {
		Validate.notNull(rdias);
		this.rdias = rdias;
	}

	/**
	 * Sets the {@linkplain Branch Branches} for the {@link Blackboard}.
	 *
	 * @param branches the {@linkplain Branch Branches} for the {@link Blackboard}. May
	 *            not be {@code null}.
	 */
	public void setBranches(final Set<SeffBranch> branches) {
		Validate.notNull(branches);
		this.branches = branches;
	}

	/**
	 * Sets the {@linkplain SeffLoop SeffLoops} for the {@link Blackboard}.
	 *
	 * @param loops the {@linkplain SeffLoop SeffLoops} for the {@link Blackboard}. May
	 *            not be {@code null}.
	 */
	public void setLoops(final Set<SeffLoop> loops) {
		Validate.notNull(loops);
		this.loops = loops;
	}

	/**
	 * The {@linkplain ExternalCallParameter ExternalCallParameter} for the
	 * {@link Blackboard}.
	 *
	 * @param externalCalls the {@linkplain ExternalCallParameter ExternalCallParameter}
	 *            for the {@link Blackboard}. May not be {@code null}.
	 */
	public void setExternalCalls(final Set<ExternalCallParameter> externalCalls) {
		Validate.notNull(externalCalls);
		this.externalCalls = externalCalls;
	}

	/**
	 * The {@link EvaluableExpressionFitnessFunction} for the {@link Blackboard}.
	 *
	 * @param fitnessFunction The {@link EvaluableExpressionFitnessFunction} for the
	 *            {@link Blackboard}. May not be {@code null}.
	 */
	public void setFitnessFunction(final EvaluableExpressionFitnessFunction fitnessFunction) {
		Validate.notNull(fitnessFunction);
		this.fitnessFunction = fitnessFunction;
	}

	/**
	 * Sets the {@link ProjectInformation} for the {@link Blackboard}.
	 *
	 * @param projectInformation the {@link ProjectInformation} for the {@link Blackboard}
	 *            . May not be {@code null}. .
	 */
	public void setProjectInformation(final ProjectInformation projectInformation) {
		Validate.notNull(projectInformation);
		this.projectInformation = projectInformation;
	}
}
