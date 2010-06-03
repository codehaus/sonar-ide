package org.sonar.ide.idea.editor;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.markup.MarkupModel;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.sonar.ide.idea.utils.SonarUtils;
import org.sonar.wsclient.Sonar;

/**
 * @author Evgeny Mandrikov
 */
public abstract class AbstractSonarTask extends Task.Backgroundable {
  private Document document;
  private String resourceKey;

  protected AbstractSonarTask(@Nullable Project project, @NotNull String title, Document document, String resourceKey) {
    super(project, title);
    this.document = document;
    this.resourceKey = resourceKey;
  }

  public Document getDocument() {
    return document;
  }

  public String getResourceKey() {
    return resourceKey;
  }

  public MarkupModel getMarkupModel() {
    return getDocument().getMarkupModel(getProject());
  }

  public Sonar getSonar() {
    return SonarUtils.getSonar(getProject());
  }

  /**
   * Removes highlighters with specified key.
   * Should be called before adding new markers to avoid duplicate markers.
   *
   * @param markupModel markup model
   * @param key key
   */
  protected static void removeSonarHighlighters(MarkupModel markupModel, Key<Boolean> key) {
    for (RangeHighlighter rangeHighlighter : markupModel.getAllHighlighters()) {
      Boolean marker = rangeHighlighter.getUserData(key);
      if (marker != null) {
        markupModel.removeHighlighter(rangeHighlighter);
      }
    }
  }
}
