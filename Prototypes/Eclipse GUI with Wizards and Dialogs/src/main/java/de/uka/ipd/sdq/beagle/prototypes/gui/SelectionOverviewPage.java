package de.uka.ipd.sdq.beagle.prototypes.gui;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * Page 1 of the wizard.
 * 
 * @author Christoph Michelbach
 */
public class SelectionOverviewPage extends WizardPage {

	/**
	 * The title of this page.
	 */
	private static final String TITLE = "Selection Overview";

	/**
	 * The description of this page.
	 */
	private static final String DESCRIPTION = "Make sure you want to anaylse the components listed below.";

	/**
	 * The number of columns of the layout of container which contains the entire content
	 * of this page.
	 */
	private static final int MAIN_LAYOUT_NR_COLUMS = 1;

	/**
	 * The main container.
	 */
	private Composite mainContainer;

	/**
	 * Constructor setting the tile and the description of this page.
	 */
	public SelectionOverviewPage() {
		super(TITLE);
		setTitle(TITLE);
		setDescription(DESCRIPTION);
	}

	@Override
	public void createControl(final Composite parent) {
		this.mainContainer = new Composite(parent, SWT.NONE);
		final GridLayout layout = new GridLayout();
		this.mainContainer.setLayout(layout);
		layout.numColumns = MAIN_LAYOUT_NR_COLUMS;

		final Label labelHead = new Label(this.mainContainer, SWT.NONE);
		labelHead.setText("These components are selected for anaylsis:");

		final Label label1 = new Label(this.mainContainer, SWT.NONE);
		label1.setText("    • Some Component");
		final Label label2 = new Label(this.mainContainer, SWT.NONE);
		label2.setText("    • Some Different Component");
		final Label label3 = new Label(this.mainContainer, SWT.NONE);
		label3.setText("    • And Another One");

		// required to avoid an error in the system
		setControl(this.mainContainer);
		setPageComplete(true);
	}
}
