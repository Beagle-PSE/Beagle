package de.uka.ipd.sdq.beagle.gui;

import de.uka.ipd.sdq.beagle.core.facade.BeagleConfiguration;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/*
 * This class is involved in creating a Graphical User Interface. Its funtionality cannot
 * reasonably be tested by automated unit tests.
 *
 * COVERAGE:OFF
 */

/**
 * Sets up and controls the wizard allowing the user to configure Beagle’s behaviour
 * during the analysis.
 *
 * @author Christoph Michelbach
 */
public class BeagleAnalysisWizard extends Wizard {

	/**
	 * The {@link BeagleConfiguration} this {@link BeagleAnalysisWizard} and therefore
	 * everything linked to it uses.
	 */
	private final BeagleConfiguration beagleConfiguration;

	/**
	 * The {@link ActionListener} which will be executed when the wizard finishes.
	 */
	private final ActionListener wizardFinished;

	/**
	 * Constructs a new {@link BeagleAnalysisWizard} which runs {@code wizardFinished}
	 * when it finishes.
	 *
	 * @param beagleConfiguration The {@link BeagleConfiguration} this
	 *            {@link BeagleAnalysisWizard} and therefore everything linked to it will
	 *            use.
	 * @param wizardFinished The {@link ActionListener} which will be executed when the
	 *            wizard finishes.
	 */
	public BeagleAnalysisWizard(final BeagleConfiguration beagleConfiguration, final ActionListener wizardFinished) {
		super();
		this.beagleConfiguration = beagleConfiguration;
		this.wizardFinished = wizardFinished;
		this.setNeedsProgressMonitor(true);
	}

	@Override
	public String getWindowTitle() {
		return "Beagle Analysis";
	}

	@Override
	public void addPages() {
		final List<WizardPage> wizardPages = new LinkedList<WizardPage>();
		wizardPages.add(new SelectionOverviewWizardPage(this.beagleConfiguration));
		wizardPages.add(new TimeoutWizardPage(this.beagleConfiguration));
		final Iterator<WizardPage> iterator = wizardPages.iterator();

		while (iterator.hasNext()) {
			this.addPage(iterator.next());
		}
	}

	@Override
	public boolean performFinish() {
		this.wizardFinished.actionPerformed(new ActionEvent(this, 0, "wizard finished"));
		return true;
	}
}
