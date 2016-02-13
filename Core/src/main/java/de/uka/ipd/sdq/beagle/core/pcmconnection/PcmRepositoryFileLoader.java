package de.uka.ipd.sdq.beagle.core.pcmconnection;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.palladiosimulator.pcm.repository.RepositoryFactory;
import org.palladiosimulator.pcm.repository.impl.RepositoryImpl;

import java.io.File;

/**
 * Contains a method to load a {@link RepositoryImpl}-instance from a given {@link File}.
 * 
 * @author Ansgar Spiegler
 */
public class PcmRepositoryFileLoader {

	/**
	 * Load/Initialize the repository for a given File.
	 *
	 * @param repositoryFile The repositoryFile
	 * @return the {@link RepositoryImpl}
	 */
	public RepositoryImpl loadRepositoryFromFile(final File repositoryFile) {
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
		return repository;
	}

}
