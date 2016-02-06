package de.uka.ipd.sdq.beagle.core.analysis;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

import java.util.ArrayList;
import java.util.List;

/*
 * This class cannot reasonably be tested through unit tests. It uses an API that’s
 * offered through a static method of a final, byte-code signed class. Automatically
 * testing the class would require inadequate effort and would likely mainly test the
 * Eclipse API. This class is tested manually.
 *
 * COVERAGE:OFF
 */

/**
 * Handles the creation searching and loading of all available measurement result
 * analysers. They are loaded through Eclipse’s extension point API. The measurement
 * result analysers need a public zero argument constructor for the instantiation.
 *
 * @author Roman Langrehr
 */
public class MeasurementResultAnalyserContributionsHandler {

	/**
	 * The extension point id for {@linkplain MeasurementResultAnalyser
	 * MeasurementResultAnalysers}.
	 */
	public static final String MEASUREMENT_RESULT_ANALYSERS_EXTENSION_POINT_ID =
		"de.uka.ipd.sdq.beagle.measurementresultanalyser";

	/**
	 * The property name of the extension point for {@linkplain MeasurementResultAnalyser}
	 * implementations.
	 */
	public static final String MEASUREMENT_RESULT_ANALYSERS_EXTENSION_POINT_CLASS_PROPERTY_NAME =
		"MeasurementResultAnalyserClass";

	/**
	 * Scans the measurement result analysers extension point for available measurement
	 * result analysers.
	 *
	 * @return All available measurement result analysers. Each call returns new instances
	 *         of the measurement result analysers.
	 * @throws RuntimeException If an instance of an measurement result analyser could not
	 *             be created for any reason, e.g. the measurement result analyser had no
	 *             public zero argument constructor or if an
	 *             {@code MeasurmentResultAnalyserClass} provided via the extension point
	 *             was not implementing {@link MeasurementResultAnalyser}.
	 */
	public List<MeasurementResultAnalyser> getAvailableMeasurmentResultAnalysers() {
		final IExtensionRegistry registry = Platform.getExtensionRegistry();
		final List<MeasurementResultAnalyser> measurementResultAnalysers = new ArrayList<>();
		final IConfigurationElement[] config =
			registry.getConfigurationElementsFor(MEASUREMENT_RESULT_ANALYSERS_EXTENSION_POINT_ID);
		try {
			for (final IConfigurationElement element : config) {
				final Object object =
					element.createExecutableExtension(MEASUREMENT_RESULT_ANALYSERS_EXTENSION_POINT_CLASS_PROPERTY_NAME);
				if (object instanceof MeasurementResultAnalyser) {
					measurementResultAnalysers.add((MeasurementResultAnalyser) object);
				} else {
					throw new RuntimeException(
						"A class provided via the measurement result analyser extension point was not implementing the"
							+ "interface \"MeasurementResultAnalyser\": " + object.getClass().getName() + " in package "
							+ object.getClass().getPackage().getName());
				}
			}
		} catch (final CoreException exception) {
			throw new RuntimeException(exception);
		}
		return measurementResultAnalysers;
	}
}
