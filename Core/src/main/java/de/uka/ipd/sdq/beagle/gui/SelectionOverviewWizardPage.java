package de.uka.ipd.sdq.beagle.gui;

import org.eclipse.jface.wizard.WizardPage;

/**
 * A page of {@link BeagleAnalysisWizard} allowing the user to choose which of the
 * components they selected will be be analysed. Leaving out some components is possible,
 * adding new ones isn't.
 *
 * @author Christoph Michelbach
 */
public class SelectionOverviewWizardPage extends WizardPage {

	/**
	 * The {@link UserConfiguration} associated with this {@link WizardPage}.
	 */
	@SuppressWarnings("unused")
	private UserConfiguration userConfiguration;

	/**
	 * Constructs a new {@link SelectionOverviewWizardPage} being linked to the given
	 * {@code userConfiguration}.
	 *
	 * @param userConfiguration The {@link UserConfiguration} this
	 *            {@link SelectionOverviewWizardPage} will be permanently linked to.
	 *            Changing the associated {@link UserConfiguration} is not possible.
	 */
	public SelectionOverviewWizardPage(final UserConfiguration userConfiguration) {

	}
}
