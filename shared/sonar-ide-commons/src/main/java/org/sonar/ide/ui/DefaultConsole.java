package org.sonar.ide.ui;

import org.sonar.ide.api.Logs;

/**
 * Console that shows the output of sonar-ws-client requests in logging system.
 *
 * @author Jérémie Lagarde
 */
public class DefaultConsole implements ISonarConsole {

  public void logRequest(String request) {
    Logs.INFO.info(request);
  }

  public void logResponse(String response) {
    Logs.INFO.info(response);
  }

  public void logError(String error) {
    Logs.INFO.error(error);
  }

  public void logError(String error, Throwable ex) {
    Logs.INFO.error(error, ex);
  }

}
