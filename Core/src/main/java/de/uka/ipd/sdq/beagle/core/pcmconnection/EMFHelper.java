package de.uka.ipd.sdq.beagle.core.pcmconnection;

import de.uka.ipd.sdq.identifier.Identifier;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.palladiosimulator.pcm.allocation.AllocationPackage;
import org.palladiosimulator.pcm.parameter.ParameterPackage;
import org.palladiosimulator.pcm.repository.RepositoryPackage;
import org.palladiosimulator.pcm.resourceenvironment.ResourceenvironmentPackage;
import org.palladiosimulator.pcm.resourcetype.ResourcetypePackage;
import org.palladiosimulator.pcm.seff.SeffPackage;
import org.palladiosimulator.pcm.system.SystemPackage;
import org.palladiosimulator.pcm.usagemodel.UsagemodelPackage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

/**
 * Helper class provided by the SDQ for working with PCM repository files. Also see
 * {@link EcoreUtil} for more helper functions like
 * {@link EcoreUtil#equals(EObject, EObject)} to test for equality.
 *
 * @author Anne Koziolek
 *
 */
public final class EMFHelper {

	/**
	 * Filename length for which storing is tried again. If a filename has at least this
	 * length when trying to store in {@link #saveToXMIFile(EObject, String)} and the
	 * storing fails, we’ll try again once more.
	 */
	private static final int RETRY_FILENAME_LENGTH = 250;

	/**
	 * Private constructor because this is an utility class.
	 */
	private EMFHelper() {
	}

	/**
	 * Checks for two PCM model elements whether they are the same, i.e. whether they have
	 * the same ID. The model elements have to be derived from Identifier. Note that two
	 * systems might use the same assembly contexts and components, but still are two
	 * different systems. If one of the Identifiers in null, false is returned.
	 *
	 * @param identifier1 One Identifier
	 * @param identifier2 Another Identifier
	 * @return true if i1.getId().equals(i2.getId()), false otherwise
	 */
	public static boolean checkIdentity(final EObject identifier1, final EObject identifier2) {
		if (identifier1 == null || identifier2 == null) {
			return false;
		}
		if (identifier1 instanceof Identifier && identifier2 instanceof Identifier) {
			return (((Identifier) identifier1).getId().equals(((Identifier) identifier2).getId()));
		} else {
			return EcoreUtil.equals(identifier1, identifier2);
		}
	}

