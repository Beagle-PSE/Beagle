package de.uka.ipd.sdq.beagle.core;

import de.uka.ipd.sdq.beagle.core.judge.EvaluableExpressionFitnessFunction;

import org.apache.commons.lang3.Validate;
import org.palladiosimulator.pcm.usagemodel.Branch;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Creates a new {@link Blackboard}. Therefore you can add information for the blackboard
 * with the methods of this class and get a Blackboard containing all these information
 * with {@link #getBlackboard()}
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
	 * Private data of tools, written through {@link #writeFor(Class, Serializable)}.
	 * Should be written to the blackboard, when creating it.
	 */
	private final Map<Class<? extends BlackboardStorer<? extends Serializable>>, Object> privateWrittenData =
		new HashMap<>();

	/**
	 * A blackboard with all information provided via this class.
	 *
	 * @return A new {@link Blackboard} instance with all information provided via this
	 *         class.
	 *
	 */
	public Blackboard createBlackboard() {
		// CHECKSTYLE:IGNORE BooleanExpressionComplexity
		if (this.rdias == null || this.branches == null || this.externalCalls == null || this.fitnessFunction == null
			|| this.projectInformation == null) {
			throw new IllegalStateException("Not everything has been setup yet.");
		}
		final Blackboard blackboard = new Blackboard(this.rdias, this.branches, this.loops, this.externalCalls,
			this.fitnessFunction, this.projectInformation);
		for (final Class<? extends BlackboardStorer<? extends Serializable>> writer : this.privateWrittenData
			.keySet()) {
			this.write(blackboard, writer);
		}
		return blackboard;
	}

	/**
	 * Helper method to perform {@link #writeFor(Class, Serializable)} on the
	 * {@link Blackboard}.
	 *
	 * @param <WRITTEN_TYPE> The type to wirte.
	 * @param blackboard the {@link Blackboard} to write to.
	 * @param writer the writer to write.
	 */
	@SuppressWarnings("unchecked")
	private <WRITTEN_TYPE extends Serializable> void write(final Blackboard blackboard,
		final Class<? extends BlackboardStorer<WRITTEN_TYPE>> writer) {
		blackboard.writeFor(writer, (WRITTEN_TYPE) this.privateWrittenData.get(writer));
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

	/**
	 * Calls to this method will be delegated to
	 * {@link Blackboard#writeFor(Class, Serializable)}, when the blackboard is created.
	 *
	 * @param writer The class the data should be written for. Must not be {@code null}.
	 * @param written The data to write.
	 * @param <WRITTEN_TYPE> {@code written}’s type.
	 * @see Blackboard#writeFor(Class, Serializable)
	 */
	public <WRITTEN_TYPE extends Serializable> void writeFor(
		final Class<? extends BlackboardStorer<WRITTEN_TYPE>> writer, final WRITTEN_TYPE written) {
		Validate.notNull(writer);
		this.privateWrittenData.put(writer, written);
	}
}
