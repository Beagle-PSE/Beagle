package de.uka.ipd.sdq.beagle.gui;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * The class setting up and controlling the wizard allowing the user to configure Beagle’s
 * behaviour during the analysis.
 * 
 * @author Christoph Michelbach
 */
public class BeagleAnalysisWizard extends Wizard {

	/**
	 * The {@link UserConfiguration} this {@link BeagleAnalysisWizard} and therefore
	 * everything linked to it uses.
	 */
	private final UserConfiguration userConfiguration;

	/**
	 * The {@link ActionListener} which will be executed when the wizard finishes.
	 */
	private ActionListener wizardFinished;

	/**
	 * Constructs a new {@link BeagleAnalysisWizard} which runs {@code wizardFinished}
	 * when it finishes.
	 * 
	 * @param userConfiguration The {@link UserConfiguration} this
	 *            {@link BeagleAnalysisWizard} and therefore everything linked to it will
	 *            use.
	 * @param wizardFinished The {@link ActionListener} which will be executed when the
	 *            wizard finishes.
	 */
	public BeagleAnalysisWizard(final UserConfiguration userConfiguration, final ActionListener wizardFinished) {
		super();
		this.userConfiguration = userConfiguration;
		this.wizardFinished = wizardFinished;
		setNeedsProgressMonitor(true);
	}

	@Override
	public String getWindowTitle() {
		return "Beagle Analysis";
	}

	@Override
	public void addPages() {
		final List<WizardPage> wizardPages = new LinkedList<WizardPage>();
		wizardPages.add(new SelectionOverviewWizardPage(this.userConfiguration));
		wizardPages.add(new TimeoutWizardPage(this.userConfiguration));
		final Iterator<WizardPage> iterator = wizardPages.iterator();

		while (iterator.hasNext()) {
			addPage(iterator.next());
		}
	}

	@Override
	public boolean performFinish() {
		this.wizardFinished.actionPerformed(new ActionEvent(this, 0, "wizard finished"));
		return true;
	}
}
