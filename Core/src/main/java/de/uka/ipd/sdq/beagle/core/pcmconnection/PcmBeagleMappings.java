package de.uka.ipd.sdq.beagle.core.pcmconnection;

import de.uka.ipd.sdq.beagle.core.ResourceDemandingInternalAction;
import de.uka.ipd.sdq.beagle.core.SeffBranch;
import de.uka.ipd.sdq.beagle.core.SeffLoop;

import java.io.Serializable;

/**
 * Maps Beagle objects to identifiers of the elements they were created for in a specific
 * PCM repository.
 *
 * @author Joshua Gleitze
 */
public class PcmBeagleMappings implements Serializable {

	/**
	 * Serialisation version UID, see {@link java.io.Serializable}.
	 */
	private static final long serialVersionUID = -2442979526968290433L;

	/**
	 * Gets the identifier used for {@code rdia} in the PCM repository.
	 *
	 * @param rdia A resource demanding internal action. Must not be {@code null}.
	 * @return The identifier used for {@code rdia} in the PCM repository if
	 *         {@code this.hasPcmIdOf(rdia)} returns {@code true}. {@code null} otherwise.
	 */
	public String getPcmIdOf(final ResourceDemandingInternalAction rdia) {
		return null;
	}

	/**
	 * Gets the identifier used for {@code branch} in the PCM repository.
	 *
	 * @param branch A SEFF branch. Must not be {@code null}.
	 * @return The identifier used for {@code branch} in the PCM repository if
	 *         {@code this.hasPcmIdOf(branch)} returns {@code true}. {@code null}
	 *         otherwise.
	 */
	public String getPcmIdOf(final SeffBranch branch) {
		return null;
	}

	/**
	 * Gets the identifier used for {@code loop} in the PCM repository.
	 *
	 * @param loop A SEFF loop. Must not be {@code null}.
	 * @return The identifier used for {@code loop} in the PCM repository if
	 *         {@code this.hasPcmIdOf(loop)} returns {@code true}. {@code null} otherwise.
	 */
	public String getPcmIdOf(final SeffLoop loop) {
		return null;
	}

	/**
	 * Query whether this mapping contains a PCM identifier for {@code rdia}.
	 *
	 * @param rdia A resource demanding internal action. Must not be {@code null}.
	 * @return {@code true} only if this mapping contains a PCM identifier for
	 *         {@code rdia}.
	 */
	public boolean hasPcmIdOf(final ResourceDemandingInternalAction rdia) {
		return false;
	}

	/**
	 * Query whether this mapping contains a PCM identifier for {@code branch}.
	 *
	 * @param branch A SEFF branch. Must not be {@code null}.
	 * @return {@code true} only if this mapping contains a PCM identifier for
	 *         {@code branch}.
	 */
	public boolean hasPcmIdOf(final SeffBranch branch) {
		return false;
	}

	/**
	 * Query whether this mapping contains a PCM identifier for {@code loop}.
	 *
	 * @param loop A SEFF loop. Must not be {@code null}.
	 * @return {@code true} only if this mapping contains a PCM identifier for
	 *         {@code loop}.
	 */
	public boolean hasPcmIdOf(final SeffLoop loop) {
		return false;
	}

	/**
	 * Stores the {@code identifier} used for {@code rdia} in the PCM repository.
	 *
	 * @param rdia A resource demanding internal action. Must not be {@code null}.
	 * @param identifier The identifier used for {@code rdia} in the PCM repository. Must
	 *            not be {@code null}.
	 * @throws IllegalStateException If there is already another identifier assigned to
	 *             {@code rdia}. More formally: if
	 *             {@code this.hasPcmIdOf(rdia) && !this.getPcmIdOf(rdia).equals(identifier)}
	 */
	public void addPcmIdOf(final ResourceDemandingInternalAction rdia, final String identifier) {
	}

	/**
	 * Stores the {@code identifier} used for {@code branch} in the PCM repository.
	 *
	 * @param branch A SEFF branch. Must not be {@code null}.
	 * @param identifier The identifier used for {@code branch} in the PCM repository.
	 *            Must not be {@code null}.
	 * @throws IllegalStateException If there is already another identifier assigned to
	 *             {@code branch}. More formally: if
	 *             {@code this.hasPcmIdOf(branch) && !this.getPcmIdOf(branch).equals(identifier)}
	 */
	public void addPcmIdOf(final SeffBranch branch, final String identifier) {
	}

	/**
	 * Stores the {@code identifier} used for {@code loop} in the PCM repository.
	 *
	 * @param loop A SEFF loop. Must not be {@code null}.
	 * @param identifier The identifier used for {@code loop} in the PCM repository. Must
	 *            not be {@code null}.
	 * @throws IllegalStateException If there is already another identifier assigned to
	 *             {@code loop}. More formally: if
	 *             {@code this.hasPcmIdOf(loop) && !this.getPcmIdOf(loop).equals(identifier)}
	 */
	public void addPcmIdOf(final SeffLoop loop, final String identifier) {
	}

}
