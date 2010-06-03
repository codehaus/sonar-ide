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

  public CoverageGutterIconRenderer(Color color) {
    this.color = color;
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
