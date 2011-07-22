package name.slide.pureclipse.wizards;

import name.slide.pureclipse.types.TemplateType;
import name.slide.pureclipse.types.TemplateTypeComposite;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.part.DrillDownComposite;

/**
 * 
 * @author Andrey Stolyarchuk
 *
 */
public abstract class AbstractWizardPage extends WizardPage {

	private ISelection selection;
	private Text creatingFileName;
	private Text creatingName;
	private String containerName;
	private TemplateTypeComposite templateTypeComposite;
	
	public AbstractWizardPage(IStructuredSelection selection, String pageName) {
		super(pageName);
		this.selection = selection;
	}
	
	protected abstract String getCreatingNameLabel();
	protected abstract String getCreatingFileLabel();
	protected abstract String getCreatingDefaultName();
	protected abstract String getCreatingPatternName();
	
	@Override
	public void createControl(Composite parent) {
		
		/* Initialize dialog */
		initializeDialogUnits(parent);
		
		/* Top level composite */
		Composite topLevel = new Composite(parent, SWT.NONE);
		GridLayout topLayout = new GridLayout(2, false);
		topLayout.verticalSpacing = 20;
		topLevel.setLayout(topLayout);
		
		// "Name" label
		Label labelName = new Label(topLevel, SWT.NONE);
		labelName.setText(getCreatingNameLabel());
		
		// "Name" text
		creatingName = new Text(topLevel, SWT.BORDER | SWT.SINGLE);
		creatingName.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		creatingName.setText(getCreatingDefaultName());
		creatingName.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				creatingFileName.setText(creatingName.getText() + getCreatingPatternName());
			}
		});
		
		// "File name" label
		Label labelFileName = new Label(topLevel, SWT.NONE);
		labelFileName.setText(getCreatingFileLabel());
		
		// "File name" text
		creatingFileName = new Text(topLevel, SWT.BORDER | SWT.SINGLE);
		creatingFileName.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		creatingFileName.setText(getCreatingDefaultName() + getCreatingPatternName());
		creatingFileName.setEnabled(false);
		
		/* Folder navigator */
		
		createNavigator(topLevel);
		
		/* Template type composite */
		templateTypeComposite = new TemplateTypeComposite(topLevel, SWT.NONE);
		
		/* Common wizard settings */
		setErrorMessage(null);
		setMessage(null);
		setControl(topLevel);
		
		/* Customizing by selection */
		
		changeSelection(selection);
	}
	
	private void createNavigator(Composite topLevel) {
		
		/* Drill down composite */
		DrillDownComposite drillDown = new DrillDownComposite(topLevel, SWT.BORDER);
		GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, true);
		layoutData.horizontalSpan = 2;
		drillDown.setLayoutData(layoutData);

		/* Tree viewer */
		final TreeViewer treeViewer = new TreeViewer(drillDown, SWT.NONE);
		drillDown.setChildTree(treeViewer);
		ITreeContentProvider cp = new WorkbenchContentProvider();
		treeViewer.setContentProvider(cp);
		treeViewer.setLabelProvider(WorkbenchLabelProvider.getDecoratingWorkbenchLabelProvider());
		treeViewer.setComparator(new ViewerComparator());
		treeViewer.setUseHashlookup(true);
		treeViewer.setInput(ResourcesPlugin.getWorkspace());
		IAdaptable adaptable = (IAdaptable)((IStructuredSelection)selection).getFirstElement();
		Object data = adaptable.getAdapter(IResource.class);
		if (data != null) {
			selection = new StructuredSelection(data);
			treeViewer.setSelection(selection);
		}
		
		// Selection listener
		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				changeSelection(event.getSelection());
			}
		});
		
		// Double click listener
		treeViewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				ISelection selection = event.getSelection();
				if (selection instanceof IStructuredSelection) {
					Object item = ((IStructuredSelection) selection).getFirstElement();
					if (item != null) {
						if (treeViewer.getExpandedState(item)) {
							treeViewer.collapseToLevel(item, 1);
						}
						else {
							treeViewer.expandToLevel(item, 1);
						}
					}
				}
			}
		});
	}
	
	private void changeSelection(ISelection selection) {
		if (selection != null && ! selection.isEmpty() && selection instanceof IStructuredSelection) {
			IStructuredSelection ssel = (IStructuredSelection) selection;
			if (ssel.size() == 1) {
				Object obj = ssel.getFirstElement();
				if (obj instanceof IFile) {
					IFile file = (IFile) obj;
					String name = file.getName();
					if (name.endsWith(".as")) {
						name = name.replace(".as", "");
					}
					creatingFileName.setText(name);
					if (name.endsWith(getCreatingPatternName())) {
						name = name.substring(0, name.length() - getCreatingPatternName().length());
					}
					creatingName.setText(name);
					containerName = file.getParent().getFullPath().toString();
				}
				else if (obj instanceof IContainer) {
					IContainer container = (IContainer) obj;
					containerName = container.getFullPath().toString();
				}
			}
		}
	}

	public String getFileName() {
		return creatingFileName.getText();
	}

	public String getContainerName() {
		return containerName;
	}
	
	public String getTargetView() {
		return creatingFileName.getText();
	}
	
	public TemplateType getType() {
		return templateTypeComposite.getType();
	}
	
}
