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
    setHost(properties.getProperty(SonarProperties.HOST_PROPERTY));
    setUsername(properties.getProperty(SonarProperties.USERNAME_PROPERTY));
    setPassword(properties.getProperty(SonarProperties.PASSWORD_PROPERTY));
  }

  public String getHost() {
    return host.getText();
  }

  public void setHost(String host) {
    this.host.setText(host);
  }

  public String getUsername() {
    return username.getText();
  }

  public void setUsername(String username) {
    this.username.setText(username);
  }

  public String getPassword() {
    return new String(password.getPassword());
  }

  public void setPassword(String password) {
    this.password.setText(password);
  }

  public static void main(String[] args) {
    SwingAppRunner.run(new SonarConfigPanel());
  }
}
