import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

/**
 * The main method launching this prototype.
 * 
 * @author Christoph Michelbach
 *
 */
public class Main {
	/**
	 * Private constructor preventing instantiation.
	 */
	private Main() {
	}

	/**
	 * The main method launching this prototype.
	 * 
	 * @param args
	 *            Parameters will not be considered.
	 */
	public static void main(final String[] args) {
		final Shell shell = new Shell();
		final Wizard myWizard = new BeagleAnalysisWizard();
		final WizardDialog wizardDialog = new WizardDialog(shell, myWizard);
		System.out.println(wizardDialog.open());
	}
}
