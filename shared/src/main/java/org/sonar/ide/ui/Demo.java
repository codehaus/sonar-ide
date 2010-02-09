package org.sonar.ide.ui;

import org.sonar.ide.client.SonarClient;

import javax.swing.*;
import java.awt.*;

/**
 * @author Evgeny Mandrikov
 */
public final class Demo {
  private static Container createContent() {
    AbstractIconLoader iconLoader = new DefaultIconLoader();
    SonarClient sonarClient = new SonarClient("http://localhost:9000");
    String resourceKey = "1";
    MeasuresViewer viewer = new MeasuresViewer(sonarClient, iconLoader, resourceKey);
    System.out.println(sonarClient.getServerTrips());
    return viewer;
  }

  private static void createAndShowGUI() {
    //Create and set up the window.
    JFrame frame = new JFrame("SonarIDE Demo");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    //Create and set up the content pane.
    frame.setContentPane(createContent());
    //Display the window.
    frame.pack();
    frame.setVisible(true);
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        createAndShowGUI();
      }
    });
  }
}
