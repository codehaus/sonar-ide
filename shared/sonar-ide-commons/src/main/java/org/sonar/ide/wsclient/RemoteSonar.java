package org.sonar.ide.wsclient;

import java.util.Collection;

import org.sonar.ide.api.SourceCode;
import org.sonar.ide.api.SourceCodeDiffEngine;
import org.sonar.ide.api.SourceCodeSearchEngine;
import org.sonar.wsclient.Host;

/**
 * @author Evgeny Mandrikov
 * @since 0.2
 */
public class RemoteSonar implements SourceCodeSearchEngine {

  private RemoteSonarIndex index;

  public RemoteSonar(Host host) {
    this(host, new SimpleSourceCodeDiffEngine());
  }

  public RemoteSonar(Host host, SourceCodeDiffEngine diffEngine) {
    index = new RemoteSonarIndex(host, diffEngine);
  }

  public SourceCode search(String key) {
    return index.search(key);
  }

  public Collection<SourceCode> getProjects() {
    return index.getProjects();
  }

}
