package de.uka.ipd.sdq.beagle.gui;

import de.uka.ipd.sdq.beagle.core.failurehandling.FailureHandler;
import de.uka.ipd.sdq.beagle.core.failurehandling.FailureReport;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.internal.ui.SWTFactory;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.debug.ui.JDIDebugUIPlugin;
import org.eclipse.jdt.internal.debug.ui.launcher.LauncherMessages;
import org.eclipse.jdt.ui.JavaElementLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;

/**
 * A tab of Beagle's launch configuration for setting up the project to analyse.
 *
 * @author Roman Langrehr
 */
@SuppressWarnings("restriction")
public class ProjectTab extends AbstractLaunchConfigurationTab {

	/**
	 * The key in the Beagle Launch Configuration determining the name of the
	 * {@link IJavaProject} to analyse.
	 */
	public static final String BEAGLE_LAUNCH_CONFIGURATION_IJAVAPROJECT = "de.uka.ipd.sdq.beagle.IJAVAPROJECT";

	/**
	 * The key in the Beagle Launch Configuration determining the path of the repository
	 * file to analyse.
	 */
	public static final String BEAGLE_LAUNCH_CONFIGURATION_REPOSITORY_FILE = "de.uka.ipd.sdq.beagle.REPOSITORY_FILE";

	/**
	 * The default value for the key {@link #BEAGLE_LAUNCH_CONFIGURATION_REPOSITORY_FILE}.
	 */
	public static final String BEAGLE_LAUNCH_CONFIGURATION_REPOSITORY_FILE_DEFAULT_VALUE =
		"model/internal_architecture_model.repository";

	/**
	 * The title of this tab.
	 */
	private static final String TITLE = "Project";

	/**
	 * The text box containing the project name.
	 */
	private Text fProjText;

	/**
	 * The text box containing the repository file path.
	 */
	private Text repositoryText;

	/**
	 * The select Project Button.
	 */
	private Button fProjButton;

	@Override
	public void setDefaults(final ILaunchConfigurationWorkingCopy configuration) {
		// There are no useful defaults for the project.
		configuration.setAttribute(BEAGLE_LAUNCH_CONFIGURATION_REPOSITORY_FILE,
			BEAGLE_LAUNCH_CONFIGURATION_REPOSITORY_FILE_DEFAULT_VALUE);
	}

	@Override
	public void initializeFrom(final ILaunchConfiguration configuration) {
		try {
			this.fProjText.setText(configuration.getAttribute(BEAGLE_LAUNCH_CONFIGURATION_IJAVAPROJECT, ""));
			this.repositoryText.setText(configuration.getAttribute(BEAGLE_LAUNCH_CONFIGURATION_REPOSITORY_FILE,
				BEAGLE_LAUNCH_CONFIGURATION_REPOSITORY_FILE_DEFAULT_VALUE));
		} catch (final CoreException coreException) {
			FailureHandler.getHandler(this.getClass())
				.handle(new FailureReport<>().cause(coreException).retryWith(() -> this.initializeFrom(configuration)));
		}
	}

	@Override
	public void performApply(final ILaunchConfigurationWorkingCopy configuration) {
		final IJavaProject selectedJavaProject = this.getJavaProject();
		configuration.setAttribute(BEAGLE_LAUNCH_CONFIGURATION_IJAVAPROJECT,
			selectedJavaProject == null ? "" : this.getJavaProject().getProject().getName());
		configuration.setAttribute(BEAGLE_LAUNCH_CONFIGURATION_REPOSITORY_FILE, this.repositoryText.getText());
	}

	@Override
	public String getName() {
		return TITLE;
	}

	/**
	 * Creates the widgets for the repository file selection.
	 *
	 * @param parent The parent for the widget.
	 */
	protected void createRepositoryFileEditor(final Composite parent) {
		final Font font = parent.getFont();
		final Group group = new Group(parent, SWT.NONE);
		group.setText("Repository File:");
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		group.setLayoutData(gridData);
		final GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		group.setLayout(layout);
		group.setFont(font);
		this.repositoryText = new Text(group, SWT.SINGLE | SWT.BORDER);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		this.repositoryText.setLayoutData(gridData);
		this.repositoryText.setFont(font);
	}

	/*
	 * Disclaimer:
	 *
	 * Parts of the following code in this class has been copied from
	 * org.eclipse.jdt.internal.debug.ui.launcher.AbstractJavaMainTab and
	 * org.eclipse.jdt.debug.ui.launchConfigurations.AppletMainTab and slightly adapted.
	 *
	 * As those classes contained a lot of stuff, we don't need/want to have here, there
	 * was no way to properly use the existing code. But as we wanted to have the
	 * "Select Project" button exactly the same way, we copied the code for this elements.
	 */

