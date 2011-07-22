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
public class MediatorForThisViewWizard extends AbstractCreateFileWizard {
	
	private static final String TEMPLATE_RESOURCE_SINGLECORE = "templates/Singlecore_Mediator.as";
	private static final String TEMPLATE_RESOURCE_MULTICORE = "templates/Multicore_Mediator.as";
	private static final String TEMPLATE_RESOURCE_FABRICATION = "templates/Fabrication_Mediator.as";
	private MediatorForThisViewWizardPage page;

	public MediatorForThisViewWizard() {
		super("New mediator");
	}
	
	@Override
	public void addPages() {
		page = new MediatorForThisViewWizardPage(selection);
		addPage(page);
	}
	
	@Override
	protected InputStream createContent() {
		Map<TemplateKey, String> params = new HashMap<TemplateKey, String>();
		String packageName = StringUtils.extractPackage(page.getContainerName());
		params.put(TemplateKey.PACKAGE_NAME, StringUtils.getUpperPackage(packageName));
		params.put(TemplateKey.CLASS_NAME, page.getFileName());
		params.put(TemplateKey.NAME, page.getTargetView());
		params.put(TemplateKey.SELECTED_VIEW_CLASS_PATH, packageName + "." + page.getTargetView());
		params.put(TemplateKey.SELECTED_VIEW_CLASS, page.getTargetView());
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
		String packageName = StringUtils.extractPackage(page.getContainerName());
		if (packageName.equals(StringUtils.getUpperPackage(packageName))) {
			return page.getContainerName();
		}
		return StringUtils.getUpperContainer(page.getContainerName());
	}

	@Override
	protected String getFileName() {
		return page.getFileName();
	}
	
}