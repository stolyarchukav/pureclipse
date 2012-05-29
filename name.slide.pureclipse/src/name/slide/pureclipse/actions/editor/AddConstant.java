package name.slide.pureclipse.actions.editor;

import name.slide.pureclipse.dialogs.AddConstantDialog;
import name.slide.pureclipse.license.LicenseManager;
import name.slide.pureclipse.utils.PlatformUtils;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.swt.widgets.Shell;

/**
 * 
 * @author Andrey Stolyarchuk
 *
 */
public class AddConstant extends AbstractSnippetAction {

	@Override
	public void run(IAction action) {
		if (! LicenseManager.getInstance().checkLicense()) {
			return;
		}
		Shell parent = PlatformUtils.getShell();
		AddConstantDialog dialog = new AddConstantDialog(parent);
		if (dialog.open() == Dialog.OK) {
			String snippet = dialog.getResultText();
			snippet = snippet + "\n";
			try {
				insertSnippet(snippet);
			}
			catch (BadLocationException e) {
				e.printStackTrace();
				MessageDialog.openError(parent, "Error", "Add constant error. Try again");
			}
		}
	}
	
}
