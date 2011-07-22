package name.slide.pureclipse.dialogs;

import java.util.ArrayList;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

/**
 * 
 * @author Andrey Stolyarchuk
 *
 */
public class RetrieveInstanceDialog extends Dialog {
	
	private static final String TYPE_LABEL = "Type:";
	private static final String FILTER_LABEL = "Select instance to retrieve:";
	private static final String[] ITEMS = new String[] {"Proxy", "Mediator"};
	
	private String componentType;
	private String selectedInstance;
	private java.util.List<IResource> resources;
	private boolean fabrication;
	private List list;
	private Button fabricationButton;
	
	public RetrieveInstanceDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected Control createContents(Composite parent) {
		getShell().setText("Retrieve instance...");
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout topLayout = new GridLayout(2, false);
		topLayout.verticalSpacing = 10;
		topLayout.horizontalSpacing = 10;
		topLayout.marginHeight = 10;
		topLayout.marginWidth = 10;
		composite.setLayout(topLayout);
		
		Label labelType = new Label(composite, SWT.NONE);
	    labelType.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, true, false));
	    labelType.setText(TYPE_LABEL);
	    
	    Label labelFilter = new Label(composite, SWT.NONE);
	    labelFilter.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, true, false));
	    labelFilter.setText(FILTER_LABEL);
	    
	    final Combo comboType = new Combo(composite, SWT.READ_ONLY);
	    comboType.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, true, false));
	    comboType.setItems(ITEMS);
	    comboType.select(0);
	    
	    final Text textFilter = new Text(composite, SWT.BORDER);
	    GridData textFilterGD = new GridData(SWT.FILL, SWT.FILL, true, false);
	    textFilterGD.minimumWidth = 200;
	    textFilter.setLayoutData(textFilterGD);
	    
	    list = new List(composite, SWT.BORDER | SWT.V_SCROLL);
	    GridData listGD = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
	    listGD.heightHint = 300;
	    list.setLayoutData(listGD);
	    
	    // "Fabrication is used" button
	    fabricationButton = new Button(composite, SWT.CHECK);
	    fabricationButton.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, true, true, 2, 1));
	    fabricationButton.setText("Fabrication is used");
	    
	    loadResources();
	    changeComponentType(comboType, textFilter);
	    
	    comboType.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				changeComponentType(comboType, textFilter);
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				changeComponentType(comboType, textFilter);
			}
		});
	    
	    textFilter.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				filterAndFillList(comboType, textFilter, list);
			}
		});
	    
	    list.addMouseListener(new MouseListener() {
			@Override
			public void mouseUp(MouseEvent e) {
			}
			@Override
			public void mouseDown(MouseEvent e) {
			}
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				dialogComplete();
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
				dialogComplete();
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

	private void changeComponentType(Combo comboType, Text textFilter) {
		filterAndFillList(comboType, textFilter, list);
	    componentType = comboType.getItem(comboType.getSelectionIndex());
	}
	
	private void dialogComplete() {
		selectedInstance = list.getItem(list.getSelectionIndex());
		fabrication = fabricationButton.getSelection();
		okPressed();
	}
	
	private void filterAndFillList(Combo combo, Text text, List list) {
		list.setItems(filterResources(combo.getItem(combo.getSelectionIndex()), text.getText()));
	}
	
	private void loadResources() {
		resources = new ArrayList<IResource>();
		try {
			IResource[] ress = ResourcesPlugin.getWorkspace().getRoot().members();
			loadResources(ress);
		}
		catch (CoreException e1) {
			e1.printStackTrace();
			MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
					"Error", "Retrieve instance error. Try again");
		}
	}

	private String[] filterResources(String type, String filter) {
		java.util.List<String> filtered = new ArrayList<String>();
		for (IResource res : resources) {
			String name = res.getName();
			name = name.substring(0, name.lastIndexOf("."));
			if (name.toUpperCase().contains(type.toUpperCase()) && 
					name.toUpperCase().contains(filter.toUpperCase())) {
				filtered.add(name);
			}
		}
		return filtered.toArray(new String[0]);
	}
	
	private void loadResources(IResource[] ress) throws CoreException {
		for (IResource res : ress) {
			if (res instanceof IContainer) {
				IContainer container = (IContainer) res;
				if (container.isAccessible()) {
					loadResources((container).members());
				}
			}
			else if (res.getName().endsWith(".as")) {
					resources.add(res);
			}
		}
	}
	
	@Override
	protected void handleShellCloseEvent() {
		super.handleShellCloseEvent();
		cancelPressed();
	}

	public String getSelectedInstance() {
		return selectedInstance;
	}
	
	public String getComponentType() {
		return componentType;
	}
	
	public boolean isFabricationUsed() {
		return fabrication;
	}
	
}
