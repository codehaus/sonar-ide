package org.sonar.ide.idea.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diff.*;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import org.apache.commons.lang.StringUtils;
import org.sonar.ide.idea.utils.IdeaResourceUtils;
import org.sonar.ide.idea.utils.actions.SonarAction;
import org.sonar.ide.idea.utils.actions.SonarActionUtils;
import org.sonar.ide.idea.vfs.SonarVirtualFile;
import org.sonar.wsclient.Sonar;
import org.sonar.wsclient.services.Source;
import org.sonar.wsclient.services.SourceQuery;

/**
 * TODO Experimental SONARIDE-97
 *
 * @author Evgeny Mandrikov
 */
public class OpenDiffToolAction extends SonarAction {

  @Override
  public void actionPerformed(AnActionEvent event) {
    Project project = SonarActionUtils.getProject(event);
    VirtualFile file = SonarActionUtils.getVirtualFile(event);
    Document doc1 = FileDocumentManager.getInstance().getDocument(file);
    PsiFile psiFile = PsiDocumentManager.getInstance(project).getPsiFile(doc1);
    // Load code from Sonar
    String resourceKey = IdeaResourceUtils.getInstance().getFileKey(psiFile);
    Sonar sonar = SonarActionUtils.getSonar(event);
    SourceQuery sourceQuery = new SourceQuery(resourceKey);
    Source source = sonar.find(sourceQuery);
    String content = StringUtils.join(source.getLines().toArray(), "\n");
    // Prepare content for diff
    SonarVirtualFile sonarVirtualFile = new SonarVirtualFile("Test.java", content.getBytes(), resourceKey);
    DiffContent sonarDiffContent = createDiffContent(project, sonarVirtualFile);
    Document doc2 = sonarDiffContent.getDocument();
    doc2.setReadOnly(true);
    // Show diff
    DiffRequest request = getDiffRequest(project, doc1, doc2);
    DiffManager.getInstance().getDiffTool().show(request);
  }

  private DiffRequest getDiffRequest(final Project project, Document doc1, Document doc2) {
    return getDiffRequest(project, new DocumentContent(doc2), new DocumentContent(doc1));
  }

  private DiffRequest getDiffRequest(final Project project, final DocumentContent doc1, final DocumentContent doc2) {
    return new DiffRequest(project) {
      @Override
      public DiffContent[] getContents() {
        return new DiffContent[]{doc1, doc2};
      }

      @Override
      public String[] getContentTitles() {
        return new String[]{"Sonar server", "Local copy"};
      }

      @Override
      public String getWindowTitle() {
        return "Diff"; // TODO
      }
    };
  }

  private DiffContent createDiffContent(final Project project, final VirtualFile virtualFile) {
    return new FileContent(project, virtualFile);
  }
}
