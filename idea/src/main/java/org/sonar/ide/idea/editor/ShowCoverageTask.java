package org.sonar.ide.idea.editor;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.markup.HighlighterLayer;
import com.intellij.openapi.editor.markup.MarkupModel;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.sonar.ide.api.Logs;
import org.sonar.ide.shared.coverage.CoverageData;
import org.sonar.ide.shared.coverage.CoverageLoader;

import java.awt.*;

/**
 * @author Evgeny Mandrikov
 */
public class ShowCoverageTask extends AbstractSonarTask {
  private static final Key<Boolean> SONAR_COVERAGE_DATA_KEY = Key.create("SONAR_COVERAGE_DATA_KEY");

  private static final Color UNCOVERED = new Color(250, 200, 200);
  private static final Color PARTIALLY_COVERED = Color.YELLOW;
  private static final Color FULLY_COVERED = Color.GREEN;

  public ShowCoverageTask(@Nullable Project project, Document document, String resourceKey) {
    super(project, "Loading violations from Sonar for " + resourceKey, document, resourceKey);
  }

  @Override
  public void run(@NotNull ProgressIndicator progressIndicator) {
    // Load coverage
    final CoverageData coverageData = CoverageLoader.getCoverage(getSonar(), getResourceKey());
    // Add to UI
    UIUtil.invokeLaterIfNeeded(new Runnable() {
      @Override
      public void run() {
        removeSonarHighlighters(getMarkupModel());
        for (int line = 0; line < getDocument().getLineCount(); line++) {
          CoverageData.CoverageStatus status = coverageData.getCoverageStatus(line + 1);
          switch (status) {
            case FULLY_COVERED:
              addCoverageHighlighter(line, FULLY_COVERED);
              break;
            case PARTIALLY_COVERED:
              addCoverageHighlighter(line, PARTIALLY_COVERED);
              break;
            case UNCOVERED:
              addCoverageHighlighter(line, UNCOVERED);
              break;
            case NO_DATA:
              break;
            default:
              Logs.INFO.error("Unknown coverage status for line {} in {}", line, getResourceKey());
              break;
          }
        }
      }
    });
  }

  protected void addCoverageHighlighter(int line, Color color) {
    TextAttributes attr = new TextAttributes();
    attr.setBackgroundColor(color);
    RangeHighlighter highlighter = getMarkupModel().addLineHighlighter(line, HighlighterLayer.FIRST, attr);
    highlighter.putUserData(SONAR_COVERAGE_DATA_KEY, true);
    /* TODO
    highlighter.setLineMarkerRenderer(new LineMarkerRenderer(){
      @Override
      public void paint(Editor editor, Graphics graphics, Rectangle rectangle) {
        graphics.setColor(Color.GREEN);
        graphics.fillRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
      }
    });
    */

    highlighter.setThinErrorStripeMark(false);
    highlighter.setGutterIconRenderer(new CoverageGutterIconRenderer(color));
    highlighter.setGreedyToRight(false);
    highlighter.setGreedyToLeft(true);
  }

  public static void removeSonarHighlighters(MarkupModel markupModel) {
    removeSonarHighlighters(markupModel, SONAR_COVERAGE_DATA_KEY);
  }
}
