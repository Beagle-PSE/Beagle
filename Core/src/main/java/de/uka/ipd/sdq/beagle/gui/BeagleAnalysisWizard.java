package de.uka.ipd.sdq.beagle.gui;

import org.eclipse.jface.wizard.WizardPage;

/**
 * Sets up and controls the graphical wizard allowing the user to configure Beagle’s
 * behaviour during the analysis and afterwards starts the analysis.
 * 
 * @author Christoph Michelbach
 */
public class BeagleAnalysisWizard {

	/**
	 * The user configuration associated with this {@link BeagleAnalysisWizard}.
	 */
	private UserConfiguration userConfiguration;

	/**
	 * The {@link WizardPage}s of this {@link BeagleAnalysisWizard}.
	 */
	private WizardPage[] wizardPages;
}
