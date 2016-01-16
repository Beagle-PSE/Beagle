package de.uka.ipd.sdq.beagle.core.measurement;

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
	private static final String MEASUREMENT_TOOL_EXTENSION_POINT_ID = "de.uka.ipd.sdg.beagle.measurementtool";

	/**
	 * The property name of the extension point for the measurement tools for the
	 * {@link MeasurementTool} implementations.
	 */
	private static final String MEASUREMENT_TOOL_EXTENSION_POINT_CLASS_PROPERTY_NAME = "MeasurementToolClass";

	/**
	 * Scans the measurement tool extension point for available measurement tools.
	 *
	 * @return All available measurement tools.
	 * @throws RuntimeException If an instance of an measurement tool could not be created
	 *             for any reason, e.g. the measurement tool had no public zero argument
	 *             constructor or if an {@code MeasurmentToolClass} provided via the
	 *             extension point was not implementing {@link MeasurementTool}.
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
		final IConfigurationElement[] config =
			registry.getConfigurationElementsFor(MEASUREMENT_TOOL_EXTENSION_POINT_ID);
		try {
			for (final IConfigurationElement element : config) {
				final Object object =
					element.createExecutableExtension(MEASUREMENT_TOOL_EXTENSION_POINT_CLASS_PROPERTY_NAME);
				if (object instanceof MeasurementTool) {
					measurementTools.add((MeasurementTool) object);
				} else {
					throw new RuntimeException(
						"A class provided via the measurement tool extension point was not implementing the"
							+ "interface \"Measurement Tool\": " + object.getClass().getName() + " in package "
							+ object.getClass().getPackage().getName());
				}
			}
		} catch (final CoreException exception) {
			throw new RuntimeException(exception);
		}
		return measurementTools;
	}
}
