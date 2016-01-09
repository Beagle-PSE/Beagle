package de.uka.ipd.sdq.beagle.gui;

import org.eclipse.jface.wizard.WizardPage;

/**
 * A page of {@link BeagleAnalysisWizard} allowing the user to choose between an adaptive
 * timeout, a constant timeout, or no timeout at all.
 * 
 * @author Christoph Michelbach
 */
public class TimeoutWizardPage extends WizardPage {

	/**
	 * The {@link UserConfiguration} associated with this {@link WizardPage}.
	 */
	private UserConfiguration userConfiguration;

	/**
	 * Constructs a new {@link TimeoutWizardPage} being linked to the given
	 * {@code userConfiguration}.
	 *
	 * @param userConfiguration The {@link UserConfiguration} this
	 *            {@link TimeoutWizardPage} will be permanently linked to. Changing the
	 *            associated {@link UserConfiguration} is not possible.
	 */
	public TimeoutWizardPage(final UserConfiguration userConfiguration) {

	}
}
