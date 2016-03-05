package de.uka.ipd.sdq.beagle.gui;

import de.uka.ipd.sdq.beagle.core.failurehandling.FailureHandler;
import de.uka.ipd.sdq.beagle.core.failurehandling.FailureReport;
import de.uka.ipd.sdq.beagle.core.timeout.AdaptiveTimeout;
import de.uka.ipd.sdq.beagle.core.timeout.ConstantTimeout;
import de.uka.ipd.sdq.beagle.core.timeout.NoTimeout;
import de.uka.ipd.sdq.beagle.core.timeout.Timeout;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
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
 * This class is involved in creating a Graphical User Interface. Its functionality cannot
 * reasonably be tested by automated unit tests.
 *
 * COVERAGE:OFF
 */

/**
 * A tab of Beagle's launch configuration allowing the user to choose between an adaptive
 * timeout, a constant timeout, or no timeout at all.
 *
 * @author Christoph Michelbach
 * @author Roman Langrehr
 */
public class TimeoutTab extends AbstractLaunchConfigurationTab {

	/**
	 * The key in the Beagle Launch Configuration determining the type of the timeout to
	 * be used.
	 *
	 * <p>Permitted values are:
	 *
	 * <ul>
	 *
	 * <li> {@link #BEAGLE_LAUNCH_CONFIGURATION_TIMEOUT_TYPE_VALUE_ADAPTIVE_TIMEOUT}
	 *
	 * <li>{@link #BEAGLE_LAUNCH_CONFIGURATION_TIMEOUT_TYPE_VALUE_CONSTANT_TIMEOUT}
	 *
	 * <li>{@link #BEAGLE_LAUNCH_CONFIGURATION_TIMEOUT_TYPE_VALUE_NO_TIMEOUT}
	 *
	 * </ul>
	 *
	 */
	public static final String BEAGLE_LAUNCH_CONFIGURATION_TIMEOUT_TYPE = "TIMEOUT_TYPE";

	/**
	 * This value in Beagle's launch configuration for the key
	 * {@link #BEAGLE_LAUNCH_CONFIGURATION_TIMEOUT_TYPE} specifies that the
	 * {@link Timeout} used should be a {@link AdaptiveTimeout}.
	 */
	public static final String BEAGLE_LAUNCH_CONFIGURATION_TIMEOUT_TYPE_VALUE_ADAPTIVE_TIMEOUT = "ADAPTIVE_TIMEOUT";

	/**
	 * This value in Beagle's launch configuration for the key
	 * {@link #BEAGLE_LAUNCH_CONFIGURATION_TIMEOUT_TYPE} specifies that the
	 * {@link Timeout} used should be a {@link ConstantTimeout}.
	 */
	public static final String BEAGLE_LAUNCH_CONFIGURATION_TIMEOUT_TYPE_VALUE_CONSTANT_TIMEOUT = "CONSTANT_TIMEOUT";

	/**
	 * This value in Beagle's launch configuration for the key
	 * {@link #BEAGLE_LAUNCH_CONFIGURATION_TIMEOUT_TYPE} specifies that the
	 * {@link Timeout} used should be a {@link NoTimeout}.
	 */
	public static final String BEAGLE_LAUNCH_CONFIGURATION_TIMEOUT_TYPE_VALUE_NO_TIMEOUT = "NO_TIMEOUT";

	/**
	 * The default value in Beagle's launch configuration for the key
	 * {@link #BEAGLE_LAUNCH_CONFIGURATION_TIMEOUT_TYPE}.
	 */
	public static final String BEAGLE_LAUNCH_CONFIGURATION_TIMEOUT_TYPE_DEFAULT_VALUE =
		BEAGLE_LAUNCH_CONFIGURATION_TIMEOUT_TYPE_VALUE_NO_TIMEOUT;

	/**
	 * The key in the Beagle Launch Configuration determining the time set up for an
	 * {@link ConstantTimeout} in seconds.
	 */
	public static final String BEAGLE_LAUNCH_CONFIGURATION_CONSTANT_TIMEOUT_VALUE = "CONSTANT_TIMEOUT_VALUE";

	/**
	 * The default value in Beagle's launch configuration for the key
	 * {@link #BEAGLE_LAUNCH_CONFIGURATION_CONSTANT_TIMEOUT_VALUE}.
	 */
	public static final int BEAGLE_LAUNCH_CONFIGURATION_CONSTANT_TIMEOUT_VALUE_DEFAULT_VALUE = 60;

	/**
	 * The title of this tab.
	 */
	private static final String TITLE = "Timeout";

