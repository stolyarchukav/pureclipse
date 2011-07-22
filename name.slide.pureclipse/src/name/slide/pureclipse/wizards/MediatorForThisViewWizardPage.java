package name.slide.pureclipse.wizards;

import name.slide.pureclipse.types.TemplateType;
import name.slide.pureclipse.types.TemplateTypeComposite;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * 
 * @author Andrey Stolyarchuk
 *
 */
public class MediatorForThisViewWizardPage extends WizardPage {

	private ISelection selection;
	private Text forViewText;
	private Text mediatorNameText;
	private String containerName;
	private TemplateTypeComposite templateTypeComposite;

	public MediatorForThisViewWizardPage(IStructuredSelection selection) {
		super("New Mediator");
		setDescription("Create new Mediator wizard");
		this.selection = selection;
	}
	
	@Override
	public void createControl(Composite parent) {
		
		/* Initialize dialog */
		initializeDialogUnits(parent);
		
		/* Top level composite */
		Composite topLevel = new Composite(parent, SWT.NONE);
		GridLayout topLayout = new GridLayout(2, false);
		topLayout.verticalSpacing = 20;
		topLevel.setLayout(topLayout);
		
		// "For view" label
		Label forViewLabel = new Label(topLevel, SWT.NONE);
		forViewLabel.setText("For View :");
		
		// "For view" text
		forViewText = new Text(topLevel, SWT.BORDER | SWT.SINGLE | SWT.READ_ONLY);
		forViewText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		forViewText.setText("View");
		
		// "Mediator name" label
		Label mediatorNameLabel = new Label(topLevel, SWT.NONE);
		mediatorNameLabel.setText("Mediator Name :");
		
		// "Mediator name" text
		mediatorNameText = new Text(topLevel, SWT.BORDER | SWT.SINGLE);
		mediatorNameText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		mediatorNameText.setText("ViewMediator");
		
		/* Template type composite */
		templateTypeComposite = new TemplateTypeComposite(topLevel, SWT.NONE);
		
		/* Common wizard settings */
		setErrorMessage(null);
		setMessage(null);
		setControl(topLevel);
		
		/* Customizing by selection */
		if (selection != null && ! selection.isEmpty() && selection instanceof IStructuredSelection) {
			IStructuredSelection ssel = (IStructuredSelection) selection;
			if (ssel.size() == 1) {
				Object obj = ssel.getFirstElement();
				if (obj instanceof IFile) {
					IFile file = (IFile) obj;
					String name = file.getName();
					if (! fileType(name, "as") && ! fileType(name, "mxml")) {
						forViewText.setEditable(true);
					}
					containerName = file.getParent().getFullPath().toString();
				}
				else {
					forViewText.setEditable(true);
					if (obj instanceof IContainer) {
						containerName = ((IContainer) obj).getFullPath().toString();
					}
					else if (obj instanceof IResource) {
						containerName = ((IResource) obj).getFullPath().toString();
					}
					else if (obj instanceof IAdaptable) {
						IAdaptable adaptable = (IAdaptable) obj;
						IResource resource = (IResource) adaptable.getAdapter(IResource.class);
						if (resource != null) {
							containerName = resource.getFullPath().removeLastSegments(1).toString();
						}
						else {
							containerName = "/";
						}
					}
				}
			}
		}
	}

	private boolean fileType(String name, String ext) {
		String end = "." + ext;
		if (name.endsWith(end)) {
			name = name.replace(end, "");
			forViewText.setText(name);
			mediatorNameText.setText(name + "Mediator");
			return true;
		}
		return false;
	}
	
	public String getFileName() {
		return mediatorNameText.getText();
	}

	public String getContainerName() {
		return containerName;
	}
	
	public String getTargetView() {
		return forViewText.getText();
	}
	
	public TemplateType getType() {
		return templateTypeComposite.getType();
	}
	
}
