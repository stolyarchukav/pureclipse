package name.slide.pureclipse.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;

public class ProxyWizardPage extends AbstractWizardPage {

	public ProxyWizardPage(IStructuredSelection selection) {
		super(selection, "ProxyWizardPage");
		setTitle("New Proxy");
		setDescription("Create new Proxy wizard");
	}

	@Override
	protected String getCreatingDefaultName() {
		return "SomeData";
	}

	@Override
	protected String getCreatingFileLabel() {
		return "File name :";
	}

	@Override
	protected String getCreatingNameLabel() {
		return "Proxy name :";
	}

	@Override
	protected String getCreatingPatternName() {
		return "Proxy";
	}

}
