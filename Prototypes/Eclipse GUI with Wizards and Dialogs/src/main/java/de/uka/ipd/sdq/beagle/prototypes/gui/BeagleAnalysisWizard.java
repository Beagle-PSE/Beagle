package de.uka.ipd.sdq.beagle.prototypes.gui;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Shell;

/**
 * The class setting up and controlling the wizard allowing the user to configure Beagle’s
 * behaviour during the analysis and afterwards starting the analysis.
 * 
 * @author Christoph Michelbach
 */
public class BeagleAnalysisWizard extends Wizard {

	/**
	 * A page of this wizard.
	 */
	private SelectionOverviewPage selectionOverviewPage;

	/**
	 * A page of this wizard.
	 */
	private TimeoutPage timeoutPage;

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
		this.selectionOverviewPage = new SelectionOverviewPage();
		this.timeoutPage = new TimeoutPage();

		addPage(this.selectionOverviewPage);
		addPage(this.timeoutPage);
	}

	@Override
	public boolean performFinish() {
		// open dialog
		final String dialogTitle = "Beagle Analysis is Running";
		final String dialogMessage = "Beagle Analysis is running.";
		final String[] buttonLabels = {"Abort", "Pause"};
		final MessageDialog dialog = new MessageDialog(new Shell(), dialogTitle, null, dialogMessage,
			MessageDialog.INFORMATION, buttonLabels, 0);
		final int buttonClick = dialog.open();

		if (buttonClick == 0) {
			this.doSomething();
		}

		if (buttonClick == 1) {
			this.doSomething();
		}

		return true;
	}

	/**
	 * Method for demonstrative purpose.
	 *
	 */
	private void doSomething() {

	}
}
