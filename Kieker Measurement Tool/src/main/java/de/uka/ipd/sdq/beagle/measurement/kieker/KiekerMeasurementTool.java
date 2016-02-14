package de.uka.ipd.sdq.beagle.measurement.kieker;

import de.uka.ipd.sdq.beagle.core.LaunchConfiguration;
import de.uka.ipd.sdq.beagle.core.failurehandling.FailureHandler;
import de.uka.ipd.sdq.beagle.core.failurehandling.FailureReport;
import de.uka.ipd.sdq.beagle.core.measurement.MeasurementTool;
import de.uka.ipd.sdq.beagle.core.measurement.order.MeasurementEvent;
import de.uka.ipd.sdq.beagle.core.measurement.order.MeasurementOrder;
import de.uka.ipd.sdq.beagle.measurement.kieker.instrumentation.EclipseAstInstrumentor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A measurement tool instrumenting the project’s source code to be measured by <a
 * href="http://kieker-monitoring.net/">Kieker</a>.
 *
 * @author Joshua Gleitze
 */
public class KiekerMeasurementTool implements MeasurementTool {

	/**
	 * The JVM property key to set Kieker’s configuration file.
	 */
	private static final String KIEKER_CONFIGURATION_FILE_PROPERTY = "kieker.monitoring.configuration";

	/**
	 * The JVM property key to set Kieker’s output folder.
	 */
	private static final String KIEKER_OUTPUT_FOLDER_PROPERTY =
		"kieker.monitoring.writer.filesystem.AsyncFsWriter.customStoragePath";

	/**
	 * The handler of failures.
	 */
	private static final FailureHandler FAILURE_HANDLER = FailureHandler.getHandler("Kieker Measurement Tool");

	@Override
	public List<MeasurementEvent> measure(final MeasurementOrder measurementOrder) {
		try {
			this.doMeasure(measurementOrder);
		} catch (final IOException readWriteError) {
			final FailureReport<List<MeasurementEvent>> failure =
				new FailureReport<List<MeasurementEvent>>().cause(readWriteError)
					.continueWith(() -> new ArrayList<>())
					.retryWith(() -> this.measure(measurementOrder));
			return FAILURE_HANDLER.handle(failure);
		}
		return null;
	}

	/**
	 * Delegation method from {@link #measure(MeasurementOrder)}. Performs the actual
	 * work.
	 *
	 * @param measurementOrder The measurement instructions.
	 * @return The captured measurement events.
	 * @throws IOException If accessing or writing the files needed to measure fails.
	 * @see #measure(MeasurementOrder)
	 */
	private List<MeasurementEvent> doMeasure(final MeasurementOrder measurementOrder) throws IOException {
		final MeasurementFileManager fileManager = new MeasurementFileManager();

		new EclipseAstInstrumentor(fileManager::getInstrumentationFileFor)
			.useCharset(measurementOrder.getProjectInformation().getCharset())
			.useStrategy(new ResourceDemandInstrumentationStrategy(), measurementOrder.getResourceDemandSections())
			.instrument();

		new EclipseCompiler(fileManager.getInstrumentedSourceCodeFolder())
			.useClassPath(measurementOrder.getProjectInformation().getBuildPath())
			.useClassPath(fileManager.getMeasurementRemotePackage().toString())
			.useCharset(measurementOrder.getProjectInformation().getCharset())
			.intoFolder(fileManager.getCompiledByteCodeFolder())
			.compile();

		for (final LaunchConfiguration launch : measurementOrder.getProjectInformation().getLaunchConfigurations()) {
			launch.prependClasspath(fileManager.getKiekerJar().toString())
				.prependClasspath(fileManager.getMeasurementRemotePackage().toString())
				.prependClasspath(fileManager.getCompiledByteCodeFolder().toString())
				.appendJvmArgument(jvmArg(KIEKER_CONFIGURATION_FILE_PROPERTY, fileManager.getKiekerConfigurationFile()))
				.appendJvmArgument(jvmArg(KIEKER_OUTPUT_FOLDER_PROPERTY, fileManager.getKiekerResultsFolder()))
				.execute();
		}

		return new ArrayList<>();
	}

	/**
	 * Creates a custom ({@code -D}) JVM property string.
	 *
	 * @param propertyName The property’s key.
	 * @param propertyValue The property’s value.
	 * @return The property string.
	 */
	private static String jvmArg(final String propertyName, final Object propertyValue) {
		return String.format("-D%s=\"%s\"", propertyName, propertyValue);
	}

}
