package name.slide.pureclipse.actions.wizard;

import name.slide.pureclipse.wizards.AbstractCreateFileWizard;
import name.slide.pureclipse.wizards.MediatorForThisViewWizard;

public class MediatorForThisView extends AbstractCreateFileWizardAction {

	@Override
	protected AbstractCreateFileWizard getWizard() {
		return new MediatorForThisViewWizard();
	}

}
