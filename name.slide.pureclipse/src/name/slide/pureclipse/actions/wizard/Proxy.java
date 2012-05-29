package name.slide.pureclipse.actions.wizard;

import name.slide.pureclipse.wizards.AbstractCreateFileWizard;
import name.slide.pureclipse.wizards.ProxyWizard;

public class Proxy extends AbstractCreateFileWizardAction {

	@Override
	protected AbstractCreateFileWizard getWizard() {
		return new ProxyWizard();
	}

}
