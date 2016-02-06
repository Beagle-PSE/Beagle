package de.uka.ipd.sdq.beagle.gui;

import de.uka.ipd.sdq.beagle.core.UserConfiguration;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/*
 * This class is involved in creating a Graphical User Interface. Its funtionality cannot
 * reasonably be tested by automated unit tests.
 *
 * COVERAGE:OFF
 */

/**
 * A page of {@link BeagleAnalysisWizard} allowing the user to choose between an adaptive
 * timeout, a constant timeout, or no timeout at all.
 *
 * @author Christoph Michelbach
 */
public class TimeoutWizardPage extends WizardPage {

	/**
	 * The title of this page.
	 */
	private static final String TITLE = "Timeout";

	/**
	 * The description of this page.
	 */
	private static final String DESCRIPTION =
		"To avoid infinite loops or too long calculations, a timeout can be used. Please choose as you wish.";

	/**
	 * The number of columns of the layout of container which contains the entire content
	 * of this page.
	 */
	private static final int MAIN_LAYOUT_NR_COLUMS = 2;

	/**
	 * The number of columns of the layout of the upper container.
	 */
	private static final int UPPER_LAYOUT_NR_COLUMS = 1;

	/**
	 * The number of columns of the layout of the lower container.
	 */
	private static final int LOWER_LAYOUT_NR_COLUMS = 3;

	/**
	 * The {@link UserConfiguration} this {@link TimeoutWizardPage} uses.
	 */
	private final UserConfiguration userConfiguration;

	/**
	 * A textbox for the timeout in seconds (if the timeout is set manually).
	 */
	private Text textboxTimeoutSeconds;

	/**
	 * The main container for this page.
	 */
	private Composite mainContainer;

	/**
	 * A sub-container.
	 */
	private Composite lowerContainer;

	/**
	 * A sub-container.
	 */
	private Composite upperContainer;

	/**
	 * Applies the default setting for the timeout. [-2 → adaptive timeout] [-1 → no
	 * timeout] [≥ 0 → timeout in seconds]
	 */
	private int timeout;

	/**
	 * The {@link SelectionListener} which will be called when the radio box indicating
	 * that an adaptive timeout will be used gets selected.
	 */
	private SelectionListener radioAdaptiveTimeoutSelected;

	/**
	 * The {@link SelectionListener} which will be called when the radio box indicating
	 * that a set timeout will be used gets selected.
	 */
	private SelectionListener radioSetTimeoutSelected;

	/**
	 * The {@link SelectionListener} which will be called when the radio box indicating
	 * that a set timeout will be used is no longer selected.
	 */
	private SelectionListener radioSetTimeoutDeselected;

	/**
	 * The {@link SelectionListener} which will be called when the radio box indicating
	 * that no timeout will be used gets selected.
	 */
	private SelectionListener radioNoTimeoutSelected;

	/**
	 * Constructs a new {@link TimeoutWizardPage} being linked to the given
	 * {@code userConfiguration}.
	 *
	 * @param userConfiguration The {@link UserConfiguration} this
	 *            {@link TimeoutWizardPage} will be permanently linked to. Changing the
	 *            associated {@link UserConfiguration} is not possible.
	 */
	public TimeoutWizardPage(final UserConfiguration userConfiguration) {
		super(TITLE);
		this.setTitle(TITLE);
		this.setDescription(DESCRIPTION);
		this.setControl(this.textboxTimeoutSeconds);
		this.userConfiguration = userConfiguration;
		this.timeout = this.userConfiguration.getTimeout();
	}

