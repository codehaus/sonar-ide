package org.sonar.ide.ui;

import javax.swing.*;

/**
 * @author Evgeny Mandrikov
 */
public final class SwingAppRunner {
  private static final int DEFAULT_WIDTH = 400;
  private static final int DEFAULT_HEIGHT = 400;

  private SwingAppRunner() {
  }

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
}