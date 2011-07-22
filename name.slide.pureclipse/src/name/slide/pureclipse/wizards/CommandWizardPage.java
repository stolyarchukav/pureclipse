package name.slide.pureclipse.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;

public class CommandWizardPage extends AbstractWizardPage {

	public CommandWizardPage(IStructuredSelection selection) {
		super(selection, "CommandWizardPage");
		setTitle("New Command");
		setDescription("Create new Command wizard");
	}

	@Override
	protected String getCreatingDefaultName() {
		return "DoSomething";
	}

	@Override
	protected String getCreatingFileLabel() {
		return "File name :";
	}

	@Override
	protected String getCreatingNameLabel() {
		return "Command name :";
	}

	@Override
	protected String getCreatingPatternName() {
		return "Command";
	}
	
}
