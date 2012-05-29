package name.slide.pureclipse.types;


import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

/**
 * 
 * @author Andrey Stolyarchuk
 *
 */
public class TemplateTypeComposite extends Composite {
	
	private Button singleCoreButton;
	private Button multiCoreButton;
	private Button fabricationButton;
	
	public TemplateTypeComposite(Composite parent, int style) {
		super(parent, style);
		
		RowLayout checkTypesLayout = new RowLayout();
		checkTypesLayout.spacing = 30;
		checkTypesLayout.wrap = true;
		this.setLayout(checkTypesLayout);
		GridData layoutData = new GridData();
		layoutData.horizontalSpan = 3;
		this.setLayoutData(layoutData);
		
		// "SingleCore" button
		singleCoreButton = new Button(this, SWT.RADIO);
		singleCoreButton.setText("SingleCore");
		singleCoreButton.setSelection(true);
		
		// "MultiCore" button
		multiCoreButton = new Button(this, SWT.RADIO);
		multiCoreButton.setText("MultiCore");
		
		// "Fabrication" button
		fabricationButton = new Button(this, SWT.RADIO);
		fabricationButton.setText("Fabrication");
		
	}
	
	public TemplateType getType() {
		if (singleCoreButton.getSelection()) {
			return TemplateType.SINGLE_CORE;
		}
		else if (multiCoreButton.getSelection()) {
			return TemplateType.MULTI_CORE;
		}
		return TemplateType.FABRICATION;
	}
	
}
