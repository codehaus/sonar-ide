package org.sonar.ide.idea.editor;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.EditorFactoryEvent;
import com.intellij.openapi.editor.event.EditorFactoryListener;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.ide.idea.utils.IdeaResourceUtils;

/**
 * @author Evgeny Mandrikov
 */
public class SonarEditorListener implements EditorFactoryListener {
  private static final Logger LOG = LoggerFactory.getLogger(SonarEditorListener.class);

  public void editorCreated(EditorFactoryEvent editorFactoryEvent) {
    LOG.info("Editor created");
    try {
      Editor editor = editorFactoryEvent.getEditor();
      Document document = editor.getDocument();
      Project project = editor.getProject();

      processFile(project, document);
    } catch (Throwable e) { // NOSONAR
      // This is a critical part in file opening procedure in IDEA.
      // Even if we can't do something file should be opened anyway.
      LOG.error(e.getMessage(), e);
    }
  }

  public void editorReleased(EditorFactoryEvent editorFactoryEvent) {
    LOG.info("Editor released");
    try {
      Editor editor = editorFactoryEvent.getEditor();
      Project project = editor.getProject();
      ShowViolationsTask.removeSonarHighlighters(editor.getDocument().getMarkupModel(project));
    } catch (Throwable e) { // NOSONAR
      // Even if we can't do something editor should be released.
      LOG.error(e.getMessage(), e);
    }
  }

  protected void processFile(Project project, Document document) {
    VirtualFile file = FileDocumentManager.getInstance().getFile(document);
    PsiFile psiFile = PsiManager.getInstance(project).findFile(file);
    if (psiFile instanceof PsiJavaFile) {
      PsiJavaFile psiJavaFile = (PsiJavaFile) psiFile;
      final String resourceKey = IdeaResourceUtils.getInstance().getFileKey(psiJavaFile);
      new ShowViolationsTask(project, document, resourceKey).queue();
    }
  }

}