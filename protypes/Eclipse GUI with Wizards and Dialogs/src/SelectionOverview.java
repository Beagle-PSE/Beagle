import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class SelectionOverview extends WizardPage {

	private Text text1;

	private Composite container;

	public SelectionOverview() {

		super("Selection Overview");

		setTitle("Selection Overview");

		setDescription("Make sure you want to anaylse the components listed below.");

	}

	@Override

	public void createControl(Composite parent) {

		container = new Composite(parent, SWT.NONE);

		GridLayout layout = new GridLayout();

		container.setLayout(layout);

		layout.numColumns = 2;

		Label label1 = new Label(container, SWT.NONE);

		label1.setText(
				"<html>These components are selected for anaylsis:<br>    • Some component<br>    • Some other component</html>");

		/*
		 * text1 = new Text(container, SWT.BORDER | SWT.SINGLE);
		 * 
		 * text1.setText("");
		 * 
		 * text1.addKeyListener(new KeyListener() {
		 * 
		 * @Override
		 * 
		 * public void keyPressed(KeyEvent e) {
		 * 
		 * }
		 * 
		 * @Override
		 * 
		 * public void keyReleased(KeyEvent e) {
		 * 
		 * if (!text1.getText().isEmpty()) {
		 * 
		 * setPageComplete(true);
		 * 
		 * }
		 * 
		 * }
		 * 
		 * });
		 * 
		 * GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		 * 
		 * text1.setLayoutData(gd);
		 */

		// required to avoid an error in the system

		setControl(container);

		setPageComplete(true);

	}

	public String getText1() {

		return text1.getText();

	}

}
