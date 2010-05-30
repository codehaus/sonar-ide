/*
 * Copyright (C) 2010 Evgeny Mandrikov
 *
 * Sonar-IDE is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * Sonar-IDE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Sonar-IDE; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */

package org.sonar.ide.idea.editor;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.markup.HighlighterLayer;
import com.intellij.openapi.editor.markup.MarkupModel;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.sonar.ide.idea.utils.SonarUtils;
import org.sonar.ide.shared.ViolationUtils;
import org.sonar.ide.shared.ViolationsLoader;
import org.sonar.ide.shared.duplications.Duplication;
import org.sonar.ide.shared.duplications.DuplicationsLoader;
import org.sonar.wsclient.Sonar;
import org.sonar.wsclient.services.Violation;

import java.util.*;

/**
 * Task for loading violations.
 *
 * @author Evgeny Mandrikov
 */
public class ShowViolationsTask extends Task.Backgroundable {
  private static final Key<Boolean> SONAR_DATA_KEY = Key.create("SONAR_DATA_KEY");

  private Document document;
  private String resourceKey;

  public ShowViolationsTask(@Nullable Project project, Document document, String resourceKey) {
    super(project, "Loading violations from Sonar for " + resourceKey);
    this.document = document;
    this.resourceKey = resourceKey;
  }

  @Override
  public void run(@NotNull ProgressIndicator progressIndicator) {
    try {
      Project project = getProject();
      final Sonar sonar = SonarUtils.getSonar(project);
      String text = document.getText();
      // Load violations
      final Collection<Violation> violations = ViolationsLoader.getViolations(sonar, resourceKey, text);
      final Map<Integer, List<Violation>> violationsByLine = ViolationUtils.splitByLines(violations);
      final MarkupModel markupModel = document.getMarkupModel(project);
      // Load duplications
      final Collection<Duplication> duplications = DuplicationsLoader.getDuplications(sonar, resourceKey, text);
      // Add to UI
      UIUtil.invokeLaterIfNeeded(new Runnable() {
        @Override
        public void run() {
          removeSonarHighlighters(document.getMarkupModel(getProject()));
          for (Map.Entry<Integer, List<Violation>> entry : violationsByLine.entrySet()) {
            addViolationsHighlighter(markupModel, entry.getKey() - 1, entry.getValue());
          }
          for (Duplication duplication : duplications) {
            addDuplicationsHighlighter(markupModel, duplication.getStart(), Arrays.asList(duplication));
          }
        }
      });
    } catch (Exception e) {
      // Ignore, because notification about exception is very annoying.
      e.printStackTrace();
    }
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

  protected static void addHighlighter(MarkupModel markupModel, int line, ViolationGutterIconRenderer renderer) {
    RangeHighlighter highlighter = addSonarHighlighter(markupModel, line, HighlighterLayer.ERROR + 1);
    highlighter.setGutterIconRenderer(renderer);
    highlighter.setErrorStripeMarkColor(renderer.getErrorStripeMarkColor());
    highlighter.setErrorStripeTooltip(renderer.getTooltipText());
  }

  /**
   * Adds highlighter for specified duplications.
   *
   * @param markupModel  markup model
   * @param line         numer of line
   * @param duplications duplications
   */
  protected static void addDuplicationsHighlighter(MarkupModel markupModel, int line, List<Duplication> duplications) {
    addHighlighter(markupModel, line, new ViolationGutterIconRenderer(Collections.<Violation>emptyList(), duplications));
  }

  /**
   * Adds highlighter for specified violations.
   *
   * @param markupModel markup model
   * @param line        number of line
   * @param violations  violations
   */
  protected static void addViolationsHighlighter(MarkupModel markupModel, int line, List<Violation> violations) {
    addHighlighter(markupModel, line, new ViolationGutterIconRenderer(violations));
  }

  /**
   * Removes all Sonar highlighters. Should be called before adding new markers to avoid duplicate markers.
   *
   * @param markupModel markup model
   */
  protected static void removeSonarHighlighters(MarkupModel markupModel) {
    for (RangeHighlighter rangeHighlighter : markupModel.getAllHighlighters()) {
      Boolean marker = rangeHighlighter.getUserData(ShowViolationsTask.SONAR_DATA_KEY);
      if (marker != null) {
        markupModel.removeHighlighter(rangeHighlighter);
      }
    }
  }
}