	@Override
	public void createControl(final Composite parent) {
		final Composite projComp = SWTFactory.createComposite(parent, parent.getFont(), 1, 1, GridData.FILL_BOTH);
		((GridLayout) projComp.getLayout()).verticalSpacing = 0;
		this.createProjectEditor(projComp);
		this.createRepositoryFileEditor(projComp);

		this.createVerticalSpacer(projComp, 1);
		this.setControl(projComp);
	}

	/**
	 * Creates the widgets for the project selection button.
	 *
	 * @param parent The parent for the widget.
	 */
	protected void createProjectEditor(final Composite parent) {
		final Font font = parent.getFont();
		final Group group = new Group(parent, SWT.NONE);
		group.setText(LauncherMessages.AbstractJavaMainTab_0);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		group.setLayoutData(gridData);
		final GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		group.setLayout(layout);
		group.setFont(font);
		this.fProjText = new Text(group, SWT.SINGLE | SWT.BORDER);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		this.fProjText.setLayoutData(gridData);
		this.fProjText.setFont(font);
		final WidgetListener fListener = new WidgetListener();
		this.fProjText.addModifyListener(fListener);
		this.fProjButton = this.createPushButton(group, LauncherMessages.AbstractJavaMainTab_1, null);
		this.fProjButton.addSelectionListener(fListener);
	}

	/**
	 * Handles the clicks on the project button.
	 */
	private void handleProjectButtonSelected() {
		final IJavaProject project = this.chooseJavaProject();
		if (project == null) {
			return;
		}
		final String projectName = project.getElementName();
		ProjectTab.this.fProjText.setText(projectName);
	}

	/**
	 * Shows a dialog to choose the {@link IJavaProject}.
	 *
	 * @return the chosen {@link IJavaProject}
	 */
	private IJavaProject chooseJavaProject() {
		final ILabelProvider labelProvider = new JavaElementLabelProvider(JavaElementLabelProvider.SHOW_DEFAULT);
		final ElementListSelectionDialog dialog = new ElementListSelectionDialog(this.getShell(), labelProvider);
		dialog.setTitle(LauncherMessages.AbstractJavaMainTab_4);
		dialog.setMessage(LauncherMessages.AbstractJavaMainTab_3);
		try {
			dialog.setElements(JavaCore.create(this.getWorkspaceRoot()).getJavaProjects());
		} catch (final JavaModelException javaModelException) {
			JDIDebugUIPlugin.log(javaModelException);
		}
		final IJavaProject javaProject = this.getJavaProject();
		if (javaProject != null) {
			dialog.setInitialSelections(new Object[] {
				javaProject
			});
		}
		if (dialog.open() == Window.OK) {
			return (IJavaProject) dialog.getFirstResult();
		}
		return null;
	}

	/**
	 * Gives the Workspace root.
	 *
	 * @return the Workspace root
	 */
	protected IWorkspaceRoot getWorkspaceRoot() {
		return ResourcesPlugin.getWorkspace().getRoot();
	}

	/**
	 * Gives the currently selected {@link IJavaProject}.
	 *
	 * @return the currently selected {@link IJavaProject} or {@code null}, when no
	 *         project was selected.
	 */
	protected IJavaProject getJavaProject() {
		final String projectName = this.fProjText.getText().trim();
		if (projectName.length() < 1) {
			return null;
		}
		return this.getJavaModel().getJavaProject(projectName);
	}

	/**
	 * Gives the {@link IJavaModel}.
	 *
	 * @return the {@link IJavaModel}.
	 */
	private IJavaModel getJavaModel() {
		return JavaCore.create(this.getWorkspaceRoot());
	}

	/**
	 * Class for handling the clicks on widgets.
	 *
	 * @author Roman Langrehr
	 */
	private class WidgetListener implements ModifyListener, SelectionListener {

		@Override
		public void modifyText(final ModifyEvent event) {
			ProjectTab.this.updateLaunchConfigurationDialog();
		}

		@Override
		public void widgetDefaultSelected(final SelectionEvent event) {
			/* do nothing */
		}

		@Override
		public void widgetSelected(final SelectionEvent event) {
			final Object source = event.getSource();
			if (source == ProjectTab.this.fProjButton) {
				ProjectTab.this.handleProjectButtonSelected();
			} else {
				ProjectTab.this.updateLaunchConfigurationDialog();
			}
		}
	}

}
