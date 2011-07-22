package name.slide.pureclipse.wizards;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import name.slide.pureclipse.types.TemplateType;
import name.slide.pureclipse.utils.StringUtils;
import name.slide.pureclipse.utils.TemplateKey;
import name.slide.pureclipse.utils.TemplateProcessor;

import org.eclipse.jface.dialogs.MessageDialog;

/**
 * 
 * @author Andrey Stolyarchuk
 *
 */
public class CommandWizard extends AbstractCreateFileWizard {
	
	private static final String TEMPLATE_RESOURCE_SINGLECORE = "templates/Singlecore_Command.as";
	private static final String TEMPLATE_RESOURCE_MULTICORE = "templates/Multicore_Command.as";
	private static final String TEMPLATE_RESOURCE_FABRICATION = "templates/Fabrication_Command.as";
	private CommandWizardPage page;

	public CommandWizard() {
		super("Create new Command");
	}
	
	@Override
	public void addPages() {
		page = new CommandWizardPage(selection);
		addPage(page);
	}
	
	@Override
	protected InputStream createContent() {
		Map<TemplateKey, String> params = new HashMap<TemplateKey, String>();
		params.put(TemplateKey.PACKAGE_NAME, StringUtils.extractPackage(page.getContainerName()));
		params.put(TemplateKey.CLASS_NAME, page.getFileName());
		InputStream result = new ByteArrayInputStream("".getBytes());
		try {
			String resource = TEMPLATE_RESOURCE_SINGLECORE;
			if (page.getType() == TemplateType.MULTI_CORE) {
				resource = TEMPLATE_RESOURCE_MULTICORE;
			}
			else if (page.getType() == TemplateType.FABRICATION) {
				resource = TEMPLATE_RESOURCE_FABRICATION;
			}
			result = TemplateProcessor.process(getClass().getClassLoader().getResourceAsStream(resource), params);
		}
		catch (Exception e) {
			MessageDialog.openError(getShell(), "Error", e.getMessage());
		}
		return result;
	}

	@Override
	protected String getContainerName() {
		return page.getContainerName();
	}

	@Override
	protected String getFileName() {
		return page.getFileName();
	}

}