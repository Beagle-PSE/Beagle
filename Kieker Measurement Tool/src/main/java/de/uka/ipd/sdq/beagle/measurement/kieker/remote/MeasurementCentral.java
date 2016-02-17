package de.uka.ipd.sdq.beagle.measurement.kieker.remote;

import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;

/**
 * The central measurement instance instrumented statements report to.
 *
 * @author Joshua Gleitze
 *
 */
public final class MeasurementCentral {

	/**
	 * The singleton instance.
	 */
	public static final MeasurementCentral INSTANCE = new MeasurementCentral();

	/**
	 * Singleton instance of the monitoring controller.
	 */
	private static final IMonitoringController MONITORING_CONTROLLER = MonitoringController.getInstance();

	/**
	 * The start time of the last reported resource demanding code section.
	 */
	private static long rdiaStart;

	/**
	 * The id of the last reported resource demanding code section.
	 */
	private static int rdiaId = -1;

	/**
	 * This is a singleton that must be obtained through {@link #INSTANCE}.
	 */
	private MeasurementCentral() {
	}

	/**
	 * Reports that the resource demanding code section identified by {@code rdiaId} is
	 * about to be entered.
	 *
	 * @param demandId Identifier of the entered resource demanding code section.
	 */
	public static void startResourceDemand(final int demandId) {
		assert rdiaId == -1;
		rdiaId = demandId;
		rdiaStart = MONITORING_CONTROLLER.getTimeSource().getTime();
	}

	/**
	 * Reports that the last started resource demanding code section has just been left.
	 */
	public static void stopResourceDemand() {
		final long stopTime = MONITORING_CONTROLLER.getTimeSource().getTime();
		final OperationExecutionRecord record = new OperationExecutionRecord(
			OperationExecutionRecord.NO_OPERATION_SIGNATURE, OperationExecutionRecord.NO_SESSION_ID, rdiaId, rdiaStart,
			stopTime, OperationExecutionRecord.NO_HOSTNAME, OperationExecutionRecord.NO_EOI_ESS,
			OperationExecutionRecord.NO_EOI_ESS);
		MONITORING_CONTROLLER.newMonitoringRecord(record);
		rdiaId = -1;
	}
}
