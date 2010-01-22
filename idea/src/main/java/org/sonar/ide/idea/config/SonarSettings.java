package org.sonar.ide.idea.config;

import org.sonar.wsclient.Server;

import java.io.*;
import java.util.Properties;

/**
 * @author Evgeny Mandrikov
 */
public class SonarSettings {
  private Server server;
  private String projectKey;

  public SonarSettings(Server server, String projectKey) {
    this.server = server;
    this.projectKey = projectKey;
  }

  public static SonarSettings load(String path) {
    File file = new File(path);
    Properties properties = new Properties();
    if (path != null && file.exists()) {
      try {
        properties.load(new FileInputStream(file));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return new SonarSettings(
        new Server(
            properties.getProperty("host", "http://localhost:9000"),
            properties.getProperty("username"),
            properties.getProperty("password")
        ),
        properties.getProperty("project", "org.codehaus.sonar-ide:test-project")
    );
  }

  public void save(String path) {
    Properties properties = new Properties();
    if (server.getHost() != null) {
      properties.setProperty("host", server.getHost());
    }
    if (server.getUsername() != null) {
      properties.setProperty("username", server.getUsername());
    }
    if (server.getPassword() != null) {
      properties.setProperty("password", server.getPassword());
    }
    try {
      properties.store(new FileOutputStream(path), "Sonar Settings");
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public Server getServer() {
    return server;
  }

  public String getProjectKey() {
    return projectKey;
  }
}
