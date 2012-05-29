package name.slide.pureclipse.actions.editor;

import name.slide.pureclipse.dialogs.RetrieveInstanceDialog;
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
public class RetrieveInstance extends AbstractSnippetAction {

	private static final String TEMPLATE_FABRICATION = "var %1s : %2s = %3s(retrieve%4s(%5s.NAME));";
	private static final String TEMPLATE = "var %1s : %2s = %3s(facade.retrieve%4s(%5s.NAME));";

	@Override
	public void run(IAction action) {
		if (! LicenseManager.getInstance().checkLicense()) {
			return;
		}
		Shell parent = PlatformUtils.getShell();
		RetrieveInstanceDialog dialog = new RetrieveInstanceDialog(parent);
		if (dialog.open() == Dialog.OK) {
			String snippet = formatSnippet(dialog.getComponentType(), 
					dialog.getSelectedInstance(), dialog.isFabricationUsed());
			try {
				insertSnippet(snippet);
			}
			catch (BadLocationException e) {
				e.printStackTrace();
				MessageDialog.openError(parent, "Error", "Retrieve instance error. Try again");
			}
		}
	}
	
	private String formatSnippet(String componentType, String selectedInstance, boolean fabrication) {
		StringBuilder var = new StringBuilder();
		for (char ch : selectedInstance.toCharArray()) {
			if (Character.isUpperCase(ch)) {
				var.append(ch);
			}
		}
		if (var.length() == 0) {
			var.append(selectedInstance.charAt(0));
		}
		String template = TEMPLATE;
		if (fabrication) {
			template = TEMPLATE_FABRICATION;
		}
		return String.format(template, var.toString().toLowerCase(), selectedInstance, 
				selectedInstance, componentType, selectedInstance) + "\n";
	}

}
