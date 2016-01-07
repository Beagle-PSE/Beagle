package de.uka.ipd.sdq.beagle.pcmconnection;

import de.uka.ipd.sdq.beagle.core.Blackboard;
import de.uka.ipd.sdq.beagle.core.BlackboardStorer;

import java.io.File;
import java.util.Collection;
import java.util.Set;

/**
 * Creates {@link Blackboard} instances suitable to analyse elements from a PCM
 * repository. The factory searches the repository for resource demanding internal
 * actions, SEFF loops and SEFF branches (hereafter called <em>PCM elements</em>). It
 * translates these into the corresponding Beagle Object and puts them on a new
 * Blackboard. To be translated, <em>PCM elements</em> must have a binding to sections in
 * source code files expressed in the source code decorator model.
 *
 * @author Joshua Gleitze
 */
public class PcmRepositoryBlackboardFactory implements BlackboardStorer<PcmBeagleMappings> {

	/**
	 * Creates a factory that will search the provided PCM files for <em>PCM
	 * elements</em>.
	 *
	 * @param pcmRepositoryFiles PCM repository files.
	 */
	public PcmRepositoryBlackboardFactory(final File... pcmRepositoryFiles) {
	}

	/**
	 * Creates a factory that will search the provided PCM files for <em>PCM
	 * elements</em>.
	 *
	 * @param pcmRepositoryFiles PCM repository files.
	 */
	public PcmRepositoryBlackboardFactory(final Set<File> pcmRepositoryFiles) {
	}

	/**
	 * Builds a new blackboard and writes the Beagle objects representing all found
	 * <em>PCM elements</em> to it. Only <em>PCM elements</em> that fulfil the
	 * restrictions described in the class description will be written.
	 *
	 * @return A new blackboard having all translated <em>PCM elements</em> written on it.
	 *         Will never be {@code null}.
	 */
	public Blackboard getBlackboardForAllElements() {
		return null;
	}

	/**
	 * Builds a new blackboard and writes the Beagle objects representing <em>PCM
	 * elements</em> related to {@code ids} to it. Only <em>PCM elements</em> that fulfil
	 * the restrictions described in the class description will be written.
	 *
	 * <p>For any provided ID {@code id}, <em>PCM elements</em> will be selected to be
	 * written on the blackboard as follows:
	 *
	 * <ul>
	 *
	 * <li>If {@code id} describes a component, all <em>PCM elements</em> that are part of
	 * the component’s service effect specification will be selected. Nested components
	 * will be inspected recursively.
	 *
	 * <li>If {@code id} describes a repository, all contained components will be
	 * inspected as if their ID had been provided.
	 *
	 * <li>If {@code id} describes a <em>PCM element</em>, it will be selected.
	 *
	 * <li>Any other ID will be silently ignored.
	 *
	 * </ul>
	 *
	 * @param identifiers Identifiers of elements in the repository files that shall be
	 *            written to the Blackboard.
	 * @return A new blackboard having all selected and translated <em>PCM elements</em>
	 *         written on it. Will never be {@code null}.
	 */
	public Blackboard getBlackboardForIds(final Collection<String> identifiers) {
		return null;
	}

	/**
	 * Builds a new blackboard and writes the Beagle objects representing <em>PCM
	 * elements</em> related to {@code ids} to it. Only <em>PCM elements</em> that fulfil
	 * the restrictions described in the class description will be written.
	 *
	 * <p>For any provided ID {@code id}, <em>PCM elements</em> will be selected to be
	 * written on the blackboard as follows:
	 *
	 * <ul>
	 *
	 * <li>If {@code id} describes a component, all <em>PCM elements</em> that are part of
	 * the component’s service effect specification will be selected. Nested components
	 * will be inspected recursively.
	 *
	 * <li>If {@code id} describes a repository, all contained components will be
	 * inspected as if their ID had been provided.
	 *
	 * <li>If {@code id} describes a <em>PCM element</em>, it will be selected.
	 *
	 * <li>Any other ID will be silently ignored.
	 *
	 * </ul>
	 *
	 * @param identifiers Identifiers of elements in the repository files that shall be
	 *            written to the Blackboard.
	 * @return A new blackboard having all selected and translated <em>PCM elements</em>
	 *         written on it. Will never be {@code null}.
	 */
	public Blackboard getBlackboardForIds(final String... identifiers) {
		return null;
	}
}
