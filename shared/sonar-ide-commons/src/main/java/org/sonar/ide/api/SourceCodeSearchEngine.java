package org.sonar.ide.api;

import java.util.Collection;

/**
 * @author Evgeny Mandrikov
 * @since 0.2
 */
public interface SourceCodeSearchEngine {

  /**
   * @return null, if not found
   */
  SourceCode search(String key);

  Collection<SourceCode> getProjects();

}
