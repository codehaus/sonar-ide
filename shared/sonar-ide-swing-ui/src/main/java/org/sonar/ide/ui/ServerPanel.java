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
import org.apache.commons.lang.NotImplementedException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Evgeny Mandrikov
 */
public class ServerPanel extends JPanel {
  private final JTextField host;
  private final JTextField username;
  private final JPasswordField password;

  private final JCheckBox useGlobalProxyConfiguration;
  private final JTextField proxyHost;
  private final JTextField proxyPort;
  private final JTextField proxyUsername;
  private final JPasswordField proxyPassword;

  public ServerPanel() {
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    host = new JTextField();
    username = new JTextField();
    password = new JPasswordField();

    useGlobalProxyConfiguration = new JCheckBox("Use global network connections preferences", true);
    proxyHost = new JTextField();
    proxyPort = new JTextField();
    proxyUsername = new JTextField();
    proxyPassword = new JPasswordField();
    // TODO Godin: enable in future
    useGlobalProxyConfiguration.setEnabled(false);
    proxyHost.setEnabled(false);
    proxyPort.setEnabled(false);
    proxyUsername.setEnabled(false);
    proxyPassword.setEnabled(false);

    final JButton testConnectionButton = new JButton("Test connection");
    testConnectionButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        // TODO
        throw new NotImplementedException();
      }
    });

    DefaultFormBuilder formBuilder = new DefaultFormBuilder(new FormLayout(""));
    formBuilder.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    formBuilder.appendColumn("right:pref");
    formBuilder.appendColumn("3dlu");
    formBuilder.appendColumn("fill:p:g");

    formBuilder.append("Sonar server URL:", host);
    formBuilder.append("Username:", username);
    formBuilder.append("Password:", password);

    formBuilder.appendSeparator("Proxy server configuration");
    formBuilder.append("", useGlobalProxyConfiguration);
    formBuilder.append("Host:", proxyHost);
    formBuilder.append("Port:", proxyPort);
//    formBuilder.append("Enable proxy authentication", new JCheckBox());
    formBuilder.append("Username:", proxyUsername);
    formBuilder.append("Password:", proxyPassword);
    formBuilder.append(testConnectionButton);

    add(formBuilder.getPanel());
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

  public boolean isUseGlobalProxyConfiguration() {
    return useGlobalProxyConfiguration.isSelected();
  }

  public void setUseGlobalProxyConfiguration(boolean use) {
    this.useGlobalProxyConfiguration.setSelected(use);
  }

  public String getProxyHost() {
    return proxyHost.getText();
  }

  public void setProxyHost(String proxyHost) {
    this.proxyHost.setText(proxyHost);
  }

  public String getProxyPort() {
    return proxyPort.getText();
  }

  public void setProxyPort(String proxyPort) {
    this.proxyPort.setText(proxyPort);
  }

  public String getProxyUsername() {
    return proxyUsername.getText();
  }

  public void setProxyUsername(String proxyUsername) {
    this.proxyUsername.setText(proxyUsername);
  }

  public String getProxyPassword() {
    return new String(proxyPassword.getPassword());
  }

  public void getProxyPassword(String proxyPassword) {
    this.proxyPassword.setText(proxyPassword);
  }
}
