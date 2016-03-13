package de.uka.ipd.sdq.beagle.gui;

import de.uka.ipd.sdq.beagle.core.failurehandling.FailureHandler;
import de.uka.ipd.sdq.beagle.core.failurehandling.FailureReport;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.internal.ui.DebugPluginImages;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.debug.ui.IDebugUIConstants;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import java.util.ArrayList;
import java.util.List;

/**
 * A tab of Beagle's launch configuration for setting up the launch configurations to run
 * for the analysis.
 *
 * @author Roman Langrehr
 */
@SuppressWarnings("restriction")
public class LaunchConfigurationTab extends AbstractLaunchConfigurationTab {

	/**
	 * The key in the Beagle Launch Configuration determining the name of the
	 * {@link IJavaProject} to analyse.
	 */
	public static final String BEAGLE_LAUNCH_CONFIGURATION_LAUNCHCONFIGURATION =
		"de.uka.ipd.sdq.beagle.LAUNCHCONFIGURATION";

	/**
	 * The number of columns of the layout of container which contains the entire content
	 * of this tab.
	 */
	private static final int MAIN_LAYOUT_NR_COLUMS = 1;

	/**
	 * The title of this tab.
	 */
	private static final String TITLE = "Launch Configurations";

	/**
	 * The value of
	 * {@code org.eclipse.jdt.internal.junit.launcher.JUnitLaunchConfiguration.ID_JUNIT_APPLICATION}
	 * .
	 */
	private static final String JUNIT_LAUNCH_CONFIGURATION_ID = "org.eclipse.jdt.junit.launchconfig";

	/**
	 * The width of {@link #tableViewer}.
	 */
	private static final int LAUNCH_CONFIGURATION_TABLE_VIEW_WIDTH = 697;

	/**
	 * The height of {@link #tableViewer}.
	 */
	private static final int LAUNCH_CONFIGURATION_TABLE_VIEW_HEIGHT = 509;

	/**
	 * The main container for this page.
	 */
	private Composite mainContainer;

	/**
	 * The {@link TableViewer} showing the {@linkplain ILaunchConfiguration
	 * ILaunchConfigurations}.
	 */
	private CheckboxTableViewer tableViewer;

	/**
	 * The available {@linkplain ILaunchConfiguration ILaunchConfigurations}.
	 */
	private List<String> launchConfigurations = new ArrayList<>();

	/**
	 * An (maybe older) version of {@link #launchConfigurations}. May only be updated by
	 * {@link #updateTableViewer()}.
	 */
	private List<String> oldLaunchConfigurations = new ArrayList<>();

	/**
	 * Checkbox where the user can select, whether to show all
	 * {@linkplain ILaunchConfiguration ILaunchConfigurations} or only the one for the
	 * project under analysis.
	 */
	private Button hideLaunchConfigurationsForOtherProjectsCheckbox;

	/**
	 * The name of the project under analysis.
	 */
	private String projectName;

