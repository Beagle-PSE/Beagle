package de.uka.ipd.sdq.beagle.gui;

import de.uka.ipd.sdq.beagle.core.failurehandling.FailureHandler;
import de.uka.ipd.sdq.beagle.core.failurehandling.FailureReport;

import org.eclipse.core.resources.IProject;
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
import org.eclipse.swt.widgets.Label;
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
	 * The key in the Beagle Launch Configuration determining the path of the source code
	 * link file to analyse.
	 */
	public static final String BEAGLE_LAUNCH_CONFIGURATION_SOURCECODELINK_FILE =
		"de.uka.ipd.sdq.beagle.SOURCE_CODE_FILE_LINK";

	/**
	 * The default value for the key
	 * {@link #BEAGLE_LAUNCH_CONFIGURATION_SOURCECODELINK_FILE}.
	 */
	public static final String BEAGLE_LAUNCH_CONFIGURATION_SOURCECODELINK_FILE_DEFAULT_VALUE =
		"model/internal_architecture_model_source_statement_links.xml";

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
	 * The text box containing the source code link file path.
	 */
	private Text sourcecodelinkText;

	/**
	 * The select Project Button.
	 */
	private Button fProjButton;

	@Override
	public void setDefaults(final ILaunchConfigurationWorkingCopy configuration) {
		// There are no useful defaults for the project.
		configuration.setAttribute(BEAGLE_LAUNCH_CONFIGURATION_REPOSITORY_FILE,
			BEAGLE_LAUNCH_CONFIGURATION_REPOSITORY_FILE_DEFAULT_VALUE);
		configuration.setAttribute(BEAGLE_LAUNCH_CONFIGURATION_SOURCECODELINK_FILE,
			BEAGLE_LAUNCH_CONFIGURATION_SOURCECODELINK_FILE_DEFAULT_VALUE);
	}

	@Override
	public void initializeFrom(final ILaunchConfiguration configuration) {
		try {
			this.fProjText.setText(configuration.getAttribute(BEAGLE_LAUNCH_CONFIGURATION_IJAVAPROJECT, ""));
			this.repositoryText.setText(configuration.getAttribute(BEAGLE_LAUNCH_CONFIGURATION_REPOSITORY_FILE,
				BEAGLE_LAUNCH_CONFIGURATION_REPOSITORY_FILE_DEFAULT_VALUE));
			this.sourcecodelinkText.setText(configuration.getAttribute(BEAGLE_LAUNCH_CONFIGURATION_SOURCECODELINK_FILE,
				BEAGLE_LAUNCH_CONFIGURATION_SOURCECODELINK_FILE_DEFAULT_VALUE));
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
		configuration.setAttribute(BEAGLE_LAUNCH_CONFIGURATION_SOURCECODELINK_FILE, this.sourcecodelinkText.getText());
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
	protected void createRepositoryEditor(final Composite parent) {
		final Font font = parent.getFont();
		final Group group = new Group(parent, SWT.NONE);
		group.setText("Repository");
		final GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		group.setLayoutData(gridData);
		final GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		group.setLayout(layout);
		group.setFont(font);

		final Composite repositoryComposite = new Composite(group, SWT.NONE);
		final GridData gridData2 = new GridData(GridData.FILL_HORIZONTAL);
		repositoryComposite.setLayoutData(gridData2);
		repositoryComposite.setLayout(new GridLayout(2, false));
		final Label repositoryLabel = new Label(repositoryComposite, SWT.NONE);
		repositoryLabel.setText("Repository file: ");
		this.repositoryText = new Text(repositoryComposite, SWT.SINGLE | SWT.BORDER);
		this.repositoryText.addModifyListener(new WidgetListener());
		final GridData gridData3 = new GridData(GridData.FILL_HORIZONTAL);
		this.repositoryText.setLayoutData(gridData3);
		this.repositoryText.setFont(font);

		final Composite sourcodelinkComposite = new Composite(group, SWT.NONE);
		final GridData gridData4 = new GridData(GridData.FILL_HORIZONTAL);
		sourcodelinkComposite.setLayoutData(gridData4);
		sourcodelinkComposite.setLayout(new GridLayout(2, false));
		final Label sourcecodelinkLabel = new Label(sourcodelinkComposite, SWT.NONE);
		sourcecodelinkLabel.setText("Source code link file: ");
		this.sourcecodelinkText = new Text(sourcodelinkComposite, SWT.SINGLE | SWT.BORDER);
		this.sourcecodelinkText.addModifyListener(new WidgetListener());
		final GridData gridData5 = new GridData(GridData.FILL_HORIZONTAL);
		this.sourcecodelinkText.setLayoutData(gridData5);
		this.sourcecodelinkText.setFont(font);
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
		this.createRepositoryEditor(projComp);

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

	/*
	 * This method has to perform checks. Thus, there are a lot of paths through it, but
	 * that makes sense at this point.
	 */
	// CHECKSTYLE:IGNORE NPath
	@Override
	public boolean isValid(final ILaunchConfiguration launchConfig) {
		final IProject project;
		final String projectName;
		final String repositoryFilePath;
		final String sslFilePath;

		try {
			projectName = launchConfig.getAttribute(ProjectTab.BEAGLE_LAUNCH_CONFIGURATION_IJAVAPROJECT, (String) null);
		} catch (final CoreException coreException) {
			this.setErrorMessage("Malformed project configuration.");
			return false;
		}
		if (projectName == null) {
			this.setErrorMessage("No project is configured");
			return false;
		}
		project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		if (!project.exists()) {
			this.setErrorMessage("The configured project does not exist.");
			return false;
		}

		try {
			repositoryFilePath = launchConfig.getAttribute(BEAGLE_LAUNCH_CONFIGURATION_REPOSITORY_FILE, (String) null);
		} catch (final CoreException coreException) {
			this.setErrorMessage("Malformed repository file configuration.");
			return false;
		}
		if (repositoryFilePath == null) {
			this.setErrorMessage("No repository file is configured.");
			return false;
		}
		if (!project.getFile(repositoryFilePath).exists()) {
			this.setErrorMessage("The configured repository file cannot be found.");
			return false;
		}

		try {
			sslFilePath = launchConfig.getAttribute(BEAGLE_LAUNCH_CONFIGURATION_SOURCECODELINK_FILE, (String) null);
		} catch (final CoreException coreException) {
			this.setErrorMessage("Malformed source statement file configuration.");
			return false;
		}
		if (sslFilePath == null) {
			this.setErrorMessage("No source statement link model file is configured.");
			return false;
		}
		if (!project.getFile(sslFilePath).exists()) {
			this.setErrorMessage("The configured source statement link model file cannot be found.");
			return false;
		}

		this.setErrorMessage(null);
		return true;
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
