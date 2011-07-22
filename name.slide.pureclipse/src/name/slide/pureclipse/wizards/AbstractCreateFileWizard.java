package name.slide.pureclipse.wizards;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

import name.slide.pureclipse.license.LicenseManager;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;


public abstract class AbstractCreateFileWizard extends Wizard implements INewWizard {

	private static final String PLUGIN_ID = "ru.plugins.puremvc";
	protected IStructuredSelection selection;
	
	protected AbstractCreateFileWizard() {
		this("Create new File");
	}
	
	protected AbstractCreateFileWizard(String title) {
		setWindowTitle(title);
		setNeedsProgressMonitor(true);
	}
	
	protected abstract String getContainerName();
	
	protected abstract String getFileName();
	
	protected abstract InputStream createContent();
	
	@Override
	public boolean performFinish() {
		try {
			final String containerName = getContainerName();
			String name = getFileName();
			if (! name.endsWith(".as")) {
				name = name.concat(".as");
			}
			final String fileName = name;
			final InputStream content = createContent();
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			IResource resource = root.findMember(new Path(containerName));
			if (!resource.exists() || !(resource instanceof IContainer)) {
				throwCoreException("Container \"" + containerName + "\" does not exist.");
			}
			IContainer container = (IContainer) resource;
			final IFile file = container.getFile(new Path(fileName));
			if (! file.exists() || MessageDialog.openQuestion(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
					"File already exists", "File with this name already exists. " +
					"Do you want to override it?")) {
				IRunnableWithProgress op = new IRunnableWithProgress() {
					public void run(IProgressMonitor monitor) throws InvocationTargetException {
						try {
							doFinish(file, monitor, content);
						}
						catch (CoreException e) {
							throw new InvocationTargetException(e);
						}
						finally {
							monitor.done();
						}
					}
				};
				getContainer().run(true, false, op);
			}
			else {
				return false;
			}
		}
		catch (InvocationTargetException e) {
			Throwable realException = e.getTargetException();
			MessageDialog.openError(getShell(), "Error", realException.getMessage());
			return false;
		}
		catch (Exception e) {
			MessageDialog.openError(getShell(), "Error", e.getMessage());
			return false;
		}
		return true;
	}
	
	private void doFinish(final IFile file, IProgressMonitor monitor, InputStream content) throws CoreException {
		monitor.beginTask("Creating " + file.getName(), 2);
		try {
			if (file.exists()) {
				file.setContents(content, true, true, monitor);
			}
			else {
				file.create(content, true, monitor);
			}
			content.close();
		}
		catch (IOException e) {
			throwCoreException("Can't create file \"" + file.getName() + "\".");
		}
		monitor.worked(1);
		monitor.setTaskName("Opening file for editing...");
		getShell().getDisplay().asyncExec(new Runnable() {
			public void run() {
				IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				try {
					IDE.openEditor(page, file, true);
				}
				catch (PartInitException e) {
				}
			}
		});
		monitor.worked(1);
	}
	
	private void throwCoreException(String message) throws CoreException {
		IStatus status = new Status(IStatus.ERROR, PLUGIN_ID, IStatus.OK, message, null);
		throw new CoreException(status);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		if (! LicenseManager.getInstance().checkLicense()) {
			performCancel();
			return;
		}
		this.selection = selection;
	}
	
}