	/**
	 * Implements an identifier-based contains. Calls
	 * {@link #checkIdentity(EObject, EObject)} to compare the {@link Identifier} i with
	 * the contents of the collection.
	 *
	 * @param coll A collection of objects.
	 * @param identity An identity to search for.
	 * @return true if there is an {@link Identifier} in {@code coll} with an id equal to
	 *         i.getID().
	 */
	public static boolean contains(final Collection<? extends EObject> coll, final EObject identity) {
		for (final EObject identifier : coll) {
			if (checkIdentity(identifier, identity)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * {@link Collection#retainAll(Collection)} for {@link Identifier Identifiers} based
	 * on {@link #checkIdentity(EObject, EObject)}. This method will leave all identifiers
	 * in {@code collection} that describe an object is {@code itemsToRetain}.
	 *
	 * @param collection A collection of identifiers.
	 * @param itemsToRetain The items whose identifiers should be left in
	 *            {@code collection}.
	 * @return {@code true} only if an element was removed from {@code collection}.
	 */
	public static boolean retainAll(final Collection<? extends Identifier> collection,
		final Collection<? extends EObject> itemsToRetain) {
		boolean removedAny = false;
		for (final Iterator<? extends Identifier> iterator = collection.iterator(); iterator.hasNext();) {
			final Identifier identifier = iterator.next();
			boolean identifierContainedInItemsToRetain = false;
			for (final EObject identifierToRetain : itemsToRetain) {
				identifierContainedInItemsToRetain |= checkIdentity(identifier, identifierToRetain);
			}
			if (!identifierContainedInItemsToRetain) {
				iterator.remove();
				removedAny = true;
			}
		}
		return removedAny;
	}

	/**
	 * Save the given EObject to the file given by filename.
	 *
	 * @param modelToSave The EObject to save
	 * @param fileName The filename where to save.
	 * @throws IOException If accessing the file fails.
	 */
	public static void saveToXMIFile(final EObject modelToSave, final String fileName) throws IOException {
		saveToXMIFile(modelToSave, fileName, true);
	}

	/**
	 * Additional parameter mayRetry to detect to deep recursion.
	 *
	 * @param modelToSave The EObject to save
	 * @param fileName The filename where to save.
	 * @param mayRetry {@code true} saving shall be tried once again if a
	 *            {@link FileNotFoundException} is thrown and {@code fileName} is longer
	 *            as {@link #RETRY_FILENAME_LENGTH}.
	 * @throws IOException If accessing the file fails.
	 */
	private static void saveToXMIFile(final EObject modelToSave, final String fileName, final boolean mayRetry)
		throws IOException {
		// final Logger logger = Logger.getLogger("de.uka.ipd.sdq.dsexplore");

		// logger.debug("Saving " + modelToSave.toString() + " to " + fileName);

		// Create a resource set.
		final ResourceSet resourceSet = new ResourceSetImpl();

		// Register the default resource factory -- only needed for stand-alone!
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
			.put(Resource.Factory.Registry.DEFAULT_EXTENSION, new XMIResourceFactoryImpl());

		final URI myURI = URI.createURI(fileName);

		final Resource resource = resourceSet.createResource(myURI);
		resource.getContents().add(modelToSave);

		try {
			resource.save(Collections.EMPTY_MAP);
		} catch (final FileNotFoundException fileNotFound) {
			if (mayRetry && fileName.length() > RETRY_FILENAME_LENGTH) {
				// try again with a shorter filename, but just one more try (mayRetry =
				// false).
				saveToXMIFile(modelToSave,
					fileName.substring(0, fileName.indexOf("-")) + "-shortened-" + fileName.hashCode(), false);
			} else {
				throw fileNotFound;
			}
		}
	}

	/**
	 * Loads the root object from an EMF File.
	 *
	 * @param fileName Name of the file to load from.
	 * @param ePackage The package to load for.
	 * @return The root element read.
	 */
	public static EObject loadFromXMIFile(final String fileName, final EPackage ePackage) {
		// Create a resource set to hold the resources.
		final ResourceSet resourceSet = new ResourceSetImpl();

		// Register the appropriate resource factory to handle all file
		// extensions.
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
			.put(Resource.Factory.Registry.DEFAULT_EXTENSION, new XMIResourceFactoryImpl());

		// Register the package to ensure it is available during loading.
		registerPackages(resourceSet);

		return loadFromXMIFile(fileName, resourceSet, ePackage);
	}

	/**
	 * Loads the root object from an EMF File.
	 *
	 * @param fileName Name of the file to load from.
	 * @param resourceSet The resource set to load.
	 * @param ePackage The package to load for.
	 * @return The root element read.
	 */
	public static EObject loadFromXMIFile(final String fileName, final ResourceSet resourceSet,
		final EPackage ePackage) {
		// Construct the URI for the instance file.
		// The argument is treated as a file path only if it denotes an existing
		// file. Otherwise, it's directly treated as a URL.
		final File file = new File(fileName);
		final URI fileUri = file.isFile() ? URI.createFileURI(file.getAbsolutePath()) : URI.createURI(fileName);

		Resource resource = null;
		// Demand load resource for this file.
		resourceSet.getPackageRegistry().put(ePackage.getNsURI(), ePackage);
		resource = resourceSet.getResource(fileUri, true);

		final EObject eObject = resource.getContents().iterator().next();
		return EcoreUtil.getRootContainer(eObject);
	}

	/**
	 * Copied From de.uka.ipd.sdq.pcmsolver.models.PCMInstance.
	 *
	 * @param resourceSet The resource set to register all contained model packages with.
	 */
	private static void registerPackages(final ResourceSet resourceSet) {
		resourceSet.getPackageRegistry().put(AllocationPackage.eNS_URI, AllocationPackage.eINSTANCE);
		resourceSet.getPackageRegistry().put(ParameterPackage.eNS_URI, ParameterPackage.eINSTANCE);
		resourceSet.getPackageRegistry().put(ResourceenvironmentPackage.eNS_URI, ResourceenvironmentPackage.eINSTANCE);
		resourceSet.getPackageRegistry().put(ResourcetypePackage.eNS_URI, ResourcetypePackage.eINSTANCE);
		resourceSet.getPackageRegistry().put(RepositoryPackage.eNS_URI, RepositoryPackage.eINSTANCE);
		resourceSet.getPackageRegistry().put(SeffPackage.eNS_URI, SeffPackage.eINSTANCE);
		resourceSet.getPackageRegistry().put(SystemPackage.eNS_URI, SystemPackage.eINSTANCE);
		resourceSet.getPackageRegistry().put(UsagemodelPackage.eNS_URI, UsagemodelPackage.eINSTANCE);

	}
}
