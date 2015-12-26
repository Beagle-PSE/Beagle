package de.uka.ipd.sdq.beagle.extensionpointprototype;

import de.uka.sdq.beagle.measurement.MeasurementTool;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import java.util.List;

public class ShowMeasurmentToolsHandler extends AbstractHandler {

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final List<MeasurementTool> measurementTools = new MeasurementToolContributionsHandler()
				.getAvailableMeasurmentTools();
		String measurmentToolsAsString = "";
		for (final MeasurementTool measurmentTool : measurementTools) {
			measurmentToolsAsString += " - " + measurmentTool.getClass().getPackage() + "."
					+ measurmentTool.getClass().getName() + "\n";
		}
		final IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		MessageDialog.openInformation(window.getShell(), "Beagle is alive!",
				"Available measurment tools:\n" + measurmentToolsAsString);
		return null;
	}
}
