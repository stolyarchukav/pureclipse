package name.slide.pureclipse.license;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * 
 * @author Andrey Stolyarchuk
 *
 */
public class SerialNumberDialog extends Dialog {
	
	public SerialNumberDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected Control createContents(Composite parent) {
		getShell().setText("Enter serial number");
		
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout topLayout = new GridLayout(3, false);
		topLayout.verticalSpacing = 10;
		topLayout.horizontalSpacing = 10;
		topLayout.marginHeight = 10;
		topLayout.marginWidth = 10;
		composite.setLayout(topLayout);
		
		Image image = new Image(getShell().getDisplay(), getClass().getClassLoader().getResourceAsStream("logo/pureclipse_logo_transparent.png"));
		Label labelImage = new Label(composite, SWT.NONE);
		labelImage.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, true, false, 1, 5));
		labelImage.setImage(image);
		
		final Label labelError = new Label(composite, SWT.NONE);
		labelError.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, false, 2, 1));
		labelError.setForeground(new Color(getShell().getDisplay(), 255, 0, 0));
		labelError.setText("Please, enter the correct email and serial number");
		labelError.setVisible(false);
		
		Label labelEmail = new Label(composite, SWT.NONE);
	    labelEmail.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, true, false));
	    labelEmail.setText("Email : ");
	    final Text textEmail = new Text(composite, SWT.BORDER);
	    GridData textEmailGD = new GridData(SWT.LEFT, SWT.FILL, true, false);
	    textEmailGD.minimumWidth = 200;
	    textEmail.setLayoutData(textEmailGD);
	    
	    Label labelSN = new Label(composite, SWT.NONE);
	    labelSN.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, true, false));
	    labelSN.setText("Serial number : ");
	    final Text textSN = new Text(composite, SWT.BORDER);
	    GridData textSNGD = new GridData(SWT.LEFT, SWT.FILL, true, false);
	    textSNGD.minimumWidth = 200;
	    textSN.setLayoutData(textSNGD);
		
	    Link link = new Link(composite, SWT.NONE);
		link.setText("<a href=\"https://sites.fastspring.com/slide/instant/pureclipse\" style=\"color:blue\">" +
				"Purchase license from Slide UI</a> ");
		link.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				Program.launch(event.text);
		   	}
		});
		Font initialFont = link.getFont();
	    FontData[] fontData = initialFont.getFontData();
	    for (int i = 0; i < fontData.length; i++) {
	    	fontData[i].setHeight(12);
	    }
	    Font newFont = new Font(getShell().getDisplay(), fontData);
	    link.setFont(newFont);
	    link.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, true, false, 2, 1));
	    
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
				String email = textEmail.getText();
				String serialNumber = textSN.getText();
				LicenseType licenseType = LicenseValidator.validate(email, serialNumber);
				if (licenseType != null) {
					if (licenseType == LicenseType.FULL) {
						LicenseManager.getInstance().writeSerialNumber(email, serialNumber);
						close();
					}
					else if (licenseType == LicenseType.TEMP) {
						if (LicenseManager.getInstance().startTrial(true)) {
							close();
						}
						else {
							labelError.setVisible(true);
						}
					}
				}
				else {
					labelError.setVisible(true);
				}
			}
		});
	    
	    Button buttonCancel = new Button(compositeButton, SWT.NONE);
		buttonCancel.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false));
	    buttonCancel.setText("Cancel");
	    buttonCancel.addListener(SWT.Selection, new Listener() {
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