	@Override
	public void createControl(final Composite parent) {
		this.mainContainer = new Composite(parent, SWT.NONE);
		final GridLayout layoutAll = new GridLayout();
		this.mainContainer.setLayout(layoutAll);
		layoutAll.numColumns = MAIN_LAYOUT_NR_COLUMS;

		this.upperContainer = new Composite(this.mainContainer, SWT.NONE);
		final GridLayout upperLayout = new GridLayout();
		this.upperContainer.setLayout(upperLayout);
		upperLayout.numColumns = UPPER_LAYOUT_NR_COLUMS;

		final GridData gridData = new GridData(GridData.FILL_HORIZONTAL);

		final Button radioAdaptiveTimout = new Button(this.upperContainer, SWT.RADIO);
		radioAdaptiveTimout.setText("Use an adaptive timeout.");

		final Button radioSetTimout = new Button(this.upperContainer, SWT.RADIO);
		radioSetTimout.setText("Use a set timeout.");

		final Button radioNoTimeout = new Button(this.upperContainer, SWT.RADIO);
		radioNoTimeout.setText("Don't use a timeout.");

		radioAdaptiveTimout.setSelection(true);

		this.radioAdaptiveTimeoutSelected = new SelectionListener() {

			@Override
			public void widgetSelected(final SelectionEvent selectionEvent) {
				TimeoutWizardPage.this.timeout = UserConfiguration.ADAPTIVE_TIMEOUT;
				TimeoutWizardPage.this.userConfiguration.setTimeout(TimeoutWizardPage.this.timeout);
			}

			@Override
			public void widgetDefaultSelected(final SelectionEvent selectionEvent) {
				// do nothing
			}
		};

		this.radioSetTimeoutSelected = new SelectionListener() {

			@Override
			public void widgetSelected(final SelectionEvent selectionEvent) {
				TimeoutWizardPage.this.textboxTimeoutSeconds.setEnabled(true);

				if (TimeoutWizardPage.this.textboxTimeoutSeconds.getText().isEmpty()) {
					TimeoutWizardPage.this.setPageComplete(false);
				} else {
					TimeoutWizardPage.this.setPageComplete(true);
					TimeoutWizardPage.this.timeout =
						Integer.parseInt(TimeoutWizardPage.this.textboxTimeoutSeconds.getText());
					TimeoutWizardPage.this.userConfiguration.setTimeout(UserConfiguration.ADAPTIVE_TIMEOUT);
					TimeoutWizardPage.this.userConfiguration.setTimeout(TimeoutWizardPage.this.timeout);
				}
			}

			@Override
			public void widgetDefaultSelected(final SelectionEvent selectionEvent) {
				// do nothing
			}
		};

		this.radioSetTimeoutDeselected = new SelectionListener() {

			@Override
			public void widgetSelected(final SelectionEvent selectionEvent) {
				TimeoutWizardPage.this.textboxTimeoutSeconds.setEnabled(false);
				TimeoutWizardPage.this.setPageComplete(true);
			}

			@Override
			public void widgetDefaultSelected(final SelectionEvent selectionEvent) {
				// do nothing
			}
		};

		this.radioNoTimeoutSelected = new SelectionListener() {

			@Override
			public void widgetSelected(final SelectionEvent selectionEvent) {
				TimeoutWizardPage.this.timeout = UserConfiguration.NO_TIMEOUT;
				TimeoutWizardPage.this.userConfiguration.setTimeout(TimeoutWizardPage.this.timeout);
			}

			@Override
			public void widgetDefaultSelected(final SelectionEvent selectionEvent) {
				// do nothing
			}
		};

		radioAdaptiveTimout.addSelectionListener(this.radioAdaptiveTimeoutSelected);

		radioAdaptiveTimout.addSelectionListener(this.radioSetTimeoutDeselected);
		radioSetTimout.addSelectionListener(this.radioSetTimeoutSelected);
		radioNoTimeout.addSelectionListener(this.radioSetTimeoutDeselected);

		radioNoTimeout.addSelectionListener(this.radioNoTimeoutSelected);

		this.lowerContainer = new Composite(this.mainContainer, SWT.NONE);
		final GridLayout lowerLayout = new GridLayout();
		this.lowerContainer.setLayout(lowerLayout);
		lowerLayout.numColumns = LOWER_LAYOUT_NR_COLUMS;

		final Label label1 = new Label(this.lowerContainer, SWT.NONE);
		label1.setText("Custom timeout: ");

		this.textboxTimeoutSeconds = new Text(this.lowerContainer, SWT.BORDER | SWT.SINGLE);
		this.textboxTimeoutSeconds.setText("");
		this.textboxTimeoutSeconds.setEnabled(false);

		final Label label2 = new Label(this.lowerContainer, SWT.NONE);
		label2.setText("seconds");

		this.textboxTimeoutSeconds.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(final KeyEvent keyEvent) {
			}

			@Override
			public void keyReleased(final KeyEvent keyEvent) {
				// remove everything not a number
				if (keyEvent.character < '0' || keyEvent.character > '9') {
					TimeoutWizardPage.this.textboxTimeoutSeconds.setText(
						TimeoutWizardPage.this.textboxTimeoutSeconds.getText().replace("" + keyEvent.character, ""));
				}

				if (TimeoutWizardPage.this.textboxTimeoutSeconds.getText().isEmpty()) {
					TimeoutWizardPage.this.setPageComplete(false);
				} else {
					TimeoutWizardPage.this.setPageComplete(true);
				}
			}
		});

		this.textboxTimeoutSeconds.setLayoutData(gridData);

		// required to avoid an error in the system
		this.setControl(this.upperContainer);

		this.setPageComplete(true);

		this.adaptPageToDefaultValues();
	}

	/**
	 * Sets the content of this {@link WizardPage} to the default values defined in
	 * {@link UserConfiguration}. Note that this method will not rely on
	 * {@code DEFAULT_TIMEOUT} of {@link UserConfiguration} but instead will get the
	 * timeout of the user {@link UserConfiguration} object associated with this
	 * {@link WizardPage} so the visibility of this constant can be changed in the future.
	 */
	private void adaptPageToDefaultValues() {
		switch (this.userConfiguration.getTimeout()) {
			case UserConfiguration.ADAPTIVE_TIMEOUT:
				// Nothing needs to be done because {@link TimeoutWizardPage} is written
				// so this is the default.
				break;
			case UserConfiguration.NO_TIMEOUT:
				this.radioNoTimeoutSelected.widgetSelected(new SelectionEvent(null));
				break;
			default:
				// will be chosen when a set timeout is default
				this.radioSetTimeoutSelected.widgetSelected(new SelectionEvent(null));
				break;
		}
	}

	/**
	 * Returns the timeout chosen by the user.
	 *
	 * @return the timeout chosen by the user.
	 */
	public int getTimeout() {
		return this.timeout;
	}
}
