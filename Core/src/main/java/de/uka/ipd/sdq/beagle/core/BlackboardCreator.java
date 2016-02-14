package de.uka.ipd.sdq.beagle.core;

import de.uka.ipd.sdq.beagle.core.judge.EvaluableExpressionFitnessFunction;
import de.uka.ipd.sdq.beagle.core.pcmconnection.PcmBeagleMappings;
import de.uka.ipd.sdq.beagle.core.pcmconnection.PcmRepositoryBlackboardFactoryAdder;

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
		if (this.rdias == null || this.branches == null || this.externalCalls == null || this.fitnessFunction == null
			|| this.projectInformation == null || this.pcmMappings == null) {
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
	 *            create.
	 */
	public void setPcmMappings(final PcmBeagleMappings pcmMappings) {
		this.pcmMappings = pcmMappings;
	}

	/**
	 * Sets the {@linkplain ResourceDemandingInternalAction
	 * ResourceDemandingInternalActions} for the {@link Blackboard}.
	 *
	 * @param rdias the {@linkplain ResourceDemandingInternalAction
	 *            ResourceDemandingInternalActions} for the {@link Blackboard}
	 */
	public void setRdias(final Set<ResourceDemandingInternalAction> rdias) {
		this.rdias = rdias;
	}

	/**
	 * Sets the {@linkplain Branch Branches} for the {@link Blackboard}.
	 *
	 * @param branches the {@linkplain Branch Branches} for the {@link Blackboard}
	 */
	public void setBranches(final Set<SeffBranch> branches) {
		this.branches = branches;
	}

	/**
	 * Sets the {@linkplain SeffLoop SeffLoops} for the {@link Blackboard}.
	 *
	 * @param loops the {@linkplain SeffLoop SeffLoops} for the {@link Blackboard}
	 */
	public void setLoops(final Set<SeffLoop> loops) {
		this.loops = loops;
	}

	/**
	 * The {@linkplain ExternalCallParameter ExternalCallParameter} for the
	 * {@link Blackboard}.
	 *
	 * @param externalCalls the {@linkplain ExternalCallParameter ExternalCallParameter}
	 *            for the {@link Blackboard}
	 */
	public void setExternalCalls(final Set<ExternalCallParameter> externalCalls) {
		this.externalCalls = externalCalls;
	}

	/**
	 * The {@link EvaluableExpressionFitnessFunction} for the {@link Blackboard}.
	 *
	 * @param fitnessFunction The {@link EvaluableExpressionFitnessFunction} for the
	 *            {@link Blackboard}.
	 */
	public void setFitnessFunction(final EvaluableExpressionFitnessFunction fitnessFunction) {
		this.fitnessFunction = fitnessFunction;
	}

	/**
	 * Sets the {@link ProjectInformation} for the {@link Blackboard}.
	 *
	 * @param projectInformation the {@link ProjectInformation} for the {@link Blackboard}
	 *            .
	 */
	public void setProjectInformation(final ProjectInformation projectInformation) {
		this.projectInformation = projectInformation;
	}
}
