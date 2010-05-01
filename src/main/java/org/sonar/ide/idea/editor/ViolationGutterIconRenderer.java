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

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.PopupStep;
import com.intellij.openapi.ui.popup.util.BaseListPopupStep;
import com.intellij.openapi.util.IconLoader;
import org.jetbrains.annotations.NotNull;
import org.sonar.ide.shared.ViolationUtils;
import org.sonar.ide.ui.IconsUtils;
import org.sonar.wsclient.services.Violation;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Evgeny Mandrikov
 */
public class ViolationGutterIconRenderer extends GutterIconRenderer {
  /**
   * Violations sorted by priority in descending order.
   */
  private final List<Violation> violations;

  private final String description;

  public ViolationGutterIconRenderer(List<Violation> violations) {
    this.violations = ViolationUtils.sortByPriority(violations);
    this.description = getDescription(this.violations);
  }

  @NotNull
  @Override
  public Icon getIcon() {
    return IconLoader.getIcon(IconsUtils.getIconPath(violations.get(0)));
  }

  @Override
  public String getTooltipText() {
    return description;
  }

  @Override
  public AnAction getClickAction() {
    return new AnAction() {
      @Override
      public void actionPerformed(AnActionEvent event) {
        List<String> text = new ArrayList<String>();
        List<Icon> icons = new ArrayList<Icon>();
        for (Violation violation : violations) {
          text.add(violation.getRuleName() + " : " + violation.getMessage());
          icons.add(IconLoader.getIcon(IconsUtils.getPriorityIconPath(violation)));
        }

        JBPopupFactory.getInstance().createListPopup(new BaseListPopupStep<String>("Violations", text, icons) {
          @Override
          public PopupStep onChosen(String selectedValue, boolean finalChoice) {
            // TODO show rule description
            return super.onChosen(selectedValue, finalChoice);
          }
        }).showInBestPositionFor(event.getDataContext());
      }
    };
  }

  protected static String getDescription(List<Violation> violations) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < violations.size(); i++) {
      Violation violation = violations.get(i);
      if (i != 0) {
        sb.append("<hr/>");
      }
      sb.append("<b>").append(violation.getRuleName()).append("</b>");
      sb.append("<br/>").append(violation.getMessage());
    }
    return sb.toString();
  }

  public Color getErrorStripeMarkColor() {
    return IconsUtils.getColor(violations.get(0));
  }
}
