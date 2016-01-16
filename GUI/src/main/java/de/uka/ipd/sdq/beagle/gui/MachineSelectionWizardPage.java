package de.uka.ipd.sdq.beagle.gui;

import org.eclipse.jface.wizard.WizardPage;

/**
 * A page of {@link BeagleAnalysisWizard} allowing the user to configure whether they want
 * to perform the analysis on the machine they're at or on a machine connected via a
 * network.
 *
 * @author Christoph Michelbach
 */
public class MachineSelectionWizardPage extends WizardPage {

	/**
	 * The {@link UserConfiguration} associated with this {@link WizardPage}.
	 */
	@SuppressWarnings("unused")
	private UserConfiguration userConfiguration;

	/**
	 * Constructs a new {@link MachineSelectionWizardPage} being linked to the given
	 * {@code userConfiguration}.
	 *
	 * @param userConfiguration The {@link UserConfiguration} this
	 *            {@link MachineSelectionWizardPage} will be permanently linked to.
	 *            Changing the associated {@link UserConfiguration} is not possible.
	 */
	public MachineSelectionWizardPage(final UserConfiguration userConfiguration) {

	}
}
