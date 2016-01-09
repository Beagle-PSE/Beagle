package de.uka.ipd.sdq.beagle.gui.wizardpages;

import de.uka.ipd.sdq.beagle.gui.BeagleAnalysisWizard;
import de.uka.ipd.sdq.beagle.gui.UserConfiguration;
import de.uka.ipd.sdq.beagle.gui.EclipseWizardPage;

/**
 * A page of {@link BeagleAnalysisWizard} allowing the user to choose which of the
 * components they selected will be be analysed. Leaving out some components is possible,
 * adding new ones isn't.
 * 
 * @author Christoph Michelbach
 */
public class SelectionOverviewWizardPage extends EclipseWizardPage {

	/**
	 * The {@link UserConfiguration} associated with this {@link EclipseWizardPage}.
	 */
	private UserConfiguration userConfiguration;

	/**
	 * Constructs a new {@link SelectionOverviewWizardPage} being linked to the given
	 * {@code userConfiguration}.
	 *
	 * @param userConfiguration The {@link UserConfiguration} this
	 *            {@link SelectionOverviewWizardPage} will be permanently linked to.
	 *            Changing the associated {@link UserConfiguratien} is not possible.
	 */
	public SelectionOverviewWizardPage(final UserConfiguration userConfiguration) {

	}
}
