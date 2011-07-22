package name.slide.pureclipse.help;

import name.slide.pureclipse.license.LicenseManager;
import name.slide.pureclipse.license.TrialDialog;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;

/**
 * 
 * @author Andrey Stolyarchuk
 *
 */
public class Help implements IViewActionDelegate {

	@Override
	public void run(IAction action) {
		TrialDialog dialog = new TrialDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
				LicenseManager.getInstance().getDaysLeft(), LicenseManager.getInstance().getSerialNumber(),
				false);
		dialog.open();
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		/*Not needed implementation*/
	}
	
	@Override
	public void init(IViewPart view) {
		/*Not needed implementation*/
	}

}
