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

public class ResourceTypeMappings {

	private static final String RESOURCETYPE_URI =
		"platform:/plugin/org.palladiosimulator.pcm.resources/defaultModels/Palladio.resourcetype";

	private static BidiMap<ResourceDemandType, ProcessingResourceType> beagleTypeToPcmType = new DualHashBidiMap<>();

	public static ResourceTypeMappings getMappings() {
		if (beagleTypeToPcmType.isEmpty()) {

			final Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
			final Map<String, Object> m = reg.getExtensionToFactoryMap();
			m.put("resourcetype", new XMIResourceFactoryImpl());

			final URI uri = URI.createURI(RESOURCETYPE_URI);

			final ResourceSet resSet = new ResourceSetImpl();
			final Resource resource = resSet.getResource(uri, true);

			ResourceRepository resourceRepository = (ResourceRepository) resource.getContents().get(0);
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

	private ResourceTypeMappings() {

	}

	public ResourceDemandType getBeagleType(ProcessingResourceType prt) {
		return beagleTypeToPcmType.getKey(prt);
	}

	public ProcessingResourceType getPcmType(ResourceDemandType rdt) {
		return beagleTypeToPcmType.get(rdt);
	}
}
