package de.uka.ipd.sdq.beagle.prototypes.extensionpoint;

import de.uka.sdq.beagle.measurement.MeasurementTool;

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
 */
public class ShowMeasurementToolsHandler extends AbstractHandler {

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final List<MeasurementTool> measurementTools =
			MeasurementToolContributionsHandler.getAvailableMeasurmentTools();
		String measurementToolsAsString = "";
		for (final MeasurementTool measurementTool : measurementTools) {
			measurementToolsAsString += " - " + measurementTool.getClass().getPackage().getName() + "."
				+ measurementTool.getClass().getName() + "\n";
		}
		final IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		MessageDialog.openInformation(window.getShell(), "Beagle is alive!",
			"Available measurment tools:\n" + measurementToolsAsString);
		return null;
	}
}
