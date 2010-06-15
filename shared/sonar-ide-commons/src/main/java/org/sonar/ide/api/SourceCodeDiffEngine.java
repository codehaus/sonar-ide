package org.sonar.ide.api;

/**
 * HIGHLY EXPERIMENTAL!!!
 *
 * @author Evgeny Mandrikov
 * @since 0.2
 */
public interface SourceCodeDiffEngine {

  SourceCodeDiff diff(String local[], String remote[]);

}
