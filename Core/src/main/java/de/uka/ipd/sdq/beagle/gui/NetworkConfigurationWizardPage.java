package de.uka.ipd.sdq.beagle.gui;

/**
 * A page of {@link BeagleAnalysisWizard} allowing the user to configure the network if
 * they previously chose to perform the analysis on a machine connected over a network.
 * 
 * @author Christoph Michelbach
 */
public class NetworkConfigurationWizardPage extends EclipseWizardPage {

	/**
	 * The {@link UserConfiguration} associated with this {@link EclipseWizardPage}.
	 */
	private UserConfiguration userConfiguration;

	/**
	 * Constructs a new {@link NetworkConfigurationWizardPage} being linked to the given
	 * {@code userConfiguration}.
	 *
	 * @param userConfiguration The {@link UserConfiguration} this
	 *            {@link NetworkConfigurationWizardPage} will be permanently linked to.
	 *            Changing the associated {@link UserConfiguration} is not possible.
	 */
	public NetworkConfigurationWizardPage(final UserConfiguration userConfiguration) {

	}
}
