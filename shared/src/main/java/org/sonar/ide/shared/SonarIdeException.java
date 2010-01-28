package org.sonar.ide.shared;

/**
 * @author Evgeny Mandrikov
 */
public class SonarIdeException extends RuntimeException {
  public SonarIdeException() {
    super();
  }

  public SonarIdeException(String message) {
    super(message);
  }

  public SonarIdeException(String message, Throwable cause) {
    super(message, cause);
  }

  public SonarIdeException(Throwable cause) {
    super(cause);
  }
}
