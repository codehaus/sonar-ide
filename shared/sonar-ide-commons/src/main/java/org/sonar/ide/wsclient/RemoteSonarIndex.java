package org.sonar.ide.wsclient;

import java.util.ArrayList;
import java.util.Collection;

import org.sonar.ide.api.SourceCode;
import org.sonar.ide.api.SourceCodeDiffEngine;
import org.sonar.ide.api.SourceCodeSearchEngine;
import org.sonar.wsclient.Host;
import org.sonar.wsclient.Sonar;
import org.sonar.wsclient.connectors.ConnectorFactory;
import org.sonar.wsclient.services.Resource;
import org.sonar.wsclient.services.ResourceQuery;

/**
 * EXPERIMENTAL!!!
 * Layer between Sonar IDE and Sonar based on sonar-ws-client :
 * Sonar IDE -> RemoteSonarIndex -> sonar-ws-client -> Sonar
 * 
 * @author Evgeny Mandrikov
 * @since 0.2
 */
class RemoteSonarIndex implements SourceCodeSearchEngine {

  private final Host host;
  private final Sonar sonar;
  private final SourceCodeDiffEngine diffEngine;

  /**
   * @deprecated use {@link #RemoteSonarIndex(Host, Sonar, SourceCodeDiffEngine)} instead of it
   */
  @Deprecated
  public RemoteSonarIndex(Sonar sonar, SourceCodeDiffEngine diffEngine) {
    this(null, sonar, diffEngine);
  }

  public RemoteSonarIndex(Host host, SourceCodeDiffEngine diffEngine) {
    this(host, new Sonar(ConnectorFactory.create(host)), diffEngine);
  }

  private RemoteSonarIndex(Host host, Sonar sonar, SourceCodeDiffEngine diffEngine) {
    this.sonar = sonar;
    this.diffEngine = diffEngine;
    this.host = host;
  }

  public SourceCode search(String key) {
    Resource resource = sonar.find(new ResourceQuery().setResourceKeyOrId(key));
    if (resource == null) {
      return null;
    }
    return new RemoteSourceCode(key).setRemoteSonarIndex(this);
  }

  public Collection<SourceCode> getProjects() {
    ArrayList<SourceCode> result = new ArrayList<SourceCode>();
    for (Resource resource : sonar.findAll(new ResourceQuery())) {
      result.add(new RemoteSourceCode(resource.getKey(), resource.getName()).setRemoteSonarIndex(this));
    }
    return result;
  }

  protected Host getServer() {
    return host;
  }

  protected Sonar getSonar() {
    return sonar;
  }

  protected SourceCodeDiffEngine getDiffEngine() {
    return diffEngine;
  }

}
