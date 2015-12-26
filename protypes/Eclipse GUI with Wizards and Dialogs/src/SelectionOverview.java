import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * Page 1 of the wizard.
 * 
 * @author Christoph Michelbach
 *
 */
public class SelectionOverview extends WizardPage {
	/**
	 * The title of this page.
	 */
	private static String title = "Selection Overview";

	/**
	 * The description of this page.
	 */
	private static String description = "Make sure you want to anaylse the components listed below.";

	/**
	 * The main container.
	 */
	private Composite mainContainer;

	/**
	 * Constructor setting the tile and the description of this page.
	 */
	public SelectionOverview() {
		super(title);
		setTitle(title);
		setDescription(description);
	}

	@Override
	public void createControl(final Composite parent) {
		this.mainContainer = new Composite(parent, SWT.NONE);
		final GridLayout layout = new GridLayout();
		this.mainContainer.setLayout(layout);
		layout.numColumns = 1;

		final Label labelHead = new Label(this.mainContainer, SWT.NONE);
		labelHead.setText("These components are selected for anaylsis:");

		final Label label1 = new Label(this.mainContainer, SWT.NONE);
		label1.setText("    • Some Component");
		final Label label2 = new Label(this.mainContainer, SWT.NONE);
		label2.setText("    • Some Different Component");
		final Label label3 = new Label(this.mainContainer, SWT.NONE);
		label3.setText("    • And Another One");

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
		setControl(this.mainContainer);
		setPageComplete(true);
	}
}
