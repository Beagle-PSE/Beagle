package de.uka.ipd.sdq.beagle.gui;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.palladiosimulator.pcm.core.entity.Entity;

import java.util.ArrayList;
import java.util.LinkedList;

/*
 * This class is involved in creating a Graphical User Interface. Its functionality cannot
 * reasonably be tested by automated unit tests.
 *
 * COVERAGE:OFF
 */

/**
 * A page of {@link BeagleAnalysisWizard} allowing the user to choose which of the
 * components they selected will be be analysed. Leaving out some components is possible,
 * adding new ones isn't.
 *
 * @author Christoph Michelbach
 * @author Roman Langrehr
 */
public class SelectionOverviewTab extends AbstractLaunchConfigurationTab {

	/**
	 * The key in the Beagle Launch Configuration determining whether the user wants to
	 * analyse the whole repository or selected some elements.
	 *
	 * <p>Permitted values are:
	 *
	 * <ul>
	 *
	 * <li> {@link #BEAGLE_LAUNCH_CONFIGURATION_SELECTION_TYPE_VALUE_WHOLE_REPOSITORY}
	 *
	 * <li>{@link #BEAGLE_LAUNCH_CONFIGURATION_SELECTION_TYPE_VALUE_CUSTOM_REPOSITORY}
	 *
	 * </ul>
	 *
	 */
	private static final String BEAGLE_LAUNCH_CONFIGURATION_SELECTION_TYPE = "SELECTION_TYPE";

	/**
	 * This value in Beagle's launch configuration for the key
	 * {@link #BEAGLE_LAUNCH_CONFIGURATION_SELECTION_TYPE} specifies that the whole
	 * repository should be analysed.
	 */
	private static final String BEAGLE_LAUNCH_CONFIGURATION_SELECTION_TYPE_VALUE_WHOLE_REPOSITORY = "WHOLE_REPOSITORY";

	/**
	 * This value in Beagle's launch configuration for the key
	 * {@link #BEAGLE_LAUNCH_CONFIGURATION_SELECTION_TYPE} specifies that some elements
	 * were selected for the analysis.
	 */
	private static final String BEAGLE_LAUNCH_CONFIGURATION_SELECTION_TYPE_VALUE_CUSTOM_REPOSITORY = "CUSTOM_SELECTION";

	/**
	 * The key in the Beagle Launch Configuration for the List of elements the user
	 * selected.
	 */
	private static final String BEAGLE_LAUNCH_CONFIGURATION_CUSTOM_SELECTION = "THE_CUSTOM_SELECTION";

	/**
	 * The title of this page.
	 */
	private static final String TITLE = "Selection Overview";

	/**
	 * The description of this page.
	 */
	private static final String DESCRIPTION = "Make sure you want to analyse the elements listed below.";

	/**
	 * The number of columns of the layout of container which contains the entire content
	 * of this page.
	 */
	private static final int MAIN_LAYOUT_NR_COLUMS = 1;

	/**
	 * The main container.
	 */
	private Composite mainContainer;

	@Override
	public void createControl(final Composite parent) {
		this.mainContainer = new Composite(parent, SWT.NONE);
		final GridLayout layout = new GridLayout();
		this.mainContainer.setLayout(layout);
		layout.numColumns = MAIN_LAYOUT_NR_COLUMS;

		final Label labelHeader = new Label(this.mainContainer, SWT.NONE);
		labelHeader.setText("These elements are selected for analysis:");

		final LinkedList<String> elements = new LinkedList<String>();

		if (this.beagleConfiguration.getElements() != null) {
			for (final Entity element : this.beagleConfiguration.getElements()) {
				elements.add(element.eClass().getName() + ": " + element.getEntityName());
			}
		} else {
			elements.add("Repository: " + this.beagleConfiguration.getRepositoryFile().getAbsolutePath());
		}

		for (final String element : elements) {
			final Label labelItem = new Label(this.mainContainer, SWT.NONE);
			labelItem.setText("    • " + element);
		}

		// required to avoid an error in the system
		this.setControl(this.mainContainer);
	}

	@Override
	public void setDefaults(final ILaunchConfigurationWorkingCopy configuration) {
		configuration.setAttribute(BEAGLE_LAUNCH_CONFIGURATION_SELECTION_TYPE,
			BEAGLE_LAUNCH_CONFIGURATION_SELECTION_TYPE_VALUE_WHOLE_REPOSITORY);
		configuration.setAttribute(BEAGLE_LAUNCH_CONFIGURATION_CUSTOM_SELECTION, new ArrayList<String>());
	}

	@Override
	public void initializeFrom(final ILaunchConfiguration configuration) {
		// TODO Auto-generated method stub

	}

	@Override
	public void performApply(final ILaunchConfigurationWorkingCopy configuration) {
		// The user can't set up anything in this wizard page (yet).
	}

	@Override
	public String getName() {
		return TITLE;
	}
}
