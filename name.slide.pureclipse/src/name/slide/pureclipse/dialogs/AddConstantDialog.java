package name.slide.pureclipse.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * 
 * @author Andrey Stolyarchuk
 *
 */
public class AddConstantDialog extends Dialog {
	
	private static final String RESULT_LABEL = "Result:";
	private static final String NAME_LABEL = "Name:";
	private static final String NAME_TEXT = "SOME_NOTIFICATION";
	private static final String RESULT_TEXT = "public static const %1S : String = \"%2s\";";
	
	private String resultText;
	
	public AddConstantDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected Control createContents(Composite parent) {
		getShell().setText("Add constant");
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout topLayout = new GridLayout(2, false);
		topLayout.verticalSpacing = 10;
		topLayout.horizontalSpacing = 10;
		topLayout.marginHeight = 10;
		topLayout.marginWidth = 10;
		composite.setLayout(topLayout);
		
		Label labelName = new Label(composite, SWT.NONE);
	    labelName.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, true, false));
	    labelName.setText(NAME_LABEL);
	    final Text textName = new Text(composite, SWT.BORDER);
	    textName.setText(NAME_TEXT);
	    GridData textNameGD = new GridData(SWT.FILL, SWT.FILL, true, false);
	    textNameGD.minimumWidth = 200;
	    textName.setLayoutData(textNameGD);
	    
	    Label labelResult = new Label(composite, SWT.NONE);
	    labelResult.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, true, false, 2, 1));
	    labelResult.setText(RESULT_LABEL);
	    final StyledText textResult = new StyledText(composite, SWT.BORDER | SWT.MULTI | SWT.WRAP);
	    textResult.setText(formatResultText(NAME_TEXT));
	    GridData textResultGD = new GridData(SWT.LEFT, SWT.FILL, true, true, 2, 1);
	    textResultGD.minimumWidth = 300;
	    textResultGD.minimumHeight = 100;
	    textResult.setLayoutData(textResultGD);
	    
	    textName.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				textResult.setText(formatResultText(textName.getText()));
			}
		});
	    
	    Composite compositeButton = new Composite(composite, SWT.NONE);
	    compositeButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		GridLayout layoutButton = new GridLayout(2, false);
		layoutButton.horizontalSpacing = 20;
		compositeButton.setLayout(layoutButton);
		
		Button buttonOk = new Button(compositeButton, SWT.NONE);
	    buttonOk.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false));
	    buttonOk.setText("Ok");
	    buttonOk.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				resultText = textResult.getText();
				okPressed();
			}
		});
	    
	    Button buttonCancel = new Button(compositeButton, SWT.NONE);
		buttonCancel.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false));
	    buttonCancel.setText("Cancel");
	    buttonCancel.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				cancelPressed();
			}
		});
	    
		return composite;
	}
	
	private String formatResultText(String nameText) {
		String[] parts = nameText.toLowerCase().split("_");
		StringBuilder valueText = new StringBuilder();
		for (String part : parts) {
			if (valueText.length() > 0 && part.length() > 0) {
				valueText.append(part.substring(0, 1).toUpperCase() + part.substring(1));
			}
			else {
				valueText.append(part);
			}
		}
		String result = String.format(RESULT_TEXT, nameText, valueText);
		return result;
	}
	
	@Override
	protected void handleShellCloseEvent() {
		super.handleShellCloseEvent();
		cancelPressed();
	}

	public String getResultText() {
		return resultText;
	}
	
}
