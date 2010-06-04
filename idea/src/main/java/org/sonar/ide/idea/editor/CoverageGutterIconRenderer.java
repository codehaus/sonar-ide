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

import com.intellij.openapi.editor.markup.GutterIconRenderer;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

/**
 * @author Evgeny Mandrikov
 */
public class CoverageGutterIconRenderer extends GutterIconRenderer {
  private Color color;
  private String tooltip;

  public CoverageGutterIconRenderer(Color color, String tooltip) {
    this.color = color;
    this.tooltip = tooltip;
  }

  @NotNull
  @Override
  public Icon getIcon() {
    return new MyIcon();
  }

  @Override
  public Alignment getAlignment() {
    return Alignment.RIGHT;
  }

  @Override
  public String getTooltipText() {
    return tooltip;
  }

  private class MyIcon implements Icon {
    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
      g.setColor(color);
      g.fillRect(x, y, getIconWidth(), getIconHeight());
    }

    @Override
    public int getIconWidth() {
      return 10;
    }

    @Override
    public int getIconHeight() {
      return 12;
    }
  }
}
