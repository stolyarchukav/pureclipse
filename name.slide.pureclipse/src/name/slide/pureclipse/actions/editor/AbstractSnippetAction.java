package name.slide.pureclipse.actions.editor;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.editors.text.TextEditor;

/**
 * 
 * @author Andrey Stolyarchuk
 *
 */
public abstract class AbstractSnippetAction implements IEditorActionDelegate {

	private IEditorPart targetEditor;
	
	@Override
	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
		this.targetEditor = targetEditor;
	}
	
	void insertSnippet(String snippet) throws BadLocationException {
		TextEditor editor = (TextEditor) targetEditor;
		ISelection selection = editor.getSelectionProvider().getSelection();
		int offset = ((ITextSelection) selection).getOffset();
		int length = ((ITextSelection) selection).getLength();
		IDocument document = editor.getDocumentProvider().getDocument(editor.getEditorInput());
		document.replace(offset, length, snippet);
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		/*Not needed implementation*/
	}

}
