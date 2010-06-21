package org.sonar.ide.wsclient;

import java.util.Collection;

import org.sonar.ide.api.SourceCode;
import org.sonar.ide.api.SourceCodeDiffEngine;
import org.sonar.ide.api.SourceCodeSearchEngine;
import org.sonar.wsclient.Host;
import org.sonar.wsclient.Sonar;

/**
 * @author Evgeny Mandrikov
 * @since 0.2
 */
public class RemoteSonar implements SourceCodeSearchEngine {

  private RemoteSonarIndex index;

  /**
   * @deprecated use {@link #RemoteSonar(Host)} instead of it
   */
  @Deprecated
  public RemoteSonar(Sonar sonar) {
    index = new RemoteSonarIndex(sonar, new SimpleSourceCodeDiffEngine());
  }

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
