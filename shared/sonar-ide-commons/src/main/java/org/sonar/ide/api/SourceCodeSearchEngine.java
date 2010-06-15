package org.sonar.ide.api;

/**
 * @author Evgeny Mandrikov
 * @since 0.2
 */
public interface SourceCodeSearchEngine {

  SourceCode search(String key);

}
