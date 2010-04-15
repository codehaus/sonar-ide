package org.sonar.ide.ui;

/**
 * @author Jérémie Lagarde
 */
public class ConsoleManager {

    private static ConsoleManager instance = null;
    private final static ISonarConsole SILENT_CONOLE = new SilentSonarConole();
    private ISonarConsole console;

    protected ConsoleManager() {
        instance = this;
    }

    public static ISonarConsole getConsole() {
        if (instance == null)
            return SILENT_CONOLE;

        return instance.create();
    }

    protected ISonarConsole create() {
        if (console == null)
            console = new DefaultConsole();
        return console;
    }

    static private class SilentSonarConole implements ISonarConsole {

        public void logError(String error) {
        }

        public void logRequest(String request) {
        }

        public void logResponse(String response) {
        }

        public void logError(String error, Throwable ex) {
        }

    }
}
