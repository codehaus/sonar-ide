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

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import org.sonar.ide.client.SonarClient;
import org.sonar.ide.shared.SonarProperties;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;

/**
 * @author Evgeny Mandrikov
 */
public class SonarConfigPanel extends AbstractConfigPanel {
  private final JTextField host;
  private final JTextField username;
  private final JPasswordField password;

  public SonarConfigPanel() {
    super();

    host = new JTextField();
    username = new JTextField();
    password = new JPasswordField();
    JButton testConnection = new JButton("Test connection");
    testConnection.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        SonarClient sonar = new SonarClient(getHost(), getUsername(), getPassword());
        if (!sonar.isAvailable()) {
          JOptionPane.showMessageDialog(
              SonarConfigPanel.this,
              "Unable to connect",
              "Test Connection",
              JOptionPane.ERROR_MESSAGE
          );
        } else {
          JOptionPane.showMessageDialog(
              SonarConfigPanel.this,
              "Succesfully connected",
              "Test Connection",
              JOptionPane.INFORMATION_MESSAGE
          );
        }
      }
    });

    DefaultFormBuilder formBuilder = new DefaultFormBuilder(new FormLayout(""));
    formBuilder.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    formBuilder.appendColumn("right:pref");
    formBuilder.appendColumn("3dlu");
    formBuilder.appendColumn("fill:p:g");
    formBuilder.appendColumn("3dlu");
    formBuilder.appendColumn("pref");
    formBuilder.append("Host:", host);
    formBuilder.append(testConnection);
    formBuilder.append("Username:", username);
    formBuilder.nextRow();
    formBuilder.append("Password:", password);

    add(formBuilder.getPanel());
  }

  @Override
  public boolean isModified() {
    // TODO
    return true;
  }

  public Properties getProperties() {
    Properties properties = new Properties();
    properties.setProperty(SonarProperties.HOST_PROPERTY, getHost());
    properties.setProperty(SonarProperties.USERNAME_PROPERTY, getUsername());
    properties.setProperty(SonarProperties.PASSWORD_PROPERTY, getPassword());
    return properties;
  }

  public void setProperties(Properties properties) {
    host.setText(properties.getProperty(SonarProperties.HOST_PROPERTY));
    username.setText(properties.getProperty(SonarProperties.USERNAME_PROPERTY));
    password.setText(properties.getProperty(SonarProperties.PASSWORD_PROPERTY));
  }

  private String getHost() {
    return host.getText();
  }

  private String getUsername() {
    return username.getText();
  }

  private String getPassword() {
    return new String(password.getPassword());
  }
}
