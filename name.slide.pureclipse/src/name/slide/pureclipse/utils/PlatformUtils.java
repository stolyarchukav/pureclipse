package name.slide.pureclipse.utils;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

/**
 * 
 * @author Andrey Stolyarchuk
 *
 */
public class PlatformUtils {

	public static Shell getShell() {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
	}
	
}
