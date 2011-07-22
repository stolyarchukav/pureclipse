package name.slide.pureclipse.actions.wizard;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import name.slide.pureclipse.license.LicenseManager;
import name.slide.pureclipse.utils.StringUtils;
import name.slide.pureclipse.utils.TemplateKey;
import name.slide.pureclipse.utils.TemplateProcessor;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;

/**
 * 
 * @author Andrey Stolyarchuk
 *
 */
public class BuildStructure implements IViewActionDelegate {
	
	private static final String TEMPLATE_RESOURCE = "templates/Constants.as";

	private ISelection selection;

	@Override
	public void run(IAction action) {
		if (! LicenseManager.getInstance().checkLicense()) {
			return;
		}
		if (selection != null && ! selection.isEmpty() && selection instanceof IStructuredSelection) {
			IStructuredSelection ssel = (IStructuredSelection) selection;
			Object obj = ssel.getFirstElement();
			if (obj instanceof IAdaptable) {
				IResource resource = (IResource)((IAdaptable)obj).getAdapter(IResource.class);
				if (resource == null) {
					MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
							"Error", "Can't build MVC structure in this place");
				}
				else {
					try {
						IPath path = resource.getFullPath();
						IFolder folder = ResourcesPlugin.getWorkspace().getRoot().getFolder(path.append("model"));
						folder.create(IResource.FORCE, true, null);
						IPath pathView = path.append("view");
						folder = ResourcesPlugin.getWorkspace().getRoot().getFolder(pathView);
						folder.create(IResource.FORCE, true, null);
						folder = ResourcesPlugin.getWorkspace().getRoot().getFolder(pathView.append("components"));
						folder.create(IResource.FORCE, true, null);
						folder = ResourcesPlugin.getWorkspace().getRoot().getFolder(path.append("controller"));
						folder.create(IResource.FORCE, true, null);
						String className = path.lastSegment() + "Constants";
						String start = String.valueOf(className.charAt(0));
						className = className.replaceFirst(start, start.toUpperCase());
						IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path.append(className + ".as"));
						Map<TemplateKey, String> params = new HashMap<TemplateKey, String>();
						params.put(TemplateKey.PACKAGE_NAME, StringUtils.extractPackage(path.toString()));
						params.put(TemplateKey.CLASS_NAME, className);
						InputStream is = new ByteArrayInputStream("".getBytes());
						String res = TEMPLATE_RESOURCE;
						is = TemplateProcessor.process(getClass().getClassLoader().getResourceAsStream(res), params);
						file.create(is, IResource.NONE, null);
					}
					catch (Exception e) {
						MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
								"Error", "Can't build MVC structure. Cause: " +  e.getMessage());
					}
				}
			}
		}
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
