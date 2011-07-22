package name.slide.pureclipse.actions.wizard;

import name.slide.pureclipse.license.LicenseManager;
import name.slide.pureclipse.wizards.AbstractCreateFileWizard;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;

/**
 * 
 * @author Andrey Stolyarchuk
 *
 */
public abstract class AbstractCreateFileWizardAction implements IViewActionDelegate {

	private ISelection selection;
	
	protected abstract AbstractCreateFileWizard getWizard();
	
	@Override
	public void run(IAction action) {
		if (! LicenseManager.getInstance().checkLicense()) {
			return;
		}
		AbstractCreateFileWizard wizard = getWizard();
		wizard.init(PlatformUI.getWorkbench(), (IStructuredSelection)selection);
		WizardDialog dialog = new WizardDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), wizard);
		dialog.create();
		dialog.open();
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;
	}
	
	@Override
	public void init(IViewPart view) {
		/*Not needed implementation*/
	}
	
}
