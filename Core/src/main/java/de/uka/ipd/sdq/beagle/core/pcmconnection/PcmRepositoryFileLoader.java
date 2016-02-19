package de.uka.ipd.sdq.beagle.core.pcmconnection;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.palladiosimulator.pcm.repository.RepositoryFactory;
import org.palladiosimulator.pcm.repository.impl.RepositoryImpl;

import java.io.File;
import java.io.FileNotFoundException;

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
	 * @throws FileNotFoundException if given repository file does not exist
	 */
	public RepositoryImpl loadRepositoryFromFile(final File repositoryFile) throws FileNotFoundException {
		if (repositoryFile == null) {
			throw new NullPointerException();
		}

		if (!(repositoryFile.exists())) {
			throw new FileNotFoundException("File " + repositoryFile.getAbsolutePath() + " does not exist!");
		}

		RepositoryFactory.eINSTANCE.createRepository();
		// Not sure if this final declaration could lead to a problem.
		final EPackage ePackage = RepositoryFactory.eINSTANCE.getEPackage();

		final EObject eObject = EmfHelper.loadFromXMIFile(repositoryFile.getAbsolutePath(), ePackage);
		if (!(eObject.getClass() == RepositoryImpl.class)) {
			throw new IllegalArgumentException("File is not a repository!");
		}
		final RepositoryImpl repository = (RepositoryImpl) eObject;
		return repository;
	}

}
