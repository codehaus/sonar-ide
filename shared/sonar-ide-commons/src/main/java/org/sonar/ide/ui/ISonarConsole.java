package org.sonar.ide.ui;

/**
 * Console interface for request, response and error messages.
 *
 * @author Jérémie Lagarde
 */
public interface ISonarConsole {

    public void logRequest(String request);

    public void logResponse(String response);

    public void logError(String error);

    public void logError(String error, Throwable ex);
}
