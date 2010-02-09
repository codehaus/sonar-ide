package org.sonar.ide.ui;

import javax.swing.*;
import java.util.Properties;

/**
 * @author Evgeny Mandrikov
 */
public class SonarConfigPanel extends AbstractConfigPanel {
  private JTextField host;
  private JTextField username;
  private JPasswordField password;

  public SonarConfigPanel() {
    super();
    host = new JTextField();
    username = new JTextField();
    password = new JPasswordField();
    add(host);
    add(username);
    add(password);
  }

  @Override
  public boolean isModified() {
    // TODO
    return true;
  }

  public Properties getProperties() {
    Properties properties = new Properties();
    properties.setProperty("host", host.getText());
    properties.setProperty("username", username.getText());
    properties.setProperty("password", new String(password.getPassword()));
    return properties;
  }
}
