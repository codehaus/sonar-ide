package org.sonar.ide.api;

import java.util.Collection;

/**
 * @author Evgeny Mandrikov
 */
public interface SourceCodeSearchEngine {

  SourceCode search(String key);

}
