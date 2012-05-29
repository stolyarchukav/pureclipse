package name.slide.pureclipse.license;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

/**
 * 
 * @author Andrey Stolyarchuk
 *
 */
public class TrialDialog extends Dialog {
	
	private int daysLeft;
	private String serialNumber;
	private boolean continueDialog;
	
	public TrialDialog(Shell parentShell, int daysLeft, String serialNumber, boolean continueDialog) {
		super(parentShell);
		this.daysLeft = daysLeft;
		this.serialNumber = serialNumber;
		this.continueDialog = continueDialog;
	}

	@Override
	protected Control createContents(Composite parent) {
		final StringBuilder text = new StringBuilder(100);
		text.append("Pureclipse 1.5.02 \n");
		text.append("Built for Flash Builder 4");
		text.append("\n\n\n");
		if (serialNumber != null) {
			text.append("Thank you for purchasing Pureclipse.\n");
			text.append("Serial number: ");
			text.append(serialNumber);
		}
		else if (daysLeft > 0) {
			text.append("This is a trial version: " + daysLeft + " days left");
		}
		else {
			text.append("Your trial period has expired");
		}
		text.append("\n");
		text.append("(c) Copyright 2010 SlideUI LLC. \n");
		text.append("All rights reserved");
		
		getShell().setText("Help");
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout topLayout = new GridLayout(2, false);
		topLayout.verticalSpacing = 10;
		topLayout.horizontalSpacing = 10;
		topLayout.marginHeight = 10;
		topLayout.marginWidth = 10;
		composite.setLayout(topLayout);
		
		Image image = new Image(getShell().getDisplay(), getClass().getClassLoader().getResourceAsStream("logo/pureclipse_logo_transparent.png"));
		Label labelImage = new Label(composite, SWT.NONE);
		labelImage.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, true, false, 1, 2));
		labelImage.setImage(image);
		
		Label label = new Label(composite, SWT.NONE);
	    label.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, false));
	    label.setText(text.toString());
	    Font initialFont = label.getFont();
	    FontData[] fontData = initialFont.getFontData();
	    for (int i = 0; i < fontData.length; i++) {
	    	fontData[i].setHeight(10);
	    }
	    Font newFont = new Font(getShell().getDisplay(), fontData);
	    label.setFont(newFont);
	    
	    Composite compositeButton = new Composite(composite, SWT.NONE);
	    compositeButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		GridLayout layoutButton = new GridLayout(2, false);
		layoutButton.horizontalSpacing = 20;
		compositeButton.setLayout(layoutButton);
		
		if (serialNumber == null) {
			Button snButton = new Button(compositeButton, SWT.NONE);
		    snButton.setText("Enter serial number");
		    snButton.addListener(SWT.Selection, new Listener() {
				@Override
				public void handleEvent(Event event) {
					close();
					SerialNumberDialog snDialog = new SerialNumberDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
					snDialog.open();
				}
			});
		    snButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		}
		
		Button okButton = new Button(compositeButton, SWT.NONE);
	    if (serialNumber != null) {
	    	okButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
	    }
	    else {
	    	okButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		}
	    if (continueDialog) {
	    	okButton.setText("Continue");
	    }
	    else {
	    	okButton.setText("Ok");
	    }
	    okButton.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				close();
			}
		});
	    
		return composite;
	}
	
	@Override
	protected void handleShellCloseEvent() {
		super.handleShellCloseEvent();
		close();
	}
	
}
