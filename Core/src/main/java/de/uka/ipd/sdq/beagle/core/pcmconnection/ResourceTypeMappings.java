package de.uka.ipd.sdq.beagle.core.pcmconnection;

import de.uka.ipd.sdq.beagle.core.ResourceDemandType;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.palladiosimulator.pcm.resourcetype.ProcessingResourceType;
import org.palladiosimulator.pcm.resourcetype.ResourceRepository;
import org.palladiosimulator.pcm.resourcetype.ResourceType;

import java.util.Map;

/**
 * This class offers a bidirectional mapping between the ResourceTypes of Beagle and the
 * PalladioComponentModel.
 * 
 * @author Ansgar Spiegler
 * @author Joshua Gleitze
 */
public final class ResourceTypeMappings {

	/**
	 * The location of the resourceType-file of PCM.
	 */
	private static final String RESOURCETYPE_URI =
		"platform:/plugin/org.palladiosimulator.pcm.resources/defaultModels/Palladio.resourcetype";

	/**
	 * The bidirectional Map between {@link ResourceDemandType} and
	 * {@link ProcessinResourceType}.
	 */
	private static BidiMap<ResourceDemandType, ProcessingResourceType> beagleTypeToPcmType = new DualHashBidiMap<>();

	/**
	 * Private constructor.
	 */
	private ResourceTypeMappings() {

	}

	/**
	 * Creates a mapping for ({@link ResourceDemandType#RESOURCE_TYPE_CPU_NS},{@code CPU})
	 * and ({@link ResourceDemandType#RESOURCE_TYPE_HDD_NS}, {@code HDD}).
	 *
	 * @return An instance of this class.
	 */
	public static ResourceTypeMappings getMappings() {
		if (beagleTypeToPcmType.isEmpty()) {

			final Resource.Factory.Registry registry = Resource.Factory.Registry.INSTANCE;
			final Map<String, Object> factoryMap = registry.getExtensionToFactoryMap();
			factoryMap.put("resourcetype", new XMIResourceFactoryImpl());

			final URI resourceTypeUri = URI.createURI(RESOURCETYPE_URI);

			final ResourceSet resSet = new ResourceSetImpl();
			final Resource resource = resSet.getResource(resourceTypeUri, true);

			final ResourceRepository resourceRepository = (ResourceRepository) resource.getContents().get(0);
			for (ResourceType resourceType : resourceRepository.getAvailableResourceTypes_ResourceRepository()) {
				switch (resourceType.getEntityName()) {
					case "CPU":
						beagleTypeToPcmType.put(ResourceDemandType.RESOURCE_TYPE_CPU_NS,
							(ProcessingResourceType) resourceType);
						break;
					case "HDD":
						beagleTypeToPcmType.put(ResourceDemandType.RESOURCE_TYPE_HDD_NS,
							(ProcessingResourceType) resourceType);
						break;
					default:
				}
			}

			return new ResourceTypeMappings();

		}

		return new ResourceTypeMappings();
	}

	/**
	 * Reading from the bidirectional map.
	 *
	 * @param processingResourceType The {@link ProcessingRessourceType}
	 * @return The linked {@link ResourceDemandType}
	 */
	public ResourceDemandType getBeagleType(final ProcessingResourceType processingResourceType) {
		return beagleTypeToPcmType.getKey(processingResourceType);
	}

	/**
	 * Reading from the bidirectional map.
	 *
	 * @param resourceDemandType The {@link ResourceDemandType}
	 * @return The linked {@link ProcessingResourceType}
	 */
	public ProcessingResourceType getPcmType(final ResourceDemandType resourceDemandType) {
		return beagleTypeToPcmType.get(resourceDemandType);
	}
}
