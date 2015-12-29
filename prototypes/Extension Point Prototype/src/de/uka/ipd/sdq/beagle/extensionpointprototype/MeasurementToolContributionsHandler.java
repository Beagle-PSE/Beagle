package de.uka.ipd.sdq.beagle.extensionpointprototype;

import de.uka.sdq.beagle.measurement.MeasurementTool;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

import java.util.ArrayList;
import java.util.List;

/**
 * This class handles the creation searching and loading of all available measurement
 * tools.
 *
 * @author Roman Langrehr
 */
public class MeasurementToolContributionsHandler {

	/**
	 * The extension point id for the measurement tools.
	 */
	private static final String MEASUREMENT_TOOL_EXTENSION_POINT_ID = "de.uka.ipd.sdg.beagle.measurmenttool";

	/**
	 * The property name of the extension point for the measurement tools for the
	 * {@link MeasurementTool} implementations.
	 */
	private static final String MEASUREMENT_TOOL_EXTENSION_POINT_CLASS_PROPERTY_NAME = "MeasurmentToolClass";

	/**
	 * Scans the measurement tool extension point for available measurement tools.
	 *
	 * @return All available measurement tools.
	 */
	public List<MeasurementTool> getAvailableMeasurmentTools() {
		final IExtensionRegistry registry = Platform.getExtensionRegistry();
		// final IExtensionPoint point =
		// registry.getExtensionPoint(MEASUREMT_TOOL_EXTENSION_POINT_ID);
		// if (point == null) {
		// return null;
		// }
		// final IExtension[] extensions = point.getExtensions();
		final List<MeasurementTool> measurementTools = new ArrayList<>();
		final IConfigurationElement[] config = registry.getConfigurationElementsFor(MEASUREMENT_TOOL_EXTENSION_POINT_ID);
		try {
			for (final IConfigurationElement element : config) {
				final Object object = element
						.createExecutableExtension(MEASUREMENT_TOOL_EXTENSION_POINT_CLASS_PROPERTY_NAME);
				if (object instanceof MeasurementTool) {
					measurementTools.add((MeasurementTool) object);
				}
			}
		} catch (final CoreException exception) {
			throw new RuntimeException(exception);
		}
		return measurementTools;
	}
}
