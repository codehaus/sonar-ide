package org.sonar.ide.idea.editor;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.markup.HighlighterLayer;
import com.intellij.openapi.editor.markup.MarkupModel;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.sonar.ide.idea.utils.SonarUtils;
import org.sonar.ide.shared.ViolationUtils;
import org.sonar.ide.shared.ViolationsLoader;
import org.sonar.wsclient.Sonar;
import org.sonar.wsclient.services.Violation;

import javax.swing.*;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Evgeny Mandrikov
 */
public class ShowViolationsTask extends Task.Backgroundable {

  protected static final Key<Boolean> SONAR_DATA_KEY = Key.create("SONAR_DATA_KEY");

  private Document document;
  private String resourceKey;

  public ShowViolationsTask(@Nullable Project project, Document document, String resourceKey) {
    super(project, "Loading violations from Sonar");
    this.document = document;
    this.resourceKey = resourceKey;
  }

  @Override
  public void run(@NotNull ProgressIndicator progressIndicator) {
    Project project = getProject();
    final Sonar sonar = SonarUtils.getSonar(project);

    // Violations
    String text = document.getText();
    final Collection<Violation> violations = ViolationsLoader.getViolations(sonar, resourceKey, text);
    final Map<Integer, List<Violation>> violationsByLine = ViolationUtils.splitByLines(violations);
    final MarkupModel markupModel = document.getMarkupModel(project);
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        for (Map.Entry<Integer, List<Violation>> entry : violationsByLine.entrySet()) {
          addHighlighter(markupModel, entry.getKey() - 1, entry.getValue());
        }
      }
    });
  }

  /*
  protected static void addCoverageHighlighter(MarkupModel markupModel, int line, String value) {
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
  */

  protected static RangeHighlighter addSonarHighlighter(MarkupModel markupModel, int line, int layer) {
    RangeHighlighter highlighter = markupModel.addLineHighlighter(line, layer, null);
//    RangeHighlighter highlighter = markupModel.addRangeHighlighter(startOffset, endOffset);
    highlighter.putUserData(SONAR_DATA_KEY, true);
    highlighter.setGreedyToRight(true);
    highlighter.setGreedyToLeft(true);
    return highlighter;
  }

  protected static void addHighlighter(MarkupModel markupModel, int line, List<Violation> violations) {
    RangeHighlighter highlighter = addSonarHighlighter(markupModel, line, HighlighterLayer.ERROR + 1);
    ViolationGutterIconRenderer renderer = new ViolationGutterIconRenderer(violations);
    highlighter.setGutterIconRenderer(renderer);
    highlighter.setErrorStripeMarkColor(renderer.getErrorStripeMarkColor());
    highlighter.setErrorStripeTooltip(renderer.getTooltipText());
  }

  protected static void removeSonarHighlighters(MarkupModel markupModel) {
    for (RangeHighlighter rangeHighlighter : markupModel.getAllHighlighters()) {
      Boolean marker = rangeHighlighter.getUserData(ShowViolationsTask.SONAR_DATA_KEY);
      if (marker != null) {
        markupModel.removeHighlighter(rangeHighlighter);
      }
    }
  }
}
