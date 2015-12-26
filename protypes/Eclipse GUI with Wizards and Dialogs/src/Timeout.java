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

/**
 * Page 2 of the wizard.
 * 
 * @author Christoph Michelbach
 *
 */
public class Timeout extends WizardPage {
	/**
	 * The title of this page.
	 */
	private static String title = "Timeout";

	/**
	 * The description of this page.
	 */
	private static String description = "Blah blah ... a timeout can be used.";

	/**
	 * A textbox for the timeout in seconds (if the timeout is set manually).
	 */
	private Text tbTimeoutSeconds;

	/**
	 * The main container for this page.
	 */
	private Composite mainContainer;
	/**
	 * A sub-container.
	 */
	private Composite container1;
	/**
	 * A sub-container.
	 */
	private Composite container2;
	/**
	 * The default setting for the timeout. [-2 -> adaptive timeout] [-1 -> no timeout]
	 * [>= 0 -> timeout in seconds]
	 */
	private final int defaultTimeout = -2;
	/**
	 * The default setting for the timeout. [-2 -> adaptive timeout] [-1 -> no timeout]
	 * [>= 0 -> timeout in seconds]
	 */
	private int timeout = this.defaultTimeout;

	/**
	 * Constructor setting the tile and the introduction to this page.
	 */
	public Timeout() {
		super(title);
		setTitle(title);
		setDescription(description);
		setControl(this.tbTimeoutSeconds);
	}

	@Override
	public void createControl(final Composite parent) {
		this.mainContainer = new Composite(parent, SWT.NONE);
		final GridLayout layoutAll = new GridLayout();
		this.mainContainer.setLayout(layoutAll);
		layoutAll.numColumns = 1;

		this.container2 = new Composite(this.mainContainer, SWT.NONE);
		final GridLayout layout2 = new GridLayout();
		this.container2.setLayout(layout2);
		layout2.numColumns = 2;

		final GridData gd = new GridData(GridData.FILL_HORIZONTAL);

		final Button radioAdaptiveTimout = new Button(this.container2, SWT.RADIO);
		final Label lblRadioAdaptiveTimout = new Label(this.container2, SWT.NONE);
		lblRadioAdaptiveTimout.setText("Use an adaptive timeout.");

		final Button radioSetTimout = new Button(this.container2, SWT.RADIO);
		final Label lblRadioSetTimout = new Label(this.container2, SWT.NONE);
		lblRadioSetTimout.setText("Use a set timout.");

		final Button radioNoTimeout = new Button(this.container2, SWT.RADIO);
		final Label lblRadioNoTimeout = new Label(this.container2, SWT.NONE);
		lblRadioNoTimeout.setText("Don't use a timout.");

		radioAdaptiveTimout.setSelection(true);

		final SelectionListener radioAdaptiveTimeoutSelected = new SelectionListener() {
			@Override
			public void widgetSelected(final SelectionEvent arg0) {
				Timeout.this.timeout = -2;
			}

			@Override
			public void widgetDefaultSelected(final SelectionEvent arg0) {
				// do nothing
			}
		};

		final SelectionListener radioSetTimeoutSelected = new SelectionListener() {
			@Override
			public void widgetSelected(final SelectionEvent arg0) {
				Timeout.this.tbTimeoutSeconds.setEnabled(true);

				if (Timeout.this.tbTimeoutSeconds.getText().isEmpty()) {
					setPageComplete(false);
				} else {
					setPageComplete(true);
					Timeout.this.timeout = Integer.parseInt(tbTimeoutSeconds.getText());
				}
			}

			@Override
			public void widgetDefaultSelected(final SelectionEvent arg0) {
				// do nothing
			}
		};

		final SelectionListener radioSetTimeoutDeselected = new SelectionListener() {
			@Override
			public void widgetSelected(final SelectionEvent arg0) {
				Timeout.this.tbTimeoutSeconds.setEnabled(false);
				setPageComplete(true);
			}

			@Override
			public void widgetDefaultSelected(final SelectionEvent arg0) {
				// do nothing
			}
		};

		final SelectionListener radioNoTimeoutSelected = new SelectionListener() {
			@Override
			public void widgetSelected(final SelectionEvent arg0) {
				Timeout.this.timeout = -1;
			}

			@Override
			public void widgetDefaultSelected(final SelectionEvent arg0) {
				// do nothing
			}
		};

		radioAdaptiveTimout.addSelectionListener(radioAdaptiveTimeoutSelected);

		radioAdaptiveTimout.addSelectionListener(radioSetTimeoutDeselected);
		radioSetTimout.addSelectionListener(radioSetTimeoutSelected);
		radioNoTimeout.addSelectionListener(radioSetTimeoutDeselected);

		radioNoTimeout.addSelectionListener(radioNoTimeoutSelected);

		this.container1 = new Composite(this.mainContainer, SWT.NONE);
		final GridLayout layout = new GridLayout();
		this.container1.setLayout(layout);
		layout.numColumns = 3;

		final Label label1 = new Label(this.container1, SWT.NONE);
		label1.setText("Custom timeout: ");

		this.tbTimeoutSeconds = new Text(this.container1, SWT.BORDER | SWT.SINGLE);
		this.tbTimeoutSeconds.setText("");
		this.tbTimeoutSeconds.setEnabled(false);

		final Label label2 = new Label(this.container1, SWT.NONE);
		label2.setText("seconds");

		this.tbTimeoutSeconds.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(final KeyEvent e) {
			}

			@Override
			public void keyReleased(final KeyEvent e) {
				// remove everything not a number
				if (e.character < '0' || e.character > '9') {
					Timeout.this.tbTimeoutSeconds
							.setText(Timeout.this.tbTimeoutSeconds.getText().replace("" + e.character, ""));
				}

				if (Timeout.this.tbTimeoutSeconds.getText().isEmpty()) {
					setPageComplete(false);
				} else {
					setPageComplete(true);
				}
			}

		});

		this.tbTimeoutSeconds.setLayoutData(gd);

		// required to avoid an error in the system

		setControl(this.container2);

		setPageComplete(true);
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
