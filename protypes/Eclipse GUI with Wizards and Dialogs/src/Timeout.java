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

public class Timeout extends WizardPage {
	private Text text1;
	private Composite containerAll, container1, container2;
	private int timeout = -2;

	public Timeout() {
		super("Timeout");
		setTitle("Timeout");
		setDescription("Blah blah ... a timeout can be used.");
		setControl(text1);
	}

	@Override

	public void createControl(Composite parent) {
		containerAll = new Composite(parent, SWT.NONE);
		GridLayout layoutAll = new GridLayout();
		containerAll.setLayout(layoutAll);
		layoutAll.numColumns = 1;

		container2 = new Composite(containerAll, SWT.NONE);
		GridLayout layout2 = new GridLayout();
		container2.setLayout(layout2);
		layout2.numColumns = 2;

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);

		Button radioAdaptiveTimout = new Button(container2, SWT.RADIO);
		Label lblRadioAdaptiveTimout = new Label(container2, SWT.NONE);
		lblRadioAdaptiveTimout.setText("Use an adaptive timeout.");

		Button radioSetTimout = new Button(container2, SWT.RADIO);
		Label lblRadioSetTimout = new Label(container2, SWT.NONE);
		lblRadioSetTimout.setText("Use a set timout.");

		Button radioNoTimeout = new Button(container2, SWT.RADIO);
		Label lblRadioNoTimeout = new Label(container2, SWT.NONE);
		lblRadioNoTimeout.setText("Don't use a timout.");

		radioAdaptiveTimout.setSelection(true);

		SelectionListener radioAdaptiveTimeoutSelected = new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				timeout = -2;
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// do nothing
			}
		};

		SelectionListener radioSetTimeoutSelected = new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				text1.setEnabled(true);

				if (text1.getText().isEmpty()) {
					setPageComplete(false);
				} else {
					setPageComplete(true);
					timeout = Integer.parseInt(text1.getText());
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// do nothing
			}
		};

		SelectionListener radioSetTimeoutDeselected = new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				text1.setEnabled(false);
				setPageComplete(true);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// do nothing
			}
		};

		SelectionListener radioNoTimeoutSelected = new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				timeout = -1;
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// do nothing
			}
		};

		radioAdaptiveTimout.addSelectionListener(radioAdaptiveTimeoutSelected);

		radioAdaptiveTimout.addSelectionListener(radioSetTimeoutDeselected);
		radioSetTimout.addSelectionListener(radioSetTimeoutSelected);
		radioNoTimeout.addSelectionListener(radioSetTimeoutDeselected);

		radioNoTimeout.addSelectionListener(radioNoTimeoutSelected);

		container1 = new Composite(containerAll, SWT.NONE);
		GridLayout layout = new GridLayout();
		container1.setLayout(layout);
		layout.numColumns = 3;

		Label label1 = new Label(container1, SWT.NONE);
		label1.setText("Custom timeout: ");

		text1 = new Text(container1, SWT.BORDER | SWT.SINGLE);
		text1.setText("");
		text1.setEnabled(false);

		Label label2 = new Label(container1, SWT.NONE);
		label2.setText("seconds");

		text1.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.character < '0' || e.character > '9') { // remove
																// everything
																// not a number
					text1.setText(text1.getText().replace("" + e.character, ""));
				}

				if (text1.getText().isEmpty()) {
					setPageComplete(false);
				} else {
					setPageComplete(true);
				}
			}

		});

		text1.setLayoutData(gd);

		// required to avoid an error in the system

		setControl(container2);

		setPageComplete(true);
	}

	public int getTimeout() {
		return timeout;
	}
}
