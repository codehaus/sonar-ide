package org.sonar.ide.api;

/**
 * Runtime exception.
 * Good practice is to always specify detail message for exception,
 * so there is no constructors without parameter "message".
 *
 * @author Evgeny Mandrikov
 */
public class SonarIdeException extends RuntimeException {
    /**
     * Constructs a new runtime exception with the specified detail message.
     *
     * @param message the detail message
     */
    public SonarIdeException(String message) {
        super(message);
    }

    /**
     * Constructs a new runtime exception with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause   the cause
     */
    public SonarIdeException(String message, Throwable cause) {
        super(message, cause);
    }
}
