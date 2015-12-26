
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Shell;

/**
 * The class setting up and controlling the wizard.
 * 
 * @author Christoph Michelbach
 *
 */
public class BeagleAnalysisWizard extends Wizard {
	/**
	 * A page of this wizard.
	 */
	protected SelectionOverview selectionOverview;
	/**
	 * A page of this wizard.
	 */
	protected Timeout timeout;

	/**
	 * The constructor of this class.
	 */
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
		this.selectionOverview = new SelectionOverview();
		this.timeout = new Timeout();

		addPage(this.selectionOverview);
		addPage(this.timeout);
	}

	@Override

	public boolean performFinish() {
		// open dialog
		// TODO use timeout.getTimeout() when launching the analysis
		final MessageDialog dialog = new MessageDialog(new Shell(), "Beagle Analysis is Running", null,
				"Beagle Analysis is running.", MessageDialog.INFORMATION, new String[] { "Abort", "Pause" }, 0);
		final int buttonClick = dialog.open();

		if (buttonClick == 0) {
			System.out.println("User clicked 'Abort'.");
		}

		if (buttonClick == 1) {
			System.out.println("User clicked 'Pause'.");
		}

		return true;
	}

}
