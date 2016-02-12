package de.uka.ipd.sdq.beagle.core.pcmconnection;
/**
 * ATTENTION: Test coverage check turned off. Remove this comments block when implementing
 * this class!
 * 
 * <p>COVERAGE:OFF
 */

import de.uka.ipd.sdq.beagle.core.ExternalCallParameter;
import de.uka.ipd.sdq.beagle.core.ResourceDemandingInternalAction;
import de.uka.ipd.sdq.beagle.core.SeffBranch;
import de.uka.ipd.sdq.beagle.core.SeffLoop;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Maps Beagle objects to identifiers of the elements they were created for in a specific
 * PCM repository.
 *
 * @author Joshua Gleitze
 * @author Ansgar Spiegler
 */
public class PcmBeagleMappings implements Serializable {

	/**
	 * Serialisation version UID, see {@link java.io.Serializable}.
	 */
	private static final long serialVersionUID = -2442979526968290433L;

	/**
	 * The HashMap for {@link ResourceDemandingInternalAction} storing all IDs as values
	 * for one specific SeffElement.
	 */
	private HashMap<ResourceDemandingInternalAction, String> rdiaMap;

	/**
	 * The HashMap for {@link SeffBranch} storing all IDs as values for one specific
	 * SeffElement.
	 */
	private HashMap<SeffBranch, String> seffBranchMap;

	/**
	 * The HashMap for {@link SeffLoop} storing all IDs as values for one specific
	 * SeffElement.
	 */
	private HashMap<SeffLoop, String> seffLoopMap;

	/**
	 * The HashMap for {@link ExternalCallParameter} storing all IDs as values for one
	 * specific SeffElement.
	 */
	private HashMap<ExternalCallParameter, String> ecpMap;

	/**
	 * Gets the identifier used for {@code rdia} in the PCM repository.
	 *
	 * @param rdia A resource demanding internal action. Must not be {@code null}.
	 * @return The identifier used for {@code rdia} in the PCM repository if
	 *         {@code this.hasPcmIdOf(rdia)} returns {@code true}. {@code null} otherwise.
	 */
	public String getPcmIdOf(final ResourceDemandingInternalAction rdia) {
		if (rdia == null) {
			throw new NullPointerException("Parameter of getPcmIdOf should not be null!");
		}
		if (this.hasPcmIdOf(rdia)) {
			return this.rdiaMap.get(rdia);
		}
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
		if (branch == null) {
			throw new NullPointerException("Parameter of getPcmIdOf should not be null!");
		}
		if (this.hasPcmIdOf(branch)) {
			return this.seffBranchMap.get(branch);
		}
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
		if (loop == null) {
			throw new NullPointerException("Parameter of getPcmIdOf should not be null!");
		}
		if (this.hasPcmIdOf(loop)) {
			return this.seffLoopMap.get(loop);
		}
		return null;
	}

	/**
	 * Gets the identifier used for {@code loop} in the PCM repository.
	 *
	 * @param externalCallParameter An ExternalCallParameter. Must not be {@code null}.
	 * @return The identifier used for {@code externalCallParameter} in the PCM repository
	 *         if {@code this.hasPcmIdOf(externalCallParameter)} returns {@code true}.
	 *         {@code null} otherwise.
	 */
	public String getPcmIdOf(final ExternalCallParameter externalCallParameter) {
		if (externalCallParameter == null) {
			throw new NullPointerException("Parameter of getPcmIdOf should not be null!");
		}
		if (this.hasPcmIdOf(externalCallParameter)) {
			return this.ecpMap.get(externalCallParameter);
		}
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
		if (rdia == null) {
			throw new NullPointerException("Parameter of getPcmIdOf should not be null!");
		}
		return (this.rdiaMap.containsKey(rdia) && (this.rdiaMap.get(rdia) != null));
	}

	/**
	 * Query whether this mapping contains a PCM identifier for {@code branch}.
	 *
	 * @param branch A SEFF branch. Must not be {@code null}.
	 * @return {@code true} only if this mapping contains a PCM identifier for
	 *         {@code branch}.
	 */
	public boolean hasPcmIdOf(final SeffBranch branch) {
		if (branch == null) {
			throw new NullPointerException("Parameter of getPcmIdOf should not be null!");
		}
		return (this.seffBranchMap.containsKey(branch) && (this.seffBranchMap.get(branch) != null));
	}

	/**
	 * Query whether this mapping contains a PCM identifier for {@code loop}.
	 *
	 * @param loop A SEFF loop. Must not be {@code null}.
	 * @return {@code true} only if this mapping contains a PCM identifier for
	 *         {@code loop}.
	 */
	public boolean hasPcmIdOf(final SeffLoop loop) {
		if (loop == null) {
			throw new NullPointerException("Parameter of getPcmIdOf should not be null!");
		}
		return (this.seffLoopMap.containsKey(loop) && (this.seffLoopMap.get(loop) != null));
	}

	/**
	 * Query whether this mapping contains a PCM identifier for
	 * {@code ExternalCallParameter}.
	 *
	 * @param externalCallParameter An ExternalCallParamter. Must not be {@code null}.
	 * @return {@code true} only if this mapping contains a PCM identifier for
	 *         {@code ExternalCallParameter}.
	 */
	public boolean hasPcmIdOf(final ExternalCallParameter externalCallParameter) {
		if (externalCallParameter == null) {
			throw new NullPointerException("Parameter of getPcmIdOf should not be null!");
		}
		return (this.ecpMap.containsKey(externalCallParameter) && (this.ecpMap.get(externalCallParameter) != null));
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
		if (rdia == null || identifier == null) {
			throw new NullPointerException("Parameter of getPcmIdOf should not be null!");
		}
		if (this.hasPcmIdOf(rdia) && !this.getPcmIdOf(rdia).equals(identifier)) {
			throw new IllegalStateException();
		}
		this.rdiaMap.put(rdia, identifier);
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
		if (branch == null || identifier == null) {
			throw new NullPointerException("Parameter of getPcmIdOf should not be null!");
		}
		if (this.hasPcmIdOf(branch) && !this.getPcmIdOf(branch).equals(identifier)) {
			throw new IllegalStateException();
		}
		this.seffBranchMap.put(branch, identifier);
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
		if (loop == null || identifier == null) {
			throw new NullPointerException("Parameter of getPcmIdOf should not be null!");
		}
		if (this.hasPcmIdOf(loop) && !this.getPcmIdOf(loop).equals(identifier)) {
			throw new IllegalStateException();
		}
		this.seffLoopMap.put(loop, identifier);
	}

	/**
	 * Stores the {@code identifier} used for {@code loop} in the PCM repository.
	 *
	 * @param externalCallParameter An ExternalCallParamete. Must not be {@code null}.
	 * @param identifier The identifier used for {@code externalCallParameter} in the PCM
	 *            repository. Must not be {@code null}.
	 * @throws IllegalStateException If there is already another identifier assigned to
	 *             {@code externalCallParameter}. More formally: if
	 *             {@code this.hasPcmIdOf(externalCallParameter) && !this.getPcmIdOf(externalCallParameter).equals(identifier)}
	 */
	public void addPcmIdOf(final ExternalCallParameter externalCallParameter, final String identifier) {
		if (externalCallParameter == null || identifier == null) {
			throw new NullPointerException("Parameter of getPcmIdOf should not be null!");
		}
		if (this.hasPcmIdOf(externalCallParameter) && !this.getPcmIdOf(externalCallParameter).equals(identifier)) {
			throw new IllegalStateException();
		}
		this.ecpMap.put(externalCallParameter, identifier);
	}

}
