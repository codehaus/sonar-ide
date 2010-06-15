package org.sonar.ide.wsclient;

import org.sonar.ide.api.SourceCode;
import org.sonar.ide.api.SourceCodeDiffEngine;
import org.sonar.ide.api.SourceCodeSearchEngine;
import org.sonar.wsclient.Sonar;

/**
 * @author Evgeny Mandrikov
 * @since 0.2
 */
public class RemoteSonar implements SourceCodeSearchEngine {

  private RemoteSonarIndex index;

  public RemoteSonar(Sonar sonar, SourceCodeDiffEngine diffEngine) {
    index = new RemoteSonarIndex(sonar, diffEngine);
  }

  public SourceCode search(String key) {
    return index.search(key);
  }

}
