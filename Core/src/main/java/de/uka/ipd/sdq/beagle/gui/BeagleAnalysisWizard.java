package de.uka.ipd.sdq.beagle.gui;

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
	 * The {@link EclipseWizardPage}s of this {@link BeagleAnalysisWizard}.
	 */
	private EclipseWizardPage[] eclipseWizardPages;
}
