package de.uka.ipd.sdq.beagle.gui;

import de.uka.ipd.sdq.beagle.core.failurehandling.FailureHandler;
import de.uka.ipd.sdq.beagle.core.failurehandling.FailureReport;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/*
 * This class is involved in creating a Graphical User Interface. Its functionality cannot
 * reasonably be tested by automated unit tests.
 *
 * COVERAGE:OFF
 */

/**
 * A tab of Beagle's launch configuration allowing the user to see which of the components
 * they selected will be be analysed.
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
	public static final String BEAGLE_LAUNCH_CONFIGURATION_SELECTION_TYPE = "de.uka.ipd.sdq.beagle.SELECTION_TYPE";

	/**
	 * This value in Beagle's launch configuration for the key
	 * {@link #BEAGLE_LAUNCH_CONFIGURATION_SELECTION_TYPE} specifies that the whole
	 * repository should be analysed.
	 */
	public static final String BEAGLE_LAUNCH_CONFIGURATION_SELECTION_TYPE_VALUE_WHOLE_REPOSITORY = "WHOLE_REPOSITORY";

	/**
	 * This value in Beagle's launch configuration for the key
	 * {@link #BEAGLE_LAUNCH_CONFIGURATION_SELECTION_TYPE} specifies that some elements
	 * were selected for the analysis.
	 */
	public static final String BEAGLE_LAUNCH_CONFIGURATION_SELECTION_TYPE_VALUE_CUSTOM_REPOSITORY = "CUSTOM_SELECTION";

	/**
	 * The default value in Beagle's launch configuration for the key
	 * {@link #BEAGLE_LAUNCH_CONFIGURATION_SELECTION_TYPE}.
	 */
	public static final String BEAGLE_LAUNCH_CONFIGURATION_SELECTION_TYPE_DEFAULT_VALUE =
		BEAGLE_LAUNCH_CONFIGURATION_SELECTION_TYPE_VALUE_WHOLE_REPOSITORY;

	/**
	 * The key in the Beagle Launch Configuration for the List of elements the user
	 * selected.
	 */
	public static final String BEAGLE_LAUNCH_CONFIGURATION_CUSTOM_SELECTION =
		"de.uka.ipd.sdq.beagle.THE_CUSTOM_SELECTION";

	/**
	 * The default value in Beagle's launch configuration for the key
	 * {@link #BEAGLE_LAUNCH_CONFIGURATION_CUSTOM_SELECTION}.
	 */
	public static final List<String> BEAGLE_LAUNCH_CONFIGURATION_CUSTOM_SELECTION_DEFAULT_VALUE = new ArrayList<>();

	/**
	 * The title of this tab.
	 */
	private static final String TITLE = "Selection Overview";

	/**
	 * The number of columns of the layout of container which contains the entire content
	 * of this tab.
	 */
	private static final int MAIN_LAYOUT_NR_COLUMS = 1;

	/**
	 * The main container.
	 */
	private Composite mainContainer;

	/**
	 * The type of the selection.
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
	 */
	private String selectionType;

	/**
	 * The ids of the elements to analyse.
	 */
	private List<String> identifiers;

	@Override
	public void createControl(final Composite parent) {
		this.mainContainer = new Composite(parent, SWT.NONE);
		final GridLayout layout = new GridLayout();
		this.mainContainer.setLayout(layout);
		layout.numColumns = MAIN_LAYOUT_NR_COLUMS;

		this.setControl(this.mainContainer);
	}

	/**
	 * Re-creates the elements showing the ids.
	 *
	 * <p>Should be called each time {@link #identifiers} changes.
	 */
	private void updateElements() {
		for (final Control control : this.mainContainer.getChildren()) {
			control.dispose();
		}
		final Label labelHeader = new Label(this.mainContainer, SWT.NONE);
		labelHeader.setText("These elements are selected for analysis:");

		final LinkedList<String> elements = new LinkedList<String>();

		if (this.selectionType.equals(BEAGLE_LAUNCH_CONFIGURATION_SELECTION_TYPE_VALUE_CUSTOM_REPOSITORY)) {
			for (final String identifier : this.identifiers) {
				elements.add(identifier);
			}
		} else if (this.selectionType.equals(BEAGLE_LAUNCH_CONFIGURATION_SELECTION_TYPE_VALUE_WHOLE_REPOSITORY)) {
			elements.add("Whole Repository");
		}

		for (final String element : elements) {
			final Label labelItem = new Label(this.mainContainer, SWT.NONE);
			labelItem.setText("    • " + element);
		}
	}

	@Override
	public void setDefaults(final ILaunchConfigurationWorkingCopy configuration) {
		configuration.setAttribute(BEAGLE_LAUNCH_CONFIGURATION_SELECTION_TYPE,
			BEAGLE_LAUNCH_CONFIGURATION_SELECTION_TYPE_DEFAULT_VALUE);
		configuration.setAttribute(BEAGLE_LAUNCH_CONFIGURATION_CUSTOM_SELECTION,
			BEAGLE_LAUNCH_CONFIGURATION_CUSTOM_SELECTION_DEFAULT_VALUE);
	}

	@Override
	public void initializeFrom(final ILaunchConfiguration configuration) {
		try {
			this.selectionType = configuration.getAttribute(BEAGLE_LAUNCH_CONFIGURATION_SELECTION_TYPE,
				BEAGLE_LAUNCH_CONFIGURATION_SELECTION_TYPE_DEFAULT_VALUE);
			this.identifiers = configuration.getAttribute(BEAGLE_LAUNCH_CONFIGURATION_CUSTOM_SELECTION,
				BEAGLE_LAUNCH_CONFIGURATION_CUSTOM_SELECTION_DEFAULT_VALUE);
		} catch (final CoreException coreException) {
			FailureHandler.getHandler(this.getClass())
				.handle(new FailureReport<>().cause(coreException).retryWith(() -> this.initializeFrom(configuration)));
		}
		this.updateElements();
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
