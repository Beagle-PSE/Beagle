package de.uka.ipd.sdq.beagle.extensionpointprototype;

import de.uka.sdq.beagle.measurement.MeasurementTool;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.e4.core.di.annotations.Execute;

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
	private static final String MEASUREMT_TOOL_EXTENSION_POINT_ID = "de.uka.ipd.sdg.beagle.measurmenttool";

	/**
	 * Method, that is executed by eclipse with all available extensions.
	 *
	 * @param registry
	 *            the extension registry.
	 */
	@Execute
	public void execute(final IExtensionRegistry registry) {
		final List<MeasurementTool> measurementTools = new ArrayList<>();
		final IConfigurationElement[] config = registry.getConfigurationElementsFor(MEASUREMT_TOOL_EXTENSION_POINT_ID);
		try {
			for (final IConfigurationElement element : config) {
				final Object object = element.createExecutableExtension("class");
				if (object instanceof MeasurementTool) {
					measurementTools.add((MeasurementTool) object);
				}
			}
		} catch (final CoreException exception) {
			throw new RuntimeException(exception);
		}
	}
}
