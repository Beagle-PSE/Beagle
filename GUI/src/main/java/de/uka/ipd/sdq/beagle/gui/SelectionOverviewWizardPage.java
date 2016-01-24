package de.uka.ipd.sdq.beagle.gui;

import de.uka.ipd.sdq.beagle.core.UserConfiguration;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import java.util.Iterator;

/**
 * A page of {@link BeagleAnalysisWizard} allowing the user to choose which of the
 * components they selected will be be analysed. Leaving out some components is possible,
 * adding new ones isn't.
 * 
 * @author Christoph Michelbach
 */
public class SelectionOverviewWizardPage extends WizardPage {

	/**
	 * The title of this page.
	 */
	private static final String TITLE = "Selection Overview";

	/**
	 * The description of this page.
	 */
	private static final String DESCRIPTION = "Make sure you want to analyse the elements listed below.";

	/**
	 * The number of columns of the layout of container which contains the entire content
	 * of this page.
	 */
	private static final int MAIN_LAYOUT_NR_COLUMS = 1;

	/**
	 * The {@link UserConfiguration} this {@link SelectionOverviewWizardPage} uses.
	 */
	private final UserConfiguration userConfiguration;

	/**
	 * The main container.
	 */
	private Composite mainContainer;

	/**
	 * Constructs a new {@link SelectionOverviewWizardPage} being linked to the given
	 * {@code userConfiguration}.
	 *
	 * @param userConfiguration The {@link UserConfiguration} this
	 *            {@link SelectionOverviewWizardPage} will be permanently linked to.
	 *            Changing the associated {@link UserConfiguration} is not possible.
	 */
	public SelectionOverviewWizardPage(final UserConfiguration userConfiguration) {
		super(TITLE);
		setTitle(TITLE);
		setDescription(DESCRIPTION);
		this.userConfiguration = userConfiguration;
	}

	@Override
	public void createControl(final Composite parent) {
		this.mainContainer = new Composite(parent, SWT.NONE);
		final GridLayout layout = new GridLayout();
		this.mainContainer.setLayout(layout);
		layout.numColumns = MAIN_LAYOUT_NR_COLUMS;

		final Label labelHeader = new Label(this.mainContainer, SWT.NONE);
		labelHeader.setText("These elements are selected for analysis:");

		final Iterator<String> iterator = this.userConfiguration.getElements().iterator();
		while (iterator.hasNext()) {
			final String component = iterator.next();

			final Label labelItem = new Label(this.mainContainer, SWT.NONE);
			labelItem.setText("    • Component: " + component);
		}

		// required to avoid an error in the system
		setControl(this.mainContainer);
		setPageComplete(true);
	}
}
