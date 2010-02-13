package org.sonar.ide.ui;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import org.sonar.ide.client.SonarClient;

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
    properties.setProperty("host", getHost());
    properties.setProperty("username", getUsername());
    properties.setProperty("password", getPassword());
    return properties;
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
