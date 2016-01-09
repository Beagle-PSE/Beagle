package de.uka.ipd.sdq.beagle.gui.wizardpages;

import de.uka.ipd.sdq.beagle.gui.BeagleAnalysisWizard;
import de.uka.ipd.sdq.beagle.gui.UserConfiguration;
import de.uka.ipd.sdq.beagle.gui.WizardPage;

/**
 * A page of {@link BeagleAnalysisWizard} allowing the user to configure whether they want
 * to perform the analysis on the machine they're at or on a machine connected via a
 * network.
 * 
 * @author Christoph Michelbach
 */
public class MachineSelectionWizardPage extends WizardPage {

	/**
	 * Constructs a new {@link MachineSelectionWizardPage} being linked to the given
	 * {@code userConfiguration}.
	 *
	 * @param userConfiguration The {@link UserConfiguration} this
	 *            {@link MachineSelectionWizardPage} will be permanently linked to.
	 *            Changing the associated {@link UserConfiguratien} is not possible.
	 */
	public MachineSelectionWizardPage(final UserConfiguration userConfiguration) {

	}
}
