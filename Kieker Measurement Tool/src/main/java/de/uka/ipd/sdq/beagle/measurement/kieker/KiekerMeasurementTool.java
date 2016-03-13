package de.uka.ipd.sdq.beagle.measurement.kieker;

import de.uka.ipd.sdq.beagle.core.CodeSection;
import de.uka.ipd.sdq.beagle.core.LaunchConfiguration;
import de.uka.ipd.sdq.beagle.core.measurement.MeasurementTool;
import de.uka.ipd.sdq.beagle.core.measurement.order.MeasurementEvent;
import de.uka.ipd.sdq.beagle.core.measurement.order.MeasurementOrder;
import de.uka.ipd.sdq.beagle.measurement.kieker.instrumentation.EclipseAstInstrumentor;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
	private static final String KIEKER_CONFIGURATION_FILE_ARG = "kieker.monitoring.configuration";

	/**
	 * The JVM property key to set Kieker’s output folder.
	 */
	private static final String KIEKER_OUTPUT_FOLDER_ARG =
		"kieker.monitoring.writer.filesystem.SyncFsWriter.customStoragePath";

	/**
	 * Manages all files needed for the measurement.
	 */
	private final MeasurementFileManager fileManager = new MeasurementFileManager();

	/**
	 * Identifies resource demand code sections.
	 */
	private final CodeSectionIdentifier resourceDemandIdentifer = new CodeSectionIdentifier();

	/**
	 * Contains the order that is to be measured.
	 */
	private MeasurementOrder measurementOrder;

	/**
	 * Whether we have successfully instrumented the source code for
	 * {@link #measurementOrder} yet.
	 */
	private boolean instrumented;

	/**
	 * The set of launch configurations not yet executed.
	 */
	private Set<LaunchConfiguration> unlaunchedConfigurations;

	@Override
	public List<MeasurementEvent> measure(final MeasurementOrder newMeasurementOrder) {
		if (!this.instrumented || !newMeasurementOrder.equals(this.measurementOrder)) {
			// we have not yet instrumented this order
			this.measurementOrder = newMeasurementOrder;
			this.instrumented = false;
			this.unlaunchedConfigurations = newMeasurementOrder.getProjectInformation().getLaunchConfigurations();
			this.fileManager.allocate();

			this.instrument();

			this.instrumented = true;
		}

		try {
			this.executeMeasurements();
		} catch (final InterruptedException interrupt) {
			// If interrupted, we safe what we have and return.
		}

		final List<MeasurementEvent> resultEvents =
			new KiekerMeasurementResultProcessor(this.fileManager.getKiekerResultsFolder())
				.useResourceDemandIdentifier(this.resourceDemandIdentifer).process();
		this.fileManager.moveKiekerResultsToDone();

		return resultEvents;
	}

	/**
	 * Instruments the source code, preparing everything to execute the measured software.
	 *
	 * @see #measure(MeasurementOrder)
	 */
	private void instrument() {
		final Set<CodeSection> resourceDemandSections = this.measurementOrder.getResourceDemandSections();

		new EclipseAstInstrumentor(this.fileManager::getInstrumentationFileFor)
			.useCharset(this.measurementOrder.getProjectInformation().getCharset())
			.useStrategy(new ResourceDemandInstrumentationStrategy(this.resourceDemandIdentifer),
				resourceDemandSections)
			.instrument();

		this.fileManager.copyRemoteMeasurementByteCodeToInstrumentedByteCode();

		new EclipseCompiler(this.fileManager.getInstrumentedSourceCodeFolder())
			.useClassPath(this.measurementOrder.getProjectInformation().getBuildPath())
			.useClassPath(this.fileManager.getCompiledByteCodeFolder().toString())
			.useCharset(this.measurementOrder.getProjectInformation().getCharset())
			.intoFolder(this.fileManager.getCompiledByteCodeFolder())
			.compile();
	}

	/**
	 * Runs the measured software, thus producing Kieker measurement results.
	 *
	 * @throws InterruptedException If the current thread is interrupted while executing
	 *             the measured software.
	 */
	private void executeMeasurements() throws InterruptedException {
		for (final Iterator<LaunchConfiguration> configurations =
			this.unlaunchedConfigurations.iterator(); configurations.hasNext();) {
			configurations.next()
				.prependClasspath(this.fileManager.getKiekerJar().toString())
				.prependClasspath(this.fileManager.getCompiledByteCodeFolder().toString())
				.appendJvmArgument(jvmArg(KIEKER_CONFIGURATION_FILE_ARG, this.fileManager.getKiekerConfigurationFile()))
				.appendJvmArgument(jvmArg(KIEKER_OUTPUT_FOLDER_ARG, this.fileManager.getKiekerResultsFolder()))
				.execute();
			configurations.remove();
		}
		assert this.unlaunchedConfigurations.size() == 0;
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
