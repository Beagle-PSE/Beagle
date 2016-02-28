package de.uka.ipd.sdq.beagle.gui;

import de.uka.ipd.sdq.beagle.core.facade.BeagleConfiguration;
import de.uka.ipd.sdq.beagle.core.timeout.AdaptiveTimeout;
import de.uka.ipd.sdq.beagle.core.timeout.ConstantTimeout;
import de.uka.ipd.sdq.beagle.core.timeout.NoTimeout;
import de.uka.ipd.sdq.beagle.core.timeout.Timeout;

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
	 * Extensive inquiries yielded that a second comprises this many milliseconds.
	 */
	private static final int SECOND_MILLISECOND_RATIO = 1000;

	/**
	 * The {@link BeagleConfiguration} this {@link TimeoutWizardPage} uses.
	 */
	private final BeagleConfiguration beagleConfiguration;

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
	 * {@code beagleConfiguration}.
	 *
	 * @param beagleConfiguration The {@link BeagleConfiguration} this
	 *            {@link TimeoutWizardPage} will be permanently linked to. Changing the
	 *            associated {@link BeagleConfiguration} is not possible.
	 */
	public TimeoutWizardPage(final BeagleConfiguration beagleConfiguration) {
		super(TITLE);
		this.setTitle(TITLE);
		this.setDescription(DESCRIPTION);
		this.setControl(this.textboxTimeoutSeconds);
		this.beagleConfiguration = beagleConfiguration;
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
				TimeoutWizardPage.this.beagleConfiguration.setTimeout(new AdaptiveTimeout());
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
					final ConstantTimeout constantTimeout =
						new ConstantTimeout(Integer.parseInt(TimeoutWizardPage.this.textboxTimeoutSeconds.getText())
							* SECOND_MILLISECOND_RATIO);
					TimeoutWizardPage.this.beagleConfiguration.setTimeout(constantTimeout);
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
				TimeoutWizardPage.this.beagleConfiguration.setTimeout(new NoTimeout());
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
	 * {@link BeagleConfiguration}. Note that this method will not rely on
	 * {@code DEFAULT_TIMEOUT} of {@link BeagleConfiguration} but instead will get the
	 * timeout of the user {@link BeagleConfiguration} object associated with this
	 * {@link WizardPage} so the visibility of this constant can be changed in the future.
	 */
	private void adaptPageToDefaultValues() {

		// CHECKSTYLE:OFF
		if (this.beagleConfiguration.getTimeout() instanceof AdaptiveTimeout) {
			// Nothing needs to be done because {@link TimeoutWizardPage} is written
			// so this is the default.
			// CHECKSTYLE:ON
		} else if (this.beagleConfiguration.getTimeout() instanceof NoTimeout) {
			this.radioNoTimeoutSelected.widgetSelected(new SelectionEvent(null));
		} else {
			// will be chosen when a set timeout is default
			this.radioSetTimeoutSelected.widgetSelected(new SelectionEvent(null));
		}
	}

	/**
	 * Returns the timeout chosen by the user.
	 *
	 * @return the timeout chosen by the user.
	 */
	public Timeout getTimeout() {
		return this.beagleConfiguration.getTimeout();
	}
}
