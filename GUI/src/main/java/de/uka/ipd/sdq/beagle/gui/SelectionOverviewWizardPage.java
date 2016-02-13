package de.uka.ipd.sdq.beagle.gui;

import de.uka.ipd.sdq.beagle.core.facade.BeagleConfiguration;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.palladiosimulator.pcm.core.entity.Entity;

import java.util.LinkedList;

/*
 * This class is involved in creating a Graphical User Interface. Its funtionality cannot
 * reasonably be tested by automated unit tests.
 *
 * COVERAGE:OFF
 */

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
	 * The {@link BeagleConfiguration} this {@link SelectionOverviewWizardPage} uses.
	 */
	private final BeagleConfiguration beagleConfiguration;

	/**
	 * The main container.
	 */
	private Composite mainContainer;

	/**
	 * Constructs a new {@link SelectionOverviewWizardPage} being linked to the given
	 * {@code beagleConfiguration}.
	 *
	 * @param beagleConfiguration The {@link BeagleConfiguration} this
	 *            {@link SelectionOverviewWizardPage} will be permanently linked to.
	 *            Changing the associated {@link BeagleConfiguration} is not possible.
	 */
	public SelectionOverviewWizardPage(final BeagleConfiguration beagleConfiguration) {
		super(TITLE);
		this.setTitle(TITLE);
		this.setDescription(DESCRIPTION);
		this.beagleConfiguration = beagleConfiguration;
	}

	@Override
	public void createControl(final Composite parent) {
		this.mainContainer = new Composite(parent, SWT.NONE);
		final GridLayout layout = new GridLayout();
		this.mainContainer.setLayout(layout);
		layout.numColumns = MAIN_LAYOUT_NR_COLUMS;

		final Label labelHeader = new Label(this.mainContainer, SWT.NONE);
		labelHeader.setText("These elements are selected for analysis:");

		final LinkedList<String> elements = new LinkedList<String>();

		if (this.beagleConfiguration.getElements().size() != 0) {
			for (final Entity element : this.beagleConfiguration.getElements()) {
				elements.add(element.eClass().getName() + ": " + element.getEntityName());
			}
		} else {
			elements.add("Repository: " + this.beagleConfiguration.getRepositoryFile().getAbsolutePath());
		}

		for (final String element : elements) {
			final Label labelItem = new Label(this.mainContainer, SWT.NONE);
			labelItem.setText("    • " + element);
		}

		// required to avoid an error in the system
		this.setControl(this.mainContainer);
		this.setPageComplete(true);
	}
}