	@Override
	public void createControl(final Composite parent) {
		this.mainContainer = new Composite(parent, SWT.NONE);
		final GridLayout layoutAll = new GridLayout();
		this.mainContainer.setLayout(layoutAll);
		layoutAll.numColumns = MAIN_LAYOUT_NR_COLUMS;

		this.hideLaunchConfigurationsForOtherProjectsCheckbox = new Button(this.mainContainer, SWT.CHECK);
		this.hideLaunchConfigurationsForOtherProjectsCheckbox
			.setText("Hide launch configurations of other projects (than the one to analyse).");
		this.hideLaunchConfigurationsForOtherProjectsCheckbox.setSelection(true);
		this.hideLaunchConfigurationsForOtherProjectsCheckbox.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(final SelectionEvent event) {
				if (LaunchConfigurationTab.this.hideLaunchConfigurationsForOtherProjectsCheckbox.getSelection()) {
					LaunchConfigurationTab.this.showProjectLaunchConfigurations();
				} else {
					LaunchConfigurationTab.this.showAllLaunchConfigurations();
				}
			}

			@Override
			public void widgetDefaultSelected(final SelectionEvent event) {
			}
		});

		this.tableViewer = CheckboxTableViewer.newCheckList(this.mainContainer, SWT.BORDER | SWT.V_SCROLL | SWT.SINGLE);
		final GridData tableViewerGridData = new GridData();
		tableViewerGridData.horizontalAlignment = SWT.FILL;
		tableViewerGridData.verticalAlignment = SWT.FILL;
		tableViewerGridData.heightHint = LAUNCH_CONFIGURATION_TABLE_VIEW_HEIGHT;
		tableViewerGridData.widthHint = LAUNCH_CONFIGURATION_TABLE_VIEW_WIDTH;
		this.tableViewer.addCheckStateListener(event -> LaunchConfigurationTab.this.updateLaunchConfigurationDialog());
		this.tableViewer.getTable().setLayoutData(tableViewerGridData);

		this.updateTableViewer();

		// required to avoid an error in the system
		this.setControl(this.mainContainer);
	}

	/**
	 * Updates the content of {@link #tableViewer} according to
	 * {@link #launchConfigurations}.
	 */
	private void updateTableViewer() {
		final Object[] oldSelection = this.tableViewer.getCheckedElements();
		this.tableViewer.remove(this.oldLaunchConfigurations.toArray());
		this.tableViewer.add(this.launchConfigurations.toArray());
		this.tableViewer.setCheckedElements(oldSelection);
		this.oldLaunchConfigurations = this.launchConfigurations;
	}

	/**
	 * Makes the {@link #tableViewer} show the {@linkplain ILaunchConfiguration
	 * ILaunchConfigurations} for the project under analysis.
	 */
	private void showProjectLaunchConfigurations() {
		this.launchConfigurations = ILaunchConfigurationHelper.toNameStrings(ILaunchConfigurationHelper
			.filterByProject(ILaunchConfigurationHelper.getAllLaunchConfigurationsInWorkspace(), this.projectName));

		this.updateTableViewer();
	}

	/**
	 * Makes the {@link #tableViewer} show the {@linkplain ILaunchConfiguration
	 * ILaunchConfigurations} for all projects.
	 */
	private void showAllLaunchConfigurations() {
		this.launchConfigurations = ILaunchConfigurationHelper
			.toNameStrings(ILaunchConfigurationHelper.getAllLaunchConfigurationsInWorkspace());

		this.updateTableViewer();
	}

	@Override
	public void setDefaults(final ILaunchConfigurationWorkingCopy configuration) {
		try {
			configuration.setAttribute(BEAGLE_LAUNCH_CONFIGURATION_LAUNCHCONFIGURATION,
				ILaunchConfigurationHelper.toNameStrings(ILaunchConfigurationHelper.filterByProject(
					ILaunchConfigurationHelper.filterByType(
						ILaunchConfigurationHelper.getAllLaunchConfigurationsInWorkspace(),
						JUNIT_LAUNCH_CONFIGURATION_ID),
					configuration.getAttribute(ProjectTab.BEAGLE_LAUNCH_CONFIGURATION_IJAVAPROJECT, ""))));
		} catch (final CoreException coreException) {
			FailureHandler.getHandler(this.getClass()).handle(new FailureReport<>().cause(coreException));
		}
	}

	@Override
	public void initializeFrom(final ILaunchConfiguration configuration) {
		try {
			this.projectName = configuration.getAttribute(ProjectTab.BEAGLE_LAUNCH_CONFIGURATION_IJAVAPROJECT, "");
			this.showProjectLaunchConfigurations();

			final List<String> selection =
				configuration.getAttribute(BEAGLE_LAUNCH_CONFIGURATION_LAUNCHCONFIGURATION, new ArrayList<>());
			this.tableViewer.setCheckedElements(selection.toArray());
		} catch (final CoreException coreException) {
			FailureHandler.getHandler(this.getClass()).handle(new FailureReport<>().cause(coreException));
		}
	}

	@Override
	public void performApply(final ILaunchConfigurationWorkingCopy configuration) {
		final Object[] selection = this.tableViewer.getCheckedElements();
		final List<String> result = new ArrayList<>();
		for (final Object selected : selection) {
			result.add((String) selected);
		}
		configuration.setAttribute(BEAGLE_LAUNCH_CONFIGURATION_LAUNCHCONFIGURATION, result);
	}

	@Override
	public String getName() {
		return TITLE;
	}

	@Override
	public Image getImage() {
		return DebugPluginImages.getImage(IDebugUIConstants.IMG_OBJS_LAUNCH_RUN);
	}

	@Override
	public boolean isValid(final ILaunchConfiguration launchConfig) {
		final LaunchChecker checker = new LaunchChecker(launchConfig);
		checker.checkForLaunchConfigurationError();
		this.setErrorMessage(checker.getErrorMessage());
		return !checker.hasError();
	}
}
