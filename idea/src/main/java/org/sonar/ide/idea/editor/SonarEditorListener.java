package org.sonar.ide.idea.editor;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.EditorFactoryEvent;
import com.intellij.openapi.editor.event.EditorFactoryListener;
import com.intellij.openapi.editor.markup.HighlighterLayer;
import com.intellij.openapi.editor.markup.LineMarkerRenderer;
import com.intellij.openapi.editor.markup.MarkupModel;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.ide.idea.utils.IdeaResourceUtils;
import org.sonar.ide.idea.utils.SonarUtils;
import org.sonar.ide.shared.CoverageLoader;
import org.sonar.ide.shared.ViolationUtils;
import org.sonar.ide.shared.ViolationsLoader;
import org.sonar.wsclient.Sonar;
import org.sonar.wsclient.services.Violation;

import java.awt.*;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Evgeny Mandrikov
 */
public class SonarEditorListener implements EditorFactoryListener {
  private static boolean experimental = false;

  private static final Logger LOG = LoggerFactory.getLogger(SonarEditorListener.class);

  private static final Key<Boolean> SONAR_DATA_KEY = Key.create("SONAR_DATA_KEY");

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
      removeSonarHighlighters(editor.getDocument().getMarkupModel(project));
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
      Sonar sonar = SonarUtils.getSonar(project);

      String resourceKey = IdeaResourceUtils.getInstance().getFileKey(psiJavaFile);

      // Violations
      String text = psiJavaFile.getText();
      Collection<Violation> violations = ViolationsLoader.getViolations(
          sonar,
          resourceKey,
          text
      );
      Map<Integer, List<Violation>> violationsByLine = ViolationUtils.splitByLines(violations);
      MarkupModel markupModel = document.getMarkupModel(project);
      for (Map.Entry<Integer, List<Violation>> entry : violationsByLine.entrySet()) {
        addHighlighter(markupModel, entry.getKey() - 1, entry.getValue());
      }
      // Coverage
      if (experimental) {
        Map<Integer, String> coverage = CoverageLoader.getCoverageLineHits(sonar, resourceKey);
        for (Map.Entry<Integer, String> entry : coverage.entrySet()) {
          addCoverageHighlighter(markupModel, entry.getKey() - 1, entry.getValue());
        }
      }
    }
  }

  private void addCoverageHighlighter(MarkupModel markupModel, int line, String value) {
    RangeHighlighter highlighter = addSonarHighlighter(markupModel, line, HighlighterLayer.ERROR + 1);
    final int intValue = Integer.parseInt(value);
    highlighter.setLineMarkerRenderer(new LineMarkerRenderer() {
      @Override
      public void paint(Editor editor, Graphics graphics, Rectangle rectangle) {
        graphics.setColor(intValue == 0 ? Color.RED : Color.GREEN);
        graphics.fillRect(rectangle.x, rectangle.y, 10, 10);
      }
    });
  }

  protected void removeSonarHighlighters(MarkupModel markupModel) {
    for (RangeHighlighter rangeHighlighter : markupModel.getAllHighlighters()) {
      Boolean marker = rangeHighlighter.getUserData(SONAR_DATA_KEY);
      if (marker != null) {
        markupModel.removeHighlighter(rangeHighlighter);
      }
    }
  }

  protected RangeHighlighter addSonarHighlighter(MarkupModel markupModel, int line, int layer) {
    RangeHighlighter highlighter = markupModel.addLineHighlighter(line, layer, null);
//    RangeHighlighter highlighter = markupModel.addRangeHighlighter(startOffset, endOffset);
    highlighter.putUserData(SONAR_DATA_KEY, true);
    highlighter.setGreedyToRight(true);
    highlighter.setGreedyToLeft(true);
    return highlighter;
  }

  protected void addHighlighter(MarkupModel markupModel, int line, List<Violation> violations) {
    RangeHighlighter highlighter = addSonarHighlighter(markupModel, line, HighlighterLayer.ERROR + 1);
    ViolationGutterIconRenderer renderer = new ViolationGutterIconRenderer(violations);
    highlighter.setGutterIconRenderer(renderer);
    highlighter.setErrorStripeMarkColor(renderer.getErrorStripeMarkColor());
    highlighter.setErrorStripeTooltip(renderer.getTooltipText());
  }
}
