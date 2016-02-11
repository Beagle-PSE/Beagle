package de.uka.ipd.sdq.beagle.core.pcmconnection;
/**
 * ATTENTION: Test coverage check turned off. Remove this comments block when implementing
 * this class!
 * 
 * <p>COVERAGE:OFF
 * 
 * Also Checkstyle <p>CHECKSTYLE:OFF
 */

import de.uka.ipd.sdq.beagle.core.Blackboard;
import de.uka.ipd.sdq.beagle.core.ResourceDemandingInternalAction;
import de.uka.ipd.sdq.beagle.core.SeffBranch;
import de.uka.ipd.sdq.beagle.core.SeffLoop;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpression;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.palladiosimulator.pcm.repository.RepositoryFactory;
import org.palladiosimulator.pcm.repository.impl.RepositoryImpl;

import java.io.File;

/**
 * Writes Beagle’s findings back to a PCM repository. This class is meant to be used for
 * Blackboard instances created by {@link PcmRepositoryBlackboardFactory} and may not be
 * useable with other blackboards.
 *
 * @author Joshua Gleitze
 * @author Ansgar Spiegler
 */
public class PcmRepositoryWriter {

	/**
	 * Blackboard to get Mapping from.
	 */
	private final Blackboard blackboard;

	// private final Set<ResourceDemandingInternalAction> rdiaSet;

	/**
	 * Creates a writer to write the results written on {@code blackboard} back to a PCM
	 * repository.
	 *
	 * @param blackboard The blackboard containing results.
	 */
	public PcmRepositoryWriter(final Blackboard blackboard) {
		this.blackboard = blackboard;
	}

	/**
	 * Writes the Beagle’s findings to the {@code repositoryFile}. For each
	 * {@linkplain ResourceDemandingInternalAction}, {@linkplain SeffBranch} and
	 * {@linkplain SeffLoop}, the method will look up the identifier of the element in the
	 * source repository file the object was created for. If {@code repositoryFile}
	 * contains an element with this identifier that is of the appropriate type, the
	 * object’s result will appropriately written to {@code repositoryFile}. Nothing will
	 * be written otherwise.
	 *
	 * @param repositoryFile A PCM repository file to write Beagle’s results to. It should
	 *            use the same identifiers for elements as the repository file Beagle’s
	 *            blackboard was initially created for.
	 */
	public void writeTo(final File repositoryFile) {

		if (repositoryFile == null) {
			throw new NullPointerException();
		}

		RepositoryFactory.eINSTANCE.createRepository();
		// Not sure if this final declaration could lead to a problem.
		final EPackage ePackage = RepositoryFactory.eINSTANCE.getEPackage();

		final EObject eObject = EMFHelper.loadFromXMIFile(repositoryFile.getAbsolutePath(), ePackage);
		if (!(eObject.getClass() == RepositoryImpl.class)) {
			throw new IllegalArgumentException("File is not a repository!");
		}
		final RepositoryImpl repository = (RepositoryImpl) eObject;

		for (ResourceDemandingInternalAction rdia : this.blackboard.getAllRdias()) {
			EvaluableExpression evaEx = this.blackboard.getFinalExpressionFor(rdia);
			if (evaEx != null) {

			}
		}

	}

	private void annotateEvaExTo() {

	}
}
