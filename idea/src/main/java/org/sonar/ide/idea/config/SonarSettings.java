package org.sonar.ide.idea.config;

import org.jetbrains.annotations.NotNull;
import org.sonar.wsclient.Server;

import java.io.*;
import java.util.Properties;

/**
 * @author Evgeny Mandrikov
 */
public class SonarSettings {
  private Server server;
  private boolean onTheFly = false;

  public SonarSettings(Server server) {
    this.server = server;
  }

  public static SonarSettings load(String path) {
    Properties properties = new Properties();
    if (path != null) {
      File file = new File(path);
      if (file.exists()) {
        try {
          properties.load(new FileInputStream(file));
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return new SonarSettings(
        new Server(
            properties.getProperty("host", "http://localhost:9000"),
            properties.getProperty("username"),
            properties.getProperty("password")
        )
    );
  }

  public void save(@NotNull String path) {
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

  public boolean isOnTheFly() {
    return onTheFly;
  }
}
