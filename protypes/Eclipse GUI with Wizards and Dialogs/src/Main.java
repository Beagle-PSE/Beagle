import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

public class Main {

	public static void main(String[] args) {
		Shell shell = new Shell();
		Wizard myWizard = new MyWizard();
		WizardDialog wizardDialog = new WizardDialog(shell, myWizard);
		System.out.println(wizardDialog.open());
	}
}
