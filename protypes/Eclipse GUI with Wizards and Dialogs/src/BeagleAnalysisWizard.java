
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Shell;

public class BeagleAnalysisWizard extends Wizard {
	protected SelectionOverview selectionOverview;
	protected Timeout timeout;

	public BeagleAnalysisWizard() {
		super();
		setNeedsProgressMonitor(true);
	}

	@Override
	public String getWindowTitle() {
		return "Beagle Analysis";
	}

	@Override
	public void addPages() {
		selectionOverview = new SelectionOverview();
		timeout = new Timeout();

		addPage(selectionOverview);
		addPage(timeout);
	}

	@Override

	public boolean performFinish() {
		// open dialog
		// TODO use timeout.getTimeout() when launching the analysis
		MessageDialog dialog = new MessageDialog(new Shell(), "Beagle Analysis", null, "Beagle Analysis is running.",
				MessageDialog.INFORMATION, new String[] { "Abort", "Pause" }, 0);
		int buttonClick = dialog.open();

		if (buttonClick == 0) {
			System.out.println("User clicked 'Abort'.");
		}

		if (buttonClick == 1) {
			System.out.println("User clicked 'Pause'.");
		}

		return true;
	}

}
