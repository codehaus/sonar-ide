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

package org.sonar.ide.ui;

import javax.swing.*;

/**
 * @author Evgeny Mandrikov
 */
public final class SwingAppRunner {
  private static final int DEFAULT_WIDTH = 400;
  private static final int DEFAULT_HEIGHT = 400;

  public static void run(JComponent component) {
    run(component, "Test Application", DEFAULT_WIDTH, DEFAULT_HEIGHT);
  }

  public static void run(final JComponent component, final String title, final int width, final int height) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        JFrame frame = new JFrame(title);
        frame.setSize(width, height);
        frame.getContentPane().add(component);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
      }
    });
  }

  /**
   * Hide utility-class constructor.
   */
  private SwingAppRunner() {
  }
}
