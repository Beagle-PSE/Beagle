package de.uka.ipd.sdq.beagle.core.pcmconnection;

import de.uka.ipd.sdq.beagle.core.ResourceDemandType;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.apache.commons.lang3.Validate;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.palladiosimulator.pcm.resourcetype.ProcessingResourceType;
import org.palladiosimulator.pcm.resourcetype.ResourceRepository;
import org.palladiosimulator.pcm.resourcetype.ResourceType;

import java.util.HashMap;
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
	private static final String RESOURCETYPE_URI = "pathmap://PCM_MODELS/Palladio.resourcetype";

	/**
	 * The bidirectional Map between {@link ResourceDemandType} and
	 * {@link ProcessinResourceType}.
	 */
	private final BidiMap<ResourceDemandType, PcmTypeIdEqualsWrapper> beagleTypeToPcmType = new DualHashBidiMap<>();

	/**
	 * {@code true} if {@link ResourceTypeMappings} is initialised.
	 */
	private boolean inited;

	/**
	 * Creates a mapping for ({@link ResourceDemandType#RESOURCE_TYPE_CPU_NS},{@code CPU})
	 * and ({@link ResourceDemandType#RESOURCE_TYPE_HDD_NS}, {@code HDD}).
	 *
	 */
	public void initialise() {
		if (!this.inited) {

			final Resource.Factory.Registry registry = Resource.Factory.Registry.INSTANCE;
			final Map<String, Object> factoryMap = registry.getExtensionToFactoryMap();
			factoryMap.put("resourcetype", new XMIResourceFactoryImpl());

			final URI resourceTypeUri = URI.createURI(RESOURCETYPE_URI);

			final ResourceSet resSet = new ResourceSetImpl();
			final Resource resource = resSet.getResource(resourceTypeUri, true);

			final ResourceRepository resourceRepository = (ResourceRepository) resource.getContents().get(0);
			for (final ResourceType resourceType : resourceRepository.getAvailableResourceTypes_ResourceRepository()) {
				switch (resourceType.getEntityName()) {
					case "CPU":
						this.beagleTypeToPcmType.put(ResourceDemandType.RESOURCE_TYPE_CPU_NS,
							new PcmTypeIdEqualsWrapper((ProcessingResourceType) resourceType));
						break;
					case "HDD":
						this.beagleTypeToPcmType.put(ResourceDemandType.RESOURCE_TYPE_HDD_NS,
							new PcmTypeIdEqualsWrapper((ProcessingResourceType) resourceType));
						break;
					default:
				}
			}

			this.inited = true;
		}

	}

	/**
	 * Reading from the bidirectional map.
	 *
	 * @param processingResourceType The {@link ProcessingResourceType}
	 * @return The linked {@link ResourceDemandType}
	 */
	public ResourceDemandType getBeagleType(final ProcessingResourceType processingResourceType) {
		Validate.validState(this.inited);
		return this.beagleTypeToPcmType.getKey(new PcmTypeIdEqualsWrapper(processingResourceType));
	}

	/**
	 * Reading from the bidirectional map.
	 *
	 * @param resourceDemandType The {@link ResourceDemandType}
	 * @return The linked {@link ProcessingResourceType}
	 */
	public ProcessingResourceType getPcmType(final ResourceDemandType resourceDemandType) {
		Validate.validState(this.inited);
		return this.beagleTypeToPcmType.get(resourceDemandType).pcmType;
	}

	/**
	 * Wraps an {@link ProcessingResourceType} to override its
	 * {@link Object#equals(Object)} and {@link Object#hashCode()} to be based on the
	 * resource type’s id. This allows them to be inserted into a {@link HashMap} and be
	 * retrieved as expected.
	 *
	 * @author Joshua Gleitze
	 */
	private final class PcmTypeIdEqualsWrapper {

		/**
		 * The wrapped resource type.
		 */
		private final ProcessingResourceType pcmType;

		/**
		 * Creates a wrapper for the given {@code type}.
		 *
		 * @param type The resource type to wrap.
		 */
		private PcmTypeIdEqualsWrapper(final ProcessingResourceType type) {
			this.pcmType = type;
		}

		@Override
		public boolean equals(final Object otherObject) {
			if (otherObject == this) {
				return true;
			}
			if (otherObject == null) {
				return false;
			}
			if (otherObject.getClass() != this.getClass()) {
				return false;
			}
			final PcmTypeIdEqualsWrapper other = (PcmTypeIdEqualsWrapper) otherObject;
			return this.pcmType.getId().equals(other.pcmType.getId());
		}

		@Override
		public int hashCode() {
			return this.pcmType.getId().hashCode();
		}
	}
}
