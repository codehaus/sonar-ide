/*
 * Sonar, open source software quality management tool.
 * Copyright (C) 2010 SonarSource
 * mailto:contact AT sonarsource DOT com
 *
 * Sonar is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * Sonar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Sonar; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */

package org.sonar.ide.ui;

import org.sonar.ide.api.Logs;
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
    Logs.INFO.info("Server trips: {}", sonarClient.getServerTrips());
    return viewer;
  }

  private static void createAndShowGUI() {
    //Create and set up the window.
    JFrame frame = new JFrame("SonarIDE Demo");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    //Create and set up the content pane.
//    frame.setContentPane(createContent());
    frame.setContentPane(new SonarConfigPanel());
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

  private Demo() {
  }
}