	/**
	 * The number of columns of the layout of container which contains the entire content
	 * of this tab.
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
	 * The type of timeout that is currently selected.
	 *
	 * <p>Permitted values are:
	 *
	 * <ul>
	 *
	 * <li> {@link #BEAGLE_LAUNCH_CONFIGURATION_TIMEOUT_TYPE_VALUE_ADAPTIVE_TIMEOUT}
	 *
	 * <li>{@link #BEAGLE_LAUNCH_CONFIGURATION_TIMEOUT_TYPE_VALUE_CONSTANT_TIMEOUT}
	 *
	 * <li>{@link #BEAGLE_LAUNCH_CONFIGURATION_TIMEOUT_TYPE_VALUE_NO_TIMEOUT}
	 *
	 * </ul>
	 */
	private String currentTimeoutTypeSelection;

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
				TimeoutTab.this.currentTimeoutTypeSelection =
					BEAGLE_LAUNCH_CONFIGURATION_TIMEOUT_TYPE_VALUE_ADAPTIVE_TIMEOUT;
			}

			@Override
			public void widgetDefaultSelected(final SelectionEvent selectionEvent) {
				// do nothing
			}
		};

		this.radioSetTimeoutSelected = new SelectionListener() {

			@Override
			public void widgetSelected(final SelectionEvent selectionEvent) {
				TimeoutTab.this.textboxTimeoutSeconds.setEnabled(true);

				TimeoutTab.this.currentTimeoutTypeSelection =
					BEAGLE_LAUNCH_CONFIGURATION_TIMEOUT_TYPE_VALUE_CONSTANT_TIMEOUT;
			}

			@Override
			public void widgetDefaultSelected(final SelectionEvent selectionEvent) {
				// do nothing
			}
		};

		this.radioSetTimeoutDeselected = new SelectionListener() {

			@Override
			public void widgetSelected(final SelectionEvent selectionEvent) {
				TimeoutTab.this.textboxTimeoutSeconds.setEnabled(false);
			}

			@Override
			public void widgetDefaultSelected(final SelectionEvent selectionEvent) {
				// do nothing
			}
		};

		this.radioNoTimeoutSelected = new SelectionListener() {

			@Override
			public void widgetSelected(final SelectionEvent selectionEvent) {

				TimeoutTab.this.currentTimeoutTypeSelection = BEAGLE_LAUNCH_CONFIGURATION_TIMEOUT_TYPE_DEFAULT_VALUE;
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
					TimeoutTab.this.textboxTimeoutSeconds
						.setText(TimeoutTab.this.textboxTimeoutSeconds.getText().replace("" + keyEvent.character, ""));
				}
			}
		});

		this.textboxTimeoutSeconds.setLayoutData(gridData);

		// required to avoid an error in the system
		this.setControl(this.mainContainer);
	}

	@Override
	public void setDefaults(final ILaunchConfigurationWorkingCopy configuration) {
		configuration.setAttribute(BEAGLE_LAUNCH_CONFIGURATION_TIMEOUT_TYPE,
			BEAGLE_LAUNCH_CONFIGURATION_TIMEOUT_TYPE_DEFAULT_VALUE);
		configuration.setAttribute(BEAGLE_LAUNCH_CONFIGURATION_CONSTANT_TIMEOUT_VALUE,
			BEAGLE_LAUNCH_CONFIGURATION_CONSTANT_TIMEOUT_VALUE_DEFAULT_VALUE);
	}

	@Override
	public void initializeFrom(final ILaunchConfiguration configuration) {
		try {
			this.currentTimeoutTypeSelection = configuration.getAttribute(BEAGLE_LAUNCH_CONFIGURATION_TIMEOUT_TYPE,
				BEAGLE_LAUNCH_CONFIGURATION_TIMEOUT_TYPE_DEFAULT_VALUE);
			this.textboxTimeoutSeconds
				.setText("" + configuration.getAttribute(BEAGLE_LAUNCH_CONFIGURATION_CONSTANT_TIMEOUT_VALUE,
					BEAGLE_LAUNCH_CONFIGURATION_CONSTANT_TIMEOUT_VALUE_DEFAULT_VALUE));
		} catch (final CoreException coreException) {
			FailureHandler.getHandler(this.getClass()).handle(new FailureReport<>().cause(coreException)
				.retryWith(() -> TimeoutTab.this.initializeFrom(configuration)));
		}

	}

	@Override
	public void performApply(final ILaunchConfigurationWorkingCopy configuration) {
		configuration.setAttribute(BEAGLE_LAUNCH_CONFIGURATION_TIMEOUT_TYPE, this.currentTimeoutTypeSelection);
		configuration.setAttribute(BEAGLE_LAUNCH_CONFIGURATION_CONSTANT_TIMEOUT_VALUE,
			Integer.parseInt(this.textboxTimeoutSeconds.getText()));
	}

	@Override
	public String getName() {
		return TITLE;
	}

	@Override
	public boolean isValid(final ILaunchConfiguration launchConfig) {
		this.setErrorMessage(null);
		if (this.textboxTimeoutSeconds.getText().isEmpty() && this.currentTimeoutTypeSelection
			.equals(BEAGLE_LAUNCH_CONFIGURATION_TIMEOUT_TYPE_VALUE_CONSTANT_TIMEOUT)) {
			this.setErrorMessage("Please enter a value for the constant timeout");
		}
		return true;
	}
}
