package name.slide.pureclipse.actions.wizard;

import name.slide.pureclipse.wizards.AbstractCreateFileWizard;
import name.slide.pureclipse.wizards.CommandWizard;

public class Command extends AbstractCreateFileWizardAction {

	@Override
	protected AbstractCreateFileWizard getWizard() {
		return new CommandWizard();
	}

}
