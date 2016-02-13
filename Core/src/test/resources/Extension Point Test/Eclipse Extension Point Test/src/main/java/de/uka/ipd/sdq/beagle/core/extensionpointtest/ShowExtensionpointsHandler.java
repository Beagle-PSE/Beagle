package de.uka.ipd.sdq.beagle.core.extensionpointtest;

import de.uka.ipd.sdq.beagle.core.analysis.MeasurementResultAnalyser;
import de.uka.ipd.sdq.beagle.core.analysis.MeasurementResultAnalyserContributionsHandler;
import de.uka.ipd.sdq.beagle.core.analysis.ProposedExpressionAnalyser;
import de.uka.ipd.sdq.beagle.core.analysis.ProposedExpressionAnalyserContributionsHandler;
import de.uka.ipd.sdq.beagle.core.measurement.MeasurementTool;
import de.uka.ipd.sdq.beagle.core.measurement.MeasurementToolContributionsHandler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import java.util.List;

/**
 * Handles the menu entry "show available measurement tools".
 *
 * @author Roman Langrehr
 * @author Michael Vogt
 */
public class ShowExtensionpointsHandler extends AbstractHandler {

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final List<MeasurementTool> measurementTools =
			new MeasurementToolContributionsHandler().getAvailableMeasurmentTools();
		final List<MeasurementResultAnalyser> measurementResultAnalysers =
			new MeasurementResultAnalyserContributionsHandler().getAvailableMeasurmentResultAnalysers();
		final List<ProposedExpressionAnalyser> proposedExpressionAnalysers =
			new ProposedExpressionAnalyserContributionsHandler().getAvailableProposedExpressionAnalysers();
		String measurementToolsAsString = "";
		for (final MeasurementTool measurementTool : measurementTools) {
			measurementToolsAsString += " - " + measurementTool.getClass().getPackage().getName() + "."
				+ measurementTool.getClass().getName() + "\n";
		}
		String measurementResultAnalysersAsString = "";
		for (final MeasurementResultAnalyser measurementResultAnalyser : measurementResultAnalysers) {
			measurementResultAnalysersAsString += " - " + measurementResultAnalyser.getClass().getPackage().getName()
				+ "." + measurementResultAnalyser.getClass().getName() + "\n";
		}
		String proposedExpressionAnalysersAsString = "";
		for (final ProposedExpressionAnalyser proposedExpressionAnalyser : proposedExpressionAnalysers) {
			proposedExpressionAnalysersAsString += " - " + proposedExpressionAnalyser.getClass().getPackage().getName()
				+ "." + proposedExpressionAnalyser.getClass().getName() + "\n";
		}
		final IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		MessageDialog.openInformation(window.getShell(), "Available Tools",
			"Available measurment tools:\n" + measurementToolsAsString + "Available measurment result analyser:\n"
				+ measurementResultAnalysersAsString + "Available proposed expression analyser:\n"
				+ proposedExpressionAnalysersAsString);
		return null;
	}
}
